const express = require('express');
const auth = require('../util/auth');

const router = express.Router();
const controller = require('../controllers/ride');

router.get('/', auth, controller.get);
router.get('/:id', auth, controller.getById);
router.post('/', auth, controller.post);
router.get('/:id/users', auth, controller.rideUsersGet);
router.post('/:id/users', auth, controller.rideUsersPost);

module.exports = router;
