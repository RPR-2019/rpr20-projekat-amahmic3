package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.KorisnikDAO;
import ba.unsa.etf.rpr.Main;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


@ExtendWith(ApplicationExtension.class)
class LoginControllerTest {
    @Start
    public void start (Stage stage) throws Exception {
        Main.otvoriLoginProzor(stage);
    }
    @BeforeEach
    void vratiNaDefault(FxRobot robot) {
        KorisnikDAO.getInstance().vratiNaDefault();
   }
    @Test
    void adminTest(FxRobot robot) {
        robot.clickOn("#fldUsername").write("admin");
        robot.clickOn("#fldPassword").write("admin");
        robot.clickOn("#btnLogin");
        TableView korisnikTableView = robot.lookup("#tblInspektori").queryAs(TableView.class);
        assertNotEquals(korisnikTableView,null);
        robot.closeCurrentWindow();
     }
     @Test
    void pogresniPodaciTest(FxRobot robot){
         robot.clickOn("#fldUsername").write("pogresanUsername");
         robot.clickOn("#fldPassword").write("pogresanPassword");
         robot.clickOn("#btnLogin");
         assertEquals(robot.listWindows().size(),2);
         robot.closeCurrentWindow();
         robot.closeCurrentWindow();
     }
}