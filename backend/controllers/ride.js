
const db = require('../util/db');
const respond = require('../util/respond');

/**
 * @api {get} /ride list rides
 * @apiName RideGet
 * @apiGroup ride
 * 
 * @apiSuccess (200) {Object[]} data list of ride profiles
 *
 * @apiSuccessExample Success-Response:
 * HTTP/1.1 200 OK
{
    "error": false,
    "data": [
        {
            "id": 10,
            "driver_id": 2,
            "departure_time": "2020-04-20T16:00:00.000Z",
            "location": "troy",
            "active": 1,
            "car": "2010 ford fusion",
            "name": "darpan",
            "phone_number": "1412122234",
            "access_id": "ab1234"
        },
        {
            "id": 11,
            "driver_id": 2,
            "departure_time": "2020-05-20T04:00:00.000Z",
            "location": "troy",
            "active": 1,
            "car": "2010 ford fusion",
            "name": "darpan",
            "phone_number": "1412122234",
            "access_id": "ab1234"
        }
    ]
}
 *
 * @apiError (Error 5xx) {String} 500 Internal Error: {error message}
 *
 */
module.exports.get = (req, res) => {
  const sql = 'select ride.id, driver.id as driver_id, ride.departure_time, ride.location, ride.active, driver.car, driver.name, driver.phone_number, driver.access_id from ride inner join (select driver.id as id, car, name, phone_number, access_id from driver inner join user on driver.user_id = user.id) as driver on ride.driver_id = driver.id where ride.active = 1';

  db.query(sql)
    .then(rows => {
      respond(200, rows, res);
    })
    .catch(err => {
      respond(500, err, res);
    });
};

/**
 * @api {get} /ride:id get ride by ride ID
 * @apiName RideGetById
 * @apiGroup ride
 * 
 * @apiParam {Number} id specific ride id
 * 
 * @apiSuccess (200) {Object} data ride profile
 * @apiSuccess (204) {Null} blank No Content
 *
 * @apiSuccessExample Success-Response:
 * HTTP/1.1 200 OK
{
    "error": false,
    "data": {
        "ride": {
            "id": 10,
            "driver_id": 2,
            "departure_time": "2020-04-20T16:00:00.000Z",
            "location": "troy",
            "active": 1,
            "car": "2010 ford fusion",
            "name": "darpan",
            "phone_number": "1412122234",
            "access_id": "ab1234"
        },
        "users": [
            {
                "id": 8,
                "access_id": "aa5555"
            }
        ]
    }
}
 *
 * @apiError (Error 5xx) {String} 500 Internal Error: {error message}
 *
 */
module.exports.getById = (req, res) => {
  const rideId = req.params.id;

  const sql = 'select ride.id, driver.id as driver_id, ride.departure_time, ride.location, ride.active, driver.car, driver.name, driver.phone_number, driver.access_id from ride inner join (select driver.id as id, car, name, phone_number, access_id from driver inner join user on driver.user_id = user.id) as driver on ride.driver_id = driver.id where ride.active = 1 && ride.id = ? limit 1';

  let rideData;
  db.query('START TRANSACTION')
    .then(() => db.query(sql, [rideId]))
    .then(rows => {
      rideData = rows[0];
      return db.query('select user.id, access_id from ride_user_join inner join user on ride_user_join.user_id = user.id where ride_user_join.ride_id = ?', [rideId]);
    })
    .then(rows => {
      respond(200, {
        ride: rideData,
        users: rows,
      }, res);
      db.query('COMMIT');
    })
    .catch(err => {
      respond(500, err, res);
      db.query('ROLLBACK');
    });
};


/**
 * @api {post} /ride create ride
 * @apiName RidePost
 * @apiGroup ride
 *
 * @apiParam {String} driver access ID of driver
 * @apiParam {Date} departure_time date and time of departure in format "YYYY-MM-DD hh:mm:ss"
 * @apiParam {String} location departure location
 * 
 * @apiParamExample {json} Request-Example:
{
	"driver": "ab1234",
	"departure_time": "2020-05-20",
	"location":"troy"
}
 *
 * @apiSuccessExample Success-Response:
 * HTTP/1.1 200 OK
{
    "error": false,
    "data": {
        "fieldCount": 0,
        "affectedRows": 1,
        "insertId": 8,
        "serverStatus": 2,
        "warningCount": 0,
        "message": "",
        "protocol41": true,
        "changedRows": 0
    }
}
 *
 * @apiError (Error 4xx) {String} 400 Bad Request: "departure time is invalid. please supply a valid departure time."
 * @apiError (Error 5xx) {String} 500 Internal Error: {error message}
 *
 */
module.exports.post = (req, res) => {
  let sql;
  const driverId = req.body.driver;
  const departureTime = req.body.departure_time;
  const { location } = req.body;

  try {
    const date = new Date(departureTime);
    console.log(date);

    if (!(date instanceof Date && !isNaN(date)) || date < new Date()) {
      throw new Error();
    }
  } catch (err) {
    respond(400, `${departureTime} is invalid. please supply a valid departure time.`, res);
    return;
  }

  if (location === '' || location === undefined) {
    respond(400, 'please supply a valid location.', res);
    return;
  }
  db.query('START TRANSACTION')
    .then(() => {
      sql = 'SELECT driver.id FROM driver INNER JOIN user where user.access_id=?';
      return db.query(sql, [driverId]);
    })
    .then(rows => {
      if (rows.length === 0) {
        const errString = `driver with access id ${driverId} not found`;
        respond(400, errString, res);
        throw errString;
      }
      sql = 'INSERT INTO ride (driver_id, departure_time, location) VALUES (?, ?, ?)';
      return db.query(sql, [rows[0].id, departureTime, req.body.location]);
    })
    .then(rows => {
      db.query('COMMIT');
      respond(200, rows, res);
    })
    .catch(err => {
      db.query('ROLLBACK')
        .then(() => {
          respond(500, err, res);
        });
    });
};

/**
 * @api {put} /ride/:id add user to ride
 * @apiName RidePut
 * @apiGroup ride
 * 
 * @apiParam {String} id specific ride id
 * @apiParam {String[]} users list of users' access IDs
 * @apiParam {Boolean} whether to add or remove these users from the ride. Must be true or false
 * 
 *  * @apiParamExample {json} Request-Example:
{
  "users": ["aa5555", "bb6666"],
  "active": true
}
 *
 * @apiSuccessExample Success-Response:
 * HTTP/1.1 200 OK
{
    "error": false,
    "data": {
        "fieldCount": 0,
        "affectedRows": 2,
        "insertId": 0,
        "serverStatus": 2,
        "warningCount": 0,
        "message": "",
        "protocol41": true,
        "changedRows": 0
    }
}
 *
 * @apiError (Error 5xx) {String} 500 Internal Error: {error message}
 *
 */
module.exports.put = (req, res) => {
  const rideId = req.params.id;
  const { active } = req.body;
  const { users } = req.body;

  if (!rideId) {
    respond(400, 'please provide valid access id.', res);
    return;
  }

  if (active !== undefined && typeof active !== "boolean") {
    respond(400, 'active must be true or false.', res);
    return;
  }

  db.query('START TRANSACTION')
    .then(() => {
      return db.query('SELECT id FROM ride WHERE id = ?', rideId);
    })
    .then(rows => {
      if (!rows.length) {
        const e = new Error('ride does not exist');
        e.statusCode = 400;
        throw e;
      }

      if (users === undefined || !users.length) {
        return null;
      }
      sql = 'SELECT id FROM user WHERE access_id in (?)';
      return db.query(sql, [users])
    })
    .then(rows => {

      if (rows === null) return;

      let values = [];
      for (userId of rows.map(r => r.id)) {
        values.push([rideId, userId, active]);
      }

      const sql = 'INSERT INTO ride_user_join (ride_id, user_id, active) VALUES ? ON DUPLICATE KEY UPDATE active = VALUES(active)';
      return db.query(sql, [values]);
    })
    .then(rows => {
      respond(200, rows, res);
      db.query('COMMIT')
    })
    .catch(err => {
      respond(err.statusCode || 500, err, res);
      db.query('ROLLBACK')
    });
};

/**
 * @api {delete} /ride/:id deactivate ride
 * @apiName RideDelete
 * @apiGroup ride
 * 
 * @apiParam {String} id specific ride id
 *
 * @apiSuccessExample Success-Response:
 * HTTP/1.1 200 OK
{
    "error": false,
    "data": {
        "fieldCount": 0,
        "affectedRows": 2,
        "insertId": 0,
        "serverStatus": 2,
        "warningCount": 0,
        "message": "",
        "protocol41": true,
        "changedRows": 0
    }
}
 *
 * @apiError (Error 5xx) {String} 500 Internal Error: {error message}
 *
 */
module.exports.delete = (req, res) => {
  const rideId = req.params.id;

  db.query('START TRANSACTION')
    .then(() => {
      return db.query('SELECT id FROM ride WHERE id = ?', rideId);
    })
    .then(rows => {
      if (!rows.length) {
        const e = new Error('ride does not exist');
        e.statusCode = 400;
        throw e;
      }

      const sql = 'UPDATE ride SET active = 0 WHERE id = ?';
      return db.query(sql, rideId);
    })
    .then(rows => {
      const sql = 'UPDATE ride_user_join SET active = 0 WHERE ride_id = ?';
      return db.query(sql, rideId);
    })
    .then(rows => {
      respond(200, rows, res);
      db.query('COMMIT')
    })
    .catch(err => {
      respond(err.statusCode || 500, err, res);
      db.query('ROLLBACK')
    });
}