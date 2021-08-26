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
CREATE TABLE IF NOT EXISTS "ObrazovnaInstitucija" (
                                                      "ID"	INTEGER,
                                                      "Naziv"	TEXT,
                                                      "Adresa"	TEXT,
                                                      "PostanskiBroj"	TEXT,
                                                      "BrojTelefona"	TEXT,
                                                      PRIMARY KEY("ID")
);
CREATE TABLE IF NOT EXISTS "Svjedok" (
                                         "ID"	INTEGER,
                                         "Ime"	TEXT,
                                         "Prezime"	TEXT,
                                         "BrojTelefona"	TEXT,
                                         "Email"	TEXT,
                                         PRIMARY KEY("ID")
);
CREATE TABLE IF NOT EXISTS "IzjavaSvjedoka" (
                                                "ID"	INTEGER,
                                                "IDSvjedoka"	INTEGER,
                                                "Izjava"	TEXT,
                                                PRIMARY KEY("ID")
);
CREATE TABLE IF NOT EXISTS "Izvjestaj" (
                                           "ID"	INTEGER,
                                           "inspektorID"	INTEGER,
                                           "obrazovnaInstitucijaID"	INTEGER,
                                           "IzjavaPrvogSvjedokaID"	INTEGER,
                                           "IzjavaDrugogSvjedokaID"	INTEGER,
                                           "Opis"	TEXT,
                                           "DatumIVrijeme"	TEXT,
                                           PRIMARY KEY("ID")
);
INSERT INTO Korisnik(username,password,administrator) VALUES ("admin","admin",1);
COMMIT;
