package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.KorisnikDAO;
import ba.unsa.etf.rpr.Main;
import ba.unsa.etf.rpr.models.Izvještaj;
import ba.unsa.etf.rpr.models.Korisnik;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
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
    public ChoiceBox<String> chbFilteri;
    public TextField fldFilter;
    public DatePicker filterPrviDatum,filterZadnjiDatum;
    private ObservableList<String> listaOpcija = FXCollections.observableArrayList();
    private ObservableList<Izvještaj> pomocnaLista = FXCollections.observableArrayList();
    public  Label lblFrom,lblTo;
    public Button btnENLocalization,btnBALocalization;
    private ResourceBundle bundle = ResourceBundle.getBundle("Translation");
    public InspektorKontroler(Korisnik inspektor) {
        this.inspektor = inspektor;
        izvjestaji.addAll(KorisnikDAO.getInstance().dajSveIzvjestajeOdInspektora(inspektor));
        listaOpcija.addAll(bundle.getString("nazivInstitucije").replaceAll(": ",""),bundle.getString("imeIPrezimeInspektora").replaceAll(": ",""), bundle.getString("datumInspekcije"));
    }
    @FXML
    public void initialize(){
        btnOpen.setDisable(true);

        AdminController.postaviFilterZaIzvjestaje(chbFilteri, listaOpcija, filterPrviDatum, filterZadnjiDatum,fldFilter, tblIzvjestaji, izvjestaji, pomocnaLista,lblFrom,lblTo);
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
        if(Locale.getDefault().getCountry().matches("US")){
            btnENLocalization.getStyleClass().add("buttonSelected");
            btnBALocalization.getStyleClass().add("buttonNotSelected");
        }else{
            btnBALocalization.getStyleClass().add("buttonSelected");
            btnENLocalization.getStyleClass().add("buttonNotSelected");
        }
    }
    public void odjaviSe(ActionEvent actionEvent) throws IOException {
        ((Stage)((Node)actionEvent.getSource()).getScene().getWindow()).close();
        Main.otvoriLoginProzor(new Stage());
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
                izvjestaji.add(izvještaj);
            }
        });
    }
    public void prikaziInspektorPanel(ActionEvent actionEvent){
        profilPane.setVisible(true);
        izvjestajiPane.setVisible(false);
        btnProfil.getStyleClass().removeAll("buttonNotSelected");
        btnProfil.getStyleClass().removeAll("buttonHovered");
        btnIzvjestaji.getStyleClass().removeAll("buttonSelected");
        btnIzvjestaji.getStyleClass().add("buttonNotSelected");
        btnProfil.getStyleClass().add("buttonSelected");
    }
    public  void prikaziIzvjestaje(ActionEvent actionEvent){
        profilPane.setVisible(false);
        izvjestajiPane.setVisible(true);
        btnIzvjestaji.getStyleClass().removeAll("buttonNotSelected");
        btnIzvjestaji.getStyleClass().removeAll("buttonHovered");
        btnProfil.getStyleClass().removeAll("buttonSelected");
        btnProfil.getStyleClass().add("buttonNotSelected");
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
        otvoriIzvjestaj(tblIzvjestaji.getSelectionModel().getSelectedItem());
    }

    public static void otvoriIzvjestaj(Izvještaj izvještaj) throws IOException {
        Stage createWindow = new Stage();
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        FXMLLoader loader = new FXMLLoader(InspektorKontroler.class.getResource("/fxml/izvjestaj.fxml"),bundle);
        loader.setController(new IzvjestajController(izvještaj));
        createWindow.setTitle(bundle.getString("create"));
        createWindow.setScene(new Scene(loader.load(),USE_COMPUTED_SIZE,USE_COMPUTED_SIZE));
        createWindow.show();
    }
    public void setEN(ActionEvent actionEvent) throws IOException {
        Locale.setDefault(new Locale("en_US", "US"));
        refresh();
        ((Stage)((Node)actionEvent.getTarget()).getScene().getWindow()).close();
    }

    public void setBA(ActionEvent actionEvent) throws IOException {
        Locale.setDefault(new Locale("bs", "BA"));
        refresh();
        ((Stage)((Node)actionEvent.getTarget()).getScene().getWindow()).close();
    }
    private void refresh() throws IOException {
        LoginController.uspjesanLogin(inspektor);
    }
}
