package com.superapp.notnhacvuivangbolerotrochoiamnhac.activity;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.superapp.notnhacvuivangbolerotrochoiamnhac.R;
import com.superapp.notnhacvuivangbolerotrochoiamnhac.model.Question;
import com.superapp.notnhacvuivangbolerotrochoiamnhac.utils.DataUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class GamePlayActivity extends AppCompatActivity {
    private final int MAXTIME = 6000;
    private final static int MAX = 7;
    private List<Question> questions;
    private LinearLayout layout_texts;
    private LinearLayout layout_answers;
    private List<View> answers;
    private HashMap<View,Integer> viewHash;
    private List<View> texts;
    private int currentPos;
    private View flag;
    private Question currentQuestion;
    MediaPlayer player;
    private TextView mTxtScore;
    private TextView mTxtCountdown;
    private int mScore;
    private int mCountdown;
    private int mCurrentPosition;
    private HashMap<Integer,Boolean> mListButton;
    private CountDownTimer countDownGame;
    private boolean timeToEnd;
    private MediaPlayer mp;
    private SeekBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);
        questions = DataUtils.getINSTANCE(this).getQuestions();
        layout_texts = (LinearLayout) findViewById(R.id.layout_texts);
        layout_answers = (LinearLayout) findViewById(R.id.layout_answers);
        flag = new LinearLayout(this);
        currentPos = 0;
        player = new MediaPlayer();
        ImageView imageView = (ImageView) findViewById(R.id.image_disk);
//        RotateAnimation rotateAnimation = new RotateAnimation(0,360,50,50);
//        rotateAnimation.setDuration(5000);
//        rotateAnimation.setRepeatCount(Animation.INFINITE);
//        rotateAnimation.setInterpolator();
//        rotateAnimation.setRepeatMode(Animation.RESTART);
        Animation rotateAnimation  = AnimationUtils.loadAnimation(this,R.anim.rotation);
        rotateAnimation.setDuration(5000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setRepeatMode(Animation.RESTART);
        imageView.setAnimation(rotateAnimation);
        mScore = 0;
        mTxtScore = (TextView) findViewById(R.id.txtScore);
        mTxtScore.setText(mScore+"");
        bar = (SeekBar) findViewById(R.id.pbTimer);
        bar.setProgress(MAXTIME);
        bar.setEnabled(false);
        nextQuestion();
        startTime();
    }

    private void finishGame(){
        countDownGame.cancel();
        if (mp!=null && mp.isPlaying() ){
            mp.stop();
        }
        if (player!=null && player.isPlaying()){
            player.stop();
            player.reset();
        }
        Intent intent = new Intent(getApplicationContext(),ResultActivity.class);
        intent.putExtra("Score",mScore);
        startActivity(intent);
        finish();
    }

    private void startTime() {
        timeToEnd=false;
        countDownGame = new CountDownTimer(MAXTIME * 10, 10) {

            @Override
            public void onTick(long millisUntilFinished) {
                bar.setProgress((int) (millisUntilFinished / 10));
                if (millisUntilFinished/10<900 && !timeToEnd){
                    timeToEnd=true;
                    mp = MediaPlayer.create(GamePlayActivity.this, R.raw.countdown);
                    mp.start();
                }
            }

            @Override
            public void onFinish() {
//                Log.i(TAG,"Onfinish");
//                start = false;
//                done =true;

                mp = MediaPlayer.create(getApplicationContext(), R.raw.hetgio);
                mp.start();
                finishGame();
//                dialog = new ResultDialog(mContext, mScore, listResult);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setCanceledOnTouchOutside(false);
//                dialog.show();

            }
        };
        countDownGame.start();
    }
    private void nextQuestion(){
        if (questions!=null && questions.size()>0) {
            int randPosition = DataUtils.random(0, questions.size() - 1);
             currentQuestion = questions.get(randPosition);
            try {
                startSound("music/"+currentQuestion.getFileName());
            } catch (IOException e) {
                e.printStackTrace();
            }
            questions.remove(randPosition);
            layout_texts.removeAllViews();
            layout_answers.removeAllViews();
            List<TextView> textViews = new ArrayList<TextView>();
            answers = new ArrayList<>();
            texts = new ArrayList<>();
            viewHash = new HashMap<>();
            int count =0;

            for (char item : convertToList(currentQuestion.getKey().toCharArray())) {
                View v = getLayoutInflater().inflate(R.layout.item_text, null, false);
                TextView textView = (TextView) v.findViewById(R.id.item_text);
                textView.setText(String.valueOf(item));
                textView.setAllCaps(true);
                textView.setTag(v);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        View v2 = (View) view.getTag();
                        currentPos = getPosNull(answers);

                        if (viewHash.get(v2) == 2) {
                            viewHash.put(v2, 1);
                            layout_texts.removeView(v2);
                            layout_answers.removeViewAt(currentPos);
                            layout_answers.addView(v2, currentPos);
                            answers.set(currentPos, v2);
                            if (currentPos == answers.size() - 1) {
                                if (checkAnswer()){
                                    mScore++;
                                    mTxtScore.setText(mScore+"");
                                    Toast.makeText(GamePlayActivity.this, "Đúng: "+currentQuestion.getTitle(), Toast.LENGTH_SHORT).show();
                                    nextQuestion();
                                }else{
                                    Toast.makeText(GamePlayActivity.this, "Không đúng!", Toast.LENGTH_SHORT).show();
                                }
//                                Toast.makeText(GamePlayActivity.this, checkAnswer()+"", Toast.LENGTH_SHORT).show();
//                                nextQuestion();
                            }

                        } else {
                            viewHash.put(v2, 2);
                            int currPosition = getPosOfView(v2, answers);
                            View v1 = getLayoutInflater().inflate(R.layout.item_layout, null, false);
                            layout_answers.addView(v1, currPosition);
                            answers.set(currPosition, flag);
                            layout_answers.removeView(v2);
                            layout_texts.addView(v2);
                        }

                    }
                });
                texts.add(v);
                answers.add(flag);
                viewHash.put(v, 2);

                layout_texts.addView(v);
                View v1 = getLayoutInflater().inflate(R.layout.item_layout, null, false);
                layout_answers.addView(v1);
                count++;
            }
        }else{
            finishGame();
        }
    }

    private int getPosOfView(View v,List<View> texts){
        int index = -1;
        for(View item :texts){
            index++;
            if (item!=null && item.equals(v)){

                return index;
            }
        }
        return -1;
    }

    private void startSound(String filename) throws IOException {
            AssetFileDescriptor afd = getAssets().openFd(filename);
            if (player!=null && player.isPlaying()){
                player.stop();
                player.reset();
            }
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            player.prepare();
            player.start();

    }

    private List<Character> convertToList(char[] array){
        List<Character> characters = new ArrayList<Character>();
        for(char item:array){
            characters.add(item);
        }
        Collections.shuffle(characters);
        return characters;
    }

    private int getPosNull(List<View> texts){
        int index = -1;
        for(View item :texts){
            index++;
            if (item==flag){
                return index;
            }
        }
        return index+1;
    }

    private boolean checkAnswer(){
        String s  ="";
        for (View v:answers){
            TextView textView = (TextView) v.findViewById(R.id.item_text);
            s+=textView.getText().toString().trim();
        }

        return s.equals(currentQuestion.getKey());
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player!=null && player.isPlaying()){
            player.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player!=null ){
            player.start();
        }
    }

}
