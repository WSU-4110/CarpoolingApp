const faker = require('faker');

const data = (props = {}) => {
  const defaultProps = {
    access_id: 'aa' + (Math.floor(Math.random() * 9000) + 1000),
    name: faker.name.firstName(),
    phone_number: faker.phone.phoneNumber(0),
    location: faker.address.city(),
    password: faker.random.alphaNumeric(10)
  }; return Object.assign({}, defaultProps, props);
};

module.exports = async (props = {}) => data(props);