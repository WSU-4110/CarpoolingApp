
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
  const sql = 'select ride.id, driver.id as driver_id, ride.departure_time, ride.location, ride.active, driver.car, driver.name, driver.phone_number, driver.access_id from ride inner join (select driver.id as id, car, name, phone_number, access_id from driver inner join passenger on driver.passenger_id = passenger.id) as driver on ride.driver_id = driver.id where ride.active = 1';

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
        "passengers": [
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

  const sql = 'select ride.id, driver.id as driver_id, ride.departure_time, ride.location, ride.active, driver.car, driver.name, driver.phone_number, driver.access_id from ride inner join (select driver.id as id, car, name, phone_number, access_id from driver inner join passenger on driver.passenger_id = passenger.id) as driver on ride.driver_id = driver.id where ride.active = 1 && ride.id = ? limit 1';

  let rideData;
  db.query('START TRANSACTION')
    .then(() => db.query(sql, [rideId]))
    .then(rows => {
      rideData = rows[0];
      return db.query('select passenger.id, access_id from ride_passenger_join inner join passenger on ride_passenger_join.passenger_id = passenger.id where ride_passenger_join.ride_id = ?', [rideId]);
    })
    .then(rows => {
      respond(200, {
        ride: rideData,
        passengers: rows,
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
 *  * @apiParamExample {json} Request-Example:
{
	"driver_access_id": "ab1234",
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
  const driverId = req.body.driver_access_id;
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
      sql = 'SELECT driver.id FROM driver INNER JOIN passenger where passenger.access_id=?';
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
 * @api {put} /ride/:id add passenger to ride
 * @apiName RidePut
 * @apiGroup ride
 * 
 * @apiParam {String} id specific ride id
 * @apiParam {String[]} passengers list of passengers' access IDs
 * 
 *  * @apiParamExample {json} Request-Example:
{
	"passengers": ["aa5555", "bb6666"]
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
  for (const passenger of req.body.passengers) {
    sql = 'SELECT id FROM passenger WHERE access_id = ?';
    return db.query(sql, [passenger])
      .then(rows => {
        sql = 'INSERT INTO ride_passenger_join (ride_id, passenger_id) VALUES (?, ?)';
        return db.query(sql, [rideId, rows[0].id]);
      })
      .then(rows => {
        respond(200, rows, res);
      })
      .catch(err => {
        respond(500, err, res);
      });
  }
};
