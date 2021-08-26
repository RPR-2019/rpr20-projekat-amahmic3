package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.models.IzjavaSvjedoka;
import ba.unsa.etf.rpr.models.Izvještaj;
import ba.unsa.etf.rpr.models.Korisnik;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.function.Predicate;

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
    public ImageView loadingImg;
    public ContextMenu fldSuggestionMenu;
    public Button btnCreate;

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
        fldNazivInstitucije.textProperty().addListener(this::fldNazivInstitucijeListener);
        onChangeListener(fldNazivInstitucije);
        onChangeListener(fldAdresaInstitucije);
        onChangeListener(fldTelefonInstitucije);
        onChangeListener(fldPostanskiBroj);
        onChangeListener(fldIzvjestaj);
        onChangeListener(fldImePrvog);
        onChangeListener(fldPrezimePrvog);
        onChangeListener(fldEmailPrvog);
        onChangeListener(fldBrojPrvog);
        onChangeListener(fldIzjavaPrvog);
        onChangeListener(fldImeDrugog);
        onChangeListener(fldPrezimeDrugog);
        onChangeListener(fldEmailDrugog);
        onChangeListener(fldBrojDrugog);
        onChangeListener(fldIzjavaDrugog);

        validacijskiListener(fldNazivInstitucije,String::isEmpty);
        validacijskiListener(fldAdresaInstitucije,String::isEmpty);
        validacijskiListener(fldTelefonInstitucije, CreateKorisnikController::validirajTelefon);
        validacijskiListener(fldIzvjestaj,String::isEmpty);
        validacijskiListener(fldImePrvog,String::isEmpty);
        validacijskiListener(fldPrezimePrvog,String::isEmpty);
        validacijskiListener(fldEmailPrvog, CreateKorisnikController::validirajEmail);
        validacijskiListener(fldBrojPrvog,s-> CreateKorisnikController.validirajTelefon(s));
        validacijskiListener(fldIzjavaPrvog,String::isEmpty);
        validacijskiListener(fldImeDrugog,String::isEmpty);
        validacijskiListener(fldPrezimeDrugog,String::isEmpty);
       validacijskiListener(fldEmailDrugog,s -> CreateKorisnikController.validirajEmail(s));
        validacijskiListener(fldBrojDrugog,s-> CreateKorisnikController.validirajTelefon(s));
        validacijskiListener(fldIzjavaDrugog,String::isEmpty);
        fldPostanskiBroj.focusedProperty().addListener((obs,stari,novi)->{
            if(!novi){
                validirajPostanskiBroj(fldPostanskiBroj.textProperty().getValue());
            }
        });
        fldDatum.valueProperty().addListener((obs,stari,novi)->{
            if(novi!=null){
                fldDatum.getStyleClass().removeAll("poljeNijeIspravno");
                fldDatum.getStyleClass().add("poljeIspravno");
            }else{
                fldDatum.getStyleClass().removeAll("poljeIspravno");
                fldDatum.getStyleClass().add("poljeNijeIspravno");
            }
        });
        validacijskiListener(fldSati,this::validirajSate);
        validacijskiListener(fldMinute,this::validirajMinute);
    }
    private boolean validirajSate(String sati){
        return !sati.matches("0?[0-9]|1[0-9]|2[0-3]");
    }
    private boolean validirajMinute(String minute){
        return !minute.matches("[0-5]?[0-9]");
    }
    private void validirajPostanskiBroj(String postanskiBroj){
        Task<Boolean> posao = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                try {
                    Platform.runLater(()->{
                        loadingImg.setVisible(true);
                    });
                    URL url = new URL("https://c9.etf.unsa.ba/proba/postanskiBroj.php?postanskiBroj=" + postanskiBroj);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    InputStream is = httpURLConnection.getInputStream();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(is));

                    if (rd.readLine().matches("NOT+ \\w+")) {
                       return false;
                    } else {
                        return true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }
        };
        posao.setOnSucceeded((event)->{
            if(posao.getValue()){
                Platform.runLater(()->{
                    loadingImg.setVisible(false);
                    fldPostanskiBroj.getStyleClass().removeAll("poljeNijeIspravno");
                    fldPostanskiBroj.getStyleClass().add("poljeIspravno");
                });
            }else{
                Platform.runLater(()->{
                    loadingImg.setVisible(false);
                    fldPostanskiBroj.getStyleClass().removeAll("poljeIspravno");
                    fldPostanskiBroj.getStyleClass().add("poljeNijeIspravno");
                });
            }
        });
        new Thread(posao).start();

    }
    private void dateListener(Observable obs, LocalDate oldDate, LocalDate newDate){
        fldDatum.getStyleClass().add("poljeIspravno");

    }
    private void fldNazivInstitucijeListener(Observable obs, String oldNaziv, String newNaziv){

    }
    private void bindSvjedoka(TextField fldIme, TextField fldPrezime, TextField fldEmail, TextField fldBroj, TextField fldIzjava, IzjavaSvjedoka izjavaSvjedoka) {
        fldIme.textProperty().bindBidirectional(izjavaSvjedoka.getSvjedok().imeProperty());
        fldPrezime.textProperty().bindBidirectional(izjavaSvjedoka.getSvjedok().prezimeProperty());
        fldEmail.textProperty().bindBidirectional(izjavaSvjedoka.getSvjedok().emailProperty());
        fldBroj.textProperty().bindBidirectional(izjavaSvjedoka.getSvjedok().brojTelefonaProperty());
        fldIzjava.textProperty().bindBidirectional(izjavaSvjedoka.tekstIzjaveProperty());
    }
    private void onChangeListener(TextField textField){
        textField.textProperty().addListener((obs,novo,staro)->{
            textField.getStyleClass().removeAll("poljeIspravno");
            textField.getStyleClass().removeAll("poljeNijeIspravno");
            btnCreate.setDisable(true);
        });
    }
    public void validacijskiListener(TextField textField, Predicate<String> predikat)
    {
        textField.focusedProperty().addListener((obs,oldState,newState)->{
            if(!newState){
                if(textField.textProperty().getValue()==null){
                    return;
                }
                if(predikat.test(textField.textProperty().getValue())){
                    textField.getStyleClass().removeAll("poljeIspravno");
                    textField.getStyleClass().add("poljeNijeIspravno");
                    btnCreate.setDisable(true);
                }else{
                    textField.getStyleClass().removeAll("poljeNijeIspravno");
                    textField.getStyleClass().add("poljeIspravno");
                  //  if(sviValidni()) btnCreate.setDisable(false);
                }
            }
        });
    }
}
