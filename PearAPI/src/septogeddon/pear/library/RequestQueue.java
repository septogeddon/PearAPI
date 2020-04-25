package septogeddon.pear.library;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import septogeddon.pear.api.Packet;
import septogeddon.pear.packets.PacketDeliveredThrowable;
import septogeddon.pear.utils.Throw;

public class RequestQueue {

	public class Queue<T> {
		private CompletableFuture<T> future = new CompletableFuture<>();
		private long requestId;
		private T packet;

		public Queue(long id, T packet) {
			this.requestId = id;
			this.packet = packet;
		}

		public T get() {
			try {
				T object = timeout <= 0 ? future.get() : future.get(timeout, TimeUnit.MILLISECONDS);
				return object;
			} catch (ExecutionException e) {
				Throw.throwable(e.getCause() == null ? e : e.getCause());
				remove();
			} catch (InterruptedException | TimeoutException e) {
				Throw.throwable(e);
				remove();
			}
			
			return null;
		}
		
		public void remove() {
			queues.remove(this);
		}

		public T getPacket() {
			return packet;
		}
	}
	private long lastRequestId = 1;

	private long timeout = 0;
	private List<Queue<?>> queues = new ArrayList<>();

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
	
	@SuppressWarnings("unchecked")
	public synchronized void error(Packet packet,Throwable t) {
		System.out.println("cancelling "+packet);
		for (int i = queues.size() - 1; i >= 0; i--) {
			Queue<Object> queue = (Queue<Object>) queues.get(i);
			if (queue.requestId == packet.getRequestId()) {
				queue.future.completeExceptionally(t);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public synchronized void finish(Packet packet) {
		System.out.println("finishing "+packet);
		for (int i = queues.size() - 1; i >= 0; i--) {
			Queue<Object> queue = (Queue<Object>) queues.get(i);
			if (queue.requestId == packet.getRequestId()) {
				if (packet instanceof PacketDeliveredThrowable) {
					queue.future.completeExceptionally(((PacketDeliveredThrowable) packet).getThrown());
				} else {
					queue.future.complete(packet);
				}
				return;
			}
		}
		throw new IllegalStateException("cannot find packet with requestId " + packet.getRequestId());
	}

	public synchronized <T extends Packet> Queue<T> queue(T packet) {
		System.out.println("queueing "+packet);	
		Queue<T> queue = new Queue<>(lastRequestId, packet);
		if (packet.getMode() == Packet.MODE_CLIENT_TO_SERVER) {
			packet.setRequestId(lastRequestId++);
		}
		queues.add(queue);
		return queue;
	}

}
