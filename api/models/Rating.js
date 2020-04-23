
const config = require('../util/sequelize_config');
const UserModel = require('./User');

module.exports = (sequelize, DataTypes) => {
  const Rating = sequelize.define('rating', {
    value: {
      type: DataTypes.INTEGER,
      validate: {
        isValid(value) {
          if (value < 1 || value > 5) {
            throw new Error('value must be between 1 and 5');
          }
        },
      },
    },
    is_driver: DataTypes.BOOLEAN,

  }, config);

  Rating.getAverage = function getAverage(accessId, isDriver) {
    return this.findAll({
      attributes: [
        [sequelize.fn('COUNT', sequelize.col('value')), 'count'],
        [sequelize.fn('AVG', sequelize.col('value')), 'average'],
      ],
      group: ['user_id'],
      where: {
        is_driver: isDriver,
      },
      include: {
        model: UserModel(sequelize, DataTypes),
        where: {
          access_id: accessId,
        },
      },
    });
  };

  return Rating;
};
