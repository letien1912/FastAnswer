package game.fastanswer;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import AppDialog.GameOverDialog;
import AppDialog.PauseGameDialog;
import Entities.GameColor;
import Entities.GamePlay;
import Interface.IOnCounterTimerFinish;
import Utilities.CounterTimer;
import Utilities.GameSound;


public class MainActivity extends AppCompatActivity implements IOnCounterTimerFinish {

    private final static String QUESTION_HEADER = "Question ";
    private static final long MAX_TIME = 4000;
    private static final int LEVEL_01 = 1;
    private static final int LEVEL_02 = 2;
    private static final int LEVEL_03 = 3;
    private static final int LEVEL_04 = 4;
    private static final int LEVEL_05 = 5;

    private CounterTimer currentCounter;
    private CounterTimer defaultCounter;
    private IconRoundCornerProgressBar progressBar;

    private TextSwitcher textSwitcherQuestion;
    private ImageSwitcher imageSwitcherQuestion;

    private TextView textAnswer02;
    private TextView textAnswer01;
    private TextView textQuestionHeader;

    private Typeface FontSnapITC;

    private long timeLeft = 0;
    private int index = 0;
    private int defaultTextSize;
    private PauseGameDialog pauseGameDialog;
    private GameOverDialog gameOverDialog;

    private GamePlay gamePlay;
    private int rightColor;

    private GameSound gameSound;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getTypeFaceFromAssert();
        init();
        setUpProgressBar();
        setUpTextQuestion();
        CreateGamePlay();
    }

    private void init() {
        progressBar = (IconRoundCornerProgressBar) findViewById(R.id.progress_2);
        textSwitcherQuestion = (TextSwitcher) findViewById(R.id.textswitcher_question);
        imageSwitcherQuestion = (ImageSwitcher) findViewById(R.id.imageswitcher_question);

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
    }

    private void getTypeFaceFromAssert() {
        FontSnapITC = Typeface.createFromAsset(getAssets(), "fonts/snap_itc.ttf");
    }

    private void setUpProgressBar() {
        progressBar.setProgressColor(Color.parseColor("#EC407A"));
        progressBar.setSecondaryProgressColor(Color.parseColor("#F06292"));
        progressBar.setProgressBackgroundColor(Color.parseColor("#F8BBD0"));
        progressBar.setMax(MAX_TIME);
        progressBar.setIconBackgroundColor(Color.parseColor("#F8BBD0"));
        progressBar.setIconImageResource(R.drawable.ic_clock);
    }

    private void setUpTextQuestion() {
        addAnimation();
        makeView();
        addEvent();
    }

    private void addAnimation() {
        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
        textSwitcherQuestion.setInAnimation(in);
        textSwitcherQuestion.setOutAnimation(out);
        imageSwitcherQuestion.setInAnimation(in);
        imageSwitcherQuestion.setOutAnimation(out);
    }

    private void makeView() {
        textSwitcherQuestion.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textContain = new TextView(MainActivity.this);
                textContain.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
                textContain.setTextSize(getResources().getDimensionPixelSize(R.dimen.answer_text_size));
                textContain.setTypeface(Typeface.SANS_SERIF);
                return textContain;
            }
        });
    }

    private void addEvent() {

    }

    public void CreateGamePlay() {
        initQuestion();
        index = 0;
        progressBar.setMax(GamePlay.MAX_TIME);
        setTheFirstAnswer();
    }

    private void setTheFirstAnswer() {
        NextQuestion();
    }


    public void Answer01(View v) {
        if (MatchAnswer(textAnswer01)) {
            NextQuestion();
        } else {
            ShowGameOverDialog();
        }
    }

    private void ShowGameOverDialog() {
        gameSound.PlayGameOverSound();
        currentCounter.cancel();
        gameOverDialog = new GameOverDialog(this, gamePlay.getScore());
        gameOverDialog.show();
//        if(mInterstitialAd.isLoaded())
//            mInterstitialAd.show();
    }

    public void Answer02(View v) {
        if (MatchAnswer(textAnswer02))
            NextQuestion();
        else {
            ShowGameOverDialog();
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
        int lenQuestion = gamePlay.getListGameQuestion().size();

        rightColor = rd.nextInt(lenColor);
        int indexAnswerColor1 = rd.nextInt(lenColor);
        int indexAnswerColor2 = rd.nextInt(lenColor);

//        int indexColorText = rd.nextInt(lenColor);
        int indexQuestionText = rd.nextInt(lenQuestion);

        int wrongColor;
        do {
            wrongColor = rd.nextInt(lenQuestion);
        } while (rightColor == wrongColor);

        int level = gamePlay.getLv();
        int rightAnswerColor01;
        int rightAnswerColor02;
        switch (level) {
            case LEVEL_01:
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
        SetQuestion(rightColor, indexQuestionText);
        SetAnswer(rightColor, wrongColor, rightAnswerColor01, rightAnswerColor02);
        ++index;
    }

    private void SetTextSize() {
        int rangeRandom = 3;
        Random rd = new Random();
        int rdValue1 = -rangeRandom + rd.nextInt(2*rangeRandom + 2);
        int rdValue2 = -rangeRandom + rd.nextInt(2*rangeRandom + 2);
        textAnswer01.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize + rdValue1);
        textAnswer02.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize + rdValue2);
    }

    private void ResetCounterTimer() {
        currentCounter.cancel();
        currentCounter = new CounterTimer(gamePlay.getTime(), 10, progressBar, this);
        progressBar.setMax(gamePlay.getTime());
        currentCounter.start();
    }

    private void SetQuestion(int indexColor, int indexQuestionText) {
        String Question = gamePlay.getListGameQuestion().get(indexQuestionText);

        textQuestionHeader.setText(QUESTION_HEADER + (index + 1));
        textSwitcherQuestion.setText(Question);
        ((TextView) textSwitcherQuestion.getCurrentView()).setTextColor(gamePlay.getListGameColor().get(indexColor).getColor());
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
    protected void onStart() {
        super.onStart();
        //loadAdMobView();
    }
    private void loadAdMobView() {
//        MobileAds.initialize(getApplicationContext(),
//                "");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("6809ACF1EA2A16A4").build();
        mAdView.loadAd(adRequest);
//        mInterstitialAd = new InterstitialAd(this);
////        mInterstitialAd.setAdUnitId("ca-app-pub-5689571762868774/3432788847");
//        mInterstitialAd.setAdUnitId("ca-app-pub-0664570763252260/1769900428");
//        mInterstitialAd.loadAd(new AdRequest.Builder().build());
//        mInterstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                // Load the next interstitial.
//            }
//        });
    }

    @Override
    protected void onStop() {
        timeLeft = currentCounter.getMillisecondsLeft();
        currentCounter.cancel();
        super.onStop();
    }

    /*Event When Counter timer finished*/
    @Override
    public void onFinished() {
        if (checkDialogIsShowing()) {
            ShowGameOverDialog();
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

    void initQuestion() {
        List<GameColor> gameColors = AddGameColor();
        List<String> gameQuestions = AddQuestion();
        gamePlay = new GamePlay(gameColors, gameQuestions);

//        listQuest = new ArrayList<>();
//        Q1();
//        Q2();
//        Q3();
//        Q4();
//        Q5();
    }

    private List<GameColor> AddGameColor() {
        List<GameColor> gameColors = new ArrayList<>();
        GameColor g = new GameColor(Color.RED, "Đo");
        GameColor g1 = new GameColor(Color.YELLOW, "Vàng");
        GameColor g2 = new GameColor(Color.BLUE, "Xanh Dương");
        GameColor g3 = new GameColor(Color.GREEN, "Xanh Lá");
        GameColor g4 = new GameColor(Color.BLACK, "Đen");
        GameColor g5 = new GameColor(Color.CYAN, "Lục Lam");

        gameColors.add(g);
        gameColors.add(g1);
        gameColors.add(g2);
        gameColors.add(g3);
        gameColors.add(g4);
        gameColors.add(g5);
        return gameColors;
    }

    private List<String> AddQuestion() {
        String[] q = {"Hình Tròn ", "Hình Vuông", "Hình Chữ Nhật", "Hình Tam Giác"};
        return Arrays.asList(q);
    }

}

