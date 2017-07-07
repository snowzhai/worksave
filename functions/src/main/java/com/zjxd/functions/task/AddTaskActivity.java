package com.zjxd.functions.task;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zjxd.functions.Global;
import com.zjxd.functions.R;
import com.zjxd.functions.base.BaseActivity;
import com.zjxd.functions.task.help.mSqliteHelp;
import com.zjxd.functions.utils.ShowUtils;
import com.zjxd.functions.utils.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddTaskActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvTime;
    private TextView tvDate;
    private LinearLayout llWfsj;
    private TextView tv_task_title;
    private EditText et_task_title;
    private LinearLayout ll_task_title;
    private TextView tv_task_content;
    private EditText et_task_content;
    private LinearLayout ll_task_content;
    private TextView tv_task_grade;
    private Spinner sp_task_grade;
    private LinearLayout ll_task_grade;
    private Button btn_task_save;
    private SimpleDateFormat sdfWfrq;

    public static String GETDATA = "getdata";
    private Intent intent;
    private Taskdata getstaskdata;
    private Button btn_task_cancle;
    private Button btn_task_change;
    private LinearLayout ll_task_change;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        intent = getIntent();
        if (intent != null) {
            getstaskdata = (Taskdata) intent.getSerializableExtra(GETDATA);
        }
        sdfWfrq = new SimpleDateFormat("yyyyMMdd", Locale.SIMPLIFIED_CHINESE);
        initView();

        if (getstaskdata != null) {
            ShowUtils.i("显示数据",getstaskdata.toString());
            setdata();
        }

    }


    private void initView() {
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvDate = (TextView) findViewById(R.id.tvDate);
        llWfsj = (LinearLayout) findViewById(R.id.llWfsj);
        tv_task_title = (TextView) findViewById(R.id.tv_task_title);
        et_task_title = (EditText) findViewById(R.id.et_task_title);
        ll_task_title = (LinearLayout) findViewById(R.id.ll_task_title);
        tv_task_content = (TextView) findViewById(R.id.tv_task_content);
        et_task_content = (EditText) findViewById(R.id.et_task_content);
        ll_task_content = (LinearLayout) findViewById(R.id.ll_task_content);
        tv_task_grade = (TextView) findViewById(R.id.tv_task_grade);
        sp_task_grade = (Spinner) findViewById(R.id.sp_task_grade);
        ll_task_grade = (LinearLayout) findViewById(R.id.ll_task_grade);
        btn_task_save = (Button) findViewById(R.id.btn_task_save);
        tvDate.setText(sdfWfrq.format(new Date(System.currentTimeMillis())));
        btn_task_cancle = (Button) findViewById(R.id.btn_task_cancle);
        btn_task_change = (Button) findViewById(R.id.btn_task_change);
        ll_task_change = (LinearLayout) findViewById(R.id.ll_task_change);


        ArrayAdapter<String> adapterClf2 = new ArrayAdapter<String>(AddTaskActivity.this, android.R.layout.simple_spinner_item, Global.gradelist);
        adapterClf2.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        sp_task_grade.setAdapter(adapterClf2);
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String wfrq = tvDate.getText().toString().trim();
                try {
                    Date rq = sdfWfrq.parse(wfrq);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(rq);

                    new DatePickerDialog(AddTaskActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                                    tvDate.setText(String.format(Locale.SIMPLIFIED_CHINESE, "%1$04d年%2$02d月%3$02d日", year, monthOfYear + 1, dayOfMonth));
                                    tvDate.setText(String.format(Locale.SIMPLIFIED_CHINESE, "%1$04d%2$02d%3$02d", year, monthOfYear + 1, dayOfMonth));
//                                    tvDate.setText(String.format(Locale.SIMPLIFIED_CHINESE, "yyyyMMdd", year, monthOfYear + 1, dayOfMonth));
                                }
                            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        btn_task_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savedata();
            }
        });
        btn_task_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn_task_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               changedata();
            }
        });

        if (getstaskdata==null){
            ll_task_change.setVisibility(View.GONE);
            btn_task_save.setVisibility(View.VISIBLE);
        }else {
            ll_task_change.setVisibility(View.VISIBLE);
            btn_task_save.setVisibility(View.GONE);
        }


    }

    private void changedata() {
        submit();
        if (StringUtils.isEmpty(getstaskdata.getId()+"")){
            Toast.makeText(this, "数据id为空，修改失败", Toast.LENGTH_LONG).show();
            return;
        }
        Taskdata taskdata = getTaskdata();
        taskdata.setId(getstaskdata.getId());
        mSqliteHelp mHelp = new mSqliteHelp(AddTaskActivity.this);
        if (mHelp.updateTask(taskdata)){
            Intent intent = new Intent();
            setResult(Activity.RESULT_OK, intent);
            finish();
        }else {
            ShowUtils.showShort(AddTaskActivity.this, "修改失败！请重新尝试！");

        }
    }

    private void savedata() {
        submit();
        Taskdata taskdata = getTaskdata();
        mSqliteHelp mHelp = new mSqliteHelp(AddTaskActivity.this);
        if (!mHelp.insert(taskdata)) {
            ShowUtils.showShort(AddTaskActivity.this, "插入失败！");
        } else {
            Intent intent = new Intent();
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }

    @NonNull
    private Taskdata getTaskdata() {
        Taskdata taskdata = new Taskdata();
        taskdata.setDate(tvDate.getText().toString());
        taskdata.setContent(et_task_content.getText().toString().trim());
        taskdata.setTitle(et_task_title.getText().toString().trim());
        taskdata.setGrade(sp_task_grade.getSelectedItemPosition());
        return taskdata;
    }

    private void submit() {
        // validate
        String title = et_task_title.getText().toString().trim();
        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, "title不能为空", Toast.LENGTH_LONG).show();
            return;
        }

        String content = et_task_content.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "content不能为空", Toast.LENGTH_LONG).show();
            return;
        }
    }

    private void setdata() {
        tvDate.setText(getstaskdata.getDate());
        et_task_title.setText(getstaskdata.getTitle());
        et_task_content.setText(getstaskdata.getContent());
        sp_task_grade.setSelection(getstaskdata.getGrade());

    }


}
