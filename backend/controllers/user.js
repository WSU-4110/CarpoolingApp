// Api documentation documentation http://apidocjs.com/#params
const models = require('../models/index');
const respond = require('../util/respond');
const jwt = require('../util/jwt');
const validate = require('../util/validate');
const sequelize = require('sequelize');


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
            "name": "Evan",
            "phone_number": "0001112222",
            "location": "troy",
            "access_id": "cj5100",
            "rating": null
        },
        {
            "id": 2,
            "name": "Sam",
            "phone_number": "1112223333",
            "location": "clawson",
            "access_id": "ab1234",
            "rating": null
        }
    ]
}
 *
 * @apiError (Error 5xx) {String} 500 Internal Error: {error message}
 *
 */
module.exports.get = async (req, res) => {
  try {


    const users = await models.User.findAll();
    await Promise.all(users.map(async user => {
      const [rating] = await models.Rating.findAll({
        where: {
          userId: user.dataValues.id,
          is_driver: false,
        },
        attributes: [
          [sequelize.fn('COUNT', sequelize.col('value')), 'count'],
          [sequelize.fn('AVG', sequelize.col('value')), 'average']
        ]
      });
      user.dataValues.rating = rating.dataValues.average;

    }));

    users.map(u => { delete u.dataValues.password; return u; });
    respond(200, users, res);
  }
  catch (err) {
    respond(500, err, res);
  };
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

    const [user] = await models.User.findAll({
      limit: 1,
      where: {
        access_id: accessId,
      }
    });
    if (!user) {
      respond(400, 'user with access ID ' + accessId + ' not found', res);
      return;
    }
    const [rating] = await models.Rating.findAll({
      where: {
        userId: user.dataValues.id,
        is_driver: false,
      },
      attributes: [
        [sequelize.fn('COUNT', sequelize.col('value')), 'count'],
        [sequelize.fn('AVG', sequelize.col('value')), 'average']
      ]
    });
    user.dataValues.rating = rating.dataValues.average;

    const obj = (user && user.dataValues.id !== null) ? user : {};
    if (obj.dataValues)
      delete obj.dataValues.password;
    respond(200, obj, res);
  }
  catch (err) {
    respond(500, err, res);
  };
};

/**
 * @api {post} /user create user
 * @apiName UserPost
 * @apiGroup user
 *
 *  * @apiParamExample {json} Request-Example:
{
	"name":"Evan de Jesus",
  "access_id": "cj5102",
  "password": "1234",
	"phone_number": "5869783333",
	"location":"atlantis"
}
 * @apiParam {String} access_id user's access ID
 * @apiParam {String} name user name
 * @apiParam {String} password user password
 * @apiParam {String} phone_number user phone number
 * @apiParam {String} location user address
 *
 *
 *  @apiSuccess (200) {Object} data successful user creation
 *
 * @apiSuccessExample Success-Response:
 * HTTP/1.1 200 OK
{
    "error": false,
    "data": {
        "id": 2,
        "name": "Evan de Jesus",
        "phone_number": "5869783333",
        "location": "atlantis",
        "access_id": "cj5102"
    }
}
 *
 * @apiError (Error 4xx) {String} 400 Bad Request
 * @apiError (Error 5xx) {String} 500 Internal Error: {error message}
 *
 */
module.exports.post = (req, res) => {
  const b = req.body;
  if (!validate(b, {
    access_id: 'string',
    name: 'string',
    location: 'string',
    phone_number: 'string',
    password: 'string',
  }, res)) return;

  const phoneNumber = b.phone_number.replace(/\D/g, '');
  if (phoneNumber.length < 9 || phoneNumber.length > 10) {
    respond(400, 'Please provide a valid phone number.', res);
    return;
  }

  const user = {
    name: b.name,
    phone_number: b.phone_number,
    location: b.location,
    access_id: b.access_id,
    password: b.password,
  };

  models.User.create(user, { fields: ['name', 'phone_number', 'location', 'access_id', 'password'] })
    .then(rows => {
      delete rows.dataValues.password;
      respond(200, rows, res);
    })
    .catch(err => {
      if (err.name && err.name === 'SequelizeUniqueConstraintError') respond(400, err.errors[0].message, res);
      else respond(500, err, res);
    });
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
        "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MywibmFtZSI6IkV2YW4gZGUgSmVzdXMiLCJwaG9uZV9udW1iZXIiOiI1ODY5NzgzMzMzIiwibG9jYXRpb24iOiJhdGxhbnRpcyIsImFjY2Vzc19pZCI6ImFhMjIyMiIsImlhdCI6MTU4NjQ3MjU5MSwiZXhwIjoxNTg2NzMxNzkxfQ.7IXaMlJOJm_NSKr8vU1BJivOPl6POxQu5CWHFZb-zo4"
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
      const token = await jwt.sign(data);
      respond(200, { token }, res);
    } else {
      respond(401, 'Unauthorized', res);
    }
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
module.exports.put = (req, res) => {
  if (!validate(req.params, {
    accessId: 'string',
  }, res)) return;

  models.User.update(req.body.user, {
    where: {
      access_id: req.params.accessId,
    },
  })
    .then(updated => {
      respond(200, { updated: updated[0] }, res);
    })
    .catch(err => {
      respond(500, err, res);
    });
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
module.exports.delete = (req, res) => {
  if (!validate(req.params, {
    accessId: 'string',
  }, res)) return;

  models.User.destroy({
    where: {
      access_id: req.params.accessId,
    },
  })
    .then(deleted => {
      respond(200, { deleted }, res);
    })
    .catch(err => {
      respond(500, err, res);
    });
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
    console.log(decoded);

    const [updated] = await models.User.update({ device_token: req.body.token }, {
      where: {
        id: decoded.id
      }
    })
    respond(200, { updated }, res);

  }
  catch (err) {
    respond(500, err, res);

  }

};
