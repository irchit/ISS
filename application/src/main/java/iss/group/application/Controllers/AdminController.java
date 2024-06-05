package iss.group.application.Controllers;

import iss.group.application.Domain.Spectacol;
import iss.group.application.Domain.User;
import iss.group.application.Service.Service;
import iss.group.application.Service.Utils.Observer;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class AdminController implements Observer {
    private Service service;
    private User user;

    private final ToggleGroup selectedShow = new ToggleGroup();

    @FXML
    private ScrollPane showScroll;
    @FXML
    private AnchorPane Spectacole;
    @FXML
    private TextField title;
    @FXML
    private DatePicker date;
    @FXML
    private TextField pret;
    @FXML
    private TextArea descriere;

    // Set service and user, and register this controller as an observer
    public void setServiceUser(Service service, User user){
        this.service = service;
        this.user = user;
        service.addObserver(this);
        updateSpectacole();
        selectedShow.setUserData(null);
    }

    // Update the list of Spectacole
    private void updateSpectacole(){
        Spectacole.getChildren().clear();
        double yPosition = 0;
        double spacing = 15;

        var spectacoleList = service.getSpectacole();
        for (Spectacol show : spectacoleList){
            HBox line = new HBox();
            line.setSpacing(15);

            Label titlu = new Label(show.getTitlu());
            Label data = new Label(show.getData().toString());
            Label pret = new Label(show.getPret() + " RON");
            Label descriere = new Label(show.getDescriere());

            RadioButton radioButton = new RadioButton();
            radioButton.setToggleGroup(selectedShow);
            radioButton.setUserData(show.getId());

            line.setLayoutY(yPosition);
            yPosition += line.getHeight() + spacing;

            // Add components to HBox
            line.getChildren().addAll(radioButton, titlu, data, pret, descriere);
            Spectacole.getChildren().add(line);
        }
    }

    // Handle the add button action
    @FXML
    public void handleAddButtonAction() {
        if (descriere.getText().isEmpty() || title.getText().isEmpty() || date.getValue() == null || pret.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Fill all boxes");
            alert.showAndWait();
            return;
        }

        float pretFloat;
        try {
            pretFloat = Float.parseFloat(pret.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Price must be a number");
            alert.showAndWait();
            return;
        }

        LocalDate localDate = date.getValue();
        Date showDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        try{
            service.addSpectacole(title.getText(), descriere.getText(), showDate, pretFloat);
        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void handleEditButtonAction() {
        if (descriere.getText().isEmpty() || title.getText().isEmpty() || date.getValue() == null || pret.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Fill all boxes");
            alert.showAndWait();
            return;
        }

        float pretFloat;
        try {
            pretFloat = Float.parseFloat(pret.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Price must be a number");
            alert.showAndWait();
            return;
        }

        LocalDate localDate = date.getValue();
        Date showDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        service.updateSpectacole((Integer) selectedShow.getSelectedToggle().getUserData(), title.getText(), descriere.getText(), showDate, pretFloat);
    }

    @FXML
    public void handleDeleteButton() {
        if (selectedShow.getSelectedToggle() != null)
        {
            service.removeSpectacole((Integer) selectedShow.getSelectedToggle().getUserData());
            selectedShow.setUserData(null);
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Need to select a show");
            alert.showAndWait();
        }
    }

    @FXML
    public void onLogOut(){
        service.removeObserver(this);
        Stage stage = (Stage) Spectacole.getScene().getWindow();
        stage.close();
    }

    @Override
    public void update() {
        updateSpectacole();
    }
}
