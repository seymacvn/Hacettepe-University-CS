

import java.util.ArrayList;
import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class GameViewManager {

	private AnchorPane gamePane;
	private Scene gameScene;
	private Stage gameStage;
	
	private static final int GAME_HEIGHT = 700;
	private static final int GAME_WIDTH = 600;
	
	private ImageView car;
	private Text score;
	private Text level;
	private Text gameOver;
	private Text yourScore;
	private Text pleaseEnter;



	
	private boolean isLeftKeyPressed;
	private boolean isRightKeyPressed;
	private boolean isUpKeyPressed;

	private AnimationTimer gameTimer;
	
	private GridPane gridpane;
	private GridPane gridpane1;
	
	private final static String gameBackgroung = "newBackground.png";
	private final static String otherCars = "car_blue_1.png";
	private ImageView[] othercars, othercars1, othercars2;
	Random randomPossition;
	
	private static int speed1 = 3;
	private static int count;
	private static int levelNo = 1 ;
	
	public GameViewManager(){
		setUp();
		keyControl();
	}
	
	private void createOtherCars() {
		othercars = new ImageView[1];
		othercars1 = new ImageView[1];
		othercars2 = new ImageView[1];



		for(int i = 0 ; i < othercars.length ; i ++) {
			othercars[i] = new ImageView(otherCars);
			Image newImage =new Image(otherCars);
			othercars[i].setImage(newImage);
			carPossition(othercars[i]);
			gamePane.getChildren().add(othercars[i]);
			
		}for(int i = 0 ; i < othercars1.length ; i ++) {
			othercars1[i] = new ImageView(otherCars);
			Image newImage=new Image(otherCars);
			othercars1[i].setImage(newImage);
			carPossition1(othercars1[i]);
			gamePane.getChildren().add(othercars1[i]);
			
		}for(int i = 0 ; i < othercars2.length ; i ++) {
			othercars2[i] = new ImageView(otherCars);
			Image newImage=new Image(otherCars);
			othercars2[i].setImage(newImage);
			carPossition2(othercars2[i]);
			gamePane.getChildren().add(othercars2[i]);
		}
	}
	private void moveOthers() {
		for (int i = 0 ; i < othercars.length ; i++) {
			othercars[i].setLayoutY(othercars[i].getLayoutY() +speed1);
			othercars[i].setRotate(othercars[i].getRotate());
		}
		for (int i = 0 ; i < othercars1.length ; i++) {
			othercars1[i].setLayoutY(othercars1[i].getLayoutY() +speed1);
			othercars1[i].setRotate(othercars1[i].getRotate());
			
		}for (int i = 0 ; i < othercars2.length ; i++) {
			othercars2[i].setLayoutY(othercars2[i].getLayoutY() +speed1);
			othercars2[i].setRotate(othercars2[i].getRotate());
		}
	}
	private void check() {
		for (int i = 0 ; i < othercars.length ; i++) {
			if(othercars[i].getLayoutY() > 700) {
				count = count + levelNo;
				score.setText( "SCORE: " + count);
				calculateLevel(count);
				carPossition(othercars[i]);
			}
		}
		for (int i = 0 ; i < othercars1.length ; i++) {
			if(othercars1[i].getLayoutY() > 700) {
				count = count + levelNo;
				score.setText( "SCORE: " + count);
				calculateLevel(count);
				carPossition1(othercars1[i]);
			}
		}
		for (int i = 0 ; i < othercars2.length ; i++) {
			if(othercars2[i].getLayoutY() > 700) {
				count = count + levelNo;
				calculateLevel(count);
				score.setText( "SCORE: " + count);
				carPossition2(othercars2[i]);
			}
		}
	}
	
	
	
	private void calculateLevel(int count) {
		if( count > 3 && count % 3 == 1) {
			levelNo ++;
			level.setText(" LEVEL: "+ levelNo);
			speed1 = speed1 + 1;
		}
	}

	private void carPossition(ImageView image) {
			image.setLayoutX(randomFunction(100, 173));
			image.setLayoutY(-(randomFunction(0,1000)+ 600) +440);
	}
	private void carPossition1(ImageView image) {
		image.setLayoutX(randomFunction(242, 315));
		image.setLayoutY(-(randomFunction(1000, 2000)+ 600) +440);
	}
	private void carPossition2(ImageView image) {
		image.setLayoutX(randomFunction(385, 430));
		image.setLayoutY(-(randomFunction(2000,3000)+ 600) +440);
	}

	private static int randomFunction(int min, int max) {
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
	
	
	private void keyControl() {

		gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {

				@Override
				public void handle(KeyEvent event) {
					if(event.getCode()== KeyCode.LEFT) {
						isLeftKeyPressed = true;
					}else if(event.getCode()== KeyCode.RIGHT) {
						isRightKeyPressed = true;
					}else if(event.getCode() == KeyCode.UP) {
						isUpKeyPressed = true;
					}
				}
				
			});

		gameScene.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(event.getCode()== KeyCode.LEFT) {
					isLeftKeyPressed = false;
				}else if(event.getCode()== KeyCode.RIGHT) {
					isRightKeyPressed = false;
				}else if(event.getCode() == KeyCode.UP) {
					isUpKeyPressed = false;
				}
			}
			
		});
	}
	

	private void setUp() {
		gamePane = new AnchorPane();
		gameScene = new Scene(gamePane, GAME_WIDTH, GAME_HEIGHT);
		gameStage = new Stage();
		gameStage.setTitle( "HUBBM-Racer" );
		gameStage.setScene(gameScene);
		
		
	}
	public void CreateNewGame() {
		createBackground();
		CreateCar();
		CreateScore();
		createOtherCars();
		createGameLoop();
		gameStage.show();
	}

	private void CreateCar() {
		car = new ImageView("car_red_1.png");
		car.setLayoutX((GAME_WIDTH /2)-35);
		car.setLayoutY(GAME_HEIGHT - 150);
		gamePane.getChildren().add(car);
	}
	
	private void CreateScore() {
		score = new Text(" SCORE: 0");
		score.setTextAlignment(TextAlignment.JUSTIFY);
        score.setFont(new Font(20));
        score.setFill(Color.RED);
        score.setLayoutX(10);
        score.setLayoutY(30);
        gamePane.getChildren().add(score);
        level = new Text(" LEVEL: 1");
		level.setTextAlignment(TextAlignment.JUSTIFY);
		level.setFont(new Font(20));
		level.setFill(Color.RED);
		level.setLayoutX(10);
		level.setLayoutY(60);
        gamePane.getChildren().add(level);
	}
	

	private void createGameLoop() {
		gameTimer = new AnimationTimer() {

			@Override
			public void handle(long now) {
				moveBackground();
				moveOthers();
				check();
				changeColor();
				checkGame();
				moveCar();
			}
		};
			gameTimer.start();
		}
	private void changeColor() {
		for (int i = 0 ; i < othercars.length ; i++) {
			if(othercars[i].getLayoutY() > 530) {
				Image newImage=new Image("car_green_1.png");
				othercars[i].setImage(newImage);
			}
			else {
				Image newImage=new Image("car_blue_1.png");
				othercars[i].setImage(newImage);
			}
		}
		for (int i = 0 ; i < othercars1.length ; i++) {
			if(othercars1[i].getLayoutY() > 530) {
				Image newImage=new Image("car_green_1.png");
				othercars1[i].setImage(newImage);
			}
			else {
				Image newImage=new Image("car_blue_1.png");
				othercars1[i].setImage(newImage);
			}
		}
		for (int i = 0 ; i < othercars2.length ; i++) {
			if(othercars2[i].getLayoutY() > 530) {
				Image newImage=new Image("car_green_1.png");
				othercars2[i].setImage(newImage);
			}
			else {
				Image newImage=new Image("car_blue_1.png");
				othercars2[i].setImage(newImage);
			}
		}
	}
	private void moveCar() {
		if(isLeftKeyPressed && !isRightKeyPressed) {
			if(car.getLayoutX() > 100) {
				car.setLayoutX(car.getLayoutX() - 5);
			}
		}
	    if (isRightKeyPressed && !isLeftKeyPressed) {
	    	if(car.getLayoutX() < 430) {
				car.setLayoutX(car.getLayoutX() + 5);
	    	}
	    }
	    if ( isUpKeyPressed ) {
			moveBackground();
	    }
	}
	
	
	private void createBackground() {
		gridpane = new GridPane();
		gridpane1 = new GridPane();

		for (int i = 0; i < 120; i++) {
			ImageView background = new ImageView(gameBackgroung);
			ImageView background1 = new ImageView(gameBackgroung);
			GridPane.setConstraints(background, i %3, i /3);
			GridPane.setConstraints(background1, i %3, i /3);
			gridpane.getChildren().add(background);
			gridpane1.getChildren().add(background1);
		}
		gridpane1.setLayoutY(-1024);
		gamePane.getChildren().addAll(gridpane, gridpane1);
	}
	
	private void moveBackground() {

		gridpane.setLayoutY(gridpane.getLayoutY() + speed1);
		gridpane1.setLayoutY(gridpane1.getLayoutY() + speed1);

		if(gridpane.getLayoutY() >= 1024) {
			gridpane.setLayoutY(-1024);
		}
		if(gridpane1.getLayoutY() >= 1024) {
			gridpane1.setLayoutY(-1024);
		}
	}
	private void checkGame() {
		for(int i = 0 ; i < othercars.length; i ++) {
			if(othercars[i].getBoundsInParent().intersects(car.getBoundsInParent())) {
			    gameOver();
			    Image newImage=new Image("car_black_1.png");
			    othercars[i].setImage(newImage);
			}
		}
		for(int i = 0 ; i < othercars1.length; i ++) {
			if(othercars1[i].getBoundsInParent().intersects(car.getBoundsInParent())) {
				gameOver();
				Image newImage=new Image("car_black_1.png");
				othercars1[i].setImage(newImage);

			}
		}
		for(int i = 0 ; i < othercars2.length; i ++) {
			if(othercars2[i].getBoundsInParent().intersects(car.getBoundsInParent())) {
				gameOver();
				Image newImage=new Image("car_black_1.png");
				othercars2[i].setImage(newImage);
				
			}
		}
	}
	
	private void gameOver() {
		gameOver = new Text(" GAME OVER! ");
		Image newImage=new Image("car_black_1.png");
		car.setImage(newImage);
		gameOver.setTextAlignment(TextAlignment.JUSTIFY);
		gameOver.setFont(new Font(45));
		gameOver.setFill(Color.RED);
		gameOver.setLayoutX(160);
		gameOver.setLayoutY(150);
        gamePane.getChildren().add(gameOver);
        yourScore = new Text("Your Score: " + count);
        yourScore.setTextAlignment(TextAlignment.JUSTIFY);
        yourScore.setFont(new Font(45));
        yourScore.setFill(Color.RED);
        yourScore.setLayoutX(180);
        yourScore.setLayoutY(200);
        gamePane.getChildren().add(yourScore);
        pleaseEnter = new Text("Press ENTER to restart!");
        pleaseEnter.setTextAlignment(TextAlignment.JUSTIFY);
        pleaseEnter.setFont(new Font(45));
        pleaseEnter.setFill(Color.RED);
        pleaseEnter.setLayoutX(100);
        pleaseEnter.setLayoutY(300);
        gamePane.getChildren().add(pleaseEnter);   
        count = 0;
        levelNo = 1;
        speed1 = 3;
        gameTimer.stop();
        restart();
	}

	private void restart() {
			gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {

					@Override
					public void handle(KeyEvent event) {
						if(event.getCode()== KeyCode.ENTER) {
							gameStage.close();
					       
						}
					}
					
				});	
	}
}