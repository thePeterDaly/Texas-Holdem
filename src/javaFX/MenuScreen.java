package javaFX;

import java.util.ArrayList;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class MenuScreen {
	
	private StackPane root;
	private VBox nodes;
	private TextField tfUsername = new TextField("You");
	private TextField tfPlayer2 = new TextField("John");
	private TextField tfPlayer3 = new TextField("Paul");
	private TextField tfPlayer4 = new TextField("Keefe");
	private Button playBtn;
	private RadioButton noBetBtn;
	private RadioButton limitHoldemBtn;
	private Label numberRndsChps;
	private TextField tfNumberRndsChps = new TextField("10");
	
	
	public MenuScreen (MainApp app) {
		root = new StackPane();
		ImageView background = new ImageView("Background3.jpg");
		background.fitWidthProperty().bind(root.widthProperty());
		
		//VBox to place most nodes 
		nodes = new VBox();
		nodes.setAlignment(Pos.CENTER);
		nodes.setSpacing(20);
		
		Text title = new Text("Texas Hold'em");
		title.setFont(Font.font("Elephant", 60));
		title.setFill(Color.WHITE);
		
		Text subtitle = new Text("A School Project by Peter Daly");
		Text courseInfo = new Text("Georgia Southern - Programming Principles II, Spring 2026");
		Text professor = new Text("Professor: Joshua Farara");
		Text musicCredit = new Text("Music by Paul Daly");
		
		subtitle.setFont(Font.font("Cambria", 18));
		subtitle.setFill(Color.WHITE);
		adjustText(courseInfo);
		adjustText(professor);
		adjustText(musicCredit);
		
		VBox credits = new VBox();
		credits.setAlignment(Pos.CENTER);
		credits.setSpacing(5);
		credits.getChildren().addAll(subtitle, courseInfo, professor, musicCredit);
		
		
		//grid pane to place renaming options
		GridPane namesPane = new GridPane();
		namesPane.setVgap(5);
		namesPane.setHgap(5);
		namesPane.setAlignment(Pos.CENTER);
		
		Label lbP1 = new Label("Username: "); adjustText(lbP1);
		Label lbP2 = new Label("Player 2: "); adjustText(lbP2);
		Label lbP3 = new Label("Player 3: "); adjustText(lbP3);
		Label lbP4 = new Label("Player 4: "); adjustText(lbP4);
		
		tfUsername.setFont(Font.font("Cambria", 18));
		tfPlayer2.setFont(Font.font("Cambria", 18));
		tfPlayer3.setFont(Font.font("Cambria", 18));
		tfPlayer4.setFont(Font.font("Cambria", 18));
		
		namesPane.add(lbP1, 0, 0);
		namesPane.add(lbP2, 0, 1);
		namesPane.add(lbP3, 0, 2);
		namesPane.add(lbP4, 0, 3);
		namesPane.add(tfUsername, 1, 0);
		namesPane.add(tfPlayer2, 1, 1);
		namesPane.add(tfPlayer3, 1, 2);
		namesPane.add(tfPlayer4, 1, 3);
		
		//Radio Buttons
		GridPane optionsPane = new GridPane();
		optionsPane.setVgap(10);
//		optionsPane.setHgap(10);
		optionsPane.setAlignment(Pos.CENTER);
		
		noBetBtn = new RadioButton("No Bet Hold'em                      ");
		limitHoldemBtn = new RadioButton("Limit Hold'em");
		noBetBtn.setSelected(true);
		numberRndsChps = new Label("Number of rounds:");
		
		tfNumberRndsChps.setFont(Font.font("Cambria", 18));
		numberRndsChps.setFont(Font.font("Cambria", 18));
		numberRndsChps.setTextFill(Color.WHITE);
		noBetBtn.setFont(Font.font("Cambria", 18));
		noBetBtn.setTextFill(Color.WHITE);
		limitHoldemBtn.setFont(Font.font("Cambria", 18));
		limitHoldemBtn.setTextFill(Color.WHITE);
		
		noBetBtn.setOnAction(e-> updateLabel());
		limitHoldemBtn.setOnAction(e-> updateLabel());
		
		optionsPane.add(noBetBtn, 0, 4);
		optionsPane.add(limitHoldemBtn, 1, 4);
		optionsPane.add(numberRndsChps, 0, 5);
		optionsPane.add(tfNumberRndsChps, 1, 5);
		
		ToggleGroup tg = new ToggleGroup();
		noBetBtn.setToggleGroup(tg);
		limitHoldemBtn.setToggleGroup(tg);
		
		//Play Button
		playBtn = new Button("PLAY");
		playBtn.setOnAction(e -> {
			ArrayList<String> playerNames = new ArrayList<>();
			playerNames.add(tfUsername.getText());
			playerNames.add(tfPlayer2.getText());
			playerNames.add(tfPlayer3.getText());
			playerNames.add(tfPlayer4.getText());
			app.setPlayerNames(playerNames);
			app.setNumberRndsChps(Integer.parseInt(tfNumberRndsChps.getText()));
			if (noBetBtn.isSelected()) {
				app.showGame();
			}
			else app.showGame2();
		});
		playBtn.setFont(Font.font("Impact", 30));
		playBtn.setTextFill(Color.color(0.01, 0.32, 0.20));
		
		
		nodes.getChildren().addAll(title, credits, namesPane, optionsPane, playBtn);
		
		root.getChildren().addAll(background, nodes);
		
		
	}
	
	public void adjustText(Text t) {
		t.setFont(Font.font("Cambria", 18));
		t.setFill(Color.WHITE);
	}
	public void adjustText(Label l) {
		l.setTextFill(Color.WHITE);
		l.setFont(Font.font("Cambria", 18));
	}
	
	public Parent getRoot() {
		return root;
	}
	
	//update the label whether bet or no bet game
	public void updateLabel() {
		if(noBetBtn.isSelected()) {
			numberRndsChps.setText("Number of Rounds:");
			tfNumberRndsChps.setText("10");
		}
		else {
			numberRndsChps.setText("Number of Starting Chips:");
			tfNumberRndsChps.setText("30");
		}
	}

}
