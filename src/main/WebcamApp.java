package main;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamException;
import java.awt.Dimension;
import java.io.IOException;
import java.util.Arrays;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class WebcamApp extends Application {

    private static Webcam webcam;
    private static final ImageView imageView = new ImageView();

    public static void main(String[] args) throws IOException, WebcamException {
        webcam = Webcam.getDefault();
        webcam.getDevice().setResolution(new Dimension(848, 480));
        System.out.println(Arrays.toString(webcam.getDevice().getResolutions()));
        //webcam.setViewSize(new Dimension(320, 240));
        System.out.println("Opening webcam.");
        webcam.open();
        System.out.println("Opened webcam.");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        imageView.setImage(SwingFXUtils.toFXImage(webcam.getImage(), null));
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(300);
        Scene scene = new Scene(new StackPane(imageView));
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();
        moveToRightBottom(primaryStage);
        primaryStage.setAlwaysOnTop(true);

        Alert infoAlert = new Alert(Alert.AlertType.INFORMATION, "Starting Webcam Display");
        infoAlert.setTitle("Webcam");
        infoAlert.setHeaderText(null);
        infoAlert.showAndWait();

        new Thread(() -> {
            while (primaryStage.isShowing()) {
                imageView.setImage(SwingFXUtils.toFXImage(webcam.getImage(), null));
                imageView.setFitWidth(primaryStage.getScene().getWidth());
            }
        }).start();
    }

    private void moveToRightBottom(Stage stage) {
        Rectangle2D scrBounds = Screen.getPrimary().getVisualBounds();
        stage.setX(scrBounds.getMinX() + scrBounds.getWidth() - stage.getWidth() - 10);
        stage.setY(scrBounds.getMinY() + scrBounds.getHeight() - stage.getHeight() - 10);
    }
}
