package Utilities;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import Entities.ColorsQuestion;
import Entities.GameDrawingShapes;
import Entities.ShapesQuestion;
import Entities.WordsQuestion;
import game.fastanswer.R;


public class GameQuestionSwitcher extends ViewSwitcher {

    private Context mContext;
    private View gameQuestionView;
    private TextView questionTextView;
    private ImageView questionImageView;
    private Typeface font;
    private GameDrawingShapes gameDrawingShapes;

    private static final String TAG = GameQuestionSwitcher.class.getSimpleName();
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

        Log.d(TAG, "ColorGameResID = " + colorGameResId);
        if (gameQuestions instanceof WordsQuestion) {
            Log.d(TAG, "WordsQuestion");
            setUpTextViewQuestion((WordsQuestion) gameQuestions, colorGameResId);
        } else if (gameQuestions instanceof ShapesQuestion) {
            Log.d(TAG, "ShapesQuestion");
            setUpImageViewQuestion((ShapesQuestion) gameQuestions, colorGameResId);
        } else if (gameQuestions instanceof ColorsQuestion) {
            Log.d(TAG, "ColorsQuestion");
            setUpTextViewQuestion((ColorsQuestion) gameQuestions, colorGameResId);
        } else {
            //            throw new Exception("That object is not valid");
        }
        showNext();

    }

    private void setUpTextViewQuestion(WordsQuestion gameQuestion, int colorGameResId) {
        questionTextView.setText(gameQuestion.name());
        questionTextView.setTypeface(font);
        questionTextView.setTextColor(colorGameResId);

        questionTextView.setVisibility(VISIBLE);
        questionImageView.setVisibility(INVISIBLE);
    }

    private void setUpTextViewQuestion(ColorsQuestion gameQuestion, int colorGameResId) {
        Log.d(TAG, gameQuestion.name());

        questionTextView.setText(gameQuestion.name());
        questionTextView.setTypeface(font);
        questionTextView.setTextColor(colorGameResId);
        questionTextView.setTextSize(mContext.getResources().getDimension(R.dimen.question_text_long_size));

        questionTextView.setVisibility(VISIBLE);
        questionImageView.setVisibility(INVISIBLE);
    }

    private void setUpImageViewQuestion(ShapesQuestion gameQuestions, int colorGameResId) {
        questionImageView.setImageDrawable(gameDrawingShapes.Draw(colorGameResId, gameQuestions));

        questionImageView.setVisibility(VISIBLE);
        questionTextView.setVisibility(INVISIBLE);
    }

    public void setFont(Typeface font) {
        this.font = font;
    }
}
