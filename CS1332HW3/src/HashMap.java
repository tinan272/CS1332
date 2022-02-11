import java.util.ArrayList;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.List;

/**
 * Your implementation of a HashMap.
 *
 * @author Tina Nguyen
 * @version 1.0
 * @userid mnguyen354
 * @GTID 903574748
 *
 * Collaborators: None
 *
 * Resources: None
 */
public class HashMap<K, V> {

    /*
     * The initial capacity of the HashMap when created with the
     * default constructor.
     *
     * DO NOT MODIFY THIS VARIABLE!
     */
    public static final int INITIAL_CAPACITY = 13;

    /*
     * The max load factor of the HashMap.
     *
     * DO NOT MODIFY THIS VARIABLE!
     */
    public static final double MAX_LOAD_FACTOR = 0.67;

    /*
     * Do not add new instance variables or modify existing ones.
     */
    private MapEntry<K, V>[] table;
    private int size;

    /**
     * Constructs a new HashMap.
     *
     * The backing array should have an initial capacity of INITIAL_CAPACITY.
     *
     * Use constructor chaining.
     */
    public HashMap() {
        this(INITIAL_CAPACITY);
    }

    /**
     * Constructs a new HashMap.
     *
     * The backing array should have an initial capacity of initialCapacity.
     *
     * You may assume initialCapacity will always be positive.
     *
     * @param initialCapacity the initial capacity of the backing array
     */
    public HashMap(int initialCapacity) {
        table = (MapEntry<K, V>[]) new MapEntry[initialCapacity];
        size = 0;
    }

    /**
     * Adds the given key-value pair to the map. If an entry in the map
     * already has this key, replace the entry's value with the new one
     * passed in.
     *
     * In the case of a collision, use external chaining as your resolution
     * strategy. Add new entries to the front of an existing chain, but don't
     * forget to check the entire chain for duplicate keys first.
     *
     * If you find a duplicate key, then replace the entry's value with the new
     * one passed in. When replacing the old value, replace it at that position
     * in the chain, not by creating a new entry and adding it to the front.
     *
     * Before actually adding any data to the HashMap, you should check to
     * see if the array would violate the max load factor if the data was
     * added. Resize if the load factor (LF) is greater than max LF (it is okay
     * if the load factor is equal to max LF). For example, let's say the
     * array is of length 5 and the current size is 3 (LF = 0.6). For this
     * example, assume that no elements are removed in between steps. If
     * another entry is attempted to be added, before doing anything else,
     * you should check whether (3 + 1) / 5 = 0.8 is larger than the max LF.
     * It is, so you would trigger a resize before you even attempt to add
     * the data or figure out if it's a duplicate. Be careful to consider the
     * differences between integer and double division when calculating load
     * factor.
     *
     * When regrowing, resize the length of the backing table to
     * (2 * old length) + 1. You must use the resizeBackingTable method to do so.
     *
     * Return null if the key was not already in the map. If it was in the map,
     * return the old value associated with it.
     *
     * @param key   the key to add
     * @param value the value to add
     * @return null if the key was not already in the map. If it was in the
     * map, return the old value associated with it
     * @throws java.lang.IllegalArgumentException if key or value is null
     */
    public V put(K key, V value) {
        if (key == null || value ==  null) {
            throw new IllegalArgumentException("The key or value cannot be null.");
        }
        if ((size + 1) / (double) table.length > MAX_LOAD_FACTOR) {
            resizeBackingTable(2 * table.length +  1);
        }
        int index = Math.abs(key.hashCode() % table.length);
        if (table[index] == null) {
            MapEntry<K, V> newEntry = new MapEntry<K, V>(key, value);
            table[index] = newEntry;
        } else {
            MapEntry<K, V> aEntry = table[index];
            while (aEntry != null) {
                if (aEntry.getKey().equals(key)) {
                    V oldV = aEntry.getValue();
                    aEntry.setValue(value);
                    return oldV;
                }
                aEntry = aEntry.getNext();
            }
            MapEntry<K, V> newElement = new MapEntry<K, V>(key, value);
            newElement.setNext(table[index]);
            table[index] = newElement;
        }
        size++;
        return null;
    }

    /**
     * Removes the entry with a matching key from the map.
     *
     * @param key the key to remove
     * @return the value associated with the key
     * @throws java.lang.IllegalArgumentException if key is null
     * @throws java.util.NoSuchElementException   if the key is not in the map
     */
    public V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        int index = Math.abs(key.hashCode() % table.length);
        MapEntry<K, V> curr;
        curr = table[index];
        MapEntry<K, V> beforeKey = null;
        if (curr != null && curr.getNext() == null && curr.getKey().equals(key)) {
            V oldValue = curr.getValue();
            table[index] = null;
            size--;
            return oldValue;
        }
        while (curr != null) {
            if (curr.getKey().equals(key)) {
                V oldValue = curr.getValue();
                MapEntry<K, V> newNext = null;
                if (curr.getNext() != null) {
                    if (beforeKey != null) {
                        newNext = curr.getNext();
                        beforeKey.setNext(newNext);
                    } else {
                        MapEntry<K, V> replaceEntry = curr.getNext();
                        curr.setNext(null);
                        table[index] = replaceEntry;
                    }
                } else {
                    if (beforeKey != null) {
                        beforeKey.setNext(null);
                    } else {
                        MapEntry<K, V> newEntry = curr.getNext();
                        curr.setNext(null);
                        table[index] = newEntry;
                    }
                }
                size--;
                return oldValue;
            }
            beforeKey = curr;
            curr = curr.getNext();
        }
        throw new NoSuchElementException("Key doesn't exist in map.");
    }

    /**
     * Gets the value associated with the given key.
     *
     * @param key the key to search for in the map
     * @return the value associated with the given key
     * @throws java.lang.IllegalArgumentException if key is null
     * @throws java.util.NoSuchElementException   if the key is not in the map
     */
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        int index = Math.abs(key.hashCode() % table.length);
        MapEntry<K, V> curr = table[index];
        while (curr != null) {
            if (curr.getKey().equals(key)) {
                return curr.getValue();
            }
            curr = curr.getNext();
        }
        throw new NoSuchElementException("Key doesn't exist in map.");
    }

    /**
     * Returns whether or not the key is in the map.
     *
     * @param key the key to search for in the map
     * @return true if the key is contained within the map, false
     * otherwise
     * @throws java.lang.IllegalArgumentException if key is null
     */
    public boolean containsKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        try {
            get(key);
        } catch (NoSuchElementException nsee) {
            return false;
        }
        return true;
    }

    /**
     * Returns a Set view of the keys contained in this map.
     *
     * Use java.util.HashSet.
     *
     * @return the set of keys in this map
     */
    public Set<K> keySet() {
        HashSet<K> setK = new HashSet<K>();
        MapEntry<K, V> curr;
        for (int i = 0; i < table.length; i++) {
            curr = table[i];
            if (curr != null && curr.getNext() == null) {
                setK.add(curr.getKey());
            } else {
                while (curr != null) {
                    setK.add(curr.getKey());
                    curr = curr.getNext();
                }
            }
        }
        return setK;
    }

    /**
     * Returns a List view of the values contained in this map.
     *
     * Use java.util.ArrayList or java.util.LinkedList.
     *
     * You should iterate over the table in order of increasing index and add
     * entries to the List in the order in which they are traversed.
     *
     * @return list of values in this map
     */
    public List<V> values() {
        List<V> arrayL = new ArrayList<>();
        MapEntry<K, V> curr;
        for (int i = 0; i < table.length; i++) {
            curr = table[i];
            if (curr != null && curr.getNext() == null) {
                arrayL.add(curr.getValue());
            } else {
                while (curr != null) {
                    arrayL.add(curr.getValue());
                    curr = curr.getNext();
                }
            }
        }
        return arrayL;
    }

    /**
     * Resize the backing table to length.
     *
     * Disregard the load factor for this method. So, if the passed in length is
     * smaller than the current capacity, and this new length causes the table's
     * load factor to exceed MAX_LOAD_FACTOR, you should still resize the table
     * to the specified length and leave it at that capacity.
     *
     * You should iterate over the old table in order of increasing index and
     * add entries to the new table in the order in which they are traversed.
     *
     * Since resizing the backing table is working with the non-duplicate
     * data already in the table, you shouldn't explicitly check for
     * duplicates.
     *
     * Hint: You cannot just simply copy the entries over to the new array.
     *
     * @param length new length of the backing table
     * @throws java.lang.IllegalArgumentException if length is less than the
     *                                            number of items in the hash
     *                                            map
     */
    public void resizeBackingTable(int length) {
        if (length < size) {
            throw new IllegalArgumentException("Length cannot be less than the number of items in table.");
        }
        MapEntry<K, V>[] newTable = (MapEntry<K, V>[]) new MapEntry[length];
        MapEntry<K, V> curr;
        int index;
        int count = 0;
        for (int i = 0; i < table.length; i++) {
            if (count == size) {
                break;
            }
            if (table[i] != null) {
                curr = table[i];
                while (curr != null) {
                    index = Math.abs(curr.getKey().hashCode() % newTable.length); // finds the index
                    if (newTable[index] == null) {
                        newTable[index] = curr;
                    } else {
                        curr.setNext(newTable[index]);
                        newTable[index] = curr;
                    }
                    count++;
                    curr = curr.getNext();
                }
            }
        }
        table = newTable;
    }

    /**
     * Clears the map.
     *
     * Resets the table to a new array of the initial capacity and resets the
     * size.
     */
    public void clear() {
        table =  (MapEntry<K, V>[]) new MapEntry[INITIAL_CAPACITY];
        size = 0;
    }

    /**
     * Returns the table of the map.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the table of the map
     */
    public MapEntry<K, V>[] getTable() {
        // DO NOT MODIFY THIS METHOD!
        return table;
    }

    /**
     * Returns the size of the map.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the size of the map
     */
    public int size() {
        // DO NOT MODIFY THIS METHOD!
        return size;
    }
}
