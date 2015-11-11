package com.yezimm.gathering.protocol.activity;

import com.yezimm.gathering.protocol.user.UserData;

import java.util.List;

/**
 * Created by yezimm on 2015/11/11.
 * 活动单元--指的是发起一个活动，几个人参加等等信息...的映射
 */
public class ActionCell {

    /**
     * 活动ID
     */
    private String actionID ;
    /**
     * 活动发起人，此处使用名字赋值
     */
    private String organiger ;
    /**
     * 活动发起人的ID
     */
    private String organigerID ;
    /**
     * 参与者列表
     */
    private List<UserData> participators ;
    /**
     * 活动名称
     */
    private String actionName ;
    /**
     * 活动地址
     */
    private String actionAddress ;
    /**
     * 活动坐标
     */
    private String actionCoord ;

    public ActionCell(String actionID, String organiger, String organigerID, List<UserData> participators, String actionName, String actionAddress, String actionCoord) {
        this.actionID = actionID;
        this.organiger = organiger;
        this.organigerID = organigerID;
        this.participators = participators;
        this.actionName = actionName;
        this.actionAddress = actionAddress;
        this.actionCoord = actionCoord;
    }

    public String getActionID() {
        return actionID;
    }

    public void setActionID(String actionID) {
        this.actionID = actionID;
    }

    public String getOrganiger() {
        return organiger;
    }

    public void setOrganiger(String organiger) {
        this.organiger = organiger;
    }

    public String getOrganigerID() {
        return organigerID;
    }

    public void setOrganigerID(String organigerID) {
        this.organigerID = organigerID;
    }

    public List<UserData> getParticipators() {
        return participators;
    }

    public void setParticipators(List<UserData> participators) {
        this.participators = participators;
    }

    public String getActionAddress() {
        return actionAddress;
    }

    public void setActionAddress(String actionAddress) {
        this.actionAddress = actionAddress;
    }

    public String getActionCoord() {
        return actionCoord;
    }

    public void setActionCoord(String actionCoord) {
        this.actionCoord = actionCoord;
    }

    public String getActionName() {

        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }
}
