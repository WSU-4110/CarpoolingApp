const express = require('express');

const router = express.Router();
const passenger = require('../controllers/passenger');
const driver = require('../controllers/driver');
const ride = require('../controllers/ride');
const rating = require('../controllers/rating');

router.get('/', (req, res) => {
    res.send('OK');
})

// passenger
router.get('/passenger', passenger.get);
router.post('/passenger', passenger.post);
router.put('/passenger', passenger.put);
router.delete('/passenger', passenger.delete);

// driver
router.get('/driver', driver.get);
router.post('/driver', driver.post);

// ride 
router.get('/ride', ride.get);
router.post('/ride', ride.post);
router.put('/ride', ride.put);
<<<<<<< HEAD
router.delete('/ride', ride.put);


=======
>>>>>>> 763ff029a5376180d02152cb4839222facd91822

// rating
router.get('/rating', rating.get);
router.get('/rating/:accessId', rating.get);
router.post('/rating/:accessId', rating.post);

module.exports = router;
