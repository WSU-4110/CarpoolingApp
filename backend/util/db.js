const mysql = require('mysql');

const con = mysql.createConnection({
  host: '127.0.0.1',
  user: 'root',
  password: 'root',
  database: 'warriors_on_wheels',
  insecureAuth : true
});

con.connect(function(err) {
  if (err) throw err;
  console.log('Connected!');
});

module.exports = con;