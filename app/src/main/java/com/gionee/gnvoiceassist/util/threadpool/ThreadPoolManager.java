package com.gionee.gnvoiceassist.util.threadpool;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by twf on 2017/6/18.
 */

// http://www.jianshu.com/p/275dda9c517d
public class ThreadPoolManager {
    private ExecutorService service;
    private ExecutorCompletionService completionService;
    private static ThreadPoolManager manager;

    private ThreadPoolManager() {
        int num = Runtime.getRuntime().availableProcessors();
        this.service = Executors.newFixedThreadPool(num * 2);
    }

    public static ThreadPoolManager getInstance() {
        if(manager == null) {
            synchronized (ThreadPoolManager.class) {
                if(manager == null) {
                    manager = new ThreadPoolManager();
                }
            }
        }
        return manager;
    }

    public void executeTask(Runnable runnable) {
        service.submit(runnable);
    }

    public <T>ThreadPoolManager submitTask(Callable<T> callable) {

        if(completionService == null) {
            completionService = new ExecutorCompletionService<T>(service);
        }
        completionService.submit(callable);

        /*final Future future = service.submit(callable);*/

        service.submit(new Runnable() {
            @Override
            public void run() {

            }
        });
        return manager;
    }

    public void destroy() {
        if(service != null && !service.isShutdown()) {
            service.shutdown();
            service = null;
        }

        completionService = null;
        manager = null;
        UIKit.dispose();
    }
}
