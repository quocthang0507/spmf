package ca.pfv.spmf.datastructures.collections.map;

import java.util.Arrays;
import java.util.NoSuchElementException;

import ca.pfv.spmf.datastructures.collections.list.ArrayListObject;
import ca.pfv.spmf.datastructures.collections.map.AMapIntToLong.Entry;
import ca.pfv.spmf.datastructures.collections.map.AMapIntToShort.AKeyIterator;
import ca.pfv.spmf.datastructures.collections.map.AMapIntToShort.AValueIterator;
import ca.pfv.spmf.datastructures.collections.map.MapIntToShort.KeyIterator;
import ca.pfv.spmf.datastructures.collections.map.MapIntToShort.ValueIterator;

/*
 * Copyright (c) 2023 Philippe Fournier-Viger
 *
 * This file is part of the SPMF DATA MINING SOFTWARE
 * (http://www.philippe-fournier-viger.com/spmf).
 *
 * SPMF is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * SPMF is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * SPMF. If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * A Hashmap, optimized to map integers to integers (primitive types). The hash
 * function is the modulo. The internal structure is an array, and collisions
 * are managed using a custom resizeable array list.
 *
 * @author Philippe Fournier-Viger
 * @see MapIntToInt
 */
public class AMapIntToInt extends MapIntToInt {

    /**
     * DEFAULT CAPACITY
     */
    private static final int DEFAULT_BUCKET_COUNT = 100;
    /**
     * Array to store the data
     */
    private ArrayListObject<Entry>[] buckets;
    /**
     * Number of elements in the map
     */
    private int elementCount;
    /**
     * DEFAULT COLLISION LIST SIZE
     */
    private int initialCollisionListSize = 3;

    /**
     * Constructor
     */
    public AMapIntToInt() {
        buckets = new ArrayListObject[DEFAULT_BUCKET_COUNT];
        elementCount = 0;
    }

    /**
     * Constructor
     *
     * @param initialCapacity the initial internal capacity
     */
    public AMapIntToInt(int initialCapacity) {
        elementCount = 0;
        buckets = new ArrayListObject[initialCapacity];
    }

    /**
     * Constructor
     *
     * @param initialCapacity          the initial internal capacity
     * @param initialCollisionListSize the initial size to be used for each
     *                                 collision list
     */
    public AMapIntToInt(int initialCapacity, int initialCollisionListSize) {
        elementCount = 0;
        buckets = new ArrayListObject[initialCapacity];
        this.initialCollisionListSize = initialCollisionListSize;
    }

    /**
     * Clear the map
     */
    public void clear() {
        Arrays.fill(buckets, null);
        elementCount = 0;
    }

    /**
     * Get the number of pairs in the map
     *
     * @return the number
     */
    public int size() {
        return elementCount;
    }

    /**
     * Check if this map is empty
     *
     * @return true if empty. Otherwise, false
     */
    public boolean isEmpty() {
        return elementCount == 0;
    }

    /**
     * Calculate the hash value of a key
     *
     * @param key the key
     * @return the hash value
     */
    protected int hash(int key) {
        return key % buckets.length;
    }

    /**
     * Check if a value is in the structure
     *
     * @param key the key
     * @return true if it is contained. Otherwise, false
     */
    public boolean containsKey(int key) {
        int initialIndex = hash(key);

        if (buckets[initialIndex] == null) {
            return false;
        }

        for (int i = 0; i < buckets[initialIndex].size(); i++) {
            Entry entry = buckets[initialIndex].get(i);
            if (entry.key == key) {
                return true;
            }
        }

        return false;
    }

    /**
     * Get a value from the map
     *
     * @param key the key
     * @return the value or -1 if not found
     */
    public int get(int key) {
        int initialIndex = hash(key);

        if (buckets[initialIndex] == null) {
            return -1;
        }

        for (int i = 0; i < buckets[initialIndex].size(); i++) {
            Entry entry = buckets[initialIndex].get(i);
            if (entry.key == key) {
                return entry.value;
            }
        }

        return -1;
    }

    @Override
    public void getAndIncreaseValueBy(int key, int valueToAdd) {
        int initialIndex = hash(key);

        // If this key is not in the map
        if (buckets[initialIndex] == null) {
            buckets[initialIndex] = new ArrayListObject<>(initialCollisionListSize);
            buckets[initialIndex].add(new Entry(key, valueToAdd));
            elementCount++;
            return;
        }

        for (int i = 0; i < buckets[initialIndex].size(); i++) {
            Entry entry = buckets[initialIndex].get(i);
            if (entry.key == key) {
                entry.value += valueToAdd;
                return;
            }
        }

        // otherwise check the next position
        buckets[initialIndex].add(new Entry(key, valueToAdd));
        elementCount++;
        return;
    }

    /**
     * Put a new value in the map
     *
     * @param key   the key
     * @param value the value
     */
    public void put(int key, int value) {
        int initialIndex = hash(key);

        // If this key is not in the map
        if (buckets[initialIndex] == null) {
            buckets[initialIndex] = new ArrayListObject<>(initialCollisionListSize);
            buckets[initialIndex].add(new Entry(key, value));
            elementCount++;
            return;
        }

        for (int i = 0; i < buckets[initialIndex].size(); i++) {
            Entry entry = buckets[initialIndex].get(i);
            if (entry.key == key) {
                entry.value = value;
                return;
            }
        }

        // otherwise check the next position
        buckets[initialIndex].add(new Entry(key, value));
        elementCount++;
        return;
    }

    /**
     * Remove a key from the hash table
     *
     * @param key the key
     * @return true if removed, false if not found
     */
    public boolean remove(int key) {
        int initialIndex = hash(key);

        // If this key is not in the map
        if (buckets[initialIndex] == null) {
            return false;
        }

        for (int i = 0; i < buckets[initialIndex].size(); i++) {
            Entry entry = buckets[initialIndex].get(i);
            if (entry.key == key) {
                buckets[initialIndex].removeAt(i);
                elementCount--;
                return true;
            }
        }
        return false;
    }

    /**
     * Get an iterator on this map
     *
     * @return an iterator
     */
    public EntryIterator iterator() {
        return new AEntryIterator();
    }

    // ================= ITERATOR
    // =======================================================

    /**
     * Get an iterator on this map
     *
     * @return an iterator
     */
    public KeyIterator iteratorForKeys() {
        return new AKeyIterator();
    }

    /**
     * Get an iterator on this map
     *
     * @return an iterator
     */
    public ValueIterator iteratorForValues() {
        return new AValueIterator();
    }
    // =====================================================================
    // =====================================================================

    /**
     * Inner entry class for storing key value pairs
     */
    public class Entry extends MapEntryIntToInt {
        /**
         * a key
         */
        public int key;
        /**
         * a value
         */
        public int value;

        /**
         * Constructor
         *
         * @param key   a key
         * @param value a value
         */
        public Entry(int key, int value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public int getKey() {
            // TODO Auto-generated method stub
            return key;
        }

        @Override
        public int getValue() {
            // TODO Auto-generated method stub
            return value;
        }

    }

    /**
     * Iterator for this Map. <br/>
     * <br/>
     * Note that the iterator does not check for concurrent modifications and is not
     * synchronized. It is designed to be used by a single thread, and the map
     * should not be modified while using the iterator except by calling the
     * remove() method provided by the iterator. If the map would be modified by
     * using other methods such as using add() or removeAt(), then the iterator will
     * not work properly.
     *
     * @author Philippe Fournier-Viger 2023
     */
    public class AEntryIterator extends EntryIterator {
        /**
         * index of the bucket for the next entry
         */
        private int bucketIndexNextEntry = 0;
        /**
         * index of the next entry in its bucket
         */
        private int arrayIndexNextEntry = 0;

        /**
         * index of the bucket for the current entry
         */
        private int bucketIndexCurrentEntry = -1;
        /**
         * index of the current entry in its bucket
         */
        private int arrayIndexCurrentEntry = -1;

        /**
         * Next entry
         */
        private Entry nextEntry = null;

        /**
         * Next entry
         */
        private Entry currentEntry = null;

        /**
         * Constructor
         */
        public AEntryIterator() {

            // If the map is empty, no need to do anything else
            if (elementCount == 0) {
                return;
            }

            // If the map is not empty, we need to find the next element
            for (; bucketIndexNextEntry < buckets.length; bucketIndexNextEntry++) {
                if (buckets[bucketIndexNextEntry] != null && buckets[bucketIndexNextEntry].size() != 0) {
                    nextEntry = buckets[bucketIndexNextEntry].get(0);
                    return;
                }
            }
        }

        /**
         * Get the next entry
         *
         * @return the next entry or throws NoSuchElementException if there are no next
         * entry
         */
        public Entry next() {
            if (nextEntry == null) {
                throw new NoSuchElementException();
            }
            // The next entry becomes the current entry
            currentEntry = nextEntry;
            bucketIndexCurrentEntry = bucketIndexNextEntry;
            arrayIndexCurrentEntry = arrayIndexNextEntry;

            // Then, we need to update the next entry.

            // If there is another entry in the same bucket, this will be the next entry
            if (arrayIndexNextEntry < buckets[bucketIndexNextEntry].size() - 1) {
                arrayIndexNextEntry++;
                nextEntry = buckets[bucketIndexNextEntry].get(arrayIndexNextEntry);
                return currentEntry;
            }

            // Otherwise, look at the next bucket
            arrayIndexNextEntry = 0;
            bucketIndexNextEntry++;
            for (; bucketIndexNextEntry < buckets.length; bucketIndexNextEntry++) {
                if (buckets[bucketIndexNextEntry] != null && buckets[bucketIndexNextEntry].size() != 0) {
                    nextEntry = buckets[bucketIndexNextEntry].get(0);
                    return currentEntry;
                }
            }

            // We did not find!
            nextEntry = null;

            return currentEntry;
        }

        /**
         * Check if there is a next entry
         *
         * @return true if there is. Otherwise, false.
         */
        public boolean hasNext() {
            return nextEntry != null;
        }

        /**
         * Remove the current entry
         *
         * @return the entry or throw IllegalStateException if there is no current
         * entry.
         */
        public void remove() {
            // If there are no entry, we cannot do this operation
            if (currentEntry == null) {
                throw new IllegalStateException();
            }

            // Remove the current entry
            buckets[bucketIndexCurrentEntry].removeAt(arrayIndexCurrentEntry);

            // If the next entry is in the same bucket, we have
            // to decrease the pointer to the next entry by 1
            // because removing the previous entry will move the elements from the array
            if (bucketIndexCurrentEntry == bucketIndexNextEntry) {
                arrayIndexNextEntry--;
            }

            // Decrease the total number of elements
            elementCount--;
        }
    }
    // =====================================================================
    // =====================================================================

    /**
     * Iterator for this Map. <br/>
     * <br/>
     * Note that the iterator does not check for concurrent modifications and is not
     * synchronized. It is designed to be used by a single thread, and the map
     * should not be modified while using the iterator except by calling the
     * remove() method provided by the iterator. If the map would be modified by
     * using other methods such as using add() or removeAt(), then the iterator will
     * not work properly.
     *
     * @author Philippe Fournier-Viger 2023
     */
    public class AKeyIterator extends KeyIterator {
        /**
         * index of the bucket for the next entry
         */
        private int bucketIndexNextEntry = 0;
        /**
         * index of the next entry in its bucket
         */
        private int arrayIndexNextEntry = 0;

        /**
         * index of the bucket for the current entry
         */
        private int bucketIndexCurrentEntry = -1;
        /**
         * index of the current entry in its bucket
         */
        private int arrayIndexCurrentEntry = -1;

        /**
         * Next entry
         */
        private Entry nextEntry = null;

        /**
         * Next entry
         */
        private Entry currentEntry = null;

        /**
         * Constructor
         */
        public AKeyIterator() {

            // If the map is empty, no need to do anything else
            if (elementCount == 0) {
                return;
            }

            // If the map is not empty, we need to find the next element
            for (; bucketIndexNextEntry < buckets.length; bucketIndexNextEntry++) {
                if (buckets[bucketIndexNextEntry] != null && buckets[bucketIndexNextEntry].size() != 0) {
                    nextEntry = buckets[bucketIndexNextEntry].get(0);
                    return;
                }
            }
        }

        /**
         * Get the next entry
         *
         * @return the next entry or throws NoSuchElementException if there are no next
         * entry
         */
        public int next() {
            if (nextEntry == null) {
                throw new NoSuchElementException();
            }
            // The next entry becomes the current entry
            currentEntry = nextEntry;
            bucketIndexCurrentEntry = bucketIndexNextEntry;
            arrayIndexCurrentEntry = arrayIndexNextEntry;

            // Then, we need to update the next entry.

            // If there is another entry in the same bucket, this will be the next entry
            if (arrayIndexNextEntry < buckets[bucketIndexNextEntry].size() - 1) {
                arrayIndexNextEntry++;
                nextEntry = buckets[bucketIndexNextEntry].get(arrayIndexNextEntry);
                return currentEntry.key;
            }

            // Otherwise, look at the next bucket
            arrayIndexNextEntry = 0;
            bucketIndexNextEntry++;
            for (; bucketIndexNextEntry < buckets.length; bucketIndexNextEntry++) {
                if (buckets[bucketIndexNextEntry] != null && buckets[bucketIndexNextEntry].size() != 0) {
                    nextEntry = buckets[bucketIndexNextEntry].get(0);
                    return currentEntry.key;
                }
            }

            // We did not find!
            nextEntry = null;

            return currentEntry.key;
        }

        /**
         * Check if there is a next entry
         *
         * @return true if there is. Otherwise, false.
         */
        public boolean hasNext() {
            return nextEntry != null;
        }

        /**
         * Remove the current entry
         *
         * @return the entry or throw IllegalStateException if there is no current
         * entry.
         */
        public void remove() {
            // If there are no entry, we cannot do this operation
            if (currentEntry == null) {
                throw new IllegalStateException();
            }

            // Remove the current entry
            buckets[bucketIndexCurrentEntry].removeAt(arrayIndexCurrentEntry);

            // If the next entry is in the same bucket, we have
            // to decrease the pointer to the next entry by 1
            // because removing the previous entry will move the elements from the array
            if (bucketIndexCurrentEntry == bucketIndexNextEntry) {
                arrayIndexNextEntry--;
            }

            // Decrease the total number of elements
            elementCount--;
        }
    }

    /**
     * Iterator for this Map. <br/>
     * <br/>
     * Note that the iterator does not check for concurrent modifications and is not
     * synchronized. It is designed to be used by a single thread, and the map
     * should not be modified while using the iterator except by calling the
     * remove() method provided by the iterator. If the map would be modified by
     * using other methods such as using add() or removeAt(), then the iterator will
     * not work properly.
     *
     * @author Philippe Fournier-Viger 2023
     */
    public class AValueIterator extends ValueIterator {
        /**
         * index of the bucket for the next entry
         */
        private int bucketIndexNextEntry = 0;
        /**
         * index of the next entry in its bucket
         */
        private int arrayIndexNextEntry = 0;

        /**
         * index of the bucket for the current entry
         */
        private int bucketIndexCurrentEntry = -1;
        /**
         * index of the current entry in its bucket
         */
        private int arrayIndexCurrentEntry = -1;

        /**
         * Next entry
         */
        private Entry nextEntry = null;

        /**
         * Next entry
         */
        private Entry currentEntry = null;

        /**
         * Constructor
         */
        public AValueIterator() {

            // If the map is empty, no need to do anything else
            if (elementCount == 0) {
                return;
            }

            // If the map is not empty, we need to find the next element
            for (; bucketIndexNextEntry < buckets.length; bucketIndexNextEntry++) {
                if (buckets[bucketIndexNextEntry] != null && buckets[bucketIndexNextEntry].size() != 0) {
                    nextEntry = buckets[bucketIndexNextEntry].get(0);
                    return;
                }
            }
        }

        /**
         * Get the next entry
         *
         * @return the next entry or throws NoSuchElementException if there are no next
         * entry
         */
        public int next() {
            if (nextEntry == null) {
                throw new NoSuchElementException();
            }
            // The next entry becomes the current entry
            currentEntry = nextEntry;
            bucketIndexCurrentEntry = bucketIndexNextEntry;
            arrayIndexCurrentEntry = arrayIndexNextEntry;

            // Then, we need to update the next entry.

            // If there is another entry in the same bucket, this will be the next entry
            if (arrayIndexNextEntry < buckets[bucketIndexNextEntry].size() - 1) {
                arrayIndexNextEntry++;
                nextEntry = buckets[bucketIndexNextEntry].get(arrayIndexNextEntry);
                return currentEntry.value;
            }

            // Otherwise, look at the next bucket
            arrayIndexNextEntry = 0;
            bucketIndexNextEntry++;
            for (; bucketIndexNextEntry < buckets.length; bucketIndexNextEntry++) {
                if (buckets[bucketIndexNextEntry] != null && buckets[bucketIndexNextEntry].size() != 0) {
                    nextEntry = buckets[bucketIndexNextEntry].get(0);
                    return currentEntry.value;
                }
            }

            // We did not find!
            nextEntry = null;

            return currentEntry.value;
        }

        /**
         * Check if there is a next entry
         *
         * @return true if there is. Otherwise, false.
         */
        public boolean hasNext() {
            return nextEntry != null;
        }

        /**
         * Remove the current entry
         *
         * @return the entry or throw IllegalStateException if there is no current
         * entry.
         */
        public void remove() {
            // If there are no entry, we cannot do this operation
            if (currentEntry == null) {
                throw new IllegalStateException();
            }

            // Remove the current entry
            buckets[bucketIndexCurrentEntry].removeAt(arrayIndexCurrentEntry);

            // If the next entry is in the same bucket, we have
            // to decrease the pointer to the next entry by 1
            // because removing the previous entry will move the elements from the array
            if (bucketIndexCurrentEntry == bucketIndexNextEntry) {
                arrayIndexNextEntry--;
            }

            // Decrease the total number of elements
            elementCount--;
        }
    }
    // =====================================================================

}