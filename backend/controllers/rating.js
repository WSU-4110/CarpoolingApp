const models = require('../models/index');
const respond = require('../util/respond');
const validate = require('../util/validate');
const sequelize = require('sequelize');

/**
 * @api {get} /rating/:accessId get rating
 * @apiName RatingGet
 * @apiGroup rating
 *
 * @apiSuccess (200) {Number} average average user rating
 * @apiSuccess (200) {Number} count number of ratings for this user
 *
 * @apiSuccessExample Success-Response:
 * HTTP/1.1 200 OK
{
    "error": false,
    "data": {
        "user": {
            "count": 0,
            "average": null
        },
        "driver": {
            "count": 7,
            "average": "4.0000"
        }
    }
}
 * * @apiError (Error 4xx) {String} 400 Bad Request: "please provide valid access id."
 * @apiError (Error 5xx) {String} 500 Internal Error: {error message}
 *
 */
module.exports.get = async (req, res) => {
  if (!validate(req.params, {
    accessId: 'string',
  }, res)) return;

  try {

    const [userRatings] = await models.Rating.findAll({
      attributes: [
        [sequelize.fn('COUNT', sequelize.col('value')), 'count'],
        [sequelize.fn('AVG', sequelize.col('value')), 'average']
      ],
      where: {
        is_driver: false,
      },
      include: {
        model: models.User,
        where: {
          access_id: req.params.accessId
        }
      }
    });
    delete userRatings.dataValues.user;

    const [driverRatings] = await models.Rating.findAll({
      attributes: [
        [sequelize.fn('COUNT', sequelize.col('value')), 'count'],
        [sequelize.fn('AVG', sequelize.col('value')), 'average']
      ],
      where: {
        is_driver: true,
      },
      include: {
        model: models.User,
        where: {
          access_id: req.params.accessId
        }
      }
    });
    delete driverRatings.dataValues.user;

    respond(200, {
      user: userRatings,
      driver: driverRatings
    }, res);

  }
  catch (err) {
    respond(500, err, res);
  }

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
 * @apiParam {Boolean} is_driver whether rating is for user or driver profile
 * 
 *  * @apiParamExample {json} Request-Example:
{
	"rating": 4,
	"is_driver": true
}
 *
 * @apiSuccessExample Success-Response:
 * HTTP/1.1 200 OK
{
    "error": false,
    "data": {
        "id": 7,
        "userId": 1,
        "value": 4,
        "is_driver": true
    }
}
 *
 * @apiError (Error 4xx) {String} 400 Bad Request
 * @apiError (Error 5xx) {String} 500 Internal Error: {error message}
 *
 */
module.exports.post = async (req, res) => {
  const b = req.body;
  if (!validate(b, {
    rating: 'integer',
    is_driver: 'boolean',
  }, res)) return;

  try {

    const [user] = await models.User.findAll({
      limit: 1,
      where: {
        access_id: req.params.accessId,
      }
    })
    if (!user) {
      respond(400, `cannot find user with access ID ${b.access_id}`, res);
      return;
    }
    const userRatingInsert = await user.createRating({
      userId: user.id,
      value: b.rating,
      is_driver: b.is_driver
    })
    respond(200, userRatingInsert, res);

  }
  catch (err) {
    respond(500, err, res);

  }

  // const userId = req.params.accessId;
  // const isDriver = req.body.isDriver ? 1 : 0;
  // const { rating } = req.body;

  // if (rating < 1 || rating > 5) {
  //   respond(400, 'Rating must be between 1 and 5 inclusive', res);
  //   return;
  // }

  // let updateRows;
  // db.query('START TRANSACTION')
  //   .then(() => {
  //     const sql = 'SELECT id FROM user WHERE access_id = ?';
  //     return db.query(sql, [userId]);
  //   })
  //   .then(rows => {
  //     if (rows.length === 0) {
  //       const errString = `user with access id ${userId} not found`;
  //       respond(400, errString, res);
  //       return;
  //     }

  //     const sql = 'INSERT INTO rating (user_id, value, is_driver) VALUES(?, ?, ?)';
  //     return db.query(sql, [rows[0].id, rating, isDriver]);
  //   })
  //   .then(rows => {
  //     updateRows = rows;
  //     return db.query('COMMIT');
  //   })
  //   .then(() => {
  //     respond(200, updateRows, res);
  //   })
  //   .catch(err => {
  //     db.query('ROLLBACK')
  //       .then(() => {
  //         respond(500, err, res);
  //       });
  //   });
};
