const express = require('express');
const app = express();
const bodyParser = require('body-parser');
const db = require('./util/db');
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());
const port = process.env.port || 8080;

app.get('/', (req, res) => {
    res.send('OK');
})

app.get('/passenger', (req, res) => {
    db.query('SELECT * FROM passenger', (err, results, fields) => {
        if (err)
            res.status(500).send(err);
        else
            res.send(results);
    });
})

app.post('/passenger', (req, res) => {

    const passenger = {
        name: req.body.name,
        phoneNumber: req.body.phone_number,
        location: req.body.location,
        accessId: req.body.access_id
    }

    const values = [
        [passenger.name, passenger.phoneNumber, passenger.location, passenger.accessId]
    ]

    db.query('INSERT INTO passenger (name, phone_number, location, access_id) VALUES ? ON DUPLICATE KEY UPDATE name = VALUES(name), phone_number = VALUES(phone_number)', [values], (err, results, fields) => {
        if (err)
            res.status(500).send(err);
        else
            res.send(results);
    });
});

app.listen(port, () => {
    console.log('listening on port ' + port);
})
