package javaFX;

///PROGRAM NEARLY COMPLETED

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.util.ArrayList;

import core.*;

public class MainApp extends Application{
	
	private Stage stage;
	private Scene scene1;
	private Scene scene2;
	private Scene scene3;
	private MenuScreen menu;
	private GameScreenNoBet gameScreen1;
	private GameScreenBet gameScreen2;
	private ArrayList<String> playerNames = new ArrayList<>();
	private int numberRndsChps;
	private MediaPlayer mediaPlayer;
	
	public void start(Stage primaryStage) {
		
		this.stage = primaryStage;
		stage.setMinWidth(1200);
		stage.setMinHeight(715);
//		menu = new MenuScreen(this);
//		gameScreen = new GameView(this);
		
		
		showMenu();
		
		stage.show();
		
		//Start playing background music (created by Paul Daly)
		Media sound = new Media(getClass().getResource("/background_music_Paul_Daly.mp3").toExternalForm());
		mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Set to loop indefinitely)
		mediaPlayer.play();
		mediaPlayer.setVolume(0.2);
		
		//Setting up the scene
//		Scene scene = new Scene(mainPane, 1000, 700);
//		primaryStage.setTitle("Microwave"); // Set the stage title
//		primaryStage.setScene(scene); // Place the scene in the stage
//		primaryStage.show(); // Display the stage
	}
	
	public void showMenu() {
		menu = new MenuScreen(this);
		scene1 = new Scene(menu.getRoot(), 1200, 720);
		stage.setScene(scene1);
	}
	
	public void showGame() {
		gameScreen1 = new GameScreenNoBet(this);
		scene2 = new Scene(gameScreen1.getRoot(), 1200, 720);
		stage.setScene(scene2);
	}
	
	public void showGame2() {
		gameScreen2 = new GameScreenBet(this);
		scene3 = new Scene(gameScreen2.getRoot(), 1200, 720);
		stage.setScene(scene3);
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public MenuScreen getMenu() {
		return menu;
	}

	public void setMenu(MenuScreen menu) {
		this.menu = menu;
	}

	public ArrayList<String> getPlayerNames() {
		return playerNames;
	}

	public void setPlayerNames(ArrayList<String> playerNames) {
		this.playerNames = playerNames;
	}

	public Scene getScene1() {
		return scene1;
	}

	public void setScene1(Scene scene1) {
		this.scene1 = scene1;
	}

	public Scene getScene2() {
		return scene2;
	}

	public void setScene2(Scene scene2) {
		this.scene2 = scene2;
	}

	public Scene getScene3() {
		return scene3;
	}

	public void setScene3(Scene scene3) {
		this.scene3 = scene3;
	}

	public GameScreen getGameScreen1() {
		return gameScreen1;
	}

	public void setGameScreen1(GameScreenNoBet gameScreen1) {
		this.gameScreen1 = gameScreen1;
	}

	public GameScreenBet getGameScreen2() {
		return gameScreen2;
	}

	public void setGameScreen2(GameScreenBet gameScreen2) {
		this.gameScreen2 = gameScreen2;
	}

	public int getNumberRndsChps() {
		return numberRndsChps;
	}

	public void setNumberRndsChps(int numberRndsChps) {
		this.numberRndsChps = numberRndsChps;
	}



}
