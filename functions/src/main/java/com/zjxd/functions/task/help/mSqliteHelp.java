package com.zjxd.functions.task.help;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zjxd.functions.task.Taskdata;

import java.util.ArrayList;

/**
 *  2017/7/4.
 *  数据库操作类
 */

public  class mSqliteHelp extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "worksave.db";
    private static final String TABLE_NAME = "tasksave";
    private static final String ID_COL = "id";
    private static final String DATE = "date";//任务有效日期
    private static final String TITLE = "title";//任务标题
    private static final String CONTENT = "content";//任务内容
    private static final String STATE = "state";//任务状态 0 已完成 1 未完成 2 正在进行
    private static final String GRADE = "grade";//任务等级 0 一般 1 重要 2 很重要
    private static final String TIME = "time";//任务定时时间 -1 不是定时任务 0 定时任务已完成 >0 定时任务未完成



    public mSqliteHelp(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                        ID_COL + " INTEGER PRIMARY KEY, " +
                        DATE + " CHAR(10), " +
                        TITLE + " CHAR(15), " +
                        CONTENT + " CHAR(30), " +
                        STATE + " INTEGER, " +
                        GRADE + " INTEGER, " +
                        TIME + " INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int Version) {
        switch (oldVersion){
            case 1:
            case 2:

        }
    }

    /**
     * 插入一条任务数据
     *
     * @param data
     * @return
     */
    public Boolean insert( Taskdata data){
        if (data!=null){
            SQLiteDatabase db = this.getReadableDatabase();
            try {
                db.execSQL("insert into " + TABLE_NAME + " (" + DATE + "," + TITLE + "," + CONTENT + ","
                        + STATE + "," + GRADE + "," + TIME + ") values('"
                        + data.getDate()
                        + "','" +
                        data.getTitle()
                        + "','" +
                        data.getContent()
                        + "'," +
                        data.getState()
                        + "," +
                        data.getGrade()
                        + "," +
                        data.getTime()
                        + ");");
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
            return  true;
        }else {
            return false;
        }
    }

    /**
     * 查询某一天的任务列表
     * @param date 日期
     * @return
     */
    public Taskdata[] queryTask(String date ){
       return queryTask(date,-1,-1,true);
    }

    /**
     * 查询某一天的任务列表
     * @param date 日期
     * @param state 任务状态
     * @return
     */
    public Taskdata[] queryTask(String date,int state ){
       return queryTask(date,state,-1,true);
    }

    /**
     *
     * 查询某一天的任务列表
     * @param date 日期
     * @param state 任务状态
     * @param grade  任务等级
     * @return
     */
    public Taskdata[] queryTask(String date,int state ,int grade){
       return queryTask(date,state,grade,true);
    }
    /**
     *
     * 查询某一天的任务列表
     * @param date  传入的日期 格式  20170704
     * @param state  任务状态 -1 表示没有条件
     * @param grade  任务等级  -1 表示没有条件
     * @param order  true 降序   false 升序
     * @return
     */
    public Taskdata[] queryTask(String date,int state,int grade,boolean order ){
        ArrayList<Taskdata> lst = new ArrayList<>();
        Taskdata tmp;
        SQLiteDatabase db;
        try {
            db = this.getReadableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        Cursor cur;
        try {
            cur=db.rawQuery("select * from "+TABLE_NAME+
                            " where "+DATE+"='"+date+"'"+
                             (state==-1?"":" and state="+state)+      //判断有没有任务状态的条件
                            (grade==-1?"":" and grade="+grade)+      //判断有没有任务等级的条件
                          " ORDER BY "+STATE +                      //按照任务状态 降序排列
                          " DESC ,"+GRADE +                                   //按照等级高低 降序排列
                             (order?" DESC":" ASC")                       //按照 等级 升序还是降序排列
                          ,null);
        }catch  (Exception e) {
//            db.close();
            return null;
        }
        if (cur != null) {
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                do {
                    try {
                        tmp=new Taskdata();
                        tmp.setId(cur.getDouble(cur.getColumnIndex(ID_COL)));
                        tmp.setDate(cur.getString(cur.getColumnIndex(DATE)));
                        tmp.setTitle(cur.getString(cur.getColumnIndex(TITLE)));
                        tmp.setContent(cur.getString(cur.getColumnIndex(CONTENT)));
                        tmp.setState(cur.getInt(cur.getColumnIndex(STATE)));
                        tmp.setGrade(cur.getInt(cur.getColumnIndex(GRADE)));
                        tmp.setTime(cur.getInt(cur.getColumnIndex(TIME)));
                    } catch (Exception e) {
                        e.printStackTrace();
                        db.close();
                        tmp = null;
                    }
                    if (tmp != null)
                        lst.add(tmp);

                } while (cur.moveToNext());
            }
            cur.close();
        }
        db.close();
        return lst.toArray(new Taskdata[lst.size()]);
    }

    /**
     * 修改任务内容
     * @param data
     * @return
     */
    public boolean  updateTask(Taskdata data){
        return updateTask(data,false);
    }


    /**
     * 修改任务内容
     * @param data 要修改的任务内容  id 是原来那条的
     * @param state 是否只修改任务的状态
     * @return
     */
    public boolean  updateTask(Taskdata data,boolean state){
        SQLiteDatabase db;
        try {
            db = this.getReadableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        ContentValues cv = new ContentValues();
        if (state){
            cv.put(STATE , data.getState());
        }else{
            cv.put(DATE , data.getDate());
            cv.put(TITLE , data.getTitle());
            cv.put(CONTENT , data.getContent());
            cv.put(GRADE , data.getGrade());
            cv.put(TIME , data.getTime());
        }
        String[] args = {String.valueOf(data.getId())};
        int update = db.update(TABLE_NAME, cv, ID_COL + "=?", args);

        db.close();
        return update>0?true:false;
    }

    /**
     * 删除任务
     * @param id  任务id
     * @return
     */
    public boolean deleteTask(double id){
        SQLiteDatabase db;
        try {
            db = this.getReadableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        String[] args = {String.valueOf(id)};
        int delete=db.delete(TABLE_NAME, ID_COL+"=?" , args);
        return delete>0?true:false;
    }

}
