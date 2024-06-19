// init-mongo.js
db = db.getSiblingDB('useranalytics'); // Replace with your database name

db.createUser({
    user: 'testUser',
    pwd: 'pwd',
    roles: [
        {
            role: 'readWrite',
            db: 'useranalytics' // Replace with your database name
        }
    ]
});
