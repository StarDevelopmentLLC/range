package com.stardevllc.starlib.range;

import com.stardevllc.starlib.random.StarRandom;

import java.util.Random;

/**
 * This class allows generation of a random value in a RangeSet.
 *
 * @param <T> The type of the Value of the RangeSet
 */
public class RangeRandom<T> implements StarRandom<Long, T> {
    
    private static final Random RANDOM = new Random();

    private RangeSet<T> rangeSet;

    public RangeRandom(RangeSet<T> rangeSet) {
        this.rangeSet = rangeSet;
    }

    public T generate(Object... options) {
        return rangeSet.get(RANDOM.nextLong(rangeSet.getMin(), rangeSet.getMax() + 1));
    }

    @Override
    public Long getMinimum() {
        return rangeSet.getMin();
    }

    @Override
    public Long getMaximum() {
        return rangeSet.getMax();
    }

    @Override
    public void setMinimum(Long minimum) {
        //no-op
    }

    @Override
    public void setMaximum(Long maximum) {
        //no-op
    }
}
