package nexters.hashgoals.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import nexters.hashgoals.R;
import nexters.hashgoals.models.Detail;

/**
 * Created by kwongiho on 2017. 2. 24..
 */

public class DetailViewHolder extends CustomViewHolder<Detail>{
    private ImageView imageView;
    private TextView textView;
    public DetailViewHolder(View v) {
        super(v);

        imageView = (ImageView) v.findViewById(R.id.percent_status);
        textView = (TextView) v.findViewById(R.id.detail_list_task);
    }

    /**
     * not used
     * @param detail
     */
    public void onBindView(Detail detail) {

    }
    public void onBindView(Detail detail , Context context) {

        this.textView.setText(detail.getValue());
        Glide.with(context).load( detail.getRemainNumber() );
    }

}
