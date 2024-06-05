package iss.group.application;

import iss.group.application.Controllers.HelloController;
import iss.group.application.Repository.RepositoryBilet;
import iss.group.application.Repository.RepositoryLoc;
import iss.group.application.Repository.RepositorySpectacol;
import iss.group.application.Repository.RepositoryUser;
import iss.group.application.Service.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {

    private final String url="jdbc:postgresql://localhost:5432/teatru";
    private final String user="postgres";
    private final String pass="irchit";


    @Override
    public void start(Stage stage) throws IOException {
        RepositoryUser repositoryUser = new RepositoryUser(url, user, pass);
        RepositoryLoc repositoryLoc = new RepositoryLoc(url, user, pass);
        RepositorySpectacol repositorySpectacol = new RepositorySpectacol(url, user, pass);
        RepositoryBilet repositoryBilet = new RepositoryBilet(url, user, pass);
        Service service = new Service(repositoryUser,repositoryBilet,repositoryLoc,repositorySpectacol);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        HelloController controller = fxmlLoader.getController();
        controller.setService(service);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());
        stage.setTitle("Teatru Cluj | Authentication");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}