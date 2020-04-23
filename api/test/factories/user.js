const faker = require('faker');

const data = (props = {}) => {
  const defaultProps = {
    access_id: `aa${Math.floor(Math.random() * 9000) + 1000}`,
    name: faker.name.firstName(),
    phone_number: faker.phone.phoneNumber(0),
    street: faker.address.streetAddress(),
    city: faker.address.city(),
    state: 'MI',
    zip: faker.address.zipCode(),
    password: faker.random.alphaNumeric(10),
    validPassword: () => true,
  };
  defaultProps.dataValues = {
    address: {
      dataValues: {
        street: defaultProps.street,
        city: defaultProps.city,
        state: defaultProps.state,
        zip: defaultProps.street,
      },
    },
  };

  return { ...defaultProps, ...props };
};

module.exports = (props = {}) => data(props);
