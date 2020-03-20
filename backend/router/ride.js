const express = require('express');

const router = express.Router();
const controller = require('../controllers/ride');

router.get('/', controller.get);
router.get('/:id', controller.getById);
router.post('/', controller.post);
router.put('/:id', controller.put);

module.exports = router;