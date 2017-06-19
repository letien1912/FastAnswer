package Utilities;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import Entities.GameDrawingShapes;
import Entities.GameShapes;
import Entities.GameWords;
import game.fastanswer.R;


public class GameQuestionSwitcher extends ViewSwitcher {

    private Context mContext;
    private View gameQuestionView;
    private TextView questionTextView;
    private ImageView questionImageView;
    private Typeface font;
    private GameDrawingShapes gameDrawingShapes;

    public GameQuestionSwitcher(Context context) {
        super(context);
        mContext = context;
        launch();
    }

    public GameQuestionSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        launch();
    }

    private void launch() {
        inflate();
        bind();
        init();
    }

    private void init() {
        gameDrawingShapes = new GameDrawingShapes(mContext);
        addAnimation();
    }

    private void addAnimation() {
        setInAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left));
        setOutAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.slide_out_right));
    }

    public void inflate() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        gameQuestionView = inflater.inflate(R.layout.game_question_switcher, this);
    }

    private void bind() {
        questionTextView = (TextView) gameQuestionView.findViewById(R.id.text_switcher_question);
        questionImageView = (ImageView) gameQuestionView.findViewById(R.id.image_switcher_question);
    }

    public void setGameQuestionView(Object gameQuestions, int colorGameResId) {

        Log.d("GameQuestionSwitcher","ColorGameResID = " + colorGameResId);
        if (gameQuestions instanceof GameWords) {
            Log.d("GameQuestionSwitcher","GameWords");
            setUpTextViewQuestion((GameWords) gameQuestions, colorGameResId);
        } else if (gameQuestions instanceof GameShapes) {
            Log.d("GameQuestionSwitcher","GameShapes");
            setUpImageViewQuestion((GameShapes) gameQuestions, colorGameResId);
        } else {
//            throw new Exception("That object is not valid");
        }
        showNext();

    }

    private void setUpTextViewQuestion(GameWords gameQuestion, int colorGameResId) {
        questionTextView.setText(gameQuestion.name());
        questionTextView.setTypeface(font);
        questionTextView.setTextColor(colorGameResId);

        questionTextView.setVisibility(VISIBLE);
        questionImageView.setVisibility(INVISIBLE);

    }

    private void setUpImageViewQuestion(GameShapes gameQuestions, int colorGameResId) {
        questionImageView.setImageDrawable(gameDrawingShapes.Draw(colorGameResId, gameQuestions));

        questionImageView.setVisibility(VISIBLE);
        questionTextView.setVisibility(INVISIBLE);

    }

    public void setFont(Typeface font) {
        this.font = font;
    }
}
