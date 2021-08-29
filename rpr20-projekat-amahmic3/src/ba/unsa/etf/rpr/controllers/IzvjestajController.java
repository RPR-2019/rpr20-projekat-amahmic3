package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.models.IzjavaSvjedoka;
import ba.unsa.etf.rpr.models.Izvještaj;
import ba.unsa.etf.rpr.models.Korisnik;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.function.Function;
import java.util.function.Predicate;

public class IzvjestajController {
    private Izvještaj izvještaj;
    public TextField fldNazivInstitucije;
    public TextField fldTelefonInstitucije;
    public TextField fldAdresaInstitucije;
    public TextField fldPostanskiBroj;
    public TextArea fldIzvjestaj;
    public TextField fldImePrvog;
    public TextField fldPrezimePrvog;
    public TextField fldEmailPrvog;
    public TextField fldBrojPrvog;
    public TextArea fldIzjavaPrvog;
    public TextField fldImeDrugog;
    public TextField fldPrezimeDrugog;
    public TextField fldEmailDrugog;
    public TextField fldBrojDrugog;
    public TextArea fldIzjavaDrugog;
    public TextField fldSati;
    public TextField fldMinute;
    public DatePicker fldDatum;
    public ImageView loadingImg;
    public ContextMenu fldSuggestionMenu;
    public Button btnCreate;
    private Boolean trebaKreirati = false;
    public Parent root;
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
               if(fldPostanskiBroj.textProperty().getValue() != null&&!fldPostanskiBroj.textProperty().getValue().isEmpty())
                validirajPostanskiBroj(fldPostanskiBroj.textProperty().getValue());
            }
        });
        fldDatum.valueProperty().addListener((obs,stari,novi)->{
            if(novi!=null){
                fldDatum.getStyleClass().removeAll("poljeNijeIspravno");
                fldDatum.getStyleClass().add("poljeIspravno");
                if(sviValidni()) btnCreate.setDisable(false);
            }else{
                fldDatum.getStyleClass().removeAll("poljeIspravno");
                fldDatum.getStyleClass().add("poljeNijeIspravno");
                btnCreate.setDisable(true);
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
    private void fldNazivInstitucijeListener(Observable obs, String oldNaziv, String newNaziv){

    }
    private void bindSvjedoka(TextField fldIme, TextField fldPrezime, TextField fldEmail, TextField fldBroj, TextArea fldIzjava, IzjavaSvjedoka izjavaSvjedoka) {
        fldIme.textProperty().bindBidirectional(izjavaSvjedoka.getSvjedok().imeProperty());
        fldPrezime.textProperty().bindBidirectional(izjavaSvjedoka.getSvjedok().prezimeProperty());
        fldEmail.textProperty().bindBidirectional(izjavaSvjedoka.getSvjedok().emailProperty());
        fldBroj.textProperty().bindBidirectional(izjavaSvjedoka.getSvjedok().brojTelefonaProperty());
        fldIzjava.textProperty().bindBidirectional(izjavaSvjedoka.tekstIzjaveProperty());
    }
    private void onChangeListener(TextInputControl textField){
        textField.textProperty().addListener((obs,novo,staro)->{
            textField.getStyleClass().removeAll("poljeIspravno");
            textField.getStyleClass().removeAll("poljeNijeIspravno");
            btnCreate.setDisable(true);
        });
    }
    public void validacijskiListener(TextInputControl textField, Predicate<String> predikat)
    {
        textField.focusedProperty().addListener((obs,oldState,newState)->{
            if(!newState){
                if(textField.textProperty().getValue()==null || textField.textProperty().getValue().isEmpty()){
                    textField.getStyleClass().removeAll("poljeIspravno");
                    textField.getStyleClass().removeAll("poljeNijeIspravno");
                    btnCreate.setDisable(true);
                    return;
                }
                if(predikat.test(textField.textProperty().getValue())){
                    textField.getStyleClass().removeAll("poljeIspravno");
                    textField.getStyleClass().add("poljeNijeIspravno");
                    btnCreate.setDisable(true);
                }else{
                    textField.getStyleClass().removeAll("poljeNijeIspravno");
                    textField.getStyleClass().add("poljeIspravno");
                    if(sviValidni()) btnCreate.setDisable(false);
                }
            }
        });
    }
    private boolean sviValidni() {
        Function<Node,Boolean> validacija = CreateKorisnikController::validan;
        return validacija.apply(fldNazivInstitucije) && validacija.apply(fldAdresaInstitucije) && validacija.apply(fldPostanskiBroj) && validacija.apply(fldTelefonInstitucije)&& validacija.apply(fldIzvjestaj)&&
        validacija.apply(fldImePrvog) && validacija.apply(fldPrezimePrvog) && validacija.apply(fldEmailPrvog) && validacija.apply(fldBrojPrvog) && validacija.apply(fldIzjavaPrvog) &&
        validacija.apply(fldImeDrugog) && validacija.apply(fldPrezimeDrugog) && validacija.apply(fldEmailDrugog) && validacija.apply(fldBrojDrugog) && validacija.apply(fldIzjavaDrugog) &&
        validacija.apply(fldDatum) && validacija.apply(fldSati) && validacija.apply(fldMinute);
    }
    public void kreirajIzvjestaj(ActionEvent actionEvent){
        trebaKreirati=true;
        izvještaj.setDatumIzvještaja(LocalDateTime.of(fldDatum.getValue(), LocalTime.of(Integer.parseInt(fldSati.textProperty().getValue()),Integer.parseInt(fldMinute.textProperty().getValue()))));
        ((Stage)root.getScene().getWindow()).close();
    }

    public Boolean getTrebaKreirati() {
        return trebaKreirati;
    }

    public Izvještaj getIzvještaj() {
        return izvještaj;
    }
}
