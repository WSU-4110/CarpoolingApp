
const config = require('../util/sequelize_config');

module.exports = (sequelize, DataTypes) => {
  const Address = sequelize.define('address', {
    street: DataTypes.STRING,
    city: DataTypes.STRING,
    state: {
      type: DataTypes.STRING,
      validate: {
        is: /^([A-Z]){2}$/i,
      },
    },
    zip: DataTypes.INTEGER,
  }, {
    ...config,
    ...{
      hooks: {
        beforeCreate: async (address) => {
          address.state = address.state.toUpperCase();
        },
      },
    },
  });
  return Address;
};
