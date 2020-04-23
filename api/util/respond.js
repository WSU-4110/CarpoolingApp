/**
 * Secrets and env wrapper for
 * @param {number} statusCode Status code to respond with
 * @param {string} [message] 2xx Message to be sent back.
 *      3xx url to redirect to. 4/5xx error to send back.
 * @param {string} res Express res or response object
 * @async
 */
module.exports = function respond(statusCode, message, res) {
  // eslint-disable-next-line no-param-reassign
  statusCode = parseInt(statusCode, 10);
  let msg = false;
  if (statusCode.toString().charAt(0) === '2') {
    msg = {
      error: false,
      data: message,
    };
  } else {
    // if (process.env.ENVIRONMENT === 'prod') {
    //   msg = {
    //     error: true,
    //   };
    // } else {
    msg = {
      error: (message.stack ? message.stack : message),
    };
    // }
    console.log('ERROR: ', JSON.stringify(msg));
  }
  if (statusCode === 204) {
    res.status(statusCode).send();
  } else if (statusCode.toString().charAt(0) === '3') {
    res.status(statusCode).set({
      Location: message,
    }).send();
  } else {
    res.status(statusCode).set({
      'Content-Type': 'Application/JSON',
    }).json(msg);
  }
};
