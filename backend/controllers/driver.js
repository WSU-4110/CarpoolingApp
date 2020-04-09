const models = require('../models/index');
const respond = require('../util/respond');
const validate = require('../util/validate');

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
module.exports.get = (req, res) => {
  models.Driver.findAll({
    include: models.User,
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
    include: { model: models.User, where: { access_id: accessId } },
  })
    .then(drivers => {
      let obj = {};
      if (drivers.length) {
        const d = drivers[0];
        obj = {
          id: d.id,
          user_id: d.userId,
          name: d.user.name,
          phone_number: d.user.phone_number,
          location: d.user.location,
          access_id: d.user.access_id,
          car: d.car,
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
module.exports.post = (req, res) => {
  const b = req.body;
  if (!validate(b, {
    access_id: 'string',
    car: 'string',
  }, res)) return;

  const driver = {
    car: b.car,
  };

  let responded = false;
  models.User.findAll({
    limit: 1,
    include: models.Driver,
    where: {
      access_id: b.access_id,
    },
  })
    .then(users => {
      if (!users.length) {
        const errString = `no user exists with access ID ${b.access_id}`;
        respond(400, errString, res);
        responded = true;
        throw errString;
      } else if (users[0].driver !== null) {
        const errString = `driver with access ID ${b.access_id} already exists`;
        respond(400, errString, res);
        responded = true;
        throw errString;
      } else {
        driver.userId = users[0].id;
        return users[0].createDriver(driver, { fields: ['car', 'userId'] });
      }
    })
    .then(driver => {
      respond(200, driver, res);
    })
    .catch(err => {
      if (responded) return;
      if (err.name && err.name === 'SequelizeUniqueConstraintError') respond(400, err.errors[0].message, res);
      else respond(500, err, res);
    });
};

/**
 * @api {delete} /driver/:accessId delete driver
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
module.exports.delete = (req, res) => {
  if (!validate(req.params, {
    accessId: 'string',
  }, res)) return;

  models.User.findAll({
    limit: 1,
    include: models.Driver,
    where: {
      access_id: req.params.accessId,
    },
  })
    .then(users => {
      if (users.length) {
        return models.Driver.destroy({
          where: {
            userId: users[0].id,
          },
        });
      }
    })
    .then(deleted => {
      respond(200, { deleted }, res);
    })
    .catch(err => {
      respond(500, err, res);
    });
};
