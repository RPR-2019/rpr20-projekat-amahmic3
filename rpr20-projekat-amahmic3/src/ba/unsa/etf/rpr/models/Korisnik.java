package ba.unsa.etf.rpr.models;

public class Korisnik {
    private int id;
    private String ime,prezime,brojTelefona,email;
    private boolean administrator;

    public Korisnik(int id, String ime, String prezime, String brojTelefona, String email, boolean administrator) {
        this.id = id;
        this.ime = ime;
        this.prezime = prezime;
        this.brojTelefona = brojTelefona;
        this.email = email;

        this.administrator = administrator;
    }

    public int getId() {
        return id;
    }

    public String getIme() {
        return ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public String getBrojTelefona() {
        return brojTelefona;
    }

    public String getEmail() {
        return email;
    }

    public boolean isAdministrator() {
        return administrator;
    }
}
