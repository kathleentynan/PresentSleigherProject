package PresentSleigher;


import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.Random;

/*
*
*
* this is the start screen extra credit
* It's the intro to the game and tells the player
* to press enter to start
*
*Author: Kathleen Tynan
*
 */

public class StartScreen extends Parent{

    private Group group;
    private Timeline timeline;
    private Text message;
    private Text pressStart;
    private Text name;
    private Color[] colors = {Color.RED, Color.GREEN};
    private ImageView background;
    private ImageView santa;
    private int xDir=10, yDir=5;

    /*
    *
    * creates the nodes for the start screen
    *
     */
    public StartScreen(){

        group = new Group();
        message = new Text();
        pressStart = new Text();
        name = new Text();
        getChildren().add(group);
        background();
        santa();
        group.getChildren().addAll(message, pressStart, name);




    }//StartScreen


    /*
    *
    * moves santa across the screen
    *
     */
    public void moveSanta(){

        santa.setTranslateX(santa.getTranslateX()+xDir);
        santa.setTranslateY(santa.getTranslateY()-yDir);

    }//moveSanta

    /*
    *
    * intializes the texts
    *
     */
    public void init(){



        name.setText("Kathleen Tynan");
        name.setFont(Font.font("Arial Black", FontWeight.BOLD, 24));
        name.setTranslateX(200);
        name.setTranslateY(260);
        name.setFill(Color.GREEN);

        pressStart.setText("Press ENTER to Start!");
        pressStart.setTranslateX(130);
        pressStart.setTranslateY(535);
        pressStart.setFont(Font.font("Arial Black", FontWeight.BOLD, 30));
        pressStart.setFill(randomColor());

        message.setText("PRESENT SLEIGHER");
        message.setTranslateX(110);
        message.setTranslateY(220);
        message.setFont(Font.font("Arial Black", FontWeight.BOLD, 36));
        message.setFill(Color.RED);




    }//init

    /*
    *
    * randomizes the colors for the press
    * start button
    *
     */
    private Color randomColor(){

        Random ranNum = new Random();
        int number = ranNum.nextInt(2);

        return colors[number];

    }//randomColor

    /*
    *
    * creates the santa that moves across the
    * screen
    *
     */
    private void santa(){

        santa = new ImageView();
        Image santaImage = new Image("file:src/images/santa.png");
        santa.setImage(santaImage);
        santa.setFitHeight(santaImage.getHeight());
        santa.setFitWidth(santaImage.getWidth());
        santa.setTranslateX(-300);
        santa.setTranslateY(400);
        santa.setRotate(-20);
        group.getChildren().add(santa);

    }//santa

    /*
    *
    * creates background for start screen
    *
     */
    private void background(){

        background = new ImageView();
        Image image = new Image("file:src/images/startbackground.png");
        background.setImage(image);
        background.setFitWidth(600);
        background.setFitHeight(600);
        group.getChildren().add(background);


    }//background

} //StartScreen

