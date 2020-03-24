const db = require('../util/db');
const respond = require('../util/respond');

module.exports.get = (req, res) => {
  db.query('SELECT d.id, d.car, p.name, p.access_id, p.phone_number FROM driver as d INNER JOIN passenger as p ON d.passenger_id = p.id')
    .then(rows => {
      respond(200, rows, res);
    })
    .catch(err => {
      respond(500, err, res);
    });
};

module.exports.post = (req, res) => {
  let insertRows; let
    accessId;
  db.query('START TRANSACTION')
    .then(() => {
      accessId = req.body.access_id;
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
