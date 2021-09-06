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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;

public class AdminController {
    private Korisnik admin=null;
    public Button btnInspektori;
    private Button odabraniButton;
    public GridPane inspektoriPane;
    public GridPane izvjestajiPane;
    public Button btnIzvjestaji;
    public Button btnEdit;
    public Button btnDelete;

    public TableView<Korisnik> tblInspektori;
    public TableColumn<Korisnik,String> rowID;
    public TableColumn<Korisnik,String> rowIme;
    public TableColumn<Korisnik,String> rowPrezime;
    public TableColumn<Korisnik,String> rowUsername;
    public TableColumn<Korisnik,String> rowEmail;
    private final ObservableList<Korisnik> inspektori = FXCollections.observableArrayList();
    public TableView<Izvještaj> tblReports;
    public TableColumn<Izvještaj,Number> rowIDIzvjestaja;
    public TableColumn<Izvještaj,String> rowObrazovnaInstitucija;
    public TableColumn<Izvještaj,String> rowInspektor;
    public TableColumn<Izvještaj,String> rowDatumInspekcije;
    public TableColumn<Izvještaj,String> rowAdresaInstitucije;
    public TableColumn<Izvještaj,String> rowPostanskiBroj;
    private final ObservableList<Izvještaj> izvjestaji = FXCollections.observableArrayList();
    public ChoiceBox<String> chBoxIzvjestaji;
    public ChoiceBox<String> chBoxInspektori;
    private final ObservableList<String> listaOpcijaIzvjestaji = FXCollections.observableArrayList();
    private final ObservableList<Izvještaj> pomocnaListaIzvjestaji = FXCollections.observableArrayList();
    private final ObservableList<Korisnik> pomocnaListaInspektora = FXCollections.observableArrayList();
    public TextField fldIzvjestajiFilter;
    public DatePicker filterPrviDate, filterZadnjiDate;
    public TextField fldInspektoriFilter;
    private final ObservableList<String> listaOpcijaInspektori = FXCollections.observableArrayList();
    public Button btnOtvoriIzvjestaj;
    public Button btnOdjava;
    public Label lblFrom,lblTo;

    public AdminController(Korisnik korisnik){
        admin=korisnik;
        inspektori.addAll(KorisnikDAO.getInstance().dajSveInspektore());
        izvjestaji.addAll(KorisnikDAO.getInstance().dajSveIzvjestaje());
        listaOpcijaIzvjestaji.addAll("Naziv obrazovne institucije","Ime i prezime inspektora", "Datum inspekcije");
        listaOpcijaInspektori.addAll("ID","Ime i prezime","Email adresa");

    }

    @FXML
    public void initialize(){
        btnDelete.setDisable(true);
        btnEdit.setDisable(true);
        odabraniButton = btnInspektori;
        izvjestajiPane.setVisible(false);
        inspektoriPane.setVisible(true);
        btnOtvoriIzvjestaj.setDisable(true);
        rowID.setCellValueFactory(new PropertyValueFactory<>("id"));
        rowIme.setCellValueFactory(new PropertyValueFactory<>("ime"));
        rowPrezime.setCellValueFactory(new PropertyValueFactory<>("prezime"));
        rowEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        rowUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        tblInspektori.setItems(inspektori);
        tblReports.getSelectionModel().selectedItemProperty().addListener((obs,stari,novi)->{
            if(novi==null){
                btnOtvoriIzvjestaj.setDisable(true);
            }else btnOtvoriIzvjestaj.setDisable(false);
        });
        rowIDIzvjestaja.setCellValueFactory(i -> i.getValue().idProperty());
        rowObrazovnaInstitucija.setCellValueFactory(i -> i.getValue().getObrazovnaInstitucija().nazivProperty());
        rowInspektor.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getInspektor().getIme()+" "+i.getValue().getInspektor().getPrezime()));
        rowAdresaInstitucije.setCellValueFactory(i -> i.getValue().getObrazovnaInstitucija().adresaProperty());
        rowPostanskiBroj.setCellValueFactory(i -> i.getValue().getObrazovnaInstitucija().postanskiBrojProperty());
        rowDatumInspekcije.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getDatumIzvještaja().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))));
        tblReports.setItems(izvjestaji);
        tblInspektori.getSelectionModel().selectedItemProperty().addListener((obs,stari,novi)->{
            if(novi==null){
                btnDelete.setDisable(true);
                btnEdit.setDisable(true);
            }else{
                btnEdit.setDisable(false);
                btnDelete.setDisable(false);
            }
        });
        postaviFilterZaIzvjestaje(chBoxIzvjestaji, listaOpcijaIzvjestaji, filterPrviDate, filterZadnjiDate, fldIzvjestajiFilter, tblReports, izvjestaji, pomocnaListaIzvjestaji,lblFrom,lblTo);
        chBoxInspektori.setItems(listaOpcijaInspektori);
        chBoxInspektori.getSelectionModel().select(0);
        chBoxInspektori.getSelectionModel().selectedIndexProperty().addListener((a,b,c)->{
            fldInspektoriFilter.clear();
        });

        fldInspektoriFilter.textProperty().addListener((obs,stari,novi)->{
            switch (chBoxInspektori.getSelectionModel().getSelectedIndex()){
                case 0:
                    if(novi.length()>0) {
                        pomocnaListaInspektora.clear();
                        pomocnaListaInspektora.addAll(inspektori.stream().filter(i -> i.getId() == Integer.parseInt(novi)).collect(Collectors.toList()));
                        tblInspektori.setItems(pomocnaListaInspektora);
                    }else tblInspektori.setItems(inspektori);
                    break;
                case 1:
                    if(novi.length()>0) {
                        pomocnaListaInspektora.clear();
                        pomocnaListaInspektora.addAll(inspektori.stream().filter(i -> (i.getIme()+" "+i.getPrezime()).startsWith(novi)).collect(Collectors.toList()));
                        tblInspektori.setItems(pomocnaListaInspektora);
                    }else tblInspektori.setItems(inspektori);
                    break;
                case 2:
                    if(novi.length()>0) {
                        pomocnaListaInspektora.clear();
                        pomocnaListaInspektora.addAll(inspektori.stream().filter(i -> i.getEmail().startsWith(novi)).collect(Collectors.toList()));
                        tblInspektori.setItems(pomocnaListaInspektora);
                    }else tblInspektori.setItems(inspektori);
                    break;
            }
        });
    }

    static void postaviFilterZaIzvjestaje(ChoiceBox<String> chBoxIzvjestaji, ObservableList<String> listaOpcijaIzvjestaji, DatePicker filterDateFrom,DatePicker filterDateTo, TextField fldIzvjestajiFilter, TableView<Izvještaj> tblReports, ObservableList<Izvještaj> izvjestaji, ObservableList<Izvještaj> pomocnaListaIzvjestaji,Label from,Label to) {
        chBoxIzvjestaji.setItems(listaOpcijaIzvjestaji);
        chBoxIzvjestaji.getSelectionModel().select(0);
        chBoxIzvjestaji.getSelectionModel().selectedIndexProperty().addListener((obs, stari, novi)->{
            if(novi.intValue() == 2){
                filterDateFrom.setVisible(true);
                filterDateTo.setVisible(true);
                fldIzvjestajiFilter.setVisible(false);
                from.setVisible(true);
                to.setVisible(true);
                filterDateFrom.getEditor().clear();
                filterDateTo.getEditor().clear();
            }else{
                from.setVisible(false);
                to.setVisible(false);
                fldIzvjestajiFilter.clear();
                filterDateFrom.setVisible(false);
                filterDateTo.setVisible(false);
                fldIzvjestajiFilter.setVisible(true);
            }
            tblReports.setItems(izvjestaji);
        });
        fldIzvjestajiFilter.textProperty().addListener((obs, stari, novi)->{
            if(chBoxIzvjestaji.getSelectionModel().getSelectedIndex()==0){
                if(novi.length()>0) {
                    pomocnaListaIzvjestaji.clear();
                    pomocnaListaIzvjestaji.addAll(izvjestaji.stream().filter((s) -> s.getObrazovnaInstitucija().getNaziv().startsWith(novi)).collect(Collectors.toList()));
                    tblReports.setItems(pomocnaListaIzvjestaji);
                }else tblReports.setItems(izvjestaji);
            }else{
                if(novi.length()>0) {
                    pomocnaListaIzvjestaji.clear();
                    pomocnaListaIzvjestaji.addAll(izvjestaji.stream().filter((s) -> (s.getInspektor().getIme()+" "+s.getInspektor().getPrezime()).startsWith(novi)).collect(Collectors.toList()));
                    tblReports.setItems(pomocnaListaIzvjestaji);
                }else tblReports.setItems(izvjestaji);
            }
        });
        filterDateFrom.valueProperty().addListener((obs, stari, novi)->{
            if(filterDateTo.getValue()!=null)
            dateListener(filterDateFrom, filterDateTo, tblReports, izvjestaji, pomocnaListaIzvjestaji);
        });
        filterDateTo.valueProperty().addListener((obs, stari, novi)->{
            if(filterDateFrom.getValue()!=null)
            dateListener(filterDateFrom, filterDateTo, tblReports, izvjestaji, pomocnaListaIzvjestaji);
        });
    }

    private static void dateListener(DatePicker filterDateFrom, DatePicker filterDateTo, TableView<Izvještaj> tblReports, ObservableList<Izvještaj> izvjestaji, ObservableList<Izvještaj> pomocnaListaIzvjestaji) {
        pomocnaListaIzvjestaji.clear();
        pomocnaListaIzvjestaji.addAll(izvjestaji.stream().filter((s) ->  dateBetween(s.getDatumIzvještaja().toLocalDate(),filterDateFrom.getValue(),filterDateTo.getValue())).collect(Collectors.toList()));
        tblReports.setItems(pomocnaListaIzvjestaji);

    }

    private static boolean dateBetween(LocalDate date, LocalDate dateBefore,LocalDate dateAfter){
        return dateBefore.isBefore(dateAfter)&&((date.isAfter(dateBefore) && date.isBefore(dateAfter)) || (date.equals(dateBefore) || date.equals(dateAfter)));
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
        btnIzvjestaji.getStyleClass().add("buttonNotSelected");
    }
    public void prikaziIzvjestaje(ActionEvent actionEvent){
        odabraniButton = btnIzvjestaji;
        izvjestajiPane.setVisible(true);
        inspektoriPane.setVisible(false);
        btnIzvjestaji.getStyleClass().removeAll("buttonNotSelected");
        btnIzvjestaji.getStyleClass().removeAll("buttonHovered");
        btnInspektori.getStyleClass().removeAll("buttonSelected");
        btnIzvjestaji.getStyleClass().add("buttonSelected");
        btnInspektori.getStyleClass().add("buttonNotSelected");
    }
    public void openCreateWindow(ActionEvent actionEvent) throws IOException {
        Stage createWindow = new Stage();
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/create_korisnik.fxml"),bundle);
        loader.setController(new CreateKorisnikController());
        createWindow.setTitle(bundle.getString("create"));
        createWindow.setScene(new Scene(loader.load(),USE_COMPUTED_SIZE,400));
        createWindow.show();
        createWindow.setOnHiding((e)->{
            CreateKorisnikController kontroler = loader.getController();
            if(kontroler.isTrebaKreirati()){
                KorisnikDAO.getInstance().kreirajInspektora(kontroler.getNoviKorisnik());
                inspektori.add(kontroler.getNoviKorisnik());
            }
        });
    }
    public void otvoriIzvjestaj(ActionEvent actionEvent) throws IOException {
        InspektorKontroler.otvoriIzvjestaj(tblReports.getSelectionModel().getSelectedItem());
    }
    public void odjaviSe(ActionEvent actionEvent) throws IOException {
        ((Stage)((Node)actionEvent.getSource()).getScene().getWindow()).close();
        Main.otvoriLoginProzor(new Stage());
    }
    public void editInspektor(ActionEvent actionEvent) throws IOException {
        Stage createWindow = new Stage();
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/create_korisnik.fxml"),bundle);
        loader.setController(new CreateKorisnikController(tblInspektori.getSelectionModel().getSelectedItem()));
        createWindow.setTitle(bundle.getString("edit"));
        createWindow.setScene(new Scene(loader.load(),USE_COMPUTED_SIZE,USE_COMPUTED_SIZE));
        createWindow.show();
        createWindow.setOnHiding((e)->{
            CreateKorisnikController ctrl = loader.getController();
            if(ctrl.isTrebaKreirati()){
                KorisnikDAO.getInstance().azurirajInspektora(ctrl.getNoviKorisnik());
            }
        });
    }
    public void deleteInspektor(ActionEvent actionEvent){
        KorisnikDAO.getInstance().obrisiInspektora(tblInspektori.getSelectionModel().getSelectedItem());
        inspektori.removeIf(korisnik -> korisnik.getId()==tblInspektori.getSelectionModel().getSelectedItem().getId());

    }

}
