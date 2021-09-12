package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.KorisnikDAO;
import ba.unsa.etf.rpr.models.IzjavaSvjedoka;
import ba.unsa.etf.rpr.models.Izvještaj;
import ba.unsa.etf.rpr.models.Korisnik;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.sun.javafx.font.FontConstants;
import com.sun.javafx.font.FontFactory;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.validator.routines.EmailValidator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;

public class IzvjestajController {
    private Izvještaj izvještaj;
    public TextField fldNazivInstitucije;
    public TextField fldTelefonInstitucije;
    public TextField fldAdresaInstitucije;
    public TextField fldPostanskiBroj;
    public TextArea fldIzvjestaj;
    public TextField fldImePrvog;
    public TextField fldPrezimePrvog;
    public TextField fldEmailPrvog;
    public TextField fldBrojPrvog;
    public TextArea fldIzjavaPrvog;
    public TextField fldImeDrugog;
    public TextField fldPrezimeDrugog;
    public TextField fldEmailDrugog;
    public TextField fldBrojDrugog;
    public TextArea fldIzjavaDrugog;
    public TextField fldSati;
    public TextField fldMinute;
    public DatePicker fldDatum;
    public ImageView loadingImg;
    public ContextMenu fldSuggestionMenu;
    public Button btnCreate;
    private Boolean trebaKreirati = false;
    public Parent root;
    public Button btnExport;
    private boolean kreiranje=true;
    private ResourceBundle bundle = ResourceBundle.getBundle("Translation");
    public IzvjestajController(Korisnik inspektor) {
        izvještaj = new Izvještaj();
        izvještaj.setInspektor(inspektor);
    }
    public IzvjestajController(Izvještaj izvještaj){
        this.izvještaj = izvještaj;
        kreiranje=false;
    }
    @FXML
    void initialize(){
        btnCreate.setDisable(true);
        fldNazivInstitucije.textProperty().bindBidirectional(izvještaj.getObrazovnaInstitucija().nazivProperty());
        fldAdresaInstitucije.textProperty().bindBidirectional(izvještaj.getObrazovnaInstitucija().adresaProperty());
        fldTelefonInstitucije.textProperty().bindBidirectional(izvještaj.getObrazovnaInstitucija().brojTelefonaProperty());
        fldPostanskiBroj.textProperty().bindBidirectional(izvještaj.getObrazovnaInstitucija().postanskiBrojProperty());
        fldIzvjestaj.textProperty().bindBidirectional(izvještaj.tekstIzvještajaProperty());
        bindSvjedoka(fldImePrvog, fldPrezimePrvog, fldEmailPrvog, fldBrojPrvog, fldIzjavaPrvog,izvještaj.getPrvi());
        bindSvjedoka(fldImeDrugog, fldPrezimeDrugog, fldEmailDrugog, fldBrojDrugog, fldIzjavaDrugog, izvještaj.getDrugi());
        if(!kreiranje){
            btnExport.setVisible(true);
            btnCreate.setVisible(false);
            fldNazivInstitucije.setEditable(false);
            fldAdresaInstitucije.setEditable(false);
            fldPostanskiBroj.setEditable(false);
            fldTelefonInstitucije.setEditable(false);
            fldDatum.setEditable(false);
            fldIzvjestaj.setEditable(false);
            fldImePrvog.setEditable(false);
            fldEmailPrvog.setEditable(false);
            fldPrezimePrvog.setEditable(false);
            fldBrojPrvog.setEditable(false);
            fldIzjavaPrvog.setEditable(false);
            fldImeDrugog.setEditable(false);
            fldPrezimeDrugog.setEditable(false);
            fldBrojDrugog.setEditable(false);
            fldEmailDrugog.setEditable(false);
            fldIzjavaDrugog.setEditable(false);
            fldSati.setEditable(false);
            fldMinute.setEditable(false);
            fldDatum.setDisable(true);
            fldDatum.setOpacity(1);
            fldDatum.setValue(izvještaj.getDatumIzvještaja().toLocalDate());
            fldSati.setText(Integer.toString(izvještaj.getDatumIzvještaja().getHour()));
            fldMinute.setText(Integer.toString(izvještaj.getDatumIzvještaja().getMinute()));
        }else {
            fldNazivInstitucije.textProperty().addListener(this::fldNazivInstitucijeListener);
            onChangeListener(fldNazivInstitucije);
            onChangeListener(fldAdresaInstitucije);
            onChangeListener(fldTelefonInstitucije);
            onChangeListener(fldPostanskiBroj);
            onChangeListener(fldIzvjestaj);
            onChangeListener(fldImePrvog);
            onChangeListener(fldPrezimePrvog);
            onChangeListener(fldEmailPrvog);
            onChangeListener(fldBrojPrvog);
            onChangeListener(fldIzjavaPrvog);
            onChangeListener(fldImeDrugog);
            onChangeListener(fldPrezimeDrugog);
            onChangeListener(fldEmailDrugog);
            onChangeListener(fldBrojDrugog);
            onChangeListener(fldIzjavaDrugog);
            onChangeListener(fldSati);
            fldNazivInstitucije.textProperty().addListener((obs, stari, novi) -> {
                if (novi.length() >= 3) {
                    fldSuggestionMenu.getItems().clear();
                    var listaInstitucija = KorisnikDAO.getInstance().obrazovneInstitucijeSuggestion(novi);
                    for (var obrazovnaInstitucija : listaInstitucija) {
                        fldSuggestionMenu.getItems().add(new MenuItem(obrazovnaInstitucija.getNaziv() + ", " + obrazovnaInstitucija.getAdresa() + ", " + obrazovnaInstitucija.getPostanskiBroj() + ", " + obrazovnaInstitucija.getBrojTelefona()));
                    }
                    fldSuggestionMenu.show(fldNazivInstitucije, Side.BOTTOM, 0, 0);
                } else {
                    fldSuggestionMenu.getItems().clear();
                    fldSuggestionMenu.hide();
                }
            });
            fldSuggestionMenu.setOnAction((e) -> {
                var tekst = ((MenuItem) e.getTarget()).getText();
                String[] polja = tekst.split(", ");
                fldNazivInstitucije.textProperty().setValue(polja[0]);
                fldAdresaInstitucije.textProperty().setValue(polja[1]);
                fldPostanskiBroj.textProperty().setValue(polja[2]);
                fldTelefonInstitucije.textProperty().setValue(polja[3]);
                validirajInstituciju();
            });
            validacijskiListener(fldNazivInstitucije, String::isEmpty);
            validacijskiListener(fldAdresaInstitucije, String::isEmpty);
            validacijskiListener(fldTelefonInstitucije, this::validirajTelefon);
            validacijskiListener(fldIzvjestaj, String::isEmpty);
            validacijskiListener(fldImePrvog, String::isEmpty);
            validacijskiListener(fldPrezimePrvog, String::isEmpty);
            validacijskiListener(fldEmailPrvog, IzvjestajController::validirajEmail);
            validacijskiListener(fldBrojPrvog, this::validirajTelefon);
            validacijskiListener(fldIzjavaPrvog, String::isEmpty);
            validacijskiListener(fldImeDrugog, String::isEmpty);
            validacijskiListener(fldPrezimeDrugog, String::isEmpty);
            validacijskiListener(fldEmailDrugog, IzvjestajController::validirajEmail);
            validacijskiListener(fldBrojDrugog, this::validirajTelefon);
            validacijskiListener(fldIzjavaDrugog, String::isEmpty);
            fldPostanskiBroj.focusedProperty().addListener((obs, stari, novi) -> {
                if (!novi) {
                    if (fldPostanskiBroj.textProperty().getValue() != null && !fldPostanskiBroj.textProperty().getValue().isEmpty())
                        validirajPostanskiBroj(fldPostanskiBroj.textProperty().getValue());
                }
            });
            fldDatum.valueProperty().addListener((obs, stari, novi) -> {
                if (novi != null) {
                    fldDatum.getStyleClass().removeAll("poljeNijeIspravno");
                    fldDatum.getStyleClass().add("poljeIspravno");
                    if (sviValidni()) btnCreate.setDisable(false);
                } else {
                    fldDatum.getStyleClass().removeAll("poljeIspravno");
                    fldDatum.getStyleClass().add("poljeNijeIspravno");
                    btnCreate.setDisable(true);
                }
            });
            validacijskiListener(fldSati, this::validirajSate);
            validacijskiListener(fldMinute, this::validirajMinute);
            fldMinute.textProperty().addListener((obs,stari,novi)->{
                if(!validirajMinute(novi)){
                    btnCreate.setDisable(false);
                }else{
                    btnCreate.setDisable(true);
                }
            });
        }
    }

    private boolean validirajTelefon(String s) {
            return !s.matches("[0-9]+");
    }

    private static boolean validirajEmail(String email){
        return !EmailValidator.getInstance().isValid(email);
    }
    private void validirajInstituciju() {
        fldNazivInstitucije.getStyleClass().removeAll("poljeNijeIspravno");
        fldAdresaInstitucije.getStyleClass().removeAll("poljeNijeIspravno");
        fldPostanskiBroj.getStyleClass().removeAll("poljeNijeIspravno");
        fldTelefonInstitucije.getStyleClass().removeAll("poljeNijeIspravno");
        fldNazivInstitucije.getStyleClass().add("poljeIspravno");
        fldAdresaInstitucije.getStyleClass().add("poljeIspravno");
        fldPostanskiBroj.getStyleClass().add("poljeIspravno");
        fldTelefonInstitucije.getStyleClass().add("poljeIspravno");
    }

    private boolean validirajSate(String sati){
        return !sati.matches("0?[0-9]|1[0-9]|2[0-3]");
    }
    private boolean validirajMinute(String minute){
        return !minute.matches("[0-5]?[0-9]");
    }
    private void validirajPostanskiBroj(String postanskiBroj){
        Task<Boolean> posao = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                try {
                    Platform.runLater(()->{
                        loadingImg.setVisible(true);
                    });
                    URL url = new URL("https://c9.etf.unsa.ba/proba/postanskiBroj.php?postanskiBroj=" + postanskiBroj);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    InputStream is = httpURLConnection.getInputStream();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(is));

                    if (rd.readLine().matches("NOT+ \\w+")) {
                       return false;
                    } else {
                        return true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }
        };
        posao.setOnSucceeded((event)->{
            if(posao.getValue()){
                Platform.runLater(()->{
                    loadingImg.setVisible(false);
                    fldPostanskiBroj.getStyleClass().removeAll("poljeNijeIspravno");
                    fldPostanskiBroj.getStyleClass().add("poljeIspravno");
                });
            }else{
                Platform.runLater(()->{
                    loadingImg.setVisible(false);
                    fldPostanskiBroj.getStyleClass().removeAll("poljeIspravno");
                    fldPostanskiBroj.getStyleClass().add("poljeNijeIspravno");
                });
            }
        });
        new Thread(posao).start();

    }
    private void fldNazivInstitucijeListener(Observable obs, String oldNaziv, String newNaziv){

    }
    private void bindSvjedoka(TextField fldIme, TextField fldPrezime, TextField fldEmail, TextField fldBroj, TextArea fldIzjava, IzjavaSvjedoka izjavaSvjedoka) {
        fldIme.textProperty().bindBidirectional(izjavaSvjedoka.getSvjedok().imeProperty());
        fldPrezime.textProperty().bindBidirectional(izjavaSvjedoka.getSvjedok().prezimeProperty());
        fldEmail.textProperty().bindBidirectional(izjavaSvjedoka.getSvjedok().emailProperty());
        fldBroj.textProperty().bindBidirectional(izjavaSvjedoka.getSvjedok().brojTelefonaProperty());
        fldIzjava.textProperty().bindBidirectional(izjavaSvjedoka.tekstIzjaveProperty());
    }
    private void onChangeListener(TextInputControl textField){
        textField.textProperty().addListener((obs,novo,staro)->{
            textField.getStyleClass().removeAll("poljeIspravno");
            textField.getStyleClass().removeAll("poljeNijeIspravno");
            btnCreate.setDisable(true);
        });
    }
    public void validacijskiListener(TextInputControl textField, Predicate<String> predikat)
    {
        textField.focusedProperty().addListener((obs,oldState,newState)->{
            if(!newState){
                if(textField.textProperty().getValue()==null || textField.textProperty().getValue().isEmpty()){
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
    private boolean sviValidni() {
        Function<Node,Boolean> validacija = CreateKorisnikController::validan;
        return validacija.apply(fldNazivInstitucije) && validacija.apply(fldAdresaInstitucije) && validacija.apply(fldPostanskiBroj) && validacija.apply(fldTelefonInstitucije)&& validacija.apply(fldIzvjestaj)&&
        validacija.apply(fldImePrvog) && validacija.apply(fldPrezimePrvog) && validacija.apply(fldEmailPrvog) && validacija.apply(fldBrojPrvog) && validacija.apply(fldIzjavaPrvog) &&
        validacija.apply(fldImeDrugog) && validacija.apply(fldPrezimeDrugog) && validacija.apply(fldEmailDrugog) && validacija.apply(fldBrojDrugog) && validacija.apply(fldIzjavaDrugog) &&
        validacija.apply(fldDatum) && validacija.apply(fldSati) && validacija.apply(fldMinute);
    }
    public void kreirajIzvjestaj(ActionEvent actionEvent){
        trebaKreirati=true;
        izvještaj.setDatumIzvještaja(LocalDateTime.of(fldDatum.getValue(), LocalTime.of(Integer.parseInt(fldSati.textProperty().getValue()),Integer.parseInt(fldMinute.textProperty().getValue()))));
        ((Stage)root.getScene().getWindow()).close();
    }
    public void ugasiProzor(ActionEvent actionEvent){
        trebaKreirati=false;
        ((Stage)root.getScene().getWindow()).close();
    }

    public Boolean getTrebaKreirati() {
        return trebaKreirati;
    }

    public Izvještaj getIzvještaj() {
        return izvještaj;
    }
    public void exportIzvjestaj(ActionEvent actionEvent){
        FileChooser fajl = new FileChooser();
        fajl.setTitle("Save");
        fajl.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PDF File","*.pdf"));
        String path=new String(fajl.showSaveDialog(((Node)actionEvent.getTarget()).getScene().getWindow()).getAbsolutePath());
        try {
            PdfWriter writer = new PdfWriter(path);
            PdfDocument pdfDocument = new PdfDocument(writer);
            pdfDocument.addNewPage();
            Document document = new Document(pdfDocument);
            document.setFont(PdfFontFactory.createFont("/font/Helvetica.ttf","Cp1250"));
            Paragraph paragraph = new Paragraph(bundle.getString("izvjestaj"));
            document.add(paragraph.setFontSize(26).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph(bundle.getString("imeIPrezimeInspektora")+izvještaj.getInspektor().getIme()+" "+izvještaj.getInspektor().getPrezime()));
            document.add(new Paragraph(bundle.getString("brojTelefona")+izvještaj.getInspektor().getBrojTelefona()));
            document.add(new Paragraph(bundle.getString("email")+izvještaj.getInspektor().getEmail()));
            document.add(new Paragraph(bundle.getString("datumIVrijemeInspekcije")+getIzvještaj().getDatumIzvještaja().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))));
            document.add(new Paragraph(bundle.getString("nazivInstitucije")+izvještaj.getObrazovnaInstitucija().getNaziv()).setMarginTop(25));
            document.add(new Paragraph(bundle.getString("adresaInstitucije")+izvještaj.getObrazovnaInstitucija().getAdresa()));
            document.add(new Paragraph(bundle.getString("postanskiBroj")+izvještaj.getObrazovnaInstitucija().getPostanskiBroj()));
            document.add(new Paragraph(bundle.getString("brojTelefona")+izvještaj.getObrazovnaInstitucija().getBrojTelefona()));
            document.add(new Paragraph(bundle.getString("izvjestaj")+": "+izvještaj.getTekstIzvještaja()).setMarginTop(18));
            document.add(new Paragraph(bundle.getString("prviSvjedok")).setMarginTop(20));
            upisiIzjavuSvjedoka(document,getIzvještaj().getPrvi());
            document.add(new Paragraph(bundle.getString("drugiSvjedok")).setMarginTop(20));
            upisiIzjavuSvjedoka(document,getIzvještaj().getDrugi());

            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void upisiIzjavuSvjedoka(Document document,IzjavaSvjedoka izjava) {
        document.add(new Paragraph(bundle.getString("imeIPrezime")+izjava.getSvjedok().getIme()+" "+izjava.getSvjedok().getPrezime()));
        document.add(new Paragraph(bundle.getString("brojTelefona")+izjava.getSvjedok().getBrojTelefona()));
        document.add(new Paragraph(bundle.getString("email")+izjava.getSvjedok().getEmail()));
        document.add(new Paragraph(bundle.getString("izjavaSvjedoka")+": "+izjava.getTekstIzjave()));
    }
}
