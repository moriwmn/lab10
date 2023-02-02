package test;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;


public class LFU implements CacheReplacementPolicy{

    private Map<String, Integer> frequency;
    private Map<String, Integer> cache;
    private PriorityQueue<String> heap;

    public LFU() {
        frequency = new HashMap<>();
        cache = new HashMap<>();
        heap = new PriorityQueue<>((a, b) -> frequency.get(a) - frequency.get(b));
    }

    @Override
    public void add(String word) {
        if (cache.containsKey(word)) {
            int count = frequency.get(word);
            frequency.put(word, count + 1);
            heap.remove(word);
            heap.offer(word);
        } else {
            cache.put(word, 1);
            frequency.put(word, 1);
            heap.offer(word);
        }
    }

    @Override
    public String remove() {
        return heap.poll();
    }
   

}
