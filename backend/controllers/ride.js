
const db = require('../util/db');
const respond = require('../util/respond');

module.exports.get = (req, res) => {
  const sql = 'SELECT * FROM ride WHERE active = 1';
  db.query(sql)
    .then(rows => {
      respond(200, rows, res);
    })
    .catch(err => {
      respond(500, err, res);
    });
};

module.exports.post = (req, res) => {
  let sql;
  let rideId;
  db.query('START TRANSACTION')
    .then(() => {
      sql = 'SELECT driver.id FROM driver INNER JOIN passenger where passenger.access_id=?';
      return db.query(sql, [req.body.driver_access_id]);
    })
    .then(rows => {
      sql = 'INSERT INTO ride (driver_id, departure_time, location) VALUES (?, ?, ?)';
      return db.query(sql, [rows[0].id, req.body.departure_time, req.body.location]);
    })
    .then(rows => {
      rideId = rows.insertId;
      if (req.body.passengers) {
        sql = 'SELECT id FROM passenger WHERE access_id in ?';
        const values = [
          req.body.passengers
        ];
        return db.query(sql, [values])
      }
    })
    .then(rows => {
      let values = [];
      rows.forEach(row => {
        values.push([
          rideId,
          row.id
        ]);
      });

      sql = 'INSERT INTO ride_passenger_join (ride_id, passenger_id) VALUES ?';
      return db.query(sql, [values]);
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
}
