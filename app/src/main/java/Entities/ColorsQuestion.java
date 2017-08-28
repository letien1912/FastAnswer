package Entities;

/**
 * Created by lldtien on 8/22/2017.
 */

public enum ColorsQuestion {
    RED("#FF0000"), YELLOW("#FFFF00"), BLUE("#0000FF"), GREEN("#008000"), BLACK("#000000"), CYAN("#00FFFF"), PINK("#FF00FF"), ORANGE("#FFA500"), PURPLE("#800080"), LIME("#00FF00");



    private String colorCode;

    ColorsQuestion(String colorCode){
        this.colorCode = colorCode;
    }

    public String getColorCode() {
        return colorCode;
    }
}
