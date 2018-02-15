package com.mcsimonflash.sponge.cmdscheduler.schedule;

import com.google.common.base.Preconditions;
import com.google.common.collect.*;
import org.spongepowered.api.util.ResettableBuilder;

import java.time.YearMonth;
import java.util.*;

public class CronSchedule implements Schedule {

    //TODO: Add an 'isRegular' option that calculates an interval and verifies.
    //This here means that instead of recalculating and restarting the task for
    //each match, the delay is calculated once when the task starts. An interval
    //(GCF between matches) is set and the task's consumer checks to see if the
    //current time is a match. This will increase the performance of tasks with
    //a predictable schedule and (greatly) decrease the performance of tasks
    //with an eccentric schedule.

    private final ImmutableMap<Units, ImmutableSortedSet<Integer>> sets;

    private CronSchedule(ImmutableMap<Units, ImmutableSortedSet<Integer>> sets) {
        this.sets = sets;
    }

    //TODO: Allow for year wrapping
    //TODO: Modify a Calendar instead of an array? //Tried it. Not easy.
    @Override
    public long getDelay(Calendar calendar) {
        int[] times = new int[6];
        boolean wrap = false;
        try {
            for (Units unit : Units.values()) {
                wrap = calculateNextTime(times, unit, wrap ? 0 : calendar.get(unit.constant) + (unit == Units.MONTHS ? 1 : 0)) || wrap;
            }
        } catch (IllegalArgumentException e) {
            return -1;
        }
        Calendar matched = (Calendar) calendar.clone();
        for (Units unit : Units.values()) {
            matched.set(unit.constant, times[unit.ordinal()] - (unit == Units.MONTHS ? 1 : 0));
        }
        return matched.getTimeInMillis() - calendar.getTimeInMillis();
    }

    @Override
    public long getInterval() {
        return -1;
    }

    /**
     * A helper method for calculating the next match of the schedule.
     *
     * @param times the array of matched times
     * @param unit the unit being processed
     * @param from the starting value for calculations
     * @return whether the time has wrapped from this unit
     */
    private boolean calculateNextTime(int[] times, Units unit, int from) {
        times[unit.ordinal()] = Optional.ofNullable(sets.getOrDefault(unit, unit.values).ceiling(from)).orElse(-1);
        if (times[unit.ordinal()] == -1 || (unit == Units.DAYS && !YearMonth.now().withMonth(times[0]).isValidDay(times[1]))) {
            Preconditions.checkArgument(unit != Units.MONTHS, "Year wrap.");
            calculateNextTime(times, Units.values()[unit.ordinal() - 1], times[unit.ordinal() - 1] + 1);
            calculateNextTime(times, unit, unit.lower);
            return true;
        }
        return times[unit.ordinal()] != from;
    }

    /**
     * Returns true if the given unit is defined for this schedule.
     *
     * @param unit the unit of the schedule
     * @return whether the unit is defined
     */
    public boolean hasUnit(Units unit) {
        return sets.containsKey(unit);
    }

    /**
     * Returns the set of times accepted for the given unit. If the unit is not
     * defined ({@link #hasUnit} returns false) the set of all possible values
     * for the given unit is returned {@code unit.values}.
     *
     * The set returned is immutable and should not be changed.
     *
     * @param unit the unit of the schedule
     * @return the set of accepted times
     */
    public ImmutableSet<Integer> getUnit(Units unit) {
        return sets.getOrDefault(unit, unit.values);
    }

    @Override
    public String toString() {
        List<String> sets = Lists.newArrayList();
        for (Units unit : Units.values()) {
            if (hasUnit(unit)) {
                sets.add(unit.name().toLowerCase() + "=" + getUnit(unit));
            }
        }
        return "CronSchedule{" + String.join(", ", sets) + "}";
    }

    /**
     * Creates a new builder for a {@link CronSchedule}.
     *
     * @return the new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements ResettableBuilder<CronSchedule, Builder> {

        private final EnumMap<Units, ImmutableSortedSet<Integer>> sets = Maps.newEnumMap(Units.class);

        /**
         * Sets this builder to have the same schedule as the given
         * {@link CronSchedule}.
         *
         * @param value the existing CronSchedule
         * @return this builder
         */
        @Override
        public Builder from(CronSchedule value) {
            for (Units unit : Units.values()) {
                if (value.hasUnit(unit)) {
                    setUnit(unit, value.getUnit(unit));
                }
            }
            return this;
        }

        /**
         * Sets this builder to have no units set (accepts all values).
         *
         * @return this builder
         */
        @Override
        public Builder reset() {
            sets.clear();
            return this;
        }

        /**
         * Sets the {@link Set} for the given {@link Units} unit.
         *
         * A valid set may not be empty and must have all it's values within the
         * inclusive range of unit.lower and unit.upper. This is defined by
         * {@code !set.isEmpty() && unit.values.containsAll(set)}.
         *
         * Any unit that is not set is presumed to represent all values. If a
         * provided set contains all values, it will not be added.
         *
         * @param unit the unit to be set
         * @param set the set to encode the schedule
         * @return this builder for chaining
         * @throws IllegalArgumentException if the unit is null, the set is
         *         null, or the set is invalid
         */
        public Builder setUnit(Units unit, Set<Integer> set) throws IllegalArgumentException {
            Preconditions.checkArgument(!set.isEmpty(), "Set must be defined.");
            Preconditions.checkArgument(unit.values.containsAll(set), "Set contains illegal values. | Values:[" + Sets.difference(unit.values, set) + "]");
            if (!unit.values.equals(set)) {
                sets.put(unit, ImmutableSortedSet.copyOf(set));
            }
            return this;
        }

        /**
         * Builds a set and sets it for the given {@link Units} unit.
         *
         * @param unit the unit to be set
         * @param values the values to include in the set
         * @return this builder for chaining
         * @throws IllegalArgumentException if the unit is null or the set is
         *         invalid
         */
        public Builder setUnit(Units unit, Integer... values) throws IllegalArgumentException {
            return setUnit(unit, Sets.newHashSet(values));
        }

        /**
         * Builds a set from the given modifier and sets it for the given
         * {@link Units} unit.
         *
         * @param unit the unit to be set
         * @param modifier the modifier representing this unit
         * @return this builder for chaining
         * @throws IllegalArgumentException if unit is null, the modifier is
         *         null, empty, or could not be parsed, or the set is invalid
         */
        public Builder setUnit(Units unit, String modifier) throws IllegalArgumentException {
            return setUnit(unit, Modifiers.buildSet(unit, modifier));
        }

        /**
         * Sets the {@link Set} for the given {@link Units} unit as the lower
         * bound of it's value range, defined by {@code unit.lower}.
         *
         * @param units the units to be set
         * @return this builder for chaining
         */
        public Builder setMin(Units... units) throws IllegalArgumentException {
            for (Units unit : units) {
                setUnit(unit, Sets.newHashSet(unit.lower));
            }
            return this;
        }

        /**
         * Builds a schedule from the provided string. This call is equivalent
         * to separating the string into modifiers and calling {@link #setUnit}
         * for each unit and modifier.
         *
         * The schedule is expected to contain modifiers for each unit in
         * ascending order (based on the order defined in {@link Units}.
         *
         * Modifiers are separated by a " " character.
         *
         * @param schedule the string representing this schedule
         * @return this builder for chaining
         * @throws IllegalArgumentException if the schedule is invalid
         */
        public Builder schedule(String schedule) throws IllegalArgumentException {
            Modifiers.buildSchedule(schedule).forEach(this::setUnit);
            return this;
        }

        /**
         * Builds and returns the {@link CronSchedule} that correlates to this
         * builder.
         *
         * If the schedule is invalid (any of the sets are invalid), an
         * {@link IllegalArgumentException} is throw. See above for validity.
         *
         * @return the Cron for this builder
         * @throws IllegalArgumentException if the schedule is invalid
         */
        public CronSchedule build() throws IllegalArgumentException {
            return new CronSchedule(Maps.immutableEnumMap(sets));
        }

    }

}