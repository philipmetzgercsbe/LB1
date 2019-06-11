package com.example.lb1;

import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lb1.Models.ImageData;
import com.example.lb1.imagesFragment.OnListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ImageData} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 *
 */
public class MyimagesRecyclerViewAdapter extends RecyclerView.Adapter<MyimagesRecyclerViewAdapter.ViewHolder> {

    private final List<ImageData> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyimagesRecyclerViewAdapter(List<ImageData> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_images, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mImageView.setImageBitmap(mValues.get(position).getImage());
        holder.mIdView.setText(mValues.get(position).Id);
        String Description = String.valueOf(mValues.get(position).Latitude +  mValues.get(position).Longitude);
        holder.mContentView.setText(String.format("%s%s", mValues.get(position).getName(), Description));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public ImageData mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.imageData);
            mIdView = (TextView) view.findViewById(R.id.imagename);
            mContentView = (TextView) view.findViewById(R.id.location);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
