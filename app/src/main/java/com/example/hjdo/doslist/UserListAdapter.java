package com.example.hjdo.doslist;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    List<UserItem> dataSet = new ArrayList<UserItem> ();
    Context context;

    private RecyclerViewClickListener mListener;
    private boolean isLinear = true;
    public static final String KEY_PREF_NUMBER = "userNumberOpen";

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView_userLogin;
        TextView textView_userType;
        ImageView imageView_userImage;

        public View view;

        public ViewHolder(View v, RecyclerViewClickListener listener){
            super(v);
            textView_userLogin = (TextView) v.findViewById(R.id.textView_userLogin);
            textView_userType = (TextView) v.findViewById(R.id.textView_userType);
            imageView_userImage = (ImageView) v.findViewById(R.id.imageView_userImage);

            mListener = listener;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            mListener.onClick(view, getAdapterPosition());
        }
    }

    public UserListAdapter(Context context, List<UserItem> githubData, RecyclerViewClickListener listener, Boolean isLinear) {
        this.context = context;
        dataSet = githubData;
        this.mListener = listener;
        this.isLinear = isLinear;
    }

    // TODO App Bar - change view type(list/grid)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(isLinear ? R.layout.user_item_row_layout_list : R.layout.user_item_row_layout_grid, null);
        ViewHolder vh = new ViewHolder(v, mListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView_userLogin.setText(dataSet.get(position).getLogin());
        holder.textView_userType.setText(dataSet.get(position).getType());

        //TODO Glide library ㅅㅏ용, Glide Transformation, CircleCrop
        Glide.with(context).load(dataSet.get(position).getAvatar_url()).apply(bitmapTransform(new CircleCrop())).into(holder.imageView_userImage);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public interface RecyclerViewClickListener {
        void onClick(View view, int position);
    }
}
