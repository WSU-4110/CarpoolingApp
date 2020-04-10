const admin = require('firebase-admin');

admin.initializeApp({
    credential: admin.credential.applicationDefault(),
    databaseURL: 'https://carpooling-app-271518.firebaseio.com'
  });
  
  var registrationToken = 'cuY66e1BRSewMBDpncXfmz:APA91bGY3o9K7IAOWtiElHysVHj690j9QlOwenSjswfC_FsaE886dYNAu7TvxkKLdT59WNJ6_xrMzYeLdObX-D5z_s11JXg7PH4IRu3ocfurQYVSJ2PzKUvhZYpcZpraGY3-HtXzfjeh';

var message = {
  data: {
    score: '850',
    time: '2:45'
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
