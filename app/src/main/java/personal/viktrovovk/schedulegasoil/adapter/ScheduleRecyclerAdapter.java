package personal.viktrovovk.schedulegasoil.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

import personal.viktrovovk.schedulegasoil.R;
import personal.viktrovovk.schedulegasoil.model.ScheduleItem;

/**
 * Created by volkeee on 28.02.17.
 */

public class ScheduleRecyclerAdapter extends RecyclerView.Adapter<ScheduleRecyclerAdapter.ScheduleViewHolder> {
    public ArrayList<ScheduleItem> mItems;

    public ScheduleRecyclerAdapter(ArrayList<ScheduleItem> mItems) {
        this.mItems = mItems;
    }

    @Override
    public ScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ScheduleRecyclerAdapter.ScheduleViewHolder viewHolder = new ScheduleRecyclerAdapter.ScheduleViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.schedule_item, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ScheduleViewHolder holder, int position) {
        ScheduleItem scheduleItem = mItems.get(position);

        holder.txtView_auditory.setText(String
                .format(holder
                                .mContext
                                .getString(R.string.schedule_item_auditory),
                        scheduleItem.getAuditory()));
        holder.txtView_number.setText(String
                .format(holder
                        .mContext
                        .getString(R.string.schedule_item_number), scheduleItem.getLessonOrder()));
        holder.txtView_discipline.setText(String
                .format(holder
                        .mContext
                        .getString(R.string.schedule_item_discipline), scheduleItem.getDiscipline()));
        if (scheduleItem.getSubgroup().equals("1-ша")) {
            holder.txtView_subgroup.setText("1st");
        } else if (scheduleItem.getSubgroup().equals("2-га")) {
            holder.txtView_subgroup.setText("2nd");
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void swap(ArrayList<ScheduleItem> list) {
        if (mItems != null) {
            ArrayList<ScheduleItem> arrayList = new ArrayList<>(list);
            mItems.clear();
            mItems.addAll(arrayList);
        } else {
            mItems = list;
        }
        notifyDataSetChanged();
    }

    public void applyFilter(ArrayList<ScheduleItem> receivedList, Integer fragmentsDay) {
        ArrayList<ScheduleItem> filteredList = new ArrayList<>(receivedList);
        ArrayList<ScheduleItem> itemsToRemove = new ArrayList<>();

        for (ScheduleItem item : filteredList) {
            if (!(Objects.equals(item.getDayIndex(), fragmentsDay))) {
                itemsToRemove.add(item);
            }
        }
        filteredList.removeAll(itemsToRemove);
        swap(filteredList);
    }

    public void applyFilter(ArrayList<ScheduleItem> receivedList, Integer fragmentsDay, Integer desiredWeek) {
        ArrayList<ScheduleItem> filteredList = new ArrayList<>(receivedList);
        ArrayList<ScheduleItem> itemsToRemove = new ArrayList<>();
        String week;
        switch (desiredWeek) {
            case 1:
                week = "непарний";
                break;
            case 2:
                week = "парний";
                break;
            default:
                week = "all";
        }

        for (ScheduleItem item : filteredList) {
            if (!week.equals("all")) {
                if (!Objects.equals(item.getDayIndex(), fragmentsDay) || (!Objects.equals(item.getWeek(), week) && !Objects.equals(item.getWeek(), "--"))) {
                    itemsToRemove.add(item);
                }
            } else {
                if (!(Objects.equals(item.getDayIndex(), fragmentsDay)))
                    itemsToRemove.add(item);
            }
        }
        filteredList.removeAll(itemsToRemove);
        swap(filteredList);
    }

    public ArrayList<ScheduleItem> getAdaptersList() {
        return mItems;
    }

    public ScheduleItem getItem(int position) {
        return mItems.get(position);
    }

    static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        Context mContext;
        View colorMarkView;
        TextView txtView_auditory;
        TextView txtView_number;
        TextView txtView_discipline;
        TextView txtView_subgroup;

        public ScheduleViewHolder(View itemView) {
            super(itemView);

            mContext = itemView.getContext();
//            colorMarkView = itemView.findViewById(R.id.view_color_mark);
            txtView_auditory = (TextView) itemView.findViewById(R.id.txtView_auditory);
            txtView_number = (TextView) itemView.findViewById(R.id.txtView_number);
            txtView_discipline = (TextView) itemView.findViewById(R.id.txtView_discipline);
            txtView_subgroup = (TextView) itemView.findViewById(R.id.txtView_subgroup);
        }
    }
}
