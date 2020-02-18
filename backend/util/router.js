const express = require('express');

const router = express.Router();
const passenger = require('../controllers/passenger');

router.get('/passenger', passenger.get);
router.post('/passenger', passenger.post);

module.exports = router;
