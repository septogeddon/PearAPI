package septogeddon.pear.library;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import septogeddon.pear.api.Packet;
import septogeddon.pear.packets.PacketDeliveredThrowable;
import septogeddon.pear.utils.SneakyThrow;

public class RequestQueue {

	private long lastRequestId = 1;
	private List<Queue<?>> queues = new ArrayList<>();
	public synchronized <T extends Packet> Queue<T> queue(T packet) {
		Queue<T> queue = new Queue<>(lastRequestId,packet);
		if (packet.getMode() == Packet.MODE_CLIENT_TO_SERVER) {
			packet.setRequestId(lastRequestId++);
		}
		queues.add(queue);
		return queue;
	}
	
	@SuppressWarnings("unchecked")
	public synchronized void finish(Packet packet) {
		for (int i = queues.size() - 1; i >= 0; i--) {
			Queue<Object> queue = (Queue<Object>)queues.get(i);
			if (queue.requestId == packet.getRequestId()) {
				if (packet instanceof PacketDeliveredThrowable) {
					queue.future.completeExceptionally(((PacketDeliveredThrowable) packet).getThrown());
				} else {
					queue.future.complete(packet);
				}
				return;
			}
		}
		throw new IllegalStateException("cannot find packet with requestId "+packet.getRequestId());
	}
	
	public static class Queue<T> {
		private CompletableFuture<T> future = new CompletableFuture<>();
		private long requestId;
		private T packet;
		public Queue(long id, T packet) {
			this.requestId = id;
			this.packet = packet;
		}
		public T getPacket() {
			return packet;
		}
		public T get() {
			try {
				T object = future.get();
				return object;
			}  catch (ExecutionException e) {
				SneakyThrow.sneakyThrow(e.getCause() == null ? e : e.getCause());
			} catch (InterruptedException e) {
				SneakyThrow.sneakyThrow(e);
			}
			return null;
		}
	}
	
}
