package com.mcsimonflash.sponge.cmdscheduler.schedule;

import com.google.common.base.Preconditions;
import com.mcsimonflash.sponge.cmdscheduler.internal.Utils;
import org.spongepowered.api.util.ResettableBuilder;

import javax.annotation.Nullable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class CalendarSchedule implements Schedule {

    public static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss.SSS");

    private final Calendar calendar;
    private final long interval;

    private CalendarSchedule(Calendar calendar, long interval) {
        this.calendar = calendar;
        this.interval = interval;
    }

    @Override
    public long getDelay(Calendar calendar)  {
        return this.calendar.getTimeInMillis() - calendar.getTimeInMillis();
    }

    @Override
    public long getInterval() {
        return interval;
    }

    /**
     * @return a clone of this schedule's calendar.
     */
    public Calendar getCalendar() {
        return (Calendar) calendar.clone();
    }

    /**
     * @return this schedule's calendar time in milliseconds.
     */
    public long getTime() {
        return calendar.getTimeInMillis();
    }

    /**
     * @return this schedule's calendar time formatted with {@link #FORMAT}.
     */
    public String getDate() {
        return CalendarSchedule.FORMAT.format(calendar.getTime());
    }

    @Override
    public String toString() {
        return "CalendarSchedule{date=" + getDate() + ", interval=" + Utils.printTime(interval) + "}";
    }

    /**
     * Creates a new builder for a {@link CalendarSchedule}.
     *
     * @return the new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements ResettableBuilder<CalendarSchedule, Builder> {

        @Nullable private Calendar calendar;
        private long interval = 0;

        /**
         * Sets this builder to have the same calendar and interval as the given
         * {@link CalendarSchedule}.
         *
         * @param value the existing CalendarSchedule
         * @return this builder
         */
        @Override
        public Builder from(CalendarSchedule value) {
            this.calendar = value.getCalendar();
            this.interval = value.getInterval();
            return this;
        }

        /**
         * Sets this builder to have an undefined calendar and no interval.
         *
         * @return this builder
         */
        @Override
        public Builder reset() {
            this.calendar = null;
            this.interval = 0;
            return this;
        }

        /**
         * Sets the {@link Calendar} for this builder. This value must be set.
         *
         * If the calendar time has passed, the schedule will no longer match
         * and a task with this schedule will not be started.
         *
         * @param calendar the calendar to be set
         * @return this builder
         */
        public Builder calendar(Calendar calendar) {
            this.calendar = (Calendar) calendar.clone();
            return this;
        }

        /**
         * Sets the time in milliseconds. This is equivalent to setting the time
         * using {@link Calendar#setTimeInMillis}.
         *
         * @param time the time, in milliseconds
         * @return this builder
         */
        public Builder time(long time) {
            this.calendar = Calendar.getInstance();
            this.calendar.setTimeInMillis(time);
            return this;
        }

        /**
         * Parses the time from a String using {@link #FORMAT}.
         *
         * If a {@link ParseException} is thrown by the formatter, it
         * will be rethrown as the cause of an {@link IllegalArgumentException}.
         *
         * @param date the formatted date
         * @return this builder
         * @throws IllegalArgumentException if the date is not defined or cannot
         *         be parsed
         */
        public Builder date(String date) {
            Preconditions.checkArgument(!date.isEmpty(), "Date must be defined.");
            try {
                time(CalendarSchedule.FORMAT.parse(date).getTime());
                return this;
            } catch (ParseException e) {
                throw new IllegalArgumentException("Date does not match format. | Date:[" + date + "]", e);
            }
        }

        /**
         * Sets the interval for this builder in milliseconds.
         *
         * If the interval is negative, an {@link IllegalArgumentException} will
         * be thrown at {@link #build()}.
         *
         * @param interval the interval
         * @return this builder
         */
        public Builder interval(long interval) {
            this.interval = interval;
            return this;
        }

        /**
         * Sets the interval for this builder in the given {@link TimeUnit}.
         *
         * If the interval is negative, an {@link IllegalArgumentException} will
         * be thrown at {@link #build()}.
         *
         * @param interval the interval, in the given unit
         * @param unit the unit of the interval
         * @return this builder
         * @throws IllegalArgumentException if the unit is null
         */
        public Builder interval(long interval, TimeUnit unit) throws IllegalArgumentException {
            return interval(unit.toMillis(interval));
        }

        /**
         * Builds and returns the {@link CalendarSchedule} that correlates to
         * this builder.
         *
         * @return the new Calendar for this builder
         * @throws IllegalArgumentException if the calendar is not defined or
         *     the interval is negative
         */
        public CalendarSchedule build() throws IllegalArgumentException {
            Preconditions.checkArgument(calendar != null, "Calendar must be defined.");
            Preconditions.checkArgument(interval >= 0, "Interval cannot be negative. | Interval:[" + interval + "]");
            return new CalendarSchedule(calendar, interval);
        }

    }

}