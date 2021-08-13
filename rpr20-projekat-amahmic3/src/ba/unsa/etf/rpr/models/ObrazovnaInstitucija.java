package ba.unsa.etf.rpr.models;

public class ObrazovnaInstitucija {
    private int id;
    private String naziv,adresa,brojTelefona,email,postanskiBroj;

    public ObrazovnaInstitucija(int id, String naziv, String adresa, String brojTelefona, String email, String postanskiBroj) {
        this.id = id;
        this.naziv = naziv;
        this.adresa = adresa;
        this.brojTelefona = brojTelefona;
        this.email = email;
        this.postanskiBroj = postanskiBroj;
    }

    public int getId() {
        return id;
    }

    public String getNaziv() {
        return naziv;
    }

    public String getAdresa() {
        return adresa;
    }

    public String getBrojTelefona() {
        return brojTelefona;
    }

    public String getEmail() {
        return email;
    }

    public String getPostanskiBroj() {
        return postanskiBroj;
    }
}
