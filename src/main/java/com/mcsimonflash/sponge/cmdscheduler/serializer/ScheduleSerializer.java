package com.mcsimonflash.sponge.cmdscheduler.serializer;

import com.google.common.reflect.TypeToken;
import com.mcsimonflash.sponge.cmdscheduler.internal.Utils;
import com.mcsimonflash.sponge.cmdscheduler.schedule.*;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

public class ScheduleSerializer implements TypeSerializer<Schedule> {

    /**
     * Deserializes a {@link Schedule} from a configuration node.
     *
     * @param type the TypeToken of the Schedule class
     * @param value the node to retrieve the schedule from
     * @return the Schedule described by this node
     * @throws ObjectMappingException if the type is unknown
     */
    @Override
    public Schedule deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        String clazz = value.getNode("type").getString("");
        try {
            switch (clazz.toLowerCase()) {
                case "calendar":
                    return CalendarSchedule.builder()
                            .date(value.getNode("date").getString(""))
                            .interval(Utils.parseTime(value.getNode("interval").getString("")))
                            .build();
                case "classic":
                    ClassicSchedule.Builder classic = ClassicSchedule.builder();
                    if (!value.getNode("delay").isVirtual()) {
                        classic.delay(Utils.parseTime(value.getNode("delay").getString("0")));
                    } else if (!value.getNode("delay-ticks").isVirtual()) {
                        classic.delayTicks(value.getNode("delay-ticks").getLong(0));
                    }
                    if (!value.getNode("interval").isVirtual()) {
                        classic.interval(Utils.parseTime(value.getNode("interval").getString("")));
                    } else if (!value.getNode("interval-ticks").isVirtual()) {
                        classic.intervalTicks(value.getNode("interval-ticks").getLong(0));
                    }
                    return classic.build();
                case "cron":
                    CronSchedule.Builder cron = CronSchedule.builder();
                    if (!value.getNode("schedule").isVirtual()) {
                        cron.schedule(value.getNode("schedule").getString(""));
                    } else {
                        for (Units unit : Units.values()) {
                            if (!value.getNode(unit.name().toLowerCase()).isVirtual()) {
                                cron.setUnit(unit, value.getNode(unit.name().toLowerCase()).getString(""));
                            }
                        }
                    }
                    return cron.build();
                default:
                    throw new ObjectMappingException("No known implementation for type " + clazz);
            }
        } catch (IllegalArgumentException e) {
            throw new ObjectMappingException(e);
        }
    }

    //TODO: Serialize intervals/delays as a Duration
    //Also, learn how to spell 'serialize' without IntelliJ spellcheck.

    /**
     * Serializes a {@link Schedule} object.
     *
     * @param type the TypeToken of the Schedule class
     * @param obj the Schedule schedule to encode
     * @param value the node to encode the schedule to
     * @throws ObjectMappingException if the obj class is unknown
     */
    @Override
    public void serialize(TypeToken<?> type, Schedule obj, ConfigurationNode value) throws ObjectMappingException {
        value.getNode("type").setValue(obj.getClass().getSimpleName());
        if (obj instanceof CalendarSchedule) {
            value.getNode("date").setValue(CalendarSchedule.FORMAT.format(((CalendarSchedule) obj).getCalendar().getTime()));
            value.getNode("interval").setValue(Utils.printTime(obj.getInterval()));
        } else if (obj instanceof ClassicSchedule) {
            value.getNode(((ClassicSchedule) obj).isDelayTicks() ? "delay-ticks" : "delay").setValue(Utils.printTime(obj.getDelay()));
            value.getNode(((ClassicSchedule) obj).isIntervalTicks() ? "interval-ticks" : "interval").setValue(Utils.printTime(obj.getInterval()));
        } else if (obj instanceof CronSchedule) {
            for (Units unit : Units.values()) {
                if (((CronSchedule) obj).hasUnit(unit)) {
                    value.getNode(unit.name().toLowerCase()).setValue(Modifiers.buildModifier(unit, ((CronSchedule) obj).getUnit(unit)));
                }
            }
        } else {
            throw new ObjectMappingException("No known implementation for class " + obj.getClass().getSimpleName());
        }
    }

}
