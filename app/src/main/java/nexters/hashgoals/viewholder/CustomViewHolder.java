package nexters.hashgoals.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by kwongiho on 2017. 2. 23..
 */

public abstract class CustomViewHolder<E> extends RecyclerView.ViewHolder {
    public CustomViewHolder(View itemView) {
        super(itemView);
    }
    public abstract void onBindView(E item);
}
