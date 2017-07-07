package com.zjxd.functions.task.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjxd.functions.R;
import com.zjxd.functions.task.AddTaskActivity;
import com.zjxd.functions.task.Taskdata;

/**
 * listview 模块
 */

public class TaskAdapter extends BaseAdapter implements View.OnClickListener {
    Context con;
    ViewHolder holder;
    Taskdata[] data;
//    private CallBack callBack;
    private Intent intent;
    MyClickListener myClickListener;
    public class ViewHolder {
        public View rootView;
        //        public CheckBox ckTaskZt;
        public TextView tvTaskBt;
        public TextView tvFaceXsd;
        public TextView tvTaskNr;
        public LinearLayout lvTaskBj;
        public TextView tvTaskDetail;
        public Button btnTaskOk;


        public ViewHolder(View rootView) {
            this.rootView = rootView;
//            this.ckTaskZt = (CheckBox) rootView.findViewById(R.id.ck_task_zt);
            this.tvTaskBt = (TextView) rootView.findViewById(R.id.tv_task_bt);
            this.tvFaceXsd = (TextView) rootView.findViewById(R.id.tv_face_xsd);
            this.tvTaskNr = (TextView) rootView.findViewById(R.id.tv_task_nr);
            this.lvTaskBj = (LinearLayout) rootView.findViewById(R.id.lv_task_bj);
            this.tvTaskDetail = (TextView) rootView.findViewById(R.id.tv_task_detail);
            this.btnTaskOk = (Button) rootView.findViewById(R.id.btn_task_ok);

        }

    }

    public TaskAdapter(Context context, Taskdata[] data2, MyClickListener myClickListener2) {
        con = context;
        data = data2;
        myClickListener = myClickListener2;
    }

    @Override
    public int getCount() {
        if (data == null) {
            return 0;
        }
        return data.length;
    }

    @Override
    public Object getItem(int i) {
        if (data == null) {
            return null;
        }
        return data[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        if (arg1 == null) {
            arg1 = LayoutInflater.from(con).inflate(R.layout.function_task_listview, arg2, false);
            holder = new ViewHolder(arg1);
            arg1.setTag(holder);//利用setTagde 方法可以避免listview错位或者多次加载的问题。
        } else {
            holder = (ViewHolder) arg1.getTag();
        }
        holder.tvFaceXsd.setText(data[arg0].getTitle());
        holder.tvTaskNr.setText(data[arg0].getContent());
        holder.btnTaskOk.setOnClickListener(myClickListener);
        holder.btnTaskOk.setTag( data[arg0].getId());

//        holder.ckTaskZt.setChecked("0".equals(data[arg0].getState()) ? true : false);
//        holder.ckTaskZt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//               if (b){
//                   holder.ckTaskZt.setTag(R.id.tag_first,0);
//               }else {
//                   holder.ckTaskZt.setTag(R.id.tag_first,1);
//               }
//            }
//        });
//        holder.ckTaskZt.setTag(R.id.tag_second,data[arg0].getId());
        holder.tvTaskDetail.setOnClickListener(this);
        holder.tvTaskDetail.setTag(arg0);


        switch (data[arg0].getState()) {
            case 0:
                holder.lvTaskBj.setBackgroundColor(con.getResources().getColor(R.color.colorwc));
                break;
            case 1:
                switch (data[arg0].getGrade()) {
                    case 0:
                        holder.lvTaskBj.setBackgroundColor(con.getResources().getColor(R.color.coloryb));
                        break;
                    case 1:
                        holder.lvTaskBj.setBackgroundColor(con.getResources().getColor(R.color.colorzy));
                        break;
                    case 2:
                        holder.lvTaskBj.setBackgroundColor(con.getResources().getColor(R.color.colorfczy));
                        break;

                }
                break;

        }
        return arg1;
    }

//    public interface CallBack {
//        void check(View v);
////        public void detail(View v);
//    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.tv_task_detail) {
            intent = new Intent(con, AddTaskActivity.class);
            intent.putExtra(AddTaskActivity.GETDATA, data[(int) view.getTag()]);
            con.startActivity(intent);
        }
    }

    /**
     * * 用于回调的抽象类
     * * @author zhai
     * * 2017-07-06
     *
     * */
    public static abstract class MyClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            myOnClick((double) v.getTag(), v);
        }

        public abstract void myOnClick(double id, View v);
    }


}
