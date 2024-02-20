package com.example.testinterface;
import prj.Partie;
import prj.PartieFacile;
import prj.Generateur;
import prj.save;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import com.google.gson.Gson;

public class Main extends Application {
    Stage window;
    Scene menuScene, gameScene;
    Image icon = new Image("file:src/main/resources/img/logo.png");
    final double MAX_FONT_SIZE = 25;

    public static void main(String[] args){ //METHODE AU LANCEMENT
        launch(args);
    }

    @Override
    public void start(@SuppressWarnings("exports") Stage primaryStage) throws Exception {
        window = primaryStage;
        
        //Creation des files 
        File recordsDir = new File(System.getProperty("user.home"), ".Qui-est-ce-GRPMA/records");
        if (! recordsDir.exists()) {
            recordsDir.mkdirs();
        }
        File Entitefile = new File(System.getProperty("user.home"), ".Qui-est-ce-GRPMA/records/EntiteFile");
        if (! Entitefile.exists()) {
        	Entitefile.mkdirs();
        }
        File gene = new File(System.getProperty("user.home"), ".Qui-est-ce-GRPMA/records/FileGene");
        if (! gene.exists()) {
        	gene.mkdirs();
        }
        File test = new File (System.getProperty("user.home")+"\\.Qui-est-ce-GRPMA\\records\\EntiteFile\\Entite.json");
        test.createNewFile();
        Files.copy(Paths.get("src/main/resources/Entite.json"),Paths.get(System.getProperty("user.home")+"\\.Qui-est-ce-GRPMA\\records\\EntiteFile\\Entite.json"),StandardCopyOption.REPLACE_EXISTING);
        File test2 = new File (System.getProperty("user.home")+"\\.Qui-est-ce-GRPMA\\records\\ExeEntite.json");
        test2.createNewFile();
        Files.copy(Paths.get("src/main/resources/ExeEntite.json"), Paths.get(System.getProperty("user.home")+"\\.Qui-est-ce-GRPMA\\records\\ExeEntite.json"),StandardCopyOption.REPLACE_EXISTING);
        File test3 = new File (System.getProperty("user.home")+"\\.Qui-est-ce-GRPMA\\records\\FileGene\\GenerateurTest.json");
        test3.createNewFile();
        Files.copy(Paths.get("src/main/resources/GenerateurTest.json"), Paths.get(System.getProperty("user.home")+"\\.Qui-est-ce-GRPMA\\records\\FileGene\\GenerateurTest.json"),StandardCopyOption.REPLACE_EXISTING);
        //Declaration de(s) label(s) MENU SCENE
        Label titreMenu = new Label("MENU PRINCIPAL");
        titreMenu.setFont(new Font(MAX_FONT_SIZE));
        titreMenu.setTextFill(Color.web("#FFFFFF"));

        //Declaration des boutons MENU SCENE
        Button newGame = new Button("Nouvelle partie / charger partie");
        Button generateur = new Button("Generateur");
        Button extension = new Button("Inverser les roles");
        //Mise en place des events
        newGame.setOnAction(e -> chooseDifficultyBox());
        generateur.setOnAction(e -> chooseFileBoxGenerateur());
        extension.setOnAction(e -> InverseBox());

        //Declaration des layouts MENU SCENE
        FlowPane layoutMenu = new FlowPane(Orientation.VERTICAL);
        layoutMenu.setPrefWidth(200);
        layoutMenu.setStyle("-fx-background-color: #666699;");
        layoutMenu.setVgap(50);
        layoutMenu.setAlignment(Pos.TOP_CENTER);

        //Mise en forme MENU SCENE
        layoutMenu.getChildren().addAll(titreMenu ,newGame, generateur,extension);
        menuScene = new Scene(layoutMenu, 400, 550);
        titreMenu.setAlignment(Pos.CENTER);
        titreMenu.setMaxSize(200,100);
        newGame.setMinWidth(layoutMenu.getPrefWidth());
        extension.setMinWidth(layoutMenu.getPrefWidth());
        generateur.setMinWidth(layoutMenu.getPrefWidth());
        //Mise en place de la scene de MENU SCENE
        window.setScene(menuScene);
        window.setTitle("Menu Principal - Qui est-ce");
        window.getIcons().add(icon);
        window.setMinWidth(400);
        window.setMinHeight(550);
        window.show();

    }

    public void startGame(int row, int col,int mystere, int t, String pathJsonEntite, ArrayList<Integer> coches){
    	//CREATION PARTIE
    	Partie partie = new Partie(pathJsonEntite,row,col,t,mystere,coches);
    	System.out.println("Partie créee");
        //CREATION DU BORDER LAYOUT DE JEU
        BorderPane layoutGame = new BorderPane();
        System.out.println("Layout1 crée");
        //CREATION DU LAYOUT POUR QUITTER/SAUVEGARDER
        HBox menuGame = new HBox();
        menuGame.setAlignment(Pos.CENTER);
        System.out.println("Layout2 crée");
        //CREATION DU GRIDPANE POUR AFFICHER LES IMAGES
        GridPane grilleImg = new GridPane();
        grilleImg.setPadding(new Insets(10,10,10,10));
        grilleImg.setVgap(8);
        grilleImg.setHgap(8);
        grilleImg.setAlignment(Pos.CENTER);
        System.out.println("Gridpane crée");
        //CREATION GRIDPANE LAYOUT INTERACTION AVEC LE JEU
        GridPane interaction = new GridPane();
        interaction.setPrefWidth(150);
        interaction.setPadding(new Insets(10,10,10,10));
        interaction.setVgap(8);
        interaction.setHgap(8);
        interaction.setAlignment(Pos.CENTER);
        System.out.println("GridpaneLayout crée");
        //DECLARATION TEXTE INTERACTION
        Text cpCoups = new Text("Question 1");
        Text cpTemps = new Text("Question 2");
        Text reponseProgramme = new Text("Connecteur (NON = Pas de Q2)");
        GridPane.setConstraints(reponseProgramme,2,3);
        GridPane.setConstraints(cpCoups,0,0);
        GridPane.setConstraints(cpTemps,0,2);
        System.out.println("TexteInteract crée");
        //DECLARATION COMBOBOX INTERACTION
        ComboBox<String> typeAttribut1 = new ComboBox<>();
        typeAttribut1.setMinWidth(interaction.getPrefWidth());
        for (int i=1 ; i<partie.getListeEntites().get(0).getKeys().size();i++) {
            typeAttribut1.getItems().add(partie.getListeEntites().get(0).getKeys().get(i));}
        ComboBox<String> attribut1 = new ComboBox<>();
        attribut1.setMinWidth(interaction.getPrefWidth());
        attribut1.setOnMouseClicked(e -> {
        	attribut1.getItems().clear();
        	if(typeAttribut1.getSelectionModel().getSelectedItem() != null) {
        	ArrayList<String> attr1 = new ArrayList<String>(partie.getAllChoicesByKey(typeAttribut1.getSelectionModel().getSelectedItem()));
            System.out.println("Attr1 Array fait");
            attr1.forEach( (n) -> { attribut1.getItems().add(n); });
            System.out.println("Attr1 fait");
            }} );
        ComboBox<String> connecteur = new ComboBox<>();
        connecteur.getItems().addAll("ET","OU","NON");
        connecteur.setMinWidth(interaction.getPrefWidth());
        System.out.println("Connecteur fait");
        ComboBox<String> typeAttribut2 = new ComboBox<>();
        typeAttribut2.setMinWidth(interaction.getPrefWidth());
        for (int i=1 ; i<partie.getListeEntites().get(0).getKeys().size();i++) {
            typeAttribut2.getItems().add(partie.getListeEntites().get(0).getKeys().get(i));}
        System.out.println("Typeattr1 fait");
        ComboBox<String> attribut2 = new ComboBox<>();
        attribut2.setMinWidth(interaction.getPrefWidth());
        attribut2.setOnMouseClicked(e -> {
        	attribut2.getItems().clear();
        	if(typeAttribut2.getSelectionModel().getSelectedItem() != null) {
        	ArrayList<String> attr1 = new ArrayList<String>(partie.getAllChoicesByKey(typeAttribut2.getSelectionModel().getSelectedItem()));
            System.out.println("Attr1 Array fait");
            attr1.forEach( (n) -> { attribut2.getItems().add(n); });
            System.out.println("Attr1 fait");
            }} );
        GridPane.setConstraints(connecteur,2,1);
        GridPane.setConstraints(attribut1,3,0);
        GridPane.setConstraints(typeAttribut1,1,0);
        GridPane.setConstraints(attribut2,3,2);
        GridPane.setConstraints(typeAttribut2,1,2);
        System.out.println("Combobox crée");
        //DECLATION BOUTTON INTERACTION
        Button validerQ = new Button("Valider la question");
        validerQ.setMinWidth(interaction.getPrefWidth());
        GridPane.setConstraints(validerQ,4,1);
        validerQ.setOnAction(e -> {
        	if(connecteur.getSelectionModel().getSelectedItem() != null &&  typeAttribut1.getSelectionModel().getSelectedItem() != null && attribut1.getSelectionModel().getSelectedItem() != null) 
        	{ if(connecteur.getSelectionModel().getSelectedItem() == "NON" || (typeAttribut2.getSelectionModel().getSelectedItem() != null && attribut2.getSelectionModel().getSelectedItem() != null ) ) 
        	{
        		partie.incrT();
       		 Stage alertBoxWindow = new Stage();
             alertBoxWindow.initModality(Modality.APPLICATION_MODAL); //PERMET DE BLOQUER L'ACCES AUX AUTRES FENETRES
             alertBoxWindow.setTitle("Reponse");
             alertBoxWindow.setMinWidth(250);
             alertBoxWindow.getIcons().add(new Image("file:src/main/resources/img/logo.png"));
            	if(partie.verifQuestion(connecteur.getSelectionModel().getSelectedItem(),typeAttribut1.getSelectionModel().getSelectedItem(),attribut1.getSelectionModel().getSelectedItem(),typeAttribut2.getSelectionModel().getSelectedItem(),attribut2.getSelectionModel().getSelectedItem()))
            	{
                     //DECLARATION LABEL ET LAYOUT POUR VRAI
                     Label messageLabel = new Label("L'Entite mystère contient cet/ces attributs.");
                     HBox alertBoxLayout = new HBox(10);
                     Button easyButton = new Button("fermer");
                     alertBoxLayout.getChildren().addAll(messageLabel,easyButton);
                     alertBoxLayout.setAlignment(Pos.CENTER);
                     easyButton.setOnAction(v -> {alertBoxWindow.close();
                     });
                   //DECLARATION SCENE
                     Scene alertBoxScene = new Scene(alertBoxLayout);
                     alertBoxWindow.setScene(alertBoxScene);
                     alertBoxWindow.show();
            	}
            	else {
            	     //DECLARATION LABEL ET LAYOUT POUR FAUX
                    Label messageLabel = new Label("L'Entite mystère ne pas contient cet/ces attributs.");
                    HBox alertBoxLayout = new HBox(10);
                    Button easyButton = new Button("fermer");
                    alertBoxLayout.getChildren().addAll(messageLabel,easyButton);
                    alertBoxLayout.setAlignment(Pos.CENTER);
                    easyButton.setOnAction(v -> {alertBoxWindow.close();
                    });
                  //DECLARATION SCENE
                    Scene alertBoxScene = new Scene(alertBoxLayout);
                    alertBoxWindow.setScene(alertBoxScene);
                    alertBoxWindow.show();
            	}
        	}
        	}
        		
        	});
        System.out.println("ValidéQ crée");
        //AJOUT DE TOUT LES NODES PRECEDANT DANS LE LAYOUT INTERACTION
        interaction.getChildren().addAll(cpCoups,cpTemps, typeAttribut1, attribut1, connecteur,typeAttribut2, attribut2, reponseProgramme, validerQ);
        System.out.println("Nodes Placées");
        //DECLARATION BOUTTON DU MENU DE JEU
        Button quitButton = new Button("Quitter");
        Button saveButton = new Button("Sauvegarder");
        menuGame.getChildren().addAll(quitButton,saveButton);
        quitButton.setOnAction(e -> window.setScene(menuScene));
        saveButton.setOnAction(e ->{  Stage alertBoxWindow = new Stage();
        alertBoxWindow.initModality(Modality.APPLICATION_MODAL); //PERMET DE BLOQUER L'ACCES AUX AUTRES FENETRES
        alertBoxWindow.setTitle("Reponse");
        alertBoxWindow.setMinWidth(250);
        alertBoxWindow.getIcons().add(new Image("file:src/main/resources/img/logo.png"));
                //DECLARATION LABEL ET LAYOUT POUR VRAI
                Label messageLabel = new Label("Choisir un nom de sauvegarde");
                TextArea text = new TextArea();
                text.setText("NomSave");
                text.setMinWidth(interaction.getPrefWidth());
                text.setMaxHeight(20);
                text.setMaxWidth(20);
                HBox alertBoxLayout = new HBox(10);
                Button easyButton = new Button("Finir");
                alertBoxLayout.getChildren().addAll(messageLabel,text,easyButton);
                alertBoxLayout.setAlignment(Pos.CENTER);
                easyButton.setOnAction(v -> {partie.save(text.getText());
                	alertBoxWindow.close();
                });
              //DECLARATION SCENE
                Scene alertBoxScene = new Scene(alertBoxLayout);
                alertBoxWindow.setScene(alertBoxScene);
                alertBoxWindow.show();});
        System.out.println("Boutons menus Placés");
        //CREATION TABLEAU CONTENANT TOUTE LES PERSONNAGES SOUS FORME 
        Button [] listePerso = new Button[col*row];
        for (int i = 0; i<row*col;i++){
            String path2 = partie.getListeEntites().get(i).getAttributs().get("path").get(0);
            listePerso[i] = new Button("",new ImageView(new Image (path2)));
            grilleImg.getChildren().add(listePerso[i]);
            if(partie.getListeEntites().get(i).getBarre()) listePerso[i].setOpacity(0.5);
        }
        System.out.println("tableau crée");
        //INSERTION DE TOUTE LES IMAGES DU TABLEAU DANS LE GRIDPANE 
        int cp = 0;
        for (int r=0; r< row;r++) {
            for (int c=0; c<col; c++){
                GridPane.setConstraints(listePerso[cp],c,r);
                cp++;
            }
        }
        System.out.println("images placées");
        //EVENT ON CLICK DES PERSONNAGES // ajouter la partie avec les méthodes de partie
        for (cp = 0; cp<row*col; cp++){
            int finalCp = cp;
            listePerso[cp].setOnAction(e -> { if(listePerso[finalCp].getOpacity()==1) 
            	listePerso[finalCp].setOpacity(0.5); 
            else 
            	listePerso[finalCp].setOpacity(1);
            partie.coche(partie.getListeEntites().get(finalCp));
            if(partie.getcountbarre() == partie.getlignes()*partie.getcolonnes()-1) {
            	  Stage alertBoxWindow = new Stage();
                  alertBoxWindow.initModality(Modality.APPLICATION_MODAL); //PERMET DE BLOQUER L'ACCES AUX AUTRES FENETRES
                  alertBoxWindow.setTitle("fin de partie");
                  alertBoxWindow.setMinWidth(250);
                  alertBoxWindow.getIcons().add(new Image("file:src/main/resources/img/logo.png"));
            	if(partie.finpartie()) {
                      //DECLARATION LABEL ET LAYOUT POUR VICTOIRE
                      Label messageLabel = new Label("Bravo vous avez gagné");
                      HBox alertBoxLayout = new HBox(10);
                      Button easyButton = new Button("Quitter");
                      alertBoxLayout.getChildren().addAll(messageLabel,easyButton);
                      alertBoxLayout.setAlignment(Pos.CENTER);
                      easyButton.setOnAction(v -> {alertBoxWindow.close();
                      window.close();});
                    //DECLARATION SCENE
                      Scene alertBoxScene = new Scene(alertBoxLayout);
                      alertBoxWindow.setScene(alertBoxScene);
                      alertBoxWindow.show();
            	}
            	else {
            		 //DECLARATION LABEL ET LAYOUT POUR DEFAITE
                    Label messageLabel = new Label("Vous vous êtes trompés, réessayer?");
                    HBox alertBoxLayout = new HBox(10);
                    Button easyButton = new Button("Quitter");
                    alertBoxLayout.getChildren().addAll(messageLabel,easyButton);
                    alertBoxLayout.setAlignment(Pos.CENTER);
                    easyButton.setOnAction(v -> {alertBoxWindow.close();
                    window.close();});
                  //DECLARATION SCENE
                    Scene alertBoxScene = new Scene(alertBoxLayout);
                    alertBoxWindow.setScene(alertBoxScene);
                    alertBoxWindow.show();
            	}
            }
            });}
           

        System.out.println("coche fait");
        //INSERTION DANS LE BORDERPANE DES LAYOUT DE MENU ET DE PERSONNAGE
        layoutGame.setTop(menuGame);
        layoutGame.setCenter(grilleImg);
        layoutGame.setBottom(interaction);

        gameScene = new Scene(layoutGame);
        window.setScene(gameScene);
        window.setTitle("Qui est-ce");
    }

    public void startGameFacile(int row, int col,int mystere, int t, String pathJsonEntite, ArrayList<Integer> coches){
    	//CREATION PARTIE
    	PartieFacile partie = new PartieFacile(pathJsonEntite,row,col,t,mystere,coches);
      	System.out.println("Partie créee");
        //CREATION DU BORDER LAYOUT DE JEU
        BorderPane layoutGame = new BorderPane();
        System.out.println("Layout1 crée");
        //CREATION DU LAYOUT POUR QUITTER/SAUVEGARDER
        HBox menuGame = new HBox();
        menuGame.setAlignment(Pos.CENTER);
        System.out.println("Layout2 crée");
        //CREATION DU GRIDPANE POUR AFFICHER LES IMAGES
        GridPane grilleImg = new GridPane();
        grilleImg.setPadding(new Insets(10,10,10,10));
        grilleImg.setVgap(8);
        grilleImg.setHgap(8);
        grilleImg.setAlignment(Pos.CENTER);
        System.out.println("Gridpane crée");
        //CREATION GRIDPANE LAYOUT INTERACTION AVEC LE JEU
        GridPane interaction = new GridPane();
        interaction.setPrefWidth(150);
        interaction.setPadding(new Insets(10,10,10,10));
        interaction.setVgap(8);
        interaction.setHgap(8);
        interaction.setAlignment(Pos.CENTER);
        System.out.println("GridpaneLayout crée");
        //DECLARATION TEXTE INTERACTION
        Text cpCoups = new Text("Question 1");
        Text cpTemps = new Text("Question 2");
        Text reponseProgramme = new Text("Connecteur (NON = Pas de Q2)");
        GridPane.setConstraints(reponseProgramme,2,3);
        GridPane.setConstraints(cpCoups,0,0);
        GridPane.setConstraints(cpTemps,0,2);
        System.out.println("TexteInteract crée");
        //DECLARATION COMBOBOX INTERACTION
        ComboBox<String> typeAttribut1 = new ComboBox<>();
        typeAttribut1.setMinWidth(interaction.getPrefWidth());
        for (int i=1 ; i<partie.getListeEntites().get(0).getKeys().size();i++) {
            typeAttribut1.getItems().add(partie.getListeEntites().get(0).getKeys().get(i));}
        System.out.println("Typeattr1 fait");
        ComboBox<String> attribut1 = new ComboBox<>();
        attribut1.setMinWidth(interaction.getPrefWidth());
        attribut1.setOnMouseClicked(e -> {
        	attribut1.getItems().clear();
        	if(typeAttribut1.getSelectionModel().getSelectedItem() != null) {
        	ArrayList<String> attr1 = new ArrayList<String>(partie.getAllChoicesByKey(typeAttribut1.getSelectionModel().getSelectedItem()));
            System.out.println("Attr1 Array fait");
            attr1.forEach( (n) -> { attribut1.getItems().add(n); });
            System.out.println("Attr1 fait");
            }} );
        ComboBox<String> connecteur = new ComboBox<>();
        connecteur.getItems().addAll("ET","OU","NON");
        connecteur.setMinWidth(interaction.getPrefWidth());
        System.out.println("Connecteur fait");
        ComboBox<String> typeAttribut2 = new ComboBox<>();
        typeAttribut2.setMinWidth(interaction.getPrefWidth());
        for (int i=1 ; i<partie.getListeEntites().get(0).getKeys().size();i++) {
            typeAttribut2.getItems().add(partie.getListeEntites().get(0).getKeys().get(i));}
        System.out.println("Typeattr1 fait");
        ComboBox<String> attribut2 = new ComboBox<>();
        attribut2.setMinWidth(interaction.getPrefWidth());
        attribut2.setOnMouseClicked(e -> {
        	attribut2.getItems().clear();
        	if(typeAttribut2.getSelectionModel().getSelectedItem() != null) {
        	ArrayList<String> attr1 = new ArrayList<String>(partie.getAllChoicesByKey(typeAttribut2.getSelectionModel().getSelectedItem()));
            System.out.println("Attr1 Array fait");
            attr1.forEach( (n) -> { attribut2.getItems().add(n); });
            System.out.println("Attr1 fait");
            }} );
        typeAttribut1.setOnMouseClicked((b) ->{ attribut1.getItems().clear();});
        typeAttribut2.setOnMouseClicked((b) ->{ attribut2.getItems().clear();});
        GridPane.setConstraints(connecteur,2,1);
        GridPane.setConstraints(attribut1,3,0);
        GridPane.setConstraints(typeAttribut1,1,0);
        GridPane.setConstraints(attribut2,3,2);
        GridPane.setConstraints(typeAttribut2,1,2);
        System.out.println("Combobox crée");


        //DECLARATION BOUTTON DU MENU DE JEU
        Button quitButton = new Button("Quitter");
        Button saveButton = new Button("Sauvegarder");
        menuGame.getChildren().addAll(quitButton,saveButton);
        quitButton.setOnAction(e -> window.setScene(menuScene));
        saveButton.setOnAction(e ->{  Stage alertBoxWindow = new Stage();
        alertBoxWindow.initModality(Modality.APPLICATION_MODAL); //PERMET DE BLOQUER L'ACCES AUX AUTRES FENETRES
        alertBoxWindow.setTitle("Reponse");
        alertBoxWindow.setMinWidth(250);
        alertBoxWindow.getIcons().add(new Image("file:src/main/resources/img/logo.png"));
                //DECLARATION LABEL ET LAYOUT POUR VRAI
                Label messageLabel = new Label("Choisir un nom de sauvegarde");
                TextArea text = new TextArea();
                text.setText("NomSave");
                text.setMinWidth(interaction.getPrefWidth());
                text.setMaxHeight(20);
                text.setMaxWidth(20);
                HBox alertBoxLayout = new HBox(10);
                Button easyButton = new Button("Finir");
                alertBoxLayout.getChildren().addAll(messageLabel,text,easyButton);
                alertBoxLayout.setAlignment(Pos.CENTER);
                easyButton.setOnAction(v -> {partie.save(text.getText());
                	alertBoxWindow.close();
                });
              //DECLARATION SCENE
                Scene alertBoxScene = new Scene(alertBoxLayout);
                alertBoxWindow.setScene(alertBoxScene);
                alertBoxWindow.show();});
        //CREATION TABLEAU CONTENANT TOUS LES PERSONNAGES
        Button [] listePerso = new Button[col*row];
        for (int i = 0; i<row*col;i++){
            String path2 = partie.getListeEntites().get(i).getAttributs().get("path").get(0);
            listePerso[i] = new Button("",new ImageView(new Image (path2)));
            grilleImg.getChildren().add(listePerso[i]);
            if(partie.getListeEntites().get(i).getBarre()) listePerso[i].setOpacity(0.5);
        }
        System.out.println("tableau crée");
        //INSERTION DE TOUTE LES IMAGES DU TABLEAU DANS LE GRIDPANE 
        int cp = 0;
        for (int r=0; r< row;r++) {
            for (int c=0; c<col; c++){
                GridPane.setConstraints(listePerso[cp],c,r);
                cp++;
            }
        }
        System.out.println("images placées");
        //EVENT ON CLICK DES PERSONNAGES // ajouter la partie avec les méthodes de partie
        for (cp = 0; cp<row*col; cp++){
            int finalCp = cp;
            listePerso[cp].setOnAction(e -> { if(listePerso[finalCp].getOpacity()==1) 
            	listePerso[finalCp].setOpacity(0.5); 
            else 
            	listePerso[finalCp].setOpacity(1);
            partie.coche(partie.getListeEntites().get(finalCp));
            if(partie.getcountbarre() == partie.getlignes()*partie.getcolonnes()-1) {
            	  Stage alertBoxWindow = new Stage();
                  alertBoxWindow.initModality(Modality.APPLICATION_MODAL); //PERMET DE BLOQUER L'ACCES AUX AUTRES FENETRES
                  alertBoxWindow.setTitle("fin de partie");
                  alertBoxWindow.setMinWidth(250);
                  alertBoxWindow.getIcons().add(new Image("file:src/main/resources/img/logo.png"));
            	if(partie.finpartie()) {
                      //DECLARATION LABEL ET LAYOUT POUR VICTOIRE
                      Label messageLabel = new Label("Bravo vous avez gagné");
                      HBox alertBoxLayout = new HBox(10);
                      Button easyButton = new Button("Quitter");
                      alertBoxLayout.getChildren().addAll(messageLabel,easyButton);
                      alertBoxLayout.setAlignment(Pos.CENTER);
                      easyButton.setOnAction(v -> {alertBoxWindow.close();
                      window.close();});
                    //DECLARATION SCENE
                      Scene alertBoxScene = new Scene(alertBoxLayout);
                      alertBoxWindow.setScene(alertBoxScene);
                      alertBoxWindow.show();
            	}
            	else {
            		 //DECLARATION LABEL ET LAYOUT POUR DEFAITE
                    Label messageLabel = new Label("Vous vous êtes trompés, réessayer?");
                    HBox alertBoxLayout = new HBox(10);
                    Button easyButton = new Button("Quitter");
                    alertBoxLayout.getChildren().addAll(messageLabel,easyButton);
                    alertBoxLayout.setAlignment(Pos.CENTER);
                    easyButton.setOnAction(v -> {alertBoxWindow.close();
                    window.close();});
                  //DECLARATION SCENE
                    Scene alertBoxScene = new Scene(alertBoxLayout);
                    alertBoxWindow.setScene(alertBoxScene);
                    alertBoxWindow.show();
            	}
            }
            });}
           

        System.out.println("coche fait");

        //DECLATION BOUTTON INTERACTION
        Button validerQ = new Button("Valider la question");
        validerQ.setMinWidth(interaction.getPrefWidth());
        GridPane.setConstraints(validerQ,4,1);
        validerQ.setOnAction(e -> {
        	if(connecteur.getSelectionModel().getSelectedItem() != null &&  typeAttribut1.getSelectionModel().getSelectedItem() != null && attribut1.getSelectionModel().getSelectedItem() != null) 
        	{ if(connecteur.getSelectionModel().getSelectedItem() == "NON" || (typeAttribut2.getSelectionModel().getSelectedItem() != null && attribut2.getSelectionModel().getSelectedItem() != null ) ) 
        	{
        		partie.incrT();
       		 Stage alertBoxWindow = new Stage();
             alertBoxWindow.initModality(Modality.APPLICATION_MODAL); //PERMET DE BLOQUER L'ACCES AUX AUTRES FENETRES
             alertBoxWindow.setTitle("Reponse");
             alertBoxWindow.setMinWidth(250);
             alertBoxWindow.getIcons().add(new Image("file:src/main/resources/img/logo.png"));
            	if(partie.verifQuestion(connecteur.getSelectionModel().getSelectedItem(),typeAttribut1.getSelectionModel().getSelectedItem(),attribut1.getSelectionModel().getSelectedItem(),typeAttribut2.getSelectionModel().getSelectedItem(),attribut2.getSelectionModel().getSelectedItem()))
            	{
                     //DECLARATION LABEL ET LAYOUT POUR VRAI
                     Label messageLabel = new Label("L'Entite mystère contient cet/ces attributs.");
                     HBox alertBoxLayout = new HBox(10);
                     Button easyButton = new Button("fermer");
                     alertBoxLayout.getChildren().addAll(messageLabel,easyButton);
                     alertBoxLayout.setAlignment(Pos.CENTER);
                     easyButton.setOnAction(v -> {alertBoxWindow.close();
                     });
                   //DECLARATION SCENE
                     Scene alertBoxScene = new Scene(alertBoxLayout);
                     alertBoxWindow.setScene(alertBoxScene);
                     alertBoxWindow.show();
            	}
            	else {
            	     //DECLARATION LABEL ET LAYOUT POUR FAUX
                    Label messageLabel = new Label("L'Entite mystère ne pas contient cet/ces attributs.");
                    HBox alertBoxLayout = new HBox(10);
                    Button easyButton = new Button("fermer");
                    alertBoxLayout.getChildren().addAll(messageLabel,easyButton);
                    alertBoxLayout.setAlignment(Pos.CENTER);
                    easyButton.setOnAction(v -> {alertBoxWindow.close();
                    });
                  //DECLARATION SCENE
                    Scene alertBoxScene = new Scene(alertBoxLayout);
                    alertBoxWindow.setScene(alertBoxScene);
                    alertBoxWindow.show();
            	}
        	
                for ( int i=0 ; i<partie.getListeEntites().size();i++) {
                	
             	   if(partie.getListeEntites().get(i).getBarre()) {
             		   listePerso[i].setOpacity(0.5);
                }
             	   else
             	   {
             		   listePerso[i].setOpacity(1); }
                }
                if(partie.getcountbarre() == partie.getlignes()*partie.getcolonnes()-1) {
                	  Stage alertBoxeWindow = new Stage();
                      alertBoxeWindow.initModality(Modality.APPLICATION_MODAL); //PERMET DE BLOQUER L'ACCES AUX AUTRES FENETRES
                      alertBoxeWindow.setTitle("fin de partie");
                      alertBoxeWindow.setMinWidth(250);
                      alertBoxeWindow.getIcons().add(new Image("file:src/main/resources/img/logo.png"));
                	if(partie.finpartie()) {
                          //DECLARATION LABEL ET LAYOUT POUR VICTOIRE
                          Label messageLabel = new Label("Bravo vous avez gagné");
                          HBox alertBoxLayout = new HBox(10);
                          Button easyButton = new Button("Quitter");
                          alertBoxLayout.getChildren().addAll(messageLabel,easyButton);
                          alertBoxLayout.setAlignment(Pos.CENTER);
                          easyButton.setOnAction(v -> {alertBoxeWindow.close();
                          window.close();});
                        //DECLARATION SCENE
                          Scene alertBoxScene = new Scene(alertBoxLayout);
                          alertBoxeWindow.setScene(alertBoxScene);
                          alertBoxeWindow.show();
                	}
                	else {
                		 //DECLARATION LABEL ET LAYOUT POUR DEFAITE
                        Label messageLabel = new Label("Vous vous êtes trompés, réessayer?");
                        HBox alertBoxLayout = new HBox(10);
                        Button easyButton = new Button("Quitter");
                        alertBoxLayout.getChildren().addAll(messageLabel,easyButton);
                        alertBoxLayout.setAlignment(Pos.CENTER);
                        easyButton.setOnAction(v -> {alertBoxeWindow.close();
                        window.close();});
                      //DECLARATION SCENE
                        Scene alertBoxScene = new Scene(alertBoxLayout);
                        alertBoxeWindow.setScene(alertBoxScene);
                        alertBoxeWindow.show();
                	}
                }
        	
        	
        		
                }}});
           
          
           //AJOUT DE TOUT LES NODES PRECEDANT DANS LE LAYOUT INTERACTION
           interaction.getChildren().addAll(cpCoups,cpTemps, typeAttribut1, attribut1, connecteur,typeAttribut2, attribut2, reponseProgramme,validerQ);
        //INSERTION DANS LE BORDERPANE DES LAYOUT DE MENU ET DE PERSONNAGE
        layoutGame.setTop(menuGame);
        layoutGame.setCenter(grilleImg);
        layoutGame.setBottom(interaction);

        gameScene = new Scene(layoutGame);
        window.setScene(gameScene);
        window.setTitle("Qui est-ce");
    }
    
    public void chooseFileBox (boolean facile) {
            //DECLARATION STAGE
            Stage alertBoxWindow = new Stage();
            alertBoxWindow.initModality(Modality.APPLICATION_MODAL); //PERMET DE BLOQUER L'ACCES AUX AUTRES FENETRES
            alertBoxWindow.setTitle("Selectionner JSON");
            alertBoxWindow.setMinWidth(250);
            alertBoxWindow.getIcons().add(new Image("file:src/main/resources/img/logo.png"));
            
            //Creation fichier pour sauvegardes
            File recordsDir = new File(System.getProperty("user.home"), ".Qui-est-ce-GRPMA/records");
            if (! recordsDir.exists()) {
                recordsDir.mkdirs();
            }
            FileChooser FileChooser = new FileChooser();

            FileChooser.setInitialDirectory(recordsDir);
            FileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Json", "*.json"));
            
            //DECLARATION LABEL
            Label messageLabel = new Label("Sélectionner un fichier JSON de sauvegarde");

            //DECLARATION BOUTTONS
            Button easyButton = new Button("Choisir");      
            //GERE L'ACTION DES BOUTTONS
            easyButton.setOnAction(e -> {
            	File selectedFile = FileChooser.showOpenDialog(alertBoxWindow);;
            	System.out.println(selectedFile.getPath());
            	System.out.println(System.getProperty("user.home"));
            	 Gson gson = new Gson();
            	 Reader reader;
				try {
					reader = Files.newBufferedReader(selectedFile.toPath());
					 save save = gson.fromJson(reader,save.class);
	            	 reader.close();
	            	 System.out.println(save.getM());
	            	 System.out.println(save.getJ());
	            	 if(facile)
	            	startGameFacile(save.getL(),save.getC(),save.getM(),save.getT(),save.getJ(),save.getCo());
	            	 else
	            	startGame(save.getL(),save.getC(),save.getM(),save.getT(),save.getJ(),save.getCo());
				} 
				catch (IOException e1) {
					e1.printStackTrace();
				}            	 
                alertBoxWindow.close();});

            //DECLARATION LAYOUT
            HBox alertBoxLayout = new HBox(10);
            alertBoxLayout.getChildren().addAll(messageLabel, easyButton);
            alertBoxLayout.setAlignment(Pos.CENTER);

            //DECLARATION SCENE
            Scene alertBoxScene = new Scene(alertBoxLayout);
            alertBoxWindow.setScene(alertBoxScene);
            alertBoxWindow.show();

        }public void  InverseBox() {
        	   //DECLARATION STAGE
            Stage alertBoxWindow = new Stage();
            alertBoxWindow.initModality(Modality.APPLICATION_MODAL); //PERMET DE BLOQUER L'ACCES AUX AUTRES FENETRES
            alertBoxWindow.setTitle("Selectionner JSON");
            alertBoxWindow.setMinWidth(250);
            alertBoxWindow.getIcons().add(new Image("file:src/main/resources/img/logo.png"));
            
            //Creation fichier pour sauvegardes
            File recordsDir = new File(System.getProperty("user.home"), ".Qui-est-ce-GRPMA/records");
            if (! recordsDir.exists()) {
                recordsDir.mkdirs();
            }
            FileChooser FileChooser = new FileChooser();

            FileChooser.setInitialDirectory(recordsDir);
            FileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Json", "*.json"));
            
            //DECLARATION LABEL
            Label messageLabel = new Label("Sélectionner un fichier JSON de sauvegarde");

            //DECLARATION BOUTTONS
            Button easyButton = new Button("Choisir");      
            //GERE L'ACTION DES BOUTTONS
            easyButton.setOnAction(e -> {
            	File selectedFile = FileChooser.showOpenDialog(alertBoxWindow);;
            	System.out.println(selectedFile.getPath());
            	System.out.println(System.getProperty("user.home"));
            	 Gson gson = new Gson();
            	 Reader reader;
				try {
					reader = Files.newBufferedReader(selectedFile.toPath());
					 save save = gson.fromJson(reader,save.class);
	            	 reader.close();
	            	StartInverse(save.getL(),save.getC(),save.getT(),save.getJ(),save.getCo());
				} 
				catch (IOException e1) {
					e1.printStackTrace();
				}            	 
                alertBoxWindow.close();});

            //DECLARATION LAYOUT
            HBox alertBoxLayout = new HBox(10);
            alertBoxLayout.getChildren().addAll(messageLabel, easyButton);
            alertBoxLayout.setAlignment(Pos.CENTER);

            //DECLARATION SCENE
            Scene alertBoxScene = new Scene(alertBoxLayout);
            alertBoxWindow.setScene(alertBoxScene);
            alertBoxWindow.show();

        }
    public void StartInverse(int row, int col, int t, String pathJsonEntite, ArrayList<Integer> coches){
    	//CREATION PARTIE
    	PartieFacile partie = new PartieFacile(pathJsonEntite,row,col,t,0,coches);
    	System.out.println("Partie crée");
        //CREATION DU BORDER LAYOUT DE JEU
        BorderPane layoutGame = new BorderPane();
        System.out.println("Layout1 crée");
        //CREATION DU LAYOUT POUR QUITTER/SAUVEGARDER
        HBox menuGame = new HBox();
        menuGame.setAlignment(Pos.CENTER);
        System.out.println("Layout2 crée");
        //CREATION DU GRIDPANE POUR AFFICHER LES IMAGES
        GridPane grilleImg = new GridPane();
        grilleImg.setPadding(new Insets(10,10,10,10));
        grilleImg.setVgap(8);
        grilleImg.setHgap(8);
        grilleImg.setAlignment(Pos.CENTER);
        System.out.println("Gridpane crée");
        //CREATION GRIDPANE LAYOUT INTERACTION AVEC LE JEU
        GridPane interaction = new GridPane();
        interaction.setPrefWidth(150);
        interaction.setPadding(new Insets(10,10,10,10));
        interaction.setVgap(8);
        interaction.setHgap(8);
        interaction.setAlignment(Pos.CENTER);
        System.out.println("GridpaneLayout crée");
        //DECLARATION TEXTE INTERACTION
        Text cpCoups = new Text("Question Programme");
        Text reponseProgramme = new Text("");
        GridPane.setConstraints(reponseProgramme,1,1);
        GridPane.setConstraints(cpCoups,1,0);
        System.out.println("TexteInteract crée");
        //DECLARATION COMBOBOX INTERACTION
      
        //DECLATION BOUTTON INTERACTION
        Button non = new Button ("NON");
        Button oui = new Button ("OUI");
        Button validerQ = new Button("Question Suivante");
        validerQ.setMinWidth(interaction.getPrefWidth());
        GridPane.setConstraints(validerQ,5,1);
        GridPane.setConstraints(non,0,3);
        GridPane.setConstraints(oui,2,3);
        //DECLARATION BOUTTON DU MENU DE JEU
        Button quitButton = new Button("Quitter");
        menuGame.getChildren().addAll(quitButton);
        quitButton.setOnAction(e -> window.setScene(menuScene));
        System.out.println("Boutons menus Placés");
        //CREATION TABLEAU CONTENANT TOUTE LES PERSONNAGES SOUS FORME 
        Button [] listePerso = new Button[col*row];
        for (int i = 0; i<row*col;i++){
            String path2 = partie.getListeEntites().get(i).getAttributs().get("path").get(0);
            listePerso[i] = new Button("",new ImageView(new Image (path2)));
            grilleImg.getChildren().add(listePerso[i]);
            if(partie.getListeEntites().get(i).getBarre()) listePerso[i].setOpacity(0.5);
        }
        System.out.println("tableau crée");
        //INSERTION DE TOUTE LES IMAGES DU TABLEAU DANS LE GRIDPANE 
        int cp = 0;
        for (int r=0; r< row;r++) {
            for (int c=0; c<col; c++){
                GridPane.setConstraints(listePerso[cp],c,r);
                cp++;
            }
        }
        System.out.println("images placées");
       

        //AJOUT DE TOUT LES NODES PRECEDANT DANS LE LAYOUT INTERACTION
        validerQ.setOnAction(e -> { 
        	reponseProgramme.setText( 
        			partie.randomQ());});
        oui.setOnAction(e ->{ if(reponseProgramme.getText() !="") {
        	partie.setElimines(partie.eliminesParTour("NON",true,partie.getT1(),partie.getT2()));
        for ( int i=0 ; i<partie.getListeEntites().size();i++) {
        	
      	   if(partie.getListeEntites().get(i).getBarre()) {
      		   listePerso[i].setOpacity(0.5);
         }
      	   else
      	   {
      		   listePerso[i].setOpacity(1); }
         }
         reponseProgramme.setText("");
         if(partie.getcountbarre() == partie.getlignes()*partie.getcolonnes()-1) {
        	 reponseProgramme.setText("Le personnage que vous avez choisi est le dernier non coché");
         }}});
        non.setOnAction(e ->{ if(reponseProgramme.getText() !="") {
        	partie.setElimines(partie.eliminesParTour("NON",false,partie.getT1(),partie.getT2()));
        for ( int i=0 ; i<partie.getListeEntites().size();i++) {
        	
      	   if(partie.getListeEntites().get(i).getBarre()) {
      		   listePerso[i].setOpacity(0.5);
         }
      	   else
      	   {
      		   listePerso[i].setOpacity(1); }
         }
        reponseProgramme.setText("");
        if(partie.getcountbarre() == partie.getlignes()*partie.getcolonnes()-1) {
       	 reponseProgramme.setText("Le personnage que vous avez choisi est le dernier non coché");
        }}});
        interaction.getChildren().addAll(cpCoups,reponseProgramme,non,oui,validerQ);
        //INSERTION DANS LE BORDERPANE DES LAYOUT DE MENU ET DE PERSONNAGE
        layoutGame.setTop(menuGame);
        layoutGame.setCenter(grilleImg);
        layoutGame.setBottom(interaction);

        gameScene = new Scene(layoutGame);
        window.setScene(gameScene);
        window.setTitle("Qui est-ce");
        System.out.println("c sensé fonctionner??");
    }
    public void chooseFileBoxGenerateur () {
        //DECLARATION STAGE
        Stage alertBoxWindow = new Stage();
        alertBoxWindow.initModality(Modality.APPLICATION_MODAL); //PERMET DE BLOQUER L'ACCES AUX AUTRES FENETRES
        alertBoxWindow.setTitle("Selectionner JSON");
        alertBoxWindow.setMinWidth(250);
        alertBoxWindow.getIcons().add(new Image("file:src/main/resources/img/logo.png"));
        
        //Creation fichier pour sauvegardes
        File recordsDir = new File(System.getProperty("user.home"), ".Qui-est-ce-GRPMA/records/FileGene");
        if (! recordsDir.exists()) {
            recordsDir.mkdirs();
        }
        FileChooser FileChooser = new FileChooser();

        FileChooser.setInitialDirectory(recordsDir);
        FileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Json", "*.json"));
        
        //DECLARATION LABEL
        Label messageLabel = new Label("Sélectionner un fichier JSON pour les images,le nombre de lignes,puis de colonnes");

        //DECLARATION BOUTTONS
        Button easyButton = new Button("Choisir");
        ComboBox<String> lignes = new ComboBox<>();
        lignes.getItems().addAll("1","2","3","4","5");
        ComboBox<String> colonnes = new ComboBox<>();
        colonnes.getItems().addAll("1","2","3","4","5","6");
        //GERE L'ACTION DES BOUTTONS
        easyButton.setOnAction(e -> {
        	File selectedFile = FileChooser.showOpenDialog(alertBoxWindow);;
        	System.out.println(selectedFile.getPath());
        	System.out.println(System.getProperty("user.home"));
			startGenerateur(Integer.parseInt(lignes.getSelectionModel().getSelectedItem()),Integer.parseInt(colonnes.getSelectionModel().getSelectedItem()),selectedFile.getPath());     	 
            alertBoxWindow.close();});

        //DECLARATION LAYOUT
        HBox alertBoxLayout = new HBox(10);
        alertBoxLayout.getChildren().addAll(messageLabel,lignes,colonnes,easyButton);
        alertBoxLayout.setAlignment(Pos.CENTER);

        //DECLARATION SCENE
        Scene alertBoxScene = new Scene(alertBoxLayout);
        alertBoxWindow.setScene(alertBoxScene);
        alertBoxWindow.show();

    }
    
    public void startGenerateur(int row, int col,String pathJsonEntite){
    	//CREATION PARTIE
    	Generateur generateur = new Generateur(row,col,pathJsonEntite);
        //CREATION DU BORDER LAYOUT DE JEU
        BorderPane layoutGame = new BorderPane();
        System.out.println("Layout1 crée");
        //CREATION DU LAYOUT POUR QUITTER/SAUVEGARDER
        HBox menuGame = new HBox();
        menuGame.setAlignment(Pos.CENTER);
        System.out.println("Layout2 crée");
        //CREATION DU GRIDPANE POUR AFFICHER LES IMAGES
        GridPane grilleImg = new GridPane();
        grilleImg.setPadding(new Insets(10,10,10,10));
        grilleImg.setVgap(8);
        grilleImg.setHgap(8);
        grilleImg.setAlignment(Pos.CENTER);
        System.out.println("Gridpane crée");
        //CREATION GRIDPANE LAYOUT INTERACTION AVEC LE JEU
        GridPane interaction = new GridPane();
        interaction.setPrefWidth(150);
        interaction.setPadding(new Insets(10,10,10,10));
        interaction.setVgap(8);
        interaction.setHgap(8);
        interaction.setAlignment(Pos.CENTER);
        System.out.println("GridpaneLayout crée");
        //DECLARATION TEXTE INTERACTION
        Text cpCoups = new Text("Selection Attributs");
        GridPane.setConstraints(cpCoups,0,1);
        System.out.println("TexteInteract crée");
        ComboBox<String> connecteur = new ComboBox<>();
        connecteur.getItems().addAll("ADD","SUPPR");
        connecteur.setMinWidth(interaction.getPrefWidth());
        System.out.println("Connecteur fait");
        TextArea key = new TextArea();
        key.setText("New Key");
        key.setMinWidth(interaction.getPrefWidth());
        key.setMaxHeight(30);
        ComboBox<String> keys = new ComboBox<>();
        keys.setOnAction(e -> {  for (int i=1 ; i<generateur.getListeEntites().get(0).getKeys().size()-1;i++) {
        keys.getItems().add(generateur.getListeEntites().get(0).getKeys().get(i));}});
        keys.setMinWidth(interaction.getPrefWidth());
        GridPane.setConstraints(connecteur,2,1);
        GridPane.setConstraints(key,1,1);
        GridPane.setConstraints(keys,3,1);
        //DECLATION BOUTTON INTERACTION
        Button valider = new Button("Valider");
        valider.setMinWidth(interaction.getPrefWidth());
        GridPane.setConstraints(valider,4,1);
        valider.setOnMouseClicked(e ->{ if(connecteur.getSelectionModel().getSelectedItem() != "" ) {
        	if(connecteur.getSelectionModel().getSelectedItem() == "ADD" && key.getText() != "") {
        		System.out.println("Ajouté");
        		generateur.setK(key.getText());
        		keys.getItems().add(key.getText());
        } else if (connecteur.getSelectionModel().getSelectedItem() == "SUPPR" && keys.getSelectionModel().getSelectedItem() != null) {
        	System.out.println("La vie est dingue quand même ( suppr");
        	generateur.supprK(keys.getSelectionModel().getSelectedItem());
        	int t = keys.getSelectionModel().getSelectedIndex();
        	keys.getItems().remove(keys.getSelectionModel().getSelectedItem());
        	keys.getItems().remove(t);
        }
        	key.clear();
        	;}});
        //AJOUT DE TOUT LES NODES PRECEDANT DANS LE LAYOUT INTERACTION
        interaction.getChildren().addAll(cpCoups,key, connecteur,keys, valider);
        System.out.println("Nodes Placées");
        //DECLARATION BOUTTON DU MENU DE JEU
        Button quitButton = new Button("Quitter");
        Button saveButton = new Button("Sauvegarder");
        menuGame.getChildren().addAll(quitButton,saveButton);
        quitButton.setOnAction(e -> window.setScene(menuScene));
        saveButton.setOnAction(e ->{  Stage alertBoxWindow = new Stage();
        alertBoxWindow.initModality(Modality.APPLICATION_MODAL); //PERMET DE BLOQUER L'ACCES AUX AUTRES FENETRES
        alertBoxWindow.setTitle("Reponse");
        alertBoxWindow.setMinWidth(250);
        alertBoxWindow.getIcons().add(new Image("file:src/main/resources/img/logo.png"));
                //DECLARATION LABEL ET LAYOUT POUR VRAI
                Label messageLabel = new Label("Choisir un nom de sauvegarde");
                TextArea text = new TextArea();
                text.setText("NomSave");
                text.setMinWidth(interaction.getPrefWidth());
                text.setMaxHeight(20);
                text.setMaxWidth(20);
                HBox alertBoxLayout = new HBox(10);
                Button easyButton = new Button("Finir");
                alertBoxLayout.getChildren().addAll(messageLabel,text,easyButton);
                alertBoxLayout.setAlignment(Pos.CENTER);
                easyButton.setOnAction(v -> {generateur.saveJsonEntite(text.getText());
                	alertBoxWindow.close();
                });
              //DECLARATION SCENE
                Scene alertBoxScene = new Scene(alertBoxLayout);
                alertBoxWindow.setScene(alertBoxScene);
                alertBoxWindow.show();});
        System.out.println("Boutons menus Placés");
        //CREATION TABLEAU CONTENANT TOUTE LES PERSONNAGES SOUS FORME 
        Button [] listePerso = new Button[generateur.getlignes()*generateur.getcolonnes()];
        for (int i = 0; i<generateur.getlignes()*generateur.getcolonnes();i++){
            String path2 = generateur.getListeEntites().get(i).getAttributs().get("path").get(0);
            listePerso[i] = new Button("",new ImageView(new Image (path2)));
            grilleImg.getChildren().add(listePerso[i]);
        }
        System.out.println("tableau crée");
        //INSERTION DE TOUTE LES IMAGES DU TABLEAU DANS LE GRIDPANE 
        int cp = 0;
        for (int r=0; r< generateur.getlignes();r++) {
            for (int c=0; c<generateur.getcolonnes(); c++){
                GridPane.setConstraints(listePerso[cp],c,r);
                cp++;
            }
        }
        System.out.println("images placées");
        //EVENT ON CLICK DES PERSONNAGES // ajouter la partie avec les méthodes de partie
        for (cp = 0; cp<generateur.getlignes()*generateur.getcolonnes(); cp++){
            int finalCp = cp;
            listePerso[cp].setOnAction(e -> { 
            	 Stage alertBoxWindow = new Stage();
                 alertBoxWindow.initModality(Modality.APPLICATION_MODAL); //PERMET DE BLOQUER L'ACCES AUX AUTRES FENETRES
                 alertBoxWindow.setTitle("Reponse");
                 alertBoxWindow.setMinWidth(400);
                 alertBoxWindow.setMinHeight(100);
                 alertBoxWindow.getIcons().add(new Image("file:src/main/resources/img/logo.png"));
                 //DECLARATION LABEL ET LAYOUT POUR VRAI
                 Label messageLabel = new Label("Ajoutez les attributs");
                 HBox alertBoxLayout = new HBox(10);
                 ComboBox<String> liste = new ComboBox<String>();
                 for (int i=1 ; i<generateur.getListeEntites().get(finalCp).getKeys().size();i++) {
                     liste.getItems().add(generateur.getListeEntites().get(finalCp).getKeys().get(i));}
                 TextArea text = new TextArea();
                 text.setText("Ajout Atribut");
                 text.setMinWidth(interaction.getPrefWidth());
                 text.setMaxHeight(20);
                 text.setMaxWidth(20);
                 Button add = new Button("Ajouter");
                 System.out.println(generateur.getListeEntites().get(finalCp).getAttributs());
                 add.setOnAction(u -> { if(liste.getSelectionModel().getSelectedItem() != null & text.getText() != "")generateur.getListeEntites().get(finalCp).addAttribut(liste.getSelectionModel().getSelectedItem(),text.getText());
                 text.clear();});
                 Button easyButton = new Button("fermer");
                 alertBoxLayout.getChildren().addAll(messageLabel,liste,text,add,easyButton);
                 alertBoxLayout.setAlignment(Pos.CENTER);
                 easyButton.setOnAction(v -> {alertBoxWindow.close();
                 });
               //DECLARATION SCENE
                 Scene alertBoxScene = new Scene(alertBoxLayout);
                 alertBoxWindow.setScene(alertBoxScene);
                 alertBoxWindow.show();
            });}
           

        System.out.println("coche fait");
        //INSERTION DANS LE BORDERPANE DES LAYOUT DE MENU ET DE PERSONNAGE
        layoutGame.setTop(menuGame);
        layoutGame.setCenter(grilleImg);
        layoutGame.setBottom(interaction);

        gameScene = new Scene(layoutGame);
        window.setScene(gameScene);
        window.setTitle("Qui est-ce");
        System.out.println("c sensé fonctionner??");
    }

    public void chooseDifficultyBox (){

            //DECLARATION STAGE
            Stage alertBoxWindow = new Stage();
            alertBoxWindow.initModality(Modality.APPLICATION_MODAL); //PERMET DE BLOQUER L'ACCES AUX AUTRES FENETRES
            alertBoxWindow.setTitle("Mode de jeu");
            alertBoxWindow.setMinWidth(250);
            alertBoxWindow.getIcons().add(new Image("file:src/main/resources/img/logo.png"));

            //DECLARATION LABEL
            Label messageLabel = new Label("Sélectionner une difficulté");

            //DECLARATION BOUTTONS
            Button easyButton = new Button("Facile");
            Button normalButton = new Button("Normal");
            //GERE L'ACTION DES BOUTTONS
            easyButton.setOnAction(e -> {
                chooseFileBox(true);
                alertBoxWindow.close();} );
            normalButton.setOnAction(e -> {
                chooseFileBox(false);
                alertBoxWindow.close();
            });

            //DECLARATION LAYOUT
            HBox alertBoxLayout = new HBox(10);
            alertBoxLayout.getChildren().addAll(messageLabel, easyButton, normalButton);
            alertBoxLayout.setAlignment(Pos.CENTER);

            //DECLARATION SCENE
            Scene alertBoxScene = new Scene(alertBoxLayout);
            alertBoxWindow.setScene(alertBoxScene);
            alertBoxWindow.show();
            //DECLARATION NOUVELLE PARTIE

    }
    }
