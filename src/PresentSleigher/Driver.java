package PresentSleigher;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/*
*
* This is the driver class, it runs the game!
* Author: Kathleen Tynan
*
 */
public class Driver extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {

        Group root = new Group();
        BrickBreaker game = new BrickBreaker();
        root.getChildren().add(game);
        primaryStage.setTitle("Present Sleigher!");
        primaryStage.setResizable(false);
        Scene scene = new Scene(root, 600, 600);
        scene.setFill(Color.BLACK);
        primaryStage.setScene(scene);
        primaryStage.show();
    } // start

    public static void main(String[] args) {
        launch(args);
    } // main



} // Driver

