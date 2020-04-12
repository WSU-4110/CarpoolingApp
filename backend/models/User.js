
const bcrypt = require('bcrypt');
const config = require('../util/sequelize_config');


module.exports = (sequelize, DataTypes) => {
  const User = sequelize.define('user', {
    name: DataTypes.STRING,
    phone_number: DataTypes.STRING,
    location: DataTypes.STRING,
    password: DataTypes.STRING,
    access_id: { type: DataTypes.CHAR(6), unique: true },
  }, {
    ...config,
    ...{
      hooks: {
        beforeCreate: async (user) => {
          const salt = await bcrypt.genSalt(10); // whatever number you want
          user.password = await bcrypt.hash(user.password, salt);
        },
      },
    },
  });
  User.prototype.validPassword = async function (password) {
    return await bcrypt.compare(password, this.password);
  };

  return User;
};
