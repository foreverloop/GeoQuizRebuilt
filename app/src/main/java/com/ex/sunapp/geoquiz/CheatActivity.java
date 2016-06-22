package com.ex.sunapp.geoquiz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE = "com.ex.sunapp.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN ="com.ex.sunapp.geoquiz.answer_shown";
    private static final String EXTRA_CHEAT_KEY = "com.ex.sunapp.geoquiz.cheat_key";
    private boolean mAnswerIsTrue;
    private boolean mAnswerShown;
    private TextView mAnswerTextView;
    private Button mShowAnswer;

    public static Intent newIntent(Context packageContext, boolean answerIsTrue){
        Intent i = new Intent(packageContext,CheatActivity.class);

        return i.putExtra(EXTRA_ANSWER_IS_TRUE,answerIsTrue);

    }

    public static boolean wasAnswerShown(Intent result){
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN,false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        mAnswerIsTrue = getIntent().getExtras().getBoolean(EXTRA_ANSWER_IS_TRUE,false);

        if(savedInstanceState != null)
            mAnswerShown = savedInstanceState.getBoolean(EXTRA_CHEAT_KEY,false);

        if(mAnswerShown)
            setAnswerShown(true);

        mAnswerTextView = (TextView) findViewById(R.id.answer_textview);
        mShowAnswer = (Button) findViewById(R.id.showanswerbutton);

        mShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAnswerIsTrue)
                    mAnswerTextView.setText(R.string.true_button);
                else
                    mAnswerTextView.setText(R.string.false_button);

                setAnswerShown(true);

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EXTRA_CHEAT_KEY,mAnswerShown);
    }

    private void setAnswerShown(boolean isAnswerShown){
        mAnswerShown = isAnswerShown;
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN,mAnswerShown);
        setResult(Activity.RESULT_OK,data);
    }

}
