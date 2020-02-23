const db = require('../util/db');
const respond = require('../util/respond');

module.exports = {
  get: (req, res) => {
    db.query('SELECT * FROM passenger', (err, results) => {
      if (err) respond(500, err, toString(), res);
      else respond(200, results, res);
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

    db.query('INSERT INTO passenger (name, phone_number, location, access_id) VALUES ? ON DUPLICATE KEY UPDATE name = VALUES(name), phone_number = VALUES(phone_number), location = VALUES(location)', [values], (err, results) => {
      if (err) respond(500, err, toString(), res);
      else respond(200, results, res);
    });
  },
  put: (req, res) => {
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

    db.query('SELECT * FROM passenger WHERE access_id=?', [passenger.accessId], (selectErr, selectResponse) => {
      if (selectErr) res.status(500).send(selectErr);
      else if (selectResponse.length === 0) {
        respond(400, `passenger with access id ${passenger.accessId} not found`, res);
      } else {
        db.query('UPDATE passenger SET phone_number = ?, location = ?, name = ? where access_id = ?', [passenger.phoneNumber, passenger.location, passenger.name, passenger.accessId], (updateErr, updateResponse) => {
          if (updateErr) respond(500, updateErr, res);
          else {
            respond(200, updateResponse, res);
          }
        });
      }
    });
  },
};
