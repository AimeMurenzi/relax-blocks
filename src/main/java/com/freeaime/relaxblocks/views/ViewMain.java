/**
 * @Author: Aimé
 * @Date:   2022-03-24 20:18:24
 * @Last Modified by:   Aimé
 * @Last Modified time: 2022-03-26 21:51:40
 */
package com.freeaime.relaxblocks.views;

import com.freeaime.relaxblocks.logic.Game;
import com.freeaime.relaxblocks.models.Block;
import com.freeaime.relaxblocks.models.Grid;

public class ViewMain {
    private static void consoleDisplay(Grid grid) {
        drawLine();
        drawTopXLegend();
        for (int i = 0; i < Grid.GRID_ROWS; i++) {
            print(Integer.toString(i));// y side numbers
            for (int j = 0; j < Grid.GRID_COLS; j++) {
                Block block = grid.getBlock(i, j);
                if (block == null) {
                    System.out.println(" ");
                } else {
                    switch (block.getType()) {
                        case 0:
                            print("_");
                            break;
                        case 1:
                            print("O");
                            break;
                        case 2:
                            print("+");
                            break;
                        case 3:
                            print("X");
                            break;
                        default:
                            print(" ");
                            break;
                    }
                }
            }
            System.out.print("\n");
        }
        drawLine();
        System.out.println("Player: "+ Game.playerName+ " Score: "+Game.score);

    }

    private static void drawTopXLegend() {
        print(" ");
        for (int i = 0; i < Grid.GRID_COLS; i++) {
            print(Integer.toString(i));
        }
        System.out.print("\n");
    }

    private static void drawLine() {
        for (int i = 0; i < Grid.GRID_COLS + 2; i++) {
            print("=");
        }
        System.out.print("\n");
    }

    private static void print(String block) {
        System.out.printf("%3s", block);
    }

    public static void showGrid(Grid grid) {
        if (Game.consoleMode)
            consoleDisplay(grid);
    }

}
