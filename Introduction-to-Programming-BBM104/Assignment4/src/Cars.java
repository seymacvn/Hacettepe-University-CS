

import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Cars extends VBox {
	
	private ImageView OursCar;
	//private String ours = "car_blue_1.png";
	
	public Cars(String url) {
		OursCar = new ImageView (url);
		this.setAlignment(Pos.CENTER);
		this.setSpacing(20);
		
	}

}
