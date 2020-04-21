const express = require('express');
const auth = require('../util/auth');

const router = express.Router();
const rideController = require('../controllers/ride');
const eventController = require('../controllers/event');

// ride
router.get('/', auth, rideController.get);
router.get('/:id', auth, rideController.getById);
router.post('/', auth, rideController.post);
router.delete('/:id', auth, rideController.delete);
router.get('/:id/users', auth, rideController.rideUsersGet);
router.post('/:id/users', auth, rideController.rideUsersPost);

// events
router.get('/:id/events', auth, eventController.get);
router.post('/:id/events', auth, eventController.post);

module.exports = router;
