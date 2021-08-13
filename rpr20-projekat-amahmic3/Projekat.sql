BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "Korisnik" (
	"id"	INTEGER UNIQUE,
	"username"	TEXT UNIQUE,
	"password"	TEXT,
	"ime"	TEXT,
	"prezime"	TEXT,
	"email"	INTEGER UNIQUE,
	"brojTelefona"	TEXT UNIQUE,
	"administrator"	INTEGER,
	PRIMARY KEY("id")
);
COMMIT;
