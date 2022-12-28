package com.example.cmpt276assignmen3.GameLogic;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to manage and store all high scores and
 * number of games started by the user.
 */

public class GameManager {

    private static GameManager instance;
    private List<Game> games = new ArrayList<>();
    public List<List<Integer>> highScores = new ArrayList<>();
    public int numGamesStarted = 0;

    private GameManager(){
    }

    public static GameManager getInstance(){
        if (instance == null){
            instance = new GameManager();
        }
        return instance;
    }

    public void add(Game game){
        games.add(game);
    }

    public int retriveHighScore(int numRow, int numBuns){
        int min = 20000730;
        if (highScores.isEmpty()){
            min=0;
        }else{
            for (int i=0; i<highScores.size(); i++){
                if (highScores.get(i).get(0) == numRow &&
                        highScores.get(i).get(1) == numBuns) {
                    if (highScores.get(i).get(2) <= min){
                        min = highScores.get(i).get(2);
                    }
                }
            }
        }
        if (min==20000730){
            min = 0;
        }
        return min;

    }

}
