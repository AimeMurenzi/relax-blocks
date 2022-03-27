/**
 * @Author: Aimé
 * @Date:   2022-03-26 18:16:30
 * @Last Modified by:   Aimé
 * @Last Modified time: 2022-03-27 00:56:56
 */
package com.freeaime.relaxblocks.logic;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import com.freeaime.relaxblocks.models.Block;
import com.freeaime.relaxblocks.models.Coordinate;
import com.freeaime.relaxblocks.models.Grid;
import com.freeaime.relaxblocks.models.Level;
import com.freeaime.relaxblocks.models.OneDirection;
import com.freeaime.relaxblocks.models.Score;

public class Game {

    public static boolean consoleMode;
    public static Level level;
    public static int score;
    public static String playerName = "Player 1";
    public static final List<Score> SCORES = new ArrayList<>();
    private static final String scoreSaveLocation = Paths.get(System.getProperty("user.home"), "relaxblocks.txt")
            .toString();
    private static final String scorePlanBSaveLocation = Paths.get(System.getProperty("user.dir"), "relaxblocks.txt")
            .toString();
    public static void addToScoreBoard(Score score){
        SCORES.add(score);
        Collections.sort(SCORES);
    }
    public static void saveTopScores() {
        try (FileOutputStream fileOutputStream = new FileOutputStream(scoreSaveLocation);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);) {
            objectOutputStream.writeObject(SCORES);
        } catch (Exception e) {
            // plan B
            try (FileOutputStream fileOutputStream = new FileOutputStream(scorePlanBSaveLocation);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);) {
                objectOutputStream.writeObject(SCORES);
            } catch (Exception e1) {
            }
        }
    }
    @SuppressWarnings("unchecked")
    public static void loadTopScores() {
        Object scoreObject=null;
        try (FileInputStream fileInputStream = new FileInputStream(scoreSaveLocation);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);) {
            scoreObject = objectInputStream.readObject();
        } catch (Exception e) {
            try (FileInputStream fileInputStream = new FileInputStream(scorePlanBSaveLocation);
                    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);) {
                scoreObject = objectInputStream.readObject();
            } catch (Exception e1) { 
            }
        }
        try {
            if (scoreObject!=null) {
                List<Score> scores= (ArrayList<Score>)scoreObject;
                for (Score score : scores) {
                    SCORES.add(score);
                }
                Collections.sort(SCORES);
            }

        } catch (Exception e) { 
        }
       
    }

    public static void openScoreList() {

    }

    public static boolean isGameOver(Grid grid) {
        for (int row = 0; row < Grid.GRID_ROWS; row++) {
            for (int col = 0; col < Grid.GRID_COLS; col++) {
                int linkCount = getLinkCount(grid.getBlock(row, col));
                if (linkCount >= 3) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void removeGaps(Grid grid) {
        for (int col = 0; col < Grid.GRID_COLS; col++) {
            // sorting logic
            // inspiration voor this algorithm
            // this is modified version of a sorting algo I found here
            // https://www.javatpoint.com/how-to-sort-an-array-in-java
            //
            for (int row = 0; row < Grid.GRID_ROWS; row++) {
                Block block = grid.getBlock(row, col);
                if (block.getType() == 0) {// empty
                    int currentRow = row;
                    for (int previousRow = row - 1; previousRow >= 0; previousRow--) {
                        // Block currentBlock = grid.getBlock(currentRow, col);
                        Block blockBefore = grid.getBlock(previousRow, col);
                        // int tmpType = 0;
                        // int currentType = currentBlock.getType();//0
                        int previousType = blockBefore.getType();
                        if (previousType > 0) {
                            Block currentBlock = grid.getBlock(currentRow, col);
                            currentBlock.setType(previousType);
                            blockBefore.setType(0);
                            // tmpType = currentType;
                            // currentType=previousType;
                        }
                        currentRow--;
                    }
                }
            }
        }
    }

    private static void normalModeTraverse(Block block, List<Block> linkedBlocks) {

        // you could use normal recursion but i avoid it because it has stackoverflow
        // issues
        // this app does have enough recursions to cause a stackoverflow
        // but i'll still avoid it anyway and opt to use Depth First Search algorithm

        Stack<Block> blockStack = new Stack<>();
        blockStack.push(block);
        // why HashSet contains has O(1) search performance
        Set<Coordinate> visitedCoordinates = new HashSet<>();
        while (!blockStack.empty()) {
            Block currentBlock = blockStack.pop();
            if (currentBlock == null) {
                continue;
            }
            Block leftBlock = currentBlock.getLeftBlock();
            Block rightBlock = currentBlock.getRightBlock();
            Block topBlock = currentBlock.getTopBlock();
            Block bottomBlock = currentBlock.getBottomBlock();

            checkForIdenticalLinks(linkedBlocks, blockStack, visitedCoordinates, currentBlock, leftBlock);
            checkForIdenticalLinks(linkedBlocks, blockStack, visitedCoordinates, currentBlock, rightBlock);
            checkForIdenticalLinks(linkedBlocks, blockStack, visitedCoordinates, currentBlock, topBlock);
            checkForIdenticalLinks(linkedBlocks, blockStack, visitedCoordinates, currentBlock, bottomBlock);
            currentBlock = null;
        }

    }

    private static void checkForIdenticalLinks(List<Block> linkedBlocks, Stack<Block> blockStack,
            Set<Coordinate> visitedCoordinates,
            Block currentBlock, Block linkBlock) {
        if (currentBlock.equals(linkBlock)
                && !visitedCoordinates.contains(linkBlock.getCoordinate())) {
            linkedBlocks.add(linkBlock);
            blockStack.push(linkBlock);
            visitedCoordinates.add(linkBlock.getCoordinate());

        }
    }

    /**
     * 
     * @param block
     * @return number of links found
     */
    private static int getLinkCount(Block block) {
        return getLinkCount(block, false);
    }

    /**
     * 
     * @param block
     * @param deleteLinks if true then delete links with >=3 of the same
     * @return number of links found
     */
    public static int getLinkCount(Block block, boolean deleteLinks) {
        if (block.isEmpty()) {
            return 0;
        }
        // arraylist for keeping same linked block
        // for later removal if there are 3 or more;
        List<Block> linkedBlocks = new ArrayList<>();
        switch (level) {
            case EASY:
                normalModeTraverse(block, linkedBlocks);
                break;
            case NORMAL:
                normalModeTraverse(block, linkedBlocks);
                break;
            case HARD:
                normalModeTraverse(block, linkedBlocks);
                break;
            case NIGHTMARE:
                normalModeTraverse(block, linkedBlocks);
                break;
            case DARKSOULS:
                traverseBlocks(block, linkedBlocks, OneDirection.LEFT);
                traverseBlocks(block, linkedBlocks, OneDirection.RIGHT);
                traverseBlocks(block, linkedBlocks, OneDirection.UP);
                traverseBlocks(block, linkedBlocks, OneDirection.DOWN);
                break;
            default:
                break;
        }

        if (deleteLinks && linkedBlocks.size() >= 3) {
            for (Block linkBlock : linkedBlocks) {
                linkBlock.delete();
            }
        }
        return linkedBlocks.size();
    }

    private static void traverseBlocks(Block block, List<Block> linkedBlocks, OneDirection direction) {
        // one could use recursion but i avoid it because it has stackoverflow issues
        // this app does not have enough recursions to cause a stackoverflow
        // but i'll still avoid it anyway and opt to use Depth First Search algorithm

        Stack<Block> blockStack = new Stack<>();
        blockStack.push(block);
        while (!blockStack.empty()) {
            Block currentBlock = blockStack.pop();
            if (currentBlock == null) {
                continue;
            }
            Block nextBlock = null;
            switch (direction) {
                case LEFT:
                    nextBlock = currentBlock.getLeftBlock();
                    break;
                case RIGHT:
                    nextBlock = currentBlock.getRightBlock();
                    break;
                case UP:
                    nextBlock = currentBlock.getTopBlock();
                    break;
                case DOWN:
                    nextBlock = currentBlock.getBottomBlock();
                    break;
                default:
                    break;
            }
            if (currentBlock.equals(nextBlock)) {
                linkedBlocks.add(nextBlock);
                blockStack.push(nextBlock);
            }
        }
    }

}
