const models = require('../models/index');

module.exports = async function truncate() {
  return await Promise.all(
    Object.keys(models).map((key) => {
      if (['sequelize', 'Sequelize', '_sync'].includes(key)) return null;
      return models[key].destroy({ where: {}, force: true });
    })
  );
}