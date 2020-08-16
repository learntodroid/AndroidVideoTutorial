package com.learntodroid.androidvideotutorial;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class VideoGalleryRecyclerViewAdapter extends RecyclerView.Adapter<VideoGalleryRecyclerViewAdapter.VideoRecyclerViewHolder> {
    private List<MediaStoreVideo> mediaStoreVideoList;
    private OnVideoGalleryClickListener listener;

    public VideoGalleryRecyclerViewAdapter(OnVideoGalleryClickListener listener) {
        mediaStoreVideoList = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public VideoRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_galleryvideo, parent, false);
        return new VideoRecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoRecyclerViewHolder holder, int position) {
        MediaStoreVideo mediaStoreVideo = mediaStoreVideoList.get(position);
        holder.bind(mediaStoreVideo, listener);
    }

    @Override
    public int getItemCount() {
        return mediaStoreVideoList.size();
    }

    public void setVideoList(List<MediaStoreVideo> mediaStoreVideoList) {
        this.mediaStoreVideoList = mediaStoreVideoList;
        notifyDataSetChanged();
    }

    class VideoRecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView title, desc;
        private Button play;

        public VideoRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.item_galleryvideo_title);
            desc = itemView.findViewById(R.id.item_galleryvideo_desc);
            play = itemView.findViewById(R.id.item_galleryvideo_play);
        }

        public void bind(final MediaStoreVideo mediaStoreVideo, final OnVideoGalleryClickListener listener) {
            title.setText(mediaStoreVideo.getName());
            desc.setText("Duration: " + mediaStoreVideo.getDuration() + ", Size: " + mediaStoreVideo.getSize());
            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onVideoClick(mediaStoreVideo);
                }
            });
        }
    }
}
