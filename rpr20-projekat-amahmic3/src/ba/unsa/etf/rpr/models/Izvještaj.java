package ba.unsa.etf.rpr.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Izvještaj {
    private SimpleIntegerProperty id;
    private SimpleObjectProperty inspektor;
    private SimpleObjectProperty obrazovnaInstitucija;
    private SimpleStringProperty tekstIzvještaja;
    private LocalDateTime datumIzvještaja;
    private ArrayList<SimpleObjectProperty> izjaveSvjedoka;

    public Izvještaj(int id, int idInspektora, int idObrazovneInstitucije, String tekstIzvještaja, LocalDateTime datumIzvještaja, IzjavaSvjedoka[] izjaveSvjedoka) {
        this.id = new SimpleIntegerProperty(id)
        this.inspektor= new SimpleObjectProperty(idInspektora);
        this.obrazovnaInstitucija = new SimpleObjectProperty( idObrazovneInstitucije);
        this.tekstIzvještaja = new SimpleStringProperty(tekstIzvještaja);
        this.datumIzvještaja = datumIzvještaja;
        this.izjaveSvjedoka= new ArrayList<>();
        for(var izjavaSvjeoka:izjaveSvjedoka){
            this.izjaveSvjedoka.add(new SimpleObjectProperty(izjavaSvjeoka));
        }
    }

    public Izvještaj() {
        id = new SimpleIntegerProperty();
        inspektor = new SimpleObjectProperty();
        obrazovnaInstitucija= new SimpleObjectProperty();
        tekstIzvještaja = new SimpleStringProperty();
        datumIzvještaja = LocalDateTime.now();
        izjaveSvjedoka = new ArrayList<>();
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

    public Object getInspektor() {
        return inspektor.get();
    }

    public SimpleObjectProperty inspektorProperty() {
        return inspektor;
    }

    public void setInspektor(Object inspektor) {
        this.inspektor.set(inspektor);
    }

    public Object getObrazovnaInstitucija() {
        return obrazovnaInstitucija.get();
    }

    public SimpleObjectProperty obrazovnaInstitucijaProperty() {
        return obrazovnaInstitucija;
    }

    public void setObrazovnaInstitucija(Object obrazovnaInstitucija) {
        this.obrazovnaInstitucija.set(obrazovnaInstitucija);
    }

    public String getTekstIzvještaja() {
        return tekstIzvještaja.get();
    }

    public SimpleStringProperty tekstIzvještajaProperty() {
        return tekstIzvještaja;
    }

    public void setTekstIzvještaja(String tekstIzvještaja) {
        this.tekstIzvještaja.set(tekstIzvještaja);
    }

    public LocalDateTime getDatumIzvještaja() {
        return datumIzvještaja;
    }

    public void setDatumIzvještaja(LocalDateTime datumIzvještaja) {
        this.datumIzvještaja = datumIzvještaja;
    }

    public ArrayList<SimpleObjectProperty> getIzjaveSvjedoka() {
        return izjaveSvjedoka;
    }

    public void setIzjaveSvjedoka(ArrayList<SimpleObjectProperty> izjaveSvjedoka) {
        this.izjaveSvjedoka = izjaveSvjedoka;
    }
}
