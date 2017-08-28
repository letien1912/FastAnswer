package Entities;

import java.io.Serializable;

/**
 * Created by Admin on 23/05/2017.
 */

public class GameColor implements Serializable {
    private int Color;
    private String ColorName;

    public GameColor(int color, String colorName) {
        Color = color;
        ColorName = colorName;
    }

    public int getColor() {
        return Color;
    }

    public String getColorName() {
        return ColorName;
    }
}
