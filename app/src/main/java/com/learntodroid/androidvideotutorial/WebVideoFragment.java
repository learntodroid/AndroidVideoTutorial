package com.learntodroid.androidvideotutorial;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class WebVideoFragment extends Fragment {
    private VideoView videoView;
    private EditText uriEditText;
    private Button playButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_webvideo, container, false);

        videoView = view.findViewById(R.id.fragment_webvideo_videoView);
        uriEditText = view.findViewById(R.id.fragment_webvideo_uriEditText);
        playButton = view.findViewById(R.id.fragment_webvideo_play);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playVideoFromWeb();
            }
        });

        return view;
    }

    private void playVideoFromWeb() {
        Uri uri = Uri.parse(uriEditText.getText().toString());
        videoView.setVideoURI(uri);
        MediaController mediaController = new MediaController(getActivity());
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
        videoView.start();
    }
}
