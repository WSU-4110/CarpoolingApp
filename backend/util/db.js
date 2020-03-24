const mysql = require('mysql');

class Database {
  constructor(config) {
    this.connection = mysql.createConnection(config);
  }

  query(sql, args) {
    return new Promise((resolve, reject) => {
      this.connection.query(sql, args, (err, rows) => {
        if (err) return reject(err);
        return resolve(rows);
      });
    });
  }

  close() {
    return new Promise((resolve, reject) => {
      this.connection.end(err => {
        if (err) return reject(err);
        return resolve();
      });
    });
  }
}

let config;
if (process.env.NODE_ENV === "development") {
  config = {
    host: "127.0.0.1",
    user: "root",
    password: "root",
    database: process.env.DB_NAME,
    insecureAuth: true,
  };
}
else {
  config = {
    host: process.env.DB_HOST,
    user: process.env.DB_USER,
    password: process.env.DB_PASS,
    database: process.env.DB_NAME,
    insecureAuth: true,
  };
}


module.exports = new Database(config);
