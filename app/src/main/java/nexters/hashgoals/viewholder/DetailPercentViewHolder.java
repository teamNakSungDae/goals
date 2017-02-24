package nexters.hashgoals.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import nexters.hashgoals.R;
import nexters.hashgoals.models.Detail;
import nexters.hashgoals.models.DetailData;
import nexters.hashgoals.models.DetailPercent;

/**
 * Created by kwongiho on 2017. 2. 24..
 */

public class DetailPercentViewHolder extends CustomViewHolder<Detail>{
    private TextView percent;
    public DetailPercentViewHolder(View v) {
        super(v);
        percent = (TextView) v.findViewById(R.id.percent_value);
    }

    public void onBindView(Detail detail) {
        this.percent.setText(detail.getValue());
    }


}