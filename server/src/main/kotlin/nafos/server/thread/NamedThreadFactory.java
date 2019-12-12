package nafos.server.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 可命名的线程工厂.
 *
 */
public class NamedThreadFactory implements ThreadFactory {
	private final AtomicInteger threadCounter = new AtomicInteger(1);
	private final String name;
	private final boolean hasSuffix;
	private final ThreadGroup group;

	public NamedThreadFactory(String name) {
		this(name, true);
	}

	public NamedThreadFactory(String name, boolean hasSuffix) {
		final SecurityManager securitymanager = System.getSecurityManager();
		this.group = securitymanager == null ? Thread.currentThread().getThreadGroup() : securitymanager.getThreadGroup();
		this.name = name;
		this.hasSuffix = hasSuffix;
	}

	@Override
	public Thread newThread(Runnable runnable) {
		StringBuilder threadName = new StringBuilder(56);
		threadName.append(name);
		if (hasSuffix) {
			threadName.append("-").append(threadCounter.getAndIncrement());
		}

		Thread thread = new Thread(group, runnable, threadName.toString());
		if (thread.isDaemon()) {
			thread.setDaemon(false);
		}
		if (thread.getPriority() != Thread.NORM_PRIORITY) {
			thread.setPriority(Thread.NORM_PRIORITY);
		}
		return thread;
	}
}