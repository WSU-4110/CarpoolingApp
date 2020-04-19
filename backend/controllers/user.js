const models = require('../models/index');
const respond = require('../util/respond');
const jwt = require('../util/jwt');
const validate = require('../util/validate');


/**
 * @api {get} /user list users
 * @apiName UserGet
 * @apiGroup user
 *
 * @apiSuccess (200) {Object[]} list of user profiles
 *
 * @apiSuccessExample Success-Response:
 * HTTP/1.1 200 OK
{
    "error": false,
    "data": [
        {
            "id": 1,
            "access_id": "cj5100",
            "name": "Evan de Jesus",
            "phone_number": "5869783333",
            "device_token": null,
            "rating": 5,
            "street": "14345 Brierstone Dr.",
            "city": "Sterling Heights",
            "state": "MI",
            "zip": 48312
        }
    ]
}
 *
 * @apiError (Error 5xx) {String} 500 Internal Error: {error message}
 *
 */
module.exports.get = async (req, res) => {
  try {
    const users = await models.User.findAll({
      include: models.Address,
    });
    await Promise.all(users.map(async user => {
      const [rating] = await models.Rating.getAverage(user.access_id, false);
      user.dataValues.rating = rating !== undefined ? rating.dataValues.average : 5;
    }));

    users.map(u => {
      const address = u.dataValues.address.dataValues;
      delete u.dataValues.password;
      u.dataValues.street = address.street;
      u.dataValues.city = address.city;
      u.dataValues.state = address.state;
      u.dataValues.zip = address.zip;
      delete u.dataValues.address;
      return u;
    });
    respond(200, users, res);
  } catch (err) {
    respond(500, err, res);
  }
};

/**
 * @api {get} /user/:accessId get user by access ID
 * @apiName UserGetById
 * @apiGroup user
 *
 * @apiParam {String} accessId specific user's access ID
 *
 * @apiSuccess (200) {Object} data user profile
 *
 * @apiSuccessExample Success-Response:
 * HTTP/1.1 200 OK
{
    "error": false,
    "data": {
        "id": 2,
        "name": "Sam",
        "phone_number": "1112223333",
        "location": "clawson",
        "access_id": "ab1234",
        "rating": null
    }
}
 *
 * @apiError (Error 4xx) {String} 400 Bad Request
 * @apiError (Error 5xx) {String} 500 Internal Error: {error message}
 *
 */
module.exports.getById = async (req, res) => {
  try {
    const { accessId } = req.params;

    const user = await models.User.findOne({
      include: models.Address,
      where: {
        access_id: accessId,
      },
    });
    if (!user) {
      respond(400, `user with access ID ${accessId} not found`, res);
      return;
    }
    const address = user.dataValues.address.dataValues;
    delete user.dataValues.password;
    user.dataValues.street = address.street;
    user.dataValues.city = address.city;
    user.dataValues.state = address.state;
    user.dataValues.zip = address.zip;
    delete user.dataValues.address;
    const [rating] = await models.Rating.getAverage(user.access_id, false);
    user.dataValues.rating = rating !== undefined ? rating.dataValues.average : 5;

    const obj = (user && user.dataValues.id !== null) ? user : {};
    if (obj.dataValues) delete obj.dataValues.password;
    respond(200, obj, res);
  } catch (err) {
    respond(500, err, res);
  }
};

/**
 * @api {post} /user create user
 * @apiName UserPost
 * @apiGroup user
 *
 *  * @apiParamExample {json} Request-Example:
{
  "access_id": "cj5102",
  "password": "1234",
  "name":"Evan de Jesus",
  "phone_number": "5869783333",
  "street": "1111 street st.",
  "city" : "sterling heights",
  "state": "MI",
  "zip" : "48313",
  "device_token": "eScrvHntSOKlY8DNpsi6l4:APA91bE1YZX-_PDGe5Rh"
}
 * @apiParam {String} access_id user's access ID
 * @apiParam {String} name user name
 * @apiParam {String} password user password
 * @apiParam {String} phone_number user phone number
 * @apiParam {String} street user address street
 * @apiParam {String} city user address city
 * @apiParam {String} state user address state
 * @apiParam {String} zip user address zip code
 * @apiParam {String} device_token (Optional) registration token of caller's device
 *
 *
 *  @apiSuccess (200) {Object} data successful user creation
 *
 * @apiSuccessExample Success-Response:
 * HTTP/1.1 200 OK
{
    "error": false,
    "data": {
        "user": {
            "id": 1,
            "name": "Evan de Jesus",
            "phone_number": "5869783333",
            "access_id": "cj5100"
        },
        "address": {
            "id": 1,
            "street": "14345 Brierstone Dr.",
            "city": "Sterling Heights",
            "state": "MI",
            "zip": "48312",
            "userId": 1
        }
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
    access_id: 'string',
    password: 'string',
    name: 'string',
    phone_number: 'string',
    street: 'string',
    city: 'string',
    state: 'string',
    zip: 'string',
  }, res)) return;

  const t = await models.sequelize.transaction();

  try {
    const user = {
      name: b.name,
      phone_number: b.phone_number,
      location: b.location,
      access_id: b.access_id,
      password: b.password,
      device_token: b.device_token,
    };

    const address = {
      street: b.street,
      city: b.city,
      state: b.state,
      zip: b.zip,
    };

    const createdUser = await models.User.create(user,
      { transaction: t });
    delete createdUser.dataValues.password;
    const createdAddress = await createdUser.createAddress(address, { transaction: t });

    t.commit();
    respond(200, {
      user: createdUser,
      address: createdAddress,
    }, res);
  } catch (err) {
    await t.rollback();
    if (err.name && err.name.includes('Sequelize')) respond(400, err.errors[0].message, res);
    else respond(500, err, res);
  }
};

/**
 * @api {post} /user/auth login
 * @apiName UserLogin
 * @apiGroup user
 *
 *  * @apiParamExample {json} Request-Example:
{
  "access_id": "cj5102",
  "password" : "1234"
}
 *
 *
 *  @apiSuccess (200) {Object} token JWT to use in future API requests
 *
 * @apiSuccessExample Success-Response:
 * HTTP/1.1 200 OK
{
    "error": false,
    "data": {
      "token": "eyJhbGciOiJIUzI1NiIsInR"
    }
}
 *
 * @apiError (Error 4xx) {String} 400 Bad Request
 * @apiError (Error 4xx) {String} 401 Unauthorized
 * @apiError (Error 5xx) {String} 500 Internal Error: {error message}
 *
 */
module.exports.auth = async (req, res) => {
  const b = req.body;
  if (!validate(b, { access_id: 'string', password: 'string' }, res)) return;

  try {
    const users = await models.User.findAll({
      limit: 1,
      where: {
        access_id: b.access_id,
      },
    });
    if (!users.length) {
      respond(401, 'Unauthorized', res);
    } else {
      const u = users[0];
      const valid = await u.validPassword(b.password);
      if (valid) {
        const data = u.dataValues;
        delete data.password;
        const token = await jwt.sign({
          id: data.id,
          name: data.name,
          access_id: data.access_id,
        });
        respond(200, { token }, res);
      } else {
        respond(401, 'Unauthorized', res);
      }
    }
  } catch (err) {
    respond(500, err, res);
  }
};

/**
 * @api {put} /users/:accessId update user
 * @apiName UserPut
 * @apiGroup user
 *
 * @apiParam {String} accessId access ID of user to update
 * @apiParam {String} name name of user to update
 * @apiParam {String} phone_number phone number of user to update
 * @apiParam {String} device_token registration token of user's device
 * @apiParam {String} location address of user to update
 *
 *  * @apiParamExample {json} Request-Example:
 * {
 *     "name":"Test",
 *     "phone_number":"5555555555",
 *     "location":"Atlantis"
 * }
 *
 *  @apiSuccess (200) {Object} data successful user update
 *
 * @apiSuccessExample Success-Response:
 * HTTP/1.1 200 OK
{
    "error": false,
    "data": {
        "updated": 1
    }
}
 * @apiError (Error 5xx) {String} 500 Internal Error: {error message}
 *
 */
module.exports.put = async (req, res) => {
  try {
    const [updated] = await models.User.update(req.body, {
      where: {
        access_id: req.params.accessId,
      },
    });
    respond(200, { updated }, res);
  } catch (err) {
    if (err.name && err.name.includes('Sequelize')) respond(400, err.errors[0].message, res);
    else respond(500, err, res);
  }
};

/**
 * @api {delete} /users/:accessId delete user
 * @apiName UserDelete
 * @apiGroup user
 *
 * @apiParam {String} accessId access ID of user to updateupdate
 * @apiParam {String} location location of user to update
 *
 *  @apiSuccess (200) {Object} data successful user update
 *
 * @apiSuccessExample Success-Response:
 * HTTP/1.1 200 OK
{
    "error": false,
    "data": {
        "deleted": 1
    }
}
 * @apiError (Error 5xx) {String} 500 Internal Error: {error message}
 *
 */
module.exports.delete = async (req, res) => {
  try {
    const decoded = await jwt.decode(req.headers.authorization);
    const deleted = await models.User.destroy({
      where: {
        access_id: decoded.access_id,
      },
    });
    respond(200, { deleted }, res);
  } catch (err) {
    respond(500, err, res);
  }
};

/**
 * @api {post} /users/token update device token
 * @apiName UserTokenPost
 * @apiGroup user
 *
 * @apiParam {String} token registration token of caller's device
 *
 *  @apiSuccess (200) {Object} data successful token update
 *
 * @apiSuccessExample Success-Response:
 * HTTP/1.1 200 OK
{
    "error": false,
    "data": {
        "update": 1
    }
}
 * @apiError (Error 5xx) {String} 500 Internal Error: {error message}
 *
 */
module.exports.devicePost = async (req, res) => {
  try {
    const decoded = await jwt.decode(req.headers.authorization);
    const [updated] = await models.User.update({ device_token: req.body.token }, {
      where: {
        id: decoded.id,
      },
    });
    respond(200, { updated }, res);
  } catch (err) {
    respond(500, err, res);
  }
};
