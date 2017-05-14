package Entities;

import android.graphics.Color;

/**
 * Created by Admin on 05/05/2017.
 */

public class Question {
    private Ask Question;
    private RightAnswer RightAnswer;
    private WrongAnswer WrongAnswer;

    public Question(Ask question, RightAnswer rightAnswer, WrongAnswer wrongAnswer) {
        Question = question;
        WrongAnswer = wrongAnswer;
        RightAnswer = rightAnswer;
    }

    public Ask getQuestion() {
        return Question;
    }

    public Entities.RightAnswer getRightAnswer() {
        return RightAnswer;
    }

    public Entities.WrongAnswer getWrongAnswer() {
        return WrongAnswer;
    }
}
