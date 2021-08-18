package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.KorisnikDAO;
import ba.unsa.etf.rpr.models.Korisnik;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;

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
            try {
                uspjesanLogin(korisnik);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void uspjesanLogin(Korisnik korisnik) throws IOException {
        Stage adminPanel = new Stage();
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin_panel.fxml"),bundle);
        loader.setController(new AdminController(korisnik));
        adminPanel.setTitle(bundle.getString("adminNaslov"));
        adminPanel.setScene(new Scene(loader.load(),USE_COMPUTED_SIZE,400));
        adminPanel.show();
    }
}
