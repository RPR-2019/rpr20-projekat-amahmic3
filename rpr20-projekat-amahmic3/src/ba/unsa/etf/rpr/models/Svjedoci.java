package ba.unsa.etf.rpr.models;

public class Svjedoci {
    private int id;
    private String ime,prezime,email,brojTelefona;

    public Svjedoci(int id, String ime, String prezime, String email, String brojTelefona) {
        this.id = id;
        this.ime = ime;
        this.prezime = prezime;
        this.email = email;
        this.brojTelefona = brojTelefona;
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

    public String getEmail() {
        return email;
    }

    public String getBrojTelefona() {
        return brojTelefona;
    }
}
