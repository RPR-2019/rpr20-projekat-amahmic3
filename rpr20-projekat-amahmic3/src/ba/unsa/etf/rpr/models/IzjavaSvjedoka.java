package ba.unsa.etf.rpr.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class IzjavaSvjedoka {
    private SimpleIntegerProperty id;
    private SimpleObjectProperty svjedok;
    private SimpleStringProperty tekstIzjave;

    public IzjavaSvjedoka(int id, Svjedoci svjedok, String tekstIzjave) {
        this.id = new SimpleIntegerProperty(id);
        this.svjedok = new SimpleObjectProperty(svjedok);
        this.tekstIzjave = new SimpleStringProperty(tekstIzjave);
    }

    public IzjavaSvjedoka() {
        id = new SimpleIntegerProperty(0);
        svjedok = new SimpleObjectProperty();
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

    public Object getSvjedok() {
        return svjedok.get();
    }

    public SimpleObjectProperty svjedokProperty() {
        return svjedok;
    }

    public void setSvjedok(Object svjedok) {
        this.svjedok.set(svjedok);
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
