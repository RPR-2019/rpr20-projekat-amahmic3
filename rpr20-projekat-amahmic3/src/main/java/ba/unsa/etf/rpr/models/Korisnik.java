package ba.unsa.etf.rpr.models;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Korisnik {
    private SimpleIntegerProperty id= new SimpleIntegerProperty(0);
    private SimpleStringProperty ime=new SimpleStringProperty("");
    private SimpleStringProperty prezime= new SimpleStringProperty("");
    private SimpleStringProperty brojTelefona = new SimpleStringProperty("");
    private SimpleStringProperty email = new SimpleStringProperty("");
    private SimpleStringProperty username = new SimpleStringProperty("");
    private SimpleStringProperty password = new SimpleStringProperty("");
    private SimpleBooleanProperty administrator= new SimpleBooleanProperty(false);

    public Korisnik(){
       id.setValue(0);
       ime.setValue("");
       prezime.setValue("");
       brojTelefona.setValue("");
       email.setValue("");
       username.setValue("");
       password.setValue("");
       administrator.setValue(false);
   }
   public Korisnik(String ime,String prezime,String brojTelefona,String email,String username,String password){
       this.ime.setValue(ime);
       this.prezime.setValue(prezime);
       this.brojTelefona.setValue(brojTelefona);
       this.email.setValue(email);
       this.username.setValue(username);
       this.password.setValue(password);
   }
    public Korisnik(int id,String ime,String prezime,String brojTelefona,String email,boolean administrator,String username,String password){
       setId(id);
       setIme(ime);
       setPrezime(prezime);
       setBrojTelefona(brojTelefona);
       setEmail(email);
       setAdministrator(administrator);
       setUsername(username);
       setPassword(password);
    }


    public String getIme() {
        return ime.get();
    }

    public SimpleStringProperty imeProperty() {
        return ime;
    }

    public void setIme(String ime) {
        if(ime!=null)
        this.ime.set(ime);
    }

    public String getPrezime() {
        return prezime.get();
    }

    public SimpleStringProperty prezimeProperty() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        if(prezime!=null)
        this.prezime.set(prezime);
    }

    public String getBrojTelefona() {
        return brojTelefona.get();
    }

    public SimpleStringProperty brojTelefonaProperty() {
        return brojTelefona;
    }

    public void setBrojTelefona(String brojTelefona) {
        if(brojTelefona!=null)
        this.brojTelefona.set(brojTelefona);
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        if(email!=null)
        this.email.set(email);
    }

    public String getUsername() {
        return username.get();
    }

    public SimpleStringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        if(username!=null)
        this.username.set(username);
    }

    public String getPassword() {
        return password.get();
    }

    public SimpleStringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        if(password!=null)
        this.password.set(password);
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

    public boolean isAdministrator() {
        return administrator.get();
    }

    public SimpleBooleanProperty administratorProperty() {
        return administrator;
    }

    public void setAdministrator(boolean administrator) {
        this.administrator.set(administrator);
    }
}
