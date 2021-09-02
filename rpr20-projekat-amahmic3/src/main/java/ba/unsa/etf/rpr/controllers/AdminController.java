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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

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
    private ObservableList<Izvještaj> izvještaji = FXCollections.observableArrayList();
    public AdminController(Korisnik korisnik){
        admin=korisnik;
        inspektori.addAll(KorisnikDAO.getInstance().dajSveInspektore());
        izvještaji.addAll(KorisnikDAO.getInstance().dajSveIzvjestaje());

    }

    public AdminController(){
        inspektori.addAll(KorisnikDAO.getInstance().dajSveInspektore());
    }
    @FXML
    public void initialize(){
        btnDelete.setDisable(true);
        btnEdit.setDisable(true);
        odabraniButton = btnInspektori;
        izvjestajiPane.setVisible(false);
        inspektoriPane.setVisible(true);

        rowID.setCellValueFactory(new PropertyValueFactory<>("id"));
        rowIme.setCellValueFactory(new PropertyValueFactory<>("ime"));
        rowPrezime.setCellValueFactory(new PropertyValueFactory<>("prezime"));
        rowEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        rowUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        tblInspektori.setItems(inspektori);

        rowIDIzvjestaja.setCellValueFactory(i -> i.getValue().idProperty());
        rowObrazovnaInstitucija.setCellValueFactory(i -> i.getValue().getObrazovnaInstitucija().nazivProperty());
        rowInspektor.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getInspektor().getIme()+" "+i.getValue().getInspektor().getPrezime()));
        rowAdresaInstitucije.setCellValueFactory(i -> i.getValue().getObrazovnaInstitucija().adresaProperty());
        rowPostanskiBroj.setCellValueFactory(i -> i.getValue().getObrazovnaInstitucija().postanskiBrojProperty());
        rowDatumInspekcije.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getDatumIzvještaja().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))));
        tblReports.setItems(izvještaji);
        tblInspektori.getSelectionModel().selectedItemProperty().addListener((obs,stari,novi)->{
            if(novi==null){
                btnDelete.setDisable(true);
                btnEdit.setDisable(true);
            }else{
                btnEdit.setDisable(false);
                btnDelete.setDisable(false);
            }
        });
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
}