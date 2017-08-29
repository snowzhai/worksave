package com.zjxd.functions.task;

import java.io.Serializable;

/**
 * 任务数据结构
 */

public class Taskdata implements Serializable {

    private double id;//id 自增长主键
    private String date="";//
    private String title="";//任务标题
    private String content = "";//任务内容
    private  int state = 1;//任务状态 0 已完成  1 未完成 2 正在进行
    private int grade = 0;//任务等级 0 一般 1 重要 2 很重要
    private int time = -1;//任务定时时间 -1 不是定时任务 0 定时任务已完成 >0 定时任务未完成

    public void setId(double id) {
        this.id = id;
    }

    public double getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Date:"+getDate()+"Title:"+getTitle()+"Content:"+getContent()+"State:"+getState()+"Time:"+getTime();
    }
}
