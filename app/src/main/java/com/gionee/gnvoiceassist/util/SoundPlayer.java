/*
 * 描述：语音操作过程中的提示音pool
 */
package com.gionee.gnvoiceassist.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.SparseIntArray;

import com.baidu.duer.dcs.util.LogUtil;
import com.gionee.gnvoiceassist.GnVoiceAssistApplication;
import com.gionee.gnvoiceassist.R;

import java.util.Arrays;
import java.util.List;


public class SoundPlayer {
	public static final String TAG = SoundPlayer.class.getSimpleName();
    private SoundPool mRingSoundPool;
    private SoundPool mMusicSoundPool;
    private SparseIntArray mRingSoundResMap = new SparseIntArray();
    private SparseIntArray mMusicSoundResMap = new SparseIntArray();
    private static SoundPlayer sInstance;
    private static final List<Integer> mRingSoundResList = Arrays.asList( R.raw.ring_start, R.raw.ring_stop, R.raw.succeed, R.raw.message_ring );
    private static final List<Integer> mMusicSoundResList = Arrays.asList( R.raw.ring_start, R.raw.ring_stop );
    private static final int SOUND_PLAYER_PRIORITY = 1;
    private Context mContext;
    private AudioManager mAudioManager;

    /******************************* 构造函数 & Override *******************************/
    public static synchronized SoundPlayer getInstance() {
        if (null == sInstance) {
            sInstance = new SoundPlayer();
        }

        return sInstance;
    }

    private SoundPlayer() {
        mContext = GnVoiceAssistApplication.getInstance().getApplicationContext();
        initData(mContext);
        initSoundRes();
    }
    /*********************************** 对外接口 ***********************************/
    public void destorySoundPool() {
    	resetData();
        resetInstance();
        abortAudioFocus();
        LogUtil.d(TAG, "SoundPlayer destorySoundPool");
    }

    public int playRingSound(int id) {
        if (null == mRingSoundResMap || null == mRingSoundPool) {
            LogUtil.d(TAG, "SoundPlayer playRingSound mRingSoundResMap = " + mRingSoundResMap + ", mRingSoundPool = " + mRingSoundPool);
            return 0;
        }

        int ringId = mRingSoundResMap.get(id);
        if (ringId < 1) {
            LogUtil.d(TAG, "SoundPlayer playRingSound failed not find res in Map");
            return 0;
        }

        int ret = mRingSoundPool.play(ringId, 1, 1, 1, 0, 1);
        if (0 == ret) {
            LogUtil.d(TAG, "SoundPlayer playRingSound failed id=" + id);
        }

        return ret;
    }

    public int playMusicSound(final int id) {
        if (null == mMusicSoundResMap || null == mMusicSoundPool) {
            LogUtil.d(TAG, "SoundPlayer playMusicSound mMusicSoundResMap = " + mMusicSoundResMap + ", mMusicSoundPool = " + mMusicSoundPool);
            return 0;
        }
        final int ringId = mMusicSoundResMap.get(id);
        if (ringId < 1) {
            LogUtil.d(TAG, "SoundPlayer playMusicSound failed not find res in Map");
            return 0;
        }

        requestAudioFocus();
        LogUtil.d(TAG, "SoundPlayer playMusicSound");
        int ret = mMusicSoundPool.play(ringId, 1, 1, 1, 0, 1);
        if (0 == ret) {
            LogUtil.d(TAG, "SoundPlayer playMusicSound failed id=" + id);
        }
        abortAudioFocus();
        return ret;
    }

    /*********************************** 私有函数 ***********************************/
	private static synchronized void resetInstance() {
        sInstance = null;
    }
	
	private void initData(Context cxt) {
		mContext = cxt;
		// Gionee sunyang modify for GNSPR #58738 at 2017-04-12 begin
//		mRingSoundPool = new SoundPool(1, AudioManager.STREAM_ALARM, 100);
//		mMusicSoundPool = new SoundPool(1, AudioManager.STREAM_ALARM, 100);
		mRingSoundPool = new SoundPool(1, AudioManager.STREAM_RING, 100);
		mMusicSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 100);
		// Gionee sunyang modify for GNSPR #58738 at 2017-04-12 end
	}
	
	private void resetData() {
		if (null != mRingSoundPool) {
			mRingSoundPool.release();
			mRingSoundPool = null;
		}
		if (null != mMusicSoundPool) {
			mMusicSoundPool.release();
			mMusicSoundPool = null;
		}
		if (null != mRingSoundResMap) {
			mRingSoundResMap.clear();
			mRingSoundResMap = null;
		}
		if (null != mMusicSoundResMap) {
			mMusicSoundResMap.clear();
			mMusicSoundResMap = null;
		}
	}
    
    private void putRingSoundPool(List<Integer> resList) {
    	for(int resId : mRingSoundResList) {
    		putRingSoundPool(resId, SOUND_PLAYER_PRIORITY);
    	}
    }
    
    private void putMusicSoundPool(List<Integer> resList) {
    	for(int resId : mMusicSoundResList) {
    		putMusicSoundPool(resId, SOUND_PLAYER_PRIORITY);
    	}
    }
    
    private void putRingSoundPool(int resId, int priority) {
    	int ringId = mRingSoundPool.load(mContext, resId, priority);
        mRingSoundResMap.put(resId, ringId);
    }
    
    private void putMusicSoundPool(int resId, int priority) {
    	int ringId = mMusicSoundPool.load(mContext, resId, priority);
    	mMusicSoundResMap.put(resId, ringId);
    }
    
    private void initSoundRes() {
    	putRingSoundPool(mRingSoundResList);
    	putMusicSoundPool(mMusicSoundResList);
    }

    private boolean requestAudioFocus() {
        LogUtil.d(TAG, "SoundPlayer requestAudioFocus mAudioManager = " + mAudioManager);
        if (null == mAudioManager) {
            mAudioManager = (AudioManager) GnVoiceAssistApplication.
                    getInstance().getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        }

        mAudioManager.requestAudioFocus(mAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        return true;
    }

    private void abortAudioFocus() {
        LogUtil.d(TAG, "SoundPlayer abortAudioFocus mAudioManager = " + mAudioManager);
        if (null != mAudioManager) {
            mAudioManager.abandonAudioFocus(mAudioFocusChangeListener);
        }
    }

    private AudioManager.OnAudioFocusChangeListener mAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {

        @Override
        public void onAudioFocusChange(int arg0) {
            LogUtil.d(TAG, "SoundPlayer mAudioFocusChangeListener onAudioFocusChange arg0 = " + arg0);
        }
    };
}