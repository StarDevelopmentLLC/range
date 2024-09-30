package com.stardevllc.range;

public record Range<V> (long min, long max, V value) implements Comparable<Range<V>> {
    public boolean contains(long number) {
        return number >= min && number <= max;
    }

    @Override
    public int compareTo(Range<V> otherRange) {
        if (contains(otherRange.min()) || contains(otherRange.max())) {
            return 0;
        }
        
        if (max() < otherRange.min()) {
            return -1;
        }
        
        if (min() > otherRange.max()) {
            return 1;
        }
        
        return 0;
    }
}