const mysql = require('mysql');

const con = mysql.createConnection({
  host: 'dev-warriors-on-wheels.cdfibhkvsv1j.us-east-2.rds.amazonaws.com',
  user: 'admin',
  password: 'Aexohzohr5Eipoo4',
  database: 'warriors_on_wheels',
  insecureAuth: true,
});

con.connect((err) => {
  if (err) throw err;
  console.log('Connected!');
});

module.exports = con;
