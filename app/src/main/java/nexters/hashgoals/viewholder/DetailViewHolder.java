package nexters.hashgoals.viewholder;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nexters.hashgoals.R;
import nexters.hashgoals.controllers.DetailController;
import nexters.hashgoals.models.Detail;

import static android.view.View.INVISIBLE;

/**
 * Created by kwongiho on 2017. 2. 24..
 */

public class DetailViewHolder extends CustomViewHolder<Detail>{
    //private ImageView imageView;
    @Nullable
    @BindView(R.id.detail_list_task)
    TextView textView;

    @Nullable
    @BindView(R.id.repeat_no)
    TextView repeatNo;

    @Nullable
    @BindView(R.id.low_parent)
    RelativeLayout relativeLayout;

    @BindDrawable(R.drawable.detail_selected_row)
    Drawable detailSelectedRow;

    @BindDrawable(R.drawable.detail_row_background)
    Drawable detailRowBackground;

    //shared field
    public static TextView visibleRepeatTextView;
    public static TextView selectedTextView;
    public static DetailController detailController;
    public static Context context;
    public DetailViewHolder(View v) {
        super(v);
        ButterKnife.bind(this,v);
    }
    private Detail detail;
    /**
     * not used
     * @param detail
     */
    public void onBindView(Detail detail) {

    }
    public void onBindView(Detail detail , Context context) {
        this.textView.setText(detail.getTaskName());
        this.repeatNo.setText(detail.getRemain()+"회 남음");
        this.detail=detail;
        if(detailController == null ) {
            detailController = DetailController.getInstance(context);
            this.context=context;
        }
        //for debug
        //this.repeatNo.setText(detail.getForeignKey()+"-foreign key");
    }
    @OnClick(R.id.low_parent)
    public void onClickByLow(View v) {
        // for debug
        //Log.e("detailViewHolder",textView.getText().toString());
        if(visibleRepeatTextView != null) {
            visibleRepeatTextView.setVisibility(INVISIBLE);
            //selectedTextView.invalidateDrawable(detailRowBackground);
            selectedTextView.setBackgroundResource(R.drawable.detail_row_background);
            selectedTextView.setTextColor(Color.WHITE);
        }
        selectedTextView = textView;
        visibleRepeatTextView = repeatNo;

        /**
         * subText부분의 background 이미자를 변경해줌
         * 그리고 디자인에 맞춘 글씨체 변경
         * 횟수 visible.
         */
        selectedTextView.setBackgroundResource(R.drawable.detail_selected_row);
        selectedTextView.setTextColor(Color.BLACK);
        visibleRepeatTextView.setVisibility(View.VISIBLE);

        /**
         * 횟수 줄이고 , DB에 적용 후 UI에 적용
         */
        if(detail.getRemain()>0)
            detail.setRemain(detail.getRemain()-1);
        Log.e("detail.setRemain",detail.getRemain()+"");

        detail.setPercent( ((double)detail.getRemain()/detail.getRepeat())*100 );
        /**
         *
         */
        detailController.updateRemainAndPercent(detail);
        //Toast.makeText(context,"return value -"+valueee,Toast.LENGTH_SHORT).show();

        visibleRepeatTextView.setText(detail.getRemain()+"회 남음");

        //for debug
        //Toast.makeText(context,((double)detail.getRemain())/detail.getRepeat()+"",Toast.LENGTH_SHORT).show();


    }



}

