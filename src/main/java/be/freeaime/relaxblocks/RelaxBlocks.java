/**
 * @Author: Aimé
 * @Date:   2022-03-24 17:17:20
 * @Last Modified by:   Aimé
 * @Last Modified time: 2022-03-26 14:57:45
 */
package be.freeaime.relaxblocks;

import java.util.Scanner;

import be.freeaime.relaxblocks.logic.Game;
import be.freeaime.relaxblocks.models.Grid;
import be.freeaime.relaxblocks.models.Level;
import be.freeaime.relaxblocks.views.ViewDesktop;
import be.freeaime.relaxblocks.views.ViewMain;

import javafx.application.Platform;

public class RelaxBlocks {

    

    public static void main(String[] args) {

        Game.consoleMode = args.length != 0;
        Game.level = Level.EASY;

        if (Game.consoleMode) {
            Scanner scanner = new Scanner(System.in);

            System.out.println("======== Relax Blocks ===========");
            System.out.println("=================================");
            System.out.println("q to quit");
            System.out.print("Please Enter Player Name: ");
            String line = scanner.nextLine();
            if (!line.equals("q")) {
                Game.playerName = line;
            }
            Grid grid = new Grid();
            // TEST CODE:OK
            // for (int i = 0; i < 5; i++) {
            // grid.getBlock(3, i).setType(3);
            // }
            // MainView.showGrid(grid);
            // getLinkCount(grid.getBlock(3, 0));
            // MainView.showGrid(grid);
            // removeGaps(grid);
            // MainView.showGrid(grid);

            String coordinatesArray[];
            while (!line.equals("q")) {
                ViewMain.showGrid(grid);
                System.out.println("q to quit");
                System.out.println("Please Select xy Coordinates in the form of => 12:5");
                line = scanner.nextLine();
                coordinatesArray = line.split(":");
                // if (line.equals("q")) {
                // break;
                // }
                if (coordinatesArray.length == 2) {
                    // System.out.println(line);
                    try {
                        int rowInput = Integer.parseInt(coordinatesArray[0]);
                        int colInput = Integer.parseInt(coordinatesArray[1]);
                        int linkCount = Game.getLinkCount(grid.getBlock(rowInput, colInput), true);
                        if (linkCount >= 3) {
                            Game.score += linkCount;
                        }
                        ViewMain.showGrid(grid);
                        // remove gaps
                        Game.removeGaps(grid);
                        // check game over
                        if (Game.isGameOver(grid))
                            line = "q";
                        ViewMain.showGrid(grid);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Error => " + line);
                    }
                }
            }
            System.out.println("=================================");
            System.out.println("\nTop 10 High Scores");
            System.out.println();
            System.out.println("===== Thank you for playing =====");
            System.out.printf("Player: %s Score: %d\n", Game.playerName, Game.score);
            System.out.println("=================================");
            scanner.close();
        } else {
            Platform.startup(() -> {
            });
            ViewDesktop.main(args);
        }

    }

   
    

    

   

    

}
