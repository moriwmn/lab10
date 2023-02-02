package test;

import java.util.HashSet;

public class CacheManager {

    private HashSet<String> cache;
    private int max_size;
    private CacheReplacementPolicy crp;

    public CacheManager(int size, CacheReplacementPolicy crp) {

        cache = new HashSet<String>();
        this.max_size = size;
        this.crp = crp;
    }
	
    public boolean query(String query_word){

        return cache.contains(query_word);
    }

    public void add(String word){
        
        if(IsFull()){
            String to_remove = crp.remove();
            cache.remove(to_remove);
        }
        
        crp.add(word);
        cache.add(word);
    }

    private boolean IsFull(){
        return cache.size() == max_size;
    }
	

}
