const models = require('../models/index');
const respond = require('./respond');
const jwt = require('./jwt');

module.exports = async (req, res, next) => {
  const decoded = await jwt.decode(req.headers.authorization);
  const drivers = await models.Driver.findAll({
    include: {
      model: models.User,
      where: {
        access_id: decoded.access_id,
      },
    },
  });
  if (!drivers.length) respond(403, 'caller must be driver', res);
  else next();
};
