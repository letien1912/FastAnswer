package game.fastanswer;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import AppDialog.GameOverDialog;
import AppDialog.PauseGameDialog;
import Entities.Ask;
import Entities.Quest;
import Entities.Question;
import Entities.RightAnswer;
import Entities.WrongAnswer;


public class MainActivity extends AppCompatActivity {

    private final static String QUESTION_HEADER = "Question ";

    private CounterTimer counter;
    private IconRoundCornerProgressBar progressBar;
    private TextSwitcher textSwitcherQuestion;
    private ImageSwitcher imageSwitcherQuestion;

    private TextView textAnswer02;
    private TextView textAnswer01;
    private TextView textQuestionHeader;

    private Typeface FontSnapITC;
    private Typeface FontShowG;

    private GameOverDialog gameOverDialog;

    private long timeLeft = 0;
    private int index = 0;
    private ArrayList<Question> listQuest;
    private PauseGameDialog pauseGameDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getTypeFaceFromAssert();
        init();
        initQuestion();
        setUpProgressBar();
        setUpTextQuestion();

    }

    private void init() {
        progressBar = (IconRoundCornerProgressBar) findViewById(R.id.progress_2);
        textSwitcherQuestion = (TextSwitcher) findViewById(R.id.textswitcher_question);
        imageSwitcherQuestion = (ImageSwitcher) findViewById(R.id.imageswitcher_question);

        textQuestionHeader = (TextView) findViewById(R.id.text_question_header);
        textAnswer01 = (TextView) findViewById(R.id.text_answer01);
        textAnswer02 = (TextView) findViewById(R.id.text_answer02);
        setTextTypeFace();

        counter = new CounterTimer(4000, 10, progressBar);
        gameOverDialog = new GameOverDialog(this, counter, progressBar);
        pauseGameDialog = new PauseGameDialog(this,counter,progressBar);
    }

    private void setTextTypeFace() {
        textAnswer01.setTypeface(FontSnapITC);
        textAnswer02.setTypeface(FontSnapITC);
        textQuestionHeader.setTypeface(FontSnapITC);
    }

    private void getTypeFaceFromAssert() {
        FontSnapITC = Typeface.createFromAsset(getAssets(), "fonts/snap_itc.ttf");
        FontShowG = Typeface.createFromAsset(getAssets(), "fonts/show_g.ttf");
    }

    private void setUpProgressBar() {
        progressBar.setProgressColor(Color.parseColor("#EC407A"));
        progressBar.setSecondaryProgressColor(Color.parseColor("#F06292"));
        progressBar.setProgressBackgroundColor(Color.parseColor("#F8BBD0"));
        progressBar.setMax(4000f);
        progressBar.setProgress(50f);
        progressBar.setIconBackgroundColor(Color.parseColor("#F8BBD0"));
        progressBar.setIconImageResource(R.mipmap.ic_launcher);
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

    @Override
    protected void onPause() {
        Log.d("TAG", "on onPause");
        super.onPause();
    }

    @Override
    protected void onRestart() {
        gameOverDialog.show();
        Log.d("TAG", "on onRestart");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        progressBar.setProgress(timeLeft);
        Log.d("TAG", "on onResume");
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        Log.d("TAG", "on onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        timeLeft = counter.getMillisecondsLeft();
        counter.cancel();
        Log.d("TAG", "on onStop");
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        pauseGameDialog.show();
    }

    public void Answer01(View v) {
        NextQuestion();
    }

    public void Answer02(View v) {
        NextQuestion();
    }


    private void NextQuestion() {
            counter.cancel();
        counter.start();
        SetQuestion();
        SetAnswer();
        index = index == listQuest.size() ? 0 : index + 1;
    }

    private void SetQuestion() {
        String Question = listQuest.get(index).getQuestion().getText();
        textQuestionHeader.setText(QUESTION_HEADER + index + 1);
        textSwitcherQuestion.setText(Question);
    }

    private void SetAnswer() {
        Question answer = listQuest.get(index);
        String rightAnswer = answer.getRightAnswer().getText();
        String wrongAnswer = answer.getWrongAnswer().getText();

        int rightColor = answer.getRightAnswer().getTextColor();
        int wrongColor = answer.getWrongAnswer().getTextColor();

        String[] asw = {rightAnswer, wrongAnswer};
        int[] color = {rightColor, wrongColor};

        setTextContent(asw, color);
    }

    private void setTextContent(String[] asw, int[] color) {
        Random rd = new Random();
        int rdNumber = rd.nextInt(1);
        textAnswer01.setText(asw[rdNumber]);
        textAnswer02.setText(asw[1 - rdNumber]);

        textAnswer01.setTextColor(color[rdNumber]);
        textAnswer02.setTextColor(color[1 - rdNumber]);
    }


    void initQuestion() {
        listQuest = new ArrayList<Question>();
        Q1();
        Q2();
        Q3();
        Q4();
        Q5();
    }

    void Q1() {
        RightAnswer r = new RightAnswer("Red");
        WrongAnswer w = new WrongAnswer("Green");
        Ask a = new Ask("Hình Tròn Màu Đỏ");
        Question q = new Question(a, r, w);
        listQuest.add(q);
    }

    void Q2() {
        RightAnswer r = new RightAnswer("Red", Color.DKGRAY);
        WrongAnswer w = new WrongAnswer("Green", Color.YELLOW);
        Ask a = new Ask("Hình Vuong Màu Xanh");
        Question q = new Question(a, r, w);
        listQuest.add(q);
    }

    void Q3() {
        RightAnswer r = new RightAnswer("Red", Color.YELLOW);
        WrongAnswer w = new WrongAnswer("Green", Color.YELLOW);
        Ask a = new Ask("Hình R Màu Xanh");
        Question q = new Question(a, r, w);
        listQuest.add(q);
    }

    void Q4() {
        WrongAnswer w = new WrongAnswer("Red", Color.RED);
        RightAnswer r = new RightAnswer("Green", Color.GREEN);
        Ask a = new Ask("Hình tam giác Màu Xanh");
        Question q = new Question(a, r, w);
        listQuest.add(q);
    }

    void Q5() {
        RightAnswer r = new RightAnswer("Red", Color.RED);
        WrongAnswer w = new WrongAnswer("Green", Color.GREEN);
        Ask a = new Ask("Hình Học Màu Đỏ");
        Question q = new Question(a, r, w);
        listQuest.add(q);
    }
}

