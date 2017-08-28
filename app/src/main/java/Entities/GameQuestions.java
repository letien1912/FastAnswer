package Entities;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Admin on 01/06/2017.
 */

public class GameQuestions implements Serializable {
    private List listQuestion;
    private static GameQuestions gameQuestions;
    private static final String TAG = GameQuestions.class.getSimpleName();
    /*
    *
    * */
    private List<ColorsQuestion> colorsQuestions;
    private GameQuestions() {
        List<ShapesQuestion> shapeQuestions = Arrays.asList(ShapesQuestion.values());
        List<WordsQuestion> wordQuestions = Arrays.asList(WordsQuestion.values());
        colorsQuestions = Arrays.asList(ColorsQuestion.values());

        listQuestion = new ArrayList(shapeQuestions);
        listQuestion.addAll(wordQuestions);
        listQuestion.addAll(shapeQuestions);
    }

    public static GameQuestions newInstance() {
        if (gameQuestions == null)
            gameQuestions = new GameQuestions();
        return gameQuestions;
    }

    public List getListQuestion() {
        return listQuestion;
    }

    public List<ColorsQuestion> getColorsQuestions() {
        return colorsQuestions;
    }
}
