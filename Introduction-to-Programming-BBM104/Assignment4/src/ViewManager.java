
import java.beans.EventHandler;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class ViewManager {
	
	private static final int HEIGHT = 700;
	private static final int WIDTH = 600;
	private AnchorPane mainPane;
	private static Scene mainScene;
	private Stage mainStage;
	
	public ViewManager() {
		Text waitingForKey = new Text("Press ENTER to start a new game");
        waitingForKey.setTextAlignment(TextAlignment.JUSTIFY);
        waitingForKey.setFont(new Font(35));
        waitingForKey.setFill(Color.RED);
        waitingForKey.setLayoutX(50);
        waitingForKey.setLayoutY(250);
		mainPane = new AnchorPane();
		mainPane.getChildren().add(waitingForKey);
		mainScene = new Scene(mainPane, WIDTH, HEIGHT);
		mainStage= new Stage();
		mainStage.setScene(mainScene);
		createBackground();
		start();
	}
	
	public Stage getMainStage() {
		return mainStage;
	}
	
	private void createBackground() {
		Image backgroundImage = new Image("background.png", 600, 700, false, true);
		BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
		mainPane.setBackground(new Background(background));
	}
	private static void start() {
		mainScene.addEventHandler(KeyEvent.KEY_PRESSED, key -> {
			switch(key.getCode()) {
				case ENTER:
					start1();
                	break;
			}
		});
            }

	private static  void start1() {
		GameViewManager gameManager = new GameViewManager();
    	gameManager.CreateNewGame();		
	}
	}
	
	
	
	
	
	


