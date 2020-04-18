
const moment = require('moment');
const respond = require('../util/respond');
const models = require('../models/index');
const validate = require('../util/validate');
const jwt = require('../util/jwt');
const firebase = require('./firebase');

const verifyDriver = async (header, rideId, res) => {
  const decoded = await jwt.decode(header);
  const ride = await models.Ride.findByPk(rideId,
    {
      include: models.Driver,
    });

  if (decoded.id !== ride.driver.userId) {
    respond(403, 'Forbidden', res);
    return false;
  }
  return true;
};

/**
 * @api {get} /ride/:id/events get ride events
 * @apiName RideEventGet
 * @apiGroup rideEvent
 *
 * @apiParam {Number} id specific ride id
 *
 * @apiSuccess (200) {Object} data ride profile
 *
 * @apiSuccessExample Success-Response:
 * HTTP/1.1 200 OK
{
    "error": false,
    "data": [
        {
            "id": 14,
            "time": "2020-04-14 10:44:42",
            "type": 0,
            "rideId": 1,
            "userId": null
        },
        {
            "id": 15,
            "time": "2020-04-14 10:44:46",
            "type": 1,
            "rideId": 1,
            "userId": 2
        },
        {
            "id": 16,
            "time": "2020-04-14 10:44:50",
            "type": 2,
            "rideId": 1,
            "userId": null
        }
    ]
}
 *
 * @apiError (Error 5xx) {String} 500 Internal Error: {error message}
 *
 */
module.exports.get = async (req, res) => {
  try {
    if (!(await verifyDriver(req.headers.authorization, req.params.id, res))) return;

    const events = await models.RideEvent.findAll({
      where: {
        rideId: req.params.id,
      },
    });

    respond(200, events, res);
  } catch (err) {
    respond(500, err, res);
  }
};

/**
 * @api {post} /ride/:id/events post ride event
 * @apiName RideEventPost
 * @apiGroup rideEvent
 *
 * @apiDescription This call manages the ride session timeline. Only the driver associated with this ride can create events.
 *
 * Types:
 *
 * 0 - driver wants to begin ride
 *
 * 1 - driver wants to pick up a passenger
 *
 * 2 - driver wants to end ride
 *
 * @apiParam {Number} id specific ride id
 * @apiParam {Number} type type of event. Must be one of: [0, 1, 2]
 * @apiParam {String} access_id access ID of passenger. Provide only if type == 1
 *
 * @apiSuccess (200) {Object} data ride profile
 *
 * @apiSuccessExample Success-Response:
 * HTTP/1.1 200 OK
{
    "error": false,
    "data": [
        {
            "id": 14,
            "time": "2020-04-14 10:44:42",
            "type": 0,
            "rideId": 1,
            "userId": null
        },
        {
            "id": 15,
            "time": "2020-04-14 10:44:46",
            "type": 1,
            "rideId": 1,
            "userId": 2
        },
        {
            "id": 16,
            "time": "2020-04-14 10:44:50",
            "type": 2,
            "rideId": 1,
            "userId": null
        }
    ]
}
 *
 * @apiError (Error 5xx) {String} 500 Internal Error: {error message}
 *
 */
module.exports.post = async (req, res) => {
  // make sure type is provided
  if (!validate(req.body, {
    type: 'integer',
  }, res)) return;

  if (req.body.type < 0 || req.body.type > 2) {
    respond(400, 'invalid event type: possible values are {0, 1, 2}', res);
    return;
  }

  const { type } = req.body;
  const userAccessId = req.body.access_id || null;
  let userDbId = null;
  let beginEvent = [];
  let endEvent = [];
  let user;
  let message = '';
  // begin SQL transaction
  const t = await models.sequelize.transaction();

  try {
    // make sure caller is ride creator
    if (!(await verifyDriver(req.headers.authorization, req.params.id, res))) return;

    const ride = await models.Ride.findByPk(req.params.id);
    if (!ride) {
      await t.rollback();
      respond(400, `ride with id ${req.params.id} not found`, res);
      return;
    }

    // test if ride has already ended
    endEvent = await models.RideEvent.findAll({
      where: {
        rideId: req.params.id,
        type: 2,
      },
    }, { transaction: t });
    if (endEvent.length) {
      await t.rollback();
      respond(400, 'ride has ended', res);
      return;
    }

    // find db id of user
    if (userAccessId) {
      [user] = await models.User.findAll({
        limit: 1,
        where: {
          access_id: userAccessId,
        },
      }, { transaction: t });
      userDbId = user.dataValues.id;
    }

    // get ride's list of passengers
    const passengers = await ride.getUsers();
    const driver = await ride.getDriver({
      include: models.User,
    });
    passengers.push(driver.dataValues.user);

    let pickedUp;
    switch (type) {
    // caller wants to begin ride
    case 0:

      // test if ride has already begun
      beginEvent = await models.RideEvent.findAll({
        where: {
          rideId: req.params.id,
          type: 0,
        },
      }, { transaction: t });
      if (beginEvent.length) {
        await t.rollback();
        respond(400, 'ride has already begun', res);
        return;
      }

      // remove pending tag from ride. Ride is no longer available
      await models.Ride.update({ pending: false }, {
        where: {
          id: req.params.id,
        },
      }, { transaction: t });

      message = `Ride with driver ${driver.dataValues.user.dataValues.access_id} has started!`;
      break;

      // driver has picked up a passenger
    case 1:
      // test if ride has already begun
      beginEvent = await models.RideEvent.findAll({
        where: {
          rideId: req.params.id,
          type: 0,
        },
      }, { transaction: t });
      if (!beginEvent.length) {
        await t.rollback();
        respond(400, 'ride has not begun yet', res);
        return;
      }

      // caller must provide passenger's access id
      if (!user) {
        await t.rollback();
        respond(400, `user with access ID ${req.body.access_id} not found`, res);
        return;
      }

      // passenger must be a part of this ride
      if (passengers.find(p => p.dataValues.access_id === req.body.access_id) === undefined) {
        await t.rollback();
        respond(400, `user with access ID ${req.body.access_id} not part of this ride`, res);
        return;
      }

      [pickedUp] = await models.RideEvent.findAll({
        where: {
          rideId: req.params.id,
          userId: userDbId,
        },
      });
      if (pickedUp) {
        await t.rollback();
        respond(400, `user with access ID ${user.dataValues.access_id} has already been picked up`, res);
        return;
      }

      message = `Driver has picked up passenger ${req.body.access_id}!`;
      break;
      // driver has finished ride
    case 2:
      // test if ride has already begun
      beginEvent = await models.RideEvent.findAll({
        where: {
          rideId: req.params.id,
          type: 0,
        },
      }, { transaction: t });
      if (!beginEvent.length) {
        await t.rollback();
        respond(400, 'ride has not begun yet', res);
        return;
      }

      message = 'You have arrived at your destination. Go Warriors!';
      break;
    default:
      await t.rollback();
      respond(400, 'invalid event type: possible values are {0, 1, 2}', res);
      return;
    }

    // create new event for this ride
    const event = await models.RideEvent.create({
      time: moment(),
      type,
      userId: type === 1 ? userDbId : null,
      rideId: parseInt(req.params.id, 10),
    }, { transaction: t });

    const firebaseRequest = {
      body: {
        message,
        users: passengers.map(p => p.access_id),
      },
    };

    const notification = await firebase.post(firebaseRequest, null);
    event.dataValues.notification = notification;

    await t.commit();
    respond(200, event, res);
  } catch (err) {
    await t.rollback();
    respond(500, err, res);
  }
};
