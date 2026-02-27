package bukkit.com.rylinaux.plugman.util;

/*
 * #%L
 * PlugMan
 * %%
 * Copyright (C) 2010 - 2015 PlugMan
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import bukkit.com.rylinaux.plugman.PlugManBukkit;
import core.com.rylinaux.plugman.util.ThreadUtil;
import org.bukkit.Bukkit;

import java.util.concurrent.TimeUnit;

/**
 * Utility class for threading.
 *
 * @author rylinaux
 */
public class BukkitThreadUtil implements ThreadUtil {

    /**
     * Returns true when this JVM is running inside a Folia server.
     * Folia adds {@code io.papermc.paper.threadedregions.RegionizedServer}.
     */
    private static boolean isFolia() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }

    /**
     * Run a task in a separate thread.
     *
     * @param runnable the task.
     */
    @Override
    public void async(Runnable runnable) {
        if (isFolia()) {
            new com.tcoded.folialib.FoliaLib(PlugManBukkit.getInstance())
                    .getScheduler().runAsync((ignored) -> runnable.run());
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(PlugManBukkit.getInstance(), runnable);
    }

    /**
     * Run a task in the main thread.
     *
     * @param runnable the task.
     */
    @Override
    public void sync(Runnable runnable) {
        if (isFolia()) {
            new com.tcoded.folialib.FoliaLib(PlugManBukkit.getInstance())
                    .getScheduler().runLater(runnable, 0L);
            return;
        }
        Bukkit.getScheduler().runTask(PlugManBukkit.getInstance(), runnable);
    }

    @Override
    public void syncRepeating(Runnable runnable, long delay, long period) {
        if (isFolia()) {
            new com.tcoded.folialib.FoliaLib(PlugManBukkit.getInstance())
                    .getScheduler().runTimer(runnable, delay, period, TimeUnit.MILLISECONDS);
            return;
        }
        Bukkit.getScheduler().scheduleSyncRepeatingTask(PlugManBukkit.getInstance(), runnable, delay / 1000 / 20, period / 1000 / 20);
    }

    @Override
    public void asyncRepeating(Runnable runnable, long delay, long period) {
        if (isFolia()) {
            new com.tcoded.folialib.FoliaLib(PlugManBukkit.getInstance())
                    .getScheduler().runTimerAsync(runnable, delay, period, TimeUnit.MILLISECONDS);
            return;
        }
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(PlugManBukkit.getInstance(), runnable, delay / 1000 / 20, period / 1000 / 20);
    }
}
