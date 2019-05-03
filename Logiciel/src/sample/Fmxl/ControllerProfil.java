package sample.Fmxl;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ControllerProfil {

    public static Stage stage;

    @FXML
    public VBox profil;

    @FXML
    public Circle profilPicture;

    public static void setStage(Stage stage) {
        ControllerProfil.stage = stage;
    }

    @FXML
    public void initialize() {
        profilPicture.setFill(new ImagePattern(new Image ("./sample/profile.jpg")));
    }

    @FXML
    public void openExplorer() throws IOException {
        //Desktop.getDesktop().open(new File("C:\\"));
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une nouvelle image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images","*.jpg", "*.png","*.gif"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("GIF", "*.gif")
        );
        File selected = fileChooser.showOpenDialog(stage);
        if(selected != null){
            System.out.println(selected.toURI().toString());
            profil.getChildren().remove(0);
            //ImageView img = new ImageView();
            //Image img = new Image(selected.toURI().toString(),128,128,true,true);
           // profilPicture.setImage(img);
            //profilPicture.getChildren().add(1,img);
            Circle cir2 = new Circle(0,0,64);
            Image img = new Image(selected.toURI().toString());
            cir2.setFill(new ImagePattern(img));
            profil.getChildren().add(0,cir2);

        }
    }

    @FXML
    public void close(){
        stage.close();
    }
}
