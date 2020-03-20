// Api documentation documentation http://apidocjs.com/#params
const db = require('../util/db');
const respond = require('../util/respond');

/**
 * @api {get} /passenger list passengers
 * @apiName PassengerGet
 * @apiGroup passenger
 *
 * @apiSuccessExample Success-Response:
 * HTTP/1.1 200 OK
 * {
 *     "error": false,
 *     "data": [
        {
            "id": 2,
            "name": "darpan",
            "phone_number": "1412122234",
            "location": "vegas baby",
            "access_id": "ab1234"
        }
      ]
 * }
 *
 * @apiError (Error 5xx) {String} 500 Internal Error: {error message}
 *
 */
module.exports.get = (req, res) => {
  const accessId = req.params.accessId;

  let sql = 'SELECT * FROM passenger';
  db.query(sql)
    .then(rows => {
      respond(200, rows, res);
    })
    .catch(err => {
      respond(500, err.toString(), res);
    });
};

/**
 * @api {get} /passenger/:accessId get passenger by access ID
 * @apiName PassengerGetById
 * @apiGroup passenger
 * 
 * @apiParam {String} accessId specific user's access ID
 *
 * @apiSuccessExample Success-Response:
 * HTTP/1.1 200 OK
 * {
 *     "error": false,
 *     "data": {
            "id": 2,
            "name": "darpan",
            "phone_number": "1412122234",
            "location": "vegas baby",
            "access_id": "ab1234"
        }
 * }
 *
 * @apiError (Error 5xx) {String} 500 Internal Error: {error message}
 *
 */
module.exports.getById = (req, res) => {
  const accessId = req.params.accessId;

  let sql = 'SELECT * FROM passenger where access_id = ? limit 1';
  db.query(sql, [accessId])
    .then(rows => {
      respond(200, rows[0], res);
    })
    .catch(err => {
      respond(500, err.toString(), res);
    });

};

/**
 * @api {post} /passenger create passenger
 * @apiName PassengerPost
 * @apiGroup passenger
 * 
 *  * @apiParamExample {json} Request-Example:
 * {
 *     "name":"Test",
 *     "phone_number":"5555555555",
 *     "location":"Atlantis",
 *     "access_id":"ab1234"
 * }
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
 * @apiError (Error 5xx) {String} 500 Internal Error: {error message}
 *
 */
module.exports.post = (req, res) => {
  const passenger = {
    name: req.body.name,
    phoneNumber: req.body.phone_number,
    location: req.body.location,
    accessId: req.body.access_id,
  };

  const values = [
    [passenger.name, passenger.phoneNumber, passenger.location, passenger.accessId],
  ];

  const sql = 'INSERT INTO passenger (name, phone_number, location, access_id) VALUES ? ON DUPLICATE KEY UPDATE name = VALUES(name), phone_number = VALUES(phone_number), location = VALUES(location)';
  db.query(sql, [values])
    .then(rows => {
      respond(200, rows, res);
    })
    .catch(err => {
      respond(500, err.toString(), res);
    });
};

/**
 * @api {put} /passengers update passenger
 * @apiName PassengerPut
 * @apiGroup passenger
 * 
 *  * @apiParamExample {json} Request-Example:
 * {
 *     "name":"Test",
 *     "phone_number":"5555555555",
 *     "location":"Atlantis",
 *     "access_id":"ab1234"
 * }
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
 * @apiError (Error 4xx) {String} 400 Bad Request: "Please provide a valid access id."
 * @apiError (Error 5xx) {String} 500 Internal Error: {error message}
 *
 */
module.exports.put = (req, res) => {
  if (!req.params.access_id) {
    respond(400, 'Please provide a valid access id.', res);
    return;
  }

  const passenger = {
    name: req.body.name,
    phoneNumber: req.body.phone_number,
    location: req.body.location,
    accessId: req.params.access_id,
  };

  let updateRows;
  db.query('START TRANSACTION')
    .then(() => {
      const sql = 'SELECT * FROM passenger WHERE access_id=?';
      return db.query(sql, [passenger.accessId]);
    })
    .then(rows => {
      if (rows.length === 0) {
        const errString = `passenger with access id ${passenger.accessId} not found`;
        respond(400, errString, res);
        throw errString;
      }
      const sql = 'UPDATE passenger SET phone_number = ?, location = ?, name = ? where access_id = ?';
      return db.query(sql, [passenger.phoneNumber, passenger.location,
      passenger.name, passenger.accessId]);
    })
    .then(rows => {
      updateRows = rows;
      return db.query('COMMIT');
    })
    .then(() => {
      respond(200, updateRows, res);
    })
    .catch(err => {
      db.query('ROLLBACK')
        .then(() => {
          respond(500, err, res);
        });
    });
};

module.exports.delete = (req, res) => {


};