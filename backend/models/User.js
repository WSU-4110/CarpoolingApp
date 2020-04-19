
const bcrypt = require('bcrypt');
const config = require('../util/sequelize_config');


module.exports = (sequelize, DataTypes) => {
  const User = sequelize.define('user', {
    access_id: {
      type: DataTypes.CHAR(6),
      unique: true,
      validate: { is: /^([a-z]){2}([0-9]){4}$/ },
    },
    password: DataTypes.STRING,
    name: DataTypes.STRING,
    phone_number: {
      type: DataTypes.STRING,
      validate: {
        is: /^\(?([0-9]{3})\)?[-. ]?([0-9]{3})[-. ]?([0-9]{4})$/,
      },
    },
    device_token: DataTypes.STRING,
  }, {
    ...config,
    ...{
      hooks: {
        beforeCreate: async (user) => {
          const salt = await bcrypt.genSalt(10);
          user.password = await bcrypt.hash(user.password, salt);
        },
      },
    },
  });
  User.prototype.validPassword = async function valid(password) {
    return bcrypt.compare(password, this.password);
  };
  return User;
};
