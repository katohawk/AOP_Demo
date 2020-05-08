package com.netease.aop.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.netease.aop.login.annotation.ClickBehavior;
import com.netease.aop.login.annotation.LoginCheck;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "netease >>> ";
    private static ExecutorService threadPool = Executors.newFixedThreadPool(10, new NamedThreadFactory());

    public static class NamedThreadFactory implements ThreadFactory {

        /**
         * 原子操作保证每个线程都有唯一的
         */
        private static final AtomicInteger threadNumber = new AtomicInteger(1);

        private final AtomicInteger mThreadNum = new AtomicInteger(1);

        private final String prefix;

        private final boolean daemoThread;

        private final ThreadGroup threadGroup;

        public NamedThreadFactory() {
            this("hk-threadpool-" + threadNumber.getAndIncrement(), false);
        }

        public NamedThreadFactory(String prefix) {
            this(prefix, false);
        }


        public NamedThreadFactory(String prefix, boolean daemo) {
            this.prefix = !TextUtils.isEmpty(prefix) ? prefix + "-thread-" : "";
            daemoThread = daemo;
            SecurityManager s = System.getSecurityManager();
            threadGroup = (s == null) ? Thread.currentThread().getThreadGroup() : s.getThreadGroup();
        }

        @Override
        public Thread newThread(Runnable runnable) {
            String name = prefix + mThreadNum.getAndIncrement();
            Thread ret = new Thread(threadGroup, runnable, name, 0);
            ret.setDaemon(daemoThread);
            return ret;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // 登录点击事件（用户行为统计）
    @ClickBehavior("登录")
    public void login(View view) {
        Log.e(TAG, "模拟接口请求……验证通过，登录成功！");
        threadPool.execute(new MyRunnable());
    }

    // 用户行为统计（友盟统计？！后台要求自己统计）
    @ClickBehavior("我的专区")
    @LoginCheck
    public void area(View view) {
        Log.e(TAG, "开始跳转到 -> 我的专区 Activity");
        startActivity(new Intent(this, OtherActivity.class));
        new Thread(){
            @Override
            public void run() {
                super.run();
                Log.e(TAG,"zhuanqu hehe "+Thread.currentThread().getName());
            }
        }.start();
    }

    // 用户行为统计
    @ClickBehavior("我的优惠券")
    @LoginCheck
    public void coupon(View view) {
        Log.e(TAG, "开始跳转到 -> 我的优惠券 Activity");
        startActivity(new Intent(this, OtherActivity.class));
        new Thread("youhui"){
            @Override
            public void run() {
                super.run();
                Log.e(TAG,"youhui hehe "+Thread.currentThread().getName());
            }
        }.start();
    }

    // 用户行为统计
    @ClickBehavior("我的积分")
    @LoginCheck
    public void score(View view) {
        Log.e(TAG, "开始跳转到 -> 我的积分 Activity");
        startActivity(new Intent(this, OtherActivity.class));
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG,"jifen hehe "+Thread.currentThread().getName());
            }
        });
    }

    static class MyRunnable implements Runnable{

        @Override
        public void run() {
            Log.e(TAG,"MyRunnable hehe "+Thread.currentThread().getName());
        }
    }
}
