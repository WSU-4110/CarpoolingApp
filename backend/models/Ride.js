'use strict';
const config = require('../util/sequelize_config');

module.exports = (sequelize, DataTypes) => {
    const Ride = sequelize.define('ride', {
        departure_location: DataTypes.STRING,
        arrival_location: DataTypes.STRING,
        passenger_count: DataTypes.INTEGER,
        active: DataTypes.BOOLEAN
    }, config);

    Ride.associate = function(models){
        models.Ride.hasOne
    }

    return Ride;
};