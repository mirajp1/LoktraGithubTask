package com.miraj.loktragithubtask.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.miraj.loktragithubtask.R;
import com.miraj.loktragithubtask.model.Commit;
import com.squareup.picasso.Picasso;

import java.util.List;


public class CommitRVAdapter extends RecyclerView.Adapter<CommitRVAdapter.ViewHolder> {

    private final List<Commit> mValues;
    private final Context mContext;

    public CommitRVAdapter(List<Commit> items, Context context) {
        mValues = items;
        mContext=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.commit_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.mItem = mValues.get(position);

        holder.mCommitterNameTV.setText(holder.mItem.getCommitter().getName());
        holder.mShaTV.setText(holder.mItem.getSha());
        holder.mMessageTV.setText(holder.mItem.getCommitMessage());

        Picasso.with(mContext).load(holder.mItem.getCommitter().getAvatarUrl()).into(holder.mAvatarIV);


    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        private final TextView mCommitterNameTV;
        private final TextView mShaTV;
        private final TextView mMessageTV;
        private final ImageView mAvatarIV;

        Commit mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;

            mCommitterNameTV = (TextView) view.findViewById(R.id.committerNameTV);
            mShaTV = (TextView) view.findViewById(R.id.commitShaTV);
            mMessageTV = (TextView) view.findViewById(R.id.commitMessageTV);
            mAvatarIV = (ImageView) view.findViewById(R.id.avatarIV);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mItem.toString() + "'";
        }
    }
}
