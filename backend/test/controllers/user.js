const chai = require('chai');
const sinon = require('sinon');
const userFactory = require('../factories/user');
const userController = require('../../controllers/user');

sinon.assert.expose(chai.assert, { prefix: '' });

const mockRequest = (params, body) => {
  const req = {
    params, body,
  };
  return req;
};

const mockResponse = () => {
  const res = {};
  res.status = sinon.stub().returns(res);
  res.json = sinon.stub().returns(res);
  res.set = sinon.stub().returns(res);
  res.send = sinon.stub().returns(res);
  return res;
};

describe('tests', () => {
  let models = {};
  before(() => {
    models = {
      User: {
        findAll: () => {
          const users = [];
          for (let i = 0; i < 10; i++) {
            users.push(userFactory());
          }
          return users;
        },
        create: () => {
          const user = userFactory();

          return {
            dataValues: user,
            createAddress: sinon.stub(),
          };
        },
      },
      Rating: {
        getAverage: sinon.stub(),
      },
      sequelize: {
        transaction() {
          return {
            commit: sinon.stub(),
            rollback: sinon.stub(),
          };
        },
      },
    };
  });
  it('should create a user', async () => {
    const user = userFactory();
    const req = mockRequest(null, user);
    const res = mockResponse();
    await userController.post(models)(req, res);
    sinon.assert.calledWith(res.status, 200);
  });
  it('should fail to create without access_id', async () => {
    const user = userFactory();
    delete user.access_id;
    const req = mockRequest(null, user);
    const res = mockResponse();
    await userController.post(models)(req, res);
    sinon.assert.calledWith(res.status, 400);
    sinon.assert.calledWith(res.json, { error: 'access_id must not be undefined' });
  });
  it('should receive a list of users', async () => {
    const user = userFactory();
    const req = mockRequest(null, user);
    const res = mockResponse();
    await userController.get(models)(req, res);
    sinon.assert.calledWith(res.status, 200);
  });
});
