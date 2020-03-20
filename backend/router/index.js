const express = require('express');

const router = express.Router();
const user = require('./user');
const driver = require('./driver');
const ride = require('./ride');
const rating = require('./rating');

// index
router.get('/', (req, res) => {
  res.send(`docs: ${req.protocol}://${req.get('host')}${req.originalUrl}docs`);
});

router.use('/user', user);
router.use('/ride', ride);
router.use('/driver', driver);
router.use('/rating', rating);

// rating
router.get('/rating', rating.get);
router.get('/rating/:accessId', rating.get);
router.post('/rating/:accessId', rating.post);

module.exports = router;
