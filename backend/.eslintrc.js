module.exports = {
  env: {
    commonjs: true,
    es6: true,
    node: true,
  },
  extends: [
    'airbnb-base',
  ],
  globals: {
    Atomics: 'readonly',
    SharedArrayBuffer: 'readonly',
  },
  parserOptions: {
    ecmaVersion: 2018,
  },
  rules: {
    "indent": [2, 2],
    "no-console": "off",
    "arrow-parens": 0,
    "no-underscore-dangle": 0,
    "no-plusplus": 0,
  },
};
