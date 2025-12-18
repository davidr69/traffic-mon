package net.lavacro.traffic_mon.process;

import net.lavacro.traffic_mon.model.Ulogd;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class SpawnThreads {
	private final HashAndPersist hashAndPersist;

	private final BlockingQueue<Runnable> threadQueue = new LinkedBlockingQueue<>();
	private final ThreadPoolExecutor threads = new ThreadPoolExecutor(
			128, 128, 0L, TimeUnit.SECONDS, threadQueue
	);

	public SpawnThreads(HashAndPersist hashAndPersist) {
		this.hashAndPersist = hashAndPersist;
	}

	public void process(List<Ulogd> messages) {
		threads.submit(hashAndPersist.hashAndStore(messages));
	}
}
