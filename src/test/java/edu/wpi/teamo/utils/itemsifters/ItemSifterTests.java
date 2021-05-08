package edu.wpi.teamo.utils.itemsifters;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.teamo.utils.itemsifters.displays.IDisplay;
import org.junit.jupiter.api.Test;
import java.util.stream.Stream;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.List;

public class ItemSifterTests {

    @Test
    public void addItems() {
        List<Integer> ints = new LinkedList<>(Arrays.asList(1, 2, 3, 4, 5));

        ItemSifter<Integer> ds = new ItemSifter<>();
        ds.addItems(ints.stream());

        assertEquals(5, ds.getItems().count());
        ds.getItems().forEach(item -> assertTrue(ints.contains(item)));
    }

    @Test
    public void noFilters() {
        List<Integer> ints = new LinkedList<>(Arrays.asList(1, 2, 3, 4, 5));

        ItemSifter<Integer> ds = new ItemSifter<>();
        ds.addItems(ints.stream());

        assertEquals(5, ds.processItems().count());
        ds.processItems().forEach(item -> assertTrue(ints.contains(item)));
    }

    @Test
    public void hardFilters() {
        List<Integer> ints = new LinkedList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> expectedResult = new LinkedList<>(Arrays.asList(3, 5));

        ItemSifter<Integer> ds = new ItemSifter<>();
        ds.addItems(ints.stream());

        ds.addHardFilter((integer) -> integer >= 3);
        ds.addHardFilter((integer) -> integer % 2 != 0);

        assertEquals(2, ds.processItems().count());
        ds.processItems().forEach(item -> assertTrue(expectedResult.contains(item)));
    }

    @Test
    public void softFilters() {
        List<Integer> ints = new LinkedList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> expectedResult = new LinkedList<>(Arrays.asList(1, 3, 4, 5));

        ItemSifter<Integer> ds = new ItemSifter<>();
        ds.addItems(ints.stream());

        ds.addSoftFilter((integer) -> integer >= 3);
        ds.addSoftFilter((integer) -> integer % 2 != 0);

        assertEquals(4, ds.processItems().count());
        ds.processItems().forEach(item -> assertTrue(expectedResult.contains(item)));
    }

    @Test
    public void hardAndSoftFilters() {
        List<Integer> ints = new LinkedList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> expectedResult = new LinkedList<>(Arrays.asList(3, 5));

        ItemSifter<Integer> ds = new ItemSifter<>();
        ds.addItems(ints.stream());

        ds.addHardFilter((integer) -> integer >= 3);
        ds.addSoftFilter((integer) -> integer == 1);
        ds.addSoftFilter((integer) -> integer == 3);
        ds.addSoftFilter((integer) -> integer == 5);

        assertEquals(2, ds.processItems().count());
        ds.processItems().forEach(item -> assertTrue(expectedResult.contains(item)));
    }

    @Test
    public void displays() {
        class TestDisplay implements IDisplay<Integer> {
            public boolean wasCalled = false;
            public void update(Stream<Integer> intStream) {
                wasCalled = true;
            }
        }

        List<Integer> ints = new LinkedList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<TestDisplay> testDisplays = new LinkedList<>();
        testDisplays.add(new TestDisplay());
        testDisplays.add(new TestDisplay());
        testDisplays.add(new TestDisplay());

        ItemSifter<Integer> ds = new ItemSifter<>();
        ds.addItems(ints.stream());

        testDisplays.forEach(display -> ds.addDisplay(display::update));

        ds.update();

        testDisplays.forEach(display -> assertTrue(display.wasCalled));
    }
}
