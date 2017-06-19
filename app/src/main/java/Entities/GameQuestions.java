package Entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Admin on 01/06/2017.
 */

public class GameQuestions {
    private List listQuestion;
    private static GameQuestions gameQuestions;

    private GameQuestions() {
        List<GameShapes> gameShapes = Arrays.asList(GameShapes.values());
        List<GameWords> gameWords = Arrays.asList(GameWords.values());

        listQuestion = new ArrayList(gameShapes);
        listQuestion.addAll(gameWords);
        listQuestion.addAll(gameShapes);
    }

    public static GameQuestions newInstance() {
        if (gameQuestions == null)
            gameQuestions = new GameQuestions();
        return gameQuestions;
    }

    public List getListQuestion() {
        return listQuestion;
    }
}
