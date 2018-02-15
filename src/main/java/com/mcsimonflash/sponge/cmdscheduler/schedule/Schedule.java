package com.mcsimonflash.sponge.cmdscheduler.schedule;

import java.util.Calendar;

public interface Schedule {

    //TODO: Consider providing commonly used schedules (see ClassicSchedule)
    //TODO: Add method to return a calendar of the next match
    //TODO: Limit the delay to be within a range of times

    /**
     * Returns the delay, in milliseconds, until this schedule is next matched
     * starting from the time of the given {@link Calendar}.
     *
     * If the schedule implementation is a CalendarSchedule and the time has
     * already passed, the difference in milliseconds between then and now
     * (negative) is returned.
     *
     * If the schedule implementation is a ClassicSchedule, the delay is
     * returned independent of the calendar time.
     *
     * If the schedule implementation is a CronSchedule that will not run until
     * the next year, -1 is returned.
     *
     * @param calendar the time to check from
     * @return the delay, in milliseconds
     */
    long getDelay(Calendar calendar);

    /**
     * Calls #getDelay with a {@link Calendar} of the current instance.
     *
     * @return the delay, in milliseconds
     */
    default long getDelay() {
        return getDelay(Calendar.getInstance());
    }

    /**
     * Returns the interval, in milliseconds, of the given schedule.
     *
     * If this schedule implementation is a CronSchedule, -1 is returned.
     *
     * @return the interval, in milliseconds
     */
    long getInterval();

}