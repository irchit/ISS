package iss.group.application.Controllers;

import iss.group.application.Domain.Bilet;
import iss.group.application.Domain.Loc;
import iss.group.application.Domain.Spectacol;
import iss.group.application.Domain.User;
import iss.group.application.Service.Service;
import iss.group.application.Service.Utils.Observer;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.*;

public class ClientController implements Observer {

    private Service service;
    private User user;
    private List<Bilet> bilete;
    private List<Bilet> bileteAzi;

    @FXML
    AnchorPane main;
    @FXML
    VBox zonaLocuri;

    @FXML
    Label nume;
    @FXML
    Label MainTitle;
    @FXML
    Label titluSpectacol;
    @FXML
    Label pretSpectacol;
    @FXML
    Label descriereSpectacol;
    @FXML
    Label username;
    @FXML
    Label plata;
    @FXML
    Label numLoc;

    List<HBox> locuri = new ArrayList<>();

    List<Loc> orderLocuri = new ArrayList<>();
    Spectacol azi;

    public void setServiceUser(Service service, User user) {
        this.service = service;
        azi = null;
        LocalDate localDate = LocalDate.now();
        System.out.println(localDate.toString());
        MainTitle.setText("Teatru Cluj: " + localDate.toString());
        for (Spectacol show : service.getSpectacole()){
            var show_Date = show.getData();
            if(localDate.toString().equals(show_Date.toString()))
            {
                azi = new Spectacol(show.getId(), show.getTitlu(), show.getDescriere(), show.getData(), show.getPret());
                break;
            }
        }
        if(azi == null)
        {
            this.user = user;
            service.addObserver(this);
            makeZonaLocuri();
            setProfile();
            titluSpectacol.setText("Niciun Show Azi");
            pretSpectacol.setText("");
            descriereSpectacol.setText("");
            return;
        }
        updateBilete();
        this.user = user;
        service.addObserver(this);
        makeZonaLocuri();
        updateLocuri();
        setProfile();
        setShow();
        updatePret();
    }

    private void updateBilete() {
        bilete = service.getAllBilete();
        bileteAzi = new ArrayList<>();
        for(Bilet bilet : bilete){
            if (bilet.getSpectacol().getId() == azi.getId()){
                bileteAzi.add(bilet);
            }
        }
    }

    private void updatePret() {
        plata.setText("Total Plata: " + azi.getPret() * orderLocuri.size() + " RON");
        numLoc.setText("Numar Locuri: " + orderLocuri.size());
    }

    private void setShow() {
        titluSpectacol.setText(azi.getTitlu());
        pretSpectacol.setText(azi.getPret().toString() + " RON");
        descriereSpectacol.setText(azi.getDescriere());
    }

    private void setProfile() {
        username.setText(user.getUsername());
        nume.setText(user.getName());
    }

    private void makeZonaLocuri() {
        List<Loc> locuriArray = service.getAllLocuri();
        zonaLocuri.getChildren().clear();
        zonaLocuri.setAlignment(Pos.CENTER);
        zonaLocuri.setSpacing(10);
        locuriArray.sort((a, b) -> { return a.getNrOrdine() - b.getNrOrdine();});
        locuri.clear();
        System.out.println(locuriArray.size());
        System.out.println(locuriArray);
        int k = 0;
        for (int i = 0; i < (int) (locuriArray.size() / 8 ) + 1; i++) {
            HBox hbox = new HBox();
            hbox.setSpacing(10);
            hbox.setAlignment(Pos.CENTER);
            for (int j = 0; j < 8; j++) {
                Pane pane = new Pane();
                pane.setPrefSize(25, 25);
                pane.setId(locuriArray.get(k).getId()+"");
                pane.setStyle("-fx-background-color: rgba(128,128,128,1);");
                hbox.getChildren().add(pane);
                pane.setOnMouseClicked(mouseEvent -> {
                    System.out.println("Clicked: " + pane.getId());
                    handleLocClick(pane);
                });
                k ++;
                if (k == locuriArray.size()) {
                    break;
                }
            }
            zonaLocuri.getChildren().add(hbox);
            if (k == locuriArray.size()) {
                break;
            }
        }
        System.out.println("locuri: " + locuri.size());
    }

    private void handleLocClick(Pane pane){

        if (Objects.equals(pane.getStyle(), "-fx-background-color: rgba(128,128,128,1);")) {
            orderLocuri.add(service.findLoc(Integer.parseInt(pane.getId())));
            pane.setStyle("-fx-background-color: rgba(0,0,255,1);");
            updatePret();
        } else if(Objects.equals(pane.getStyle(), "-fx-background-color: rgba(255,0,0,1);")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Atentie");
            alert.setContentText("Loc deja cumparat");
            alert.show();
        } else if (Objects.equals(pane.getStyle(), "-fx-background-color: rgba(0,0,255,1);")){
            orderLocuri.removeIf(loc -> loc.getId() == Integer.parseInt(pane.getId()));
            pane.setStyle("-fx-background-color: rgba(128,128,128,1);");
            updatePret();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Atentie");
            alert.setContentText("Loc deja cumparat de tine");
            alert.show();
        }

    }

    private void updateLocuri(){
        updateBilete();
        for (Bilet bilet : bileteAzi) {
            if (bilet.getUser().getId() == user.getId()) {
                int loc = bilet.getLoc().getId();
                int rand = (int) ((loc - 1) / 8);
                int coloana = (loc - 1) % 8;
                HBox Rand = (HBox) zonaLocuri.getChildren().get(rand);
                Pane Loc = (Pane) Rand.getChildren().get(coloana);
                Loc.setStyle("-fx-background-color: rgba(0,255,0,1);");
            } else {
                int loc = bilet.getLoc().getId();
                int rand = (int) ((loc - 1) / 8);
                int coloana = (loc - 1) % 8;
                HBox Rand = (HBox) zonaLocuri.getChildren().get(rand);
                Pane Loc = (Pane) Rand.getChildren().get(coloana);
                Loc.setStyle("-fx-background-color: rgba(255,0,0,1);");
            }
        }
    }

    @FXML
    public void onLogOut(){
        service.removeObserver(this);
        Stage stage = (Stage) main.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void Buy() {
        if (orderLocuri.size() == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Atentie");
            alert.setContentText("Niciun loc selectat");
            alert.show();
            return;
        }
        String content = "Locuri: ";
        for (Loc loc : orderLocuri) {
            service.addBilet(user, azi, loc);
            content += loc.toString() + "; ";
        }
        content += "\nPlatit: " + orderLocuri.size() * azi.getPret();
        content += "\nUser: " + user.getUsername() + ", Nume: " + user.getName();
        content += "\nEmail: " + user.getEmail();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText("Bilet cumparat");
        alert.setContentText(content);
        alert.showAndWait();
        orderLocuri.clear();
        updatePret();
        updateLocuri();
    }

    @Override
    public void update() {
        updateLocuri();
    }
}
