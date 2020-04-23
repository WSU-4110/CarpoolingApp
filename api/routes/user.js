const express = require('express');
const auth = require('../util/auth');

const router = express.Router();
const controller = require('../controllers/user');
const models = require('../models/index');

router.get('/', auth, controller.get(models));
router.get('/:accessId', auth, controller.getById(models));
router.post('/', controller.post(models));
router.put('/:accessId', controller.put(models));
router.delete('/', auth, controller.delete(models));
router.post('/auth', controller.auth(models));
router.post('/token', auth, controller.devicePost(models));

module.exports = router;
