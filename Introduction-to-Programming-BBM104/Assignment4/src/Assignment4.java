

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class Assignment4 extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			
			ViewManager manager = new ViewManager();
			primaryStage = manager.getMainStage();
			primaryStage.setTitle( "HUBBM-Racer" );
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}