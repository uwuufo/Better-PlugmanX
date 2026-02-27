package velocity.com.rylinaux.plugman.util;

import core.com.rylinaux.plugman.util.ThreadUtil;
import velocity.com.rylinaux.plugman.PlugManVelocity;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class VelocityThreadUtil implements ThreadUtil {

    @Override
    public void async(Runnable runnable) {
        PlugManVelocity.getInstance().getServer().getScheduler()
                .buildTask(PlugManVelocity.getInstance(), runnable)
                .schedule();
    }

    @Override
    public void sync(Runnable runnable) {
        async(runnable);
    }

    @Override
    public void syncRepeating(Runnable runnable, long delay, long period) {
        PlugManVelocity.getInstance().getServer().getScheduler()
                .buildTask(PlugManVelocity.getInstance(), runnable)
                .delay(Duration.ofMillis(delay))
                .repeat(Duration.ofMillis(period))
                .schedule();
    }

    @Override
    public void asyncRepeating(Runnable runnable, long delay, long period) {
        PlugManVelocity.getInstance().getServer().getScheduler()
                .buildTask(PlugManVelocity.getInstance(), runnable)
                .delay(Duration.ofMillis(delay))
                .repeat(Duration.ofMillis(period))
                .schedule();
    }
}