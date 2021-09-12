BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "Korisnik" (
                                          "id"	INTEGER UNIQUE,
                                          "username"	TEXT,
                                          "password"	TEXT,
                                          "ime"	TEXT,
                                          "prezime"	TEXT,
                                          "email"	TEXT,
                                          "brojTelefona"	TEXT,
                                          "administrator"	INTEGER,
                                          "obrisan" INTEGER,
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
CREATE TABLE IF NOT EXISTS "IzjavaSvjedoka" (
                                                "ID"	INTEGER,
                                                "Ime"	TEXT,
                                                "Prezime"	TEXT,
                                                "Email"	TEXT,
                                                "BrojTelefona"	TEXT,
                                                "Izjava"	TEXT,
                                                PRIMARY KEY("ID")
    );
INSERT INTO Korisnik(username,password,administrator,obrisan) VALUES ("admin","admin",1,0);
COMMIT;
