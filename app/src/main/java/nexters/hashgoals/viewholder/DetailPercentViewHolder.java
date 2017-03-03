package nexters.hashgoals.viewholder;

import android.view.View;
import android.widget.TextView;

import nexters.hashgoals.R;
import nexters.hashgoals.models.Detail;

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