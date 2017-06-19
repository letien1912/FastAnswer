package game.fastanswer;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import AppDialog.GameOverDialog;
import AppDialog.PauseGameDialog;
import Entities.GameColor;
import Entities.GamePlay;
import Entities.GameQuestions;
import Interface.IOnCounterTimerFinish;
import Utilities.CounterTimer;
import Utilities.GameQuestionSwitcher;
import Utilities.GameSound;


public class MainActivity extends AppCompatActivity implements IOnCounterTimerFinish {

    private final static String QUESTION_HEADER = "Question ";
    private static final long MAX_TIME = 4000;
    private static final int LEVEL_01 = 1;
    private static final int LEVEL_02 = 2;
    private static final int LEVEL_03 = 3;
    private static final int LEVEL_04 = 4;
    private static final int LEVEL_05 = 5;
    private static final int SIZE_TEXT_RANDOM_RANGE = 8;

    private CounterTimer currentCounter;
    private CounterTimer defaultCounter;
    private IconRoundCornerProgressBar progressBar;

    private GameQuestionSwitcher gameQuestionSwitcher;

    private TextView textAnswer02;
    private TextView textAnswer01;
    private TextView textQuestionHeader;

    private Typeface FontSnapITC;

    private int index = 0;
    private int defaultTextSize;
    private PauseGameDialog pauseGameDialog;
    private GameOverDialog gameOverDialog;

    private GamePlay gamePlay;
    private int rightColor;

    private GameSound gameSound;
    private InterstitialAd mInterstitialAd;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getTypeFaceFromAssert();
        init();
        loadAdMobView();
        setUpProgressBar();
        CreateGamePlay();
    }

    private void init() {
        progressBar = (IconRoundCornerProgressBar) findViewById(R.id.progress_2);
        gameQuestionSwitcher = (GameQuestionSwitcher) findViewById(R.id.game_switcher_question);

        textQuestionHeader = (TextView) findViewById(R.id.text_question_header);
        textAnswer01 = (TextView) findViewById(R.id.text_answer01);
        textAnswer02 = (TextView) findViewById(R.id.text_answer02);
        setTextTypeFace();

        defaultCounter = new CounterTimer(MAX_TIME, 10, progressBar, this);
        currentCounter = defaultCounter;

        pauseGameDialog = new PauseGameDialog(this, currentCounter, progressBar);
        gameOverDialog = new GameOverDialog(this, 0);

        gameSound = new GameSound(this);

        defaultTextSize = getResources().getDimensionPixelSize(R.dimen.answer_text_size);
    }

    private void setTextTypeFace() {
        textAnswer01.setTypeface(FontSnapITC);
        textAnswer02.setTypeface(FontSnapITC);
        textQuestionHeader.setTypeface(FontSnapITC);
        gameQuestionSwitcher.setFont(FontSnapITC);
    }

    private void getTypeFaceFromAssert() {
        FontSnapITC = Typeface.createFromAsset(getAssets(), "fonts/snap_itc.ttf");
    }

    private void setUpProgressBar() {
        progressBar.setProgressColor(Color.parseColor("#EC407A"));
        progressBar.setSecondaryProgressColor(Color.parseColor("#F06292"));
        progressBar.setProgressBackgroundColor(Color.parseColor("#F8BBD0"));
        progressBar.setMax(MAX_TIME);
        progressBar.setIconBackgroundColor(Color.parseColor("#E91E63"));
        progressBar.setIconImageResource(R.drawable.ic_clock);
    }


    private void loadAdMobView() {
        MobileAds.initialize(getApplicationContext(),
                "ca-app-pub-5794865694837942~5235770314");
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.adView);
        mAdView = new AdView(getApplicationContext());
        mAdView.setAdSize(AdSize.SMART_BANNER);
        mAdView.setAdUnitId("ca-app-pub-5794865694837942/8189236713");
        rl.addView(mAdView);

        AdRequest adRequest = new AdRequest.Builder().addTestDevice("ABF39522DBD56E4B15D7A6D9D6FF6547").build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-5794865694837942/3479835513");
        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("ABF39522DBD56E4B15D7A6D9D6FF6547").build());
    }

    public void CreateGamePlay() {
        initQuestion();
        setDefaultSomeThing();
        setTheFirstAnswer();
    }

    private void setDefaultSomeThing() {
        index = 0;
        progressBar.setMax(GamePlay.MAX_TIME);
        textAnswer01.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize);
        textAnswer02.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize);
    }

    private void setTheFirstAnswer() {
        NextQuestion();
    }

    public void Answer01(View v) {
        if (MatchAnswer(textAnswer01)) {
            NextQuestion();
        } else {
            ShowGameOverDialogIfPossible();
        }
    }

    private void ShowGameOverDialogIfPossible() {
        if (!gameOverDialog.isShowing()) {
            gameSound.PlayGameOverSound();
            ShowGameDialog();
        }
    }

    private void ShowGameDialog() {
        StopCounter();
        gameOverDialog = new GameOverDialog(this, gamePlay.getScore());
        gameOverDialog.show();
        AddAdMob();
    }

    private void StopCounter() {
        currentCounter.cancel();
    }
    private void AddAdMob() {
        if (mInterstitialAd.isLoaded())
            mInterstitialAd.show();
    }

    public void Answer02(View v) {
        if (MatchAnswer(textAnswer02))
            NextQuestion();
        else {
            ShowGameOverDialogIfPossible();
        }
    }

    private boolean MatchAnswer(TextView textAnswer) {
        return textAnswer.getText().equals(gamePlay.getListGameColor().get(rightColor).getColorName());
    }

    private void NextQuestion() {
        gameSound.PlayNextQuestionSound();

        gamePlay.nextScore();
        Random rd = new Random();

        int lenColor = gamePlay.getListGameColor().size();
        int lenQuestion = gamePlay.getGameQuestion().getListQuestion().size();

        rightColor = rd.nextInt(lenColor);
        int indexAnswerColor1 = rd.nextInt(lenColor);
        int indexAnswerColor2 = rd.nextInt(lenColor);

        int indexQuestion = rd.nextInt(lenQuestion);

        int wrongColor;
        do {
            wrongColor = rd.nextInt(lenColor);
        } while (rightColor == wrongColor);

        int rightAnswerColor01;
        int rightAnswerColor02;
        switch (gamePlay.getLv()) {
            case LEVEL_01:
                Log.d("TestTextSize", textAnswer01.getTextSize() + "");
                rightAnswerColor01 = rightColor;
                rightAnswerColor02 = wrongColor;
                Log.d("LEVEL", "level = 1");
                break;
            case LEVEL_02:
                rightAnswerColor01 = indexAnswerColor1;
                rightAnswerColor02 = indexAnswerColor1;
                Log.d("LEVEL", "level = 2");
                break;
            case LEVEL_03:
                rightAnswerColor01 = indexAnswerColor1;
                rightAnswerColor02 = indexAnswerColor2;
                Log.d("LEVEL", "level = 3");
                break;
            case LEVEL_04:
                rightAnswerColor01 = wrongColor;
                rightAnswerColor02 = rightColor;
                Log.d("LEVEL", "level = 4");
                break;
            case LEVEL_05:
                int random = rd.nextInt(10);
                if (random < 7) {
                    rightAnswerColor01 = indexAnswerColor1;
                    rightAnswerColor02 = indexAnswerColor2;
                } else {
                    rightAnswerColor01 = indexAnswerColor2;
                    rightAnswerColor02 = indexAnswerColor1;
                }
                Log.d("LEVEL", "level = 5");
                break;
            default:
                random = rd.nextInt(10);
                if (random <= 7) {
                    rightAnswerColor01 = indexAnswerColor1;
                    rightAnswerColor02 = indexAnswerColor2;
                } else {
                    rightAnswerColor01 = indexAnswerColor2;
                    rightAnswerColor02 = indexAnswerColor1;
                }
                SetTextSize();
                break;
        }

        ResetCounterTimer();
        SetQuestion(rightColor, indexQuestion);
        SetAnswer(rightColor, wrongColor, rightAnswerColor01, rightAnswerColor02);
        ++index;
    }

    private void SetTextSize() {
        int rangeRandom = SIZE_TEXT_RANDOM_RANGE;
        Random rd = new Random();
        int rdValue1 = -rangeRandom + rd.nextInt(2 * rangeRandom + 2);
        int rdValue2 = -rangeRandom + rd.nextInt(2 * rangeRandom + 2);
        textAnswer01.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize + rdValue1);
        textAnswer02.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize + rdValue2);
        Log.d("TestTextSize", "----------------------------" + defaultTextSize);
        Log.d("TestTextSize 1", (rdValue1) + "-" + (defaultTextSize + rdValue1));
        Log.d("TestTextSize 2", (rdValue2) + "-" + (defaultTextSize + rdValue2));
    }

    private void ResetCounterTimer() {
        currentCounter.cancel();
        currentCounter = new CounterTimer(gamePlay.getTime(), 10, progressBar, this);
        progressBar.setMax(gamePlay.getTime());
        currentCounter.start();
    }

    private void SetQuestion(int indexColor, int indexQuestion) {

        GameQuestions questionShape = gamePlay.getGameQuestion();
        Object gameQuestion = questionShape.getListQuestion().get(indexQuestion);
        int colorResID = gamePlay.getListGameColor().get(indexColor).getColor();

        textQuestionHeader.setText(QUESTION_HEADER + (index + 1));
        Log.d("GameQuestionSwitcher", "Question " + (index + 1));
        gameQuestionSwitcher.setGameQuestionView(gameQuestion, colorResID);
    }

    private void SetAnswer(int indexColor, int indexWrongColor, int rightAnswerColor01, int rightAnswerColor02) {

        GameColor rightAnswerGameColorText = gamePlay.getListGameColor().get(indexColor);
        GameColor rightAnswerGameColor01 = gamePlay.getListGameColor().get(rightAnswerColor01);
        GameColor rightAnswerGameColor02 = gamePlay.getListGameColor().get(rightAnswerColor02);
        GameColor wrongAnswerGameColor = gamePlay.getListGameColor().get(indexWrongColor);

        String rightAnswer = rightAnswerGameColorText.getColorName();
        String wrongAnswer = wrongAnswerGameColor.getColorName();

        int rightColor = rightAnswerGameColor01.getColor();
        int wrongColor = rightAnswerGameColor02.getColor();

        String[] asw = {rightAnswer, wrongAnswer};
        int[] color = {rightColor, wrongColor};

        setTextContent(asw, color);
    }

    private void setTextContent(String[] asw, int[] color) {
        Random rd = new Random();
        int rdNumber = rd.nextInt(2);
        textAnswer01.setText(asw[rdNumber]);
        textAnswer02.setText(asw[1 - rdNumber]);

        textAnswer01.setTextColor(color[rdNumber]);
        textAnswer02.setTextColor(color[1 - rdNumber]);
    }

    @Override
    protected void onStop() {
        currentCounter.cancel();
        super.onStop();
    }

    /*Event When Counter timer finished*/
    @Override
    public void onFinished() {
        if (checkDialogIsShowing()) {
            ShowGameOverDialogIfPossible();
        }
    }

    private boolean checkDialogIsShowing() {
        return !(pauseGameDialog.isShowing() || gameOverDialog.isShowing());
    }

    @Override
    public void onBackPressed() {
        ShowPauseGameDialog();
    }

    private void ShowPauseGameDialog() {
        if (checkDialogIsShowing()) {
            pauseGameDialog = new PauseGameDialog(this, currentCounter, progressBar);
            pauseGameDialog.show();
        }
    }

    public void setCurrentCounter(CounterTimer currentCounter) {
        this.currentCounter = currentCounter;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ShowPauseGameDialog();
        progressBar.setProgress(currentCounter.getMillisecondsLeft());

    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdView.resume();
    }

    @Override
    protected void onDestroy() {
        mAdView.destroy();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        mAdView.pause();
        super.onPause();
    }

    void initQuestion() {
        List<GameColor> gameColors = AddGameColor();
        GameQuestions gameQuestions = AddQuestion();
        gamePlay = new GamePlay(gameColors, gameQuestions);

    }

    private List<GameColor> AddGameColor() {
        List<GameColor> gameColors = new ArrayList<>();
        GameColor g = new GameColor(Color.RED, "RED");
        GameColor g1 = new GameColor(Color.YELLOW, "YELLOW");
        GameColor g2 = new GameColor(Color.BLUE, "BLUE");
        GameColor g3 = new GameColor(Color.GREEN, "GREEN");
        GameColor g4 = new GameColor(Color.BLACK, "BLACK");
        GameColor g5 = new GameColor(Color.CYAN, "CYAN");

        gameColors.add(g);
        gameColors.add(g1);
        gameColors.add(g2);
        gameColors.add(g3);
        gameColors.add(g4);
        gameColors.add(g5);
        return gameColors;
    }

    private GameQuestions AddQuestion() {
        return GameQuestions.newInstance();
    }
}

