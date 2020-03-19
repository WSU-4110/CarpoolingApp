// Api documentation documentation http://apidocjs.com/#params
const db = require('../util/db');
const respond = require('../util/respond');

/**
 * @api {get} /passengers /passengers
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
  db.query('SELECT * FROM passenger')
    .then(rows => {
      respond(200, rows, res);
    })
    .catch(err => {
      respond(500, err.toString(), res);
    });
};

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

module.exports.put = (req, res) => {
  if (!req.body.access_id) {
    respond(400, 'Please provide a valid access id.', res);
    return;
  }

  const passenger = {
    name: req.body.name,
    phoneNumber: req.body.phone_number,
    location: req.body.location,
    accessId: req.body.access_id,
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