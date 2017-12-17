package aoetk.sample.beanvalidation;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

public class Controller implements Initializable {

    @FXML
    Label emailErrorLabel;

    @FXML
    Label passwordErrorLabel;

    @FXML
    Button registerButton;

    @FXML
    TextField emailField;

    @FXML
    PasswordField passwordField;

    private StringProperty emailMessage = new SimpleStringProperty("");

    private StringProperty passwordMessage = new SimpleStringProperty("");

    private Model model = new Model();

    private Validator validator;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
        validateModel();

        emailErrorLabel.textProperty().bind(emailMessage);
        passwordErrorLabel.textProperty().bind(passwordMessage);
        registerButton.disableProperty().bind(emailMessage.isNotEmpty().and(passwordMessage.isNotEmpty()));
        emailErrorLabel.managedProperty().bind(emailMessage.isNotEmpty());
        passwordErrorLabel.managedProperty().bind(passwordMessage.isNotEmpty());
        model.emailProperty().bind(emailField.textProperty());
        model.passwordProperty().bind(passwordField.textProperty());
        emailField.textProperty().addListener(observable -> validateModel());
        passwordField.textProperty().addListener(observable -> validateModel());
    }

    private void validateModel() {
        final Set<ConstraintViolation<Model>> violations = validator.validate(model);
        emailMessage.set("");
        passwordMessage.set("");
        for (ConstraintViolation<Model> violation : violations) {
            if (violation.getPropertyPath().toString().equals("email")) {
                emailMessage.set(violation.getMessage());
            } else if (violation.getPropertyPath().toString().equals("password")) {
                passwordMessage.set(violation.getMessage());
            }
        }
    }

}
