const jwt = require('jsonwebtoken');
const util = require('util');

const jwt_secret = process.env.JWT_SECRET || 'USE A SECRET';

const jwtVerify = util.promisify(jwt.verify);

exports.default = {
  async sign(user) {
    return jwt.sign(user, jwt_secret, {
      expiresIn: '72h',
    });
  },
  async verify(token) {
    return jwtVerify(token, jwt_secret);
  },
  decode: jwt.decode,
};
module.exports = exports.default;
