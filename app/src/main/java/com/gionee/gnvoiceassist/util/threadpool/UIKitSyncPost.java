package com.gionee.gnvoiceassist.util.threadpool;

/**
 * Created by twf on 2017/6/18.
 */

// http://blog.csdn.net/qiujuer/article/details/41900879
final class UIKitSyncPost {
    boolean end = false;
    Runnable runnable;

    UIKitSyncPost(Runnable runnable) {
        this.runnable = runnable;
    }

    public void run() {
        // TODO:
        if(!end) {
            synchronized (this) {
                if(!end) {
                    runnable.run();
                    end = true;
                    try {
                        this.notifyAll();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void waitRun() {
        // TODO:
        if(!end) {
            synchronized (this) {
                if(!end) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void waitRun(int time, boolean cancel) {
        // TODO:
        if(!end) {
            synchronized (this) {
                if(!end) {
                    try {
                        this.wait(time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        if(!end && cancel) {
                            end = true;
                        }
                    }
                }
            }
        }
    }

}
