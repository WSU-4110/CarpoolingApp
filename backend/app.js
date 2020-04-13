
require('dotenv').config();
const express = require('express');
const bodyParser = require('body-parser');
const router = require('./routes/index');
const models = require('./models');

// const firebase = require('./controllers/firebase');

const app = express();
const port = process.env.port || 8080;

app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());
app.use(router);
app.use('/docs', express.static('apidoc'));
app.delete('/_sync', (req, res) => {
  models._sync()
    .then(() => {
      res.send('OK');
    });
});
app.post('/_fill', (req, res) => {
  models._sync()
    .then(() => {
      return models.User.create({
        "access_id": "cj5100",
        "password": "1234",
        "name": "Evan",
        "phone_number": "0001112222",
        "location": "troy"
      })
    })
    .then(() => {
      return models.User.create({
        "access_id": "ab1234",
        "password": "1234",
        "name": "Sam",
        "phone_number": "1112223333",
        "location": "clawson"
      })
    })
    .then(() => {
      return models.Driver.create({
        "userId": "1",
        "car": "2012 ford fiesta"
      })
    })
    .then(() => {
      res.send('OK');
    })
});

app.listen(port, () => {
  console.log(`listening on port ${port}`);
});
