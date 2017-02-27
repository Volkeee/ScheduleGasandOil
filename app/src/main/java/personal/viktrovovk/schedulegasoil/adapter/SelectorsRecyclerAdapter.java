package personal.viktrovovk.schedulegasoil.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import personal.viktrovovk.schedulegasoil.R;
import personal.viktrovovk.schedulegasoil.model.SelectorItem;
import personal.viktrovovk.schedulegasoil.tools.RecycleTouchListener;

/**
 * Created by Viktor on 25/02/2017.
 */

public class SelectorsRecyclerAdapter extends RecyclerView.Adapter<SelectorsRecyclerAdapter.ViewHolder> {
    public ArrayList<SelectorItem> mItems;

    public SelectorsRecyclerAdapter(ArrayList<SelectorItem> selectorItems) {
        mItems = new ArrayList<>(selectorItems);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_selector, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SelectorItem selectorItem = mItems.get(position);

        holder.selectorItemTextView.setText(selectorItem.getInstitution());
    }

    public void swap(ArrayList<SelectorItem> list) {
        if (mItems != null) {
            ArrayList<SelectorItem> arrayList = new ArrayList<>(list);
            mItems.clear();
            mItems.addAll(arrayList);
        } else {
            mItems = list;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public SelectorItem getItem(int position) {
        return mItems.get(position);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        public Context mContext;
        TextView selectorItemTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            selectorItemTextView = (TextView) itemView.findViewById(R.id.selector_item_textview);
            mContext = itemView.getContext();
        }
    }
}
