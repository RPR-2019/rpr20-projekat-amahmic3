package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.models.IzjavaSvjedoka;
import ba.unsa.etf.rpr.models.Izvještaj;
import ba.unsa.etf.rpr.models.Korisnik;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class IzvjestajController {
    Izvještaj izvještaj;
    public TextField fldNazivInstitucije;
    public TextField fldTelefonInstitucije;
    public TextField fldAdresaInstitucije;
    public TextField fldPostanskiBroj;
    public TextField fldIzvjestaj;
    public TextField fldImePrvog;
    public TextField fldPrezimePrvog;
    public TextField fldEmailPrvog;
    public TextField fldBrojPrvog;
    public TextField fldIzjavaPrvog;
    public TextField fldImeDrugog;
    public TextField fldPrezimeDrugog;
    public TextField fldEmailDrugog;
    public TextField fldBrojDrugog;
    public TextField fldIzjavaDrugog;
    public TextField fldSati;
    public TextField fldMinute;
    public DatePicker fldDatum;
    public IzvjestajController(Korisnik inspektor) {
        izvještaj = new Izvještaj();
        izvještaj.setInspektor(inspektor);

    }
    @FXML
    void initialize(){
        fldNazivInstitucije.textProperty().bindBidirectional(izvještaj.getObrazovnaInstitucija().nazivProperty());
        fldAdresaInstitucije.textProperty().bindBidirectional(izvještaj.getObrazovnaInstitucija().adresaProperty());
        fldTelefonInstitucije.textProperty().bindBidirectional(izvještaj.getObrazovnaInstitucija().brojTelefonaProperty());
        fldPostanskiBroj.textProperty().bindBidirectional(izvještaj.getObrazovnaInstitucija().postanskiBrojProperty());
        fldIzvjestaj.textProperty().bindBidirectional(izvještaj.tekstIzvještajaProperty());
        bindSvjedoka(fldImePrvog, fldPrezimePrvog, fldEmailPrvog, fldBrojPrvog, fldIzjavaPrvog,izvještaj.getPrvi());
        bindSvjedoka(fldImeDrugog, fldPrezimeDrugog, fldEmailDrugog, fldBrojDrugog, fldIzjavaDrugog, izvještaj.getDrugi());

    }

    private void bindSvjedoka(TextField fldIme, TextField fldPrezime, TextField fldEmail, TextField fldBroj, TextField fldIzjava, IzjavaSvjedoka izjavaSvjedoka) {
        fldIme.textProperty().bindBidirectional(izjavaSvjedoka.getSvjedok().imeProperty());
        fldPrezime.textProperty().bindBidirectional(izjavaSvjedoka.getSvjedok().prezimeProperty());
        fldEmail.textProperty().bindBidirectional(izjavaSvjedoka.getSvjedok().emailProperty());
        fldBroj.textProperty().bindBidirectional(izjavaSvjedoka.getSvjedok().brojTelefonaProperty());
        fldIzjava.textProperty().bindBidirectional(izjavaSvjedoka.tekstIzjaveProperty());
    }
}
