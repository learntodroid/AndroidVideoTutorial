package com.learntodroid.androidvideotutorial;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MediaStoreVideoFragment extends Fragment implements OnVideoGalleryClickListener {
    private VideoView videoView;
    private List<MediaStoreVideo> mediaStoreVideoList;
    private VideoGalleryRecyclerViewAdapter videoGalleryRecyclerViewAdapter;
    private RecyclerView videoGalleryRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        videoGalleryRecyclerViewAdapter = new VideoGalleryRecyclerViewAdapter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mediastorevideo, container, false);

        videoView = view.findViewById(R.id.fragment_mediastorevideo_videoView);

        videoGalleryRecyclerView = view.findViewById(R.id.fragment_mediastorevideo_galleryRecyclerView);
        videoGalleryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        videoGalleryRecyclerView.setAdapter(videoGalleryRecyclerViewAdapter);

        retrieveVideosFromMediaStore();

        return view;
    }

    private void retrieveVideosFromMediaStore() {
        mediaStoreVideoList = new ArrayList<MediaStoreVideo>();

        String[] projection = new String[] {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.SIZE
        };

        String selection = MediaStore.Video.Media.DURATION + " >= ?";
        String[] selectionArgs = new String[] {
                String.valueOf(TimeUnit.MILLISECONDS.convert(0, TimeUnit.MINUTES))
        };
        String sortOrder = MediaStore.Video.Media.DISPLAY_NAME + " ASC";

        try (Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                sortOrder
        )) {
            // Cache column indices.
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
            int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
            int durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);
            int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE);

            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                long id = cursor.getLong(idColumn);
                String name = cursor.getString(nameColumn);
                int duration = cursor.getInt(durationColumn);
                int size = cursor.getInt(sizeColumn);

                Uri contentUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);

//                Bitmap thumbnail = getActivity().getApplicationContext().getContentResolver().loadThumbnail(contentUri, new Size(640, 480), null);

                // Stores column values and the contentUri in a local object
                // that represents the media file.
                mediaStoreVideoList.add(new MediaStoreVideo(contentUri, name, duration, size));

                Log.i("Video Found", name);
            }

            videoGalleryRecyclerViewAdapter.setVideoList(mediaStoreVideoList);

            Toast.makeText(getContext(), "Videos loaded: " + mediaStoreVideoList.size(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Video loading exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onVideoClick(MediaStoreVideo mediaStoreVideo) {
        Uri uri = mediaStoreVideo.getUri();
        videoView.setVideoURI(uri);
        MediaController mediaController = new MediaController(getActivity());
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
        videoView.start();
    }
}
