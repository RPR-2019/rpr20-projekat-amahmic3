package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.KorisnikDAO;
import ba.unsa.etf.rpr.models.Korisnik;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.util.ResourceBundle;

public class LoginController {
    public TextField fldUsername;
    public TextField fldPassword;
    private KorisnikDAO dao;
    private ResourceBundle bundle = ResourceBundle.getBundle("Translation");
    public void loginAction(ActionEvent actionEvent){
        dao = KorisnikDAO.getInstance();
        Korisnik korisnik = dao.dajKorisnika(fldUsername.textProperty().getValue(),fldPassword.textProperty().getValue());
        if (korisnik == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(bundle.getString("pogresnaSifraNaslov"));
            alert.setHeaderText(bundle.getString("pogresnaSifraNaslov"));
            alert.setContentText(bundle.getString("loginError"));
            alert.showAndWait();
        }else{

        }
    }
}
