package Entities;

import android.graphics.Color;

/**
 * Created by Admin on 05/05/2017.
 */

public class Ask  extends  Quest{
    public Ask(String text) {
        super(text);
        questType = QuestType.QUESTION;
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
