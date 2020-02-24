const express = require('express');

const router = express.Router();
const passenger = require('../controllers/passenger');
const driver = require('../controllers/driver');
const ride = require('../controllers/ride');

// passenger
router.get('/passenger', passenger.get);
router.post('/passenger', passenger.post);
router.put('/passenger', passenger.put);
router.delete('/passenger', passenger.delete);

// driver
router.get('/driver', driver.get);
router.post('/driver', driver.post);

// ride 
router.get('/ride', ride.get);
router.post('/ride', ride.post);

module.exports = router;
