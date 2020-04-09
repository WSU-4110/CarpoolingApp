// Api documentation documentation http://apidocjs.com/#params
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
  models.User.findAll()
    .then(users => {
      users.map(u => { delete u.dataValues.password; return u; });
      respond(200, users, res);
    })
    .catch(err => {
      respond(500, err, res);
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

  models.User.findAll({
    limit: 1,
    where: {
      access_id: accessId,
    },
  })
    .then(users => {
      const obj = users.length ? users[0] : {};
      delete obj.dataValues.password;
      respond(200, obj, res);
    })
    .catch(err => {
      respond(500, err, res);
    });
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
 *  @apiSuccess (200) {Object} data successful user creation
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
 * @apiParam {String} phone_number phone_number of user to update
 * @apiParam {String} location location of user to update
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
 * @apiError (Error 4xx) {String} 400 Bad Request: "Please provide a valid access id."
 * @apiError (Error 5xx) {String} 500 Internal Error: {error message}
 *
 */
module.exports.put = (req, res) => {
  if (!validate(req.params, {
    accessId: 'string',
  }, res)) return;

  const user = {
    name: req.body.name,
    phone_number: req.body.phone_number,
    location: req.body.location,
  };

  models.User.update(user, {
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
 * @apiError (Error 4xx) {String} 400 Bad Request: "Please provide a valid access id."
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
