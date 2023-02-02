package test;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class BloomFilter {
	
private BitSet bitSet;
private List<MessageDigest> hashes;

public BloomFilter(int size, String... algs) {

    this.bitSet = new BitSet(size);
    this.hashes = new ArrayList<>(algs.length);

    for( String hash : algs){
        try {
            hashes.add(MessageDigest.getInstance(hash));
        } catch (NoSuchAlgorithmException e) {}
    }

}

public void add(String word){

    for (MessageDigest hash : hashes)
        bitSet.set(calcHashBit(hash, word));
    
}

public boolean contains(String word){
    for (MessageDigest hash : hashes)
        if (!bitSet.get(calcHashBit(hash, word)))
            return false;
            
    return true;
}

public int calcHashBit(MessageDigest hashFunc, String word){
    byte[] bytes = hashFunc.digest(word.getBytes());
    BigInteger bigInteger = new BigInteger(1, bytes);
    return Math.abs(bigInteger.intValue()) % bitSet.size();
}

@Override
public String toString() {
    StringBuilder retstr = new StringBuilder();
    for (int i=0; i<bitSet.length();i++){
        retstr.append(bitSet.get(i)? '1' : '0');
    }

    return retstr.toString();
    
}

}
