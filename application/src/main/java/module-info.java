module iss.group.application {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires jakarta.validation;
    requires org.hibernate.orm.core;


    opens iss.group.application to javafx.fxml;
    exports iss.group.application;
    exports iss.group.application.Controllers;
    opens iss.group.application.Controllers to javafx.fxml;
}