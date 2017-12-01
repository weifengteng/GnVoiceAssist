package com.gionee.voiceassist.util.threadpool;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by twf on 2017/6/18.
 */

final class UIKitHandlerPost extends Handler {
    private final int ASYNC = 0x1;
    private final int SYNC = 0x2;
    private Queue<Runnable> asyncPool;
    private Queue<UIKitSyncPost> syncPool;
    private int maxMillisInsideHandleMessage;
    private boolean asyncActive;
    private boolean syncActive;

    public UIKitHandlerPost(Looper looper, int maxMillisInsideHandleMessage) {
        super(looper);
        this.maxMillisInsideHandleMessage = maxMillisInsideHandleMessage;
        asyncPool = new LinkedList<>();
        syncPool = new LinkedList<>();
    }

    public void dispose() {
        this.removeCallbacksAndMessages(null);
        this.asyncPool.clear();
        this.syncPool.clear();
    }

    public void async(Runnable runnable) {
        synchronized (asyncPool) {
            asyncPool.offer(runnable);
            if(!asyncActive) {
                asyncActive = true;
                if(!sendMessage(obtainMessage(ASYNC))) {
                    throw new RuntimeException("Could not send handler message");
                }
            }
        }
    }

    public void sync(UIKitSyncPost post) {
        synchronized (syncPool) {
            syncPool.offer(post);
            if(!syncActive) {
                syncActive = true;
                if(!sendMessage(obtainMessage(SYNC))) {
                    throw new RuntimeException("Could not handle message");
                }
            }
        }
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case ASYNC:
                // TODO:
                executeRunnableAsync();
                break;
            case SYNC:
                executeRunnableSync();
                // TODO:
                break;
            default:
                super.handleMessage(msg);
                break;
        }
        super.handleMessage(msg);
    }

    private void executeRunnableAsync() {
        boolean rescheduled = false;
        try {
            long started = SystemClock.uptimeMillis();
            while (true) {
                Runnable runnable = asyncPool.poll();
                if(runnable == null) {
                    synchronized (asyncPool) {
                        runnable = asyncPool.poll();
                        if(runnable == null) {
                            asyncActive = false;
                            return;
                        }
                    }
                }

                runnable.run();
                long timeInMethod = SystemClock.uptimeMillis() - started;
                if (timeInMethod >= maxMillisInsideHandleMessage) {
                    if(!sendMessage(obtainMessage(ASYNC))) {
                        throw new RuntimeException("Could not send handler message");
                    }
                    rescheduled = true;
                    return;
                }
            }
        } finally {
            asyncActive = rescheduled;
        }
    }

    private void executeRunnableSync() {
        boolean rescheduled = false;
        try {
            long started = SystemClock.uptimeMillis();
            while (true) {
                UIKitSyncPost post = syncPool.poll();
                if(post == null) {
                    synchronized (syncPool) {
                        post = syncPool.poll();
                        if(post == null) {
                            syncActive = false;
                            return;
                        }
                    }
                }
                post.run();
                long timeInMethod = SystemClock.uptimeMillis() - started;
                if(timeInMethod >= maxMillisInsideHandleMessage) {
                    if(!sendMessage(obtainMessage(SYNC))) {
                        throw new RuntimeException("Could not send handler message");
                    }
                    rescheduled = true;
                    return;
                }
            }

        } finally {
            syncActive = rescheduled;
        }
    }
}
