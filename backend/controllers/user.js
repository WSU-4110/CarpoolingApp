// Api documentation documentation http://apidocjs.com/#params
const db = require('../util/db');
const respond = require('../util/respond');

/**
 * @api {get} /user list users
 * @apiName UserGet
 * @apiGroup user
 * 
 * 
 * @apiSuccess (200) {Object[]} list of user profiles
 * @apiSuccess (204) {Null} blank No Content
 *
 * @apiSuccessExample Success-Response:
 * HTTP/1.1 200 OK
 * {
 *     "error": false,
 *     "data": [
        {
            "id": 2,
            "name": "darpan",
            "phone_number": "1412122234",
            "location": "vegas baby",
            "access_id": "ab1234"
        }
      ]
 * }
 *
 * @apiError (Error 5xx) {String} 500 Internal Error: {error message}
 *
 */
module.exports.get = (req, res) => {
  const sql = 'SELECT * FROM user';
  db.query(sql)
    .then(rows => {
      respond(200, rows, res);
    })
    .catch(err => {
      respond(500, err.toString(), res);
    });
};

/**
 * @api {get} /user/:accessId get user by access ID
 * @apiName UserGetById
 * @apiGroup user
 *
 * @apiParam {String} accessId specific user's access ID
 * 
 * @apiSuccess (200) {Object} data user profile
 * @apiSuccess (204) {Null} blank No Content
 *
 * @apiSuccessExample Success-Response:
 * HTTP/1.1 200 OK
 * {
 *     "error": false,
 *     "data": {
            "id": 2,
            "name": "darpan",
            "phone_number": "1412122234",
            "location": "vegas baby",
            "access_id": "ab1234"
        }
 * }
 *
 * @apiError (Error 5xx) {String} 500 Internal Error: {error message}
 *
 */
module.exports.getById = (req, res) => {
  const { accessId } = req.params;

  const sql = 'SELECT * FROM user where access_id = ? limit 1';
  db.query(sql, [accessId])
    .then(rows => {
      if (!rows.length)
        respond(204, null, res);
      else
        respond(200, rows[0], res);
    })
    .catch(err => {
      respond(500, err.toString(), res);
    });
};

/**
 * @api {post} /user create user
 * @apiName UserPost
 * @apiGroup user
 *
 *  * @apiParamExample {json} Request-Example:
 * {
 *     "name":"Test",
 *     "phone_number":"5555555555",
 *     "location":"Atlantis",
 *     "access_id":"ab1234"
 * }
 *
 * 
 *  @apiSuccess (200) {Object} data successful user creation
 * 
 * @apiSuccessExample Success-Response:
 * HTTP/1.1 200 OK
{
    "error": false,
    "data": {
        "fieldCount": 0,
        "affectedRows": 1,
        "insertId": 8,
        "serverStatus": 2,
        "warningCount": 0,
        "message": "",
        "protocol41": true,
        "changedRows": 0
    }
}
 *
 * @apiError (Error 5xx) {String} 500 Internal Error: {error message}
 *
 */
module.exports.post = (req, res) => {
  const user = {
    name: req.body.name,
    phoneNumber: req.body.phone_number,
    location: req.body.location,
    accessId: req.body.access_id,
  };

  const values = [
    [user.name, user.phoneNumber, user.location, user.accessId],
  ];

  const sql = 'INSERT INTO user (name, phone_number, location, access_id) VALUES ? ON DUPLICATE KEY UPDATE name = VALUES(name), phone_number = VALUES(phone_number), location = VALUES(location)';
  db.query(sql, [values])
    .then(rows => {
      respond(200, rows, res);
    })
    .catch(err => {
      respond(500, err.toString(), res);
    });
};

/**
 * @api {put} /users update user
 * @apiName UserPut
 * @apiGroup user
 *
 *  * @apiParamExample {json} Request-Example:
 * {
 *     "name":"Test",
 *     "phone_number":"5555555555",
 *     "location":"Atlantis",
 *     "access_id":"ab1234"
 * }
 * 
 *  @apiSuccess (200) {Object} data successful user update
 *
 * @apiSuccessExample Success-Response:
 * HTTP/1.1 200 OK
{
    "error": false,
    "data": {
        "fieldCount": 0,
        "affectedRows": 1,
        "insertId": 8,
        "serverStatus": 2,
        "warningCount": 0,
        "message": "",
        "protocol41": true,
        "changedRows": 0
    }
}
 * @apiError (Error 4xx) {String} 400 Bad Request: "Please provide a valid access id."
 * @apiError (Error 5xx) {String} 500 Internal Error: {error message}
 *
 */
module.exports.put = (req, res) => {
  if (!req.params.access_id) {
    respond(400, 'Please provide a valid access id.', res);
    return;
  }

  const user = {
    name: req.body.name,
    phoneNumber: req.body.phone_number,
    location: req.body.location,
    accessId: req.params.access_id,
  };

  let updateRows;
  db.query('START TRANSACTION')
    .then(() => {
      const sql = 'SELECT * FROM user WHERE access_id=?';
      return db.query(sql, [user.accessId]);
    })
    .then(rows => {
      if (rows.length === 0) {
        const errString = `user with access id ${user.accessId} not found`;
        respond(400, errString, res);
        throw errString;
      }
      const sql = 'UPDATE user SET phone_number = ?, location = ?, name = ? where access_id = ?';
      return db.query(sql, [user.phoneNumber, user.location,
      user.name, user.accessId]);
    })
    .then(rows => {
      updateRows = rows;
      return db.query('COMMIT');
    })
    .then(() => {
      respond(200, updateRows, res);
    })
    .catch(err => {
      db.query('ROLLBACK')
        .then(() => {
          respond(500, err, res);
        });
    });
};

module.exports.delete = (req, res) => {


};
