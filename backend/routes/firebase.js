const express = require('express');
const respond = require('../util/respond');
const validate = require('../util/validate');

const router = express.Router();
const controller = require('../controllers/firebase');

router.post('/', async (req, res) => {
  const b = req.body;
  if (!validate(b, {
    users: 'array',
    message: 'string'
  }, res)) return;

  try {
    const notify = await controller.post(req, res);
    respond(200, notify, res);
  }
  catch (err) {
    respond(500, err, res);
  }
});

module.exports = router;
