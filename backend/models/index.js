
const Sequelize = require('sequelize');
const UserModel = require('./User');
const DriverModel = require('./Driver');
const RideModel = require('./Ride');
const RatingModel = require('./Rating');
const RideEventModel = require('./RideEvent');

let sequelize = new Sequelize(
  process.env.DB_NAME,
  process.env.DB_USER,
  process.env.DB_PASS, {
  host: process.env.DB_HOST,
  dialect: 'mysql',
  pool: {
    min: 0,
    max: 5,
    idle: 10000,
  },
},
);

if (process.env.NODE_ENV === 'development') {
  sequelize = new Sequelize(
    process.env.DB_NAME,
    'root',
    'root', {
    host: '127.0.0.1',
    dialect: 'mysql',
    dialectOptions: {
      typeCast(field, next) { // for reading from database
        if (field.type === 'DATETIME') {
          return field.string();
        }
        return next();
      },
    },
    timezone: 'America/Detroit', // for writing to database
    pool: {
      min: 0,
      max: 5,
      idle: 10000,
    },
  },
  );
}

const _sync = () => module.exports.sequelize.sync({ force: true });

const User = UserModel(sequelize, Sequelize);
const Driver = DriverModel(sequelize, Sequelize);
const Ride = RideModel(sequelize, Sequelize);
const Rating = RatingModel(sequelize, Sequelize);
const RideEvent = RideEventModel(sequelize, Sequelize);

User.hasOne(Driver, { onDelete: 'cascade' });
Driver.belongsTo(User);

const RideUser = sequelize.define('ride_user_join', {}, {
  freezeTableName: true
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
  _sync,
  User,
  Driver,
  Ride,
  Rating,
  RideEvent
};
