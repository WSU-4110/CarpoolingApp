'use strict';
const config = require('../util/sequelize_config');

module.exports = (sequelize, DataTypes) => {
    const Rating = sequelize.define('rating', {
        value: {
            type: DataTypes.INTEGER, 
            validate: {
                isValid(value) {
                    if (value < 1 || value > 5) {
                        throw new Error('value must be between 1 and 5');
                    }
                }
            }
        },
        is_driver: DataTypes.BOOLEAN,

    }, config);

    return Rating;
};