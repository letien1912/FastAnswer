package Entities;

import android.graphics.Color;

/**
 * Created by Admin on 05/05/2017.
 */

public class WrongAnswer extends  Quest {
    public WrongAnswer(String text) {
        super(text);
        questType = QuestType.WRONG_ANSWER;
    }

    public WrongAnswer(String text, int textColor) {
        super(text, textColor);
    }

    @Override
    public void setImageUrl(String imageUrl) {
        super.setImageUrl(imageUrl);
    }

    @Override
    public void setTextColor(int textColor) {
        super.setTextColor(textColor);
    }
}
