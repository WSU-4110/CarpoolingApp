
const config = require('../util/sequelize_config');

module.exports = (sequelize, DataTypes) => {
  const RideEvent = sequelize.define('ride_event', {
    time: DataTypes.DATE,
    type: DataTypes.INTEGER,
  }, config);

  return RideEvent;
};
