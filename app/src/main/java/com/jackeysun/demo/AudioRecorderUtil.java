package com.jackeysun.demo;

import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.RemoteViews;

import java.io.File;
import java.io.IOException;

/**
 * Created by jackey on 2017/8/12.
 */

public class AudioRecorderUtil implements MediaRecorder.OnErrorListener, MediaRecorder.OnInfoListener {


    private static final String TAG = AudioRecorderUtil.class.getSimpleName();

    //最大录制时长
    private static final int MAX_LENGTH = 60 * 1000;
    //文件路径
    private String filePath;
    //文件夹路径
    private static final String FolderPath = Environment.getExternalStorageDirectory() + "/record/";

    private MediaRecorder mMediaRecorder;
    private long startTime;
    private boolean isRecording = false;

    private Handler mHandler = new Handler();

    private Runnable micUpdate = new Runnable() {
        @Override
        public void run() {
            updateMicStatus();
        }
    };

    public AudioRecorderUtil() {
        init();
        //创建保存文件路径
        File path = new File(FolderPath);
        if (!path.exists())
            path.mkdirs();
    }

    /**
     * 初始化
     */
    private void init() {
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT); // 设置声音来源  麦克风
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);//设置文件格式
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB); //设置编码格式
        mMediaRecorder.setMaxDuration(MAX_LENGTH);//录制最大时长
        mMediaRecorder.setOnErrorListener(this);
        mMediaRecorder.setOnInfoListener(this);
    }

    /**
     * 开始录音
     */
    public void start() {
        if (mMediaRecorder == null)
            init();
        if (!isRecording) {
            try {
                filePath = FolderPath + System.currentTimeMillis() + ".amr";
                mMediaRecorder.setOutputFile(filePath);//设置音频保存位置
                mMediaRecorder.prepare();
                mMediaRecorder.start();
                startTime = System.currentTimeMillis();
                isRecording = true;
                updateMicStatus();
            } catch (IOException e) {
                e.printStackTrace();
                if (audioRecorderListener != null) {
                    audioRecorderListener.onError(new AudioRecordException("录音启动失败", e));
                }
                stop();
                deleteFile();
            }
        }
    }

    /**
     * 停止录音
     */
    public void stop() {
        if (isRecording) {
            if (audioRecorderListener != null) {
                audioRecorderListener.onStop(filePath);
            }
            isRecording = false;
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            mHandler.removeCallbacks(micUpdate);
        }
    }

    /**
     * 更新麦克风
     */
    private void updateMicStatus() {
        if (mMediaRecorder != null) {
            double ratio = (double) mMediaRecorder.getMaxAmplitude() / 1;
            double db = 0;// 分贝
            if (ratio > 1) {
                db = 20 * Math.log10(ratio);
                if (audioRecorderListener != null) {
                    audioRecorderListener.onUpdate(db, System.currentTimeMillis() - startTime);
                }
            }
            mHandler.postDelayed(micUpdate, 100);
        }
    }

    /**
     * 删除文件
     */
    private void deleteFile() {
        File file = new File(filePath);
        if (file.exists())
            file.delete();
        filePath = "";
    }

    @Override
    public void onError(MediaRecorder mr, int what, int extra) {
        Log.d(TAG, "onError:" + "what:" + what + "======" + "extra:" + extra);
        switch (what) {
            case MediaRecorder.MEDIA_ERROR_SERVER_DIED:
                if (audioRecorderListener != null) {
                    audioRecorderListener.onError(new AudioRecordException("录音服务停止运行"));
                }
                break;
            case MediaRecorder.MEDIA_RECORDER_ERROR_UNKNOWN:
                if (audioRecorderListener != null) {
                    audioRecorderListener.onError(new AudioRecordException("未知错误"));
                }
                break;
            default:
                if (audioRecorderListener != null) {
                    audioRecorderListener.onError(new AudioRecordException("code:" + what));
                    deleteFile();
                }
        }
        stop();
        deleteFile();
    }

    @Override
    public void onInfo(MediaRecorder mr, int what, int extra) {
        Log.d(TAG, "onInfo:" + "what:" + what + "======" + "extra:" + extra);
        switch (what) {
            case MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED://最大时长监听
                if (audioRecorderListener != null) {
                    audioRecorderListener.onStop(filePath);
                    stop();
                }
                break;
            case MediaRecorder.MEDIA_RECORDER_INFO_UNKNOWN:
                if (audioRecorderListener != null) {
                    audioRecorderListener.onError(new AudioRecordException("未知错误"));
                    stop();
                    deleteFile();
                }
                break;
            case MediaRecorder.MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED://最大文件监听
                if (audioRecorderListener != null) {
                    audioRecorderListener.onStop(filePath);
                    stop();
                }
                break;
            default:
                if (audioRecorderListener != null) {
                    audioRecorderListener.onError(new AudioRecordException("code:" + what));
                    stop();
                    deleteFile();
                }
        }

    }


    public interface AudioRecorderListener {
        //出错了
        void onError(AudioRecordException e);

        //结束了
        void onStop(String filePath);

        //更新
        void onUpdate(double db, long time);

    }

    private AudioRecorderListener audioRecorderListener;

    public void setAudioRecorderListener(AudioRecorderListener audioRecorderListener) {
        this.audioRecorderListener = audioRecorderListener;
    }
}
