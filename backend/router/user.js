const express = require('express');

const router = express.Router();
const controller = require('../controllers/user');

router.get('/', controller.get);
router.get('/:accessId', controller.getById);
router.post('/', controller.post);
router.put('/:accessId', controller.put);
router.delete('/:accessId', controller.delete);

module.exports = router;
