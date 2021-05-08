package edu.wpi.teamo.utils.itemsifters.displays;

import com.jfoenix.controls.JFXListView;
import edu.wpi.teamo.utils.itemsifters.displays.ListViewDisplay;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class SelectionListDisplay<Item, Cell> extends ListViewDisplay<Item, Cell> {
    SelectionCellCreator<Item, Cell> selectionCellCreator;
    List<Item> selectedItems;

    public interface SelectionCellCreator<Item, Cell> {
        Cell makeCell(Item item, boolean selected);
    }

    public SelectionListDisplay(JFXListView<Cell> listView, SelectionCellCreator<Item, Cell> selectionCellCreator) {
        this.selectedItems = new LinkedList<>();
        this.selectionCellCreator = selectionCellCreator;
        this.listView = listView;
    }

    @Override
    protected void populateListView(Stream<Item> items) {
        selectedItems.forEach(selectedItem -> {
            Cell cell = selectionCellCreator.makeCell(selectedItem, true);
            listView.getItems().add(cell);
        });
        items.filter(item -> !selectedItems.contains(item)).forEach(item -> {
            Cell cell = selectionCellCreator.makeCell(item, false);
            listView.getItems().add(cell);
        });
    }

    public void selectItem(Item item) {
        selectedItems.add(item);
    }

    public void deSelectItem(Item item) {
        selectedItems.remove(item);
    }

    public void clearSelectedItems() {
        selectedItems.clear();
    }

    public void setSelectionCellCreator(SelectionCellCreator<Item, Cell> selectionCellCreator) {
        this.selectionCellCreator = selectionCellCreator;
    }

    public List<Item> getSelectedItems() {
        return selectedItems;
    }
}
