package septogeddon.pear;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.PluginMessageListener;

import septogeddon.pear.api.Bridge;
import septogeddon.pear.api.Network;
import septogeddon.pear.api.Packet;
import septogeddon.pear.library.NetworkImpl;
import septogeddon.pear.utils.SneakyThrow;

public class BukkitBridge implements Bridge, PluginMessageListener {

	private final String channel;
	private final Plugin plugin;
	private final Network network;
	private boolean queue;

	/***
	 * Initialize bukkit bridge
	 * @param channel the PluginMessage channel
	 * @param plugin the plugin owner
	 * @param queue should queue when there is no player online?
	 */
	public BukkitBridge(String channel, Plugin plugin, boolean queue) {
		this.channel = channel;
		this.plugin = plugin;
		network = new NetworkImpl(this);
		this.queue = queue;
	}

	public String getChannel() {
		return channel;
	}

	@Override
	public Network getNetwork() {
		return network;
	}

	public Plugin getPlugin() {
		return plugin;
	}

	public boolean isQueue() {
		return queue;
	}

	@Override
	public void onPluginMessageReceived(String arg0, Player arg1, byte[] arg2) {
		if (arg0.equals(getChannel())) {
			try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(arg2))) {
				Object packet = ois.readObject();
				if (packet instanceof Packet) {
					network.dispatchPacket((Packet) packet);
				} else {
					throw new IllegalArgumentException("invalid packet");
				}
			} catch (Throwable t) {
				SneakyThrow.sneakyThrow(t);
			}
		}
	}

	@Override
	public void send(Object obj) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try (ObjectOutputStream oos = new ObjectOutputStream(output)) {
			oos.writeObject(obj);
			if (queue) {
				getPlugin().getServer().sendPluginMessage(getPlugin(), getChannel(), output.toByteArray());
			} else {
				for (Player p : getPlugin().getServer().getOnlinePlayers()) {
					p.sendPluginMessage(getPlugin(), getChannel(), output.toByteArray());
					break;
				}
			}
		} catch (Throwable t) {
			SneakyThrow.sneakyThrow(t);
		}
	}

	public void setQueue(boolean queue) {
		this.queue = queue;
	}

	@Override
	public void shutdown() {
		Messenger mes = getPlugin().getServer().getMessenger();
		mes.unregisterOutgoingPluginChannel(getPlugin(), getChannel());
		mes.unregisterIncomingPluginChannel(getPlugin(), getChannel());
	}

	@Override
	public void start() {
		Messenger mes = getPlugin().getServer().getMessenger();
		mes.registerIncomingPluginChannel(getPlugin(), getChannel(), this);
		mes.registerOutgoingPluginChannel(getPlugin(), getChannel());
	}

}
