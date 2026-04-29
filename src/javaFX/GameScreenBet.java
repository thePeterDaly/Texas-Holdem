package javaFX;

import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.util.ArrayList;
import core.*;

public class GameScreenBet extends GameScreen{
	
	protected int blindIndex = 0; //Keep track of who's turn it is to put in small and big blinds
	protected int index = 0; //current index for whose turn it is
	protected int folds = 0; //Number of players who folded. Round ends if equal to 3.
	protected int raises = 0; //Keep track of number of raises per round (max 3)
	protected int lastRaiser = -1; //Keep track of who raised last
	protected int movesThisRound = 0; 
	protected int activePlayers = 4;
	protected int stage = 0; //Keep track of which stage we are in (0: pre-flop, 1: flop, 2: turn, 3: river)
	protected Pot pot = new Pot(players);
	protected Text potTxt;
	protected Button call; // these buttons will only appear for the human player (player1)
	protected Button raise;
	protected Button fold;
	protected Button menuEnd;
	protected VBox playBtnBox;
	protected boolean sessionOver = false;
	//Keep track of player's actions in text form
	protected VBox actionRecordBox = new VBox();
	protected Text action1 = new Text();
	protected Text action2 = new Text();
	protected Text action3 = new Text();
	protected Text action4 = new Text();
	
	protected BorderPane foldPane = new BorderPane();
	private Text folded1;
	private Text folded2;
	private Text folded3;
	private Text folded4;
	
	protected Text playerChips1;
	protected Text playerChips2;
	protected Text playerChips3;
	protected Text playerChips4;
	
	
	public GameScreenBet(MainApp app) {
		super(app);
		
		scoreBoard();

		start.setOnAction(e->{
			startRound();
			start.setVisible(false);
		});
		createPlayButtons();
		
		menuEnd = new Button("Back to Menu");
		adjustButton(menuEnd);
		menuEnd.setOnAction(e-> app.showMenu());
		
		nextRound.setOnAction(e->{
			nextRound.setVisible(false);
			potTxt.setText("Pot: 0");
			startNewRound();
		});
		
		foldPane.setPadding(new Insets(100));
		createFoldedText();
		createActionRecord();
		
		//give players their chips
		for (Player p: players) {
			p.setChips(app.getNumberRndsChps());
		}
		
		potTxt = new Text("Pot: 0");
		potTxt.setFont(Font.font("Cambria", FontWeight.BOLD, 22));
		potTxt.setFill(Color.WHITE);
		root.getChildren().add(potTxt);
		potTxt.setTranslateY(100);
		
		
		
	}

	@Override
	public void startRound() {
		super.startRound();
		this.stage  = 0; //Pre-flop
		
		//Reset round statistics
		index = blindIndex;
		activePlayers = 4;
		movesThisRound = 0;
		pot.resetPot();
		folds = 0;
		for (Player p: players) {
			p.setFolded(false);
			p.setLastMove("");
		}
		
		//ensure proper visibilities
		folded1.setVisible(false);
		folded2.setVisible(false);
		folded3.setVisible(false);
		folded4.setVisible(false);
		resetActionRecord();
		textPane.getChildren().clear();
		
		pot.smallBlind(players.get(blindIndex));
//		players.get(blindIndex).setLastMove("posted the small blind");
		updateActionRecord();
		updateScoreBoard();
		
		PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
		pause.setOnFinished(e->{
			index = getNextPlayerIndex(index);
			pot.bigBlind(players.get(getNextPlayerIndex(blindIndex)));
			updateActionRecord();
			updateScoreBoard();
			
			index = getNextPlayerIndex(index);
			blindIndex = getNextPlayerIndex(blindIndex); //update blind index for each round
			playNextStage();
		});
		pause.play();
	}
	
	@Override
	public void startNewRound() {
		super.startRound();
		this.stage  = 0; //Pre-flop
		
		//Reset round statistics
		index = blindIndex;
		activePlayers = 4;
		movesThisRound = 0;
		folds = 0;
		pot.resetPot();
		for (Player p: players) {
			p.setFolded(false);
			p.setLastMove("");
		}
		//ensure proper visibilities
		folded1.setVisible(false);
		folded2.setVisible(false);
		folded3.setVisible(false);
		folded4.setVisible(false);
		resetActionRecord();
		textPane.getChildren().clear();
		
		//Animate cards coming back to deck
		Animation a1 = returnAnimation(p1c1, deckImage);
		Animation a2 = returnAnimation(p1c2, deckImage);
		Animation a3 = returnAnimation(p2c1, deckImage);
		Animation a4 = returnAnimation(p2c2, deckImage);
		Animation a5 = returnAnimation(p3c1, deckImage);
		Animation a6 = returnAnimation(p3c2, deckImage);
		Animation a7 = returnAnimation(p4c1, deckImage);
		Animation a8 = returnAnimation(p4c2, deckImage);
		Animation a9 = returnAnimation(flop1, deckImage);
		Animation a10 = returnAnimation(flop2, deckImage);
		Animation a11 = returnAnimation(flop3, deckImage);
		Animation a12 = returnAnimation(turn, deckImage);
		Animation a13 = returnAnimation(river, deckImage);
		ParallelTransition backToDeckAnimation = new ParallelTransition(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13);
		backToDeckAnimation.play();

		//animate deck getting shuffled (just spinning real fast)
		PauseTransition pause1 = new PauseTransition(Duration.seconds(1));
		pause1.setOnFinished(e->{
			RotateTransition rotate = new RotateTransition();
			rotate.setNode(deckImage); 
			rotate.setDuration(Duration.millis(200)); 
			rotate.setByAngle(360); 
			rotate.setCycleCount(7); 
			rotate.play();
		});
		pause1.play();
		deckImage.setRotate(180); //minor detail so when cards come back to deck they rotate too, fun 
		
		PauseTransition pause2 = new PauseTransition(Duration.seconds(3));
		pause2.setOnFinished(e->{
			pot.smallBlind(players.get(blindIndex));
			updateActionRecord();
			updateScoreBoard();
			PauseTransition pause = new PauseTransition(Duration.seconds(1.5));		
			pause.setOnFinished(event->{
				index = getNextPlayerIndex(index);
				pot.bigBlind(players.get(getNextPlayerIndex(blindIndex)));
				updateActionRecord();
				updateScoreBoard();
				
				index = getNextPlayerIndex(index);
				blindIndex = getNextPlayerIndex(blindIndex);
				playNextStage();
				
			});
			pause.play();
		});
		pause2.play();
		
	}
	
	//get the index of the next player in the ArrayList
	public int getNextPlayerIndex(int index) {
		if (index < 3) {
			return index + 1;
		}
		else {
			return 0;
		}
	}
	
	//create the play buttons for the player
	public void createPlayButtons() {
		call = new Button("Call");
		raise = new Button("Raise");
		fold = new Button("Fold");
		adjustButton(call);
		adjustButton(raise);
		adjustButton(fold);
		call.setPrefWidth(60);
		raise.setPrefWidth(60);
		fold.setPrefWidth(60);
		playBtnBox = new VBox();
		playBtnBox.setSpacing(5);
		playBtnBox.getChildren().addAll(call, raise, fold);
		buttonPane.getChildren().add(playBtnBox);
		playBtnBox.setAlignment(Pos.BOTTOM_CENTER);
		playBtnBox.setPickOnBounds(false);
		playBtnBox.setTranslateY(-45);
		playBtnBox.setTranslateX(175);
		playBtnBox.setVisible(false);
		
		//Make button do stuff
		call.setOnAction(e -> {
		    try {
		        called(player1);
		    } catch (NegativeChipsException ex) {
		    	if (player1.getChips() < 2)
		    		sessionOver = true;
		        return;
		    }
		    playBtnBox.setVisible(false);
		    names.get(index).setFill(Color.WHITE);
		    updateActionRecord();
		    index = getNextPlayerIndex(index);
		    playTurn();
		});
		raise.setOnAction(e -> {
		    try {
		        if (raises < 3) {
		            raised(player1);
		        } else {
		            called(player1); // just in case button somehow still appears
		        }
		    } catch (NegativeChipsException ex) {
		        return;
		    }
		    playBtnBox.setVisible(false);
		    names.get(index).setFill(Color.WHITE);
		    updateActionRecord();
		    index = getNextPlayerIndex(index);
		    playTurn();
		});
		fold.setOnAction(e -> {
		    folded(player1);
		    playBtnBox.setVisible(false);
		    names.get(index).setFill(Color.WHITE);
		    updateActionRecord();
		    index = getNextPlayerIndex(index);
		    playTurn();
		});
	}

	
	//Method to control round of betting
	public void playTurn() {
	    // stop if round is over
	    if (bettingRoundOver()) {
	    	stage++;
	        if (folds >= 3) 
	        	declareDefaultWinner();
	        else if (stage > 3) 
	        	revealWinner();
	        else 
	        	playNextStage();
	        return;
	    }
	    Player current = players.get(index);
	    // skip folded players
	    if (current.isFolded()) {
	        index = getNextPlayerIndex(index);
	        playTurn();
	        return;
	    }
	    names.get(index).setFill(Color.YELLOW); //make player's name stick out more when his turn

	    // human player's turn
	    if (!(current instanceof ComputerPlayer)) {
	    	playBtnBox.setVisible(true);
	    	if (raises >= 3) {
	    		raise.setVisible(false);
	    	}
	        return;
	    }

	    //computer player's turn otherwise
	    ComputerPlayer p = (ComputerPlayer) current;
	    try {
	        int action = -1;
	        switch(stage) {
	        case 0: action = p.chooseActionPreFlop();
	        break;
	        case 1: action = p.chooseActionFlop(); 
	        break;
	        case 2: action = p.chooseActionTurn();
	        break;
	        case 3: action = p.chooseActionRiver();
	        break;
	        }
	        if (action == 0) folded(p);
	        else if (action == 1) called(p);
	        else {
	        	if (raises >= 3) called(p); //force call on computer if raise cap reached
	        	else raised(p);
	        }
	    } catch (NegativeChipsException e) {
	        try {
	            called(p);
	        } catch (NegativeChipsException ex) {
	            folded(p);
	        }
	    }
	    updateActionRecord();
	    //adding a delay so it feels more natural
	    PauseTransition pause = new PauseTransition(Duration.seconds(1));
	    pause.setOnFinished(e -> {
	    	names.get(index).setFill(Color.WHITE);
	        index = getNextPlayerIndex(index);
	        playTurn();
	    });
	    pause.play();
	}
	
	//method to play whichever is the next stage of the game (flop, turn, etc)
	public void playNextStage() {
		PauseTransition pause = new PauseTransition(Duration.seconds(4));
		PauseTransition pause2 = new PauseTransition(Duration.seconds(2));
		raise.setVisible(true);
		updateScoreBoard();
		//reset values for betting round
		raises = 0;
		this.movesThisRound = 0;
		
	    if (stage > 0) {
	        pot.setCurrentBet(0);
	        for (Player p: players) {
	            p.setChipsInPot(0);
	        }
	    }
		switch (stage) {
			case 0: dealHoleCards();
				pause.setOnFinished(e-> playTurn());
				pause.play();
				break;
			case 1: revealFlop();
				pause2.setOnFinished(e-> playTurn());
				pause2.play();
				break;
			case 2: revealTurn();
				pause2.setOnFinished(e-> playTurn());
				pause2.play();
				break;
			case 3: revealRiver();
				pause2.setOnFinished(e-> playTurn());
				pause2.play();
				break;
		}
	}
	
	
	public boolean bettingRoundOver() {
		if (folds >= 3) return true;
		else if (movesThisRound >= activePlayers && pot.betsMatch()) return true;
		else return false;
	}
	
	public void createFoldedText() {
		//Adding FOLDED labels to screen
		folded1 = new Text("FOLDED");
		folded2 = new Text("FOLDED");
		folded3 = new Text("FOLDED");
		folded4 = new Text("FOLDED");
		//Adding names to array list for efficiency 
		ArrayList<Text> foldTexts = new ArrayList<>();
		foldTexts.add(folded1);
		foldTexts.add(folded2);
		foldTexts.add(folded3);
		foldTexts.add(folded4);
		//Adjusting names pane and nodes
		foldPane.setPadding(new Insets(90, 50, 90, 50));
		foldPane.setBottom(folded1);
		foldPane.setLeft(folded2);
		foldPane.setTop(folded3);
		foldPane.setRight(folded4);
		folded2.setRotate(90);
		folded4.setRotate(270);
		
		DropShadow dropShadow = new DropShadow();
		dropShadow.setRadius(50.0);        
		dropShadow.setColor(Color.BLACK);
		
		for(Text n: foldTexts) {
			foldPane.setAlignment(n, Pos.CENTER);
			n.setFont(Font.font("Impact", 70));
			n.setFill(Color.WHITE);
			n.setEffect(dropShadow);
			n.setVisible(false);
		}
		foldPane.setMouseTransparent(true);
		root.getChildren().add(foldPane);
	}
	
	//method when a player folds
	public void folded(Player p) {
		if (p == player1) folded1.setVisible(true);
		else if (p == player2) folded2.setVisible(true);
		else if (p == player3) folded3.setVisible(true);
		else folded4.setVisible(true);
		
		p.fold();
		folds++;
		activePlayers--;
		movesThisRound++;
	}
	
	//method to call when player calls
	public void called(Player p) throws NegativeChipsException{
		pot.call(p);
		movesThisRound++;
		updateScoreBoard();
	}
	
	//method to call when player raises
	public void raised(Player p) throws NegativeChipsException{
		pot.raise(p);
		raises++;
		movesThisRound++;
		updateScoreBoard();
	}
	
	@Override
	//reveal winner when more than one player is left
	public void revealWinner(){
		
		if (player2.isFolded() == false) {
			p2c1.setImage(new Image(getClass().getResource("/CardDataset/" + player2.getCards().get(0).toString() + ".png").toExternalForm()));
			p2c2.setImage(new Image(getClass().getResource("/CardDataset/" + player2.getCards().get(1).toString() + ".png").toExternalForm()));
		}
		if (player3.isFolded() == false) {
			p3c1.setImage(new Image(getClass().getResource("/CardDataset/" + player3.getCards().get(0).toString() + ".png").toExternalForm()));
			p3c2.setImage(new Image(getClass().getResource("/CardDataset/" + player3.getCards().get(1).toString() + ".png").toExternalForm()));
		}
		if (player4.isFolded() == false) {
			p4c1.setImage(new Image(getClass().getResource("/CardDataset/" + player4.getCards().get(0).toString() + ".png").toExternalForm()));
			p4c2.setImage(new Image(getClass().getResource("/CardDataset/" + player4.getCards().get(1).toString() + ".png").toExternalForm()));
		}
		
		textPane.setPadding(new Insets(60, 65, 60, 65));
		VBox winnerTxt = new VBox();
		winnerTxt.setSpacing(7);
		textPane.getChildren().add(winnerTxt);
		
		ArrayList<Player> activePlayers = new ArrayList<>();
		for (Player p: players) {
			if (p.isFolded() == false)
				activePlayers.add(p);
		}
		
		try {
			Player winner = game.determineWinner(activePlayers); 
			winner.won();
			winner.setChips(winner.getChips() + pot.getPot());
			Text winnerTxt1 = new Text(winner.getName() + " won this round!");
			Text winnerTxt2 = new Text(winner.getHand().toString());
			winnerTxt1.setFont(Font.font("Cambria", FontWeight.BOLD,  25));
			winnerTxt1.setFill(Color.WHITE);
			winnerTxt2.setFont(Font.font("Cambria", 20));
			winnerTxt2.setFill(Color.WHITE);
			
			winnerTxt.getChildren().addAll(winnerTxt1, winnerTxt2);
			updateScoreBoard();
			
		}
		catch(TieException e) {
			ArrayList<Player> winners = game.determineWinners(activePlayers);
			String winnerNames = winners.get(0).getName();
			winners.get(0).won();
		
			for (int i = 1; i < winners.size(); i++) {
				winnerNames += " and " + winners.get(i).getName();
				winners.get(i).won();
			}
			for (Player p: winners) {
				int share = pot.getPot() / winners.size();
				p.setChips(p.getChips() + share);
			}
			
			if (winners.size() >= 3) {
				Text tie1 = new Text("It's a Tie between");
				Text tie2 = new Text(winnerNames + "!");
				Text tieHand = new Text(winners.get(0).getHand().toString());
				tie1.setFont(Font.font("Cambria", FontWeight.BOLD,  25));
				tie1.setFill(Color.WHITE);
				tie2.setFont(Font.font("Cambria", FontWeight.BOLD,  25));
				tie2.setFill(Color.WHITE);
				tieHand.setFont(Font.font("Cambria",  18));
				tieHand.setFill(Color.WHITE);
				
				winnerTxt.getChildren().addAll(tie1, tie2, tieHand);
			}
			else {
				Text tie = new Text("It's a Tie between " + winnerNames + "!");
				Text tieHand = new Text(winners.get(0).getHand().toString());
				tie.setFont(Font.font("Cambria", FontWeight.BOLD,  25));
				tie.setFill(Color.WHITE);
				tieHand.setFont(Font.font("Cambria", 18));
				tieHand.setFill(Color.WHITE);
				winnerTxt.getChildren().addAll(tie, tieHand);
			}
			updateScoreBoard();
		}
		
		//Change button if one or more players has less than big blind
		for(Player p: players) {
			if (p.getChips() < 2)
				sessionOver = true;
		}
		if (sessionOver) {
			nextRound.setText("End Session");
			nextRound.setOnAction(e-> createEndSessionPane());
			nextRound.setVisible(true);
		}
		else nextRound.setVisible(true);
	}
	
	//like reveal winner but only when player wins because others folded. We don't show their cards then
	public void declareDefaultWinner() {
		
		textPane.setPadding(new Insets(60, 65, 60, 65));
		VBox winnerTxt = new VBox();
		winnerTxt.setSpacing(7);
		textPane.getChildren().add(winnerTxt);
		
		Player winner; //if this method is being called, we're assuming only 1 player hasn't folded
		for (Player p: players) {
			if (p.isFolded() == false) {
				winner = p;
				winner.won();
				winner.setChips(winner.getChips() + pot.getPot());
				updateScoreBoard();
				Text winnerTxt1 = new Text(winner.getName() + " won this round!");
				Text winnerTxt2 = new Text("Won by default, all other players folded");
				winnerTxt1.setFont(Font.font("Cambria", FontWeight.BOLD,  25));
				winnerTxt1.setFill(Color.WHITE);
				winnerTxt2.setFont(Font.font("Cambria", 20));
				winnerTxt2.setFill(Color.WHITE);
				
				winnerTxt.getChildren().addAll(winnerTxt1, winnerTxt2);
				break;
			}
		}
		
		
		//Change button if one or more players has less than big blind
		for(Player p: players) {
			if (p.getChips() < 2)
				sessionOver = true;
		}
		if (sessionOver) {
			nextRound.setText("End Session");
			nextRound.setOnAction(e-> createEndSessionPane());
			nextRound.setVisible(true);
		}
		else nextRound.setVisible(true);
		
	}
	
	//Create text that updates so player can see latest moves from everyone
	public void createActionRecord() {
		actionRecordBox.getChildren().addAll(action1, action2, action3, action4);
		ArrayList<Text> actions = new ArrayList<>();
		actions.add(action1);
		actions.add(action2);
		actions.add(action3);
		actions.add(action4);
		
		for(Text t: actions) {
			t.setFont(Font.font("Cambria", FontWeight.BOLD, 20));
			t.setFill(Color.WHITE);
		}
		root.getChildren().add(actionRecordBox);
		actionRecordBox.setMouseTransparent(true);
		actionRecordBox.setAlignment(Pos.BOTTOM_LEFT);
		actionRecordBox.setTranslateX(70);
		actionRecordBox.setTranslateY(-100);
	}
	
	//Update the action records box (this will happen after every move)
	public void updateActionRecord() {
		if (action3.getText().length() != 0)
			action4.setText(action3.getText());
		if (action2.getText().length() != 0)
			action3.setText(action2.getText());
		if (action1.getText().length() != 0)
			action2.setText(action1.getText());
		
		action1.setText(players.get(index).getName() + " " + players.get(index).getLastMove() + ".");	
	}
	
	public void resetActionRecord() {
		action1.setText("");
		action2.setText("");
		action3.setText("");
		action4.setText("");
	}
	
	//method to create score board to keep track of everyone's chips
	public void scoreBoard() {
		
		ArrayList <Text> sb = new ArrayList<>();
		Text playerName1 = new Text(player1.getName()); sb.add(playerName1);
		Text playerName2 = new Text(player2.getName()); sb.add(playerName2);
		Text playerName3 = new Text(player3.getName()); sb.add(playerName3);
		Text playerName4 = new Text(player4.getName()); sb.add(playerName4);
		
		//set all initial RoundsWon values to 0
		playerChips1 = new Text("0"); sb.add(playerChips1);
		playerChips2 = new Text("0"); sb.add(playerChips2);
		playerChips3 = new Text("0"); sb.add(playerChips3);
		playerChips4 = new Text("0"); sb.add(playerChips4);
		
		for (Text t: sb) {
			t.setFont(Font.font("Cambria", FontWeight.BOLD,  20));
			t.setFill(Color.WHITE);
		}

		scoreBoard.add(playerName1, 1, 0);
		scoreBoard.add(playerName2, 1, 1);
		scoreBoard.add(playerName3, 1, 2);
		scoreBoard.add(playerName4, 1, 3);
		scoreBoard.add(playerChips1, 2, 0);
		scoreBoard.add(playerChips2, 2, 1);
		scoreBoard.add(playerChips3, 2, 2);
		scoreBoard.add(playerChips4, 2, 3);
		
		scoreBoard.setPadding(new Insets(90, 80, 90, 80));
		scoreBoard.setVgap(5);
		scoreBoard.setHgap(25);
		scoreBoard.setAlignment(Pos.TOP_RIGHT);
		scoreBoard.setMouseTransparent(true);
	}
	
	public void updateScoreBoard() {
		playerChips1.setText(String.valueOf(player1.getChips()));
		playerChips2.setText(String.valueOf(player2.getChips()));
		playerChips3.setText(String.valueOf(player3.getChips()));
		playerChips4.setText(String.valueOf(player4.getChips()));
		potTxt.setText("Pot: " + String.valueOf(pot.getPot()));
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
		
		Text gridTitle = new Text("Everyone's final earnings:");
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
		
		playerChips1 = new Text(String.valueOf(player1.getChips())); sb.add(playerChips1);
		playerChips2 = new Text(String.valueOf(player2.getChips())); sb.add(playerChips2);
		playerChips3 = new Text(String.valueOf(player3.getChips())); sb.add(playerChips3);
		playerChips4 = new Text(String.valueOf(player4.getChips())); sb.add(playerChips4);
		
		for (Text t: sb) {
			t.setFont(Font.font("Cambria", FontWeight.BOLD,  20));
			t.setFill(Color.BLACK);
		}
		finalScores.add(playerName1, 0, 0);
		finalScores.add(playerName2, 0, 1);
		finalScores.add(playerName3, 0, 2);
		finalScores.add(playerName4, 0, 3);
		finalScores.add(playerChips1, 1, 0);
		finalScores.add(playerChips2, 1, 1);
		finalScores.add(playerChips3, 1, 2);
		finalScores.add(playerChips4, 1, 3);
		
		finalScores.setAlignment(Pos.CENTER);
		
		//add all nodes to v box to then add to pane
		vboxEnd.getChildren().addAll(gameOver, gridTitle, finalScores, menuEnd);
		vboxEnd.setAlignment(Pos.CENTER);
		endSessionPane.getChildren().addAll(frame, vboxEnd);
		
		root.getChildren().add(endSessionPane);
	}
	

}
	

