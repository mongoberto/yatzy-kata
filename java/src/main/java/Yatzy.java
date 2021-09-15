import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Yatzy {

    public static int chance(int d1, int d2, int d3, int d4, int d5) {
        return d1 + d2 + d3 + d4 + d5;
    }

    public static int yatzy(int d1, int d2, int d3, int d4, int d5) {
        Map<Integer, Integer> occurrenceOfDiceMap = getOccurrenceOfDiceMap(d1, d2, d3, d4, d5);
        if (occurrenceOfDiceMap.size() != 1)
            return 0;
        return 50;
    }

    public static int ones(int d1, int d2, int d3, int d4, int d5) {
        return Arrays.stream(new int[]{d1, d2, d3, d4, d5}).filter(d -> d == 1).reduce(Integer::sum).orElse(0);
    }

    public static int twos(int d1, int d2, int d3, int d4, int d5) {
        return Arrays.stream(new int[]{d1, d2, d3, d4, d5}).filter(d -> d == 2).reduce(Integer::sum).orElse(0);
    }

    public static int threes(int d1, int d2, int d3, int d4, int d5) {
        return Arrays.stream(new int[]{d1, d2, d3, d4, d5}).filter(d -> d == 3).reduce(Integer::sum).orElse(0);
    }

    public static int fours(int d1, int d2, int d3, int d4, int d5) {
        return Arrays.stream(new int[]{d1, d2, d3, d4, d5}).filter(d -> d == 4).reduce(Integer::sum).orElse(0);
    }

    public static int fives(int d1, int d2, int d3, int d4, int d5) {
        return Arrays.stream(new int[]{d1, d2, d3, d4, d5}).filter(d -> d == 5).reduce(Integer::sum).orElse(0);
    }

    public static int sixes(int d1, int d2, int d3, int d4, int d5) {
        return Arrays.stream(new int[]{d1, d2, d3, d4, d5}).filter(d -> d == 6).reduce(Integer::sum).orElse(0);
    }

    public static int score_pair(int d1, int d2, int d3, int d4, int d5) {
        final List<Integer> multipleOccurrenceList = getDiceWithSpecificOccurrence(occurrenceOfDice -> occurrenceOfDice.getValue() >= 2,
            d1, d2, d3, d4, d5);
        return 2 * multipleOccurrenceList.stream()
            .max(Comparator.naturalOrder()).orElse(0);
    }

    public static int two_pair(int d1, int d2, int d3, int d4, int d5) {
        Map<Integer, Integer> occurrenceOfDiceMap = getOccurrenceOfDiceMap(d1, d2, d3, d4, d5);

        final Integer diceWith4OccurrenceAtLeast = occurrenceOfDiceMap.entrySet().stream()
            .filter(occurrenceOfDice -> occurrenceOfDice.getValue() >= 4)
            .map(Map.Entry::getKey)
            .findFirst().orElse(0);

        if (diceWith4OccurrenceAtLeast != 0)
            return 4 * diceWith4OccurrenceAtLeast;

        final List<Integer> diceWith2Or3OccurrenceList = occurrenceOfDiceMap.entrySet().stream()
            .filter(occurrenceOfDice -> occurrenceOfDice.getValue() >= 2)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());

        if (diceWith2Or3OccurrenceList.size() != 2)
            return 0;
        return 2 * diceWith2Or3OccurrenceList.stream().reduce(Integer::sum).get();
    }

    public static int three_of_a_kind(int d1, int d2, int d3, int d4, int d5) {
        final List<Integer> multipleOccurrenceList = getDiceWithSpecificOccurrence(occurrenceOfDice -> occurrenceOfDice.getValue() >= 3,
            d1, d2, d3, d4, d5);
        if (multipleOccurrenceList.size() != 1)
            return 0;
        return 3 * multipleOccurrenceList.get(0);
    }

    public static int four_of_a_kind(int d1, int d2, int d3, int d4, int d5) {
        final List<Integer> multipleOccurrenceList = getDiceWithSpecificOccurrence(occurrenceOfDice -> occurrenceOfDice.getValue() >= 4,
            d1, d2, d3, d4, d5);
        if (multipleOccurrenceList.size() != 1)
            return 0;
        return 4 * multipleOccurrenceList.get(0);
    }

    public static int smallStraight(int d1, int d2, int d3, int d4, int d5) {
        final List<Integer> diceWithOneOccurrenceList = getDiceWithSpecificOccurrence(occurrenceOfDice -> occurrenceOfDice.getValue() == 1,
            d1, d2, d3, d4, d5);

        if (diceWithOneOccurrenceList.size() != 5 || diceWithOneOccurrenceList.stream().reduce(Integer::sum).orElse(0) != 15)
            return 0;
        return 15;
    }

    public static int largeStraight(int d1, int d2, int d3, int d4, int d5) {
        final List<Integer> diceWithOneOccurrenceList = getDiceWithSpecificOccurrence(occurrenceOfDice -> occurrenceOfDice.getValue() == 1,
            d1, d2, d3, d4, d5);

        if (diceWithOneOccurrenceList.size() != 5 || diceWithOneOccurrenceList.stream().reduce(Integer::sum).orElse(0) != 20)
            return 0;
        return 20;
    }

    public static int fullHouse(int d1, int d2, int d3, int d4, int d5) {
        Map<Integer, Integer> occurrenceOfDiceMap = getOccurrenceOfDiceMap(d1, d2, d3, d4, d5);

        final Integer diceWith2Occurrence = occurrenceOfDiceMap.entrySet().stream()
            .filter(occurrenceOfDice -> occurrenceOfDice.getValue() == 2)
            .map(Map.Entry::getKey)
            .findFirst().orElse(0);

        final Integer diceWith3Occurrence = occurrenceOfDiceMap.entrySet().stream()
            .filter(occurrenceOfDice -> occurrenceOfDice.getValue() == 3)
            .map(Map.Entry::getKey)
            .findFirst().orElse(0);

        if (diceWith2Occurrence == 0 || diceWith3Occurrence == 0)
            return 0;

        return 2 * diceWith2Occurrence + 3 * diceWith3Occurrence;
    }

    private static List<Integer> getDiceWithSpecificOccurrence(Predicate<Map.Entry<Integer, Integer>> entryPredicate,
                                                               int d1, int d2, int d3, int d4, int d5) {
        Map<Integer, Integer> occurrenceOfDiceMap = getOccurrenceOfDiceMap(d1, d2, d3, d4, d5);
        return occurrenceOfDiceMap.entrySet().stream()
            .filter(entryPredicate)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }

    private static Map<Integer, Integer> getOccurrenceOfDiceMap(int d1, int d2, int d3, int d4, int d5) {
        final List<Integer> diceRolls = Arrays.asList(d1, d2, d3, d4, d5);
        Map<Integer, Integer> occurrenceOfDiceMap = new HashMap<>();
        diceRolls.stream().distinct().forEach(d -> occurrenceOfDiceMap.put(d, Collections.frequency(diceRolls, d)));
        return occurrenceOfDiceMap;
    }
}
