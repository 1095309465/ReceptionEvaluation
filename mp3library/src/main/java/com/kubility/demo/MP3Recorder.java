
package com.kubility.demo;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * <b>类功能描述：</b><div style="margin-left:40px;margin-top:-10px">
 * MP3实时录制功能,可暂停,注意因踩用Native开发,不能混淆
 * 配置,编译so查看:http://blog.csdn.net/cboy017/article/details/8455629
 * </div>
 *
 * @author <a href="mailto:184618345@qq.com">017</a>
 * @version 1.0
 *          </p>
 *          修改时间：</br>
 *          修改备注：</br>
 */
public class MP3Recorder {
    private String filePath;
    private int sampleRate;
    private boolean isRecording = false;
    private boolean isPause = false;
    private Handler handler;

    /**
     * 开始录音
     */
    public static final int MSG_REC_STARTED = 1;

    /**
     * 结束录音
     */
    public static final int MSG_REC_STOPPED = 2;

    /**
     * 暂停录音
     */
    public static final int MSG_REC_PAUSE = 3;

    /**
     * 继续录音
     */
    public static final int MSG_REC_RESTORE = 4;

    /**
     * 缓冲区挂了,采样率手机不支持
     */
    public static final int MSG_ERROR_GET_MIN_BUFFERSIZE = -1;

    /**
     * 创建文件时扑街了
     */
    public static final int MSG_ERROR_CREATE_FILE = -2;

    /**
     * 初始化录音器时扑街了
     */
    public static final int MSG_ERROR_REC_START = -3;

    /**
     * 录紧音(撸紧阴?)的时候出错
     */
    public static final int MSG_ERROR_AUDIO_RECORD = -4;

    private int mVolume;

    public int getVolume() {
        return mVolume;
    }

    /**
     * 录音时的音量
     */
    public static final int MP3_Volume = 10;

    /**
     * 编码时挂了
     */
    public static final int MSG_ERROR_AUDIO_ENCODE = -5;

    /**
     * 写文件时挂了
     */
    public static final int MSG_ERROR_WRITE_FILE = -6;

    /**
     * 没法关闭文件流
     */
    public static final int MSG_ERROR_CLOSE_FILE = -7;

    public MP3Recorder(String filePath) {
        this.filePath = filePath;
        this.sampleRate = 8000;
    }

    public MP3Recorder(File f) {
        this.filePath = f.getAbsolutePath();
        this.sampleRate = 8000;
    }

    private void calculateRealVolume(short[] buffer, int readSize) {
        double sum = 0;
        for (int i = 0; i < readSize; i++) {
            // 这里没有做运算的优化，为了更加清晰的展示代码
            sum += buffer[i] * buffer[i];
        }
        if (readSize > 0) {
            double amplitude = sum / readSize;
            mVolume = (int) Math.sqrt(amplitude);
            if (onCallVolumeBack != null) {
                onCallVolumeBack.getVolume(mVolume);
            }
        }
    }

    public interface OnMp3Listener {
        void getVolume(int voice);

        void startMp3();

        void stopMp3();

        void paushMp3();

        void reStoreMp3();

    }

    public OnMp3Listener onCallVolumeBack;

    public void setOnCallVolumeBack(OnMp3Listener onCallVolumeBack) {
        this.onCallVolumeBack = onCallVolumeBack;
    }


    /**
     * 暂停
     */
    public void setPaush() {
        if (isRecording()) {
            if (isPaus()) {
                restore();
            } else {
                pause();
            }
        }

    }

    /**
     * 开片
     */
    public void start() {
        if (isRecording) {
            return;
        }

        new Thread() {
            @Override
            public void run() {
                android.os.Process
                        .setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
                // 根据定义好的几个配置，来获取合适的缓冲大小
                final int minBufferSize = AudioRecord.getMinBufferSize(sampleRate,
                        AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
                if (minBufferSize < 0) {
                    if (handler != null) {
                        handler.sendEmptyMessage(MSG_ERROR_GET_MIN_BUFFERSIZE);
                    }
                    return;
                }
                AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                        sampleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT,
                        minBufferSize * 2);

                // 5秒的缓冲
                short[] buffer = new short[sampleRate * (16 / 8) * 1 * 5];
                byte[] mp3buffer = new byte[(int) (7200 + buffer.length * 2 * 1.25)];

                FileOutputStream output = null;
                try {
                    output = new FileOutputStream(new File(filePath));
                } catch (FileNotFoundException e) {
                    if (handler != null) {
                        handler.sendEmptyMessage(MSG_ERROR_CREATE_FILE);
                    }
                    return;
                }
                MP3Recorder.init(sampleRate, 1, sampleRate, 32);
                isRecording = true; // 录音状态
                isPause = false; // 录音状态
                try {
                    try {
                        audioRecord.startRecording(); // 开启录音获取音频数据
                    } catch (IllegalStateException e) {
                        // 不给录音...
                        if (handler != null) {
                            handler.sendEmptyMessage(MSG_ERROR_REC_START);
                        }
                        return;
                    }

                    try {
                        // 开始录音
                        if (handler != null) {
                            handler.sendEmptyMessage(MSG_REC_STARTED);
                        }
                        if (onCallVolumeBack != null) {
                            onCallVolumeBack.startMp3();
                        }

                        int readSize = 0;
                        boolean pause = false;
                        while (isRecording) {
                            /*--暂停--*/
                            if (isPause) {
                                if (!pause) {
                                    if (handler != null) {
                                        handler.sendEmptyMessage(MSG_REC_PAUSE);
                                    }
                                    if (onCallVolumeBack != null) {
                                        onCallVolumeBack.paushMp3();
                                    }
                                    pause = true;
                                }
                                continue;
                            }
                            if (pause) {
                                if (handler != null) {
                                    handler.sendEmptyMessage(MSG_REC_RESTORE);
                                }
                                if (onCallVolumeBack != null) {
                                    onCallVolumeBack.reStoreMp3();
                                }
                                pause = false;
                            }
                            /*--End--*/
                            /*--实时录音写数据--*/
                            readSize = audioRecord.read(buffer, 0, minBufferSize);

                            if (readSize < 0) {
                                if (handler != null) {
                                    handler.sendEmptyMessage(MSG_ERROR_AUDIO_RECORD);
                                }
                                break;
                            } else if (readSize == 0) {
                                ;
                            } else {
                                calculateRealVolume(buffer, readSize);
                                int encResult = MP3Recorder.encode(buffer, buffer, readSize,
                                        mp3buffer);
                                if (encResult < 0) {
                                    if (handler != null) {
                                        handler.sendEmptyMessage(MSG_ERROR_AUDIO_ENCODE);
                                    }
                                    break;
                                }
                                if (encResult != 0) {
                                    try {
                                        output.write(mp3buffer, 0, encResult);
                                    } catch (IOException e) {
                                        if (handler != null) {
                                            handler.sendEmptyMessage(MSG_ERROR_WRITE_FILE);
                                        }
                                        break;
                                    }
                                }
                            }
                            /*--End--*/
                        }
                        /*--录音完--*/
                        int flushResult = MP3Recorder.flush(mp3buffer);
                        if (flushResult < 0) {
                            if (handler != null) {
                                handler.sendEmptyMessage(MSG_ERROR_AUDIO_ENCODE);
                            }
                        }
                        if (flushResult != 0) {
                            try {
                                output.write(mp3buffer, 0, flushResult);
                            } catch (IOException e) {
                                if (handler != null) {
                                    handler.sendEmptyMessage(MSG_ERROR_WRITE_FILE);
                                }
                            }
                        }
                        try {
                            output.close();
                        } catch (IOException e) {
                            if (handler != null) {
                                handler.sendEmptyMessage(MSG_ERROR_CLOSE_FILE);
                            }
                        }
                        /*--End--*/
                    } finally {
                        audioRecord.stop();
                        audioRecord.release();
                    }
                } finally {
                    MP3Recorder.close();
                    isRecording = false;
                }
                if (handler != null) {
                    handler.sendEmptyMessage(MSG_REC_STOPPED);
                }
                if (onCallVolumeBack != null) {
                    onCallVolumeBack.stopMp3();
                }
            }
        }.start();
    }

    public void stop() {
        isRecording = false;
    }

    public void pause() {
        isPause = true;
    }

    public void restore() {
        isPause = false;
    }

    public boolean isRecording() {
        return isRecording;
    }

    public boolean isPaus() {
        if (!isRecording) {
            return false;
        }
        return isPause;
    }

    /**
     * 录音状态管理
     *
     * @see RecMicToMp3#MSG_REC_STARTED
     * @see RecMicToMp3#MSG_REC_STOPPED
     * @see RecMicToMp3#MSG_REC_PAUSE
     * @see RecMicToMp3#MSG_REC_RESTORE
     * @see RecMicToMp3#MSG_ERROR_GET_MIN_BUFFERSIZE
     * @see RecMicToMp3#MSG_ERROR_CREATE_FILE
     * @see RecMicToMp3#MSG_ERROR_REC_START
     * @see RecMicToMp3#MSG_ERROR_AUDIO_RECORD
     * @see RecMicToMp3#MSG_ERROR_AUDIO_ENCODE
     * @see RecMicToMp3#MSG_ERROR_WRITE_FILE
     * @see RecMicToMp3#MSG_ERROR_CLOSE_FILE
     */
    public void setHandle(Handler handler) {
        this.handler = handler;
    }


    /*--以下为Native部分--*/
    static {
        System.loadLibrary("mp3lame");
    }

    /**
     * 初始化录制参数
     */
    public static void init(int inSamplerate, int outChannel, int outSamplerate, int outBitrate) {
        init(inSamplerate, outChannel, outSamplerate, outBitrate, 7);
    }

    /**
     * 初始化录制参数
     * quality:0=很好很慢 9=很差很快
     */
    public native static void init(int inSamplerate, int outChannel, int outSamplerate,
                                   int outBitrate, int quality);

    /**
     * 音频数据编码(PCM左进,PCM右进,MP3输出)
     */
    public native static int encode(short[] buffer_l, short[] buffer_r, int samples, byte[] mp3buf);

    /**
     * 据说撸完之后要刷干净缓冲区,就是冲出来的那些东西要擦干净啦
     */
    public native static int flush(byte[] mp3buf);

    /**
     * 结束编码
     */
    public native static void close();
}
