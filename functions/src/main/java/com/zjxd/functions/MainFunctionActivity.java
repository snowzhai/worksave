package com.zjxd.functions;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.zjxd.functions.base.BaseActivity;
import com.zjxd.functions.task.TaskFragment;

public class MainFunctionActivity extends BaseActivity {

    private FrameLayout flTaskFragment;
    private TaskFragment taskFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.function_activity_main);
        initView();
    }

    private void initView() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        taskFragment = new TaskFragment();
        fragmentTransaction.replace(R.id.fl_task_fragment, taskFragment);
        fragmentTransaction.commit();


    }
}
