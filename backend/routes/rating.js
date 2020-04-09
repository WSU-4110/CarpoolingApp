const express = require('express');

const router = express.Router();
const controller = require('../controllers/rating');

router.get('/', controller.get);
router.get('/:accessId', controller.get);
router.post('/:accessId', controller.post);

module.exports = router;
