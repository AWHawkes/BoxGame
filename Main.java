package application;
	
import java.awt.Color;
import java.awt.geom.Area;
import java.awt.geom.*;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.shape.MeshView;
import java.awt.Color;
import javafx.scene.transform.*;
import javafx.scene.text.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.animation.AnimationTimer;
import java.lang.Math.*;


public class Main extends Application {
	
	static Stage window;
	Scene scene;
	Pane game;
	BorderPane root;
	
	double windowWidth = 1100;
	double windowHeight = 900;
	double boundsFix = 0;
	
	// main menu
	Text menuTitle;
	Rectangle menuBackground;
	
	Text localSingleplayerText;
	Rectangle localSingleplayerRectangle;
	
	Text localMultiplayerText;
	Rectangle localMultiplayerRectangle;
	
	Text quitText;
	Rectangle quitRectangle;
	
	Group menuGroup;
	// menu
	VBox startMenu;
	
	// arena designations
	double arenaWidth = 800;
	double arenaHeight = 600;
	double arenaX = 100; // bottom left coords of arena
	double arenaY = 700;
	
	// global variables
	
	// counters
	int mainBox = 0;
	int enemyBox = 0;
	int toWin = 3;
	
	Text mainCounter;
	Text enemyCounter;
	
	Text countDown;
	double countDownTime = 3.0;
	double startCountDown = countDownTime;
	
	
	boolean menu = true;
	boolean gameSetup = false;
	boolean gameplay = false;
	
	// debug
	
	Text debugText;
	String debugString = new String("Debug: ");
	Group debug;
	
	// UI
	
	boolean enemyAI = true;
	boolean countDownVisible = true;
	Rectangle UIBackground;
	Group UI;
	
	// box groups
	Group boxMain;
	Group mboxMain;
	
	// reset and misc variables
	int isReset = 1;
	
	Text information;
	
	// WASD buttons
	boolean Dpressed = false;
	boolean Apressed = false;
	boolean Wpressed = false;
	boolean Spressed = false;
	
	/* ARROW keys */
	boolean RIGHTpressed = false;
	boolean LEFTpressed = false;
	boolean UPpressed = false;
	boolean DOWNpressed = false;
	
	/******** main character **************/
	Rectangle body;
	Rectangle pistonBottom;
	Rectangle pistonTop;
	Rectangle pistonLeft;
	Rectangle pistonRight;
	
	double rightPistonTimer = 0;
	double leftPistonTimer = 0;
	double topPistonTimer = 0;
	double botPistonTimer = 0;
	
	/**** mirror match **/
	Rectangle mbody;
	Rectangle mpistonBottom;
	Rectangle mpistonTop;
	Rectangle mpistonLeft;
	Rectangle mpistonRight;
	
	double mrightPistonTimer = 0;
	double mleftPistonTimer = 0;
	double mtopPistonTimer = 0;
	double mbotPistonTimer = 0;
	/**/
	
	// arena
	Rectangle arenaTop;
	Rectangle arenaBot;
	Rectangle arenaLeft;
	Rectangle arenaRight;
	
	DoubleProperty mboxVelocityX = new SimpleDoubleProperty();
	DoubleProperty mboxVelocityY = new SimpleDoubleProperty();
	
	final double coolDownTime = 0.9;
	double timeCanBeOut = 0.4;
	
	// used for animationTimer
	double boxSpeedX = 200; // pixels per second
	double boxSpeedY = 200; // pixels per second
	double minX = 0;
	double maxX = 800; // max move speed for x
	double minY = -600;
	double maxY = 0; // max move speed for y
	DoubleProperty boxVelocityX = new SimpleDoubleProperty();
	DoubleProperty boxVelocityY = new SimpleDoubleProperty();
	LongProperty lastUpdateTime = new SimpleLongProperty();
	
	AnimationTimer gameAnimation;
	
	double deltaX = 0.0;
	double deltaY = 0.0;
	// mm
	double mdeltaX = 0.0;
	double mdeltaY = 0.0;
	
	// phong materials
	PhongMaterial lightBlue;
	PhongMaterial red;
	PhongMaterial arenaFloorMat;
	PhongMaterial arenaWallMat;

			
	// colors
		Color b;
		Color w;
		Color g;
		Color r;
		Color bl;
		
		javafx.scene.paint.Color blue;
	    javafx.scene.paint.Color gray;
	    javafx.scene.paint.Color sRed;
	    javafx.scene.paint.Color black;
	    javafx.scene.paint.Color white;
	    
	    //groups
	 	Group background;
	 	Group midground;
	 	Group enemies;
	 	
	 	//
	 	/*Transformations*/
		Translate pistonHoriRight;
		Translate pistonHoriLeft;
		Translate pistonVertiUp;
		Translate pistonVertiDown;
		Translate zero;
		
		// temporary mirror match transformations
		Translate mpistonHoriRight;
		Translate mpistonHoriLeft;
		Translate mpistonVertiUp;
		Translate mpistonVertiDown;
		
	@Override
	public void start(Stage primaryStage) {
		try {	
			window = primaryStage;
			window.setTitle("Box Game");
			
			game = new Pane();

			createColors();
			createMaterials();
			debugInit();
			
			createMainMenu();
			
			initializeUI();
			initializeMainCharacter();
			initializeMirrorMatch();
			initializeArena();
			
			
			background = new Group();
			midground = new Group(arenaTop,arenaBot,arenaLeft, arenaRight);
			enemies = new Group(mboxMain);
			
			initializeTransformations();
			/************Menu Layout**********************/
			
			startMenu = new VBox(10);
			mainCharacterMaker mc = new mainCharacterMaker();
			Button make_box = mc.display(game, 10, 10);
		//	make_box.setLayoutX(500);
		//	make_box.setLayoutY(500);
			
			startMenu.getChildren().addAll(make_box);
			startMenu.setId("start menu");
			
			/*************Animation Timer*******************************/
			startAnimationTimer();
			
			gameAnimation.start();
		
			/**********Project Layout**********/
			initializeProjectLayout();
			
			// scene setup
			scene = new Scene(root,windowWidth,windowHeight);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			window.setScene(scene);
			window.show();
		
			/**********Button Event Handler*********** (made separate file)
			make_box.setOnAction(new EventHandler<ActionEvent>()
					{
						public void handle(ActionEvent e)
						{
							boxMaker<Polygon> boxTemp = new boxMaker<Polygon>();
							boxTemp.setAttributes("boxBody",100.0,200.0,200.0,300.0, blue);
							Polygon boxReal = (Polygon) boxTemp.createBox("boxBody");
							//boxReal.relocate(x, y);
							root.getChildren().addAll(boxReal);
						}
					});
			
			
			/************Key Press Event Handlers******************/
			keyPressHandler();
			// main character key presses
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void createMainMenu() {
		int menuX = 200;
		menuTitle = new Text(200,400,"Box Game");
		menuTitle.setFont(new Font(80));
		menuTitle.relocate(50, 50);
		menuBackground = new Rectangle(1500,1500, gray);
		menuBackground.relocate(0, 0);
		
		localSingleplayerText = new Text(50, 250, "Local Singleplayer");
		localSingleplayerText.setFont(new Font(38));
		localSingleplayerText.relocate(menuX, 398);
		localSingleplayerRectangle = new Rectangle(320,50,blue);
		localSingleplayerRectangle.relocate(menuX, 400);
		
		localMultiplayerText = new Text(50, 150, "Local Multiplayer");
		localMultiplayerText.relocate(menuX, 525);
		localMultiplayerText.setFont(new Font(38));
		localMultiplayerRectangle = new Rectangle(320,50,blue);
		localMultiplayerRectangle.relocate(menuX, 500);
		
		quitText = new Text(20, 50, "Quit");
		quitText.relocate(menuX, 675);
		quitText.setFont(new Font(38));
		quitRectangle = new Rectangle(80,50,sRed);
		quitRectangle.relocate(menuX, 650);
		 
		menuGroup = new Group(
				menuBackground, menuTitle,
				localSingleplayerRectangle, localSingleplayerText,
				localMultiplayerRectangle, localMultiplayerText,
				quitRectangle, quitText
				);
	}
	
	public void startAnimationTimer() {
		gameAnimation = new AnimationTimer() {
			@Override
			public void handle(long timestamp) {
				if(lastUpdateTime.get() > 0) {
					final double elapsedSeconds = (timestamp - lastUpdateTime.get()) / 1_000_000_000.0;
					// current movement
					final double oldX = boxMain.getTranslateX();
					final double oldY = boxMain.getTranslateY();
					final double moldX = mboxMain.getTranslateX();
					final double moldY = mboxMain.getTranslateY();
					
					mainCharacterBodyCollision();
					enemyBodyCollision();
					
					
					if(enemyAI) {
						
						if(testIntersection(body, mbody))
							enemyAIAttackLogic();
					}
					
					if(gameplay) {
						pistonCollision();
						pistonTimerUpdater(elapsedSeconds);
						pistonResetter();
					}
					
					stuffMovement(elapsedSeconds, oldX, oldY, moldX, moldY);
					
					if(gameSetup)
						if(startCountDown > -1.1) {
							startCountDown = countDownDisplay(startCountDown, elapsedSeconds);
							if(startCountDown < 0.0)
								gameplay = true;
						} else {
							gameSetup = false;
						}
					
					
					// resets isReset tag
					isReset = 1;
					
				}
				lastUpdateTime.set(timestamp);
				
			}
		};
	}
	
	public void stuffMovement(double elapsedSeconds, double oldX, double oldY, double moldX, double moldY) {
		// x movement
		final double deltaX = elapsedSeconds * boxVelocityX.get() * isReset;
		final double newX = (oldX + deltaX) * isReset;//Math.max(minX,  Math.min(maxX, oldX + deltaX));
		// y movement
		final double deltaY = elapsedSeconds * boxVelocityY.get() * isReset;
		final double newY = (oldY + deltaY) * isReset;//Math.max(minY,  Math.min(maxY, oldY + deltaY));
		
		// mm x movement
		final double mdeltaX = elapsedSeconds * mboxVelocityX.get() * isReset;
		final double mnewX = (moldX + mdeltaX) * isReset;//Math.max(minX,  Math.min(maxX, moldX + mdeltaX));
		
		// mm y movement
		final double mdeltaY = elapsedSeconds * mboxVelocityY.get() * isReset;
		final double mnewY = (moldY + mdeltaY) * isReset;//Math.max(minY,  Math.min(maxY, moldY + mdeltaY));
		
		// setting movement
		boxMain.setTranslateX(newX);
		boxMain.setTranslateY(newY);
		
		// mm movement
		mboxMain.setTranslateX(mnewX);
		mboxMain.setTranslateY(mnewY);
	}
	
	public void pistonResetter() {
		// retracts pistons after coolDownTime passes
		if( coolDownTime - rightPistonTimer > timeCanBeOut && Dpressed) {
			pistonRight.getTransforms().addAll(pistonHoriLeft);
			Dpressed = false;
		} else if( coolDownTime - leftPistonTimer > timeCanBeOut && Apressed) {
			pistonLeft.getTransforms().addAll(pistonHoriRight);
			Apressed = false;
		} else if( coolDownTime - topPistonTimer > timeCanBeOut && Wpressed) {
			pistonTop.getTransforms().addAll(pistonVertiDown);
			Wpressed = false;
		} else if( coolDownTime - botPistonTimer > timeCanBeOut && Spressed) {
			pistonBottom.getTransforms().addAll(pistonVertiUp);
			Spressed = false;
		}
		
		/* mirror match starts here*/
		if( coolDownTime - mrightPistonTimer > timeCanBeOut && RIGHTpressed) {
			mpistonRight.getTransforms().addAll(mpistonHoriLeft);
			RIGHTpressed = false;
		}
		if( coolDownTime - mleftPistonTimer > timeCanBeOut && LEFTpressed) {
			mpistonLeft.getTransforms().addAll(mpistonHoriRight);
			LEFTpressed = false;
		}
		if( coolDownTime - mtopPistonTimer > timeCanBeOut && UPpressed) {
			mpistonTop.getTransforms().addAll(mpistonVertiDown);
			UPpressed = false;
		}
		if( coolDownTime - mbotPistonTimer > timeCanBeOut && DOWNpressed) {
			mpistonBottom.getTransforms().addAll(mpistonVertiUp);
			DOWNpressed = false;
		}
	}
	
	public void pistonTimerUpdater(double elapsedSeconds) {
		// keeps track on how long since last time piston was activated
		if(rightPistonTimer - elapsedSeconds >= 0) {
			rightPistonTimer = rightPistonTimer - elapsedSeconds;	
		} else {
			rightPistonTimer = 0;
		}
		if(leftPistonTimer - elapsedSeconds >= 0) {
			leftPistonTimer = leftPistonTimer - elapsedSeconds;
		} else {
			leftPistonTimer = 0;
		}
		if(topPistonTimer - elapsedSeconds >= 0) {
			topPistonTimer = topPistonTimer - elapsedSeconds;
		} else {
			topPistonTimer = 0;
		}
		if(botPistonTimer - elapsedSeconds >= 0) {
			botPistonTimer = botPistonTimer - elapsedSeconds;
		} else {
			botPistonTimer = 0;
		}
		/* mirror match key starts here*/
		if(mrightPistonTimer - elapsedSeconds >= 0) {
			mrightPistonTimer = mrightPistonTimer - elapsedSeconds;	
		} else {
			mrightPistonTimer = 0;
		}
		if(mleftPistonTimer - elapsedSeconds >= 0) {
			mleftPistonTimer = mleftPistonTimer - elapsedSeconds;
		} else {
			mleftPistonTimer = 0;
		}
		if(mtopPistonTimer - elapsedSeconds >= 0) {
			mtopPistonTimer = mtopPistonTimer - elapsedSeconds;
		} else {
			mtopPistonTimer = 0;
		}
		if(mbotPistonTimer - elapsedSeconds >= 0) {
			mbotPistonTimer = mbotPistonTimer - elapsedSeconds;
		} else {
			mbotPistonTimer = 0;
		}
	}
	
	public void enemyAIAttackLogic() {
		if(enemyAI && !RIGHTpressed) {
			debugString = new String(debugString + "rightAI");
			mpistonRight.getTransforms().addAll(mpistonHoriRight);
			//mpistonBottom.getTransforms().addAll(mpistonVertiDown);
			RIGHTpressed = true;
			mrightPistonTimer = coolDownTime;
		}
		if(enemyAI && !LEFTpressed) {
			mpistonLeft.getTransforms().addAll(mpistonHoriLeft);
			LEFTpressed = true;
			mleftPistonTimer = coolDownTime;
		}
		if(enemyAI && !UPpressed) {
			mpistonTop.getTransforms().addAll(mpistonVertiUp);
			UPpressed = true;
			mtopPistonTimer = coolDownTime;
		}
		if(enemyAI && !DOWNpressed) {
			mpistonBottom.getTransforms().addAll(mpistonVertiDown);
			DOWNpressed = true;
			mbotPistonTimer = coolDownTime;
		}
	}
	
	public double countDownDisplay(double startCountDown, double elapsedSeconds) {
		if(startCountDown < 0.0 && startCountDown > -1.0) {
			countDown.setText("GO!");
		} else if(startCountDown < -1.01){
			 countDown.setVisible(false);
		} else if(startCountDown - elapsedSeconds < 0) {
			countDown.setText("READY: 0.0");
		}   else {
			countDown.setText("READY: " + Double.parseDouble(Double.toString(startCountDown).substring(0, 3)));
		}
		return startCountDown - elapsedSeconds;
	}
	
	public void pistonCollision() {
		// detecting piston collision
		if(Dpressed || Apressed || Wpressed || Spressed) {
			if(
					testIntersection(pistonRight,mbody) ||
					testIntersection(pistonLeft,mbody) ||
					testIntersection(pistonTop,mbody) ||
					testIntersection(pistonBottom,mbody)
					) {
				// kill enemy
				
				roundWin(1);
			} else {
				if(Dpressed && testIntersection(pistonRight,arenaRight)) {
					boxVelocityX.set( - boxSpeedX);
				}
				if (Apressed && testIntersection(pistonLeft,arenaLeft)) {
					boxVelocityX.set(+ boxSpeedX);
				}
				if (Wpressed && testIntersection(pistonTop,arenaTop)) {
					boxVelocityY.set(+ boxSpeedY);
				}
				if (Spressed && testIntersection(pistonBottom,arenaBot)) {
					boxVelocityY.set(- boxSpeedY);
				}
			}
		}
			//detecting piston collision for mirror match
		if(RIGHTpressed || LEFTpressed || UPpressed || DOWNpressed) {
			if(
					testIntersection(mpistonRight,body) ||
					testIntersection(mpistonLeft,body) ||
					testIntersection(mpistonTop,body) ||
					testIntersection(mpistonBottom,body)
					) {
				// kill enemy
				
				roundWin(2);
			} else {
				if(RIGHTpressed && testIntersection(mpistonRight,arenaRight)) {
					mboxVelocityX.set( - boxSpeedX);
				}
				if (LEFTpressed && testIntersection(mpistonLeft,arenaLeft)) {
					mboxVelocityX.set(+ boxSpeedX);
				}
				if (UPpressed && testIntersection(mpistonTop,arenaTop)) {
					mboxVelocityY.set(+ boxSpeedY);
				}
				if (DOWNpressed && testIntersection(mpistonBottom,arenaBot)) {
					mboxVelocityY.set(- boxSpeedY);
				}
			}
		}
	}
	
	
	
	public void mainCharacterBodyCollision() {
		// detecting body collision
		if(testIntersection(body,arenaRight)) {
			boxVelocityX.set(0);
			//double Xchange = putOutside(body,arenaRight,"left","return");
		}
		if (testIntersection(body,arenaLeft)) {
			boxVelocityX.set(0);
		}
		if (testIntersection(body,arenaTop)) {
			boxVelocityY.set(0);
		}
		if (testIntersection(body,arenaBot)) {
			boxVelocityY.set(0);
		}
		// detects body collision
		if(testIntersection(body,mbody)) {
			int direction = intersectionDirection(mbody,body);
			moveDirection(boxMain,direction);
			
		}
	}
	
	
	public void enemyBodyCollision() {
		/*
			mboxVelocityX.set(0);
			//double Xchange = putOutside(mbody,arenaRight,"left","return");
			
			// figure out which direction the thing is
			
			
			if(enemyAI && !RIGHTpressed) {
				debugString = new String(debugString + "rightAI");
				mpistonRight.getTransforms().addAll(mpistonHoriRight);
				//mpistonBottom.getTransforms().addAll(mpistonVertiDown);
				RIGHTpressed = true;
				mrightPistonTimer = coolDownTime;
			}*/
			
			// detecting enemy body collision
			if(testIntersection(mbody,arenaRight)) {
				mboxVelocityX.set(0);
				//double Xchange = putOutside(mbody,arenaRight,"left","return");
				if(enemyAI && !RIGHTpressed) {
					debugString = new String(debugString + "rightAI");
					mpistonRight.getTransforms().addAll(mpistonHoriRight);
					//mpistonBottom.getTransforms().addAll(mpistonVertiDown);
					RIGHTpressed = true;
					mrightPistonTimer = coolDownTime;
				}
			}
			if (testIntersection(mbody,arenaLeft)) {
				mboxVelocityX.set(0);
				if(enemyAI && !LEFTpressed) {
					mpistonLeft.getTransforms().addAll(mpistonHoriLeft);
					LEFTpressed = true;
					mleftPistonTimer = coolDownTime;
				}
			}
			if (testIntersection(mbody,arenaTop)) {
				mboxVelocityY.set(0);
				if(enemyAI && !UPpressed) {
					mpistonTop.getTransforms().addAll(mpistonVertiUp);
					UPpressed = true;
					mtopPistonTimer = coolDownTime;
				}
			}
			if (testIntersection(mbody,arenaBot)) {
				mboxVelocityY.set(0);
				if(enemyAI && !DOWNpressed) {
					mpistonBottom.getTransforms().addAll(mpistonVertiDown);
					DOWNpressed = true;
					mbotPistonTimer = coolDownTime;
				}
			}
		
	}
	
	
	

	
	public void initializeTransformations() {
		/*************Transformations**********************/
		
		// transforming right piston
		pistonHoriRight = new Translate();
		pistonHoriRight.setX(32);
		pistonHoriRight.setY(0);
		pistonHoriRight.setZ(0);
		
		// transforming left piston
		pistonHoriLeft = new Translate();
		pistonHoriLeft.setX(-32);
		pistonHoriLeft.setY(0);
		pistonHoriLeft.setZ(0);	
		
		// transforming verti piston upwards
		pistonVertiUp = new Translate();
		pistonVertiUp.setX(0);
		pistonVertiUp.setY(-32);
		pistonVertiUp.setZ(0);
		
		// transforming verti piston downwards
		pistonVertiDown = new Translate();
		pistonVertiDown.setX(0);
		pistonVertiDown.setY(+32);
		pistonVertiDown.setZ(0);
		
		// zero translate
		zero = new Translate();
		zero.setX(0);
		zero.setY(0);
		zero.setZ(0);
	}
	
	public void activatePiston() {
		
	}
	
	public void transformPiston(Rectangle piston, String direction) {
		switch(direction) {
		case "Right" :
			transformPistonToRight(piston);
			break;
		case "Left" :
			transformPistonToLeft(piston);
			break;
		case "Up" :
			transformPistonToUp(piston);
			break;
		case "Down" :
			transformPistonToDown(piston);
			break;
		default :
			System.out.println(" Invalid piston transform direction");
		}
	}
	
	public void transformPistonToRight(Rectangle piston) {
		piston.getTransforms().addAll(pistonHoriRight);
	}
	
	public void transformPistonToLeft(Rectangle piston) {
		piston.getTransforms().addAll(pistonHoriLeft);
	}
	
	public void transformPistonToUp(Rectangle piston) {
		piston.getTransforms().addAll(pistonVertiUp);
	}
	
	public void transformPistonToDown(Rectangle piston) {
		piston.getTransforms().addAll(pistonVertiDown);
	}
	
	public void keyPressHandler() {
		scene.setOnKeyPressed(e -> {
			if(e.getCode() == KeyCode.D && Dpressed == false && rightPistonTimer == 0) {
				//transformPistonToRight(pistonRight);
				transformPiston(pistonRight, "Right");
				Dpressed = true;
				rightPistonTimer = coolDownTime;
			}
			if(e.getCode() == KeyCode.A && Apressed == false && leftPistonTimer == 0) {
				transformPiston(pistonLeft,"Left");
				Apressed = true;
				leftPistonTimer = coolDownTime;
			}
			if(e.getCode() == KeyCode.W && Wpressed == false && topPistonTimer == 0) {
				transformPiston(pistonTop,"Up");
				Wpressed = true;
				topPistonTimer = coolDownTime;
			}
			if(e.getCode() == KeyCode.S && Spressed == false && botPistonTimer == 0) {
				transformPiston(pistonBottom,"Down");
				Spressed = true;
				botPistonTimer = coolDownTime;
			}
			
			/* mirror match key presses (also second player I guess)*/
			if(!enemyAI ) { // disables arrow key press/releases if enemyAI is on
				if(e.getCode() == KeyCode.RIGHT && RIGHTpressed == false && mrightPistonTimer == 0) {
					transformPiston(mpistonRight,"Right");
					RIGHTpressed = true;
					mrightPistonTimer = coolDownTime;
				}
				if(e.getCode() == KeyCode.LEFT && LEFTpressed == false && mleftPistonTimer == 0) {
					transformPiston(mpistonLeft,"Left");
					LEFTpressed = true;
					mleftPistonTimer = coolDownTime;
				}
				if(e.getCode() == KeyCode.UP && UPpressed == false && mtopPistonTimer == 0) {
					transformPiston(mpistonTop,"Up");
					UPpressed = true;
					mtopPistonTimer = coolDownTime;
				}
				if(e.getCode() == KeyCode.DOWN && DOWNpressed == false && mbotPistonTimer == 0) {
					transformPiston(mpistonBottom,"Down");
					DOWNpressed = true;
					mbotPistonTimer = coolDownTime;
				}
			}
		});
	
		scene.setOnKeyReleased(e -> {
			if(e.getCode() == KeyCode.D && Dpressed == true) {
				pistonRight.getTransforms().addAll(pistonHoriLeft);
				Dpressed = false;
			}
			if(e.getCode() == KeyCode.A && Apressed == true) {
				pistonLeft.getTransforms().addAll(pistonHoriRight);
				Apressed = false;
			}
			if(e.getCode() == KeyCode.W && Wpressed == true ) {
				pistonTop.getTransforms().addAll(pistonVertiDown);
				Wpressed = false;
			}
			if(e.getCode() == KeyCode.S && Spressed == true ) {
				pistonBottom.getTransforms().addAll(pistonVertiUp);
				Spressed = false;
			}
			// mirror match key releases
			
				if(e.getCode() == KeyCode.RIGHT && RIGHTpressed == true) {
					mpistonRight.getTransforms().addAll(mpistonHoriLeft);
					RIGHTpressed = false;
				}
				if(e.getCode() == KeyCode.LEFT && LEFTpressed == true) {
					mpistonLeft.getTransforms().addAll(mpistonHoriRight);
					LEFTpressed = false;
				}
				if(e.getCode() == KeyCode.UP && UPpressed == true ) {
					mpistonTop.getTransforms().addAll(mpistonVertiDown);
					UPpressed = false;
				}
				if(e.getCode() == KeyCode.DOWN && DOWNpressed == true ) {
					mpistonBottom.getTransforms().addAll(mpistonVertiUp);
					DOWNpressed = false;
				}
		});
		
		
		
		localSingleplayerText.setOnMouseReleased(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				menuGroup.setVisible(false);
				menu = !menu;
				gameSetup = true;
				gameplay = false;
			}
		});
		
		localMultiplayerText.setOnMouseReleased(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				menuGroup.setVisible(false);
				menu = !menu;
				gameSetup = true;
				enemyAI = false;	
				gameplay = false;
			}		
		});
	}
	
	public void initializeUI() {
		/****** UI *********/
		
		UIBackground = new Rectangle(1100,50,black);
		UIBackground.relocate(0,0);
		
		mainCounter = new Text(10,50,"BLUE: 0");
		mainCounter.setFont(new Font(40));
		mainCounter.setFill(white);
		mainCounter.relocate(70, 0);
		
		enemyCounter = new Text (10,50,"RED: 0");
		enemyCounter.setFont(new Font(40));
		enemyCounter.setFill(white);
		enemyCounter.relocate(900, 0);
		
		information = new Text(10,50,"The first to three wins!");
		information.setFont(new Font(40));
		information.setFill(white);
		information.relocate(340, 0);
		
		countDown = new Text(10,10,"READY: " + startCountDown);
		countDown.setFont(new Font(40));
		countDown.setFill(sRed);
		countDown.relocate(450,400);
		
		UI = new Group(UIBackground,mainCounter,enemyCounter,information,countDown);
	}
	
	public void initializeMainCharacter() {
		body = new Rectangle(100,100,blue);
		body.relocate(120, 120);
		
		pistonBottom = new Rectangle (30,45,sRed);
		pistonBottom.relocate(155, 175);
		
		pistonTop = new Rectangle (30,45,sRed);
		pistonTop.relocate(155, 120);
		
		pistonLeft = new Rectangle (45,30,sRed);
		pistonLeft.relocate(120, 155);
		
		pistonRight = new Rectangle (45,30,sRed);
		pistonRight.relocate(175, 155);
		
		boxMain = new Group(pistonTop,pistonBottom,pistonLeft,pistonRight,body);
		boxMain.relocate(arenaX, arenaY);
	}
	
	public void initializeMirrorMatch() {
		
		mbody = new Rectangle(100,100,sRed);
		mbody.relocate(120, 120);
		
		mpistonBottom = new Rectangle (30,45,blue);
		mpistonBottom.relocate(155, 175);
		
		mpistonTop = new Rectangle (30,45,blue);
		mpistonTop.relocate(155, 120);
		
		mpistonLeft = new Rectangle (45,30,blue);
		mpistonLeft.relocate(120, 155);
		
		mpistonRight = new Rectangle (45,30,blue);
		mpistonRight.relocate(175, 155);
		
		mboxMain = new Group(mpistonTop,mpistonBottom,mpistonLeft,mpistonRight,mbody);
		mboxMain.relocate(arenaX+810, arenaY-610);
	}
	
	public void initializeArena() {
		/*
		Box arenaFloor = new Box (900, 700, 0);
		arenaFloor.relocate(arenaX, arenaX-100);
		arenaFloor.setMaterial(arenaFloorMat);*/
		/*Rectangle arenaFloor = new Rectangle (900, 700, black);
		arenaFloor.relocate(arenaX, arenaX-100);*/
		// arena walls
		
		arenaTop = new Rectangle (1100, 200, gray);
		arenaTop.relocate(arenaX-100, arenaY - 800);
		
		arenaBot = new Rectangle(1100, 200, gray);
		arenaBot.relocate(arenaX-100, arenaY + 100);
		
		arenaLeft = new Rectangle (200, 1100, gray);
		arenaLeft.relocate(arenaX-200, arenaY - 800);
		
		arenaRight = new Rectangle (200, 1100, gray);
		arenaRight.relocate(arenaX+900, arenaY - 800);
	}
	
	public void createColors() {
		b = Color.BLUE;
		w = Color.WHITE;
		g = Color.GRAY;
		r = Color.RED;
		bl = Color.BLACK;
		
		blue = javafx.scene.paint.Color.rgb(b.getRed(), b.getGreen(), b.getBlue(), b.getAlpha() / 255.0);
	    gray = javafx.scene.paint.Color.rgb(g.getRed(), g.getGreen(), g.getBlue(), g.getAlpha() / 255.0);
	    sRed = javafx.scene.paint.Color.rgb(r.getRed(), r.getGreen(), r.getBlue(), r.getAlpha() / 255.0);
	    black = javafx.scene.paint.Color.rgb(bl.getRed(), bl.getGreen(), bl.getBlue(), bl.getAlpha() / 255.0);
	    white = javafx.scene.paint.Color.rgb(w.getRed(), w.getGreen(), w.getBlue(), w.getAlpha() / 255.0);
	    
	    lightBlue = new PhongMaterial();
		lightBlue.setDiffuseColor(blue);
		lightBlue.setSpecularColor(gray);
		
		red = new PhongMaterial();
		red.setDiffuseColor(sRed);
		red.setSpecularColor(sRed);
	
	}
	
	public void createMaterials() {
		arenaFloorMat = new PhongMaterial();
		arenaFloorMat.setDiffuseColor(black);
		arenaFloorMat.setSpecularColor(white);
		
		arenaWallMat = new PhongMaterial();
		arenaWallMat.setDiffuseColor(gray);
		arenaWallMat.setSpecularColor(white);
	}
	
	// only works for shape objects
	public static boolean testIntersection(Shape shapeA, Shape shapeB) {
		
		Shape intersect = Shape.intersect(shapeA, shapeB);
		if(intersect.getBoundsInLocal().getWidth() != -1)
		{
			return true;
		}
		return false;

	}
	

	
	public static Shape[] whatIntersects(Shape shape) {
		Shape[] shapeArray = {};
		
		return shapeArray;
	}
	
	
	
	public void moveDirection(Group group, int direction)
	{
		// 1 = top,2 = left,3  = right,4 = bot,
		//5 = top right corner,6 = top left corner,7 = bot right corner,8 = bot left corner
		if(direction == 1) {
			boxVelocityX.set( - boxSpeedX);
		} else if(direction == 2) {
			boxVelocityX.set(+ boxSpeedX);
		} else if(direction == 3) {
			boxVelocityY.set(+ boxSpeedY);
		} else if(direction == 4) {
			boxVelocityY.set(- boxSpeedY);
		} else if(direction == 5) {
			boxVelocityX.set(+ boxSpeedX);
			boxVelocityY.set(- boxSpeedY);
		} else if(direction == 6) {
			boxVelocityX.set(- boxSpeedX);
			boxVelocityY.set(- boxSpeedY);
		} else if(direction == 7) {
			boxVelocityX.set(+ boxSpeedX);
			boxVelocityY.set(+ boxSpeedY);
		} else if(direction == 8) {
			boxVelocityX.set(- boxSpeedX);
			boxVelocityY.set(+ boxSpeedY);
		} else if(direction == 0){
		//	boxVelocityX.set(0);
		//	boxVelocityY.set(0);
		}
		debugString = new String(debugString + direction + " ");
		debugText.setText(debugString);
		return;
	}
	
	public void affectVelocity(int affectX, int affectY) {
		
	}

	
	public boolean rightCollision(Rectangle shapeA, Rectangle shapeB) {
		
		if(!testIntersection(shapeA,shapeB)) {
			return false;
		}
		
		Shape intersection = Shape.intersect(shapeA, shapeB);
		
		return false;
	}
	
	// checks intersection of two shapes, and which direction shapeA is being intersected by shapeB
	public static int intersectionDirection(Shape shapeA, Shape shapeB) {
		Shape intersection = Shape.intersect(shapeA, shapeB);
		
		//check that there is an intersection
		if(!testIntersection(shapeA,shapeB)) {
			return 0;
		}
		
		// intersection width/height/min/max
		double width = intersection.getBoundsInParent().getWidth();
		double height = intersection.getBoundsInParent().getHeight();
		double minX = intersection.getBoundsInParent().getMinX();
		double minY = intersection.getBoundsInParent().getMinY();
		double maxX = intersection.getBoundsInParent().getMaxX();
		double maxY = intersection.getBoundsInParent().getMaxY();
		// shape A max/minx
		double Awidth = shapeA.getBoundsInParent().getWidth();
		double Aheight = shapeA.getBoundsInParent().getHeight();
		double AminX = shapeA.getBoundsInParent().getMinX();
		double AminY = shapeA.getBoundsInParent().getMinY();
		double AmaxX = shapeA.getBoundsInParent().getMaxX();
		double AmaxY = shapeA.getBoundsInParent().getMaxY();
		// shape B max/minx
		double Bwidth = shapeB.getBoundsInParent().getWidth();
		double Bheight = shapeB.getBoundsInParent().getHeight();
		double BminX = shapeB.getBoundsInParent().getMinX();
		double BminY = shapeB.getBoundsInParent().getMinY();
		double BmaxX = shapeB.getBoundsInParent().getMaxX();
		double BmaxY = shapeB.getBoundsInParent().getMaxY();
		
		// first compare the relative deltaV of shapeB in regards to shapeA
		// i.e.: what velocity X&Y is shapeB moving towards shapeA?
		// use translate property to see what direction the shapes moved recently
		double deltaX;// = shapeA.getTranslateX() + shapeB.getTranslateX();
		double deltaY;// = shapeA.getTranslateX() + shapeB.getTranslateX();
		
		if(posOrNeg(shapeA.getTranslateX()) == posOrNeg(shapeB.getTranslateX())) {
			deltaX = shapeA.getTranslateX() - shapeB.getTranslateX();
		} else if (posOrNeg(shapeA.getTranslateX()) != posOrNeg(shapeB.getTranslateX())) {
			deltaX = shapeA.getTranslateX() + shapeB.getTranslateX();
		} else {
			deltaX = shapeA.getTranslateX() + shapeB.getTranslateX();
		}
		
		if(posOrNeg(shapeA.getTranslateY()) == posOrNeg(shapeB.getTranslateY())) {
			deltaY = shapeA.getTranslateY() - shapeB.getTranslateY();
		} else if (posOrNeg(shapeA.getTranslateY()) != posOrNeg(shapeB.getTranslateY())) {
			deltaY = shapeA.getTranslateY() + shapeB.getTranslateY();
		} else {
			deltaY = shapeA.getTranslateY() + shapeB.getTranslateY();
		}
		
		
		
		// if below is true, can only be from top, left, or right
		if(minY == AminY) {
			
			// can only be top or right
			if(maxX == AmaxX) {
				// intersects from right if true
				if(Math.abs(deltaX) > Math.abs(deltaY)) {
					return 3; // right
				} 
				// intersects from top if true
				else if(Math.abs(deltaY) > Math.abs(deltaX)) {
					return 1;
				} else if(Math.abs(deltaX) == Math.abs(deltaY)) {
					// it's an exact diagonal path intersection
					// if width is wider, it's from top
					if(width > height) {
						return 1; // top
					} else if (height > width) {
						return 3; // right
					} else {
						// means that the two shapes hit exactly on the diagonal, 
						//from exactly relative diagonal paths
						return 5; // top right corner
					}
				}
			}
			// can only be top or left
			if(minX == AminX) {
				// intersects from left if true
				if(Math.abs(deltaX) > Math.abs(deltaY)) {
					return 2; //left
				} 
				// intersects from top if true
				else if(Math.abs(deltaY) > Math.abs(deltaX)) {
					return 1;
				} else if(Math.abs(deltaX) == Math.abs(deltaY)) {
					// it's an exact diagonal path intersection
					// if width is wider, it's from top
					if(width > height) {
						return 1; // top
					} else if (height > width) {
						return 2; // left
					} else {
						// means that the two shapes hit exactly on the diagonal, 
						//from exactly relative diagonal paths
						return 6; // top left corner
					}
				}
			}
		}
		// if below is true, can only be from bot, left, or right
		if(maxY == AmaxY) {
			
			// can only be bot or right
			if(maxX == AmaxX) {
				// intersects from right if true
				if(Math.abs(deltaX) > Math.abs(deltaY)) {
					return 3; // right
				} 
				// intersects from bot if true
				else if(Math.abs(deltaY) > Math.abs(deltaX)) {
					return 4;
				} else if(Math.abs(deltaX) == Math.abs(deltaY)) {
					// it's an exact diagonal path intersection
					// if width is wider, it's from bot
					if(width > height) {
						return 4; // bot
					} else if (height > width) {
						return 3; // right
					} else {
						// means that the two shapes hit exactly on the diagonal, 
						//from exactly relative diagonal paths
						return 7; // bot right corner
					}
				}
			}
			// can only be bot or left
			if(minX == AminX) {
				// intersects from left if true
				if(Math.abs(deltaX) > Math.abs(deltaY)) {
					return 2; //left
				} 
				// intersects from bot if true
				else if(Math.abs(deltaY) > Math.abs(deltaX)) {
					return 4;// bot
				} else if(Math.abs(deltaX) == Math.abs(deltaY)) {
					// it's an exact diagonal path intersection
					// if width is wider, it's from top
					if(width > height) {
						return 1; // top
					} else if (height > width) {
						return 2; // left
					} else {
						// means that the two shapes hit exactly on the diagonal, 
						//from exactly relative diagonal paths
						return 8; // bot left corner
					}
				}
			}
		}
		
		
		return 0; // returns 0 if everything else fails / the shapes don't intersect
	}
	
	
	
	public static boolean posOrNeg(double num) {
		if(num >= 0) {
			return true;
		} else {
			return false;
		}

	}
	
	public void roundWin(int player) {
		
		
		if(player == 1) {
			// increments player 1 score by 1
			++mainBox;
			mainCounter.setText("BLUE: " + mainBox);
			if(mainBox == toWin){
				gameWin(player);
			} else {
				countDown.setVisible(true);
				countDown.setText("BLUE HITS");
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				resetBoxes();
			}
		} else if (player == 2) {
			// increments player 2 score by 1
			++enemyBox;
			enemyCounter.setText("RED: " + enemyBox);
			if(enemyBox == toWin){
				gameWin(player);
			} else {
				countDown.setVisible(true);
				countDown.setText("RED HITS");
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				resetBoxes();
			}
		} else {
			resetBoxes();
		}
		countDown.setVisible(false);
		startCountDown = countDownTime;
		resetUI();
		
		return;
	}

	public void gameWin(int player) {
		
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		resetUI();

		// reset back to normal
		mainBox = 0;
		enemyBox = 0;
		mainCounter.setText("BLUE: " + mainBox);
		enemyCounter.setText("RED: " + enemyBox);
		
		resetBoxes();
		return;
	}
	
	public void resetUI() {
		startCountDown = countDownTime;
		countDown.setVisible(true);
		gameplay = false;
		gameSetup = true;
		return;
	}
	
	// sets things to the normal setup
	public void resetBoxes() {
		
		// stops movement of both boxes
		boxVelocityX.set(0);
		boxVelocityY.set(0);
		mboxVelocityX.set(0);
		mboxVelocityY.set(0);
		isReset = 0;
		// relocation of characters
		boxMain.relocate(arenaX, arenaY);
		mboxMain.relocate(arenaX+810, arenaY-610);
		return;
	}
	

	
	// relocates shapeA to just outside of shapeB, relocate direction based upon String direction
	public static double putOutside(Shape shapeA, Shape shapeB, String direction, String ret)
	{
		// buffer between shapes after relocation
		double buffer = 1;
		
		// gets info on where the shapes intersected
		Shape intersection = Shape.intersect(shapeA, shapeB);
		
		// direction should only ever say left, right, up, or down <- direction ShapeA should be moved
		if(direction == "left")
		{
			// gets size of intersection by X
			double minX = intersection.getBoundsInParent().getMinX();
			double maxX = intersection.getBoundsInParent().getMaxX();
			// gets coords of shapeA needed for relocation
			double minXA = intersection.getBoundsInParent().getMinX();
			double maxYA = intersection.getBoundsInParent().getMaxY();
			
			// later might need to make this value absolute just in case
			double relocateDistanceX = (maxX - minX);
			if(ret == "return"){
				return minXA - relocateDistanceX;
			} else {
				// relocates shape to the right
				shapeA.relocate(minXA - relocateDistanceX, maxYA);
				return 0;
			}
		}else if(direction == "right")
		{
			// gets size of intersection by X
			double minX = intersection.getBoundsInParent().getMinX();
			double maxX = intersection.getBoundsInParent().getMaxX();
			// gets coords of shapeA needed for relocation
			double minXA = intersection.getBoundsInParent().getMinX();
			double maxYA = intersection.getBoundsInParent().getMaxY();
			
			// later might need to make this value absolute just in case
			double relocateDistanceX = (maxX - minX);
			if(ret == "return"){
				return minXA + relocateDistanceX;
			} else {
				// relocates shape to the right
				shapeA.relocate(minXA + relocateDistanceX, maxYA);
				return 0;
			}
		}
			
		double minY = intersection.getBoundsInParent().getMinY();
		double maxY = intersection.getBoundsInParent().getMaxY();
		
		return 0;
	}
	
	public void debugInit() {
		debugText = new Text(10,50,"Debug: ");
		debugText.setFont(new Font(20));
		debugText.setFill(white);
		debugText.relocate(10, 100);
		
		debug = new Group(debugText);
	}
	
	public void initializeProjectLayout() {
		root = new BorderPane();
		root.setTop(startMenu);
		root.setCenter(game);
		root.setStyle("-fx-background-color: tan;");
		root.getChildren().addAll(background,enemies,boxMain,midground,UI,menuGroup);
	}

	
	public static void main(String[] args) {
		launch(args);
	}
	
	
	
}
