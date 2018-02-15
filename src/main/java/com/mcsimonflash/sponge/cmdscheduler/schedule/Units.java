package com.mcsimonflash.sponge.cmdscheduler.schedule;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSortedSet;

import java.util.Calendar;
import java.util.stream.IntStream;

public enum Units {

    //TODO: Implementation of weekdays and/or years?
    MONTHS(Calendar.MONTH, 1, 12),
    DAYS(Calendar.DAY_OF_MONTH, 1, 31),
    HOURS(Calendar.HOUR_OF_DAY, 0, 23),
    MINUTES(Calendar.MINUTE, 0, 59),
    SECONDS(Calendar.SECOND, 0, 59),
    MILLISECONDS(Calendar.MILLISECOND, 0, 999);

    public final int constant;
    public final int lower;
    public final int upper;
    public final ImmutableSortedSet<Integer> values;

    Units(int constant, int lower, int upper) {
        this.constant = constant;
        this.lower = lower;
        this.upper = upper;
        this.values = ImmutableSortedSet.copyOf(IntStream.rangeClosed(lower, upper).boxed().toArray(Integer[]::new));
    }

    /**
     * Checks the given number against the lower and upper bounds of this unit,
     * throwing an {@link IllegalArgumentException} if they are out of range.
     *
     * @param number the number
     * @return the number (for chaining)
     * @throws IllegalArgumentException if the number is outside the range of
     *         this unit
     */
    public int checkBounds(int number) throws IllegalArgumentException {
        Preconditions.checkArgument(lower <= number && number <= upper, "Number '" + number + "' is out of range. | Range:[" + lower + "-" + upper + "] Unit:[" + name() + "]");
        return number;
    }

}