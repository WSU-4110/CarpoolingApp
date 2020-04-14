
const config = require('../util/sequelize_config');

module.exports = (sequelize, DataTypes) => {
  const Driver = sequelize.define('driver', {
    car: DataTypes.STRING,
  }, config);

  Driver.associate = function (models) {
    models.Driver.hasOne(models.User);
  };

  return Driver;
};
