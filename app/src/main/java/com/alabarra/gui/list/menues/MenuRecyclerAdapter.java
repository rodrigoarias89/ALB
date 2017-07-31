package com.alabarra.gui.list.menues;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.alabarra.R;
import com.alabarra.gui.listeners.OnMenuItemClickListener;
import com.alabarra.gui.utils.MoneyUtils;
import com.alabarra.model.Category;
import com.alabarra.model.Menu;
import com.alabarra.model.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rodrigoarias on 7/15/17.
 */

class MenuRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int CATEGORY_ROW_TYPE = 1;
    private final static int MENU_ITEM_ROW_TYPE = 2;

    private Menu mMenu;
    private Map<Category, Boolean> mExpandedMap;
    private OnMenuItemClickListener mMenuItemListener;

    private MenuRowFormatter mRowFormatter;

    private List<RowWrapper> mRowWrappers;

    MenuRecyclerAdapter() {
        init();
    }

    void setMenu(Menu menu) {
        init();
        mMenu = menu;

        for (Category category : mMenu.getCategories()) {
            mExpandedMap.put(category, false);
            mRowWrappers.add(new RowWrapper(category));
        }

    }

    private void init() {
        mRowWrappers = new ArrayList<>();
        mExpandedMap = new HashMap<>();
        mRowFormatter = new MenuRowFormatter();
    }

    @Override
    public int getItemViewType(int position) {
        return mRowWrappers.get(position).type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case CATEGORY_ROW_TYPE:
                return new CategoryViewHolder(new CategoryRecyclerCell(parent.getContext()));
            case MENU_ITEM_ROW_TYPE:
                return new MenuItemViewHolder(new MenuItemRecyclerCell(parent.getContext()));
        }
        return new CategoryViewHolder(new CategoryRecyclerCell(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case CATEGORY_ROW_TYPE:
                ((CategoryViewHolder) holder).setData(mRowWrappers.get(position).wrapperCategory);
                break;
            case MENU_ITEM_ROW_TYPE:
                MenuItemViewHolder itemHolder = ((MenuItemViewHolder) holder);
                itemHolder.setData(mRowWrappers.get(position).wrapperMenuItem);
                if (mRowFormatter.isEvenRow(position)) {
                    itemHolder.setBackground(R.color.colorPrimaryLight);
                } else {
                    itemHolder.setBackground(R.color.colorPrimary);
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mRowWrappers.size();
    }

    private class RowWrapper {

        int type;

        Category wrapperCategory;
        MenuItem wrapperMenuItem;

        RowWrapper(Category category) {
            wrapperCategory = category;
            type = CATEGORY_ROW_TYPE;
        }

        RowWrapper(MenuItem item) {
            wrapperMenuItem = item;
            type = MENU_ITEM_ROW_TYPE;
        }
    }

    private class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Category mCategory;
        private CategoryRecyclerCell mCell;

        CategoryViewHolder(CategoryRecyclerCell itemView) {
            super(itemView);
            mCell = itemView;
        }

        public void setData(final Category category) {
            mCategory = category;
            mCell.setData(mCategory.getName());
            mCell.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mExpandedMap.get(mCategory)) {
                collapseCategory(mCategory, getAdapterPosition());
            } else {
                expandCategory(mCategory, getAdapterPosition());
            }
        }
    }

    private class MenuItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private MenuItem mMenuItem;
        private MenuItemRecyclerCell mCell;

        MenuItemViewHolder(MenuItemRecyclerCell itemView) {
            super(itemView);
            mCell = itemView;
        }

        public void setData(final MenuItem item) {
            mMenuItem = item;
            mCell.setData(item.getName(), MoneyUtils.getAmountWithCurrencySymbol(item.getPrice()));
            mCell.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mMenuItemListener != null) {
                mMenuItemListener.onMenuItemClick(mMenuItem);
            }
        }

        void setBackground(int background) {
            mCell.setBackgroundResource(background);
        }
    }

    private void expandCategory(Category category, int categoryPosition) {
        if (!category.getItems().isEmpty()) {
            mExpandedMap.put(category, true);
            int itemPosition = 1;
            for (MenuItem item : category.getItems()) {
                mRowWrappers.add(categoryPosition + itemPosition, new RowWrapper(item));
                itemPosition++;
            }

            mRowFormatter.clear();
            for (RowWrapper row : mRowWrappers) {
                if (row.type == CATEGORY_ROW_TYPE) {
                    mRowFormatter.addCategoryIndexRow(mRowWrappers.indexOf(row));
                }
            }

            notifyItemRangeInserted(categoryPosition + 1, category.getItems().size());
        }
    }

    private void collapseCategory(Category category, int categoryPosition) {
        if (!category.getItems().isEmpty()) {
            mExpandedMap.put(category, false);

            for (int i = 0; i < category.getItems().size(); i++) {
                mRowWrappers.remove(categoryPosition + 1);
            }

            mRowFormatter.clear();
            for (RowWrapper row : mRowWrappers) {
                if (row.type == CATEGORY_ROW_TYPE) {
                    mRowFormatter.addCategoryIndexRow(mRowWrappers.indexOf(row));
                }
            }
            notifyItemRangeRemoved(categoryPosition + 1, category.getItems().size());
        }
    }

    void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
        mMenuItemListener = listener;
    }

}
