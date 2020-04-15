
const config = require('../util/sequelize_config');

module.exports = (sequelize, DataTypes) => {
  const Ride = sequelize.define('ride', {
    date: DataTypes.DATE,
    departure_location: DataTypes.STRING,
    arrival_location: { type: DataTypes.STRING, defaultValue: '42 W Warren Ave, Detroit, MI 48202' },
    pending: { type: DataTypes.BOOLEAN, defaultValue: true },
    passenger_count: {
      type: DataTypes.INTEGER,
      validate: {
        isValid(value) {
          if (value < 1) {
            throw new Error('value must be at least 1');
          }
        },
      },
    },
  }, config);

  return Ride;
};
