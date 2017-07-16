package com.alabarra.gui.list.menues;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.alabarra.model.Category;
import com.alabarra.model.Menu;

/**
 * Created by rodrigoarias on 7/15/17.
 */

public class MenuRecyclerAdapter extends RecyclerView.Adapter<MenuRecyclerAdapter.CategoryViewHolder> {

    private Menu mMenu;

    public void setMenu(Menu menu) {
        mMenu = menu;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CategoryViewHolder(new CategoryRecyclerCell(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        holder.setData(mMenu.getCategories().get(position));
    }

    @Override
    public int getItemCount() {
        if (mMenu != null) {
            return mMenu.getCategories().size();
        } else {
            return 0;
        }
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CategoryRecyclerCell mCell;

        public CategoryViewHolder(CategoryRecyclerCell itemView) {
            super(itemView);
            mCell = itemView;
        }

        public void setData(final Category category) {
            mCell.setData(category.getName());
        }

        @Override
        public void onClick(View view) {
        }
    }

}
