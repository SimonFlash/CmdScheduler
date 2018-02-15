package com.mcsimonflash.sponge.cmdscheduler.serializer;

import com.google.common.reflect.TypeToken;
import com.mcsimonflash.sponge.cmdscheduler.schedule.Schedule;
import com.mcsimonflash.sponge.cmdscheduler.task.AdvancedTask;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.util.Tristate;

public class AdvancedTaskSerializer implements TypeSerializer<AdvancedTask> {

    /**
     * Deserializes a {@link AdvancedTask} from a configuration node. An
     * example node for this might look like:
     *
     * task {
     *     name="Example Task"
     *     command="/broadcast Magnetar is awesome!"
     *     schedule {
     *         //Schedule
     *     }
     *     async=false
     * }
     *
     * @param type the TypeToken of the AdvancedTask class
     * @param value the node to retrieve the task from
     * @return the AdvancedTask described by this node
     * @throws ObjectMappingException if the task's schedule cannot be retrieved
     */
    @Override
    public AdvancedTask deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        return AdvancedTask.builder()
                .name(value.getNode("name").getString((String) value.getKey()))
                .schedule(value.getNode("schedule").getValue(TypeToken.of(Schedule.class)))
                .async(value.getNode("async").isVirtual() ? Tristate.UNDEFINED : Tristate.fromBoolean(value.getNode("async").getBoolean(false)))
                .command(value.getNode("command").getString(""))
                .build();
    }

    /**
     * Serializes a {@link AdvancedTask} object.
     *
     * @param type the TypeToken of the AdvancedTask class
     * @param obj the AdvancedTask to encode
     * @param value the node to encode the schedule to
     * @throws ObjectMappingException lies.
     */
    @Override
    public void serialize(TypeToken<?> type, AdvancedTask obj, ConfigurationNode value) throws ObjectMappingException {
        value.getNode("name").setValue(obj.getName());
        value.getNode("schedule").setValue(obj.getSchedule());
        value.getNode("async").setValue(obj.getAsync());
        //TODO: Handle serialization of executor (command)
    }

}