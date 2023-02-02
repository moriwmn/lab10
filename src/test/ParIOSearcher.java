package test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ParIOSearcher {

    private IOSearcher ioSearcher;
    private ExecutorService  cachedPool;

    public ParIOSearcher() {

        this.ioSearcher = new IOSearcher();
        this.cachedPool = Executors.newCachedThreadPool();
    }

    public boolean search(String word, String... fileNames){
    
        CompletionService<Boolean> completionService = new ExecutorCompletionService<>(cachedPool);
        List<Future<Boolean>> futures = new ArrayList<>();
    
        for (String fileName : fileNames) {
            Future<Boolean> future = completionService.submit(() -> ioSearcher.search(word, fileName));
            futures.add(future);
        }
    
        for (int i = 0; i < fileNames.length; i++) {
            try {
                if(completionService.take().get())
                    return true;
            } catch (Exception e) {return false;}
        }
        return false;
    }

    public void stop(){
        ioSearcher.stop();
        cachedPool.shutdownNow();
    }

    @Override
    protected void finalize() throws Throwable {
        cachedPool.shutdown();
    }

}
