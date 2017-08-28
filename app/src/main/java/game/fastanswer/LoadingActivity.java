package game.fastanswer;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import Entities.ColorsQuestion;
import Entities.GameColor;
import Entities.GamePlay;
import Entities.GameQuestions;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        new Thread() {
            @Override
            public void run() {
                try {
                    Intent it = new Intent(getApplicationContext(), StartActivity.class);
                    it.putExtra(getString(R.string.GAMEPLAY_CONTENT), initQuestion());
                    sleep(1000);
                    startActivity(it);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private GamePlay initQuestion() {
        List<GameColor> gameColors = AddGameColor();
        GameQuestions gameQuestions = AddQuestion();
        return new GamePlay(gameColors, gameQuestions);
    }

    private List<GameColor> AddGameColor() {
        List<GameColor> gameColors = new ArrayList<>();
        for (ColorsQuestion q : ColorsQuestion.values())
            gameColors.add(new GameColor(Color.parseColor(q.getColorCode()), q.name()));
        return gameColors;
    }

    private GameQuestions AddQuestion() {
        return GameQuestions.newInstance();
    }
}
