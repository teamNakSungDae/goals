package nexters.hashgoals.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobeta.android.dslv.DragSortListView;
import com.mobeta.android.dslv.SimpleDragSortCursorAdapter;

import nexters.hashgoals.R;
import nexters.hashgoals.activities.GoalActivity;
import nexters.hashgoals.controllers.GoalDataController;
import nexters.hashgoals.helpers.DatabaseHelper;

import static android.R.attr.button;


/**
 * Created by flecho on 2017. 2. 10..
 */

public class GoalDragSortAdapter extends SimpleDragSortCursorAdapter{

    private class ViewHolder {
        public ImageView orderButton;
        public TextView textView;
    }

    /*
    * ListView 속도 문제 개선을 위해, Adapter를 Customize하였다.
    * 최적화를 위해, getView() 메소드(Scroll을 움직일 때마다 계속해서 호출되는)를 최대한 가볍게 만드는 것이 관건.
    * getView 내에서 ViewHolder pattern을 이용하였고, 불필요하게 object를 생성하는 일이 없도록 함.
    * 폰트는 처음 생성자에서 한 번 만든 것을 계속 가져다가 씀.
    * */

    private Context mContext; // In order to call MainActivity's method
    private GoalDataController mGoalDataController;
    private static DatabaseHelper mDatabaseHelper; // Uses mDatabaseHelper for convenience.

    public GoalDragSortAdapter(Context context, int layout, Cursor c, String[] from, int[] to, GoalDataController controller){
        super(context, layout, c, from, to, 0);
        mContext = context;
        mGoalDataController = controller;
        mDatabaseHelper = DatabaseHelper.getInstance(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        int positionSync = position + 1;

        if (convertView == null) {

            LayoutInflater li = (LayoutInflater)mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.list_item_goal, parent, false);

            holder = new ViewHolder();
            holder.orderButton = (ImageView) convertView.findViewById(R.id.order_button);
            holder.textView = (TextView) convertView.findViewById(R.id.goal_content);
            setMemo(positionSync, holder.textView);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    public DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
        @Override
        public void drop(int from, int to){

            if (from == to)
                return;
            else {
                from += 1;
                to += 1; // Since db index starts from 1.
            }

            mDatabaseHelper.remapping(from, to);
            reflection();

            // for debugging.
            //Toast.makeText(mContext, "from: " + from + " to: " + to, Toast.LENGTH_SHORT).show();
        }
    };

    public void reflection() {
        Cursor cursor = mDatabaseHelper.getCursor();
        cursor.moveToFirst();
        do {
            Log.d("damn", "index: " + cursor.getString(cursor.getColumnIndex("list_index")));

        } while (cursor.moveToNext());

        changeCursor(cursor);

    }
    
    /*
    * Might be improved using order by query.
    * */
    public void setMemo(int position, TextView textView){

                Cursor cursor = mGoalDataController.getMemoData();
                cursor.moveToFirst();
                try{
                    do {
                        if (Integer.toString(position).equals(cursor.getString(2))) { // 0 was changed to 2.
                            textView.setText(cursor.getString(1));
                            break;
                        }
                    } while (cursor.moveToNext());

                } finally {
                    if(cursor != null)
                        cursor.close();
        }


    }
}
