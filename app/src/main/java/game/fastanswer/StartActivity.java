package game.fastanswer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import Entities.GamePlay;

public class StartActivity extends AppCompatActivity {

    private SharedPreferences sharedPref;
    private ImageView btSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);
        Init();
    }

    private void Init() {
        btSound = (ImageView) findViewById(R.id.button_sound);
        sharedPref = getSharedPreferences(getString(R.string.pref_change_sound), MODE_PRIVATE);
    }

    public void ChangeSoundEvent(View v) {
        ChangeSound();
        SetSoundIcon();
    }

    private void ChangeSound() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.putBoolean(getString(R.string.pref_change_sound), !getSoundCurrent());
        editor.apply();
        editor.commit();
    }

    private void SetSoundIcon() {
        Log.d("asdas", getSoundCurrent() + "");
        if (getSoundCurrent()) {
            btSound.setImageResource(R.drawable.ic_sound);
        } else {
            btSound.setImageResource(R.drawable.ic_sound_turn_off);
        }
    }

    private boolean getSoundCurrent() {
        return sharedPref.getBoolean(getString(R.string.pref_change_sound), true);
    }

    public void StartNewGame(View v) {
        Intent it = new Intent(this, MainActivity.class);
        GamePlay gamePlay = (GamePlay) getIntent().getExtras().get(getString(R.string.GAMEPLAY_CONTENT));
        it.putExtra(getString(R.string.GAMEPLAY_CONTENT), gamePlay);
        startActivity(it);
    }

    public void ShareGameEvent(View v) {
        String message = "Join The Game !!!";
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(share, message));
        Log.d("Share Game Event", "Share done");
    }

    public void ViewScoreEvent(View v) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        SetSoundIcon();
    }
}
