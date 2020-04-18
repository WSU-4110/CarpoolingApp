
const Sequelize = require('sequelize');
const UserModel = require('./User');
const DriverModel = require('./Driver');
const RideModel = require('./Ride');
const RatingModel = require('./Rating');
const RideEventModel = require('./RideEvent');
const config = require('../config/config');

let sequelize = new Sequelize(config.development);
if (process.env.NODE_ENV === 'production') {
  sequelize = new Sequelize(config.production);
}

const User = UserModel(sequelize, Sequelize);
const Driver = DriverModel(sequelize, Sequelize);
const Ride = RideModel(sequelize, Sequelize);
const Rating = RatingModel(sequelize, Sequelize);
const RideEvent = RideEventModel(sequelize, Sequelize);

User.hasOne(Driver, { onDelete: 'cascade' });
Driver.belongsTo(User);

const RideUser = sequelize.define('ride_user_join', {}, {
  freezeTableName: true,
});
User.belongsToMany(Ride, { through: RideUser, unique: false });
Ride.belongsToMany(User, { through: RideUser, unique: false });

Driver.hasMany(Ride);
Ride.belongsTo(Driver);

User.hasOne(Rating);
Rating.belongsTo(User);

Ride.hasMany(RideEvent);
RideEvent.belongsTo(Ride);

User.hasMany(RideEvent);
RideEvent.belongsTo(User);

module.exports = {
  sequelize,
  User,
  Driver,
  Ride,
  Rating,
  RideEvent,
};
