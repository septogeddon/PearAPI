package septogeddon.pear.library;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.bukkit.Bukkit;
import org.spigotmc.SneakyThrow;

import septogeddon.pear.BukkitPear;

public class BukkitThread {

	public static void async(Runnable r) {
		Bukkit.getScheduler().runTaskAsynchronously(BukkitPear.getPlugin(BukkitPear.class), r);
	}

	public static <T> T get(Callable<T> t) {
		try {
			return Bukkit.getScheduler().callSyncMethod(BukkitPear.getPlugin(BukkitPear.class), t).get();
		} catch (InterruptedException | ExecutionException e) {
			SneakyThrow.sneaky(e);
			return null; // dead code
		}
	}

	public static void run(Runnable r) {
		Bukkit.getScheduler().runTask(BukkitPear.getPlugin(BukkitPear.class), r);
	}

}
