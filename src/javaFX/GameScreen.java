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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.util.ArrayList;
import core.*;

public class GameScreen {
	
	//Data field
	//Panes
	protected StackPane root;
	protected BorderPane namesPane = new BorderPane();
	protected BorderPane cardLayer = new BorderPane();
	protected Pane animationLayer = new Pane();
	protected StackPane buttonPane = new StackPane();
	protected StackPane textPane = new StackPane();
	protected GridPane scoreBoard = new GridPane();
	protected StackPane startPane;
	//Create deck and game
	protected Deck deck = new Deck();
	protected Game game;
	protected Button start;
	//help us keep track of where we are in the game
	protected int step = 0;
	protected int rounds;
	//Creating Players 
	protected Player player1;
	protected ComputerPlayer player2;
	protected ComputerPlayer player3;
	protected ComputerPlayer player4;
	protected ArrayList<Player> players = new ArrayList<>();
	protected ArrayList<Text> names;
	protected Text playerWins1;
	protected Text playerWins2;
	protected Text playerWins3;
	protected Text playerWins4;
	//Card Images
	protected ImageView p1c1;
	protected ImageView p1c2;
	protected ImageView p2c1;
	protected ImageView p2c2;
	protected ImageView p3c1;
	protected ImageView p3c2;
	protected ImageView p4c1;
	protected ImageView p4c2;
	protected ImageView deckImage;
	protected ImageView flop1;
	protected ImageView flop2;
	protected ImageView flop3;
	protected ImageView turn;
	protected ImageView river;
	//Background image
	protected ImageView background;
	//boxes
	protected HBox buttons;
	protected HBox communityCards;
	protected HBox player1Cards;
	protected VBox player2Cards;
	protected HBox player3Cards;
	protected VBox player4Cards;
	//Buttons
	protected Button menu;
	protected Button nextRound;
	
	
	
	public GameScreen(MainApp app) {
		root = new StackPane();
		
		//create players
		player1 = new Player(app.getPlayerNames().get(0));
		player2 = new ComputerPlayer(app.getPlayerNames().get(1));
		player3 = new ComputerPlayer(app.getPlayerNames().get(2));
		player4 = new ComputerPlayer(app.getPlayerNames().get(3));
		
		//show names of players 
		displayNames();
		
		//create a game object to use its determine winner method);
		game = new Game(players, deck); 
		
	    //Add background picture
		background = new ImageView("Background3.jpg");
		background.fitWidthProperty().bind(root.widthProperty());
		
		//Create start Button
		startPane = new StackPane();
		start = new Button("START");
		startPane.getChildren().add(start);
		start.setFont(Font.font("Impact", FontWeight.BOLD, 30));
		start.setTextFill(Color.color(0.01, 0.32, 0.20));
		start.setAlignment(Pos.CENTER);
		start.setOnAction(e-> {
			startRound();
			dealHoleCards();
			start.setVisible(false);
			startPane.setMouseTransparent(true);
		});
		
		//BUTTONS on top right
		menu = new Button("Back to Menu");
		nextRound = new Button("Next Round"); // make it so all cards go back to deck, get shuffled, and the cycle continues
		nextRound.setVisible(false);
		adjustButton(menu);
		adjustButton(nextRound);
		
		menu.setOnAction(e-> app.showMenu()); //go back to the menu Screen
		
		buttonPane.setPadding(new Insets(50, 80, 50, 80));
		buttons = new HBox();
		buttons.setSpacing(10);
		buttons.getChildren().addAll(nextRound, menu);
		buttonPane.getChildren().add(buttons);
		buttons.setPickOnBounds(false);
		buttons.setAlignment(Pos.TOP_RIGHT);
		
		
		//Adjust pane for all the Cards
		//This pane includes several H and VBoxes
		cardLayer.setPadding(new Insets(60, 90, 60, 90));
		communityCards = new HBox();
		communityCards.setMaxWidth(700);
		communityCards.setSpacing(4);
		communityCards.setAlignment(Pos.CENTER_LEFT);
		cardLayer.setCenter(communityCards);
		
		//Deck image card
		deckImage = new ImageView(getClass().getResource("/CardDataset/Back.png").toExternalForm());
		adjustCard(deckImage);
		communityCards.getChildren().add(deckImage);
		
		//FINAL PANE ADJUSTMENTS
		//Make top panes transparent for clicking
		buttonPane.setPickOnBounds(false);
		animationLayer.setMouseTransparent(true);
		namesPane.setMouseTransparent(true);
		textPane.setMouseTransparent(true);
		
		//Add all panes to the root pane
		root.getChildren().addAll(background, cardLayer, namesPane, scoreBoard, textPane, startPane, buttonPane, animationLayer);
		

	}
	
	//method to control flow of one round
	public void startRound() {
		this.step = 0;
		for (Player p: players) {
			p.setCards(new ArrayList<>());
		}
		deck.shuffle();
		deck.setCurrentIndex(0);
	}
	
	//method used when 'next round' is pressed
	public void startNewRound() {
		
		this.step = 0;
		
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
		
		p2c1.setImage(new Image(getClass().getResource("/CardDataset/Back.png").toExternalForm()));
		p2c2.setImage(new Image(getClass().getResource("/CardDataset/Back.png").toExternalForm()));
		p3c1.setImage(new Image(getClass().getResource("/CardDataset/Back.png").toExternalForm()));
		p3c2.setImage(new Image(getClass().getResource("/CardDataset/Back.png").toExternalForm()));
		p4c1.setImage(new Image(getClass().getResource("/CardDataset/Back.png").toExternalForm()));
		p4c2.setImage(new Image(getClass().getResource("/CardDataset/Back.png").toExternalForm()));
		
		startRound();
		
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
		deckImage.setRotate(180);
		
		PauseTransition pause2 = new PauseTransition(Duration.seconds(3));
		pause2.setOnFinished(e->{
			dealHoleCards();
		});
		pause2.play();
	}
	
	//method to show everybody's names
	public void displayNames() {
		//Add players to list
		players.add(player1);
		players.add(player2);
		players.add(player3);
		players.add(player4);
		//Adding name of players to screen
		Text name1 = new Text(player1.getName());
		Text name2 = new Text(player2.getName());
		Text name3 = new Text(player3.getName());
		Text name4 = new Text(player4.getName());
		//Adding names to array list for efficiency 
		names = new ArrayList<>();
		names.add(name1);
		names.add(name2);
		names.add(name3);
		names.add(name4);
		//Adjusting names pane and nodes
		namesPane.setPadding(new Insets(10, 0, 10, 0));
		namesPane.setBottom(name1);
		namesPane.setLeft(name2);
		namesPane.setTop(name3);
		namesPane.setRight(name4);
		name2.setRotate(90);
		name4.setRotate(270);
		
		for(Text n: names) {
			namesPane.setAlignment(n, Pos.CENTER);
			n.setFont(Font.font("Elephant", 25));
			n.setFill(Color.WHITE);
		}	
	}
	
	//method to animate a card being dealt (slide animation)
	public Animation dealAnimation(ImageView deck, ImageView target) {
		Bounds startBounds = deck.localToScene(deck.getBoundsInLocal());
	    Bounds endBounds = target.localToScene(target.getBoundsInLocal());
	
	    ImageView movingCard = new ImageView(deck.getImage());
	    adjustCard(movingCard);
	    
	    //Make sure the right coordinates are in
	    Point2D start = animationLayer.sceneToLocal(startBounds.getMinX(), startBounds.getMinY());
	    Point2D end = animationLayer.sceneToLocal(endBounds.getMinX(), endBounds.getMinY());
	    movingCard.setLayoutX(start.getX());
	    movingCard.setLayoutY(start.getY());
	    
	    animationLayer.getChildren().add(movingCard);
	    
	    //Animate sliding of card
	    TranslateTransition move = new TranslateTransition(Duration.millis(400), movingCard);
	    move.setToX(end.getX() - start.getX());
	    move.setToY(end.getY() - start.getY());
	    
	    //Animate proper rotation of card
	    RotateTransition rotate = new RotateTransition(Duration.millis(400), movingCard);
	    rotate.setToAngle(target.getRotate());
	    
	    //Add both animations so that both happen simultaneously
	    ParallelTransition transition = new ParallelTransition(move, rotate);
	    
	    //make the proper card appear once arrived
	    transition.setOnFinished(e -> {
	        animationLayer.getChildren().remove(movingCard);
	        target.setVisible(true);
	    });
	    return transition;
	}
	
	//method to animate card going back to deck
	public Animation returnAnimation(ImageView card, ImageView deck) {
		Bounds startBounds = card.localToScene(card.getBoundsInLocal());
	    Bounds endBounds = deck.localToScene(deck.getBoundsInLocal());
	
	    ImageView movingCard = new ImageView(card.getImage());
	    adjustCard(movingCard);
	    
	    //Make sure the right coordinates are in
	    Point2D start = animationLayer.sceneToLocal(startBounds.getMinX(), startBounds.getMinY());
	    Point2D end = animationLayer.sceneToLocal(endBounds.getMinX(), endBounds.getMinY());
	    movingCard.setLayoutX(start.getX());
	    movingCard.setLayoutY(start.getY());
	    
	    card.setVisible(false);
	    animationLayer.getChildren().add(movingCard);
	    
	
	    //Animate sliding of card
	    TranslateTransition move = new TranslateTransition(Duration.millis(900), movingCard);
	    move.setToX(end.getX() - start.getX());
	    move.setToY(end.getY() - start.getY());
	    
	    //Animate proper rotation of card
	    RotateTransition rotate = new RotateTransition(Duration.millis(900), movingCard);
	    rotate.setToAngle(deck.getRotate());
	    
	    //Add both animations so that both happen simultaneously
	    ParallelTransition transition = new ParallelTransition(move, rotate);
	    
	    //make the proper card appear once arrived
	    transition.setOnFinished(e -> {
	        animationLayer.getChildren().remove(movingCard);
	    });
	    return transition;
	}
	
	//method to instantiate flop cards
	public void createFlop() {
		communityCards.getChildren().removeAll(flop1, flop2, flop3, turn, river); // get rid of any cards from previous rounds
		flop1 = new ImageView(getClass().getResource("/CardDataset/" + player1.getCards().get(2).toString() + ".png").toExternalForm());
		flop2 = new ImageView(getClass().getResource("/CardDataset/" + player1.getCards().get(3).toString() + ".png").toExternalForm());
		flop3 = new ImageView(getClass().getResource("/CardDataset/" + player1.getCards().get(4).toString() + ".png").toExternalForm());
		adjustCard(flop1);
		adjustCard(flop2);
		adjustCard(flop3);
		flop1.setVisible(false);
		flop2.setVisible(false);
		flop3.setVisible(false);
		communityCards.getChildren().addAll(flop1, flop2, flop3);
	}
	
	//method to instantiate the turn card
	public void createTurn() {
		turn = new ImageView(getClass().getResource("/CardDataset/" + player1.getCards().get(5).toString() + ".png").toExternalForm());
		adjustCard(turn);
		turn.setVisible(false);
		communityCards.getChildren().add(turn);
	}
	
	//instantiate the river card
	public void createRiver() {
		river = new ImageView(getClass().getResource("/CardDataset/" + player1.getCards().get(6).toString() + ".png").toExternalForm());
		adjustCard(river);
		river.setVisible(false);
		communityCards.getChildren().add(river);
	}
	
	//instantiate all hole cards for every player
	public void createPlayerCards() {
		//PLAYER CARDS
		//player 1 cards
		player1Cards = new HBox();
		player1Cards.setSpacing(10);
		//use correct image to display the right cards to the human player
		p1c1 = new ImageView(getClass().getResource("/CardDataset/" + player1.getCards().get(0).toString() + ".png").toExternalForm());
		p1c2 = new ImageView(getClass().getResource("/CardDataset/" + player1.getCards().get(1).toString() + ".png").toExternalForm());
		adjustCard(p1c1);
		adjustCard(p1c2);
		//Hide cards for now until they are dealt
		p1c1.setVisible(false);
		p1c2.setVisible(false);
		player1Cards.getChildren().addAll(p1c1, p1c2);
		player1Cards.setAlignment(Pos.CENTER);
		cardLayer.setBottom(player1Cards);		
		
		//player 2 cards
		player2Cards = new VBox();
		player2Cards.setSpacing(-33);
		p2c1 = new ImageView(getClass().getResource("/CardDataset/Back.png").toExternalForm());
		p2c2 = new ImageView(getClass().getResource("/CardDataset/Back.png").toExternalForm());
		adjustCard(p2c1);
		adjustCard(p2c2);
		p2c1.setRotate(90);
		p2c2.setRotate(90);
		//Hide cards for now until they are dealt
		p2c1.setVisible(false);
		p2c2.setVisible(false);
		player2Cards.getChildren().addAll(p2c1, p2c2);
		player2Cards.setAlignment(Pos.CENTER);
		cardLayer.setLeft(player2Cards);
		
		//player 3 cards
		player3Cards = new HBox();
		player3Cards.setSpacing(10);
		p3c1 = new ImageView(getClass().getResource("/CardDataset/Back.png").toExternalForm());
		p3c2 = new ImageView(getClass().getResource("/CardDataset/Back.png").toExternalForm());
		adjustCard(p3c1);
		adjustCard(p3c2);
		p3c1.setRotate(180);
		p3c2.setRotate(180);
		//Hide cards for now until they are dealt
		p3c1.setVisible(false);
		p3c2.setVisible(false);
		player3Cards.getChildren().addAll(p3c1, p3c2);
		player3Cards.setAlignment(Pos.CENTER);
		cardLayer.setTop(player3Cards);	
		
		//player 4 cards
		player4Cards = new VBox();
		player4Cards.setSpacing(-33);
		p4c1 = new ImageView(getClass().getResource("/CardDataset/Back.png").toExternalForm());
		p4c2 = new ImageView(getClass().getResource("/CardDataset/Back.png").toExternalForm());
		adjustCard(p4c1);
		adjustCard(p4c2);
		p4c1.setRotate(270);
		p4c2.setRotate(270);
		//Hide cards for now until they are dealt
		p4c1.setVisible(false);
		p4c2.setVisible(false);
		player4Cards.getChildren().addAll(p4c1, p4c2);
		player4Cards.setAlignment(Pos.CENTER);
		cardLayer.setRight(player4Cards);	
	}
	
	//method to adjust the size and shadow of each card
	public void adjustCard(ImageView card) {
		card.setFitHeight(160);
		card.setPreserveRatio(true);
		card.setSmooth(true);
		
		//Create shadow object that will be used on cards
		DropShadow dropShadow = new DropShadow();
		dropShadow.setRadius(10.0);        
		dropShadow.setColor(Color.BLACK);
		card.setEffect(dropShadow);
	}
	
	//method for animation to deal hole cards
	public void dealHoleCards() {
		deck.dealHoleCards(players);
		createPlayerCards();
		//add a short pause (otherwise animation is off because placement of cards not updates fast enough)
		PauseTransition pause = new PauseTransition(Duration.millis(200));
		pause.setOnFinished(e->{
			SequentialTransition sequence = new SequentialTransition();
			sequence.getChildren().add(dealAnimation(deckImage, p1c1));
			sequence.getChildren().add(dealAnimation(deckImage, p2c1));
			sequence.getChildren().add(dealAnimation(deckImage, p3c1));
			sequence.getChildren().add(dealAnimation(deckImage, p4c1));
			sequence.getChildren().add(dealAnimation(deckImage, p1c2));
			sequence.getChildren().add(dealAnimation(deckImage, p2c2));
			sequence.getChildren().add(dealAnimation(deckImage, p3c2));
			sequence.getChildren().add(dealAnimation(deckImage, p4c2));
			this.step++;
			sequence.play();
		});
		pause.play();
	}
	
	//animate all flop cards getting revealed
	public void revealFlop() {
		deck.flop(players);
		createFlop();
		PauseTransition pause = new PauseTransition(Duration.millis(200));
		pause.setOnFinished(e->{
			SequentialTransition sequence = new SequentialTransition();
			sequence.getChildren().add(dealAnimation(deckImage, flop1));
			sequence.getChildren().add(dealAnimation(deckImage, flop2));
			sequence.getChildren().add(dealAnimation(deckImage, flop3));
			this.step++;
			sequence.play();
		});
		pause.play();
	}
	
	//Animate turn card revealed
	public void revealTurn() {
		deck.turn(players);
		createTurn();
		
		//add a short pause (otherwise animation is off because placement of cards not updates fast enough)
		PauseTransition pause = new PauseTransition(Duration.millis(200));
		pause.setOnFinished(e->{
			Animation animation = dealAnimation(deckImage, turn);
			this.step++;
			animation.play();
		});
		pause.play();
	}
	
	//Animate last community card being revealed
	public void revealRiver() {
		deck.river(players);
		createRiver();
		//add a short pause (otherwise animation is off because placement of cards not updates fast enough)
		PauseTransition pause = new PauseTransition(Duration.millis(200));
		pause.setOnFinished(e->{
			Animation animation = dealAnimation(deckImage, river);
			this.step++;
			animation.play();
		});
		pause.play();
	}
	
	//reveal all cards and reveal the winner
	public void revealWinner(){
		
		playerWins1.setText(String.valueOf(player1.getRoundsWon()));
		playerWins2.setText(String.valueOf(player2.getRoundsWon()));
		playerWins3.setText(String.valueOf(player3.getRoundsWon()));
		playerWins4.setText(String.valueOf(player4.getRoundsWon()));
		
		p2c1.setImage(new Image(getClass().getResource("/CardDataset/" + player2.getCards().get(0).toString() + ".png").toExternalForm()));
		p2c2.setImage(new Image(getClass().getResource("/CardDataset/" + player2.getCards().get(1).toString() + ".png").toExternalForm()));
		p3c1.setImage(new Image(getClass().getResource("/CardDataset/" + player3.getCards().get(0).toString() + ".png").toExternalForm()));
		p3c2.setImage(new Image(getClass().getResource("/CardDataset/" + player3.getCards().get(1).toString() + ".png").toExternalForm()));
		p4c1.setImage(new Image(getClass().getResource("/CardDataset/" + player4.getCards().get(0).toString() + ".png").toExternalForm()));
		p4c2.setImage(new Image(getClass().getResource("/CardDataset/" + player4.getCards().get(1).toString() + ".png").toExternalForm()));
		
		textPane.setPadding(new Insets(60, 65, 60, 65));
		VBox winnerTxt = new VBox();
		winnerTxt.setSpacing(7);
		textPane.getChildren().add(winnerTxt);
		
		try {
			Player winner = game.determineWinner(players); 
			winner.won();
			Text winnerTxt1 = new Text(winner.getName() + " won this round!");
			Text winnerTxt2 = new Text(winner.getHand().toString());
			winnerTxt1.setFont(Font.font("Cambria", FontWeight.BOLD,  25));
			winnerTxt1.setFill(Color.WHITE);
			winnerTxt2.setFont(Font.font("Cambria", 20));
			winnerTxt2.setFill(Color.WHITE);
			
			winnerTxt.getChildren().addAll(winnerTxt1, winnerTxt2);
			
		}
		catch(TieException e) {
			ArrayList<Player> winners = game.determineWinners(players);
			String winnerNames = winners.get(0).getName();
			winners.get(0).won();
			for (int i = 1; i < winners.size(); i++) {
				winnerNames += " and " + winners.get(i).getName();
				winners.get(i).won();
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
			
		}
		
	}
	
	//change 
	public void adjustButton(Button b) {
		b.setFont(Font.font("Cambria", FontWeight.BOLD, 15));
		b.setTextFill(Color.BLACK);
	}
	
	
	
	//GETTERS AND SETTERS
	public Parent getRoot() {
		return root;
	}

	public BorderPane getNamesPane() {
		return namesPane;
	}

	public void setNamesPane(BorderPane namesPane) {
		this.namesPane = namesPane;
	}

	public BorderPane getCardLayer() {
		return cardLayer;
	}

	public void setCardLayer(BorderPane cardLayer) {
		this.cardLayer = cardLayer;
	}

	public Pane getAnimationLayer() {
		return animationLayer;
	}

	public void setAnimationLayer(Pane animationLayer) {
		this.animationLayer = animationLayer;
	}

	public StackPane getButtonPane() {
		return buttonPane;
	}

	public void setButtonPane(StackPane buttonPane) {
		this.buttonPane = buttonPane;
	}

	public Deck getDeck() {
		return deck;
	}

	public void setDeck(Deck deck) {
		this.deck = deck;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public int getRounds() {
		return rounds;
	}

	public void setRounds(int rounds) {
		this.rounds = rounds;
	}

	public Player getPlayer1() {
		return player1;
	}

	public void setPlayer1(Player player1) {
		this.player1 = player1;
	}

	public Player getPlayer2() {
		return player2;
	}

	public void setPlayer2(ComputerPlayer player2) {
		this.player2 = player2;
	}

	public Player getPlayer3() {
		return player3;
	}

	public void setPlayer3(ComputerPlayer player3) {
		this.player3 = player3;
	}

	public Player getPlayer4() {
		return player4;
	}

	public void setPlayer4(ComputerPlayer player4) {
		this.player4 = player4;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}

	public ImageView getP1c1() {
		return p1c1;
	}

	public void setP1c1(ImageView p1c1) {
		this.p1c1 = p1c1;
	}

	public ImageView getP1c2() {
		return p1c2;
	}

	public void setP1c2(ImageView p1c2) {
		this.p1c2 = p1c2;
	}

	public ImageView getP2c1() {
		return p2c1;
	}

	public void setP2c1(ImageView p2c1) {
		this.p2c1 = p2c1;
	}

	public ImageView getP2c2() {
		return p2c2;
	}

	public void setP2c2(ImageView p2c2) {
		this.p2c2 = p2c2;
	}

	public ImageView getP3c1() {
		return p3c1;
	}

	public void setP3c1(ImageView p3c1) {
		this.p3c1 = p3c1;
	}

	public ImageView getP3c2() {
		return p3c2;
	}

	public void setP3c2(ImageView p3c2) {
		this.p3c2 = p3c2;
	}

	public ImageView getP4c1() {
		return p4c1;
	}

	public void setP4c1(ImageView p4c1) {
		this.p4c1 = p4c1;
	}

	public ImageView getP4c2() {
		return p4c2;
	}

	public void setP4c2(ImageView p4c2) {
		this.p4c2 = p4c2;
	}

	public ImageView getDeckImage() {
		return deckImage;
	}

	public void setDeckImage(ImageView deckImage) {
		this.deckImage = deckImage;
	}

	public ImageView getFlop1() {
		return flop1;
	}

	public void setFlop1(ImageView flop1) {
		this.flop1 = flop1;
	}

	public ImageView getFlop2() {
		return flop2;
	}

	public void setFlop2(ImageView flop2) {
		this.flop2 = flop2;
	}

	public ImageView getFlop3() {
		return flop3;
	}

	public void setFlop3(ImageView flop3) {
		this.flop3 = flop3;
	}

	public ImageView getTurn() {
		return turn;
	}

	public void setTurn(ImageView turn) {
		this.turn = turn;
	}

	public ImageView getRiver() {
		return river;
	}

	public void setRiver(ImageView river) {
		this.river = river;
	}

	public ImageView getBackground() {
		return background;
	}

	public void setBackground(ImageView background) {
		this.background = background;
	}

	public Button getMenu() {
		return menu;
	}

	public void setMenu(Button menu) {
		this.menu = menu;
	}

	public HBox getButtons() {
		return buttons;
	}

	public void setButtons(HBox buttons) {
		this.buttons = buttons;
	}

	public HBox getCommunityCards() {
		return communityCards;
	}

	public void setCommunityCards(HBox communityCards) {
		this.communityCards = communityCards;
	}

	public HBox getPlayer1Cards() {
		return player1Cards;
	}

	public void setPlayer1Cards(HBox player1Cards) {
		this.player1Cards = player1Cards;
	}

	public VBox getPlayer2Cards() {
		return player2Cards;
	}

	public void setPlayer2Cards(VBox player2Cards) {
		this.player2Cards = player2Cards;
	}

	public HBox getPlayer3Cards() {
		return player3Cards;
	}

	public void setPlayer3Cards(HBox player3Cards) {
		this.player3Cards = player3Cards;
	}

	public VBox getPlayer4Cards() {
		return player4Cards;
	}

	public void setPlayer4Cards(VBox player4Cards) {
		this.player4Cards = player4Cards;
	}

	public void setRoot(StackPane root) {
		this.root = root;
	}
	

}
