/**
 * @Author: Aimé
 * @Date:   2022-03-24 17:31:50
 * @Last Modified by:   Aimé
 * @Last Modified time: 2022-03-26 13:06:21
 */
package com.freeaime.relaxblocks.models;

import java.util.Random;

public class Grid {

    public static final int GRID_COLS = 15;
    public static final int GRID_ROWS = 15;
    // why are [GRID_HEIGHT][GRID_WIDTH] instead of [GRID_WIDTH][GRID_HEIGHT]
    // because of the way nested for loop works. this makes i much easier to
    // visualize. remember this reason while reading code ;)
    private Block[][] gridArray = new Block[GRID_ROWS][GRID_COLS];

    private int amountUniqueBlock;

    public Grid() {
        this.amountUniqueBlock = 3;
        initGrid(); 
    }

    private void assignBlockNeighbors() {
        for (int row = 0; row < GRID_ROWS; row++) {
            for (int col = 0; col < GRID_COLS; col++) {
                Block leftBlock = getLeftBlock(gridArray, row, col);
                Block rightBlock = getRightBlock(gridArray, row, col);
                Block topBlock = getTopBlock(gridArray, row, col);
                Block bottomBlock = getBottomBlock(gridArray, row, col);
                //
                gridArray[row][col].setLeftBlock(leftBlock);
                gridArray[row][col].setRightBlock(rightBlock);
                gridArray[row][col].setTopBlock(topBlock);
                gridArray[row][col].setBottomBlock(bottomBlock);

            }
        }
    }

    private void initGrid() {
        Random random = new Random();
        for (int row = 0; row < GRID_ROWS; row++) {
            for (int col = 0; col < GRID_COLS; col++) {
                int type = random.nextInt(amountUniqueBlock) + 1;
                gridArray[row][col] = new Block(type,new Coordinate(row, col));
            }
        }
        assignBlockNeighbors();
    }

    private Block getLeftBlock(Block[][] gridArray, int row, int col) {
        int leftBlockIndex = col - 1;
        return leftBlockIndex < 0 ? null : gridArray[row][leftBlockIndex];
    }

    private Block getRightBlock(Block[][] gridArray, int row, int col) {
        int rightBlockIndex = col + 1;
        return rightBlockIndex< GRID_COLS ? gridArray[row][rightBlockIndex] :null ;
    }

    private Block getTopBlock(Block[][] gridArray, int row, int col) {
        int topBlockIndex = row - 1;
        return topBlockIndex < 0 ? null : gridArray[topBlockIndex][col];
    }

    private Block getBottomBlock(Block[][] gridArray, int row, int col) {
        int bottomBlockIndex = row + 1;
        return bottomBlockIndex < GRID_ROWS ? gridArray[bottomBlockIndex][col]:null;
    }

    public Block[][] getGridArray() {
        return gridArray;
    }

    public Block getBlock(int row, int col) {
        boolean widthIndexCheck = (row >= 0) && (row < GRID_ROWS);
        boolean heightIndexCheck = (col >= 0) && (col < GRID_COLS);
        if (widthIndexCheck && heightIndexCheck) {
            return gridArray[row][col];
        }
        return null;
    } 
}
