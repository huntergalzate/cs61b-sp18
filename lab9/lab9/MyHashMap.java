package lab9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  @author Your name here
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    private static final int DEFAULT_SIZE = 16;
    private static final double MAX_LF = 0.75;
    private final double MIN_LF = 0.25;

    private ArrayMap<K, V>[] buckets;
    private int size;

    private double loadFactor() {

        return (double) size / buckets.length;
    }

    private void resizeHashTable(int targetBucketsM) {
        MyHashMap<K,V> tempHashMap = new MyHashMap<>(targetBucketsM);
        int oldM = buckets.length;
        for (int i = 0; i < oldM; i++) {
            ArrayMap<K,V> mapI = buckets[i];
            for (K k : mapI.keySet()) {
                tempHashMap.put(k, mapI.get(k));
            }
        }
        this.buckets = tempHashMap.buckets;
        this.size = tempHashMap.size;
    }

    public MyHashMap() {
        this(DEFAULT_SIZE);
    }

    public MyHashMap(int mBuckets) {
        buckets = new ArrayMap[mBuckets];
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        this.size = 0;
        for (int i = 0; i < this.buckets.length; i += 1) {
            this.buckets[i] = new ArrayMap<>();
        }
    }

    /** Computes the hash function of the given key. Consists of
     *  computing the hashcode, followed by modding by the number of buckets.
     *  To handle negative numbers properly, uses floorMod instead of %.
     */
    private int hash(K key) {
        if (key == null) {
            return 0;
        }

        int numBuckets = buckets.length;
        return Math.floorMod(key.hashCode(), numBuckets);
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        int hashCode = hash(key);
        return buckets[hashCode].get(key);
    }

    /* Associates the specified value with the specified key in this map. */
    //insert the key Key. If it is already present, updates value to Value
    @Override
    public void put(K key, V value) {
        if (loadFactor() > MAX_LF) {
            int currentMBuckets = buckets.length;
            resizeHashTable(2*currentMBuckets);
        }
        int hashCode = hash(key);
        V itemAlreadyThere = buckets[hashCode].get(key);

        //if no item found, then we are increasing the size. else, we are just updating the (key, value) pair
        if (itemAlreadyThere == null) {
            size = size + 1;
        }
        buckets[hashCode].put(key, value);
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        Set<K> set = new HashSet<>();
        for (int i = 0; i < buckets.length; i++) {
            ArrayMap<K,V> mapI = buckets[i];
            set.addAll(mapI.keySet());
        }
        return set;
    }

    /* Removes the mapping for the specified key from this map if exists.
     * Not required for this lab. If you don't implement this, throw an
     * UnsupportedOperationException. */
    @Override
    public V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to remove() is null");
        }
        if (loadFactor() < MIN_LF && buckets.length > DEFAULT_SIZE) {
            int currentMBuckets = buckets.length;
            resizeHashTable((int) currentMBuckets/2);
        }
        int hashCode = hash(key);
        V returnItem = buckets[hashCode].remove(key);
        if (returnItem != null) {
            size = size - 1;
        }
        return returnItem;
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for this lab. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    @Override
    public V remove(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("argument to remove() is null");
        }
        if (loadFactor() < MIN_LF && buckets.length > DEFAULT_SIZE) {
            int currentMBuckets = buckets.length;
            resizeHashTable((int) currentMBuckets/2);
        }
        int hashCode = hash(key);
        V returnItem = buckets[hashCode].get(key);
        if (returnItem != null && returnItem.equals(value)) {
            buckets[hashCode].remove(key);
            size = size - 1;
        }
        return returnItem;
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }

}
