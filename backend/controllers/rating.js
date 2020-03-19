const db = require('../util/db');
const respond = require('../util/respond');

module.exports.get = (req, res) => {
    const userId = req.params.accessId;
    if (!userId) {
        respond(400, "please provide valid access id.", res);
        return;
    }


    sql = 'SELECT rating.user_id AS user_id, IFNULL(AVG(rating.value), 0) AS average, COUNT(rating.value) AS count FROM passenger LEFT JOIN rating ON passenger.id=rating.user_id WHERE passenger.access_id = ?;'
    db.query(sql, userId)
        .then(rows => {
            respond(200, rows, res);
        })
        .catch(err => {
            respond(500, err, res);
        });
}

module.exports.post = (req, res) => {
    const userId = req.params.accessId;
    const isDriver = req.body.isDriver ? 1 : 0;
    const rating = req.body.rating;

    if(rating < 1 || rating > 5){
        respond(400, "Rating must be between 1 and 5 inclusive", res);
        return;
    }

    let updateRows;
    db.query('START TRANSACTION')
        .then(() => {
            const sql = "SELECT id FROM passenger WHERE access_id = ?";
            return db.query(sql, [userId]);
        })
        .then(rows => {
            if (rows.length === 0) {
                const errString = `passenger with access id ${userId} not found`;
                respond(400, errString, res);
                return;
            }

            const sql = 'INSERT INTO rating (user_id, value, is_driver) VALUES(?, ?, ?)';
            return db.query(sql, [rows[0].id, rating, isDriver]);
        })
        .then(rows => {
            updateRows = rows;
            return db.query('COMMIT');
        })
        .then(() => {
            respond(200, updateRows, res);
        })
        .catch(err => {
            db.query('ROLLBACK')
                .then(() => {
                    respond(500, err, res);
                });
        });
}