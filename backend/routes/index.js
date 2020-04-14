const express = require('express');

const router = express.Router();
const user = require('./user');
const driver = require('./driver');
const ride = require('./ride');
const rating = require('./rating');
const firebase = require('./firebase');

// index
router.get('/', (req, res) => {
  res.send(`docs: ${req.protocol}://${req.get('host')}${req.originalUrl}docs`);
});

router.use('/user', user);
router.use('/ride', ride);
router.use('/driver', driver);
router.use('/rating', rating);
router.use('/firebase', firebase);

module.exports = router;
