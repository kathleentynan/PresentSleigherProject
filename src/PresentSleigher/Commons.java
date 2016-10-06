package PresentSleigher;


import javafx.util.Duration;

/**
 * This is the static final class for the field and objects in the field.
 * Author: Kathleen Tynan
 */
public final class Commons {

    public static final Duration ANIMATION_TIME = Duration.millis(40);

    //screen
    public static final int SCREEN_WIDTH = 600;
    public static final int SCREEN_HEIGHT = 600;

    //bricks
    public static final int BRICK_WIDTH = 60;
    public static final int BRICK_HEIGHT = 60;


    //bricks images
    public static final String PRESENT = "file:src/images/present.png";
    public static final String BROKEN1 = "file:src/images/broken1.png";
    public static final String BROKEN2 = "file:src/images/broken2.png";


    private Commons(){

    }
}
