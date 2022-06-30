package application;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

public class Controller implements Initializable{
	//Defining rules for game
	private static final int COLUMNS = 7;
	private static final int ROWS = 6;
	private static final int CIRCLE_DIAMETER = 80;
	private static final String disc_One_Colour = "#24303E";
	private static final String disc_Two_Colour = "4CAA88";
	
	
	
	//Defining players
	private static String Player_One;
	private static String Player_Two;
	
	
	//Defining condition for players turn
	private boolean isPlayerOneTurn = true;
	
	private Disc insertedDiscArray[][] = new Disc[ROWS][COLUMNS];
	
	
	// To avoid same color disc is being added by the user by clicking multiple time fastly
	private boolean isAllowedToInsert = true;
	
	//Defining controls and containers
	@FXML
	public GridPane rootGridPane;
	
	@FXML
	public Pane insertedDiscPane;
	
	@FXML
	public Label entryMessageLabel;
	
	@FXML
	public Label playerNameLabel;
	
	@FXML
	public Label turnLabel;
	
	@FXML
	public Pane insertNamePane;
	
	@FXML
	public TextField playerOneName;
	
	@FXML
	public TextField playerTwoName;
	
	@FXML
	public Button setNameButton;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		
	}

	
	//Creating game UI
	public void createPlayground()
	{
		entryMessageLabel.setVisible(true);
		playerNameLabel.setVisible(false);
		turnLabel.setVisible(false);
		
		
		setNameButton.setOnAction(event->{
		//Checking if user has entered the player name or not
				checkNameConvention();
				updatePlayerName();
		});
		
		
		//Overlapping our grinish pane with white rectangular space later we will cut circles from it so that our holes will get created
		Shape rectangleWithHole = new Rectangle((COLUMNS+1)*CIRCLE_DIAMETER, (ROWS+1)*CIRCLE_DIAMETER);
		
		
		
		//Creating holes will be just like a 2D array of holes
				for(int row = 0; row < ROWS ; row++)
				{
					for(int columns = 0; columns < COLUMNS ; columns++)
					{
						//Creating circles
						Circle circle = new Circle();
						circle.setRadius(CIRCLE_DIAMETER/2);
						circle.setCenterX(CIRCLE_DIAMETER/2);
						circle.setCenterY(CIRCLE_DIAMETER/2);
						circle.setSmooth(true);
						
						//Changing the position for new hole for each iteration
						circle.setTranslateX(columns * (CIRCLE_DIAMETER + 5) + CIRCLE_DIAMETER/4);
						circle.setTranslateY(row * (CIRCLE_DIAMETER + 5 ) + CIRCLE_DIAMETER/4);
						
						
						rectangleWithHole = Shape.subtract(rectangleWithHole, circle);
					}
				}	
					//Changing rectangle color to white
					rectangleWithHole.setFill(Color.WHITE);
					
					
		
		//Adding rectangle in the second pane at index 0,1
		rootGridPane.add(rectangleWithHole, 0, 1);
		/* After it goto .fxml file and remove the row and column constraints because due to these constraints our second pane will
		 appear distorted and after  removing constraints set first row(Pane 1 on which menubar is present) minheight as 10 so that
		 menubar will get appear and after it define createPlayground Method in controller and call it in main
		  */
		
		
		List<Rectangle> rectangleList = hoverRectangles();
		//Adding rectangles to grid pane
		for(Rectangle rectangle : rectangleList)
		{
			rootGridPane.add(rectangle, 0, 1);
		}
		
		
	}
	
	
	private void updatePlayerName() {
		
		
		entryMessageLabel.setVisible(false);
		String input1 = playerOneName.getText();
		Player_One = input1;
		
		String input2 = playerTwoName.getText();
		Player_Two = input2;
		
		
		playerNameLabel.setText(isPlayerOneTurn ? Player_One: Player_Two);
		playerNameLabel.setVisible(true);
		turnLabel.setVisible(true);
	}


	private void checkNameConvention() {
			
		//Checking if user has entered the player name or not
		if((Player_One == null) && (Player_Two == null))
		{
			inputField();
		}
		
		else if((Player_One == null) && (Player_Two != null))
		{
			inputField();
		}
		
		
		else if((Player_Two == null) && (Player_One != null))
		{
			inputField();
		}
		
		
		else if((Player_One != null) && (Player_Two != null) && (Player_One == Player_Two))
		{
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Naming Convention");
			alert.setHeaderText("Check Naming Convention");
			alert.setContentText("Name of Player One and Player Two cannot be same or NULL ....");
			alert.show();
				
		}
		
		
	}


	// Creating rectangles for hover effect and since we will create a list of rectangles here so we will use List here
	private List<Rectangle> hoverRectangles()
	{
		
		List<Rectangle> rectangleList = new ArrayList<>();
		
		for(int col = 0 ; col < COLUMNS ; col++)
		{
			Rectangle rectangle = new Rectangle(CIRCLE_DIAMETER, (ROWS+1)*CIRCLE_DIAMETER);
			rectangle.setFill(Color.TRANSPARENT);
			
			
			// Hover Event
			rectangle.setOnMouseEntered(event->rectangle.setFill(Color.valueOf("#eeeeee26")));
			rectangle.setOnMouseExited(event->rectangle.setFill(Color.TRANSPARENT));
			
			
			// On-click event on holes
			final int column = col;
			rectangle.setOnMouseClicked(event->
			{
				
				if(isAllowedToInsert)
				{
					isAllowedToInsert = false; /*After clicking on mouse when disc is being 
					dropped meanwhile stop the user to enter again by making isAllowedToInsert
					 flag false
					 */
										
					insertDisc(new Disc(isPlayerOneTurn), column);
				}
				
			});
			
			
			
			
			rectangle.setTranslateX(col * (CIRCLE_DIAMETER + 5) + CIRCLE_DIAMETER/4);
			
			rectangleList.add(rectangle);
			
			
		}
		
		
		
		return rectangleList;
	}


	private void insertDisc(Disc disc, int column) {
		
		
		int row = ROWS - 1;  //Since row is index so we have mentioned it as ROWS - 1
		
		// Checking that hole is empty or not
		while(row >= 0)
		{
			if(getDiscIfPresent(row, column) == null)
			{
				break;
			}
			else
			{
				row--;
			}
		}
		
		
		// Defining condition if all the holes are fully filled
		if(row < 0)
		{
			return;    //return here depicts that if all the holes are full then do nothing
		}
		
		
						
		//Checking Name Convention
		checkNameConvention();
		
		
		//Inserting disc in pane
		insertedDiscArray[row][column] = disc; // Structural change : For developers
		insertedDiscPane.getChildren().add(disc);  // Visual change : For players
		
		
		// Adding disc at our desired position
		disc.setTranslateX(column * (CIRCLE_DIAMETER + 5) + CIRCLE_DIAMETER/4  + CIRCLE_DIAMETER/2);
		
		
		//Creating animation for discs
		TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.3), disc);
		translateTransition.setToY(row * (CIRCLE_DIAMETER + 5) + CIRCLE_DIAMETER/4  + CIRCLE_DIAMETER/2);
		
		
		// After one turn of animation finishes means after player one has completed his turn then we need to toggle between the players
		final int currentDiscRow = row;
		translateTransition.setOnFinished(event->{
			
			
			// When a player has entered the disc then allow the second player to insert the disc
			isAllowedToInsert = true;
			
			
			//Checking that whether anyone won the game or not if won then end the game if not then give the chance to second player
			if(gameEnded(currentDiscRow, column)){
				
				gameOver();
				return; // when game ends one then terminate then after that nothing should happen
			}
			
			isPlayerOneTurn = !isPlayerOneTurn;
			
			//Changing label on toggling the players
			playerNameLabel.setText(isPlayerOneTurn ? Player_One: Player_Two);
		});
		
		
		// Playing Animation
		
		translateTransition.play();
	}



	public void inputField() {
		
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Player's Name");
			alert.setHeaderText("Mention Player's name");
			alert.setContentText("Mention your name in the given textfield first so that you will be much clear that at which point whose turn is coming !");
			ButtonType okButtonType = new ButtonType("Ok");
			ButtonType noButtonType = new ButtonType("No");
			alert.getButtonTypes().setAll(okButtonType, noButtonType);
			
			Optional<ButtonType> clickedButton = alert.showAndWait();
			if(clickedButton.isPresent() && clickedButton.get() == noButtonType)
			{
				System.out.println("Application has stopped it's execution......  ");
				Platform.exit();
				System.exit(0);
				
			}
			
	}


	private boolean gameEnded(int row, int column) {
		
		// Point2D is a class that stores the value of two constants ---> Point2D--> x,y

		// Vertical Combination of holes
		List<Point2D> verticalCombinationList = IntStream.rangeClosed(row - 3, row + 3 ) //Range of row values 0.....5
														 .mapToObj(r -> new Point2D(r, column))  // 0,3  1,3  2,3  3,3  4,3  5,3
														 .collect(Collectors.toList());
		
		
		// Horizontal Combination of holes
		List<Point2D> horizontalCombinationList = IntStream.rangeClosed(column - 3, column + 3 ) 
				 										.mapToObj(col -> new Point2D(row, col))
				 										.collect(Collectors.toList());
			
		
		// Forward diagonal combination
		Point2D startPoint1 = new Point2D( row - 3, column + 3);
		List<Point2D> forwardDiagonal = IntStream.rangeClosed(0, 6)
												.mapToObj(i-> startPoint1.add(i, -i))
												.collect(Collectors.toList());
		
		
		// Backward diagonal combination
		Point2D startPoint2 = new Point2D( row - 3, column - 3);
		List<Point2D> backwardDiagonal = IntStream.rangeClosed(0, 6)
												.mapToObj(i-> startPoint2.add(i, i))
												.collect(Collectors.toList());
												
		
		// Checking for combinations
		boolean isEnded = checkCombinations(verticalCombinationList) || checkCombinations(horizontalCombinationList)
							|| checkCombinations(forwardDiagonal) || checkCombinations(backwardDiagonal);
	    return isEnded;
	}

	

	
	private boolean checkCombinations(List<Point2D> points) {
		
		int chain = 0;
		for (Point2D point : points) {
			
			int rowIndexOfArray = (int) point.getX();
			int columnIndexOfArray = (int) point.getY();
			
			Disc disc = getDiscIfPresent(rowIndexOfArray, columnIndexOfArray);
			
			
			//if the last inserted disc belongs to the  current player
			if(disc != null && disc.isPlayerOneMove == isPlayerOneTurn)
			{
				chain++;
				if(chain == 4)
				{
					return true;
				}
				
			}
			else
			{
				chain = 0;
			}
			
		}
		
		return false;
	}


	
// For avoiding ArrayIndexOutOfBounds Exception
	private Disc getDiscIfPresent(int row, int column) {
		
		if(row >= ROWS || row < 0 || column >= COLUMNS || column < 0)
		{
			return null;
		}
		
		return insertedDiscArray[row][column];
	}  
	

	private void gameOver() {
		
		String winner = isPlayerOneTurn ? Player_One : Player_Two;
		
		
		// Declaring winner 
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Result of your game");
		alert.setHeaderText("The winner is : " + winner);
		alert.setContentText("Want to play again ? ");
		
		
		// Adding our desired buttons in dialogue box
		ButtonType yesButtonType = new ButtonType("Yes");
		ButtonType noButtonType = new ButtonType("No, Exit");
		
		alert.getButtonTypes().setAll(yesButtonType, noButtonType);
		
		
		// Platform later will ensure that we are exiting after ending of animation otherwise it will throw an exception
		Platform.runLater(()->
		{
			
			Optional<ButtonType> clickedButton = alert.showAndWait(); /*It will check which button 
			has been clicked and waits for the given instruction to be executed on clicking buttons */
			
			if(clickedButton.isPresent() && clickedButton.get() == yesButtonType)
			{
				Alert alert1 = new Alert(AlertType.INFORMATION);
				alert1.setTitle("Reset activity");
				alert1.setHeaderText("Activity before starting the game");
				alert1.setContentText("Before starting the game set the players name again and then start......");
				alert1.show();
				resetGame();
			}
			else
			{
				System.out.println("Application has stopped functioning........");
				Platform.exit();
				System.exit(0);
			}
			
		});
		
		
	}  




	public void resetGame() {
		
		// Clearing the pane
		insertedDiscPane.getChildren().clear();
		
		
		// Making the values of every hole again null 
		for (int row = 0; row < insertedDiscArray.length; row++) 
		{
			for (int col = 0; col < insertedDiscArray[row].length; col++) 
			{
				insertedDiscArray[row][col] = null;
			}
		}
		
		updatePlayerName();
		
		// Setting the default start of game by player one
		isPlayerOneTurn = true;
		playerNameLabel.setText(Player_One);
		
		
		// Creating a fresh playground
		createPlayground();
		
	}


	private static class Disc extends Circle
	{
		private final boolean isPlayerOneMove;
		public  Disc(boolean isPlayerOneMove)
		{
			this.isPlayerOneMove = isPlayerOneMove;
			setFill(isPlayerOneMove ? Color.valueOf(disc_One_Colour) : Color.valueOf(disc_Two_Colour));
            setRadius(CIRCLE_DIAMETER/2);
            setTranslateX(CIRCLE_DIAMETER/2);
            setTranslateY(CIRCLE_DIAMETER/2);
		}
	}
}


