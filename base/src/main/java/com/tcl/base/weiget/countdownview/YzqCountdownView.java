package com.tcl.base.weiget.countdownview;

import android.content.Context;
import android.util.AttributeSet;

import java.util.Timer;
import java.util.TimerTask;


/**
 * @author : Yzq
 * time : 2021/1/11 19:02
 * 新增计时器线程，用作实时记录倒计时的时间戳，修复原Countdown在<code>{@link #onDetachedFromWindow()}</code>方法中调用了{@code stop()}方法，
 * 但并没有在<code>{@link #onAttachedToWindow()}</code>方法中恢复计时的问题，并且，计时器也没有实时的记录运行中的准确数值。
 * <p/>
 * booooo 2018/11/23 11:49
 */
public class YzqCountdownView extends CountdownView
{
    public YzqCountdownView(Context context)
    {
        super(context);
    }

    public YzqCountdownView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public YzqCountdownView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    private long time = 0;
    private boolean isStartingTask = false;
    private final Timer mTimer = new Timer();
    private final TimerTask task = new TimerTask()
    {
        @Override
        public void run()
        {
            time -= 1000;
        }
    };

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        this.start(time);
    }

    @Override
    public void start(long millisecond)
    {
        super.start(millisecond);
        time = millisecond;
        if (!isStartingTask)
        {
            mTimer.scheduleAtFixedRate(task, 0, 1000);
            isStartingTask = true;
        }
    }

    @Override
    public void updateShow(long ms)
    {
        super.updateShow(ms);
        time = ms;
    }

    public void finishTimer()
    {
        mTimer.cancel();
    }
}
