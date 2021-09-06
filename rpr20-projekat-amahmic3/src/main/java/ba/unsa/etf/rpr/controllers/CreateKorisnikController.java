package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.KorisnikDAO;
import ba.unsa.etf.rpr.models.Korisnik;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.validator.routines.EmailValidator;

import java.util.ResourceBundle;
import java.util.function.Predicate;

public class CreateKorisnikController {
    private Korisnik noviKorisnik;
    public Parent rootPane;
    public TextField fldIme;
    public TextField fldPrezime;
    public TextField fldEmail;
    public TextField fldBrojTelefona;
    public TextField fldUsername;
    public TextField fldPassword;
    public Button btnCreate;
    public CheckBox chBoxAdministrator;
    private boolean trebaKreirati = false;
    private boolean edit = false;
    private final ResourceBundle bundle = ResourceBundle.getBundle("Translation");
    private  boolean validirajEmail(String email){
        return !EmailValidator.getInstance().isValid(email) || KorisnikDAO.getInstance().postojiLiEmail(email,noviKorisnik.getId());
    }

    public CreateKorisnikController() {
        noviKorisnik=new Korisnik();
    }

    public CreateKorisnikController(Korisnik noviKorisnik) {
        this.noviKorisnik = noviKorisnik;
        edit = true;
    }

    @FXML
    public void initialize(){
        onChangeListener(fldIme);
        onChangeListener(fldPrezime);
        onChangeListener(fldEmail);
        onChangeListener(fldBrojTelefona);
        onChangeListener(fldUsername);
        onChangeListener(fldPassword);
        chBoxAdministrator.selectedProperty().bindBidirectional(noviKorisnik.administratorProperty());
        fldIme.textProperty().bindBidirectional(noviKorisnik.imeProperty());
        fldPrezime.textProperty().bindBidirectional(noviKorisnik.prezimeProperty());
        fldEmail.textProperty().bindBidirectional(noviKorisnik.emailProperty());
        fldBrojTelefona.textProperty().bindBidirectional(noviKorisnik.brojTelefonaProperty());
        fldUsername.textProperty().bindBidirectional(noviKorisnik.usernameProperty());
        fldPassword.textProperty().bindBidirectional(noviKorisnik.passwordProperty());

        validacijskiListener(fldIme, String::isEmpty);
        validacijskiListener(fldPrezime, String::isEmpty);
        validacijskiListener(fldEmail, this::validirajEmail);
        validacijskiListener(fldBrojTelefona, this::validirajTelefon);
        validacijskiListener(fldUsername,s->!validirajUsername(s));
        validacijskiListener(fldPassword,String::isEmpty);
        fldPassword.textProperty().addListener((obs,oldState,newState)->{
           if(newState.isEmpty()){
               fldPassword.getStyleClass().removeAll("poljeIspravno");
               btnCreate.setDisable(true);
           }else {
               fldPassword.getStyleClass().removeAll("poljeNijeIspravno");
               fldPassword.getStyleClass().add("poljeIspravno");
               if(btnCreate.isDisabled()&&sviValidni()) btnCreate.setDisable(false);
           }
        });

        if(edit){
            btnCreate.setDisable(false);
            btnCreate.setText(bundle.getString("edit"));
            fldIme.getStyleClass().add("poljeIspravno");
            fldPrezime.getStyleClass().add("poljeIspravno");
            fldPassword.getStyleClass().add("poljeIspravno");
            fldEmail.getStyleClass().add("poljeIspravno");
            fldUsername.getStyleClass().add("poljeIspravno");
            fldPassword.getStyleClass().add("poljeIspravno");
            fldBrojTelefona.getStyleClass().add("poljeIspravno");

        }else{
            btnCreate.setDisable(true);
        }
    }
    private void onChangeListener(TextField textField){
        textField.textProperty().addListener((obs,novo,staro)->{
            textField.getStyleClass().removeAll("poljeIspravno");
            textField.getStyleClass().removeAll("poljeNijeIspravno");
            btnCreate.setDisable(true);
        });
    }
    public boolean validirajUsername(String username){
         return KorisnikDAO.getInstance().provjeriUsername(username,noviKorisnik.getId()) && !username.isEmpty();
    }
    public boolean validirajTelefon(String brTelefona){
        return !brTelefona.matches("[0-9]+") || KorisnikDAO.getInstance().postojiLiBrojTelefona(brTelefona,noviKorisnik.getId());
    }

    public void validacijskiListener(TextField textField, Predicate<String> predikat)
    {
        textField.focusedProperty().addListener((obs,oldState,newState)->{
            if(!newState){
                if(textField.textProperty().getValue()==null || textField.textProperty().getValue().isEmpty() ){
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

    private Boolean sviValidni() {
        return validan(fldIme) && validan(fldPrezime) && validan(fldEmail) && validan(fldBrojTelefona) && validan(fldUsername) && validan(fldPassword);
    }
    public static boolean validan(Node textField){
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
        noviKorisnik.setAdministrator(chBoxAdministrator.isSelected());
        ((Stage)rootPane.getScene().getWindow()).close();
    }
    public boolean isTrebaKreirati(){
        return trebaKreirati;
    }
    public Korisnik getNoviKorisnik() {
        return noviKorisnik;
    }
    public boolean isEdit() {return edit;}

    public void cancelKreiranje(ActionEvent actionEvent){
        ((Stage)rootPane.getScene().getWindow()).close();
    }

}
