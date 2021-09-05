package ba.unsa.etf.rpr;

import ba.unsa.etf.rpr.controllers.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        otvoriLoginProzor(primaryStage);
    }
    public static void otvoriLoginProzor(Stage loginWindow) throws IOException {
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/login.fxml"),bundle);
        loader.setController(new LoginController());
        Parent root = loader.load();
        loginWindow.setTitle(bundle.getString("login"));
        loginWindow.setScene(new Scene(root,USE_COMPUTED_SIZE,USE_COMPUTED_SIZE));
        loginWindow.setResizable(false);
        loginWindow.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
