const models = require('../models/index');
const respond = require('../util/respond');
const validate = require('../util/validate');

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
 * * @apiError (Error 4xx) {String} 400 Bad Request
 * @apiError (Error 5xx) {String} 500 Internal Error: {error message}
 *
 */
module.exports.get = async (req, res) => {
  try {
    const [userRatings] = await models.Rating.getAverage(req.params.accessId, false);
    if (userRatings) delete userRatings.dataValues.user;

    const [driverRatings] = await models.Rating.getAverage(req.params.accessId, true);
    if (driverRatings) delete driverRatings.dataValues.user;

    respond(200, {
      user: userRatings || { count: 0, average: 5 },
      driver: driverRatings || { count: 0, average: 5 },
    }, res);
  } catch (err) {
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
    is_driver: 'string',
  }, res)) return;

  try {
    const [user] = await models.User.findAll({
      limit: 1,
      where: {
        access_id: req.params.accessId,
      },
    });
    if (!user) {
      respond(400, `cannot find user with access ID ${req.params.accessId}`, res);
      return;
    }

    if (b.is_driver) {
      const [driver] = await models.Driver.findAll({
        limit: 1,
        where: {
          userId: user.dataValues.id,
        },
      });
      if (!driver) {
        respond(400, `cannot find driver with access ID ${req.params.accessId}`, res);
        return;
      }
    }
    const userRatingInsert = await user.createRating({
      userId: user.id,
      value: b.rating,
      is_driver: (b.is_driver.toLowerCase() === 'true'),
    });
    respond(200, userRatingInsert, res);
  } catch (err) {
    respond(500, err, res);
  }
};
