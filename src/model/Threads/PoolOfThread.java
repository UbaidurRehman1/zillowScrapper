package model.Threads;

import java.util.Vector;
import java.util.concurrent.ExecutorService;

public interface PoolOfThread
{
	final Vector<ExecutorService> poolOfPool = new Vector<ExecutorService>();
	
	/**
	 * this method add threadPool in the Vector
	 * @param threadPool
	 */
	public void addThreadPool(ExecutorService threadPool);
	
	public void clearAllThreadPool();
	
	public void shutDownAllThreadPoo();
}
