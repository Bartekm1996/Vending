package machine;

import javafx.animation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.geometry.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Graphical extends Application {


    private Label label;
    private Timeline timeline;
    private String[] iconValues = {"5c","10c","20c","50c","$1","$2"};
    private Stage stage;
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage = new Stage();
        primaryStage.initStyle(StageStyle.UNDECORATED);
        stage = primaryStage;
        Platform.setImplicitExit(false);
        Scene scene = new Scene(logInPane(),509,360);
        sceneOnMouseDraged(scene,primaryStage);
        seceneOnMousePressed(scene,primaryStage);
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();
    }

    private void setScene(Parent root){
        stage.getScene().setRoot(root);
    }

    private Stage getStage(){
        return stage;
    }

    private void seceneOnMousePressed(Scene scene,Stage stage){
        scene.setOnMousePressed(event -> {
            xOffset = stage.getX() - event.getScreenX();
            yOffset = stage.getY() - event.getScreenY();
        });
    }

    private void sceneOnMouseDraged(Scene scene,Stage stage){
        scene.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() + xOffset);
            stage.setY(event.getScreenY() + yOffset);
        });
    }

    private void optionsStage(String actionType,LineItem lineItem){

        Stage stage = new Stage();
              stage.initStyle(StageStyle.UNDECORATED);
              stage.initModality(Modality.APPLICATION_MODAL);
              stage.setResizable(false);

        GridPane gridPane = new GridPane();
                 gridPane.setAlignment(Pos.CENTER);
                 gridPane.setVgap(10);
                 gridPane.setHgap(10);
                 gridPane.getStylesheets().add("/style.css");
                 gridPane.setPadding(new Insets(10));

        Button addButton = new Button();
               addButton.setText(actionType);
        Button cancelButton = new Button("Cancel");
               cancelButton.setOnAction(event -> stage.close());
        ComboBox<Integer> comboBox = new ComboBox<>();

        if(actionType.equals("Add Products")){
            int number = VendingMachine.MAX_AMOUNT - lineItem.getAmount();
            for(int i = 1; i <= number; i++){
                comboBox.getItems().add(i);
            }
            addButton.setOnAction(event -> {
                int added = comboBox.getSelectionModel().getSelectedItem();
                lineItem.setAmount(lineItem.getAmount() + added);
                infoStage(added + " products have been added",stage);
            });
            comboBox.setPromptText("Select No Of Products to Add");
        }else if(actionType.equals("Remove Products")){
            for(int i = 1; i <= lineItem.getAmount(); i++){
                comboBox.getItems().add(i);
            }
            addButton.setOnAction(event -> {
                int removed = comboBox.getSelectionModel().getSelectedItem();
                lineItem.setAmount(lineItem.getAmount() - removed);
                infoStage(removed + " products have been Removed",stage);
            });
            comboBox.setPromptText("Select No Of Products to Remove");
        }

        GridPane.setHalignment(cancelButton,HPos.CENTER);
        GridPane.setHalignment(addButton, HPos.CENTER);

        gridPane.add(comboBox,0,0,2,1);
        gridPane.add(addButton,1,1);
        gridPane.add(cancelButton,0,1);
        stage.setScene(new Scene(gridPane,250,75));
        stage.sizeToScene();
        stage.show();
    }

    private BorderPane logInPane(){

        BorderPane borderPane = new BorderPane();
                   borderPane.getStylesheets().add("/style.css");

        VBox vBox = new VBox(10);
        HBox hBox = new HBox();
             hBox.setMaxWidth(248.00);

        Label label = new Label("Enter User Name");
              label.setStyle("-fx-text-fill: deepskyblue; -fx-font-family: DejaVu Serif Bold; -fx-font-size: 20px;");

              Label errorLabel = new Label();
                    errorLabel.setStyle("-fx-text-fill: red");

                    TextField userNameField = new TextField();
                              userNameField.setPromptText("Enter User Name");
                              userNameField.setAlignment(Pos.CENTER);
                              userNameField.addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED, Event::consume);

        CheckBox checkBox = new CheckBox("Admin");
                 checkBox.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

                 hBox.getChildren().addAll(userNameField,checkBox);

        Button button = new Button("Buy Products");
               button.setPrefWidth(248.00);
               button.setOnAction(actionEvent -> setOnAction(checkBox,userNameField,borderPane,errorLabel));

               borderPane.setOnKeyPressed(event -> {
                   if(event.getCode() == KeyCode.ENTER) setOnAction(checkBox,userNameField,borderPane,errorLabel);
               });

        vBox.getChildren().addAll(label,hBox,button,errorLabel);
        vBox.setAlignment(Pos.CENTER);
        borderPane.setTop(toolBarMenu());
        borderPane.setCenter(vBox);
        return borderPane;

    }

    private BorderPane passWordPane(TextField textField){

        BorderPane borderPane = new BorderPane();
                   borderPane.getStylesheets().add("/style.css");
        FlowPane flowPane = new FlowPane();
                 flowPane.setHgap(10);

        Hyperlink backLink = new Hyperlink("Back");
                  backLink.setOnAction(actionEvent -> slideScene(borderPane,logInPane(),-1));
                  backLink.setPadding(new Insets(10,0,0,0));
        Hyperlink forgotPassWordLink = new Hyperlink("Forgot Password");
                  forgotPassWordLink.setOnAction(event -> borderPane.getScene().setRoot(forgotPassWordPane(textField.getText())));
                  forgotPassWordLink.setPadding(new Insets(10,0,0,0));

                  flowPane.getChildren().addAll(toolBarMenu(),backLink,forgotPassWordLink);

        Label label = new Label("Enter Password");
              label.setId("enterPasswordLabel");

        PasswordField passwordField = new PasswordField();
                      passwordField.setMaxWidth(248.00);
                      passwordField.setPromptText("Enter Password");
                      passwordField.setAlignment(Pos.CENTER);
                      passwordField.addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED,Event::consume);

                      Label errorLabel = new Label();
                            errorLabel.setStyle("-fx-text-fill: red");

                            Button button = new Button("Log In");
                                   button.setPrefWidth(248.00);
                                   button.setOnAction(event -> checkPassWord(borderPane,textField.getText(),passwordField.getText(),errorLabel));

                                   borderPane.setOnKeyPressed(event -> {
                                                 if(event.getCode() == KeyCode.ENTER)checkPassWord(borderPane,textField.getText(),passwordField.getText(),errorLabel);
                                   });

                    VBox vBox = new VBox(10);
                    vBox.getChildren().addAll(label,passwordField,button,errorLabel);
                    vBox.setAlignment(Pos.CENTER);
                    borderPane.setCenter(vBox);
                    borderPane.setTop(flowPane);

        return borderPane;
    }

    private void checkPassWord(BorderPane borderPane,String userName,String passWord,Label errorLabel) {

        if(passWord.isEmpty()) errorLabel.setText("Enter Password");
        else {
            if (VendingMachineMenu.getHashtable().get(userName).equals(passWord)){
                borderPane.getScene().setRoot(vendingPane("admin"));
            }
            else errorLabel.setText("Incorrect Password");
        }
    }

    private GridPane moneyPane(String userType){

        GridPane gridPane = new GridPane();
                 gridPane.setAlignment(Pos.CENTER);
                 gridPane.setVgap(5);
                 gridPane.setPadding(new Insets(0,10,0,10));

        StackPane stackPane = new StackPane();
                  stackPane.setMaxWidth(200.00);

        Label backLabel = new Label("000000000");
              backLabel.setId("backLabel");

        Button[] buttons = new Button[iconValues.length];
                 this.label = new Label();
                 this.label.setId("frontLabel");
                 stackPane.getChildren().addAll(backLabel,this.label);
        gridPane.add(stackPane,0,0,2,1);

        for(int i = 0; i < buttons.length; i++){
            buttons[i] = new Button(iconValues[i]);
            buttons[i].setPrefSize(60.00,60.00);
            if(i < 3)gridPane.add(buttons[i],0,i+2);
            else {
                gridPane.add(buttons[i],1,(i+2)-3);
                GridPane.setHalignment(buttons[i], HPos.RIGHT);
            }

            if(userType.equals("user"))buttonOnAction(buttons[i]);
        }

        Button button = new Button("Cancel");
        button.setPrefWidth(140);

        if(userType.equals("user")) {
            button.setOnAction(event -> {
                if (VendingMachineMenu.vendingMachine.getPurchasedProducts() == 0) {
                    VendingMachineMenu.vendingMachine.giveChange();
                    this.label.setText(Double.toString(VendingMachineMenu.vendingMachine.getCoinSet().getTotal()));
                }
            });
        }

               gridPane.add(button,0,5,2,1);


        return gridPane;
    }

    private void buttonOnAction(@NotNull Button button){
        button.setOnAction(event -> {
            VendingMachineMenu.vendingMachine.getCoinSet().addCoinsToTable(VendingMachineMenu.vendingMachine.getCoinSet().coinIconToValue(button.getText()),1);
            this.label.setText(Double.toString((VendingMachineMenu.vendingMachine.getCoinSet().getTotal())));
        });
    }

    private GridPane productsPane(String userType){

        GridPane gridPane = new GridPane();
                 gridPane.setStyle("-fx-border-color: black; -fx-border-width: 2px");

                 Button[] buttons = new Button[VendingMachineMenu.vendingMachine.getProductsTable().size()];
                 Label[] buyButtons = new Label[buttons.length];
                 VBox[] vBoxes = new VBox[buttons.length];
                 LineItem[] lineItems = VendingMachineMenu.vendingMachine.lineItems();


        for(int i = 0; i < buttons.length; i++){

            vBoxes[i] = new VBox();
            buyButtons[i] = new Label(Double.toString(lineItems[i].getProduct().getPrice()));
            buyButtons[i].setPrefWidth(100.00);
            buyButtons[i].setAlignment(Pos.CENTER);
            buttons[i] = new Button(lineItems[i].getName(),lineItems[i].getProduct().getImageView());
            buttons[i].setPrefSize(100.00,100.00);
            buttons[i].setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            vBoxes[i].getChildren().addAll(buttons[i],buyButtons[i]);
            if(i < 4){
                gridPane.add(vBoxes[i],0,i);
            }
            else if(i >= 4 && i < 8)gridPane.add(vBoxes[i],1,i-4);
            else if(i >= 8 && i < 12)gridPane.add(vBoxes[i],2,i-8);
            else if(i >= 12)gridPane.add(vBoxes[i],3,i-12);
            setOnAction(buttons[i],userType,lineItems[i]);

        }

        return gridPane;
    }

    private void setOnAction(Button button,String userType,LineItem lineItems){

        button.setOnMouseClicked(mouseEvent -> {
            if(userType.matches("admin")) {
                if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                    productMenu(lineItems).show(button,mouseEvent.getScreenX(), mouseEvent.getScreenY());
                }else if(mouseEvent.getButton() == MouseButton.PRIMARY){
                        this.label.setText(Integer.toString(lineItems.getAmount()));
                }
            }else if(userType.matches("user")){
                if(mouseEvent.getButton() == MouseButton.PRIMARY) {

                    if(this.timeline != null) {
                        if (this.timeline.getStatus() == Animation.Status.RUNNING)this.timeline.stop();
                    }

                    if (lineItems.getAmount() == 0) {
                        this.label.setText("Sold Out");
                        //flashText(this.label);
                        textFlow();
                    }else if(lineItems.getAmount() > 0 && VendingMachineMenu.vendingMachine.getCoinSet().getTotal() == 0){
                        this.label.setText(Double.toString(lineItems.getProduct().getPrice()));
                    }else if(lineItems.getAmount() > 0 && VendingMachineMenu.vendingMachine.getCoinSet().getTotal() < lineItems.getProduct().getPrice()){
                        this.label.setText("Ins funds");
                        flashText(this.label);
                    }else if(lineItems.getAmount() > 0&& VendingMachineMenu.vendingMachine.getCoinSet().getTotal() >= lineItems.getProduct().getPrice()){
                        VendingMachineMenu.vendingMachine.purchaseProduct(lineItems.getName());
                        this.label.setText("Change " + VendingMachineMenu.vendingMachine.getCoinSet().getTotal());
                        flashText(label);
                    }
                }
            }
        });

    }

    private ContextMenu productMenu(LineItem item){

        ContextMenu contextMenu = new ContextMenu();
                    contextMenu.setStyle("-fx-background-color: white;");
        MenuItem menuAddItem = new MenuItem("Add Products");
        MenuItem menuRemoveItem = new MenuItem("Remove Products");
        MenuItem menuProductInfo = new MenuItem("Product Info");

        menuAddItem.setOnAction(actionEvent -> optionsStage("Add Products",item));
        menuRemoveItem.setOnAction(event -> optionsStage("Remove Products",item));
        menuProductInfo.setOnAction(event -> productInfoStage(item));
        contextMenu.getItems().addAll(menuAddItem,menuRemoveItem,menuProductInfo);

        return contextMenu;
    }

    private void productInfoStage(LineItem item){

        Stage stage = new Stage();
              stage.setResizable(false);
              stage.initModality(Modality.APPLICATION_MODAL);
              stage.initStyle(StageStyle.UNDECORATED);

              GridPane gridPane = new GridPane();
                       gridPane.setVgap(10);
                       gridPane.setHgap(10);
                       gridPane.setPadding(new Insets(10));
                       gridPane.getStylesheets().add("/style.css");
                       Button closeButton = exitButton();
                              closeButton.setOnAction(event -> stage.close());
                              closeButton.setPadding(new Insets(5,5,0,0));
                              gridPane.add(closeButton,1,0);
                              GridPane.setHalignment(closeButton,HPos.RIGHT);

              String[] labelsString = {"Name","Quantity","Price"};
              Label[] labels = new Label[labelsString.length*2];
              for(int i = 0; i < labels.length; i++){

                  if(i < labelsString.length){
                      labels[i] = new Label(labelsString[i]);
                      gridPane.add(labels[i],0,i+1);
                  }else{
                      labels[i] = new Label();
                      gridPane.add(labels[i],1,(i+1)-3);
                  }
              }

              labels[3].setText(item.getName());
              labels[4].setText(Integer.toString(item.getAmount()) + " / " + VendingMachine.MAX_AMOUNT);
              labels[5].setText(Double.toString(item.getProduct().getPrice()));

              stage.setScene(new Scene(gridPane));
              stage.show();
    }

    private AnchorPane forgotPassWordPane(String username){

        AnchorPane anchorPane = new AnchorPane();
                   anchorPane.getStylesheets().add("/style.css");
                   double[] anchors = {140.00,211.00};

                   String[] textLabels = {"Password ","Confirm Password"};

        Label[] labels = new Label[textLabels.length];
        for(int i = 0; i < labels.length; i++){
            labels[i] = new Label(textLabels[i] + ":");
            labels[i].setStyle("-fx-font-weight: bold");
            AnchorPane.setTopAnchor(labels[i],anchors[i]);
            AnchorPane.setLeftAnchor(labels[i],93.00);
        }


        PasswordField[] passwordFields = new PasswordField[2];
        for(int i = 0; i < passwordFields.length; i++){
            passwordFields[i] = new PasswordField();
            passwordFields[i].setPromptText("Enter " + textLabels[i]);
            passwordFields[i].addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED, Event::consume);
            passwordFields[i].setAlignment(Pos.CENTER);
            AnchorPane.setLeftAnchor(passwordFields[i],279.00);
            AnchorPane.setTopAnchor(passwordFields[i],anchors[i]-8);
        }

        Button button = new Button("Change Password");
               button.setOnAction(event -> {
                   if(Password.match(passwordFields[0].getText(),passwordFields[1].getText())){
                       VendingMachineMenu.getHashtable().put(username,passwordFields[0].getText());
                       infoStage("Password changed",null);
                       anchorPane.getScene().setRoot(logInPane());
                   }else infoStage("Password don't match",null);
               });

        AnchorPane.setTopAnchor(button,275.00);
        AnchorPane.setLeftAnchor(button,338.00);
        anchorPane.getChildren().addAll(labels[0],labels[1],passwordFields[0],passwordFields[1],button);

        return anchorPane;
    }

    private MenuBar menuBar(String userType){

        final MenuBar menuBar = new MenuBar();
                      menuBar.setStyle("-fx-background-color: lightgray");
        final Menu menu = new Menu("Options");

        String[] menuLabels = {"Add New Product","Remove Product","Remove Coins","Log Out","Back","Exit"};
        MenuItem[] menuItmes = new MenuItem[menuLabels.length];
        for(int i = 0; i < menuItmes.length; i++){
            menuItmes[i] = new MenuItem(menuLabels[i]);

            if(i == 4)menu.getItems().add(new SeparatorMenuItem());

            if(userType.matches("admin") && i != 4)menu.getItems().add(menuItmes[i]);
            else if(userType.matches("user")){
                if(i > 3)menu.getItems().add(menuItmes[i]);
            }
        }

        menuItmes[0].setOnAction(event -> addNewProduct());
        menuItmes[1].setOnAction(event -> removeProduct());
        menuItmes[2].setOnAction(event -> RemoveCoins());
        menuItmes[3].setOnAction(event -> exitStage("logOut"));
        menuItmes[4].setOnAction(event -> getStage().getScene().setRoot(logInPane()));
        menuItmes[5].setAccelerator(new KeyCodeCombination(KeyCode.ESCAPE,KeyCodeCombination.CONTROL_DOWN));
        menuItmes[5].setOnAction(event -> exitStage("exit"));
        menuBar.getMenus().add(menu);
        return menuBar;
    }

    private void exitStage(@NotNull String actionType){

        Stage exitStage = new Stage();
              exitStage.initModality(Modality.APPLICATION_MODAL);
              exitStage.initStyle(StageStyle.UNDECORATED);
              exitStage.setAlwaysOnTop(true);


              GridPane gridPane = new GridPane();
                       gridPane.setAlignment(Pos.CENTER);
                       gridPane.getStylesheets().add("/style.css");
                       gridPane.setVgap(10);


        Label label = new Label();
        Button noButton = new Button("No");
               noButton.setPrefWidth(100.00);
               GridPane.setHalignment(noButton, HPos.CENTER);

               Button yesButton = new Button("Yes");
                      yesButton.setPrefWidth(100.00);
                      GridPane.setHalignment(yesButton, HPos.CENTER);


        if(actionType.matches("logOut")) {
            label.setText("Are you sure you want to log Out ?");
            yesButton.setOnAction(actionEvent -> {
                exitStage.close();
                setScene(logInPane());
            });
        } else if(actionType.matches("exit")){
            label.setText("Are you sure you want to exit ?");
            yesButton.setOnAction(actionEvent -> {
                    exitStage.close();
                    VendingMachineMenu.closeAll();
                    Platform.exit();
                    System.exit(1);
            });
        }

        noButton.setOnAction(event -> exitStage.close());

        gridPane.add(label,0,0,2,1);
        gridPane.add(noButton,0,1);
        gridPane.add(yesButton,1,1);

        exitStage.setScene(new Scene(gridPane,250,75));
        exitStage.sizeToScene();
        exitStage.show();
    }

    private BorderPane vendingPane(String userType){

        BorderPane borderPane = new BorderPane();
                   borderPane.getStylesheets().addAll("/vendingPane.css","/style.css");

        HBox vBox = new HBox(10);
             vBox.setStyle("-fx-background-color: lightgray");
             vBox.getChildren().addAll(toolBarMenu(),menuBar(userType));
        borderPane.setTop(vBox);
        borderPane.setLeft(moneyPane(userType));
        borderPane.setCenter(productsPane(userType));

        return borderPane;
    }

    private void addNewProduct(){

        Stage stage = new Stage();
              stage.initStyle(StageStyle.UNDECORATED);
              stage.initModality(Modality.APPLICATION_MODAL);

        GridPane gridPane = new GridPane();
                 gridPane.getStylesheets().add("/style.css");
                 gridPane.setAlignment(Pos.CENTER);
                 gridPane.setVgap(10);
                 gridPane.setPadding(new Insets(10));
                 Button closeButton  = exitButton();
                        closeButton.setOnAction(event -> stage.close());
                 gridPane.add(closeButton,1,0);
                 GridPane.setHalignment(closeButton,HPos.RIGHT);

        String[] strLabels = {"Product Name ","Product Price ","Quantity ","Img Src "};

        TextField[] textFields = new TextField[strLabels.length];
        Label[] labels = new Label[strLabels.length];

        for(int i = 0; i < labels.length; i++){

                labels[i] = new Label(strLabels[i] + " :");
                labels[i].setStyle("-fx-font-weight: bold");
                textFields[i] = new TextField();
                textFields[i].addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED,Event::consume);
                textFields[i].setPromptText("Enter " + strLabels[i]);
                gridPane.add(textFields[i],1,i+1);
                gridPane.add(labels[i],0,i+1);
        }

        FileChooser fileChooser = new FileChooser();
        Button button = new Button("Photo Src");

                button.setOnAction(event -> {
                   File file = fileChooser.showOpenDialog(stage);
                   if(file != null)textFields[3].setText(file.getPath());
                });

            HBox hBox = new HBox();
                 hBox.getChildren().addAll(textFields[3],button);
                 gridPane.add(hBox,1,4);
                 textFields[3].setDisable(true);

            Button addButton = new Button("Add Product");
                   gridPane.add(addButton,1,5);
                   GridPane.setHalignment(addButton,HPos.RIGHT);
                   addButton.setOnAction(event -> {
                       VendingMachineMenu.vendingMachine.addProduct(textFields[0].getText(),textFields[0].getText(),Integer.parseInt(textFields[1].getText()),Double.parseDouble(textFields[2].getText()),textFields[3].getText());
                       getStage().getScene().setRoot(vendingPane("admin"));
                   });

            stage.setScene(new Scene(gridPane));
            stage.show();
    }

    private void removeProduct(){

        Stage stage = new Stage();
              stage.initStyle(StageStyle.UNDECORATED);
              stage.initModality(Modality.APPLICATION_MODAL);
              stage.setResizable(false);

        GridPane gridPane = new GridPane();
                 gridPane.getStylesheets().add("/style.css");
                 gridPane.setPadding(new Insets(10));
                 gridPane.setVgap(10);
                 gridPane.setHgap(10);

        ComboBox<String> selectProdcut = new ComboBox<>();
                         selectProdcut.getItems().addAll(VendingMachineMenu.vendingMachine.getProductsTable().keySet());
                         selectProdcut.setPromptText("Select Product to remove");

        Button cancelButton = new Button("Cancel");
               cancelButton.setOnAction(event -> stage.close());

        Button deleteButton = new Button("Delete");
               deleteButton.setOnAction(event -> {
                   String name = selectProdcut.getSelectionModel().getSelectedItem();
                   VendingMachineMenu.vendingMachine.removeProduct(name);
                   infoStage(name + " Has been deleted successfully",stage);
                   getStage().getScene().setRoot(vendingPane("admin"));
               });


        gridPane.add(selectProdcut,0,0,2,1);
        gridPane.add(cancelButton,0,1);
        gridPane.add(deleteButton,1,1);
        gridPane.setAlignment(Pos.CENTER);
        stage.setScene(new Scene(gridPane));
        stage.show();
    }

    private void RemoveCoins() {

        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));
        gridPane.getStylesheets().add("/style.css");

        Label label = new Label("Enter amount of coins to  ");
        Label label1 = new Label("Total : ");
        Label label2 = new Label();
        Label label3 = new Label();

        CheckBox checkBox = new CheckBox("Remove All Coins");
        checkBox.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);

        int[] amounts = VendingMachineMenu.vendingMachine.getCoinSet().amounts();
        Label[] labels = new Label[iconValues.length];
        TextField[] textFields = new TextField[labels.length];
        Label[] labelsBox = new Label[labels.length];

        for (int i = 0; i < labels.length; i++) {
            labels[i] = new Label(iconValues[i]);
            labels[i].setStyle("-fx-font-weight: bold");
            textFields[i] = new TextField();
            textFields[i].setPrefSize(20.00, 20.00);
            textFields[i].setPromptText("Enter how many coins to remove");
            textFields[i].textProperty().addListener((observable,oldValue,newValue) -> {
                if(!newValue.isEmpty()) {
                    if (Integer.parseInt(newValue) > amounts[0]) {
                        textFields[0].setStyle("-fx-border-color: red");
                        textFields[0].setText("Cannot remove more coins than there is");
                    }else {
                        textFields[0].setStyle("-fx-border-color: green");
                    }
                }
            });
            labelsBox[i] = new Label(" / " + Integer.toString(amounts[i]));
            GridPane.setHalignment(labels[i], HPos.CENTER);
            gridPane.add(labels[i], 0, i + 2);
            gridPane.add(textFields[i], 1, i + 2);
            gridPane.add(labelsBox[i], 2, i + 2);
        }

        checkBox.setOnAction(event -> {
            for (int i = 0; i < textFields.length; i++) {
                if (checkBox.isSelected()) textFields[i].setDisable(true);
                else textFields[i].setDisable(false);
            }
            if (checkBox.isSelected()) {
                label2.setText(Integer.toString(VendingMachineMenu.vendingMachine.getCoinSet().getCoinsAmount()));
                label3.setText(Double.toString(VendingMachineMenu.vendingMachine.getCoinSet().getTotalValue()));
            }
        });


        Button button = new Button("Remove");

        button.setOnAction(event -> {
            double total = 0;
            if(!checkBox.isSelected()){
                VendingMachineMenu.vendingMachine.getCoinSet().removeAllCoins();
                infoStage(label3.getText() + " has been removed",stage);
            }else {
                for(int i = 0; i < textFields.length; i++) {
                    if(!textFields[i].getText().isEmpty() && Integer.parseInt(textFields[i].getText()) > 0) {
                        VendingMachineMenu.vendingMachine.getCoinSet().removeCoinsFromTable(Double.toString(VendingMachineMenu.vendingMachine.getCoinSet().coinIconToValue(iconValues[i])), Integer.parseInt(textFields[i].getText()));
                        total = total +  (VendingMachineMenu.vendingMachine.getCoinSet().coinIconToValue(iconValues[i])*Integer.parseInt(textFields[i].getText()));
                    }
                }
                label3.setText(Double.toString(total));
                infoStage(Double.toString(total) + " has been remove",stage);
            }
        });

        Button closeButton = exitButton();
               closeButton.setOnAction(event -> stage.close());

               gridPane.add(closeButton,2,0);
               GridPane.setHalignment(closeButton,HPos.RIGHT);
               gridPane.add(label,0,1);
               gridPane.add(checkBox,1,1);
               gridPane.add(button,1,9,2,1);
               gridPane.add(label1,0,8);
               gridPane.add(label2,2,8);
               gridPane.add(label3,1,8);
               GridPane.setHalignment(button,HPos.RIGHT);

        stage.setScene(new Scene(gridPane));
        stage.setResizable(false);
        stage.show();
    }

    private void flashText(@NotNull Label label){
        timeline = new Timeline(new KeyFrame(Duration.seconds(0.2), evt -> label.setVisible(false)),new KeyFrame(Duration.seconds(0.4), evt -> label.setVisible(true)));
        timeline.setCycleCount(5);
        timeline.play();
    }

    private void textFlow(){

        this.label.setAlignment(Pos.CENTER_RIGHT);
        char[] textArray = this.label.getText().toCharArray();
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            AtomicInteger atomicInteger = new AtomicInteger(-1);
            StringBuilder stringBuilder = new StringBuilder();
            @Override
            public void run() {
                atomicInteger.getAndIncrement();
                if(atomicInteger.get() < textArray.length){
                stringBuilder.append(textArray[atomicInteger.get()]);
                Platform.runLater(() -> {
                     System.out.println(stringBuilder.toString());
                     label.setText(stringBuilder.toString());

                });
            }}
        };

        timer.scheduleAtFixedRate(timerTask,10,100);
        timerTask.run();


    }

    private void setOnAction(CheckBox checkBox,TextField userNameField,BorderPane borderPane,Label errorLabel){
        if (checkBox.isSelected() && VendingMachineMenu.getHashtable().containsKey(userNameField.getText()))slideScene(borderPane,passWordPane(userNameField),1);
        else if (checkBox.isSelected() && !VendingMachineMenu.getHashtable().containsKey(userNameField.getText()))errorLabel.setText("Admin User Doesn't Exist");
        else if (!checkBox.isSelected() && !VendingMachineMenu.getHashtable().containsKey(userNameField.getText()))borderPane.getScene().setRoot(vendingPane("user"));
    }

    private void slideScene(BorderPane borderPane,BorderPane borderPane1,int direction){
        borderPane1.translateXProperty().set(borderPane.getWidth()*direction);
        Timeline timeline = new Timeline();

        KeyValue KeyValue = new KeyValue(borderPane1.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame KeyFrame = new KeyFrame(Duration.millis(200), KeyValue);

        Scene scene =  borderPane.getScene();
        scene.setRoot(borderPane1);
        timeline.getKeyFrames().add(KeyFrame);
        timeline.play();
    }

    private void infoStage(String text,Stage parentStage){

        Stage stage = new Stage();
              stage.initOwner(parentStage);
              stage.initModality(Modality.APPLICATION_MODAL);
              stage.initStyle(StageStyle.UNDECORATED);
              stage.setResizable(false);


        BorderPane borderPane = new BorderPane();
                   borderPane.getStylesheets().addAll("/style.css");
                   Label label1 = new Label();
                   borderPane.setCenter(label1);

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            int num = 4;
            @Override
            public void run() {
                num = num - 1;
                Platform.runLater(() -> {
                    label1.setText(text + " .." + Integer.toString(num));
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask,1000,3000);
        timerTask.run();


        stage.setScene(new Scene(borderPane,250,50));
        stage.sizeToScene();

        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.execute(() -> Platform.runLater(stage::show));
        scheduledExecutorService.schedule(() -> Platform.runLater(() -> { stage.close();if(parentStage != null)parentStage.close();}),3, TimeUnit.SECONDS);
    }

    private HBox toolBarMenu(){
        HBox hBox = new HBox(10);
             hBox.setPadding(new Insets(5,0,0,10));
             hBox.getChildren().addAll(exitButton(),miniButton());
             hBox.getStylesheets().add("/buttonBar.css");
        return hBox;
    }

    private Button exitButton(){
        Button exitButton = new Button();
               exitButton.setId("closeButton");
               exitButton.getStylesheets().add("/buttonBar.css");
               exitButton.setOnAction(event -> exitStage("exit"));
               return exitButton;
    }

    private Button miniButton(){
        Button miniButton = new Button();
               miniButton.setId("miniButton");
               miniButton.getStylesheets().add("/buttonBar.css");
               miniButton.setOnAction(event -> getStage().setIconified(true));
               return miniButton;
    }
}
