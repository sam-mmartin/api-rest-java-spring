db.createUser(
    {
        user: "sam",
        pwd: "123456",
        roles: [
            {
                role: "readWrite",
                db: "personal"
            }
        ]
    }
);