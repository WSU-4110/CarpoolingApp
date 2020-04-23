require('dotenv').config();

module.exports = {
  development: {
    username: 'root',
    password: 'root',
    database: 'warriors_on_wheels',
    host: '127.0.0.1',
    dialect: 'mysql',
  },
  production: {
    username: process.env.DB_USER,
    password: process.env.DB_PASS,
    database: process.env.DB_NAME,
    host: process.env.DB_HOST,
    dialect: 'mysql',
  },
};
