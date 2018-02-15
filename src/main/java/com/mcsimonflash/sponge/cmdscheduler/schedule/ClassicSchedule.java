package com.mcsimonflash.sponge.cmdscheduler.schedule;

import com.google.common.base.Preconditions;
import com.mcsimonflash.sponge.cmdscheduler.internal.Utils;
import org.spongepowered.api.util.ResettableBuilder;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class ClassicSchedule implements Schedule {

    private final long delay;
    private final boolean delayTicks;
    private final long interval;
    private final boolean intervalTicks;

    private ClassicSchedule(long delay, boolean delayTicks, long interval, boolean intervalTicks) {
        this.delay = delay;
        this.delayTicks = delayTicks;
        this.interval = interval;
        this.intervalTicks = intervalTicks;
    }

    @Override
    public long getDelay() {
        return delay;
    }

    @Override
    public long getDelay(Calendar calendar) {
        return delay;
    }

    @Override
    public long getInterval() {
        return interval;
    }

    /**
     * @return true if the delay is in ticks, else false
     */
    public boolean isDelayTicks() {
        return delayTicks;
    }

    /**
     * @return true if the interval is in ticks, else false
     */
    public boolean isIntervalTicks() {
        return intervalTicks;
    }

    @Override
    public String toString() {
        return "ClassicSchedule{" + (delayTicks ? "delay-ticks=" + delay : "delay=" + Utils.printTime(delay)) + (intervalTicks ? ", interval-ticks=" + interval : ", interval=" + Utils.printTime(interval)) + "}";
    }

    /**
     * Creates a new builder for a {@link ClassicSchedule}.
     *
     * @return the new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements ResettableBuilder<ClassicSchedule, Builder> {

        private long delay = 0;
        private boolean delayTicks = false;
        private long interval = 0;
        private boolean intervalTicks = false;

        /**
         * Sets this builder to have the same calendar and interval as the given
         * {@link ClassicSchedule}.
         *
         * @param value the existing ClassicSchedule
         * @return this builder
         */
        @Override
        public Builder from(ClassicSchedule value) {
            this.delay = value.getDelay();
            this.delayTicks = value.isDelayTicks();
            this.interval = value.getInterval();
            this.intervalTicks = value.isIntervalTicks();
            return this;
        }

        /**
         * Sets this builder to have no delay and no interval.
         *
         * @return this builder
         */
        @Override
        public Builder reset() {
            this.delay = 0;
            this.delayTicks = false;
            this.interval = 0;
            this.intervalTicks = false;
            return this;
        }

        /**
         * Sets the delay for this builder internally.
         *
         * @param delay the delay (in milliseconds/ticks)
         * @param delayTicks if the delay is ticks or not
         * @return this builder for chaining
         */
        private Builder setDelay(long delay, boolean delayTicks) {
            this.delay = delay;
            this.delayTicks = delayTicks;
            return this;
        }

        /**
         * Sets the delay for this builder in milliseconds.
         *
         * If the delay is negative, an {@link IllegalArgumentException} will
         * be thrown at {@link #build()}.
         *
         * @param delay the delay, in milliseconds
         * @return this builder for chaining
         */
        public Builder delay(long delay) {
            return setDelay(delay, false);
        }

        /**
         * Sets the delay for this builder as a time relative to the given
         * {@link TimeUnit}.
         *
         * If the delay is negative, an {@link IllegalArgumentException} will
         * be thrown at {@link #build()}.
         *
         * @param delay the delay, in the given unit
         * @param unit the unit of the delay
         * @return this builder for chaining
         * @throws IllegalArgumentException if the unit is null
         */
        public Builder delay(long delay, TimeUnit unit) throws IllegalArgumentException {
            return delay(unit.toMillis(delay));
        }

        /**
         * Sets the delay for this builder in ticks.
         *
         * If the delay is negative, an {@link IllegalArgumentException} will
         * be thrown at {@link #build()}.
         *
         * @param delay the delay (in ticks)
         * @return this builder for chaining
         */
        public Builder delayTicks(long delay) {
            return setDelay(delay, true);
        }

        /**
         * Sets the interval for this builder internally.
         *
         * @param interval the interval (in milliseconds/ticks)
         * @param intervalTicks if the interval is ticks or not
         * @return this builder for chaining
         */
        private Builder setInterval(long interval, boolean intervalTicks) {
            this.interval = interval;
            this.intervalTicks = intervalTicks;
            return this;
        }

        /**
         * Sets the interval for this builder in milliseconds.
         *
         * If the interval is negative, an {@link IllegalArgumentException} will
         * be thrown at {@link #build()}.
         *
         * @param interval the interval, in milliseconds
         * @return this builder for chaining
         */
        public Builder interval(long interval) {
            return setInterval(interval, false);
        }

        /**
         * Sets the interval for this builder as a time relative to the given
         * {@link TimeUnit}.
         *
         * If the interval is negative, an {@link IllegalArgumentException} will
         * be thrown at {@link #build()}.
         *
         * @param interval the interval, in the given unit
         * @param unit the unit of the interval
         * @return this builder for chaining
         * @throws IllegalArgumentException if the unit is null
         */
        public Builder interval(long interval, TimeUnit unit) throws IllegalArgumentException {
            return interval(unit.toMillis(interval));
        }

        /**
         * Sets the interval for this builder in ticks.
         *
         * If the interval is negative, an {@link IllegalArgumentException} will
         * be thrown at {@link #build()}.
         *
         * @param interval the interval (in ticks)
         * @return this builder for chaining
         */
        public Builder intervalTicks(long interval) {
            return setInterval(interval, true);
        }

        /**
         * Builds and returns the {@link ClassicSchedule} that correlates to
         * this builder.
         *
         * @return the schedule for this builder
         * @throws IllegalArgumentException if the delay or interval is negative
         */
        public ClassicSchedule build() throws IllegalArgumentException {
            Preconditions.checkArgument(delay >= 0, "Delay cannot be negative. | Delay:[" + delay + "]");
            Preconditions.checkArgument(interval >= 0, "Interval cannot be negative. | Interval:[" + interval + "]");
            return new ClassicSchedule(delay, delayTicks, interval, intervalTicks);
        }

    }

}