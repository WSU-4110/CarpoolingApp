const express = require('express');

const router = express.Router();
const controller = require('../controllers/firebase');

router.post('/', controller.post);

module.exports = router;
