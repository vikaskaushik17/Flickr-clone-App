package com.vikdev.flickrbrowser;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;

import java.util.List;

class FlickrRecyclerViewAdapter extends RecyclerView.Adapter<FlickrRecyclerViewAdapter.FlickrImageViewHolder> {
    private static final String TAG = "FlickrRecyclerViewAdapt";
    private List<Photo> mPhotoList;
    private Context mContext;

    public FlickrRecyclerViewAdapter(List<Photo> photoList, Context context) {
        this.mPhotoList = photoList;
        this.mContext = context;
    }


    @NonNull
    @Override
    public FlickrImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //called by the layout manager when it needs a new view
        Log.d(TAG, "onCreateViewHolder: new view requested");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browse, parent, false);
        return new FlickrImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlickrImageViewHolder holder, int position) {
        // called by the layout manager when it wants new data in an existing row

        if (mPhotoList == null || (mPhotoList.size()) == 0) {

            holder.thumbnail.setImageResource(R.drawable.placeholer);
            holder.title.setText(R.string.empty_photo);

        } else {
            Photo photoItem = mPhotoList.get(position);
            Log.d(TAG, "onBindViewHolder: " + photoItem.getTitle() + " ----->  " + position);
            Picasso.with(mContext).load(photoItem.getImage()).error(R.drawable.placeholer)
                    .placeholder(R.drawable.placeholer).into(holder.thumbnail);

            holder.title.setText(photoItem.getTitle());
        }
    }

    @Override
    public int getItemCount() {
        //  Log.d(TAG, "getItemCount: called");
        return ((mPhotoList != null) && (mPhotoList.size() != 0) ? mPhotoList.size() : 1);
    }


    void loadNewData(List<Photo> newPhotos) {
        this.mPhotoList = newPhotos;
        notifyDataSetChanged();
    }

    public Photo getPhoto(int position) {
        return ((mPhotoList != null) && (mPhotoList.size() != 0) ? mPhotoList.get(position) : null);
    }

    static class FlickrImageViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "FlickrImageViewHolder";
        ImageView thumbnail = null;
        TextView title = null;

        public FlickrImageViewHolder(View view) {
            super(view);
            Log.d(TAG, "FlickrImageViewHolder: starts");
            this.thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            this.title = (TextView) view.findViewById(R.id.title_recycle);
        }
    }
}
