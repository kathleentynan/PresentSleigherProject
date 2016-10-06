package PresentSleigher;


import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


/**
 * This creates the paddle, its image, and gets the width and height of it.
 *
 * Author: Kathleen Tynan
 */
public class Paddle extends Parent{

    private ImageView imageView;
    private int width;
    private int height;
    private Image image;

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public Paddle(){
        image = new Image("file:src/images/sleigh.png");
        height = (int)image.getHeight();
        width = 150;
        imageView = new ImageView();
        imageView.setImage(image);
        getChildren().add(imageView);

    }


}
