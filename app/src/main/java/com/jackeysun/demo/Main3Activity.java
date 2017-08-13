package com.jackeysun.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Main3Activity extends AppCompatActivity {

    private static final String TAG = Main3Activity.class.getSimpleName();
    private AudioRecorderUtil audioRecorderUtil;

    private DialogFactory dialogFactory;

    private ImageView mImageView;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        audioRecorderUtil = new AudioRecorderUtil();

        View view = LayoutInflater.from(this).inflate(R.layout.dialog, null);
        dialogFactory = new DialogFactory
                .Builder(this)
                .setView(view)
                .create();

        mImageView = (ImageView) view.findViewById(R.id.iv_recording_icon);
        mTextView = (TextView) view.findViewById(R.id.tv_recording_time);

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioRecorderUtil.start();
                dialogFactory.show();
            }
        });

        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioRecorderUtil.stop();
            }
        });

        audioRecorderUtil.setAudioRecorderListener(new AudioRecorderUtil.AudioRecorderListener() {
            @Override
            public void onError(AudioRecordException e) {
                dialogFactory.dismiss();
            }

            @Override
            public void onStop(String filePath) {
                dialogFactory.dismiss();
                Toast.makeText(Main3Activity.this, "录音保存在：" + filePath, Toast.LENGTH_SHORT).show();
                mTextView.setText(TimeUtils.long2String(0));
            }

            @Override
            public void onUpdate(double db, long time) {
                Log.d(TAG, "db: " + db + "=======" + "time:" + time);
                mImageView.getDrawable().setLevel((int) (3000 + 6000 * db / 100));
                mTextView.setText(TimeUtils.long2String(time));
            }
        });
    }
}
