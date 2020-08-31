package com.shu.pojo;

import java.util.Date;

public class ChatMsg {

    private String id;


    private String sendUserId;


    private String receiveUserId;


    private String msg;


    private Integer signFlag;


    private Date creatTime;


    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public String getSendUserId() {
        return sendUserId;
    }


    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }


    public String getReceiveUserId() {
        return receiveUserId;
    }


    public void setReceiveUserId(String receiveUserId) {
        this.receiveUserId = receiveUserId;
    }


    public String getMsg() {
        return msg;
    }


    public void setMsg(String msg) {
        this.msg = msg;
    }


    public Integer getSignFlag() {
        return signFlag;
    }


    public void setSignFlag(Integer signFlag) {
        this.signFlag = signFlag;
    }


    public Date getCreatTime() {
        return creatTime;
    }


    public void setCreatTime(Date creatTime) {
        this.creatTime = creatTime;
    }
}