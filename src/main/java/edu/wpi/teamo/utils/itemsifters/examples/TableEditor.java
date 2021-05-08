package edu.wpi.teamo.utils.itemsifters.examples;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamo.utils.itemsifters.ItemSearch;
import edu.wpi.teamo.utils.itemsifters.displays.ListViewDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

public class TableEditor {
    private final ItemSearch<Thing, HBox> itemSearch;
    private final double columnWidth = 150;
    private final double columnSpacing = 10;

    public TableEditor(JFXTextField searchBar, JFXListView<HBox> listView) {

        ItemSearch.TextMatcher<Thing> matcher = (thing, text) -> {
            text = text.toLowerCase(Locale.ROOT);

            boolean matching = thing.getName().toLowerCase(Locale.ROOT).contains(text);
            matching |= String.valueOf(thing.getAge()).toLowerCase(Locale.ROOT).contains(text);
            matching |= String.valueOf(thing.getSize()).toLowerCase(Locale.ROOT).contains(text);

            return matching;
        };

        /*
            for each attribute of Thing, a textField is created, displaying that attribute while allowing changes.
            To each, we assign an action event handler.
            The handler passes a reference to the changed field and the Thing in question.
         */
        ItemSearch.CellCreator<Thing, HBox> rowCreator = (thing) -> {
            JFXTextField nameField = new JFXTextField();
            nameField.setText(thing.getName());
            nameField.setOnAction(event -> handleNameChange(nameField, thing));
            nameField.setMinWidth(columnWidth);
            nameField.setMaxWidth(columnWidth);

            JFXTextField sizeField = new JFXTextField();
            sizeField.setText(String.valueOf(thing.getSize()));
            sizeField.setOnAction(event -> handleSizeChange(sizeField, thing));
            sizeField.setMinWidth(columnWidth);
            sizeField.setMaxWidth(columnWidth);

            JFXTextField ageField = new JFXTextField();
            ageField.setText(String.valueOf(thing.getAge()));
            ageField.setOnAction(event -> handleAgeChange(ageField, thing));
            ageField.setMinWidth(columnWidth);
            ageField.setMaxWidth(columnWidth);

            HBox row = new HBox(nameField, sizeField, ageField);
            row.setSpacing(columnSpacing);
            return row;
        };

        ListViewDisplay.HeaderCellCreator<HBox> headerCreator = () -> {
            List<Label> columns = new LinkedList<>();
            columns.add(new Label("ID"));
            columns.add(new Label("Start Node ID"));
            columns.add(new Label("End Node ID"));
            columns.forEach(column -> column.setMinWidth(columnWidth));
            columns.forEach(column -> column.setMaxWidth(columnWidth));

            HBox row = new HBox();
            row.getChildren().addAll(columns);
            row.setSpacing(columnSpacing);
            return row;
        };

        itemSearch = new ItemSearch<>(searchBar, listView, matcher, rowCreator, headerCreator);
    }

    private void handleAgeChange(JFXTextField ageField, Thing thing) {
        try {
            Integer parsedAge = Integer.valueOf(ageField.getText());
            thing.setAge(parsedAge);
        }
        catch (NumberFormatException e) {
            System.out.println("Not an integer.");
        }
        finally {
            update();
        }
    }

    private void handleSizeChange(JFXTextField sizeField, Thing thing) {
        try {
            Integer parsedSize = Integer.valueOf(sizeField.getText());
            thing.setSize(parsedSize);
        }
        catch (NumberFormatException e) {
            System.out.println("Not an integer.");
        }
        finally {
            update();
        }
    }

    private void handleNameChange(JFXTextField nameField, Thing thing) {
        thing.setName(nameField.getText());
        update();
    }

    public void addThings(Stream<Thing> things) {
        things.forEach(this::addThing);
    }

    public void addThing(Integer size, Integer age, String name) {
        addThing(new Thing(size, age, name));
    }

    public void addThing(Thing thing) {
        itemSearch.addItem(thing);
    }

    public void update() {
        itemSearch.update();
    }
}

/**
 * a thing that you want to display and edit in the table
 */
class Thing {
    Integer size;
    Integer age;
    String name;
    public Thing(Integer size, Integer age, String name) {
        this.size = size;
        this.age = age;
        this.name = name;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
