package com.alabarra.gui.list.menues;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rodrigoarias on 7/30/17.
 */

public class MenuRowFormatter {

    private List<Integer> mCategoryRowsIndexes;

    MenuRowFormatter() {
        mCategoryRowsIndexes = new ArrayList<>();
    }

    void clear() {
        mCategoryRowsIndexes.clear();
    }

    void addCategoryIndexRow(int index) {
        mCategoryRowsIndexes.add(index);
    }

    boolean isEvenRow(int indexRow) {
        for (int i = 0; i < mCategoryRowsIndexes.size(); i++) {

            if (mCategoryRowsIndexes.size() == i + 1) {
                //Last category row
                return (indexRow - mCategoryRowsIndexes.get(i)) % 2 == 0;
            } else {
                if (indexRow > mCategoryRowsIndexes.get(i) && indexRow < mCategoryRowsIndexes.get(i + 1)) {
                    return (indexRow - mCategoryRowsIndexes.get(i)) % 2 == 0;
                }
            }
        }
        return false;
    }
}
