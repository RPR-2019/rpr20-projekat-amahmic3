package ba.unsa.etf.rpr.models;

import java.time.LocalDateTime;

public class Izvještaj {
    private int id;
    private int idInspektora;
    private int idObrazovneInstitucije;
    private String tekstIzvještaja;
    private LocalDateTime datumIzvještaja;
    private IzjavaSvjedoka[] izjaveSvjedoka=new IzjavaSvjedoka[]{new IzjavaSvjedoka(),new IzjavaSvjedoka()};

    public Izvještaj(int id, int idInspektora, int idObrazovneInstitucije, String tekstIzvještaja, LocalDateTime datumIzvještaja, IzjavaSvjedoka[] izjaveSvjedoka) {
        this.id = id;
        this.idInspektora = idInspektora;
        this.idObrazovneInstitucije = idObrazovneInstitucije;
        this.tekstIzvještaja = tekstIzvještaja;
        this.datumIzvještaja = datumIzvještaja;
        this.izjaveSvjedoka = izjaveSvjedoka;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdInspektora() {
        return idInspektora;
    }

    public void setIdInspektora(int idInspektora) {
        this.idInspektora = idInspektora;
    }

    public int getIdObrazovneInstitucije() {
        return idObrazovneInstitucije;
    }

    public void setIdObrazovneInstitucije(int idObrazovneInstitucije) {
        this.idObrazovneInstitucije = idObrazovneInstitucije;
    }

    public String getTekstIzvještaja() {
        return tekstIzvještaja;
    }

    public void setTekstIzvještaja(String tekstIzvještaja) {
        this.tekstIzvještaja = tekstIzvještaja;
    }

    public LocalDateTime getDatumIzvještaja() {
        return datumIzvještaja;
    }

    public void setDatumIzvještaja(LocalDateTime datumIzvještaja) {
        this.datumIzvještaja = datumIzvještaja;
    }

    public IzjavaSvjedoka[] getIzjaveSvjedoka() {
        return izjaveSvjedoka;
    }

    public void setIzjaveSvjedoka(IzjavaSvjedoka[] izjaveSvjedoka) {
        this.izjaveSvjedoka = izjaveSvjedoka;
    }
}
