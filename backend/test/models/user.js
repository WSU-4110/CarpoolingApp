const chai = require('chai');
const chaiHttp = require('chai-http');
const { assert } = require('chai');
const userFactory = require('../factories/user');
const truncate = require('../truncate');
const app = require('../../app');

chai.use(chaiHttp);
chai.should();

describe("POST /", () => {
  let user, token;
  before(async () => {
    await truncate();
    user = await userFactory();
  });
  it("should create a user", (done) => {
    chai.request(app)
      .post('/user')
      .send(user)
      .end((err, res) => {
        res.should.have.status(200);
        res.body.should.be.a('object');
        done();
      });
  });
  it("should login a user", (done) => {
    chai.request(app)
      .post('/user/auth')
      .send({
        access_id: user.access_id,
        password: user.password
      })
      .end((err, res) => {
        res.should.have.status(200);
        res.body.should.be.a('object');
        res.body.data.token.should.be.a('string');
        done();
      });
  });
  it("should login a user", (done) => {
    chai.request(app)
      .post('/user/auth')
      .send({
        access_id: user.access_id,
        password: user.password
      })
      .end((err, res) => {
        res.should.have.status(200);
        res.body.should.be.a('object');
        res.body.data.token.should.be.a('string');
        token = res.body.data.token;
        done();
      });
  });
  it("should list one user", (done) => {
    chai.request(app)
      .get('/user')
      .set('Authorization', token)
      .end((err, res) => {
        res.should.have.status(200);
        res.body.should.be.a('object');
        res.body.data.should.be.an('array');
        assert.equal(res.body.data.length, 1);
        done();
      });
  });
  it("should fail without auth token", (done) => {
    chai.request(app)
      .get('/user')
      .end((err, res) => {
        res.should.have.status(401);
        done();
      });
  });
  it("should delete user", (done) => {
    chai.request(app)
      .delete('/user/' + user.access_id)
      .set('Authorization', token)
      .end((err, res) => {
        res.should.have.status(200);
        done();
      });
  });
});