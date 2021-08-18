package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.models.Korisnik;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class AdminController {
    private Korisnik admin=null;
    public Button btnInspektori;
    private Button odabraniButton;
    public GridPane inspektoriPane;
    public GridPane izvjestajiPane;
    public Button btnIzvjestaji;
    public AdminController(Korisnik korisnik){
        admin=korisnik;
    }
    @FXML
    public void initialize(){
        odabraniButton = btnInspektori;
        izvjestajiPane.setVisible(false);
        inspektoriPane.setVisible(true);
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
    public void prikaziInspektore(ActionEvent actionEvent){
        odabraniButton = btnInspektori;
        izvjestajiPane.setVisible(false);
        inspektoriPane.setVisible(true);
        btnInspektori.getStyleClass().removeAll("buttonNotSelected");
        btnInspektori.getStyleClass().removeAll("buttonHovered");
        btnIzvjestaji.getStyleClass().removeAll("buttonSelected");
        btnInspektori.getStyleClass().add("buttonSelected");
    }
    public void prikaziIzvjestaje(ActionEvent actionEvent){
        odabraniButton = btnIzvjestaji;
        izvjestajiPane.setVisible(true);
        inspektoriPane.setVisible(false);
        btnIzvjestaji.getStyleClass().removeAll("buttonNotSelected");
        btnIzvjestaji.getStyleClass().removeAll("buttonHovered");
        btnInspektori.getStyleClass().removeAll("buttonSelected");
        btnIzvjestaji.getStyleClass().add("buttonSelected");
    }
}
