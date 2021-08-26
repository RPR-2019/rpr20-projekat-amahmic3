package ba.unsa.etf.rpr.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class IzjavaSvjedoka {
    private SimpleIntegerProperty id;
    private Svjedok svjedok;
    private SimpleStringProperty tekstIzjave;

    public IzjavaSvjedoka(int id, Svjedok svjedok, String tekstIzjave) {
        this.id = new SimpleIntegerProperty(id);
        this.svjedok = (svjedok);
        this.tekstIzjave = new SimpleStringProperty(tekstIzjave);
    }

    public IzjavaSvjedoka() {
        id = new SimpleIntegerProperty(0);
        svjedok = new Svjedok();
        tekstIzjave = new SimpleStringProperty("");
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

    public Svjedok getSvjedok() {
        return svjedok;
    }

    public void setSvjedok(Svjedok svjedok) {
        this.svjedok = svjedok;
    }

    public String getTekstIzjave() {
        return tekstIzjave.get();
    }

    public SimpleStringProperty tekstIzjaveProperty() {
        return tekstIzjave;
    }

    public void setTekstIzjave(String tekstIzjave) {
        this.tekstIzjave.set(tekstIzjave);
    }
}
