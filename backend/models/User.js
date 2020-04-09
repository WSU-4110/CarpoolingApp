'use strict';
const config = require('../util/sequelize_config');

module.exports = (sequelize, DataTypes) => {
    const User = sequelize.define('user', {
        name: DataTypes.STRING,
        phone_number: DataTypes.STRING,
        location: DataTypes.STRING,
        access_id: { type: DataTypes.CHAR(6), unique: true }
    }, config);

    return User;
};