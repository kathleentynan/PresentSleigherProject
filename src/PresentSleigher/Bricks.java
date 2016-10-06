package PresentSleigher;

import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This creates manages the bricks, their images, and handles when their images change
 * Author: Kathleen Tynan
 */
public class Bricks extends Parent {




    private String type;
    private ImageView content;
    private Image image;



    public Bricks(String type){

        content = new ImageView();
        changePresent(type);

        getChildren().add(content);

    }//bricks

    public String getType(){
        return type;
    }//getType



    /*
    *
    * this changes the present
    *
     */
    public void changePresent(String newType){
        this.type = newType;
        image = new Image(newType);
        content.setImage(image);
        content.setFitWidth(Commons.BRICK_WIDTH);
        content.setFitHeight(Commons.BRICK_HEIGHT);

    }//changePresent

    






}
