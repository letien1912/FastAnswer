package Entities;

import android.graphics.Color;

/**
 * Created by Admin on 05/05/2017.
 */

public class RightAnswer extends  Quest {

    public RightAnswer(String text) {
        super(text);
        questType = QuestType.RIGHT_ANSWER;
    }

    public RightAnswer(String text, int textColor) {
        super(text, textColor);
    }

    @Override
    public void setTextColor(int textColor) {
        super.setTextColor(textColor);
    }

    @Override
    public void setImageUrl(String imageUrl) {
        super.setImageUrl(imageUrl);
    }
}
