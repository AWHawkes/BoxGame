package application;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
//import javafx.scene.input.MouseButton;
import javafx.scene.transform.Translate;
import javafx.scene.transform.*;

public class boxMaker<E> {

	double frameX = 800;
	double frameY = 400;
	double orgSceneX, orgSceneY;
	double topX, botX, leftY, rightY;
	Color color;
	E e;
	
	// constructor to initialize object
	public boxMaker() {
	}
	
	public void setAttributes(String var, double topX,double botX,double leftY,double rightY, Color color )
	{
		if (var == "boxBody")
		{ 
			this.topX = topX;
			this.botX = botX;
			this.leftY = leftY;
			this.rightY = rightY;
			this.color = color;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public E createBox(String var) {
		
		// materials
		
		// transforming right piston
		Translate pistonHoriRight = new Translate();
		pistonHoriRight.setX(32);
		pistonHoriRight.setY(0);
		pistonHoriRight.setZ(0);
		
		Color b = Color.BLUE;
		Color w = Color.WHITE;
		Color g = Color.GRAY;
		Color r = Color.RED;
		
		boolean Dpressed = false;
		
		if(var == "boxBody")
		{
			e = (E)new Polygon();
			((Polygon) e).getPoints().addAll(new Double[]
			{
				topX,leftY,
				botX,leftY,
				botX,rightY,
				topX,rightY
			});
			((Shape) e).setFill(color);
			((Node) e).setCursor(Cursor.HAND);
			
		} else if(var == "piston") {
			e = null;
		} else {
		
			e = null;
		}
		

		((Node) e).setOnKeyPressed((t) -> {
			if(t.getCode() == KeyCode.D && Dpressed == false) {
				((Polygon) e).getTransforms().addAll(pistonHoriRight);
			}
		});
		
		
		return e;
	}
	
	
	
	
	
}
