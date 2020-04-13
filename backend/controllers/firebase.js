const admin = require('firebase-admin');

admin.initializeApp({
  name: 'Carpool App',
  credential: admin.credential.applicationDefault(),
  databaseURL: 'https://carpooling-app-271518.firebaseio.com'
});

var registrationToken = 'cuY66e1BRSewMBDpncXfmz:APA91bGY3o9K7IAOWtiElHysVHj690j9QlOwenSjswfC_FsaE886dYNAu7TvxkKLdT59WNJ6_xrMzYeLdObX-D5z_s11JXg7PH4IRu3ocfurQYVSJ2PzKUvhZYpcZpraGY3-HtXzfjeh';

var message = {
  notification: {
    title: 'Carpool App',
    body: ' this is my message'
  },
  android: {
    ttl: 3600 * 1000,
    notification: {
      icon: 'stock_ticker_update',
      color: '#f45342',
    },
  },

  token: registrationToken
};

// Send a message to the device corresponding to the provided
// registration token.
admin.messaging().send(message)
  .then((response) => {
    // Response is a message ID string.
    console.log('Successfully sent message:', response);
  })
  .catch((error) => {
    console.log('Error sending message:', error);
  });
