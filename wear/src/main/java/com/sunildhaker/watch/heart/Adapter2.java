package com.sunildhaker.watch.heart;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by s on 15/4/15.
 */


public class Adapter2 extends WearableListView.Adapter {
    private String[] mDataset , mDataset2 , mDataset3 ;
    int[] mFaces ;
    private final Context mContext;
    private final LayoutInflater mInflater;

    // Provide a suitable constructor (depends on the kind of dataset)
    public Adapter2(Context context, String[] dataset , String[] dataset2 , String[] dataset3 , int[] faces ) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mDataset = dataset;
        mDataset2 = dataset2;
        mDataset3 = dataset3;
        mFaces = faces ;
    }

    // Provide a reference to the type of views you're using
    public static class ItemViewHolder extends WearableListView.ViewHolder {
        private TextView name , address , duedate;
        private ImageView imageView ;
        public ItemViewHolder(View itemView) {
            super(itemView);
            // find the text view within the custom item's layout
            name = (TextView) itemView.findViewById(R.id.name);
            address = (TextView) itemView.findViewById(R.id.place);
            duedate = (TextView) itemView.findViewById(R.id.dueDate);
            imageView = (ImageView) itemView.findViewById(R.id.circle);
        }
    }

    // Create new views for list items
    // (invoked by the WearableListView's layout manager)
    @Override
    public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // Inflate our custom layout for list items
        return new ItemViewHolder(mInflater.inflate(R.layout.listitem2, null));
    }

    // Replace the contents of a list item
    // Instead of creating new views, the list tries to recycle existing ones
    // (invoked by the WearableListView's layout manager)
    @Override
    public void onBindViewHolder(WearableListView.ViewHolder holder,
                                 int position) {
        // retrieve the text view
        ItemViewHolder itemHolder = (ItemViewHolder) holder;

        // replace text contents
        itemHolder.name.setText(mDataset[position]);
        itemHolder.address.setText(mDataset3[position]);
        itemHolder.duedate.setText(mDataset2[position]);
        // replace list item's metadata
        itemHolder.imageView.setImageDrawable(mContext.getResources().getDrawable(mFaces[position]));
        holder.itemView.setTag(position);
    }

    // Return the size of your dataset
    // (invoked by the WearableListView's layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}