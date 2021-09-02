package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.KorisnikDAO;
import ba.unsa.etf.rpr.models.Izvještaj;
import ba.unsa.etf.rpr.models.Korisnik;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;

public class InspektorKontroler {
    Korisnik inspektor;
    public Pane profilPane;
    public Pane izvjestajiPane;
    private final ObservableList<Izvještaj> izvjestaji = FXCollections.observableArrayList();
    public TableView<Izvještaj> tblIzvjestaji;
    public TableColumn<Izvještaj,Number> rowID;
    public TableColumn<Izvještaj,String> rowObrazovnaInstitucija;
    public TableColumn<Izvještaj,String> rowAdresaInstitucije;
    public TableColumn<Izvještaj,String> rowPostanskiBroj;
    public TableColumn<Izvještaj,String> rowDatumInspekcije;
    public Button btnIzvjestaji;
    public Button btnProfil;
    public Button btnOpen;
    public Label lblIme,lblPrezime,lblUsername,lblEmail,lblBrojTelefona;
    public InspektorKontroler(Korisnik inspektor) {
        this.inspektor = inspektor;
        izvjestaji.addAll(KorisnikDAO.getInstance().dajSveIzvjestajeOdInspektora(inspektor));
    }
    @FXML
    public void initialize(){
        profilPane.setVisible(true);
        izvjestajiPane.setVisible(false);
        lblIme.setText(inspektor.getIme());
        lblPrezime.setText(inspektor.getPrezime());
        lblUsername.setText(inspektor.getUsername());
        lblEmail.setText(inspektor.getEmail());
        lblBrojTelefona.setText(inspektor.getBrojTelefona());
        rowID.setCellValueFactory(i -> i.getValue().idProperty());
        rowObrazovnaInstitucija.setCellValueFactory(i -> i.getValue().getObrazovnaInstitucija().nazivProperty());
        rowAdresaInstitucije.setCellValueFactory(i -> i.getValue().getObrazovnaInstitucija().adresaProperty());
        rowPostanskiBroj.setCellValueFactory(i -> i.getValue().getObrazovnaInstitucija().postanskiBrojProperty());
        rowDatumInspekcije.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getDatumIzvještaja().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))));
        tblIzvjestaji.setItems(izvjestaji);

        tblIzvjestaji.getSelectionModel().selectedItemProperty().addListener((obs,stari,novi)->{
            if(novi!=null){
                btnOpen.setDisable(false);
            }else{
                btnOpen.setDisable(true);
            }
        });
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
            if(ctrl.getTrebaKreirati()) {
                Izvještaj izvještaj = ctrl.getIzvještaj();
                KorisnikDAO.getInstance().upisiIzvjestaj(izvještaj);
            }
        });
    }
    public void prikaziInspektorPanel(ActionEvent actionEvent){
        profilPane.setVisible(true);
        izvjestajiPane.setVisible(false);
        btnProfil.getStyleClass().removeAll("buttonNotSelected");
        btnProfil.getStyleClass().removeAll("buttonHovered");
        btnIzvjestaji.getStyleClass().removeAll("buttonSelected");
        btnProfil.getStyleClass().add("buttonSelected");
    }
    public  void prikaziIzvjestaje(ActionEvent actionEvent){
        profilPane.setVisible(false);
        izvjestajiPane.setVisible(true);
        btnIzvjestaji.getStyleClass().removeAll("buttonNotSelected");
        btnIzvjestaji.getStyleClass().removeAll("buttonHovered");
        btnProfil.getStyleClass().removeAll("buttonSelected");
        btnIzvjestaji.getStyleClass().add("buttonSelected");
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
    public void otvoriIzvjestaj(ActionEvent actionEvent) throws IOException {
        Stage createWindow = new Stage();
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/izvjestaj.fxml"),bundle);
        loader.setController(new IzvjestajController(tblIzvjestaji.getSelectionModel().getSelectedItem()));
        createWindow.setTitle(bundle.getString("create"));
        createWindow.setScene(new Scene(loader.load(),USE_COMPUTED_SIZE,USE_COMPUTED_SIZE));
        createWindow.show();
    }
}
