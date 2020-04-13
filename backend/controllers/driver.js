const models = require('../models/index');
const respond = require('../util/respond');
const validate = require('../util/validate');
const sequelize = require('sequelize');
const jwt = require('../util/jwt');

/**
 * @api {get} /driver list drivers
 * @apiName DriverGet
 * @apiGroup driver
 *
 * @apiSuccess (200) {Object} data list of driver profiles
 *
 * @apiSuccessExample Success-Response:
 * HTTP/1.1 200 OK
{
    "error": false,
    "data": [
        {
            "id": 2,
            "car": "2010 ford fusion",
            "name": "darpan",
            "access_id": "ab1234",
            "phone_number": "1412122234"
        }
    ]
}
 *
 * @apiError (Error 5xx) {String} 500 Internal Error: {error message}
 *
 */

// include: {
//   model: models.Rating,
//     attributes: [
//       [sequelize.fn('COUNT', sequelize.col('value')), 'count'],
//       [sequelize.fn('AVG', sequelize.col('value')), 'average']
//     ]
// }
module.exports.get = (req, res) => {
  models.Driver.findAll({
    include: {
      model: models.User,
      include: {
        model: models.Rating,
        attributes: [
          [sequelize.fn('COUNT', sequelize.col('value')), 'count'],
          [sequelize.fn('AVG', sequelize.col('value')), 'average']
        ]
      }
    },
  })
    .then(drivers => {
      const list = drivers.map(d => ({
        id: d.id,
        user_id: d.userId,
        name: d.user.name,
        phone_number: d.user.phone_number,
        location: d.user.location,
        access_id: d.user.access_id,
        car: d.car,
        rating: d.user.rating
      }));

      respond(200, list, res);
    })
    .catch(err => {
      respond(500, err, res);
    });
};

/**
 * @api {get} /driver/:accessId get driver by access ID
 * @apiName DriverGetById
 * @apiGroup driver
 *
 * @apiParam {String} accessId specific user's access ID
 *
 * @apiSuccess (200) {Object} data list of driver profiles
 *
 * @apiSuccessExample Success-Response:
 * HTTP/1.1 200 OK
{
    "error": false,
    "data": 
        {
            "id": 2,
            "car": "2010 ford fusion",
            "name": "darpan",
            "access_id": "ab1234",
            "phone_number": "1412122234"
        }
    
}
 *
 * @apiError (Error 5xx) {String} 500 Internal Error: {error message}
 *
 */
module.exports.getById = (req, res) => {
  const { accessId } = req.params;

  models.Driver.findAll({
    limit: 1,
    include: {
      model: models.User,
      where: { access_id: accessId },
      include: {
        model: models.Rating,
        attributes: [
          [sequelize.fn('COUNT', sequelize.col('value')), 'count'],
          [sequelize.fn('AVG', sequelize.col('value')), 'average']
        ]
      }
    },
  })
    .then(drivers => {
      let obj = {};
      if (drivers.length && drivers[0].dataValues.id !== null) {
        const d = drivers[0];
        obj = {
          id: d.id,
          user_id: d.userId,
          name: d.user.name,
          phone_number: d.user.phone_number,
          location: d.user.location,
          access_id: d.user.access_id,
          car: d.car,
          rating: d.user.rating

        };
      }

      respond(200, obj, res);
    })
    .catch(err => {
      respond(500, err, res);
    });
};


/**
 * @api {post} /driver create driver
 * @apiName DriverPost
 * @apiGroup driver
 *
 * @apiSuccess (200) {Object} data successful driver creation
 *
 *  @apiParamExample {json} Request-Example:
 * {
 *     "access_id":"ab1234",
 *     "car":"2010 ford fusion",
 * }
 *
 * @apiSuccessExample Success-Response:
 * HTTP/1.1 200 OK
{
    "error": false,
    "data": {
        "id": 2,
        "car": "2016 VW Tiguan",
        "userId": 3
    }
}
 *
 * @apiError (Error 5xx) {String} 500 Internal Error: {error message}
 * @apiError (Error 4xx) {String} 400 Bad Request: cannot find user with access id <accessId>
 */
module.exports.post = async (req, res) => {
  const b = req.body;
  if (!validate(b, {
    car: 'string',
  }, res)) return;

  try {

    const decoded = await jwt.decode(req.headers.authorization);

    const [driver] = await models.Driver.findAll({
      include: {
        model: models.User,
        where: {
          access_id: decoded.access_id
        }
      }
    });

    if (driver) {
      respond(400, 'Driver with access ID ${decoded.access_id} already exists', res);
      return;
    }

    const created = await models.Driver.create({
      car: b.car,
      userId: decoded.id
    }, { fields: ['car', 'userId'] });

    respond(200, created, res);
  }

  catch (err) {
    if (err.name && err.name === 'SequelizeUniqueConstraintError') respond(400, err.errors[0].message, res);
    else respond(500, err, res);
  }
};

/**
 * @api {delete} /driver delete driver
 * @apiName DriverDelete
 * @apiGroup driver
 *
 * @apiSuccess (200) {Object} data successful driver creation
 *
 * @apiSuccessExample Success-Response:
 * HTTP/1.1 200 OK
{
    "error": false,
    "data": {
        "deleted": 1
    }
}
 *
 * @apiError (Error 5xx) {String} 500 Internal Error: {error message}
 * @apiError (Error 4xx) {String} 400 Bad Request: cannot find user with access id <accessId>
 */
module.exports.delete = async (req, res) => {
  if (!validate(req.params, {
    accessId: 'string',
  }, res)) return;

  try {
    const decoded = await jwt.decode(req.headers.authorization);
    const deleted = await models.Driver.destroy({
      where: {
        userId: decoded.id,
      }
    });
    respond(200, { deleted }, res);
  }
  catch (err) {
    respond(500, err, res);
  }

}
