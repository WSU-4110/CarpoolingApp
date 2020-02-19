const express = require('express');

const router = express.Router();
const passenger = require('../controllers/passenger');
const ride = require('../controllers/ride');

// passenger
router.get('/passenger', passenger.get);
router.post('/passenger', passenger.post);
router.put('/passenger', passenger.put);

// ride
router.get('/ride', ride.get);


module.exports = router;
