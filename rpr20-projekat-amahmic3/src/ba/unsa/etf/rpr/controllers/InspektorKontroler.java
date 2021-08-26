package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.KorisnikDAO;
import ba.unsa.etf.rpr.models.Izvještaj;
import ba.unsa.etf.rpr.models.Korisnik;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;

public class InspektorKontroler {
    Korisnik inspektor;

    public InspektorKontroler(Korisnik inspektor) {
        this.inspektor = inspektor;
    }
    public void kreirajIzvjestaj(ActionEvent actionEvent) throws IOException {
        Stage createWindow = new Stage();
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/izvjestaj.fxml"),bundle);
        loader.setController(new IzvjestajController(inspektor));
        createWindow.setTitle(bundle.getString("create"));
        createWindow.setScene(new Scene(loader.load(),USE_COMPUTED_SIZE,USE_COMPUTED_SIZE));
        createWindow.show();
        createWindow.setOnHiding((e)->{
            IzvjestajController ctrl = loader.getController();
            Izvještaj izvještaj = ctrl.getIzvještaj();
            KorisnikDAO.getInstance().upisiIzvjestaj(izvještaj);
        });
    }
    public void prikaziInspektore(ActionEvent actionEvent){

    }
    public  void prikaziIzvjestaje(ActionEvent actionEvent){

    }
    public void obojiUlaz(MouseEvent actionEvent){
        Button btn = (Button)actionEvent.getSource();
        btn.getStyleClass().removeAll("buttonNotSelected");
        btn.getStyleClass().add("buttonHovered");
    }
    public void obojiIzlaz(MouseEvent actionEvent){
        Button btn = (Button)actionEvent.getSource();
        btn.getStyleClass().removeAll("buttonHovered");
        btn.getStyleClass().add("buttonNotSelected");
    }
}
