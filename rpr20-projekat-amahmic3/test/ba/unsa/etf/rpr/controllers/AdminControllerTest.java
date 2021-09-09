package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.KorisnikDAO;
import ba.unsa.etf.rpr.Main;
import ba.unsa.etf.rpr.models.Korisnik;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
class AdminControllerTest {
    @Start
    public void start (Stage stage) throws Exception {
        Main.otvoriLoginProzor(stage);
    }
    void loginAdmin(FxRobot robot) {
        robot.clickOn("#fldUsername").write("admin");
        robot.clickOn("#fldPassword").write("admin");
        robot.clickOn("#btnLogin");
    }
    @Test
    void dodajKorisnikaTest(FxRobot robot){
        KorisnikDAO.getInstance().vratiNaDefault();
        loginAdmin(robot);
        robot.clickOn("#btnCreateKorisnik");
        Button createButton = robot.lookup("#btnCreate").queryAs(Button.class);
        assertTrue(createButton.isDisabled());
        robot.clickOn("#fldIme").write("Adnan");
        robot.clickOn("#fldPrezime").write("Mahmić");
        robot.clickOn("#fldEmail").write("amahmic3@etf.unsa.ba");
        robot.clickOn("#fldBrojTelefona").write("06012345678");
        robot.clickOn("#btnGenerate");
        robot.clickOn("#fldPassword").write("123456");
        assertFalse(createButton.isDisabled());
        robot.clickOn("#btnCreate");
        TableView<Korisnik> tableView = robot.lookup("#tblInspektori").queryAs(TableView.class);
        assertEquals(1, tableView.getItems().stream().filter(k -> k.getEmail().equals("amahmic3@etf.unsa.ba")).count());
        assertTrue(KorisnikDAO.getInstance().dajSveInspektore().stream().anyMatch(k -> k.getEmail().equals("amahmic3@etf.unsa.ba")));
    }
    @Test
    void validacijaTest(FxRobot robot){
        loginAdmin(robot);
        robot.clickOn("#btnCreateKorisnik");
        Button createButton = robot.lookup("#btnCreate").queryAs(Button.class);
        assertTrue(createButton.isDisabled());
        robot.clickOn("#fldEmail").write("amahmic3@etf.unsa.ba");
        robot.clickOn("#fldUsername").write("amahmić1");
        robot.clickOn("#fldBrojTelefona").write("06012345678");

        robot.clickOn("#fldIme").write("A");
        assertTrue(robot.lookup("#fldEmail").queryAs(TextField.class).getStyleClass().contains("poljeNijeIspravno"));
        assertTrue(robot.lookup("#fldBrojTelefona").queryAs(TextField.class).getStyleClass().contains("poljeNijeIspravno"));
        assertTrue(robot.lookup("#fldUsername").queryAs(TextField.class).getStyleClass().contains("poljeNijeIspravno"));
        robot.clickOn("#btnCancel");
    }
    @Test
    void promjenaJezikaTest(FxRobot robot){
        loginAdmin(robot);
        robot.clickOn("#btnENLocalization");
        String en= robot.lookup("#lblReports").queryAs(Label.class).getText();
        assertEquals(en,"Reports");
        robot.clickOn("#btnBALocalization");
        String ba= robot.lookup("#lblReports").queryAs(Label.class).getText();
        assertEquals(ba,"Izvještaji");

    }
    @AfterEach
    void ugasiProzor(FxRobot fxRobot){
        fxRobot.closeCurrentWindow();
    }

}