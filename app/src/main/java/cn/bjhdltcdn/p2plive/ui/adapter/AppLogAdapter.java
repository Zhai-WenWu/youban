package cn.bjhdltcdn.p2plive.ui.adapter;

import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.LogAdapter;
import com.orhanobut.logger.PrettyFormatStrategy;

/**
 * app 日志管理
 */

public class AppLogAdapter implements LogAdapter {


    private final FormatStrategy formatStrategy;

    public AppLogAdapter() {
        this.formatStrategy = PrettyFormatStrategy.newBuilder().build();
    }

    public AppLogAdapter(FormatStrategy formatStrategy) {
        this.formatStrategy = formatStrategy;
    }

    @Override
    public boolean isLoggable(int priority, String tag) {
        return false;
    }

    @Override
    public void log(int priority, String tag, String message) {
        formatStrategy.log(priority, tag, message);
    }
}
