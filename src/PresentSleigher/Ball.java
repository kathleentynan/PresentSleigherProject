package PresentSleigher;

import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This creates the ball, its image, and retrieves it's diameter
 * Author: Kathleen Tynan
 */
public class Ball extends Parent {

    private ImageView imageView;
    private int diameter;
    private Image image;

    public Ball(){
        imageView = new ImageView();
        image = new Image("file:src/images/elf.png");
        getChildren().add(imageView);
        imageView.setImage(image);
        diameter = (int) imageView.getImage().getWidth();

    }//Ball


    public int getDiameter(){
        return diameter;
    }//getDiameter



}
