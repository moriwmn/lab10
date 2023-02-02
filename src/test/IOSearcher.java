package test;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class IOSearcher implements FileSearcher{

    private volatile boolean stop = false;
    @Override
    public boolean search(String word, String... fileNames) {
        for (String file : fileNames){
            if (!stop) {
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String line;
                    while (!stop && (line = br.readLine()) != null) {
                        if (line.contains(word)){
                            br.close();
                            return true;
                        }
                    }
                    br.close();
                } catch (IOException e) {return false;}                
            }
        }
        return false;
    }

    @Override
    public void stop() {

        stop = true;
    }

}
