const express = require('express');

const router = express.Router();
const controller = require('../controllers/driver');

router.get('/', controller.get);
router.get('/:accessId', controller.getById);
router.post('/', controller.post);

module.exports = router;
