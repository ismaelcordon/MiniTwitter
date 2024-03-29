package com.icdominguez.minitwitter.ui.tweets;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.icdominguez.minitwitter.R;
import com.icdominguez.minitwitter.common.Constants;
import com.icdominguez.minitwitter.common.SharedPreferencesManager;
import com.icdominguez.minitwitter.data.TweetViewModel;
import com.icdominguez.minitwitter.retrofit.response.Like;
import com.icdominguez.minitwitter.retrofit.response.Tweet;

import java.util.List;

public class MyTweetRecyclerViewAdapter extends RecyclerView.Adapter<MyTweetRecyclerViewAdapter.ViewHolder> {

    private Context ctx;
    private List<Tweet> mValues;
    private String username = SharedPreferencesManager.getSomeStringValue(Constants.PREF_USERNAME);
    private TweetViewModel tweetViewModel;

    public MyTweetRecyclerViewAdapter(Context context, List<Tweet> items) {
        mValues = items;
        ctx = context;
        tweetViewModel = ViewModelProviders.of((FragmentActivity) ctx).get(TweetViewModel.class);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_tweet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        if(mValues != null) {
            holder.mItem = mValues.get(position);

            holder.tvUsername.setText(holder.mItem.getUser().getUsername());
            holder.tvContent.setText(holder.mItem.getMensaje());
            holder.tvNumLikes.setText(String.valueOf(holder.mItem.getLikes().size()));

            String photo = holder.mItem.getUser().getPhotoUrl();
            if(!photo.equals("")) {
                Glide.with(ctx).load("https://www.minitwitter.com/apiv1/uploads/photos/" + photo).into(holder.ivAvatar);
            } else {
                holder.ivAvatar.setBackground(ctx.getResources().getDrawable(R.drawable.ic_baseline_account_circle_24));
            }

            Glide.with(ctx)
                    .load(R.drawable.ic_like)
                    .into(holder.ivLike);
            holder.tvNumLikes.setTextColor(ctx.getResources().getColor(android.R.color.black));
            holder.tvNumLikes.setTypeface(null, Typeface.NORMAL);

            holder.ivShowMenu.setVisibility(View.GONE);

            if(holder.mItem.getUser().getUsername().equals(username)) {
                holder.ivShowMenu.setVisibility(View.VISIBLE);
            }

            holder.ivShowMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tweetViewModel.openDialogTweetMenu(ctx, holder.mItem.getId());
                }
            });

            holder.ivLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tweetViewModel.likeTweet(holder.mItem.getId());
                }
            });

            for(Like like : holder.mItem.getLikes()) {
                if(like.getUsername().equals(username)) {
                    Glide.with(ctx)
                            .load(R.drawable.ic_like_pink)
                            .into(holder.ivLike);
                    holder.tvNumLikes.setTextColor(ctx.getResources().getColor(R.color.pink));
                    holder.tvNumLikes.setTypeface(null, Typeface.BOLD);
                    break;
                }
            }
        }

    }

    public void setData(List<Tweet> tweetList) {
        this.mValues = tweetList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {

        if(mValues != null) {
            return mValues.size();
        } else return 0;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView tvUsername;
        public final TextView tvContent;
        public final ImageView ivAvatar;
        public final ImageView ivLike;
        public final ImageView ivShowMenu;
        public final TextView tvNumLikes;
        public Tweet mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvUsername = view.findViewById(R.id.textViewUsername);
            tvContent = view.findViewById(R.id.textViewContent);
            ivAvatar = view.findViewById(R.id.imageViewUserPhoto);
            ivLike = view.findViewById(R.id.imageViewLike);
            tvNumLikes = view.findViewById(R.id.textViewNumLikes);
            ivShowMenu = view.findViewById(R.id.imageViewShowMenu);
        }
    }
}