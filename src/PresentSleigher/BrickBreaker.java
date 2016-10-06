package PresentSleigher;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;

/**
 * This is the main class where my game is created. It handles the bounds, levels, score updating,
 * life updating, when the game starts, when it is game over, and when you win.
 *
 * Author: Kathleen Tynan
 */
public class BrickBreaker extends Parent {

    private ArrayList<Bricks> bricks=new ArrayList<Bricks>();
    private int brickCount = 24;
    private Group group;
    private Group info;
    private ImageView background;

    private Paddle paddle;
    private Ball ball;
    private Text score;
    private Text life;
    private Text theLevel;
    private Text nextLevel = new Text();
    private Timeline timeline;
    private Timeline startTimeline;
    private double ballSpeed;
    private int level = 1;
    private int rowMax = 6, colMax = 4;
    private Text yourScore = new Text();
    private Text finalScore = new Text();

    private int keepScore = 0;
    private int keepLife = 3;
    private boolean stateLevel = false;
    private boolean startUp=true;
    private StartScreen startScreen;

    double ballX, ballY;
    double xDir, yDir;

    /*
    *
    * creates a new BrickBreaker game
    *
     */
    public BrickBreaker() {

        //creates the start scene
        startScreen = new StartScreen();
        group = new Group();
        startScreen.setFocusTraversable(true);
        getChildren().add(startScreen);
        startScreen();



    }//BrickBreaker

    /*
    *
    * timeline for the start scene and then leads into the
    * initialization of the real game
    *
     */
    private void startScreen(){


        startTimeline = new Timeline();
        startTimeline.setCycleCount(timeline.INDEFINITE);
        KeyFrame kf = new KeyFrame(new Duration(100), event -> {

            startScreen.init();
            startScreen.moveSanta();


            startScreen.setOnKeyPressed(event1 -> {


                KeyCode keyCode = event1.getCode();
                //handle the ENTER to start
                if (keyCode == KeyCode.ENTER) {

                    startTimeline.stop();
                    getChildren().remove(startScreen);
                    startScreen.setFocusTraversable(false);


                    //starting real game
                    getChildren().add(group);
                    content();
                    background();
                    ball = new Ball();
                    paddle = new Paddle();
                    initStartingTimeline();
                    group.setFocusTraversable(true);
                    group.getChildren().addAll(ball, paddle, info);


                }


            });


        });

        startTimeline.getKeyFrames().add(kf);
        startTimeline.play();



        //handles key movements
        group.setOnKeyPressed(event -> {

            KeyCode keyCode = event.getCode();

            switch (keyCode) {
                case LEFT:
                    paddle.setTranslateX(paddle.getTranslateX() - 30);
                    break;
                case RIGHT:
                    paddle.setTranslateX(paddle.getTranslateX() + 30);
                    break;
                case UP:
                    if(startUp) {
                        init();
                        startUp=false;
                    }
                    break;
            }

        });//setOnKeyPressed



    }


    /*
    *
    * Initializes when the objects go for the game
    * start
    *
     */
    private void initStartingTimeline() {
        levelIncrease(level);
        paddle.setTranslateY((Commons.SCREEN_HEIGHT - 50));
        paddle.setTranslateX((Commons.SCREEN_WIDTH - paddle.getWidth()) / 2.0);
        ball.setTranslateX((Commons.SCREEN_WIDTH - ball.getDiameter()) / 2.0);
        ball.setTranslateY(Commons.SCREEN_HEIGHT - 50 - ball.getDiameter());


    }//initStartingTimeline


    /*
    *
    * initializes the actual start / restart of the game,
    * starts the animation timeline
    *
    *
     */
    private void init() {


        //sets the next level text as false
        nextLevel.setVisible(false);
        finalScore.setVisible(false);
        yourScore.setVisible(false);
        //sets bricks as visible again when next level started
        if(stateLevel) {
            for (int i = 0; i < bricks.size(); i++) {
                bricks.get(i).setVisible(true);
                background.setVisible(true);
            }
            stateLevel = false;
        }//if


        //assigns the speed and direction of ball
        xDir = randomNumber();
        yDir = -ballSpeed;

        //starts the timeline for gameplay
        KeyFrame kf = new KeyFrame(Commons.ANIMATION_TIME, event -> {

            start();

        });//kf

        timeline = new Timeline(kf);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

    }//init

    /*
    *
    * starting of the game
    *
     */
    private void start() {

        //gets ball moving
        ballX = ball.getTranslateX() + xDir;
        ballY = ball.getTranslateY() + yDir;
        ball.setTranslateX(ballX);
        ball.setTranslateY(ballY);


        //bounds so ball can't go off screen
        if (ballX < 0) {
            xDir = ballSpeed;
        }//if

        if (ballX > Commons.SCREEN_WIDTH - ball.getDiameter()) {

            xDir = -ballSpeed;
        }//if

        if (ball.getTranslateY() < 0) {
            yDir = ballSpeed;
        }//if


        //bounds so paddle can go off screen
        if(paddle.getTranslateX() <= 0){

            paddle.setTranslateX(paddle.getTranslateX() + 20);

        }else if(paddle.getTranslateX()+paddle.getWidth() >= Commons.SCREEN_WIDTH){

            paddle.setTranslateX(paddle.getTranslateX() - 20);
        }//if


        //paddle and ball hit
        if (yDir > 0 &&
                ball.getTranslateY() + ball.getDiameter() > paddle.getTranslateY() &&
                ballX >= paddle.getTranslateX() - ball.getDiameter() &&
                ballX <= paddle.getTranslateX() + paddle.getWidth() + ball.getDiameter()) {


            yDir = -ballSpeed;
            xDir = randomNumber();

        }//if

        //ball fall below paddle
        if (ball.getTranslateY() + ball.getDiameter() > paddle.getTranslateY() + paddle.getHeight()) {
            yDir = -ballSpeed;
            timeline.stop();
            loseLife();
            startUp = true;
            keepLife--;
        }//if


        //brick hit
        brickHit();


        //score and lives
        updateScore(keepScore);
        updateLives(keepLife);

        //if no lives, then game over
        if (keepLife == 0) gameOver();

        //when no more bricks, level ends
        if (brickCount <= 0) levelEnd();


    }//start

    /*
    *
    * handles when the level ends
    *
     */
    private void levelEnd(){

        //stop timeline when level ends
        timeline.stop();

        //level up
        level++;
        levelIncrease(level);


        //notify to press up for next level when on level 1 and 2
        if(level<=3) {
            nextLevel.setText("Press UP for Next Level");
            nextLevel.setTranslateX(95);
            nextLevel.setTranslateY(260);
            nextLevel.setFont(Font.font("Arial", FontWeight.BOLD, 36));
            nextLevel.setFill(Color.WHITE);
            yourScore();
            if(level==2) group.getChildren().addAll(nextLevel, yourScore, finalScore);
            nextLevel.setVisible(true);
            yourScore.setVisible(true);
            finalScore.setVisible(true);
            background.setVisible(false);
            for (Bricks brick : bricks) {
                brick.setVisible(false);
                stateLevel = true;
            }//for
        }//if


        //resets paddle and ball to initial position again
        loseLife();

        //initiates brickcount when moved to next level
        if(level==2){
            brickCount=30;
        }else if(level==3){
            brickCount=36;
        }//if

        startUp=true;

    }

    /*
    *
    * handles when a brick is hit by the ball
    *
     */
    private void brickHit() {

        //when hit by a brick
        for (int i = 0; i < bricks.size(); i++) {
            if (bricks.get(i).isVisible()) {
                if (bricks.get(i).getBoundsInParent().intersects(ball.getBoundsInParent())) {

                    //change present type
                    if (bricks.get(i).getType().equals(Commons.PRESENT)) {
                        bricks.get(i).changePresent(Commons.BROKEN1);
                    } else if (bricks.get(i).getType().equals(Commons.BROKEN1)) {
                        bricks.get(i).changePresent(Commons.BROKEN2);
                    } else if (bricks.get(i).getType().equals(Commons.BROKEN2)) {
                        bricks.get(i).setVisible(false);
                        keepScore += 1;
                        brickCount--;
                    }//if


                    //hit bottom side of brick
                    if (bricks.get(i).getTranslateY() + Commons.BRICK_HEIGHT >= ball.getTranslateY() && ball.getTranslateY() >
                            bricks.get(i).getTranslateY() + Commons.BRICK_HEIGHT / 2) {
                        yDir = ballSpeed;
                    }//if

                    //hit top side of brick
                    if (bricks.get(i).getTranslateY() <= ball.getTranslateY() + ball.getDiameter() && ball.getTranslateY() + ball.getDiameter() <
                            bricks.get(i).getTranslateY() + Commons.BRICK_HEIGHT / 2) {
                        yDir = -ballSpeed;
                    }//if

                    //hit left side of brick
                    if (bricks.get(i).getTranslateX() <= ball.getTranslateX() + ball.getDiameter() && ball.getTranslateX() + ball.getDiameter()
                            < bricks.get(i).getTranslateX() + Commons.BRICK_WIDTH / 2) {
                        xDir = -ballSpeed;
                    }//if

                    //hit right side of brick
                    if (bricks.get(i).getTranslateX() + Commons.BRICK_WIDTH >= ball.getTranslateX() && ball.getTranslateX()
                            > bricks.get(i).getTranslateX() + Commons.BRICK_WIDTH / 2) {
                        xDir = ballSpeed;
                    }//if
                }
            }//if visible


        }//for

    }//brickHit

    /*
    *
    * Remove everything from screen and display game over
    *
     */

    private void gameOver() {

        startUp = false;
        paddle.setVisible(false);
        ball.setVisible(false);
        info.setVisible(false);
        background.setVisible(false);
        for (int i=0; i<bricks.size(); i++) {
            bricks.get(i).setVisible(false);
        }

        Text gameOver = new Text();
        gameOver.setText("GAME OVER");
        gameOver.setTranslateX(200);
        gameOver.setTranslateY(260);
        gameOver.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        gameOver.setFill(Color.WHITE);
        yourScore();
        group.getChildren().add(gameOver);
        if(level==1)group.getChildren().addAll(yourScore, finalScore);
        yourScore.setVisible(true);
        finalScore.setVisible(true);


    }//gameOver

    /*
    *
    * puts back paddle and ball to initial setting
    *
     */
    private void loseLife() {

        paddle.setTranslateY((Commons.SCREEN_HEIGHT - 50));
        paddle.setTranslateX((Commons.SCREEN_WIDTH - paddle.getWidth()) / 2.0);
        ball.setTranslateX((Commons.SCREEN_WIDTH - ball.getDiameter()) / 2.0);
        ball.setTranslateY(Commons.SCREEN_HEIGHT - 50 - ball.getDiameter());

    }//loseLife

    /*
    *
    * generates random number for x speed when hit paddle
    *
     */
    public double randomNumber() {
        Random generator = new Random();
        double randNum =1;
        if(level==1){
            randNum = Level.BALL_L1_SPEED;
        }else if(level==2){
            randNum=Level.BALL_L2_SPEED;
        }else if(level == 3){
            randNum=Level.BALL_L3_SPEED;
        }

        return generator.nextInt((int) (randNum+randNum)) - randNum;
    }//randomNumber

    /*
    *
    * updates the score
    *
     */
    private void updateScore(int n) {

        String s = String.valueOf(n);
        score.setText(s);

    }//updateScore

    /*
    *
    * updates the lives
    *
     */
    private void updateLives(int n) {

        String s = String.valueOf(n);
        life.setText(s);

    }//updateLives

    /*
    *
    * update the level label
    *
     */
    private void updateLevel(int n){

        String s = String.valueOf(n);
        theLevel.setText(s);

    }

    /*
    *
    * initializes the bricks
    *
     */
    private void initBricks() {

        bricks.clear();



        for (int row = 0; row < rowMax; row++) {
            for (int col = 0; col < colMax; col++) {

                Bricks brick = new Bricks(Commons.PRESENT);
                brick.setTranslateX(row * Commons.BRICK_WIDTH + 115);
                brick.setTranslateY(col * Commons.BRICK_HEIGHT + 20);
                bricks.add(brick);
                group.getChildren().add(brick);
            }//for

        }//for

    }//initBricks


    /*
    *
    * content/info for the game
    *
     */
    private void content() {

        info = new Group();
        Text scoreLabel = new Text();
        scoreLabel.setText("Score:");
        scoreLabel.setFill(Color.WHITE);
        scoreLabel.setTranslateX(15);
        scoreLabel.setTranslateY(30);
        scoreLabel.setFont(Font.font("Arial Black", FontWeight.BOLD, 13));
        score = new Text();
        score.setTranslateX(65);
        score.setTranslateY(30);
        score.setText("0");
        score.setFill(Color.WHITE);
        score.setFont(Font.font("Arial Black", FontWeight.BOLD, 13));
        Text lifeLabel = new Text();
        lifeLabel.setTranslateX(15);
        lifeLabel.setTranslateY(45);
        lifeLabel.setFill(Color.WHITE);
        lifeLabel.setText("Lives:");
        lifeLabel.setFont(Font.font("Arial Black", FontWeight.BOLD, 13));
        life = new Text();
        life.setTranslateX(65);
        life.setTranslateY(45);
        life.setFill(Color.WHITE);
        life.setText("3");
        life.setFont(Font.font("Arial Black", FontWeight.BOLD, 13));
        Text levelLabel = new Text();
        levelLabel.setText("Level: ");
        levelLabel.setFill(Color.WHITE);
        levelLabel.setTranslateX(15);
        levelLabel.setTranslateY(60);
        levelLabel.setFont(Font.font("Arial Black", FontWeight.BOLD, 13));
        theLevel=new Text();
        theLevel.setText("1");
        theLevel.setFill(Color.WHITE);
        theLevel.setFont(Font.font("Arial Black", FontWeight.BOLD, 13));
        theLevel.setTranslateX(65);
        theLevel.setTranslateY(60);


        info.getChildren().addAll(scoreLabel, score, lifeLabel, life, levelLabel, theLevel);

    }//content

    /*
    *
    * when level increases
    *
     */
    private void levelIncrease(int level) {

        if (level == 1) {
            ballSpeed = Level.BALL_L1_SPEED;
            rowMax = Level.L1_ROWS;
            colMax = Level.L1_COLS;
            brickCount = 24;
            initBricks();
        } else if (level == 2) {
            ballSpeed = Level.BALL_L2_SPEED;
            timeline.stop();
            rowMax = Level.L2_ROWS;
            colMax = Level.L2_COLS;
            brickCount = 30;
            keepLife++;
            initBricks();
            updateLevel(level);
        } else if (level == 3) {
            ballSpeed = Level.BALL_L3_SPEED;
            timeline.stop();
            rowMax = Level.L3_ROWS;
            colMax = Level.L3_COLS;
            brickCount = 36;
            keepLife++;
            initBricks();
            updateLevel(level);
        }else if(level == 4){
            timeline.stop();
            startUp=false;

            paddle.setVisible(false);
            ball.setVisible(false);
            info.setVisible(false);
            background.setVisible(false);
            for (Bricks brick : bricks) {
                brick.setVisible(false);
            }//for

            Text youWin = new Text();
            youWin.setText("YOU WIN");
            youWin.setTranslateX(225);
            youWin.setTranslateY(260);
            youWin.setFont(Font.font("Arial", FontWeight.BOLD, 36));
            youWin.setFill(Color.WHITE);
            yourScore();
            group.getChildren().addAll(youWin);
            yourScore.setVisible(true);
            finalScore.setVisible(true);
        }




    }//level increase

    /*
    *
    * the final score of the game display
    *
     */
    private void yourScore(){

        yourScore.setText("Your Score: ");
        String s = String.valueOf(keepScore);
        finalScore.setText(s);
        yourScore.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        yourScore.setFill(Color.WHITE);
        finalScore.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        finalScore.setFill(Color.WHITE);
        yourScore.setTranslateY(320);
        yourScore.setTranslateX(200);
        finalScore.setTranslateY(320);
        finalScore.setTranslateX(380);


    }

    /*
    *
    * add background image
    *
     */
    private void background(){
        background = new ImageView();
        Image backgroundImage = new Image("file:src/main/java/SantaSleigher/images/background.png");
        background.setImage(backgroundImage);
        background.setFitWidth(Commons.SCREEN_WIDTH);
        background.setFitHeight(Commons.SCREEN_HEIGHT);
        group.getChildren().add(background);



    }//background


}
