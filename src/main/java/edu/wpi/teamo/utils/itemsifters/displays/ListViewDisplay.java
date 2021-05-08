package edu.wpi.teamo.utils.itemsifters.displays;

import com.jfoenix.controls.JFXListView;
import edu.wpi.teamo.utils.itemsifters.displays.IDisplay;

import java.util.stream.Stream;

/**
 * help me improve this class to make it more useful: nllopez@wpi.edu
 * needs stuff that interacts with list view's actual cell factory
 */
public class ListViewDisplay<T, Cell> implements IDisplay<T> {
    HeaderCellCreator<Cell> headerCellCreator = null;
    CellCreator<T, Cell> cellCreator;
    JFXListView<Cell> listView;

    public interface CellCreator<T, Cell> {
        Cell makeCell(T item);
    }

    public interface HeaderCellCreator<Cell> {
        Cell makeCell();
    }

    public ListViewDisplay() {
        this.cellCreator = null;
        this.listView = null;
    }

    public ListViewDisplay(JFXListView<Cell> listView, CellCreator<T, Cell> cellCreator) {
        this.cellCreator = cellCreator;
        this.listView = listView;
    }

    @Override
    public void update(Stream<T> items) {
        if (listView == null) return;

        clearItems();
        addHeader();
        populateListView(items);
    }

    protected void populateListView(Stream<T> items) {
        if (cellCreator == null) return;
        Stream<Cell> cellStream = items.map(item -> cellCreator.makeCell(item));
        cellStream.forEach(cell -> listView.getItems().add(cell));
    }

    public void clearItems() {
        listView.getItems().clear();
    }

    public void addHeader() {
        if (headerCellCreator != null) {
            Cell header = headerCellCreator.makeCell();
            listView.getItems().add(header);
        }
    }

    /**
     * Set a cell creator that will be ran once, at the top of the list, to create a "header" cell.
     * This is useful if you want to make a table. See TableEditor example.
     */
    public void setHeaderCellCreator(HeaderCellCreator<Cell> headerCellCreator) {
        this.headerCellCreator = headerCellCreator;
    }
}
