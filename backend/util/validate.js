const respond = require('../util/respond');

const statusCode = 400;
/**
* @param {Object} object Object to be validated
* @param {Object} validator Object describing the validation
* @param {Object} res The res or resource object from express.
*/
exports.default = function validate(objIn, val, res) {
  try {
    let obj = objIn;
    if (typeof obj === 'string') {
      obj = JSON.parse(objIn);
    }
    if (typeof obj !== 'object') {
      respond(statusCode, 'invaild or missing body, query or parameter', res);
      return false;
    }
    const keys = Object.keys(val);
    for (let i = 0; i < keys.length; i++) {
      const k = keys[i];
      const o = val[k];
      if (typeof o === 'function') {
        const rtn = o(obj[k], k);
        if (rtn !== true) {
          if (typeof rtn === 'string') {
            respond(statusCode, rtn, res);
          } else {
            respond(statusCode, `${k} is invalid`, res);
          }
        }
        return (rtn === true);
      }
      if (typeof obj[k] === 'undefined') {
        respond(statusCode, `${k} must not be undefined`, res);
        return false;
      }
      if (obj[k] === 'null') {
        respond(statusCode, `${k} must not be null`, res);
        return false;
      }
      if (o === 'number' || o === 'natural' || o === 'integer') {
        if (Number.isNaN(Number(obj[k]))) {
          respond(statusCode, `${k} must be numeric`, res);
          return false;
        }
        if (o === 'natural' || o === 'integer') {
          if (Number(obj[k]) !== parseInt(Number(obj[k]), 10)) {
            respond(statusCode, `${k} must be a integer`, res);
            return false;
          }
          if (o === 'natural') {
            if (!(Number(obj[k]) > 0)) {
              respond(statusCode, `${k} must be a natural number`, res);
              return false;
            }
          }
        }
        obj[k] = Number(obj[k]);
      }
      if (o === 'string' && typeof obj[k] !== 'string') {
        respond(statusCode, `${k} must be a string`, res);
        return false;
      }
      if (o === 'boolean' && typeof obj[k] !== 'boolean') {
        respond(statusCode, `${k} must be a boolean`, res);
        return false;
      }
      if (o === 'object' || o === 'array') {
        if (o === 'object' && typeof obj[k] !== 'object') {
          respond(statusCode, `${k} must be a object`, res);
          return false;
        }
        if (o === 'array' && !Array.isArray(obj[k])) {
          respond(statusCode, `${k} must be a array`, res);
          return false;
        }
      }
      const dateReg = /^([0-9]{2,4})-([0-1]{0,1}[0-9])-([0-3]{0,1}[0-9])(?:( [0-2]{0,1}[0-9]):([0-5]{0,1}[0-9]):([0-5]{0,1}[0-9]))?$/;
      if (o === 'date' && dateReg.exec(obj[k]) === null) {
        respond(statusCode, `${k} must be a date`, res);
        return false;
      }
      const emailReg = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
      if (o === 'email' && emailReg.exec(obj[k]) === null) {
        respond(statusCode, `${k} must be a valid email`, res);
        return false;
      }
    }
    return true;
  } catch (error) {
    respond(statusCode, 'Invalid body', res);
    return false;
  }
};
module.exports = exports.default;
//     number
//     natural
//     integer
//     string
//     boolean
//     object
//     array
//     date
//     email
