package sample.Fmxl;

import annotation.AnnotatedClass;
import annotation.Status;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.Model.Nuage;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Status(author = "Krishan Class",
        progression = 50,
        version = 2.3)
public class ControllerFile implements AnnotatedClass {
    public static Stage stage;
    @FXML
    public HBox resizable;

    @FXML
    public FlowPane flowpane;

    @FXML
    public VBox myFile;

    @FXML
    public VBox myFiles;

    @FXML
    public VBox nuageFile;

    @FXML
    public VBox nuageFiles;

    @FXML
    public Label label1;

    @FXML
    public Label label2;


    public  String  url1;
    public  String  url2;
    public  ArrayList<Nuage> nuageArray = new ArrayList<Nuage>();

    public static void setStage(Stage primaryStage){
        stage = primaryStage;
    }

    public  void initialize() {

        setUrlFromOs();

        label1.setText(url1);
        label2.setText(url2);

        for(int i = 0 ; i < 100; i++){
            if(i % 5 == 0){
                nuageArray.add(new Nuage("My nuage "+i, "/sample/LN.png", "15/12/19", "nuages"));
            }else if( i % 5 == 1){
                nuageArray.add(new Nuage("My nuage "+i, "/sample/LN.png", "15/12/19", "shareNuages"));
            }else if( i % 5 == 2){
                nuageArray.add(new Nuage("My nuage "+i, "/sample/LN.png", "15/12/19", "recent"));
            }else if( i % 5 == 3){
                nuageArray.add(new Nuage("My nuage "+i, "/sample/LN.png", "15/12/19", "favorit"));
            }else if( i % 5 == 4){
                nuageArray.add(new Nuage("My nuage "+i, "/sample/LN.png", "15/12/19", "trash"));
            }else{
                nuageArray.add(new Nuage("My nuage "+i, "/sample/LN.png", "15/12/19", "trash"));
            }


        }
        for(Nuage i : nuageArray){
            addNuage(i.getImagePath(),i.getName(),i.getLastEdit());
        }

        //Nuage file
        listFile2(nuageFile,url2);

        // Fichier d'un dossier courant local
       //listFileByFolder(myFiles,url1);
        // Fichier d'un dossier courant distant
       //listFileByFolder(nuageFiles,url2);

    }


    public void addNuage(String nuageImage, String nuageName, String lastEdit){
        VBox vbox = new VBox();
        vbox.getStyleClass().add("nuages");
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(15, 15, 15, 15));
        Image image = new Image(nuageImage, 70, 50, true, true);
        ImageView imageView = new ImageView(image);
        Label label1 = new Label(nuageName);
        Label label2 = new Label("Derniere modification le :"+lastEdit);
        vbox.getChildren().add(imageView);
        vbox.getChildren().add(label1);
        vbox.getChildren().add(label2);
        flowpane.getChildren().add(vbox);
    }

    public String getSizeOfFile(double size){
        NumberFormat nf = new DecimalFormat("0.##");
        String[] data = {"B", "KB", "MB", "GB", "TB"};
        int index = 0;
        while(size > 1024 ) {
            size /= 1024;
            index++;
        }
        return nf.format(size)+" "+data[index];
    }

    public void listFile(VBox vbox, String filename ){
        TreeView<File> fileViewMine = new TreeView<File>(
                new SimpleFileTreeItem(new File(filename)));
        vbox.getChildren().add(fileViewMine);
        fileViewMine.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            url1 = newValue.getValue().toString();
            myFiles.getChildren().removeAll();
            listFileByFolder(myFiles,url1);
            label1.setText(url1);
        });

    }

    public void listFile2(VBox vbox, String filename ){
        TreeView<File> fileViewMine = new TreeView<File>(
                new SimpleFileTreeItem(new File(filename)));
        vbox.getChildren().add(fileViewMine);
        fileViewMine.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            url2 = newValue.getValue().toString();
            nuageFiles.getChildren().removeAll();
            listFileByFolder(nuageFiles,url2);
            label2.setText(url2);
        });

    }


    public void listFileByFolder(VBox vbox, String filename){
        //Filtre
        FilenameFilter fnf = (current, name) -> {
            File file = new File(current, name);
            return !file.isHidden() && !file.isDirectory();

        };

        ImageView img = new ImageView();
        img.setImage(new Image("/sample/file.png"));
        img.setFitHeight(40);
        img.setFitWidth(40);
        img.setPreserveRatio(true);

        //Deroulement
        File repertoire2 = new File(filename);
        String[] liste = repertoire2.list(fnf);
        File[] tmp = repertoire2.listFiles();
        for (int i = 0; i < liste.length; i++) {
            HBox hbox =  new HBox();
            hbox.setUserData(liste[i]);
            //hbox.getChildren().add(img);
            hbox.getChildren().add(new Label(liste[i]));
            Pane pane = new Pane();
            HBox.setHgrow(pane, Priority.ALWAYS);
            hbox.getChildren().add(pane);
            hbox.getChildren().add(new Label(getSizeOfFile(tmp[i].length())));
            vbox.getChildren().add(hbox);
            hbox.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> System.out.println(hbox.getUserData().toString()));
        }
    }

    public void setUrlFromOs(){
        if( System.getProperty("os.name").contains("Windows")){//Windows
            url1 = "C:\\";
            url2 = "D:\\";
            TreeItem t = new TreeItem("Ordinateur");
            for(File file : File.listRoots()){
                SimpleFileTreeItem To = new SimpleFileTreeItem(new File(String.valueOf(file)));
                t.getChildren().addAll(To);

            }
            TreeView<File> fileViewMine = new TreeView<File>(t);
            myFile.getChildren().add(fileViewMine);
            fileViewMine.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                url1 = newValue.getValue().toString();
                myFiles.getChildren().clear();
                listFileByFolder(myFiles,url1);
                label1.setText(url1);
            });
        }else{ // other
            url1 = "/";
            url2 = "/";
            listFile(myFile,url1);
            listFileByFolder(myFiles,url1);
        }
    }


    @FXML
    public void orderbyNuage() {
        setNewNuage("nuages");
    }
    @FXML
    public void orderbyNuageShared(){
        setNewNuage("shareNuages");
    }

    @FXML
    public void orderbyLastEdit(){
        setNewNuage("recent");
    }

    @FXML
    public void orderbyFavorite(){
        setNewNuage("favorit");
    }

    @FXML
    public void orderbyTrash(){
        setNewNuage("trash");
    }

    @FXML
    public void orderbyNothing(){
        setNewNuage("nothing");
    }

    @FXML
    public void setNewNuage(String content){
        ArrayList<Nuage> nuageToPrint;
        flowpane.getChildren().clear();
        if(content == "nuages"){
            nuageToPrint =  new ArrayList<Nuage>( nuageArray.stream().filter(type -> type.getType()== content).collect(Collectors.<Nuage>toList()));
        }else if(content == "shareNuages"){
            nuageToPrint = new ArrayList<Nuage>( nuageArray.stream().filter(type -> type.getType()== content).collect(Collectors.<Nuage>toList()));
        }else if(content == "recent"){
            nuageToPrint = new ArrayList<Nuage>( nuageArray.stream().filter(type -> type.getType()== content).collect(Collectors.<Nuage>toList()));
        }else if(content == "favorit"){
            nuageToPrint = new ArrayList<Nuage>( nuageArray.stream().filter(type -> type.getType()== content).collect(Collectors.<Nuage>toList()));
        }else if(content == "trash"){
            nuageToPrint = new ArrayList<Nuage>( nuageArray.stream().filter(type -> type.getType()== content).collect(Collectors.<Nuage>toList()));
        }else {
            nuageToPrint = nuageArray;
        }
        for(Nuage i : nuageToPrint){
            addNuage(i.getImagePath(),i.getName(),i.getLastEdit());
        }
    }


    @FXML
    public void disconnect() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("index.fxml"));
        Scene scene = new Scene(loader.load());
        ControllerIndex controllerIndex = loader.getController();
        controllerIndex.setStage(stage);
        stage.setResizable(true);
        stage.setTitle("Le-Nuage");
        stage.setScene(scene);
        scene.getStylesheets().add("sample/stylesheet.css");
        stage.show();
    }

    @FXML
    public void openProfile() throws IOException {
        /*Stage subStage = new Stage();
        subStage.setTitle("Mon profile");
        subStage.setWidth(250);
        subStage.setResizable(false);
        subStage.getIcons().add(new Image("/sample/LN.png"));
        subStage.setHeight(250);
        subStage.initOwner(stage);
        subStage.initModality(Modality.WINDOW_MODAL);
        subStage.show();*/

        Stage subStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("profil.fxml"));
        Scene scene = new Scene(loader.load(),400,600);
        ControllerProfil controllerProfil = loader.getController();
        controllerProfil.setStage(subStage);
        subStage.setResizable(false);
        subStage.setTitle("Mon profil");
        subStage.setScene(scene);
        subStage.initOwner(stage);
        subStage.initModality(Modality.WINDOW_MODAL);
        scene.getStylesheets().add("sample/stylesheet.css");
        subStage.show();
    }

    @FXML
    public void openLoading() throws IOException {
        Stage subStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Loading.fxml"));
        Scene scene = new Scene(loader.load(),400,600);
        ControllerLoading controllerLoading = loader.getController();
        ControllerLoading.setStage(subStage);
        subStage.setResizable(false);
        subStage.setTitle("Mon profil");
        subStage.setScene(scene);
        subStage.initOwner(stage);
        subStage.initModality(Modality.WINDOW_MODAL);
        scene.getStylesheets().add("sample/stylesheet.css");
        subStage.show();
    }



}
