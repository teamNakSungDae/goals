package nexters.hashgoals.viewholder;

import android.view.View;
import android.widget.TextView;
import nexters.hashgoals.R;
import nexters.hashgoals.models.Detail;
import nexters.hashgoals.models.DetailPercent;
import nexters.hashgoals.models.Percent;

/**
 * Created by kwongiho on 2017. 2. 24..
 */

public class DetailPercentViewHolder extends CustomViewHolder<Percent>{
    private TextView percent;
    public DetailPercentViewHolder(View v) {
        super(v);
        percent = (TextView) v.findViewById(R.id.percent_value);
    }

    public void onBindView(Percent percent) {
        this.percent.setText( Double . toString ( percent . getPercent() ) );
    }


}