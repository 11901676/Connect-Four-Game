package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;


public class Main extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
	
	
	@Override
	   public void init() throws Exception {
	       super.init();
	       System.out.println("Initialization has been processed....  ");
	   }
	
	
	private Controller controller;
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		System.out.println("Application has started functioning.....");
		FXMLLoader loader = new FXMLLoader(getClass().getResource("game_layout.fxml"));
		GridPane rootNode = loader.load();
		
		
		//Connecting FXML to java
		controller = loader.getController();
		
		
		//Calling Play Ground from controller
		controller.createPlayground();
		
		
		//Since we have to make our menu in the first children pane of Grid-pane so we will pass index 0
		Pane menuPane = (Pane) rootNode.getChildren().get(0);
				
		MenuBar menuBar = createMenu();
		menuPane.getChildren().add(menuBar);
		
		//Setting MenuBar to whole Grid-pane width
		menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
		
		Scene scene = new Scene(rootNode);
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("Connect Four Game");
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	
	private MenuBar createMenu() {
		
		//File Menu
		Menu fileMenu = new Menu("Controls");
		
		MenuItem newMenuItem = new MenuItem("New Game");
		newMenuItem.setOnAction(event->controller.resetGame());
		SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
		
		MenuItem resetMenuItem = new MenuItem("Reset Game");
		resetMenuItem.setOnAction(event->controller.resetGame());
		SeparatorMenuItem separatorMenuItem2 = new SeparatorMenuItem();
		
		MenuItem exitItem = new MenuItem("Exit Game");
		
		//On-click event on Exit Game
		exitItem.setOnAction(evnet -> {
			System.out.println("Application has stopped functioning........");
			Platform.exit();
			System.exit(0);
		});
		
		//Adding all menuItems to fileMenu
		fileMenu.getItems().addAll(newMenuItem,separatorMenuItem,resetMenuItem,separatorMenuItem2,exitItem);
			
		
		
		//Help Menu
		Menu helpMenu = new Menu("Help");
		
		MenuItem gameMenuItem = new MenuItem("About Game");
		
		//Adding onclick event on About  Game
		gameMenuItem.setOnAction(event->gameInfo());
		
		SeparatorMenuItem separatorMenuItem3 = new SeparatorMenuItem();
		
		MenuItem meMenuItem = new MenuItem("About Me");
		//Adding onclick event on About Me
		meMenuItem.setOnAction(event->developerInfo());
		
		
		//Adding all menuItems to helpMenu
		helpMenu.getItems().addAll(gameMenuItem,separatorMenuItem3,meMenuItem);
	
		
		
		
		//MenuBar
		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().addAll(fileMenu,helpMenu);
		
		return menuBar;
	}
	

	private Object developerInfo() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Developer Information");
		alert.setHeaderText("Dipesh Tiwari");
		alert.setContentText("I am learning JavaFX and with the help of javafx i am creating this game");
		alert.show();
		return null;
	}


	private Object gameInfo() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("About Connect Four Game");
		alert.setHeaderText("How to play ?");
		alert.setContentText("Connect Four is a two-player connection game in which the players "
				+ "first choose a color and then take turns dropping colored discs from the top into a "
				+ "seven-column, six-row vertically suspended grid. The pieces fall straight down, "
				+ "occupying the next available space within the column. The objective of the game is to be "
				+ "the first to form a horizontal, vertical, or diagonal line of four of one's own discs."
				+ " Connect Four is a solved game."
				+ " The first player can always win by playing the right moves.");
		alert.show();
		return null;
	}


	@Override
	   public void stop() throws Exception {
	       super.stop();
	       System.out.println("Application has finished it's execution......        ");
	   }
	
}
