package com.aarcosg.copdhelp.ui.fragment.video;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aarcosg.copdhelp.BuildConfig;
import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.ui.activity.YoutubePlayerActivity;
import com.aarcosg.copdhelp.ui.adapteritem.VideoItem;
import com.aarcosg.copdhelp.ui.fragment.BaseFragment;
import com.aarcosg.copdhelp.utils.Utils;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.laimiux.rxtube.RxTube;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.GenericItemAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.Subscriptions;

public class VideoListFragment extends BaseFragment {

    private static final String TAG = VideoListFragment.class.getCanonicalName();

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.progress)
    ProgressBar mProgressBar;

    private List<String> youtubeIds = new ArrayList<>();
    private FastAdapter<VideoItem> mFastAdapter;
    private GenericItemAdapter<Video, VideoItem> mItemAdapter;
    private Subscription mVideoSubscription = Subscriptions.empty();
    private YouTube mYoutube;
    private RxTube mRxTube;

    public static VideoListFragment newInstance() {
        VideoListFragment fragment = new VideoListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public VideoListFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mYoutube = Utils.createDefaultYouTube(getString(R.string.app_name));
        mRxTube = new RxTube(mYoutube, BuildConfig.YOUTUBE_BROWSER_DEV_KEY);
        youtubeIds.add("QyzCE6KxNpI");
        youtubeIds.add("VvcsR-mxbKI");
        youtubeIds.add("KpRbIuuq9vg");
        youtubeIds.add("RpheX5e6uzU");
        youtubeIds.add("3QKO1qWSB9A");
        youtubeIds.add("xUdFhdOnrMc");
        youtubeIds.add("Mqjym5PdZJA");
        youtubeIds.add("48HdyJliqTE");
        youtubeIds.add("e5JYiVwgt1o");
        youtubeIds.add("E6CqsERG3jI");
        youtubeIds.add("WgETdlGDI5o");
        youtubeIds.add("jilYLoP5vdU");
        youtubeIds.add("RFADi6wAw10");
        youtubeIds.add("_ekS1Gt9Hbc");
        youtubeIds.add("dUdhTX8K6ak");
        youtubeIds.add("Ne3fmCfQJPM");
        youtubeIds.add("medj0kz1pH4");
        youtubeIds.add("UgA9G_rY4NY");
        youtubeIds.add("flLQJSDmHOM");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_video_list, container, false);
        ButterKnife.bind(this, fragmentView);
        setupAdapter();
        setupRecyclerView();
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mProgressBar.setVisibility(View.VISIBLE);
        mVideoSubscription = mRxTube.create(youTube -> {
            final YouTube.Videos.List request = youTube.videos().list("snippet");
            request.setId(TextUtils.join(",", youtubeIds));
            return request;
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            mProgressBar.setVisibility(View.GONE);
                            mItemAdapter.addModel(response.getItems());
                            mItemAdapter.notifyDataSetChanged();
                        }
                        , throwable -> {
                            mProgressBar.setVisibility(View.GONE);
                            Toast.makeText(getContext()
                                    , getString(R.string.exception_message_load_youtube_videos_error)
                                    , Toast.LENGTH_LONG)
                                    .show();
                        }
                );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(!mVideoSubscription.isUnsubscribed()){
            mVideoSubscription.unsubscribe();
        }
    }

    private void setupAdapter() {
        mItemAdapter = new GenericItemAdapter<>(VideoItem.class, Video.class);
        mFastAdapter = new FastAdapter<>();
        mFastAdapter.withOnClickListener((v, adapter, item, position) -> {
            YoutubePlayerActivity.launch(
                    getActivity()
                    , BuildConfig.YOUTUBE_DEV_KEY
                    , item.getModel().getId());
            return false;
        });

    }

    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mItemAdapter.wrap(mFastAdapter));
    }
}