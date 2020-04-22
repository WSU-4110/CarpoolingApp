const express = require('express');

const router = express.Router();
const user = require('./user');
const driver = require('./driver');
const ride = require('./ride');
const rating = require('./rating');
const firebase = require('./firebase');
const db = require('../models/index');

// index
router.get('/', (req, res) => {
  res.send(`docs: ${req.protocol}://${req.get('host')}${req.originalUrl}docs`);
});

// clears db
router.delete('/sync', (req, res) => {
  db.sequelize.query('SET FOREIGN_KEY_CHECKS = 0')
    .then(() => db.sequelize.sync({ force: true })).then(() => db.sequelize.query('SET FOREIGN_KEY_CHECKS = 1')).then(() => res.send('OK'))
    .catch((err) => {
      res.json({ isError: true, status: err.message });
    });
});

router.use('/user', user);
router.use('/ride', ride);
router.use('/driver', driver);
router.use('/rating', rating);
router.use('/firebase', firebase);

module.exports = router;
