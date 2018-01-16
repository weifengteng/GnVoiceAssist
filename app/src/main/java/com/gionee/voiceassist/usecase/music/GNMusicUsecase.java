package com.gionee.voiceassist.usecase.music;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.gionee.voiceassist.GnVoiceAssistApplication;
import com.gionee.voiceassist.R;
import com.gionee.voiceassist.basefunction.BasePresenter;
import com.gionee.voiceassist.basefunction.IBaseFunction;
import com.gionee.voiceassist.controller.ttscontrol.TtsCallback;
import com.gionee.voiceassist.controller.ttscontrol.TtsController;
import com.gionee.voiceassist.coreservice.datamodel.DirectiveEntity;
import com.gionee.voiceassist.coreservice.datamodel.LocalAudioPlayerDirectiveEntity;
import com.gionee.voiceassist.usecase.BaseUsecase;
import com.gionee.voiceassist.util.LogUtil;
import com.gionee.voiceassist.util.Utils;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.Random;

/**
 * Created by tengweifeng on 9/29/17.
 *
 * 控制音乐播放 功能实现类
 *
 */

public class GNMusicUsecase extends BaseUsecase{
    public static final String TAG = GNMusicUsecase.class.getSimpleName();
    private static final String GN_BROWSABLE_PACKAGE_NAME = "com.android.browser";
    private static final String GN_MUSIC_PACKAGE_NAME = "com.android.music";
    private static final String UTTER_ID_FIRE_FOCUS = "music_focus";
    private static final String GN_MUSIC_SEARCH_URL = "http://music.gionee.com:80";
    private static final String GN_MUSIC_VERSION_NAME_SUPPORT_SEARCH_MUSIC = "5.1.3.ca";
    private static final String GN_MUSIC_ENTRY_ACTION = "com.gionee.music.entry";
    private static final String GN_PLAY_MUSIC_ACTION = "com.android.music.intent.action.PLAY_IN_ALL_MUSIC";

    private static final String GN_LOCAL_MUSIC_SELECTION_TITLE = MediaStore.Audio.Media.IS_MUSIC + "=1 and (" + MediaStore.Audio.Media.TITLE + " like ? or " + MediaStore.Audio.Media.DISPLAY_NAME + " like ?)";
    private static final String GN_LOCAL_MUSIC_SELECTION_ARTIST = MediaStore.Audio.Media.IS_MUSIC + "=1 and (" + MediaStore.Audio.Media.ARTIST + " like ? or " + MediaStore.Audio.Media.DISPLAY_NAME + " like ?)";
    private static final String[] GN_MUSIC_PROJECTION = new String[] { MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.MIME_TYPE};

    private Intent mIntent;
    private int mIntentType = 0;
    private Context mAppCtx;

    public GNMusicUsecase() {
        mAppCtx = GnVoiceAssistApplication.getInstance().getApplicationContext();
    }

    public void procMusicAction(String singer, String song) {
        if (!getExistLocal(singer, song)) {
            searchMusicFromNet(getUrl(singer, song));
        }

        String tip = getTip(singer, song);
        LogUtil.d(TAG, "GNMusicUsecase singer = " + singer + ", song = " + song + ", tip = " + tip);
        playAndRenderText(tip, UTTER_ID_FIRE_FOCUS, new TtsCallback() {
            @Override
            public void onSpeakStart() {

            }

            @Override
            public void onSpeakFinish(String utterId) {
                if(mIntent == null) {
                    LogUtil.d(TAG, "GNMusicUsecase ttsCompleted mIntent = " + mIntent);
                    return;
                }
                if(TextUtils.equals(utterId, UTTER_ID_FIRE_FOCUS)) {
                    if(0 == getRestoreMusicApp()){
                        mIntentType = 0;
                        mIntent = null;
                        return;
                    }

                    if (mIntentType != 0) {
                        Utils.startActivity(mAppCtx, mIntent);
                    } else {
                        try {
                            mAppCtx.startService(mIntent);
                        } catch (Exception e) {
                            LogUtil.d(TAG, "GNMusicUsecase ttsCompleted Exception = " + e);
                        }
                    }
                    mIntentType = 0;
                    mIntent = null;
                }
            }

            @Override
            public void onSpeakError(TtsController.TtsResultCode ttsResultCode, String s) {

            }
        });

    }

    public void playMusic(String data, String type) {
        LogUtil.d(TAG, "GNMusicUsecase data= " + data + " type = " + type);
        mIntent = new Intent();
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.setPackage(GN_MUSIC_PACKAGE_NAME);
        mIntent.setAction(GN_PLAY_MUSIC_ACTION);
        if(Build.VERSION.SDK_INT >= 23) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        mIntent.setDataAndType(Uri.fromFile(new File(data)), type);
        mIntentType = 0;
    }

    private int getRestoreMusicApp(){
        PackageManager mPm = mAppCtx.getPackageManager();
        int result = -1;
        try {
            Class<?> tclass = (Class<?>)Class.forName("android.content.pm.PackageManager");
            Method method = tclass.getDeclaredMethod("AmigoRecoverRemovableApp", String.class, int.class);
            // TODO:
//            result = (int)method.invoke(mPm, GN_MUSIC_PACKAGE_NAME, UserHandle.myUserId());
            result = 1;
        }catch(Exception e){
            LogUtil.e(TAG, "GNMusicUsecase getRestoreMusicApp  ------Exception = " + e);
        }finally {

        }

        LogUtil.d(TAG, "GNMusicUsecase getRestoreMusicApp  ------result = " + result);
        return result;
    }

    private Cursor getCursor(String selection, String[] selectionArgs) {
        return mAppCtx.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, GN_MUSIC_PROJECTION, selection, selectionArgs, null);
    }

    private boolean searchLocalMusic(String title, String artist) {
        String likeTitle = "%" + title + "%";
        Cursor cursor = getCursor(GN_LOCAL_MUSIC_SELECTION_TITLE, new String[] { likeTitle, likeTitle });

        if(null == cursor) {
            LogUtil.e(TAG, "GNMusicUsecase searchLocalMusic(String title, String artist) null == cursor");
            return false;
        }

        try {
            if (cursor.moveToFirst()) {
                if(cursor.getCount() < 2 || TextUtils.isEmpty(artist)) {
                    playMusic(cursor.getString(0), cursor.getString(4));
                    return true;
                }

                do {
                    if (cursor.getString(1).contains(artist) || cursor.getString(2).contains(artist)
                            || cursor.getString(3).contains(artist)) {
                        playMusic(cursor.getString(0), cursor.getString(4));
                    }
                } while (cursor.moveToNext());
                return true;
            }
        } catch(Exception e1) {
            LogUtil.e(TAG, "GNMusicUsecase searchLocalMusic(String title, String artist) e1 = " + e1);
        } finally {
            try {
                cursor.close();
            } catch(Exception e2) {
                LogUtil.e(TAG, "GNMusicUsecase searchLocalMusic(String title, String artist) e2 = " + e2);
            }
        }
        return false;
    }

    private boolean searchLocalMusic(String artist) {
        String likeTitle = "%" + artist + "%";
        Cursor cursor = getCursor(GN_LOCAL_MUSIC_SELECTION_ARTIST, new String[] { likeTitle, likeTitle });

        if(null == cursor) {
            LogUtil.e(TAG, "GNMusicUsecase searchLocalMusic(String artist) null == cursor");
            return false;
        }
        try {
            if (cursor.moveToFirst()) {
                cursor.moveToPosition(getPlayMusicPos(cursor.getCount()));
                playMusic(cursor.getString(0), cursor.getString(4));
                return true;
            }
        } catch(Exception e1) {
            LogUtil.e(TAG, "GNMusicUsecase searchLocalMusic(String artist) e1 = " + e1);
        } finally {
            try {
                cursor.close();
            } catch(Exception e2) {
                LogUtil.e(TAG, "GNMusicUsecase searchLocalMusic(String artist) e2 = " + e2);
            }
        }
        return false;
    }

    private int getPlayMusicPos(int count) {
        int radomNumber = new Random(System.currentTimeMillis()).nextInt();
        return Math.abs(radomNumber % count);
    }

    private boolean searchLocalMusic() {
        Cursor cursor = getCursor(MediaStore.Audio.Media.IS_MUSIC + "=1", null);

        if(null == cursor) {
            LogUtil.e(TAG, "GNMusicUsecase searchLocalMusic() null == cursor");
            return false;
        }
        try {
            if (cursor.moveToFirst()) {
                cursor.moveToPosition(getPlayMusicPos(cursor.getCount()));
                playMusic(cursor.getString(0), cursor.getString(4));
                return true;
            }
        } catch(Exception e1) {
            LogUtil.e(TAG, "GNMusicUsecase searchLocalMusic() e1 = " + e1);
        } finally {
            try {
                cursor.close();
            } catch(Exception e2) {
                LogUtil.e(TAG, "GNMusicUsecase searchLocalMusic() e2 = " + e2);
            }
        }
        return false;
    }

    private boolean getExistLocal(String singer, String song) {
        boolean existLocal = false;
        if (!TextUtils.isEmpty(song)) {
            existLocal = searchLocalMusic(song, singer);
        } else if (!TextUtils.isEmpty(singer)) {
            existLocal = searchLocalMusic(singer);
        } else {
            existLocal = searchLocalMusic();
        }
        LogUtil.d(TAG, "GNMusicUsecase getExistLocal existLocal = " + existLocal);
        return existLocal;
    }

    private String getTip(String singer, String song) {
        String tip = "";
        if (!TextUtils.isEmpty(singer) && !TextUtils.isEmpty(song)) {
            tip = String.format(mAppCtx.getString(R.string.music_search_tip3), singer, song);
        } else if (!TextUtils.isEmpty(singer)) {
            tip = String.format(mAppCtx.getString(R.string.music_search_tip1), singer);
        } else if (!TextUtils.isEmpty(song)) {
            tip = String.format(mAppCtx.getString(R.string.music_search_tip2), song);
        } else {
            tip = mAppCtx.getString(R.string.music_search_tip4);
        }

        LogUtil.d(TAG, "GNMusicUsecase getTip tip = " + tip);
        return tip;
    }

    private String getUrl(String singer, String song) {
        String url;
        StringBuffer encodedParams = new StringBuffer();
        try {
            if(!TextUtils.isEmpty(singer)) {
                encodedParams.append(URLEncoder.encode(singer, "UTF8"));
            }
            if(!TextUtils.isEmpty(song)) {
                encodedParams.append(" ");
                encodedParams.append(URLEncoder.encode(song, "UTF8"));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        if(isAmigoMusicSupportSearchNetMusic()) {
            url = GN_MUSIC_SEARCH_URL + "?bundle_extra_type=searchresult&searchStr=" + encodedParams.toString();
        } else {
            url = String.format(mAppCtx.getString(R.string.music_web_search), encodedParams.toString());
        }

        LogUtil.d(TAG, "GNMusicUsecase getUrl url = " + url);
        return url;
    }

    private void searchMusicFromNet(String url) {
        Uri uri = Uri.parse(url);
        if(isAmigoMusicSupportSearchNetMusic()) {
            setDataForAmigoMusicSearch(uri);
        } else {
            setDataForBrowserSearch(uri);
        }
        mAppCtx.startActivity(mIntent);
    }

    private void setDataForAmigoMusicSearch(Uri uri) {
        mIntent = new Intent(GN_MUSIC_ENTRY_ACTION, uri);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntentType = 1;
    }


    private void setDataForBrowserSearch(Uri uri) {
        mIntent = new Intent(Intent.ACTION_VIEW, uri);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.addCategory(Intent.CATEGORY_BROWSABLE);
        mIntent.setPackage(GN_BROWSABLE_PACKAGE_NAME);
        mIntentType = 1;
    }

    /**
     * 判断音乐版本号是否大于 GN_MUSIC_VERSION_NAME_SUPPORT_SEARCH_MUSIC,
     * 大于该版本时才支持通过语音搜索Amigo音乐的接口
     * @return
     */
    private boolean isAmigoMusicSupportSearchNetMusic() {
        int result = versionCompare(getAmigoMusicAppVersionName(), GN_MUSIC_VERSION_NAME_SUPPORT_SEARCH_MUSIC);
        boolean isSupport = result >= 0;
        LogUtil.d(TAG, "GNMusicUsecase isAmigoMusicSupportSearchNetMusic = " + isSupport);
        return isSupport;
    }

    private String getAmigoMusicAppVersionName() {
        String versionName = "0.0.0";
        try {
            versionName = mAppCtx.getPackageManager().getPackageInfo(
                    GN_MUSIC_PACKAGE_NAME, 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtil.d(TAG, "GNMusicUsecase getAmigoMusicAppVersionName versionName = " + versionName);
        return versionName;
    }

    /**
     *
     * @param versionName1
     * @param versionName2
     * @return versionName1 > versionName2 返回 1；
     * versionName1 < versionName2 返回 -1；
     * versionName1 = versionName2 返回 0；
     */
    private int versionCompare(String versionName1, String versionName2) {
        versionName1 = versionName1.toLowerCase();
        versionName2 = versionName2.toLowerCase();
        String[] currVersion = versionName1.split("\\.");
        String[] serVersion = versionName2.split("\\.");
        int length = Math.min(currVersion.length, serVersion.length);
        for(int i=0; i<length; i++) {
            String name1 = currVersion[i];
            String name2 = serVersion[i];
            if(name1.length() > name2.length()) {
                return 1;
            } else if (name1.length() < name2.length()) {
                return -1;
            }
            int result = name1.compareTo(name2);
            if(result == 0) {
                continue;
            }
            LogUtil.d(TAG, "GNMusicUsecase versionCompare result = " + result);
            return result;
        }
        return 0;
    }

    @Override
    public void handleDirective(DirectiveEntity payload) {
        LocalAudioPlayerDirectiveEntity gnmusicPayload = (LocalAudioPlayerDirectiveEntity) payload;
        LocalAudioPlayerDirectiveEntity.SearchMusicAction action = gnmusicPayload.getAction();
        procMusicAction(gnmusicPayload.getSinger(), gnmusicPayload.getSong());
    }

    @Override
    public void handleUiFeedback(String uri) {

    }

    @Override
    public String getAlias() {
        return "localplayer";
    }

}
