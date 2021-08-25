package ba.unsa.etf.rpr.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ObrazovnaInstitucija {
    private SimpleIntegerProperty id;
    private SimpleStringProperty naziv,adresa,brojTelefona,email,postanskiBroj;

    public ObrazovnaInstitucija(int id, String naziv, String adresa, String brojTelefona, String postanskiBroj) {
        this.id = new SimpleIntegerProperty(id);
        this.naziv = new SimpleStringProperty(naziv);
        this.adresa = new SimpleStringProperty(adresa);
        this.brojTelefona = new SimpleStringProperty(brojTelefona);
        this.postanskiBroj = new SimpleStringProperty(postanskiBroj);
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getNaziv() {
        return naziv.get();
    }

    public SimpleStringProperty nazivProperty() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv.set(naziv);
    }

    public String getAdresa() {
        return adresa.get();
    }

    public SimpleStringProperty adresaProperty() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa.set(adresa);
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

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getPostanskiBroj() {
        return postanskiBroj.get();
    }

    public SimpleStringProperty postanskiBrojProperty() {
        return postanskiBroj;
    }

    public void setPostanskiBroj(String postanskiBroj) {
        this.postanskiBroj.set(postanskiBroj);
    }
}
