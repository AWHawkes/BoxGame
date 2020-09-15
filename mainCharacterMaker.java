package application;

import java.awt.Color;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;

public class mainCharacterMaker {
	double orgSceneX, orgSceneY; 
	
	public Button display(Pane pane, double frameX, double frameY)
	{
		Button mainCharacter = new Button("start");
		Color b = Color.BLUE;
		javafx.scene.paint.Color blue = javafx.scene.paint.Color.rgb(b.getRed(), b.getGreen(), b.getBlue(), b.getAlpha() / 255.0);
		
		mainCharacter.setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent e)
			{
				boxMaker<Polygon> boxTemp = new boxMaker<Polygon>();
				boxTemp.setAttributes("boxBody",100.0,200.0,200.0,300.0, blue);
				Polygon boxReal = (Polygon) boxTemp.createBox("boxBody");
				
				pane.getChildren().addAll(boxReal);
			}
		});
		
		return mainCharacter;
	}
	
}
