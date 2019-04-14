package model.Threads;

import java.util.concurrent.ExecutorService;

import javafx.application.Platform;

public class Pool implements PoolOfThread {

	@Override
	public void addThreadPool(ExecutorService threadPool)
	{
		poolOfPool.add(threadPool);
	}

	@Override
	public void clearAllThreadPool()
	{
		poolOfPool.clear();
	}

	@Override
	public void shutDownAllThreadPoo()
	{
		Platform.runLater(new Runnable()
		{
			
			@Override
			public void run()
			{
				for(ExecutorService threadPool : poolOfPool)
				{
					threadPool.shutdownNow();
				}				
			}
		});
	}

}
