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

import java.util.ArrayList;
import java.util.List;

import butterknife.OnTextChanged;
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

        goal.setMId(CustomPreference.getInstance(getApplicationContext()).getValue("selectedGoalsId",0)); // temp code

        //for debug
        //Log.e("DetailActivity goal id-",goal.getMId()+"");
        //Toast.makeText(getApplicationContext(),goal.getMId()+"",Toast.LENGTH_SHORT).show();
        //List<Detail> list = detailController.getAllData(goal.getMId()); // Just its have an error and require modify in future

        List<Detail> list = detailController . getAllData( goal.getMId() );
        Log.e("detailController",list == null? "list is null" : "list is not null");
        /*
         * check data
         */
        //tempLongNo = (int)goal.getMId(); // Just its have an error and require modify


        detailTitle.setText(goal.getMTitle());
        /*
         * initialize value.
         */
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        detailAdapter = new DetailAdapter(this,detailList);
        recyclerView.setAdapter(detailAdapter);

        if(list!=null)
            for(int i=0;i<list.size();i++)
                detailList.add(list.get(i));


    }

    /**
     * add sub task.
     * @param detail
     */
    public void addNewTask(DetailData detail) {


        detail.setRemainNo( Integer.parseInt( repeatEdit.getText().toString() ) );

        detail.setTaskName( addTaskDescription.getText().toString() );

        detail.setViewType(1);

        int rowId = detailController.insertData(detail);


        detailList.add(detail);

        detailAdapter.notifyDataSetChanged();

    }

    /**
     * add persent line.
     * @param persent
     */
    public void addPersent(double persent) {
        DetailPercent detailPercent = new DetailPercent();
        detailPercent.setViewType(2);
        detailPercent.setValue(Double.toString(persent));


    }

    /**
     * When you press the plus image button.
     * @param v
     */
    @OnClick(R.id.add_task_btn)
    public void addTaskBtnClick(View v) {
        EditTextValidation editTextValidation = EditTextValidation.builder().editText(addTaskDescription).build();
        EditTextValidation repeatValidation = EditTextValidation.builder().editText(repeatEdit).build();

        if( !editTextValidation.isValid() )
            Toast.makeText(getApplicationContext(), "세부일정을 등록해 주세요" ,Toast.LENGTH_SHORT).show();
        else if( !repeatValidation.isNumberic()  )
            Toast.makeText(getApplicationContext(), "반복횟수를 등록해 주세요", Toast.LENGTH_SHORT).show();
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
}
    /*
    * To 기호:
    * 아래의 방식으로 클릭된 아이템을 받을 수 있음.
    * Goal goal= getIntent().getParcelableExtra("goalInfo");
    *
    * */