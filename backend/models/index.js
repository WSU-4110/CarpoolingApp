'use strict';

const Sequelize = require('sequelize');
const UserModel = require('./User');
const DriverModel = require('./Driver');
const RideModel = require('./Ride');
const RatingModel = require('./Rating');

let sequelize = new Sequelize(
    process.env.DB_NAME,
    process.env.DB_USER,
    process.env.DB_PASS, {
    host: process.env.DB_HOST,
    dialect: 'mysql',
    pool: {
        min: 0,
        max: 5,
        idle: 10000
    }
});

if (process.env.NODE_ENV === "development") {

    sequelize = new Sequelize(
        process.env.DB_NAME,
        'root',
        'root', {
        host: '127.0.0.1',
        dialect: 'mysql',
        pool: {
            min: 0,
            max: 5,
            idle: 10000
        }
    });
}

const User = UserModel(sequelize, Sequelize);
const Driver = DriverModel(sequelize, Sequelize);
const Ride = RideModel(sequelize, Sequelize);
const Rating = RatingModel(sequelize, Sequelize);

const RideUser = sequelize.define('ride_user_join', {});
User.hasOne(Driver);
User.belongsToMany(Ride, { through: RideUser, unique: false });
User.hasOne(Rating);

sequelize.sync({ force: true }).then(() => {
    console.log(`Database & tables created!`)
})
// fs
//     .readdirSync(__dirname)
//     .filter(file => {
//         return (file.indexOf('.') !== 0) && (file !== basename) && (file.slice(-3) === '.js');
//     })
//     .forEach(file => {
//         var model = sequelize['import'](path.join(__dirname, file));
//         db[model.name] = model;
//     });

module.exports = {
    User,
    Driver,
    Ride,
};