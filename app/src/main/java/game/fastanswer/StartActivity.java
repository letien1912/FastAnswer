package game.fastanswer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class StartActivity extends AppCompatActivity {

    private SharedPreferences sharedPref;
    private boolean sound;
    private ImageView btSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);
        sharedPref = getSharedPreferences(getString(R.string.pref_change_sound), MODE_PRIVATE);
        sound = sharedPref.getBoolean(getString(R.string.pref_change_sound), false);
        Init();
        SetSoundIcon();
    }

    private void Init() {
        btSound = (ImageView) findViewById(R.id.button_sound);
    }

    public  void ChangeSoundEvent(View v){
        ChangeSound();
    }

    private void ChangeSound() {
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        sound = !sound;
        editor.putBoolean(getString(R.string.pref_change_sound),sound);
        editor.apply();
        editor.commit();

        SetSoundIcon();
    }

    private void SetSoundIcon() {
        if(sound){
            btSound.setImageResource(R.drawable.ic_sound);
        }else{
            btSound.setImageResource(R.drawable.ic_sound_turn_off);
        }
    }

    public void StartNewGame(View v) {
        Intent it = new Intent(this, MainActivity.class);
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
    protected void onStart() {
        super.onStart();
        SetSoundIcon();
    }
}
