
const
  db = require('../util/db'),
  respond = require('../util/respond'),
  moment = require('moment');

/**
 * @api {get} /ride list rides
 * @apiName RideGet
 * @apiGroup ride
 * 
 * @apiParam (Query string) {String} start_date date of departure in format "YYYY-MM-DD"
 * @apiParam (Query string) {String} start_time time time of departure in format "hh:mm:ss"
 * @apiParam (Query string) {String} end_date date of departure in format "YYYY-MM-DD"
 * @apiParam (Query string)  {String} end_time time time of departure in format "hh:mm:ss"
 * 
 * @apiExample {curl} Example usage:
 *     curl -i http://localhost:8080/ride?start_date=2020-01-20&start_time=13:00:00&end_date=2020-06-20&end_time=00:00:00
 * 
 * @apiSuccess (200) {Object[]} data list of ride profiles
 *
 * @apiSuccessExample Success-Response:
 * HTTP/1.1 200 OK
{
    "error": false,
    "data": [
        {
            "id": 11,
            "driver_id": 2,
            "time": "2020-05-20 00:00:00s",
            "location": "troy",
            "active": 1,
            "car": "2010 ford fusion",
            "name": "darpan",
            "phone_number": "1412122234",
            "access_id": "ab1234"
        },
        {
            "id": 12,
            "driver_id": 2,
            "time": "2020-05-20 00:00:00s",
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
  let start = "", end = "";
  if (req.query.start_date !== undefined && req.query.start_date.length) {
    start += req.query.start_date;

    if (req.query.start_time !== undefined && req.query.start_time.length) {
      start += " " + req.query.start_time;
    }
    else {
      start += " 00:00:00";
    }
    if (!(moment(start)).isValid() || moment(start) < moment()) {
      respond(400, 'please supply a valid start date and time.', res);
      return;
    }

  }

  if (req.query.end_date !== undefined && req.query.end_date.length) {
    end += req.query.end_date;
    if (req.query.end_time !== undefined && req.query.end_time.length) {
      end += " " + req.query.end_time;
    }
    else {
      end += " 00:00:00";
    }

    if (!(moment(end)).isValid() || moment(end) < moment()) {
      respond(400, 'please supply a valid end date and time.', res);
      return;
    }

  }

  if (start === "") {
    start = 'CURRENT_TIMESTAMP';
  }


  let sql = `select ride.id, driver.id as driver_id, DATE_FORMAT(ride.time, \'%Y-%m-%d %Ts\') as time, ride.departure_location, ride.arrival_location, ride.active, driver.car, driver.name, driver.phone_number, driver.access_id from ride inner join (select driver.id as id, car, name, phone_number, access_id from driver inner join user on driver.user_id = user.id) as driver on ride.driver_id = driver.id where ride.active = 1 && ride.time >= \'${start}\'`;

  if (end.length)
    sql += ` && ride.time <= \'${end}\'`

  console.log(sql);


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
module.exports.getById = (req, res) => {
  const rideId = req.params.id;

  const sql = 'select ride.id, driver.id as driver_id, DATE_FORMAT(ride.time, \'%Y-%m-%d %Ts\') as time, ride.location, ride.active, driver.car, driver.name, driver.phone_number, driver.access_id from ride inner join (select driver.id as id, car, name, phone_number, access_id from driver inner join user on driver.user_id = user.id) as driver on ride.driver_id = driver.id where ride.active = 1 && ride.id = ? limit 1';

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
 * @apiParam {String} date date of departure in format "YYYY-MM-DD"
 * @apiParam {String} time time of departure in format "hh:mm:ss"
 * @apiParam {String} departure_location departure location
 * @apiParam {String} arrival_location="wayne" arrival location
 * @apiParam {Number} passenger_count=3 highest number of passengers this ride can take
 * 
 * @apiParamExample {json} Request-Example:
{
	"driver": "ab1234",
	"date": "2020-05-20",
	"time": "08:00:00",
	"departure_location":"troy",
	"arrival_location":"prentis bldg",
	"passenger_count":"5"
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
  const date = req.body.date;
  const time = req.body.time;
  const departureLocation = req.body.departure_location;
  const arrivalLocation = req.body.arrival_location;
  const numPassengers = req.body.passenger_count;

  const fmt = 'YYYY-MM-DD hh:mm:ss';
  const datetime = moment(`${date} ${time}`, fmt);

  if (!datetime.isValid() || datetime < moment()) {
    respond(400, 'please supply a valid date and time.', res);
    return;
  }

  if (departureLocation === '' || departureLocation === undefined) {
    respond(400, 'please supply a valid departure location.', res);
    return;
  }

  if (arrivalLocation === '' || arrivalLocation === undefined) {
    respond(400, 'please supply a valid arrival location.', res);
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
      sql = 'INSERT INTO ride (driver_id, time, arrival_location, departure_location, passenger_count) VALUES (?, ?, ?, ?, ?)';
      return db.query(sql, [rows[0].id, datetime.format(fmt), arrivalLocation, departureLocation, numPassengers]);
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
 * @apiParam {Boolean} active=true whether to add or remove these users from the ride. Must be true or false
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