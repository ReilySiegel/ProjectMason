package edu.wpi.teamo.utils.itemsifters;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamo.utils.itemsifters.displays.SelectionListDisplay;

public class TableSearcher<T, Cell> extends SearchSelect<T, Cell> {

    public interface HeaderCellCreator<T, Cell> {
        Cell makeCell();
    }
    HeaderCellCreator<T, Cell> headerCellCreator = null;

    public TableSearcher(JFXTextField searchBar,
                         JFXListView<Cell> searchResultsList,
                         Matcher<T> matcher,
                         SelectionListDisplay.SelectionCellCreator<T, Cell> cellCreator,
                         HeaderCellCreator<T, Cell> headerCellCreator) {

        super(searchBar, searchResultsList, matcher, cellCreator);
        this.headerCellCreator = headerCellCreator;

    }

    public void setHeaderCellCreator(HeaderCellCreator<T, Cell> headerCellCreator) {
        this.headerCellCreator = headerCellCreator;
    }
}
