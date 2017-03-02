package bramble.concurrency;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class BrambleThreadPool {
    
    private ThreadPoolExecutor executor;
    
    public BrambleThreadPool(){
	this.executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    }
    
    public void run(Runnable runnable){
	executor.execute(runnable);
    }
}
