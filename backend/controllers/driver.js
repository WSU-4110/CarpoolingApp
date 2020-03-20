const db = require('../util/db');
const respond = require('../util/respond');

/**
 * @api {get} /driver get driver
 * @apiName DriverGet
 * @apiGroup driver
 * 
 * @apiSuccess (200) {Object} data list of driver profiles
 *
 * @apiSuccessExample Success-Response:
 * HTTP/1.1 200 OK
{
    "error": false,
    "data": [
        {
            "id": 2,
            "car": "2010 ford fusion",
            "name": "darpan",
            "access_id": "ab1234",
            "phone_number": "1412122234"
        }
    ]
}
 *
 * @apiError (Error 5xx) {String} 500 Internal Error: {error message}
 *
 */
module.exports.get = (req, res) => {
  db.query('SELECT d.id, d.car, p.name, p.access_id, p.phone_number FROM driver as d INNER JOIN passenger as p ON d.passenger_id = p.id')
    .then(rows => {
      respond(200, rows, res);
    })
    .catch(err => {
      respond(500, err, res);
    });
};

/**
 * @api {get} /driver:accessId get driver by access ID
 * @apiName DriverGetById
 * @apiGroup driver
 * 
 * @apiParam {String} accessId specific user's access ID
 * 
 * @apiSuccess (200) {Object} data list of driver profiles
 * @apiSuccess (204) {Null} blank No Content
 *
 * @apiSuccessExample Success-Response:
 * HTTP/1.1 200 OK
{
    "error": false,
    "data": {
        "id": 2,
        "car": "2010 ford fusion",
        "name": "darpan",
        "access_id": "ab1234",
        "phone_number": "1412122234"
    }
}
 *
 * @apiError (Error 5xx) {String} 500 Internal Error: {error message}
 *
 */
module.exports.getById = (req, res) => {
  const accessId = req.params.accessId;

  db.query('SELECT d.id, d.car, p.name, p.access_id, p.phone_number FROM driver as d INNER JOIN passenger as p ON d.passenger_id = p.id where p.access_id = ? limit 1', [accessId])
    .then(rows => {
      if (!rows.length)
        respond(204, null, res);
      else
        respond(200, rows[0], res);

    })
    .catch(err => {
      respond(500, err, res);
    });
}


/**
 * @api {post} /driver create driver
 * @apiName DriverPost
 * @apiGroup driver
 * 
 * @apiSuccess (200) {Object} data successful driver creation
 *
 * @apiSuccessExample Success-Response:
 * HTTP/1.1 200 OK
{
    "error": false,
    "data": {
        "id": 2,
        "car": "2010 ford fusion",
        "name": "darpan",
        "access_id": "ab1234",
        "phone_number": "1412122234"
    }
}
 *
 * @apiError (Error 5xx) {String} 500 Internal Error: {error message}
 * @apiError (Error 4xx) {String} 400 Bad Request: cannot find user with access id <accessId>
 */
module.exports.post = (req, res) => {
  let accessId = req.body.access_id;

  let insertRows;
  db.query('START TRANSACTION')
    .then(() => {
      const sql = 'SELECT * FROM passenger WHERE access_id=? LIMIT 1';
      return db.query(sql, [accessId]);
    })
    .then(rows => {
      if (!rows.length) {
        return respond(400, `cannot find user with access id ${accessId}`, res);
      }
      return db.query('INSERT INTO driver (passenger_id, car) VALUES (?, ?) ON DUPLICATE KEY UPDATE car = VALUES(car)', [rows[0].id, req.body.car]);
    })
    .then(rows => {
      insertRows = rows;
      return db.query('COMMIT');
    })
    .then(() => respond(200, insertRows, res))
    .catch(err => {
      db.query('ROLLBACK')
        .then(() => respond(500, err, res));
    });
};
