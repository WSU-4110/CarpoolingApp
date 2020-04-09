const express = require('express');
const auth = require('../util/auth');

const router = express.Router();
const controller = require('../controllers/driver');

router.get('/', auth, controller.get);
router.get('/:accessId', auth, controller.getById);
router.post('/', auth, controller.post);
router.delete('/:accessId', auth, controller.delete);

module.exports = router;
