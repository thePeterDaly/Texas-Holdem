package javaFX;

///CLASS NOT COMPLETED YET, STILL NEED TO FIX THINGS

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class GameScreenNoBet extends GameScreen{
	
	protected Button revealWinner;
	private int maxRounds;
	private Button menuEnd;

	public GameScreenNoBet(MainApp app) {
		super(app);
		rounds = 0;
		
		menuEnd = new Button("Back to Menu");
		adjustButton(menuEnd);
		menuEnd.setOnAction(e-> app.showMenu());
		
		maxRounds = app.getNumberRndsChps();
		//create a button to reveal all the cards (and the winner)
		revealWinner = new Button("Show all cards");
		revealWinner.setVisible(false);
		adjustButton(revealWinner);
		revealWinner.setOnAction(e-> {
			rounds++;
			revealWinner();
			if (rounds >= maxRounds) {
				nextRound.setText("Finish");
				nextRound.setOnAction(event->{
					createEndSessionPane();
				});
			}
			nextRound.setVisible(true);
			updateScoreBoard();
		});	
		buttons.getChildren().clear(); //we do this so we can order the buttons properly
		buttons.getChildren().addAll(nextRound, revealWinner, menu);
		
		//action of next round button
		nextRound.setOnAction(e-> {
			this.textPane.getChildren().clear();
			revealWinner.setVisible(false);
			startNewRound();
			nextRound.setVisible(false);
		});
		
		//Make it so the card's get dealt or revealed whenever the deck top card is pressed
		deckImage.setOnMouseClicked(e -> {
			if (this.step == 0) dealHoleCards();
			else if (this.step == 1) revealFlop();
		    else if (this.step == 2) revealTurn();
		    else if (this.step == 3) {
		    	revealRiver();
		        revealWinner.setVisible(true);
		    }
		});
		
		//Create score board
		scoreBoard();
	}
	
	//method to create score board (number of rounds won)
	public void scoreBoard() {
		
		ArrayList <Text> sb = new ArrayList<>();
		Text playerName1 = new Text(player1.getName()); sb.add(playerName1);
		Text playerName2 = new Text(player2.getName()); sb.add(playerName2);
		Text playerName3 = new Text(player3.getName()); sb.add(playerName3);
		Text playerName4 = new Text(player4.getName()); sb.add(playerName4);
		
		//set all initial RoundsWon values to 0
		playerWins1 = new Text("0"); sb.add(playerWins1);
		playerWins2 = new Text("0"); sb.add(playerWins2);
		playerWins3 = new Text("0"); sb.add(playerWins3);
		playerWins4 = new Text("0"); sb.add(playerWins4);
		
		for (Text t: sb) {
			t.setFont(Font.font("Cambria", FontWeight.BOLD,  20));
			t.setFill(Color.WHITE);
		}

		scoreBoard.add(playerName1, 1, 0);
		scoreBoard.add(playerName2, 1, 1);
		scoreBoard.add(playerName3, 1, 2);
		scoreBoard.add(playerName4, 1, 3);
		scoreBoard.add(playerWins1, 2, 0);
		scoreBoard.add(playerWins2, 2, 1);
		scoreBoard.add(playerWins3, 2, 2);
		scoreBoard.add(playerWins4, 2, 3);
		
		scoreBoard.setPadding(new Insets(90, 80, 90, 80));
		scoreBoard.setVgap(5);
		scoreBoard.setHgap(25);
		scoreBoard.setAlignment(Pos.TOP_RIGHT);
		scoreBoard.setMouseTransparent(true);
	}
	
	//update the score board to the right number of wins for each player
	public void updateScoreBoard() {
		playerWins1.setText(String.valueOf(player1.getRoundsWon()));
		playerWins2.setText(String.valueOf(player2.getRoundsWon()));
		playerWins3.setText(String.valueOf(player3.getRoundsWon()));
		playerWins4.setText(String.valueOf(player4.getRoundsWon()));
	}
	
	//this pane will get shown at the end of a game session and will allow player to see everyone's scores and go back to menu
		public void createEndSessionPane() {
			StackPane endSessionPane = new StackPane();
			Rectangle frame = new Rectangle(600, 400);
			frame.setFill(Color.WHITE);
			
			//add a drop shadow
			DropShadow dropShadow = new DropShadow();
			dropShadow.setOffsetX(7);
			dropShadow.setOffsetY(10);
			dropShadow.setRadius(10.0);        
			dropShadow.setColor(Color.BLACK);
			frame.setEffect(dropShadow);
			
			
			VBox vboxEnd = new VBox();
			vboxEnd.setSpacing(15);
			
			Text gameOver = new Text("Session Over!");
			gameOver.setFont(Font.font("Elephant", 50));
			gameOver.setFill(Color.BLACK);
			
			Text gridTitle = new Text("Everyone's final scores:");
			gridTitle.setFont(Font.font("Cambria", FontWeight.BOLD, 30));
			gridTitle.setFill(Color.BLACK);
			
			//grid pane to show everyone's stats
			GridPane finalScores = new GridPane();
			finalScores.setHgap(10);
			
			ArrayList <Text> sb = new ArrayList<>();
			Text playerName1 = new Text(player1.getName()); sb.add(playerName1);
			Text playerName2 = new Text(player2.getName()); sb.add(playerName2);
			Text playerName3 = new Text(player3.getName()); sb.add(playerName3);
			Text playerName4 = new Text(player4.getName()); sb.add(playerName4);
			
			playerWins1 = new Text(String.valueOf(player1.getRoundsWon())); sb.add(playerWins1);
			playerWins2 = new Text(String.valueOf(player2.getRoundsWon())); sb.add(playerWins2);
			playerWins3 = new Text(String.valueOf(player3.getRoundsWon())); sb.add(playerWins3);
			playerWins4 = new Text(String.valueOf(player4.getRoundsWon())); sb.add(playerWins4);
			
			for (Text t: sb) {
				t.setFont(Font.font("Cambria", FontWeight.BOLD,  20));
				t.setFill(Color.BLACK);
			}
			finalScores.add(playerName1, 0, 0);
			finalScores.add(playerName2, 0, 1);
			finalScores.add(playerName3, 0, 2);
			finalScores.add(playerName4, 0, 3);
			finalScores.add(playerWins1, 1, 0);
			finalScores.add(playerWins2, 1, 1);
			finalScores.add(playerWins3, 1, 2);
			finalScores.add(playerWins4, 1, 3);
			
			finalScores.setAlignment(Pos.CENTER);
			
			//add all nodes to v box to then add to pane
			vboxEnd.getChildren().addAll(gameOver, gridTitle, finalScores, menuEnd);
			vboxEnd.setAlignment(Pos.CENTER);
			endSessionPane.getChildren().addAll(frame, vboxEnd);
			
			root.getChildren().add(endSessionPane);
		}

}
