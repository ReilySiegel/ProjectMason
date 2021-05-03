package edu.wpi.teamo.views;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;

import java.util.List;

public class TableSearcher<T, Cell> extends SearchSelect<T, Cell> {

    public interface HeaderCellCreator<T, Cell> {
        Cell makeCell();
    }
    HeaderCellCreator<T, Cell> headerCellCreator = null;

    public TableSearcher(JFXTextField searchBar,
                         JFXListView<Cell> searchResultsList,
                         Matcher<T> matcher,
                         CellCreator<T, Cell> cellCreator,
                         HeaderCellCreator<T, Cell> headerCellCreator) {

        super(searchBar, searchResultsList, matcher, cellCreator);
        this.headerCellCreator = headerCellCreator;

    }

    protected void displayItems(List<T> matchingItems) {
        displayHeader();
        displaySelectedItems();
        displayMatchingItems(matchingItems);
    }

    private void displayHeader() {
        if (headerCellCreator != null) {
            Cell cell = headerCellCreator.makeCell();
            resultsList.getItems().add(cell);
        }
    }

    public void setHeaderCellCreator(HeaderCellCreator<T, Cell> headerCellCreator) {
        this.headerCellCreator = headerCellCreator;
    }
}
