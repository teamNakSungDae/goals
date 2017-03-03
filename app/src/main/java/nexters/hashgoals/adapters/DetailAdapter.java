package nexters.hashgoals.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import nexters.hashgoals.R;
import nexters.hashgoals.models.Detail;
import nexters.hashgoals.viewholder.CustomViewHolder;
import nexters.hashgoals.viewholder.DetailPercentViewHolder;
import nexters.hashgoals.viewholder.DetailViewHolder;

/**
 * Created by kwongiho on 2017. 2. 23..
 */

public class DetailAdapter extends CustomAdapter<Detail,CustomViewHolder> {

    public static final int TASK = 1;
    public static final int PERCENT = 2;
    public DetailAdapter (Context context , List<Detail> lists) {
        super(context,lists);
    }

    public CustomViewHolder onCreateViewHolder(ViewGroup parent , int viewType) {
        View view = null;

        switch( viewType ) {
            case TASK :
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_detail,parent,false);
                return new DetailViewHolder(view);
            case PERCENT :
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_detail_percent,parent,false);
                return new DetailPercentViewHolder(view);
            default:
                return null;
        }

    }

    public void onBindViewHolder(CustomViewHolder holder , int position) {
        switch ( position ) {
            case TASK :
                DetailViewHolder detailViewHolder = (DetailViewHolder)holder;
                detailViewHolder.onBindView(list.get(position),context);
                break;
            case PERCENT :
                DetailPercentViewHolder detailPercentViewHolder = (DetailPercentViewHolder)holder;
                detailPercentViewHolder.onBindView(list.get(position));
                break;
            default:
                return ;
        }
    }
    @Override
    public int getItemCount(){
        return list.size();
    }


}