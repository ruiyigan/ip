package alfred.main;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Controller for MainWindow. Provides the layout for the other controls.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Alfred alfred;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/DaBatman.png"));
    private Image alfredImage = new Image(this.getClass().getResourceAsStream("/images/DaAlfred.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    public void setAlfred(Alfred d) {
        alfred = d;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Alfred's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = alfred.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getAlfredDialog(response, alfredImage)
        );
        //Reused from @jehousoh with modifications
        if (input.equals("bye")) {
            PauseTransition delay = new PauseTransition(Duration.seconds(2.5));
            delay.setOnFinished(e -> Platform.exit());
            delay.play();
        }
        //@@author
        userInput.clear();
    }
}
