package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Dictionary {

    private CacheManager existWords;
    private CacheManager missedWords;
    private BloomFilter bloomFilter;
    private ParIOSearcher pIoSearcher;
    private String[] files;

    public Dictionary(String...fileNames) {
        CacheReplacementPolicy lru = new LRU() ;
        CacheReplacementPolicy lfu = new LFU() ;
        this.files = fileNames;
        this.pIoSearcher = new ParIOSearcher();
        this.existWords = new CacheManager(400, lru );
        this.missedWords = new CacheManager(100, lfu );
        this.bloomFilter = new BloomFilter(256,"MD5","SHA1");

        for (String file : files){
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                String[] words;
                while ( (line = br.readLine()) != null) {
                    words = line.split(" ");
                    for (String word : words)
                        bloomFilter.add(word);
                }
                br.close();
            } catch (IOException e) {return;} 
        }

    }

    public boolean query(String word){

        if (existWords.query(word))
            return true;
        if (missedWords.query(word))
            return false;
        if (bloomFilter.contains(word)){
            existWords.add(word);
            return true;
        }else {
            missedWords.add(word);
            return false;
        }
    }

    public boolean challenge(String word){

        if (pIoSearcher.search(word, files)){
            existWords.add(word);
            return true;
        }else{
            missedWords.add(word);
            return false;  
        }
    }

    public void close(){
        pIoSearcher.stop();
    }

}
