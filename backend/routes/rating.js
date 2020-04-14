const express = require('express');
const auth = require('../util/auth');

const router = express.Router();
const controller = require('../controllers/rating');

router.get('/', auth, controller.get);
router.get('/:accessId', auth, controller.get);
router.post('/:accessId', auth, controller.post);

module.exports = router;
