package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.KorisnikDAO;
import ba.unsa.etf.rpr.models.Korisnik;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.validator.routines.EmailValidator;

import java.util.ResourceBundle;
import java.util.function.Predicate;

public class CreateKorisnikController {
    private Korisnik noviKorisnik = new Korisnik();
    public Parent rootPane;
    public TextField fldIme;
    public TextField fldPrezime;
    public TextField fldEmail;
    public TextField fldBrojTelefona;
    public TextField fldUsername;
    public TextField fldPassword;
    public Button btnCreate;
    private boolean trebaKreirati = false;
    private ResourceBundle bundle = ResourceBundle.getBundle("Translation");
    private boolean validirajEmail(String email){
        return EmailValidator.getInstance().isValid(email);
    }
    @FXML
    public void initialize(){
        onChangeListener(fldIme);
        onChangeListener(fldPrezime);
        onChangeListener(fldEmail);
        onChangeListener(fldBrojTelefona);
        onChangeListener(fldUsername);
        onChangeListener(fldPassword);

        fldIme.textProperty().bindBidirectional(noviKorisnik.imeProperty());
        fldPrezime.textProperty().bindBidirectional(noviKorisnik.prezimeProperty());
        fldEmail.textProperty().bindBidirectional(noviKorisnik.emailProperty());
        fldBrojTelefona.textProperty().bindBidirectional(noviKorisnik.brojTelefonaProperty());
        fldUsername.textProperty().bindBidirectional(noviKorisnik.usernameProperty());
        fldPassword.textProperty().bindBidirectional(noviKorisnik.passwordProperty());

        validacijskiListener(fldIme, String::isEmpty);
        validacijskiListener(fldPrezime, String::isEmpty);
        validacijskiListener(fldEmail,s -> !validirajEmail(s));
        validacijskiListener(fldBrojTelefona, s -> !validirajTelefon(s));
        validacijskiListener(fldUsername,s->!validirajUsername(s));
        validacijskiListener(fldPassword,String::isEmpty);
        btnCreate.setDisable(true);
    }
    private void onChangeListener(TextField textField){
        textField.textProperty().addListener((obs,novo,staro)->{
            textField.getStyleClass().removeAll("poljeIspravno");
            textField.getStyleClass().removeAll("poljeNijeIspravno");
        });
    }
    public boolean validirajUsername(String username){
         return KorisnikDAO.getInstance().provjeriUsername(username) && !username.isEmpty();
    }
    public boolean validirajTelefon(String brTelefona){
        return brTelefona.matches("[0-9]+");
    }
    public void validacijskiListener(TextField textField, Predicate<String> predikat)
    {
        textField.focusedProperty().addListener((obs,oldState,newState)->{
            if(!newState){
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
        return validan(fldIme) && validan(fldPrezime) && validan(fldEmail) && validan(fldBrojTelefona) && validan(fldUsername) && validan(fldPassword);
    }
    private boolean validan(TextField textField){
        return textField.getStyleClass().contains("poljeIspravno");
    }
    public void generisiUsername(ActionEvent actionEvent){
        if(fldIme.textProperty().getValue().isEmpty() || fldPrezime.textProperty().getValue().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(bundle.getString("nemaImenaIliPrezimenaNaslov"));
            alert.setHeaderText(bundle.getString("nemaImenaIliPrezimenaNaslov"));
            alert.setContentText(bundle.getString("imePrezimeError"));
            alert.showAndWait();
        }else{
            String prefiks = fldIme.textProperty().getValue().toLowerCase().charAt(0)+fldPrezime.textProperty().getValue().toLowerCase();
            int broj = KorisnikDAO.getInstance().generisiUsername(prefiks+"%");
            fldUsername.textProperty().setValue(prefiks+broj);
            fldUsername.getStyleClass().removeAll("poljeNijeIspravno");
            fldUsername.getStyleClass().add("poljeIspravno");
            if(sviValidni()) btnCreate.setDisable(false);
        }
    }
    public void kreirajKorisnika(ActionEvent actionEvent){
        trebaKreirati= true;
        ((Stage)rootPane.getScene().getWindow()).close();
    }
    public boolean isTrebaKreirati(){
        return trebaKreirati;
    }
    public Korisnik getNoviKorisnik() {
        return noviKorisnik;
    }

}