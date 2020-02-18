const db = require('../util/db');

module.exports = {
  get: (req, res) => {
    db.query('SELECT * FROM passenger', (err, results) => {
      if (err) res.status(500).send(err);
      else res.send(results);
    });
  },
  post: (req, res) => {
    const passenger = {
      name: req.body.name,
      phoneNumber: req.body.phone_number,
      location: req.body.location,
      accessId: req.body.access_id,
    };

    const values = [
      [passenger.name, passenger.phoneNumber, passenger.location, passenger.accessId],
    ];

    db.query('INSERT INTO passenger (name, phone_number, location, access_id) VALUES ? ON DUPLICATE KEY UPDATE name = VALUES(name), phone_number = VALUES(phone_number)', [values], (err, results) => {
      if (err) res.status(500).send(err);
      else res.send(results);
    });
  },

};
