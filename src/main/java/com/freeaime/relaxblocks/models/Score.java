/**
 * @Author: Aimé
 * @Date:   2022-03-26 22:39:59
 * @Last Modified by:   Aimé
 * @Last Modified time: 2022-03-27 00:55:35
 */
package com.freeaime.relaxblocks.models;

import java.io.Serializable;

public class Score implements Comparable<Score>, Serializable{

    private String playerName;
    private int score;
    
    public Score(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    } 

    @Override
    public int compareTo(Score other) { 
        //reversed for top ten
        return other.score - this.score;
    }
}
