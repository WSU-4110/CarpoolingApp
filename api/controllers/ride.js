const moment = require('moment');
const respond = require('../util/respond');
const models = require('../models/index');
const validate = require('../util/validate');
const jwt = require('../util/jwt');
const firebase = require('./firebase');

/**
 * @api {get} /ride list rides
 * @apiName RideGet
 * @apiGroup ride
 *
 * @apiParam (Query string) {String} start date and time of departure
 * in format "YYYY-MM-DD hh:mm:ss"
 * @apiParam (Query string) {String} end date and time of departure in format "YYYY-MM-DD hh:mm:ss"
 *
 * @apiExample {curl} Example usage:
 *     curl -i http://localhost:8080/ride?start=2020-06-05%2000:00:00&end=2020-06-06%2012:00:00
 *
 * @apiSuccess (200) {Object[]} data list of ride profiles
 *
 * @apiSuccessExample Success-Response:
 * HTTP/1.1 200 OK
{
    "error": false,
    "data": [
        {
            "id": 2,
            "date": "2020-06-06 12:00:00",
            "departure_location": "troy",
            "arrival_location": "wayne",
            "passenger_count": 5,
            "driverId": 1,
            "access_id": "cj5100",
            "car": "2012 ford fiesta"
        }
    ]
}
 *
 * @apiError (Error 5xx) {String} 500 Internal Error: {error message}
 *
 */
module.exports.get = async (req, res) => {
  const q = req.query;
  const fmt = 'YYYY-MM-DD hh:mm:ss';
  const start = moment(`${q.start}`, fmt);
  const end = moment(`${q.end}`, fmt);

  try {
    const rides = await models.Ride.findAll({
      order: [['date', 'ASC']],
      include: {
        model: models.Driver,
        include: {
          model: models.User,
        },
      },
    });
    const list = rides
      .filter((r) => {
        const date = moment(`${r.dataValues.date}`, fmt);
        if (start && date < start) return false;
        if (end && date > end) return false;
        return true;
      })
      .map((ride) => {
        const { driver } = ride.dataValues;
        const { access_id } = driver.dataValues.user.dataValues; // eslint-disable-line camelcase

        delete ride.dataValues.driver;
        return {
          ...ride.dataValues,
          access_id,
          car: driver.dataValues.car,
        };
      });
    respond(200, list, res);
  } catch (err) {
    respond(500, err, res);
  }
};

/**
 * @api {get} /ride/:id get ride by ride ID
 * @apiName RideGetById
 * @apiGroup ride
 *
 * @apiParam {Number} id specific ride id
 *
 * @apiSuccess (200) {Object} data ride profile
 *
 * @apiSuccessExample Success-Response:
 * HTTP/1.1 200 OK
{
    "error": false,
    "data": {
            "id": 11,
            "driver_id": 2,
            "time": "2020-05-20 00:00:00s",
            "location": "troy",
            "active": 1,
            "car": "2010 ford fusion",
            "name": "darpan",
            "phone_number": "1412122234",
            "access_id": "ab1234"
        }
}
 *
 * @apiError (Error 5xx) {String} 500 Internal Error: {error message}
 *
 */
module.exports.getById = async (req, res) => {
  if (
    !validate(
      req.params,
      {
        id: 'integer',
      },
      res,
    )
  ) return;

  try {
    const [ride] = await models.Ride.findAll({
      limit: 1,
      where: {
        id: req.params.id,
      },
      include: {
        model: models.Driver,
        include: {
          model: models.User,
        },
      },
    });

    if (!ride) {
      respond(400, 'ride not found', res);
      return;
    }

    const { driver } = ride.dataValues;
    const { access_id } = driver.dataValues.user.dataValues; // eslint-disable-line camelcase
    delete ride.dataValues.driver;
    const obj = {
      ...ride.dataValues,
      access_id,
      car: driver.dataValues.car,
    };
    respond(200, obj || {}, res);
  } catch (err) {
    respond(500, err, res);
  }
};

/**
 * @api {post} /ride create ride
 * @apiName RidePost
 * @apiGroup ride
 *
 * @apiParam {String} driver access ID of driver
 * @apiParam {String} date date of departure in format "YYYY-MM-DD"
 * @apiParam {String} time time of departure in format "hh:mm:ss"
 * @apiParam {String} departure_location departure location
 * @apiParam {String} arrival_location="wayne" arrival location
 * @apiParam {Number} passenger_count=3 highest number of passengers this ride can take
 *
 * @apiParamExample {json} Request-Example:
{
    "driver": "cj5100",
    "departure_location": "troy",
    "arrival_location": "wayne",
    "passenger_count": 5,
    "date": "2020-06-06",
    "time": "12:00:00"
}
 *
 * @apiSuccessExample Success-Response:
 * HTTP/1.1 200 OK
{
    "error": false,
    "data": {
        "id": 2,
        "driverId": 1,
        "date": "2020-06-06T16:00:00.000Z",
        "departure_location": "troy",
        "arrival_location": "wayne",
        "passenger_count": 5
    }
}
 *
 * @apiError (Error 4xx) {String} 400 Bad Request
 * @apiError (Error 4xx) {String} 403 Forbidden
 * @apiError (Error 5xx) {String} 500 Internal Error: {error message}
 *
 */
module.exports.post = async (req, res) => {
  const b = req.body;
  if (
    !validate(
      b,
      {
        date: 'date',
        time: 'string',
        departure_location: 'string',
        arrival_location: 'string',
        passenger_count: 'integer',
      },
      res,
    )
  ) return;

  if (b.passenger_count < 1) {
    respond(400, 'passenger_count must be greater than 0', res);
    return;
  }

  const decoded = await jwt.decode(req.headers.authorization);

  const [driver] = await models.Driver.findAll({
    include: {
      model: models.User,
      where: {
        access_id: decoded.access_id,
      },
    },
  });

  if (!driver) {
    respond(403, 'Forbidden', res);
    return;
  }

  const fmt = 'YYYY-MM-DD hh:mm:ss';
  const datetime = moment(`${b.date} ${b.time}`, fmt);

  if (!datetime.isValid() || datetime < moment()) {
    respond(400, 'please supply a valid date and time.', res);
    return;
  }

  try {
    const ride = await models.Ride.create({
      driverId: driver.dataValues.id,
      date: datetime,
      departure_location: b.departure_location,
      arrival_location: b.arrival_location,
      passenger_count: b.passenger_count,
    });
    ride.dataValues.date = moment(ride.dataValues.date, fmt);
    respond(200, ride, res);
  } catch (err) {
    respond(500, err, res);
  }
};

/**
 * @api {delete} /ride/:id delete ride
 * @apiName rideDelete
 * @apiGroup ride
 *
 * @apiParam {Integer} id id of ride to delete
 *
 *  @apiSuccess (200) {Object} data successful user update
 *
 * @apiSuccessExample Success-Response:
 * HTTP/1.1 200 OK
{
    "error": false,
    "data": {
        "deleted": 1
    }
}
 * @apiError (Error 4xx) {String} 400 Bad Request: "Please provide a valid access id."
 * @apiError (Error 5xx) {String} 500 Internal Error: {error message}
 *
 */
module.exports.delete = async (req, res) => {
  try {
    const deleted = await models.Ride.destroy({
      where: {
        id: req.params.id,
      },
    });
    respond(200, { deleted }, res);
  } catch (err) {
    respond(500, err, res);
  }
};

/**
 * @api {get} /ride/:id/users get ride's user list
 * @apiName RideUserGet
 * @apiGroup ride
 *
 * @apiParam {String} id specific ride id
 *
 * @apiSuccessExample Success-Response:
 * HTTP/1.1 200 OK
{
    "error": false,
    "data": [
        {
            "id": 2,
            "name": "Sam",
            "phone_number": "1112223333",
            "location": "clawson",
            "access_id": "ab1234",
            "ride_user_join": {
                "createdAt": "2020-04-09 18:07:43",
                "updatedAt": "2020-04-09 18:07:43",
                "userId": 2,
                "rideId": 1
            }
        }
    ]
}
 *
 * @apiError (Error 4xx) {String} 400 Bad Request
 * @apiError (Error 5xx) {String} 500 Internal Error: {error message}
 *
 */
module.exports.rideUsersGet = async (req, res) => {
  if (
    !validate(
      req.params,
      {
        id: 'integer',
      },
      res,
    )
  ) return;

  const rideId = req.params.id;

  try {
    const ride = await models.Ride.findByPk(rideId);
    if (!ride) {
      respond(400, `ride with id ${rideId} not found`, res);
      return;
    }
    const rideUserSelect = await ride.getUsers();
    const resp = rideUserSelect.map((r) => {
      delete r.dataValues.password;
      return r;
    });
    respond(200, resp, res);
  } catch (err) {
    respond(500, err, res);
  }
};

/**
 * @api {post} /ride/:id/users/:userId add user to ride
 * @apiName RideUserPost
 * @apiGroup ride
 *
 * @apiParam {String} id specific ride id
 * @apiParam {String} userId access ID of user to add
 *
 * @apiSuccessExample Success-Response:
 * HTTP/1.1 200 OK
{
    "error": false,
    "data":
        {
            "id": 2,
            "name": "Sam",
            "phone_number": "1112223333",
            "location": "clawson",
            "access_id": "ab1234",
            "ride_user_join": {
                "createdAt": "2020-04-09 18:07:43",
                "updatedAt": "2020-04-09 18:07:43",
                "userId": 2,
                "rideId": 1
            }
        }

}
 *
 * @apiError (Error 4xx) {String} 400 Bad Request
 * @apiError (Error 5xx) {String} 500 Internal Error: {error message}
 *
 */
module.exports.rideUsersPost = async (req, res) => {
  if (
    !validate(
      req.params,
      {
        id: 'integer',
      },
      res,
    )
  ) return;

  const rideId = req.params.id;
  const userAccessId = req.params.userId;

  try {
    const user = await models.User.findOne({
      where: {
        access_id: userAccessId,
      },
    });

    if (!user) {
      respond(400, 'user not found', res);
      return;
    }

    const ride = await models.Ride.findByPk(rideId);
    if (!ride) {
      respond(400, `ride with id ${rideId} not found`, res);
      return;
    }

    const events = await models.RideEvent.findAll({
      where: {
        rideId,
      },
    });

    if (events.length) {
      respond(400, 'Users can no longer join this ride.', res);
      return;
    }

    let driver = await user.getDriver();
    if (driver !== null && driver.dataValues.id === ride.dataValues.driverId) {
      respond(400, 'Driver cannot be a passenger of his/her own ride', res);
      return;
    }

    const passengers = await ride.getUsers();

    if (ride.passenger_count < passengers.length + 1) {
      respond(
        400,
        `Number of users added exceeds maximum passenger count: ${ride.passenger_count}`,
        res,
      );
      return;
    }

    await ride.addUser(user);
    const resp = {};
    delete user.dataValues.password;
    resp.user = user;
    driver = await models.Driver.findByPk(ride.dataValues.driverId, {
      include: models.User,
    });

    const notifications = [];
    let firebaseRequest = {
      body: {
        message: `User ${userAccessId} was added to your ride!`,
        users: [driver.dataValues.user.dataValues.access_id],
      },
    };
    notifications.push(await firebase.post(firebaseRequest, null));
    firebaseRequest = {
      body: {
        message: `You have been added to user ${driver.dataValues.user.dataValues.access_id}'s ride!`,
        users: [userAccessId],
      },
    };
    notifications.push(await firebase.post(firebaseRequest, null));
    resp.notifications = notifications;

    respond(200, resp, res);
  } catch (err) {
    respond(500, err, res);
  }
};

/**
 * @api {delete} /ride/:id/users/:userId delete users from ride
 * @apiName RideUserDelete
 * @apiGroup ride
 *
 * @apiParam {String} id specific ride id
 * @apiParam {String} userId access ID of user to delete from ride
 *
 * @apiDescription removes users if they are on this ride
 *
 * @apiSuccessExample Success-Response:
 * HTTP/1.1 200 OK
{
    "error": false,
    "data": {
      deleted: 1
    }
}
 *
 * @apiError (Error 4xx) {String} 400 Bad Request
 * @apiError (Error 5xx) {String} 500 Internal Error: {error message}
 *
 */
module.exports.rideUsersDelete = async (req, res) => {
  if (
    !validate(
      req.params,
      {
        id: 'integer',
      },
      res,
    )
  ) return;

  const rideId = req.params.id;
  const { userId } = req.params;

  try {
    const user = await models.User.findOne({
      attributes: ['id'],
      where: {
        access_id: userId,
      },
    });

    const ride = await models.Ride.findByPk(rideId);
    if (!ride) {
      respond(400, `ride with id ${rideId} not found`, res);
      return;
    }

    const deleted = await ride.removeUser(user);

    const driver = await models.Driver.findByPk(ride.dataValues.driverId, {
      include: models.User,
    });

    let notification = {};
    if (deleted > 0) {
      const firebaseRequest = {
        body: {
          message: 'A passenger has opted out of your ride.',
          users: [driver.dataValues.user.dataValues.access_id],
        },
      };
      notification = await firebase.post(firebaseRequest, null);
    }

    respond(200, { deleted, notification }, res);
  } catch (err) {
    respond(500, err, res);
  }
};
