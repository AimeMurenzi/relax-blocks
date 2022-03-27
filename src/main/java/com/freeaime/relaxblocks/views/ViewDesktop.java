/**
 * @Author: Aimé
 * @Date:   2022-03-26 19:08:11
 * @Last Modified by:   Aimé
 * @Last Modified time: 2022-03-27 01:52:12
 */
package com.freeaime.relaxblocks.views;

import static java.lang.System.exit;

import com.freeaime.relaxblocks.interfaces.ClickCallBack;
import com.freeaime.relaxblocks.logic.Game;
import com.freeaime.relaxblocks.models.Grid;
import com.freeaime.relaxblocks.models.Score;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ViewDesktop extends Application implements ClickCallBack{

    public static final ViewFancyInputDialogue FANCY_DIALOGUE = new ViewFancyInputDialogue();
    public static final ViewHighScore VIEW_HIGH_SCORE=new ViewHighScore();
    private ViewGrid viewGrid;
    private Label scoreLabel=new Label("Player 1 Score: 0");
    private Grid grid = new Grid();
    private Button quitButton=new Button("Quit");
    private Button scoreBoardButton=new Button("Score Board");
    private HBox menuHBox=new HBox(quitButton,scoreBoardButton);

    private int roundCount=1;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        runMemoryMonitorStats(primaryStage);
        viewGrid=new ViewGrid(this); 
        Scene scene = new Scene(new StackPane(VIEW_HIGH_SCORE,FANCY_DIALOGUE, new VBox(scoreLabel, viewGrid,
         menuHBox
         )), 800, 600);
        scene.getStylesheets().add(getClass().getResource("/css/main.css").toString());
        String icon = getClass().getResource("/images/211667_a_controller_game_icon.png").toString();
        primaryStage.getIcons().add(new Image(icon));
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            exit(0);
        });
        FANCY_DIALOGUE.getInput("Please Enter Player Name",
                (message) -> {
                    Game.playerName = message != null && message.length()>0 ?message : Game.playerName;
                    updateScoreLabel();
                });
        
        viewGrid.updateView(grid);
        VBox.setMargin(scoreLabel, new Insets(20, 20, 30, 20));      
        menuHBox.setAlignment(Pos.CENTER_LEFT);
        scoreBoardButton.setOnAction(even->VIEW_HIGH_SCORE.showScoreBoard(Game.SCORES, new Score(Game.playerName, Game.score)));
        Game.loadTopScores();


    }
private void updateScoreLabel() {
    scoreLabel.setText(String.format("%s | Round %d/3 | Score: %d", Game.playerName, roundCount ,Game.score));
}
    @Override
    public void clickUpdate(int row, int col) {
        int linkCount = Game.getLinkCount(grid.getBlock(row, col), true);
        if (linkCount >= 3) {
            Game.score += linkCount;
            updateScoreLabel();
        } 
        // remove gaps
        Game.removeGaps(grid); 
        viewGrid.updateView(grid); 
        // check game over
        if (Game.isGameOver(grid)){
            grid=new Grid();
            viewGrid.updateView(grid);
            if (roundCount==3) {
                // call high score board
                roundCount=0;
                Score playerScore=new Score(Game.playerName, Game.score);
                Game.addToScoreBoard(playerScore);
                Game.saveTopScores();
                Game.score=0;
                VIEW_HIGH_SCORE.showScoreBoard(Game.SCORES, playerScore);
             
            } 

            
            roundCount++;
            updateScoreLabel();
        } 
    }



    public static void runMemoryMonitorStats(Stage primaryStage) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        long heapSize = (long) ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())
                                / 1024 / 1024);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                primaryStage.setTitle("Relax Blocks - " + heapSize + "MB / "
                                        + (Runtime.getRuntime().totalMemory() / 1024 / 1024)+ "MB JVM");
                            }
                        });
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }).start();
    } 
}
