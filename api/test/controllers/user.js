const chai = require('chai');
const sinon = require('sinon');
const userFactory = require('../factories/user');
const userController = require('../../controllers/user');

sinon.assert.expose(chai.assert, { prefix: '' });

const mockRequest = (params, body) => {
  const headers = {
    authorization: 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6NiwibmFtZSI6IkV2YW4gZGUgSmVzdXMiLCJhY2Nlc3NfaWQiOiJjajUxMDAiLCJpYXQiOjE1ODc1MTkwMDYsImV4cCI6MTU4ODEyMzgwNn0.V2w3c1Zxd4Uf8YDAK7jA4Tc9Yc0kQPh7gIZ1bAYe_co',
  };
  const req = {
    params, body, headers,
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
        update: (id) => {
          const user = userFactory({ access_id: id });

          return [{
            dataValues: user,
            createAddress: sinon.stub(),
          }];
        },
        destroy: () => 1,
      },
      Rating: {
        getAverage: () => ([{
          dataValues: {
            average: 5,
          },
        }]),
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
    const req = mockRequest(null, null);
    const res = mockResponse();
    await userController.get(models)(req, res);
    sinon.assert.calledWith(res.status, 200);
  });
  it('should update a user', async () => {
    const req = mockRequest({ access_id: userFactory().access_id }, null);
    const res = mockResponse();
    await userController.put(models)(req, res);
    sinon.assert.calledWith(res.status, 200);
  });
  it('should delete a user', async () => {
    const req = mockRequest({ access_id: userFactory().access_id }, null);
    const res = mockResponse();
    await userController.delete(models)(req, res);
    sinon.assert.calledWith(res.status, 200);
    sinon.assert.calledWith(res.json, { error: false, data: { deleted: 1 } });
  });
  it('should login', async () => {
    const user = userFactory();
    const req = mockRequest(null, {
      access_id: user.access_id,
      password: user.password,
    });
    const res = mockResponse();
    await userController.auth(models)(req, res);
    sinon.assert.calledWith(res.status, 200);
  });
});
