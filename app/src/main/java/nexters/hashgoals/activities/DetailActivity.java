package nexters.hashgoals.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import java.util.ArrayList;
import java.util.List;
import nexters.hashgoals.R;
import nexters.hashgoals.adapters.DetailAdapter;
import nexters.hashgoals.controllers.DetailController;
import nexters.hashgoals.models.CustomPreference;
import nexters.hashgoals.models.Detail;
import nexters.hashgoals.models.DetailData;
import nexters.hashgoals.models.DetailPercent;
import nexters.hashgoals.models.Goal;
import nexters.hashgoals.models.ViewType;
import nexters.hashgoals.validation.EditTextValidation;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.detail_title)
    TextView detailTitle;

    @BindView(R.id.add_task_btn)
    ImageView addTaskBtn;

    @BindView(R.id.recycler_detail_task)
    RecyclerView recyclerView;

    @BindView(R.id.repeat_edit)
    EditText repeatEdit;

    @BindView(R.id.add_task_description)
    EditText addTaskDescription;

    DetailAdapter detailAdapter;
    RecyclerView.LayoutManager layoutManager;
    private static DetailController detailController;
    private List<ViewType> detailList = new ArrayList<ViewType>();
    private List<Detail> list = null;

    Goal goal = null;
    int tempLongNo = 0 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(DetailActivity.this);
        /* use controller*/
        detailController = DetailController.getInstance(getApplicationContext());
        /*
         * getData from GoalActivity
         */
        goal= getIntent().getParcelableExtra("goalInfo");

        /*
        getIntent().getParcelableExtra() 메소드로는 goal의 primary key를 get 할 수 없어서 다음과 같이 불러왔음.
         */
        goal.setMId(CustomPreference.getInstance(getApplicationContext()).getValue("selectedGoalsId",0)); // temp code

        //for debug
        //Log.e("DetailActivity goal id-",goal.getMId()+"");
        Toast.makeText(getApplicationContext(),goal.getMId()+"-goalsmid",Toast.LENGTH_SHORT).show();


        list = detailController . getAllData( goal.getMId() );


        /*
         * check data
         */
        //tempLongNo = (int)goal.getMId(); // Just its have an error and require modify
        makeToPercentNode();
        if(goal.getMTitle().length()>=18)
            detailTitle.setText(goal.getMTitle().substring(0,18)+"..");
        else
            detailTitle.setText(goal.getMTitle());

        /* data view type setting (temp)*/

        /*
         * initialize value.
         */
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        detailAdapter = new DetailAdapter(this,detailList);
        recyclerView.setAdapter(detailAdapter);

    }
    public void makeToPercentNode(){
        Detail divideViewType = null;
        double divideValue = 100.0 ;
        boolean divide20=true,
                divide40=true,
                divide60=true,
                divide80=true;
        if(list==null)
            return;
        /**
         * view type 1 - data node
         * view type 2 - percent node.
         */
        for(int i=0;i<list.size();i++)
            list.get(i).setViewType(1);
        /**
         * for set data node and percent node in adapter
         */
        if(list!=null) {
            /**
             * total repeat no ==> data node + addedNodeCount(percentNode)
             */
            for (int i = 0; i < list.size(); i++) {
                divideViewType = list.get(i);
                divideValue = divideViewType.getPercent();
                Log.e("divideValue-"+divideValue,list.get(i).getRemain()+"-remain");

                //for debug
                //Log.e("DetailActivity ,i-"+i,"divideValue-"+divideValue+"/remain/repeat"+ ((double)divideViewType.getRemain() / (double)divideViewType.getRepeat()));
                if(80 <= divideValue && divide80) {
                    detailList.add(new DetailPercent(2,20));
                    divide80=false;
                } else if (60 <= divideValue && divide60) {
                    detailList.add(new DetailPercent(2,40));
                    divide60=false;
                } else if (40 <= divideValue && divide40) {
                    detailList.add(new DetailPercent(2,60));
                    divide40=false;
                } else if (20 <= divideValue && divide20) {
                    detailList.add(new DetailPercent(2,80));
                    divide20=false;
                }
                detailList.add(list.get(i));
                //divideValue=1.0;
            }
        }
    }
    /**
     * add sub task.
     * @param detail
     */
    public void addNewTask(DetailData detail) {

        detail.setRepeat( Integer.parseInt( repeatEdit.getText().toString() ) );

        detail.setRemain( Integer.parseInt( repeatEdit.getText().toString() ) );

        detail.setTaskName( addTaskDescription.getText().toString() );

        detail.setViewType(1);

        detail.setForeignKey(goal.getMId());

        detail.setPercent(100);

        int rowId = detailController.insertData(detail);

        detail.setId(rowId);

        Toast.makeText(getApplicationContext(),"addNewTask value - "+detail.toString(),Toast.LENGTH_LONG).show();

        detailList.add(detail);

        detailAdapter.notifyDataSetChanged();

    }

    /**
     * When you press the plus image button.
     * @param v
     */
    @OnClick(R.id.add_task_btn)
    public void addTaskBtnClick(View v) {
        EditTextValidation editTextValidation = EditTextValidation.builder().editText(addTaskDescription).build();
        EditTextValidation repeatValidation = EditTextValidation.builder().editText(repeatEdit).build();

        if( !editTextValidation.isValid() ) {
            Toast.makeText(getApplicationContext(), "세부일정을 등록해 주세요", Toast.LENGTH_SHORT).show();
            //requirement auto focus
        }
        else if( !repeatValidation.isNumberic()  ) {
            Toast.makeText(getApplicationContext(), "반복횟수를 등록해 주세요", Toast.LENGTH_SHORT).show();
            //requirement auto focus
        }
        else
            addNewTask(new DetailData());
    }
    @OnClick(R.id.add_task_description)
    public void addTaskDescription(View v ){

    }



    /**
     * repeat editor option requirement the next menu.
     * 1. isNumber?
     * 2. isNotSpace?
     * @param charSequence
     */
    @OnTextChanged(R.id.repeat_edit)
    public void TextChangedAddTaskDescription(CharSequence charSequence) {
        if( charSequence.length() <= 0 )
            return;
        int charSequenceLength = charSequence.length();
        char ch = charSequence.charAt(charSequenceLength-1);
        if( Character.isDigit(ch) )
            return ; // 만약 ch가 숫자라면 ㄱㅊ
        // 아닐경우 하나 지워주기
        charSequence.subSequence(0,charSequenceLength-1);



    }
    /**
     * Visible of cycle dialog
     * @param v
     */
    @OnClick(R.id.repeat_edit)
    public void setRecycleNo(View v) {
    }
    @OnClick(R.id.arrow_back_detail)
    public void onBackBtnClick(View v) {
        finish();
    }

}
    /*
    * To 기호:
    * 아래의 방식으로 클릭된 아이템을 받을 수 있음.
    * Goal goal= getIntent().getParcelableExtra("goalInfo");
    *
    * */