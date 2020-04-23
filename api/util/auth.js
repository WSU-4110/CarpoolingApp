const jwt = require('./jwt');
const respond = require('./respond');


module.exports = async function auth(req, res, next) {
  try {
    const token = req.headers.authorization;
    res.locals.user = await jwt.verify(token);
    next();
  } catch (err) {
    respond(401, 'Unauthorized', res);
  }
};
