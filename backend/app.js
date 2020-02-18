const express = require('express');
const app = express();
const bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());
const port = process.env.port || 8080;

app.get('/', (req, res) => {
    res.send('OK');
})

app.post('/passenger', (req, res) => {
    console.log(req.body);
    res.send(req.body);
})

app.listen(port, () => {
    console.log('listening on port ' + port);

})