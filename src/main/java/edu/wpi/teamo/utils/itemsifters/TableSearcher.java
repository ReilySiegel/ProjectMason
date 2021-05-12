package edu.wpi.teamo.utils.itemsifters;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamo.utils.itemsifters.displays.ListViewDisplay;
import edu.wpi.teamo.utils.itemsifters.displays.SelectionListDisplay;

public class TableSearcher<T, Cell> extends SearchSelect<T, Cell> {

    public TableSearcher(JFXTextField searchBar,
                         JFXListView<Cell> searchResultsList,
                         Matcher<T> matcher,
                         SelectionListDisplay.SelectionCellCreator<T, Cell> cellCreator,
                         ListViewDisplay.HeaderCellCreator<Cell> headerCellCreator) {

        super(searchBar, searchResultsList, matcher, cellCreator);
        setHeaderCellCreator(headerCellCreator);
    }

    public void setHeaderCellCreator(ListViewDisplay.HeaderCellCreator<Cell> headerCellCreator) {
        display.setHeaderCellCreator(headerCellCreator);
    }
}
