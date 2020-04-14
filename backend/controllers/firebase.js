const admin = require('firebase-admin');
const models = require('../models/index');
const { Op } = require('sequelize');
const respond = require('../util/respond');
const validate = require('../util/validate');

admin.initializeApp({
  name: 'Carpool App',
  credential: admin.credential.applicationDefault(),
  databaseURL: 'https://carpooling-app-271518.firebaseio.com'
});

module.exports.post = async (req, res) => {
  try {
    const b = req.body;
    if (!validate(b, {
      users: 'array',
      message: 'string'
    }, res)) return;


    const users = await models.User.findAll({
      where: {
        access_id: {
          [Op.in]: req.body.users
        }
      }
    })
    const tokens = users.map(u => u.dataValues.device_token).filter(t => t !== null && t !== undefined);
    var message = {
      notification: {
        title: 'Carpool App',
        body: req.body.message
      },
      android: {
        ttl: 3600 * 1000,
        notification: {
          icon: 'stock_ticker_update',
          color: '#f45342',
        },
      },

      tokens
    };

    // Send a message to the device corresponding to the provided
    // registration token.
    return new Promise((resolve, reject) => {
      if (message.tokens.length)
        admin.messaging().sendMulticast(message)
          .then(resolve)
          .catch(reject);
      else
        resolve({
          message: "No users notified"
        })
    });
  }
  catch (err) {
    respond(500, err, res);
  }
}