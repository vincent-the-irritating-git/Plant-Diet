package com.dissertation;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import com.dissertation.server_side.Database;

import java.io.IOException;


public class Main extends Application {

    @FXML
    private Pane pane; 

    public Main() throws IOException {}

    //==================================================================//

    //==================================================================
    public static void main(String[]argv){
        launch(argv);
    }

    public void loadExistingUser(Stage stage){
        try {
            FXMLLoader fx =
                new FXMLLoader(Main.class.getResource(
                    "/dissertation/fxml/body.fxml"));
            Scene scene = new Scene(fx.load(), 744, 615);
/*            scene.getStylesheets()
                 .add(getClass().getResource("application.css")
                                .toExternalForm());*/
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }
    public void createNewUser(Stage stage){
        try {
            FXMLLoader fx =
                new FXMLLoader(Main.class.getResource(
                    "/dissertation/fxml/NewRegister.fxml"));
            Scene scene = new Scene(fx.load(), 744, 615);
//            scene.getStylesheets()
//                 .add(getClass().getResource("application.css")
//                                .toExternalForm());
            stage.setScene(scene);
//            stage.setMaximized(true);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void start(Stage stage) {
        Database db=new Database();
        if (db.isRegistered()) {
            loadExistingUser(stage);
        } else {
            createNewUser(stage);
        }
        db.closeDatabase();
    }

    //our FXMLLoader
    public Object getLoader(String fxmlName){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/dissertation/fxml" +
                "/" + fxmlName +
                ".fxml"));
            Parent root = loader.load();
            //Get controller of bodyController
            Object ad = loader.getController();
            return ad;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //converts the boolean to a string character
    public static String veganBoolean(int x) {
        if (x == 0) {
            return "Ve";
        } else
            return "V";
    }

    public static void openNewWindow(Parent root) {
        Stage stage = new Stage();
        try {
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
        stage.show();
    }


}