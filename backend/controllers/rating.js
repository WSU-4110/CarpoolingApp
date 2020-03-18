const db = require('../util/db');
const respond = require('../util/respond');

module.exports.get = (req, res) => {
    const user = req.params.accessId;
    if (!user) {
        respond(400, "please provide valid access id.", res);
    }

    db.query('START TRANSACTION')
        .then(() => {
            const sql = 'SELECT * FROM passenger WHERE access_id=?';
            return db.query(sql, [user]);
        })
        .then(rows => {
            if (rows.length === 0) {
                const errString = `passenger with access id ${user} not found`;
                respond(400, errString, res);
                throw errString;
            }

            sql = 'SELECT IFNULL(AVG(value), 0) as average, count(value) as count FROM rating WHERE user_id = ?'
            return db.query(sql, rows[0].id);
        })
        .then(rows => {
            respond(200, rows, res);
            return db.query('COMMIT');
        })
        .catch(err => {
            db.query('ROLLBACK')
                .then(() => {
                    respond(500, err, res);
                });
        });
}