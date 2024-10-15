package com.stardevllc.range;

import java.util.Collection;
import java.util.LinkedList;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * This represents a group of ranges that are related. Ideally there should not be a gap between the values
 *
 * @param <V> The parameterized type of the value to represent
 */
public class RangeSet<V> implements Cloneable {
    protected SortedSet<Range<V>> ranges = new TreeSet<>();

    /**
     * Constructs an empty RangeSet
     */
    public RangeSet() {
    }

    /**
     * Constructs a RangeSet with an initial set of values. <br>
     * The ranges provided do no need to be sorted in any way. The internal collection does that automatically
     *
     * @param ranges The initial set of ranges for this RangeSet
     */
    public RangeSet(Collection<Range<V>> ranges) {
        this.ranges.addAll(ranges);
    }

    /**
     * Adds a Range to this RangeSet. This checks for existing values and prevents overlapping ranges.
     *
     * @param range The range to add
     */
    public RangeSet<V> add(Range<V> range) {
        if (this.ranges.isEmpty()) {
            this.ranges.add(range);
        }

        for (Range<V> existing : this.ranges) {
            if (existing.contains(range.min()) || existing.contains(range.max()) || range.contains(existing.min()) || range.contains(existing.max())) {
                return this;
            }
        }

        ranges.add(range);
        return this;
    }

    /**
     * Replaces a range that already has overlapping mins and maxes. <br>
     * If the range does not have any overlapping ranges, it is just added
     *
     * @param range The range to replace
     */
    public RangeSet<V> replace(Range<V> range) {
        if (this.ranges.isEmpty()) {
            this.ranges.add(range);
        }

        for (Range<V> existing : new TreeSet<>(this.ranges)) {
            if (existing.contains(range.min()) || existing.contains(range.max()) || range.contains(existing.min()) || range.contains(existing.max())) {
                this.ranges.remove(existing);
                this.ranges.add(range);
                return this;
            }
        }

        ranges.add(range);
        return this;
    }

    /**
     * Convenience method to add a range without having to create the instance directly. <br>
     * This just does the following: <code>add(new Range<>(min, max, value)</></code>
     *
     * @param min   The minimum value of the range
     * @param max   The maximum value of the range
     * @param value The value to be represented by this range.
     */
    public RangeSet<V> add(long min, long max, V value) {
        return add(new Range<>(min, max, value));
    }

    /**
     * Convenience method to add a range without having to know the previous max. <br>
     * This method grabs the last range (or 0 as starting) and adds one. This becomes the new range's min. <br>
     * The range's max becomes range min + max (the parameter)
     *
     * @param max   The amount above the previous max + 1
     * @param value The value represented by the new range.
     */
    public RangeSet<V> addMax(long max, V value) {
        Range<V> lastRange = ranges.last();

        long min = 0;
        if (lastRange != null) {
            min = lastRange.max() + 1;
        }

        long rangeMax = min + max;

        Range<V> range = new Range<>(min, rangeMax, value);
        return add(range);
    }

    /**
     * Convenience method to add a range without having to know the previous min. <br>
     * The method grabs the current first range's min (or 0 as starting) and subtracts one. This becomes the new range's max. <br>
     * The range's min becomes the new range max - min (the parameter)
     *
     * @param min   The amount below the previous min - 1
     * @param value The value represented by the new range.
     */
    public RangeSet<V> addMin(long min, V value) {
        Range<V> lastRange = ranges.last();

        long max = 0L;
        if (lastRange != null) {
            max = lastRange.min() - 1;
        }

        long rangeMin = max - min;

        Range<V> range = new Range<>(rangeMin, max, value);
        return add(range);
    }

    /**
     * Removes a range that has the <code>index</code> between the min and max values
     *
     * @param index An index between the min and max values of the range to be removed
     */
    public Range<V> remove(long index) {
        for (Range<V> range : this.ranges) {
            if (range.contains(index)) {
                ranges.remove(range);
                return range;
            }
        }

        return null;
    }

    /**
     * Removes the first range that is represented by the <code>value</code>
     *
     * @param value The value of the first range to be removed
     * @return The Range that was removed or null if none was removed.
     */
    public Range<V> remove(V value) {
        for (Range<V> range : this.ranges) {
            if (range.value().equals(value)) {
                ranges.remove(range);
                return range;
            }
        }

        return null;
    }

    /**
     * Gets the value represented by the index. <br>
     * The condition index >= range.min && index <= range.max is used for determining represented value.
     *
     * @param index The index of the value
     * @return The value if this RangeSet represents that value or null if it does not.
     */
    public V get(long index) {
        for (Range<V> range : this.ranges) {
            if (range.contains(index)) {
                return range.value();
            }
        }

        return null;
    }

    /**
     * Gets all of the ranges that represent this RangeSet. The returned collection is not backed by the main one and changes to either do not affect the other.
     *
     * @return All Ranges that this RangeSet represents.s
     */
    public Collection<Range<V>> getValues() {
        return new LinkedList<>(this.ranges);
    }

    /**
     * Gets the absolute minimum that this RangeSet represents. This does not take any gaps into account.
     *
     * @return The minimum index or Long.MAX_VALUE if none was found (empty RangeSet)
     */
    public long getMin() {
        long min = Long.MAX_VALUE;
        for (Range<V> range : ranges) {
            if (range.min() < min) {
                min = range.min();
            }
        }

        return min;
    }

    /**
     * Gets the absolute maximum that this RangeSet represents. This does not take any gaps into account.
     *
     * @return The maximum index or Long.MIN_VALUE if none was found (empty RangeSet)
     */
    public long getMax() {
        long max = Long.MIN_VALUE;
        for (Range<V> range : ranges) {
            if (range.max() > max) {
                max = range.max();
            }
        }
        return max;
    }
    
    @Override
    public RangeSet<V> clone() {
        RangeSet<V> clone = new RangeSet<>();
        for (Range<V> range : this.ranges) {
            clone.add(range.clone());
        }
        
        return clone;
    }

    /**
     * @return IntelliJ Generated toString method.
     */
    @Override
    public String toString() {
        return "RangeSet{" +
                "ranges=" + ranges +
                '}';
    }
}