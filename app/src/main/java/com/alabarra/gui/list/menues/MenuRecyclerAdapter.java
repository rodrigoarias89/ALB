package com.alabarra.gui.list.menues;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.alabarra.gui.listeners.OnMenuItemClickListener;
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

public class MenuRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int CATEGORY_ROW_TYPE = 1;
    private final static int MENU_ITEM_ROW_TYPE = 2;

    private Menu mMenu;
    private Map<Category, Boolean> mExpandedMap;
    private OnMenuItemClickListener mMenuItemListener;

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
                ((MenuItemViewHolder) holder).setData(mRowWrappers.get(position).wrapperMenuItem);
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

    class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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
            mCell.setData(item.getName(), item.getPrice().toString());
            mCell.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mMenuItemListener != null) {
                mMenuItemListener.onMenuItemClick(mMenuItem);
            }
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
            notifyItemRangeInserted(categoryPosition + 1, category.getItems().size());
        }
    }

    private void collapseCategory(Category category, int categoryPosition) {
        if (!category.getItems().isEmpty()) {
            mExpandedMap.put(category, false);

            for (int i = 0; i < category.getItems().size(); i++) {
                mRowWrappers.remove(categoryPosition + 1);
            }

            notifyItemRangeRemoved(categoryPosition + 1, category.getItems().size());
        }
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
        mMenuItemListener = listener;
    }

}
