package ba.unsa.etf.rpr.models;

public class IzjavaSvjedoka {
    private int id;
    private int idSvjedoka;
    private String tekstIzjave;

    public IzjavaSvjedoka(int id, int idSvjedoka, String tekstIzjave) {
        this.id = id;
        this.idSvjedoka = idSvjedoka;
        this.tekstIzjave = tekstIzjave;
    }

    public IzjavaSvjedoka() {
        id=0;
        idSvjedoka=0;
        tekstIzjave="";
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdSvjedoka(int idSvjedoka) {
        this.idSvjedoka = idSvjedoka;
    }

    public void setTekstIzjave(String tekstIzjave) {
        this.tekstIzjave = tekstIzjave;
    }

    public int getId() {
        return id;
    }

    public int getIdSvjedoka() {
        return idSvjedoka;
    }

    public String getTekstIzjave() {
        return tekstIzjave;
    }
}
