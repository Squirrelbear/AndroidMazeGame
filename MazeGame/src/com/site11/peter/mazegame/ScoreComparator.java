package com.site11.peter.mazegame;

import java.util.Comparator;

public class ScoreComparator implements Comparator<HighScore>{
 
    @Override
    public int compare(HighScore score1, HighScore score2) {
 
        float rank1 = score1.getScore();
        float rank2 = score2.getScore();
 
        if (rank1 > rank2){
            return +1;
        }else if (rank1 < rank2){
            return -1;
        }else{
            return 0;
        }
    }
}
