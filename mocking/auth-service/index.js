/**
 * @author Herzig Melvyn
 */

const express = require('express');
const app = express();
const port = 8081;
let users = require('./users.json')

app.use(express.json())

var rand = function() {
    return Math.random().toString(36).substr(2);
};

var token = function() {
    return rand() + rand();
};

token(); // "bnh5yzdirjinqaorq0ox1tf383nb3xr"

// POST /auth/login
app.post('/auth/login', function(req, res){

    let existingAcc = users.find(user => user.username === req.body.username && user.password === req.body.password);

    if(existingAcc === undefined) {
        let answer = {};
        answer["error"] = "The credentials are incorrect"
        res.status(403).json(answer);
        return;
    }

    let answerAccount = {};
    answerAccount["id"] = existingAcc.id;
    answerAccount["username"] = existingAcc.username;
    answerAccount["role"] = existingAcc.role;

    let answer = {};
    answer["token"] = token();
    answer["account"] = answerAccount;

    res.status(201).json(answer);

});

// POST /auth/login
app.post('/accounts/register', function(req, res){

    let newAccount = req.body;

    let existingAcc = users.find(user => user.username === newAccount.username);

    if(existingAcc !== undefined){
        let answer = {};
        answer["error"] = "The username already exist"
        res.status(409).json(answer);
        return;
    }

    if(newAccount.username === "" || newAccount.password === ""){
        let answer = {};
        answer["errors"] = [];

        if(newAccount.username === "") {
            let error = {};
            error["property"] = "username";
            error["message"] = "username connot be empty";
            answer.errors.push(error);
        }

        if(newAccount.password === "") {
            let error = {};
            error["property"] = "password";
            error["message"] = "password connot be empty";
            answer.errors.push(error);
        }

        res.status(422).json(answer);
        return;
    }

    let newId = users.length;

    let newUser = {};
    newUser["id"] = newId;
    newUser["username"] = req.body.username;
    newUser["role"] = "user";

    res.status(201).json(newUser);

    newUser["password"] = req.body.password;

    users.push(newUser);
});


// Start the server
const server = app.listen(port, () => console.log(`Auth service mock app listening on port ${port}!`));


// Export the server
module.exports = server;
