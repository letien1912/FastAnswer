package Entities;

import android.util.Log;

import java.util.List;

public class GamePlay {
    public final static long MAX_TIME = 4000;
    private final static long MIN_TIME = 2000;
    private static final long REDUCE_TIME_PER_LEVEL = 500;

    private List<GameColor> listGameColor;
    private List<String> listGameQuestion;
    private int score;
    private int lv;
    private long time;

    public GamePlay(List<GameColor> listGameColor, List<String> listGameQuestion) {
        this.listGameColor = listGameColor;
        this.listGameQuestion = listGameQuestion;
        score = -1;
        lv = 1;
        time = MAX_TIME;
    }

    public List<GameColor> getListGameColor() {
        return listGameColor;
    }

    public List<String> getListGameQuestion() {
        return listGameQuestion;
    }

    public int getScore() {
        return score;
    }

    public int getLv() {
        return lv;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setLv(int lv) {
        this.lv = lv;
    }

    private void nextLevel() {
        ++lv;
    }

    private void UpdateTime() {
        if (score % 5 == 0 && score != 0)
            if (time > MIN_TIME)
                time -= REDUCE_TIME_PER_LEVEL;
    }

    public void nextScore() {
        score++;
        UpdateTime();
        Log.d("TimeTime", time + "");
        switch (score) {
            case 3:
                nextLevel();
                break;
            case 5:
                nextLevel();
                break;
            case 7:
                nextLevel();
                break;
            case 12:
                nextLevel();
                break;
            case 20:
                nextLevel();
                break;

        }
    }

    public long getTime() {
        return time;
    }
}
