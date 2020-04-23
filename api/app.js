
require('dotenv').config();
const express = require('express');
const bodyParser = require('body-parser');
const router = require('./routes/index');

const app = express();
const port = process.env.port || 8080;

app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());
app.use(router);
app.use('/docs', express.static('apidoc'));
app.listen(port, () => {
  console.log(`listening on port ${port}`);
});

module.exports = app;
