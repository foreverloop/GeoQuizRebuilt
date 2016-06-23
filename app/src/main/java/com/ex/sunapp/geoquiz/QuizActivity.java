package com.ex.sunapp.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity {

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mPrevButton;
    private Button mCheatButton;
    private TextView mQuestionTextview;
    private TextView mSdkTextview;
    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_DID_CHEAT = "com.ex.sunapp.geoquiz.QuizActiviy.key_did_cheat";
    private static final int REQUEST_CODE_CHEAT = 0;
    private boolean mIsCheater;

    private Question[] mQuestionBank = {new Question(R.string.question_oceans,true),
                                        new Question(R.string.question_mideast,false),
                                        new Question(R.string.question_africa,false),
                                        new Question(R.string.question_americas,true),
                                        new Question(R.string.question_asia,true)};
    private int mCurrentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mIsCheater = savedInstanceState.getBoolean(KEY_DID_CHEAT,false);
        }

        mTrueButton = (Button) findViewById(R.id.true_button);
        mFalseButton = (Button) findViewById(R.id.false_button);
        mQuestionTextview = (TextView) findViewById(R.id.question_textview);
        mNextButton = (Button) findViewById(R.id.next_button);
        mPrevButton = (Button) findViewById(R.id.prev_button);
        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mSdkTextview = (TextView) findViewById(R.id.sdk_textview);

        mSdkTextview.setText(String.valueOf(Build.VERSION.SDK_INT));
        mQuestionTextview.setText(getText(mQuestionBank[mCurrentIndex].getTextResId()));

        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(CheatActivity.newIntent(QuizActivity.this,
                                        mQuestionBank[mCurrentIndex].isAnswerTrue()),
                                        REQUEST_CODE_CHEAT);

            }
        });

        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mIsCheater){
                    mQuestionBank[mCurrentIndex].setCheated(true);
                }

                if(mCurrentIndex != 0)
                    --mCurrentIndex;
                else
                    mCurrentIndex = (mQuestionBank.length - 1);

                mIsCheater = false;
                mQuestionTextview.setText(getText(mQuestionBank[mCurrentIndex].getTextResId()));
            }
        });
        
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mIsCheater){
                    mQuestionBank[mCurrentIndex].setCheated(true);
                }


                mCurrentIndex = ++mCurrentIndex % mQuestionBank.length;
                mIsCheater = false;
                mQuestionTextview.setText(getText(mQuestionBank[mCurrentIndex].getTextResId()));
            }
        });

        mQuestionTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCurrentIndex < (mQuestionBank.length - 1))
                    mCurrentIndex++;
                else
                    mCurrentIndex = 0;
                mQuestionTextview.setText(getText(mQuestionBank[mCurrentIndex].getTextResId()));
            }
        });

        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_INDEX,mCurrentIndex);
        outState.putBoolean(KEY_DID_CHEAT,mIsCheater);
    }

    private void checkAnswer(boolean userChoice){
        boolean isAnswerTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int toastText;

        if(mIsCheater || mQuestionBank[mCurrentIndex].isCheated())
            toastText = R.string.judgement_toast;
        else if(isAnswerTrue == userChoice)
            toastText = R.string.correct;
        else
            toastText = R.string.incorrect;

        Toast.makeText(QuizActivity.this,toastText,Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != Activity.RESULT_OK)
            return;

        if(requestCode == REQUEST_CODE_CHEAT) {
            if (data == null)
                return;

            mIsCheater = CheatActivity.wasAnswerShown(data);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quiz, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}