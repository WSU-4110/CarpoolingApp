const express = require('express');
const auth = require('../util/auth');

const router = express.Router();
const controller = require('../controllers/user');

router.get('/', auth, controller.get);
router.get('/:accessId', auth, controller.getById);
router.post('/', controller.post);
router.put('/:accessId', controller.put);
router.delete('/:accessId', auth, controller.delete);
router.post('/auth', controller.auth);
router.post('/token', auth, controller.devicePost);

module.exports = router;
