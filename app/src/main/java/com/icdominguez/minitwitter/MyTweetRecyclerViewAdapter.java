package com.icdominguez.minitwitter;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.icdominguez.minitwitter.common.Constants;
import com.icdominguez.minitwitter.common.SharedPreferencesManager;
import com.icdominguez.minitwitter.retrofit.response.Like;
import com.icdominguez.minitwitter.retrofit.response.Tweet;

import java.util.List;

public class MyTweetRecyclerViewAdapter extends RecyclerView.Adapter<MyTweetRecyclerViewAdapter.ViewHolder> {

    private Context ctx;
    private List<Tweet> mValues;
    private String username = SharedPreferencesManager.getSomeStringValue(Constants.PREF_USERNAME);

    public MyTweetRecyclerViewAdapter(Context context, List<Tweet> items) {
        mValues = items;
        ctx = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_tweet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.tvUsername.setText(holder.mItem.getUser().getUsername());
        holder.tvContent.setText(holder.mItem.getMensaje());
        holder.tvNumLikes.setText(String.valueOf(holder.mItem.getLikes().size()));

        String photo = holder.mItem.getUser().getPhotoUrl();
        if(!photo.equals("")) {
            Glide.with(ctx)
                    .load("https://www.minitwitter.com/apiv1/uploads/photos/" + holder.mItem.getUser().getPhotoUrl())
                    .into(holder.ivAvatar);
        }

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

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView tvUsername;
        public final TextView tvContent;
        public final ImageView ivAvatar;
        public final ImageView ivLike;
        public final TextView tvNumLikes;
        public Tweet mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvUsername = view.findViewById(R.id.textViewUsername);
            tvContent = view.findViewById(R.id.textViewContent);
            ivAvatar = view.findViewById(R.id.imageViewAvatar);
            ivLike = view.findViewById(R.id.imageViewLike);
            tvNumLikes = view.findViewById(R.id.textViewNumLikes);
        }
    }
}