package com.phuongptt.funnycal;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.phuongptt.funnycal.services.models.User;

import java.util.List;

/**
 * Created by tranvietthang on 11/28/16.
 */

public class HighScoreListAdapter extends RecyclerView.Adapter<HighScoreListAdapter.HighScoreViewHolder> {


    private List<User> users;

    public HighScoreListAdapter(List<User> users) {
        this.users = users;
    }

    @Override
    public int getItemCount() {
        Log.d(this.getClass().getName(), "getItemCount: " + users.size());
        return users.size();
    }

    @Override
    public HighScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_high_score, parent, false);
        return new HighScoreViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HighScoreViewHolder holder, int position) {
        User user = users.get(position);
        holder.getName().setText(user.fullName);
        holder.getScore().setText(String.valueOf(user.score));
        holder.getNumber().setText("No. " + String.valueOf(position + 1));
    }

    public class HighScoreViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView score;
        private TextView number;

        public HighScoreViewHolder(View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.name);
            score = (TextView) view.findViewById(R.id.score);
            number = (TextView) view.findViewById(R.id.number);
        }

        public TextView getName() {
            return name;
        }

        public void setName(TextView name) {
            this.name = name;
        }

        public TextView getScore() {
            return score;
        }

        public void setScore(TextView score) {
            this.score = score;
        }

        public TextView getNumber() {
            return number;
        }

        public void setNumber(TextView number) {
            this.number = number;
        }
    }

}
