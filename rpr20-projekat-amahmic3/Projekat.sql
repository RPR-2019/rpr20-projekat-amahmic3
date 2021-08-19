BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "Korisnik" (
	"id"	INTEGER UNIQUE,
	"username"	TEXT UNIQUE,
	"password"	TEXT,
	"ime"	TEXT,
	"prezime"	TEXT,
	"email"	TEXT UNIQUE,
	"brojTelefona"	TEXT UNIQUE,
	"administrator"	INTEGER,
	PRIMARY KEY("id")
);
INSERT INTO Korisnik(username,password,administrator) VALUES ("admin","admin",1);
COMMIT;
