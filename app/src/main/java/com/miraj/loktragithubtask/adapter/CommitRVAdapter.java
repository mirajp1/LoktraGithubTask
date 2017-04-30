package com.miraj.loktragithubtask.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.miraj.loktragithubtask.Constants;
import com.miraj.loktragithubtask.R;
import com.miraj.loktragithubtask.model.Commit;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class CommitRVAdapter extends RecyclerView.Adapter<CommitRVAdapter.ViewHolder> implements Filterable{

    private List<Commit> mValues;
    private final List<Commit> originalValues;
    private final Context mContext;
    private final SharedPreferences sp;
    private boolean shownAll;

    public CommitRVAdapter(List<Commit> items, Context context) {
        mValues = items;
        mContext=context;
        sp=mContext.getSharedPreferences(Constants.SHARED_PREFS_NAME,mContext.MODE_PRIVATE);
        originalValues = new ArrayList<>(mValues);
        shownAll=true;
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

        holder.mCommitterNameTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(holder.mItem.getCommitter().getCommitterUrl()));
                mContext.startActivity(intent);

            }
        });

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(holder.mItem.getCommitUrl()));
                mContext.startActivity(intent);

            }
        });


        if(holder.mItem.isBookmarked()){

            holder.mBookmarkedIV.setImageResource(R.drawable.bookmarked);

        }
        else {
            holder.mBookmarkedIV.setImageResource(R.drawable.unbookmarked);
        }

        holder.mBookmarkedIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = sp.edit();

                if(holder.mItem.isBookmarked()){

                    editor.remove(holder.mItem.getSha());
                    editor.apply();
                    holder.mBookmarkedIV.setImageResource(R.drawable.unbookmarked);
                    holder.mItem.setBookmarked(false);

                }
                else{
                    editor.putBoolean(holder.mItem.getSha(),true);
                    editor.apply();
                    holder.mBookmarkedIV.setImageResource(R.drawable.bookmarked);
                    holder.mItem.setBookmarked(true);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                FilterResults filterResults = new FilterResults();

                if(charSequence.equals(Constants.FILTER_BOOKMARK)) {

                    List<Commit> newValues = new ArrayList<>();

                    for (Commit commit : originalValues) {

                        if (commit.isBookmarked()) {
                            newValues.add(commit);
                        }

                    }

                    filterResults.values = newValues;
                    filterResults.count = newValues.size();

                }

                else if (charSequence.equals(Constants.FILTER_ALL)){

                    filterResults.values=originalValues;
                    filterResults.count=originalValues.size();

                }


                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                mValues= (List<Commit>) filterResults.values;
                notifyDataSetChanged();

            }
        };

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        private final TextView mCommitterNameTV;
        private final TextView mShaTV;
        private final TextView mMessageTV;
        private final ImageView mAvatarIV;
        private final ImageView mBookmarkedIV;

        Commit mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;

            mCommitterNameTV = (TextView) view.findViewById(R.id.committerNameTV);
            mShaTV = (TextView) view.findViewById(R.id.commitShaTV);
            mMessageTV = (TextView) view.findViewById(R.id.commitMessageTV);
            mAvatarIV = (ImageView) view.findViewById(R.id.avatarIV);
            mBookmarkedIV = (ImageView) view.findViewById(R.id.bookmarkIV);


        }

        @Override
        public String toString() {
            return super.toString() + " '" + mItem.toString() + "'";
        }
    }
}
