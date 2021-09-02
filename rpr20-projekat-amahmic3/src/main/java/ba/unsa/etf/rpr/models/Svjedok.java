package ba.unsa.etf.rpr.models;

import javafx.beans.property.SimpleStringProperty;

public class Svjedok {
    private SimpleStringProperty ime,prezime,email,brojTelefona;

    public Svjedok(String ime, String prezime, String email, String brojTelefona) {
        this.ime = new SimpleStringProperty(ime);
        this.prezime = new SimpleStringProperty(prezime);
        this.email = new SimpleStringProperty(email);
        this.brojTelefona = new SimpleStringProperty(brojTelefona);
    }

    public Svjedok() {
        ime = new SimpleStringProperty("");
        prezime = new SimpleStringProperty("");
        email= new SimpleStringProperty("");
        brojTelefona=new SimpleStringProperty("");
    }
    public String getIme() {
        return ime.get();
    }

    public SimpleStringProperty imeProperty() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime.set(ime);
    }

    public String getPrezime() {
        return prezime.get();
    }

    public SimpleStringProperty prezimeProperty() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime.set(prezime);
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getBrojTelefona() {
        return brojTelefona.get();
    }

    public SimpleStringProperty brojTelefonaProperty() {
        return brojTelefona;
    }

    public void setBrojTelefona(String brojTelefona) {
        this.brojTelefona.set(brojTelefona);
    }
}
