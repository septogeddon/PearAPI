package septogeddon.pear;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import septogeddon.pear.api.Bridge;
import septogeddon.pear.api.Network;
import septogeddon.pear.api.Packet;
import septogeddon.pear.library.NetworkImpl;
import septogeddon.pear.packets.PacketDeliveredThrowable;
import septogeddon.pear.utils.EncryptUtils;
import septogeddon.pear.utils.Throw;

public class SocketBridge implements Bridge {

	private ExecutorService service = Executors.newCachedThreadPool();
	private int serverport;
	private ServerSocket socket;
	private Network network;
	private String host;
	private int port;
	private String token;

	public SocketBridge(String host, int port, int serverport, String token) {
		this.serverport = serverport;
		this.host = host;
		this.port = port;
		this.token = token;
		network = new NetworkImpl(this);
	}

	@Override
	public Network getNetwork() {
		return network;
	}

	protected void handle(Socket socket) {
		try (ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {
			Packet packet = (Packet) ois.readObject();
			String password = EncryptUtils.encrypt(token);
			Object pass = ois.readObject();
			if (pass.equals(password)) {
				network.dispatchPacket(packet);
			} else {
				send(new PacketDeliveredThrowable(new IllegalArgumentException("invalid token")).handleConversion()
						.setRequestId(packet.getRequestId()).setMode(Packet.MODE_SERVER_TO_CLIENT));
			}
		} catch (Throwable t) {
			Throw.throwable(t);
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				Throw.throwable(e);
			}
		}
	}

	protected void run() {
		try {
			socket = new ServerSocket(serverport);
			while (!socket.isClosed()) {
				Socket sock = socket.accept();
				service.submit(() -> handle(sock));
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	@Override
	public void send(Packet obj) {
		try (Socket socket = new Socket(host, port)) {
			socket.setKeepAlive(false);
			try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
				out.writeObject(obj);
				out.writeObject(EncryptUtils.encrypt(token));
			}
		} catch (Throwable e) {
			getNetwork().cancelPacket(obj, e);
		}
	}

	@Override
	public void shutdown() {
		network.close();
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void start() {
		service.submit(this::run);
		network.start();
	}

}
