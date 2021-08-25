package ba.unsa.etf.rpr.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Izvještaj {
    private SimpleIntegerProperty id;
    private Korisnik inspektor;
    private ObrazovnaInstitucija obrazovnaInstitucija;
    private SimpleStringProperty tekstIzvještaja;
    private LocalDateTime datumIzvještaja;
    private ArrayList<IzjavaSvjedoka> izjaveSvjedoka;

    public Izvještaj(int id, Korisnik inspektor, ObrazovnaInstitucija obrazovnaInstitucija, String tekstIzvještaja, LocalDateTime datumIzvještaja, IzjavaSvjedoka[] izjaveSvjedoka) {
        this.id = new SimpleIntegerProperty(id);
        this.inspektor= inspektor;
        this.obrazovnaInstitucija = obrazovnaInstitucija;
        this.tekstIzvještaja = new SimpleStringProperty(tekstIzvještaja);
        this.datumIzvještaja = datumIzvještaja;
        this.izjaveSvjedoka= new ArrayList<>();
        for(var izjavaSvjedoka:izjaveSvjedoka){
            this.izjaveSvjedoka.add(izjavaSvjedoka);
        }
    }

    public Izvještaj() {
        id = new SimpleIntegerProperty();
        obrazovnaInstitucija= new ObrazovnaInstitucija();
        tekstIzvještaja = new SimpleStringProperty();
        datumIzvještaja = LocalDateTime.now();
        izjaveSvjedoka = new ArrayList<>();
        izjaveSvjedoka.add(new IzjavaSvjedoka());
        izjaveSvjedoka.add(new IzjavaSvjedoka());
    }

    public Korisnik getInspektor() {
        return inspektor;
    }

    public void setInspektor(Korisnik inspektor) {
        this.inspektor = inspektor;
    }

    public ObrazovnaInstitucija getObrazovnaInstitucija() {
        return obrazovnaInstitucija;
    }

    public void setObrazovnaInstitucija(ObrazovnaInstitucija obrazovnaInstitucija) {
        this.obrazovnaInstitucija = obrazovnaInstitucija;
    }

    public ArrayList<IzjavaSvjedoka> getIzjaveSvjedoka() {
        return izjaveSvjedoka;
    }

    public void setIzjaveSvjedoka(ArrayList<IzjavaSvjedoka> izjaveSvjedoka) {
        this.izjaveSvjedoka = izjaveSvjedoka;
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
    public IzjavaSvjedoka getPrvi(){
        return izjaveSvjedoka.get(0);
    }
    public IzjavaSvjedoka getDrugi(){
        return izjaveSvjedoka.get(1);
    }

}
