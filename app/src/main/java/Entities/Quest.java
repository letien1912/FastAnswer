package Entities;

import android.graphics.Color;

/**
 * Created by Admin on 05/05/2017.
 */

public class Quest {
    String Text;
    String ImageUrl;
    int TextColor;
    QuestType questType;

    public Quest(String text) {
        setTextColor(Color.BLACK);
        Text = text;
    }

    public Quest(String text, int textColor) {
        this(text);
        setTextColor(textColor);
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public void setTextColor(int textColor) {
        TextColor = textColor;
    }

    public int getTextColor() {
        return TextColor;
    }

    public String getText() {
        return Text;
    }
}
