const jwt = require('jsonwebtoken');
const util = require('util');

const jwtSecret = process.env.JWT_SECRET || 'USE A SECRET';

const jwtVerify = util.promisify(jwt.verify);

exports.default = {
  async sign(user) {
    return jwt.sign(user, jwtSecret, {
      expiresIn: '7d',
    });
  },
  async verify(token) {
    return jwtVerify(token, jwtSecret);
  },
  decode: jwt.decode,
};
module.exports = exports.default;
