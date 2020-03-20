const db = require('../util/db');
const respond = require('../util/respond');

/**
 * @api {get} /rating/:accessId get rating
 * @apiName RatingGet
 * @apiGroup rating
 * 
 * @apiSuccess (200) {Number} user_id id of user
 * @apiSuccess (200) {Number} average average user rating
 * @apiSuccess (200) {Number} count number of ratings for this user
 *
 * @apiSuccessExample Success-Response:
 * HTTP/1.1 200 OK
{
    "error": false,
    "data": {
        "user_id": 2,
        "average": 2.2,
        "count": 15
    }
}
 *
 * @apiError (Error 5xx) {String} 500 Internal Error: {error message}
 *
 */
module.exports.get = (req, res) => {
  const userId = req.params.accessId;
  if (!userId) {
    respond(400, 'please provide valid access id.', res);
    return;
  }


  sql = 'SELECT rating.user_id AS user_id, IFNULL(AVG(rating.value), 0) AS average, COUNT(rating.value) AS count FROM user LEFT JOIN rating ON user.id=rating.user_id WHERE user.access_id = ?;';
  db.query(sql, userId)
    .then(rows => {
      respond(200, rows[0], res);
    })
    .catch(err => {
      respond(500, err, res);
    });
};

/**
 * @api {post} /rating/:accessId create rating
 * @apiName RatingPost
 * @apiGroup rating
 * 
 * @apiSuccess (200) {Object} data successful rating creation
 * 
 * @apiParam {String} accessId specific user's access ID
 * @apiParam {Number} rating rating to add. Must be between 1 and 5 inclusive
 * @apiParam {Boolean} isDriver whether rating is for user or driver profile
 *
 * @apiSuccessExample Success-Response:
 * HTTP/1.1 200 OK
{
    "error": false,
    "data": {
        "fieldCount": 0,
        "affectedRows": 1,
        "insertId": 21,
        "serverStatus": 3,
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
  const userId = req.params.accessId;
  const isDriver = req.body.isDriver ? 1 : 0;
  const { rating } = req.body;

  if (rating < 1 || rating > 5) {
    respond(400, 'Rating must be between 1 and 5 inclusive', res);
    return;
  }

  let updateRows;
  db.query('START TRANSACTION')
    .then(() => {
      const sql = 'SELECT id FROM user WHERE access_id = ?';
      return db.query(sql, [userId]);
    })
    .then(rows => {
      if (rows.length === 0) {
        const errString = `user with access id ${userId} not found`;
        respond(400, errString, res);
        return;
      }

      const sql = 'INSERT INTO rating (user_id, value, is_driver) VALUES(?, ?, ?)';
      return db.query(sql, [rows[0].id, rating, isDriver]);
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
