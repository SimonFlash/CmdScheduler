package com.mcsimonflash.sponge.cmdscheduler.schedule;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.EnumMap;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Modifiers {

    /* Schedule Modifiers (The prefix unit: used in examples for clarity, but are not a part of the schedule)
     *
     * * (wildcard): Accepts all allowed values, effectively ignoring this unit [Ex. d:* -> Any day]
     * N (number): Applies as a specifically defined Number. [Ex. s:7 -> When seconds matches 7]
     * /N (increment): Includes multiples of ## [Ex. m:/4 -> When minutes matches 0,4,8...52,56]
     * N-N (range): Includes all numbers within the given range [Ex. H:15-21 -> When hours matches 15,16,17...20,21]
     * M,M or M, M (separator): Defines two individual modifiers. This defaults to the union (inclusion) of both.
     * &M (union): Unions this modifier with the existing set [Ex. M:5,&/2 -> When months matches 2,4,5,6,8,10,12]
     * ^M (intersection): Intersects this modifier with the existing set [Ex. ms:/100,^/250 -> When days matches 0,500]
     * $M (complement): Removes this modifier from the existing set [Ex. d:/6,$12 -> When milliseconds matches 6,18,24,30]
     * !M (inverse): Includes the inverse of the modifier [Ex. s:!/4 -> When seconds matches 1,2,3,5...55,57,58,59]
     *
     */

    /* Modifier Pattern:
     * operator: ([&^$])? - Matches an optional "&", "^", or "$" character
     * inverse: ([!]?) - Matches an optional "!" character
     * lower: ([0-9]+) - Matches the lower bound of a range
     *  -: [-] - Matches the range "-" character
     * upper : ([0-9]+) - Matches the upper bound of a range
     *  -: [/] - Matches the increment "/" character
     * increment: ([0-9]+) - Matches the increment value
     * number: ([0-9]+) - Matches a numerical value
     * wildcard: ([*]) - Matches the wildcard "*" character
     *
     * Separator Pattern:
     *  -: , ? - Matches the modifier separator "," character and optional space
     *  -: ?= ?[&^$] - Lookahead for an optional space and the operator "&", "^" or "$" characters
     *
     * Schedule Pattern:
     *  -: "(?<!,)" - Discards the modifier separator
     *  -: " _" - Matches the modifier separator " " or "_" characters
     *  -: "(?![&^$]) - Discards the modifier operators
     */

    private static final Pattern MODIFIER = Pattern.compile("(?<operator>[&^$])?(?<inverse>[!])?(?:(?:(?<lower>[0-9]+)[-](?<upper>[0-9]+))|[/](?<increment>(?:[1-9]|0(?=[0]*[1-9]))[0-9]*)|(?<number>[0-9]+)|(?<wildcard>[*]))");
    private static final Pattern SEPARATOR =  Pattern.compile("[,]|(?<![,][ _]?)(?=[&^$])"); //"[,][ _]?|(?<![,][ _]?|[ _])[ _]?(?=[&^$])"
    private static final Pattern SCHEDULE = Pattern.compile("(?<!,)[ _](?![&^$])");

    /**
     * Builds and returns a set of times representing the schedule encoded by
     * the given modifier for the given {@link Units} unit.
     *
     * The set returned must be a valid set for the given unit, defined by
     * {@code !set.isEmpty() && unit.containsAll(set)}.
     *
     * @param unit the time unit the modifier represents
     * @param modifier the string modifier to encode the set
     * @return the set of times from the modifier for this unit
     * @throws IllegalArgumentException if the modifier is invalid
     */
    public static Set<Integer> buildSet(Units unit, String modifier) throws IllegalArgumentException {
        Preconditions.checkArgument(!modifier.isEmpty(), "Modifier must be defined.");
        Set<Integer> unitSet = Sets.newHashSet();
        for (String mod : SEPARATOR.split(modifier)) {
            Matcher matcher = MODIFIER.matcher(mod.trim());
            Preconditions.checkArgument(matcher.matches(), "Modifier did not match expected format. Modifier:[" + mod + "]");
            try {
                Set<Integer> modifierSet = Sets.newHashSet();
                if (matcher.group("lower") != null) {
                    int lower = unit.checkBounds(Integer.parseInt(matcher.group("lower")));
                    int upper = unit.checkBounds(Integer.parseInt(matcher.group("upper")));
                    Preconditions.checkArgument(lower <= upper, "Lower bound cannot be greater than the upper bound. | Modifier:[" + mod + "]");
                    modifierSet.addAll(IntStream.rangeClosed(lower, upper).boxed().collect(Collectors.toSet()));
                } else if (matcher.group("increment") != null) {
                    int increment = unit.checkBounds(Integer.parseInt(matcher.group("increment")));
                    modifierSet.addAll(unit.values.stream().filter(i -> i % increment == 0).collect(Collectors.toSet()));
                } else if (matcher.group("number") != null) {
                    modifierSet.add(unit.checkBounds(Integer.parseInt(matcher.group("number"))));
                } else if (matcher.group("wildcard") != null) {
                    modifierSet.addAll(unit.values);
                }
                if (matcher.group("inverse") != null) {
                    modifierSet = Sets.difference(unit.values, modifierSet);
                }
                switch (Optional.ofNullable(matcher.group("operator")).orElse("&")) {
                    case "&":
                        unitSet = Sets.union(unitSet, modifierSet);
                        break;
                    case "^":
                        unitSet = Sets.intersection(unitSet, modifierSet);
                        break;
                    case "$":
                        unitSet = Sets.difference(unitSet, modifierSet);
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Expected an integer parsing modifier. | Modifier:[" + mod + "]", e);
            }
        }
        return Sets.newHashSet(unitSet);
    }

    public static String buildModifier(Units unit, Set<Integer> set) {
        Preconditions.checkArgument(!set.isEmpty(), "Set must be defined.");
        Preconditions.checkArgument(unit.values.containsAll(set), "Set contains illegal values. | Values:[" + Sets.difference(unit.values, set) + "]");
        return String.join(", ", set.stream().map(Object::toString).collect(Collectors.toList()));
    }

    /**
     * Builds and returns a map of sets that defines a schedule.
     *
     * A valid schedule
     *
     * @param schedule the string schedule to encode the map
     * @return the map of sets for this schedule
     * @throws IllegalArgumentException if the schedule is invalid
     */
    public static EnumMap<Units, Set<Integer>> buildSchedule(String schedule) throws IllegalArgumentException {
        Preconditions.checkArgument(!schedule.isEmpty(), "Schedule must be defined");
        EnumMap<Units, Set<Integer>> sets = Maps.newEnumMap(Units.class);
        String[] modifiers = SCHEDULE.split(schedule);
        Preconditions.checkArgument(modifiers.length == Units.values().length, "Schedule did not define the expected number of modifiers. | Expected:[" + Units.values().length + "]");
        for (Units unit : Units.values()) {
            sets.put(unit, buildSet(unit, modifiers[5 - unit.ordinal()]));
        }
        return sets;
    }

}