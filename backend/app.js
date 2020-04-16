
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

app.listen(port, () => {
  console.log(`listening on port ${port}`);
});

module.exports = app;
