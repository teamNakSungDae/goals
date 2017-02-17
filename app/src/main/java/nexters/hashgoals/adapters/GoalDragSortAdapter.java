package nexters.hashgoals.adapters;

import android.content.Context;
import android.database.Cursor;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobeta.android.dslv.DragSortListView;
import com.mobeta.android.dslv.SimpleDragSortCursorAdapter;

import nexters.hashgoals.R;
import nexters.hashgoals.activities.GoalActivity;
import nexters.hashgoals.controllers.GoalDataController;
import nexters.hashgoals.fonts.FontsLoader;
import nexters.hashgoals.helpers.DatabaseHelper;

import static android.R.id.toggle;


/**
 * Created by flecho on 2017. 2. 10..
 */

public class GoalDragSortAdapter extends SimpleDragSortCursorAdapter{

    //Possible Application State
    public static boolean isOnEditMenu = false;

    // View Toggle State
    private static final int TOGGLE_OFF = 0;
    private static final int TOGGLE_ON = 1;

    // Total number of Toggled Buttons
    private static int numOfToggledButtons = 0;

    private class ViewHolder {
        ImageView repeatButton;
        ImageView orderButton; // originally public
        TextView textView;
        ImageView checkBox;
        int toggle;
    }

    /*
    * ListView 속도 문제 개선을 위해, Adapter를 Customize하였다.
    * 최적화를 위해, getView() 메소드(Scroll을 움직일 때마다 계속해서 호출되는)를 최대한 가볍게 만드는 것이 관건.
    * getView 내에서 ViewHolder pattern을 이용하였고, 불필요하게 object를 생성하는 일이 없도록 함.
    * 폰트는 처음 생성자에서 한 번 만든 것을 계속 가져다가 씀.
    * */

    private static Context mContext; // In order to call MainActivity's method
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
        int syncedPosition = position + 1;

        if (convertView == null) {

            LayoutInflater li = (LayoutInflater)mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.list_item_goal, parent, false);

            holder = new ViewHolder();
            holder.repeatButton = (ImageView) convertView.findViewById(R.id.repeat_button);
            holder.orderButton = (ImageView) convertView.findViewById(R.id.order_button);
            holder.textView = (TextView) convertView.findViewById(R.id.goal_content);
            holder.checkBox = (ImageView) convertView.findViewById(R.id.checkbox_off);
            setMemo(syncedPosition, holder.textView);
            holder.textView.setTypeface(FontsLoader.getTypeface(mContext, FontsLoader.N_S_REGULAR));
            holder.toggle = TOGGLE_OFF;

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
            /* Didn't work that effectively.
                if (holder.toggle == TOGGLE_ON)
                    holder.checkBox.setImageResource(R.drawable.checkbox_off);
                */
            // touch feel worse.
            //holder.checkBox.setImageResource(R.drawable.checkbox_off); // for update state renewal.
        }

        /* Setting up Listeners */
        respondToCheckButton(holder, syncedPosition);

        if(isOnEditMenu){
            holder.repeatButton.setVisibility(View.INVISIBLE);
            holder.orderButton.setVisibility(View.VISIBLE);
            holder.checkBox.setVisibility(View.VISIBLE);
        } else {
            holder.repeatButton.setVisibility(View.VISIBLE);
            holder.orderButton.setVisibility(View.INVISIBLE);
            holder.checkBox.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    public static void setEditMenu(boolean value) {
        isOnEditMenu = value;
    }

    private void respondToCheckButton(final ViewHolder holder, int position) {
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // default setting, empty check box is changed to checked check box.
                //Toast.makeText(mContext, "Off", Toast.LENGTH_SHORT).show();

                if (holder.toggle == TOGGLE_OFF) {
                    holder.checkBox.setImageResource(R.drawable.checkbox_on);
                    holder.toggle = TOGGLE_ON;
                    numOfToggledButtons += 1;
                } else {
                    holder.checkBox.setImageResource(R.drawable.checkbox_off);
                    holder.toggle = TOGGLE_OFF;
                    numOfToggledButtons -= 1;
                }

                ((GoalActivity)mContext).changeEditButtonState(numOfToggledButtons);

                //GoalActivity goalActivity = (GoalActivity) mContext;
                //goalActivity.changeEditButtonState(numOfToggledButtons);
                ;

            }
        });
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
