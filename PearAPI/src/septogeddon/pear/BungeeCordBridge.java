package septogeddon.pear;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import septogeddon.pear.api.Bridge;
import septogeddon.pear.api.Network;
import septogeddon.pear.api.Packet;
import septogeddon.pear.library.NetworkImpl;
import septogeddon.pear.utils.SneakyThrow;

public class BungeeCordBridge implements Listener, Bridge {

	private final String channel;
	private final Plugin plugin;
	private final Network network;
	private final ServerInfo server;
	private boolean queue;

	/***
	 * Initialize BungeeCordBridge
	 * @param channel the PluginMessage channel
	 * @param plugin the plugin owner
	 * @param server the target server
	 * @param queue should queue when there is no player?
	 */
	public BungeeCordBridge(String channel, Plugin plugin, ServerInfo server, boolean queue) {
		this.channel = channel;
		this.plugin = plugin;
		network = new NetworkImpl(this);
		this.queue = queue;
		this.server = server;
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

	public ServerInfo getServer() {
		return server;
	}

	public boolean isQueue() {
		return queue;
	}

	@EventHandler
	public void onPluginMessageReceived(PluginMessageEvent e) {
		if (e.getTag().equals(getChannel())) {
			try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(e.getData()))) {
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
			server.sendData(getChannel(), output.toByteArray(), queue);
		} catch (Throwable t) {
			SneakyThrow.sneakyThrow(t);
		}
	}

	public void setQueue(boolean queue) {
		this.queue = queue;
	}

	@Override
	public void shutdown() {
		getPlugin().getProxy().unregisterChannel(getChannel());
		getPlugin().getProxy().getPluginManager().unregisterListener(this);
	}

	@Override
	public void start() {
		getPlugin().getProxy().registerChannel(getChannel());
		getPlugin().getProxy().getPluginManager().registerListener(getPlugin(), this);
	}
}
