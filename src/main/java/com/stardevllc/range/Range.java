package com.stardevllc.range;

import java.lang.reflect.Method;

public record Range<V> (long min, long max, V value) implements Comparable<Range<V>>, Cloneable {
    
    private final static Method cloneMethod;
    
    static {
        try {
            cloneMethod = Object.class.getDeclaredMethod("clone");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
    
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

    @Override
    public Range<V> clone() {
        if (this.value instanceof Cloneable) {
            try {
                return new Range<>(this.min, this.max, (V) cloneMethod.invoke(this.value));
            } catch (Exception e) {}
        }
        
        return new Range<>(min, max, value);
    }
}