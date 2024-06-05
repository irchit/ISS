package iss.group.application.Controllers;

import iss.group.application.Domain.Type;
import iss.group.application.Domain.User;
import iss.group.application.HelloApplication;
import iss.group.application.Service.Service;
import iss.group.application.Service.UserWatcher;
import iss.group.application.Service.Utils.Observer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelloController implements Observer {
    private Service service;
    @FXML
    private TextField usernameLogIn, passwordLogIn;
    @FXML
    private TextField username, password, email, name;

    public void setService(Service service){
        this.service = service;
    }

    @FXML
    public void logIn(){
        String username = usernameLogIn.getText();
        String password = passwordLogIn.getText();

        User user = service.getUserByCredentials(username, password);
        if(user != null && !UserWatcher.loggedIn(user)){
            UserWatcher.loggedIn(user);
            if (user.getType() == Type.ADMIN){
                try {
                    loadAdminView(user);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    loadClientView(user);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Credentials");
            alert.setContentText("Username or Password Incorrect or User already logged in");
            alert.showAndWait();
        }
    }

    @FXML
    public void register() {
        String username = this.username.getText();
        String password = this.password.getText();
        String email = this.email.getText();
        String name = this.name.getText();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || name.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Credentials");
            alert.setContentText("Empty Fields");
            alert.showAndWait();
            return;
        }

        String EMAIL_REGEX =
                "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                        "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if (!matcher.matches())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Credentials");
            alert.setContentText("Email invalid!");
            alert.showAndWait();
            return;
        }

        try{
            service.addUser(username, password, name, email);
        } catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Credentials");
            alert.setContentText("Username already taken.");
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("User created");
        alert.setContentText("User created now logging in");
        alert.showAndWait();

        User user = service.getUserByCredentials(username, password);
        if(user != null && !UserWatcher.loggedIn(user)){
            UserWatcher.loggedIn(user);
            if (user.getType() == Type.ADMIN){
                try{
                    loadAdminView(user);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    loadClientView(user);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            Alert alert2 = new Alert(Alert.AlertType.ERROR);
            alert2.setTitle("Error");
            alert2.setHeaderText("Invalid Credentials");
            alert2.setContentText("Username or Password Incorrect or User already logged in");
            alert2.showAndWait();
        }
    }

    private void loadClientView(User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("client-view.fxml"));
        Stage stage = new Stage();
        AnchorPane pane = loader.load();
        Scene scene = new Scene(pane);
        ClientController controller = loader.getController();
        controller.setServiceUser(service, user);
        stage.setTitle("Cumpara Bilete | " + user.getUsername());
        stage.setScene(scene);
        stage.show();
    }

    private void loadAdminView(User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("admin-view.fxml"));
        Stage stage = new Stage();
        AnchorPane pane = loader.load();
        Scene scene = new Scene(pane);
        AdminController controller = loader.getController();
        controller.setServiceUser(service, user);
        stage.setScene(scene);
        stage.setTitle("Admin View | " + user.getName() + " | " + user.getUsername());
        stage.show();
    }

    @Override
    public void update() {

    }
}