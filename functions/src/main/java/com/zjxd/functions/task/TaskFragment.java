package com.zjxd.functions.task;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.zjxd.functions.R;
import com.zjxd.functions.task.adapter.TaskAdapter;
import com.zjxd.functions.task.adapter.TaskAdapter.MyClickListener;
import com.zjxd.functions.task.help.mSqliteHelp;
import com.zjxd.functions.utils.ShowUtils;
import com.zjxd.functions.utils.StringUtils;

import static android.app.Activity.RESULT_OK;


public class TaskFragment extends Fragment implements AdapterView.OnItemClickListener {

    private View viewroot;
    private ListView lvTaskShow;
    private Button btnTaskAdd;

    int RESULTCODE = 0705;
    private Taskdata[] taskdatas;
    private TaskAdapter taskAdapter;
    private mSqliteHelp mHelp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewroot = inflater.inflate(R.layout.function_task, container, false);
        mHelp = new mSqliteHelp(getActivity());
        initView(viewroot);
        return viewroot;
    }


    private void initView(View viewroot) {
        lvTaskShow = (ListView) viewroot.findViewById(R.id.lv_task_show);
        btnTaskAdd = (Button) viewroot.findViewById(R.id.btn_task_add);
        lvTaskShow.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int z=i;
                new AlertDialog.Builder(getActivity())
                        .setTitle("确认删除该条任务吗？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int il) {
//                                deletedata(z);
                                if (!mHelp.deleteTask(taskdatas[z].getId())) {
                                    ShowUtils.showShort(getActivity(), "删除失败！请重新尝试");
                                }else {
                                    initdata();
                                }

                            }
                        }).setNegativeButton("取消",null).show();

                return false;
            }
        });
        btnTaskAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity(), AddTaskActivity.class), RESULTCODE);
            }
        });
        initdata();
    }

    private void deletedata(int i) {
        final int ii=i;

        new Thread() {
            public void run() {

                if (!mHelp.deleteTask(taskdatas[ii].getId())) {
                    ShowUtils.showShort(getActivity(), "更新失败！请重新尝试");
                }

                //这儿是耗时操作，完成之后更新UI；
                taskdatas = mHelp.queryTask(StringUtils.formatDateNow("yyyyMMdd"));

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //更新UI
                        taskAdapter = new TaskAdapter(getActivity(), taskdatas, mListener);
                        lvTaskShow.setAdapter(taskAdapter);
                        taskAdapter.notifyDataSetChanged();
                    }
                });
            }
        }.start();
    }

    private void initdata() {
        new Thread() {
            public void run() {
                //这儿是耗时操作，完成之后更新UI；
                taskdatas = mHelp.queryTask(StringUtils.formatDateNow("yyyyMMdd"));

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //更新UI
                        taskAdapter = new TaskAdapter(getActivity(), taskdatas, mListener);
                        lvTaskShow.setAdapter(taskAdapter);
                        taskAdapter.notifyDataSetChanged();
                    }
                });
            }
        }.start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
//            if (requestCode==RESULTCODE){
//                //刷新列表
//                initdata();
//                ShowUtils.i("刷新","1");
//            }else {
//                initdata();
//                ShowUtils.i("刷新","2");
//
//            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        initdata();
    }



    /**
     * 实现类，响应按钮点击事件
     */
    private MyClickListener mListener = new MyClickListener() {
        @Override
        public void myOnClick(double id, View v) {
            Taskdata data = new Taskdata();
            data.setId(id);
            data.setState(0);
            if (!mHelp.updateTask(data, true)) {
                ShowUtils.showShort(getActivity(), "更新失败！请重新尝试");
            } else {
                initdata();
            }
        }

    };

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
