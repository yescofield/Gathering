package com.yezimm.gathering.protocol.user;

import com.sogou.map.mobile.geometry.Coordinate;

/**
 * Created by yezimm on 2015/11/11.
 */
public class UserData implements Cloneable {

    /**
     * 用户昵称（优先取搜狗地图昵称，否则取手机号，都不存在取邮箱前缀）
     */
    private String mUserName;
    /**
     * 用户Id
     */
    private String mUserId;
    /**
     * 登陆成功后分配给用户的登陆身份
     */
    private String mUserToken;
    /**
     * 登陆后分配给用户的加密数据用的key
     */
    private String mUserKey;
    /**
     * 登陆账号类型，mobile, email, thirdplatform
     */
    private AccountType mAccountType;

    /**
     * 性别 1:男，2：女
     */
    private int gender = 0;
    /**
     * passport登录态
     */
    private String sgid = "";
    /**
     * 头像大图
     */
    private String large_avatar = "";
    /**
     * 头像中图
     */
    private String mid_avatar = "";
    /**
     * 头像小图
     */
    private String tiny_avatar = "";
    /**
     * 第三方登录openid
     */
    private String openId = "";

    private Coordinate mCoord;

    public Coordinate getmCoord() {
        return mCoord;
    }

    public void setmCoord(Coordinate mCoord) {
        this.mCoord = mCoord;
    }

    /*
         * (non-Javadoc)
         *
         * @see java.lang.Object#clone()
         */
    @Override
    public UserData clone() {
        try {
            return (UserData) super.clone();
        } catch (CloneNotSupportedException e) {
            // not reachable
            return this;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("name:" + mUserName + ",");
        builder.append("id:" + mUserId + ",");
        builder.append("token:" + mUserToken + ",");
        builder.append("key:" + mUserKey + ",");
        builder.append("accountType:" + mAccountType);
        return builder.toString();
    }

    /**
     * 获取用户昵称
     */
    public String getUserName() {
        return mUserName;
    }

    /**
     * 设置用户昵称
     */
    public void setUserName(String name) {
        mUserName = name;
    }

    /**
     * 获取用户Id
     */
    public String getUserId() {
        return mUserId;
    }

    /**
     * 设置用户Id
     */
    public void setUserId(String userId) {
        this.mUserId = userId;
    }

    /**
     * 获取登陆成功后分配给用户的登陆身份
     */
    public String getUserToken() {
        return mUserToken;
    }

    /**
     * 设置登陆成功后分配给用户的登陆身份
     */
    public void setUserToken(String token) {
        this.mUserToken = token;
    }

    /**
     * 获取登陆后分配给用户的加密数据用的key
     */
    public String getUserKey() {
        return mUserKey;
    }

    /**
     * 设置登陆后分配给用户的加密数据用的key
     */
    public void setUserKey(String key) {
        this.mUserKey = key;
    }

    /**
     * 获取账号类型
     */
    public AccountType getAccountType() {
        return mAccountType;
    }

    /**
     * 设置账号类型
     */
    public void setAccountType(AccountType accountType) {
        mAccountType = accountType;
    }

    /**
     * 设置账号类型
     * 如果类型无效则#getAccountType()为空
     *
     * @param accountType 账号类型
     * @see AccountType
     */
    void setAccountType(String accountType) {
        if ("mobile".equalsIgnoreCase(accountType)) {
            mAccountType = AccountType.MOBILE;
        } else if ("email".equalsIgnoreCase(accountType)) {
            mAccountType = AccountType.EMAIL;
        } else if ("thirdplatform".equalsIgnoreCase(accountType)) {
            mAccountType = AccountType.THIRD_PLATFORM;
        } else {
            mAccountType = null;
        }
    }

    /**
     * QQ\微博\ sogou passport性别，int型，2：女；1：男
     *
     */
    public int getGender() {
        return gender;
    }

    /**
     * QQ\微博\ sogou passport性别，int型，2：女；1：男
     *
     * @param gender
     */
    public void setGender(int gender) {
        this.gender = gender;
    }

    /**
     * 登录态
     *
     * @return
     */
    public String getSgid() {
        return sgid;
    }

    /**
     * 登录态
     *
     * @return
     */
    public void setSgid(String sgid) {
        this.sgid = sgid;
    }

    /**
     * QQ\微博\ sogou passport大图  ，string类型，头像的URL
     *
     * @return
     */
    public String getLarge_avatar() {
        return large_avatar;
    }

    /**
     * QQ\微博\ sogou passport大图  ，string类型，头像的URL
     *
     * @return
     */
    public void setLarge_avatar(String large_avatar) {
        this.large_avatar = large_avatar;
    }

    /**
     * QQ\微博\ sogou passport中图  ，string类型，头像的URL
     *
     * @return
     */
    public String getMid_avatar() {
        return mid_avatar;
    }

    /**
     * QQ\微博\ sogou passport中图  ，string类型，头像的URL
     *
     * @return
     */
    public void setMid_avatar(String mid_avatar) {
        this.mid_avatar = mid_avatar;
    }

    /**
     * QQ\微博\ sogou passport小图  ，string类型，头像的URL
     *
     * @return
     */
    public String getTiny_avatar() {
        return tiny_avatar;
    }

    /**
     * QQ\微博\ sogou passport小图  ，string类型，头像的URL
     *
     * @param tiny_avatar
     */
    public void setTiny_avatar(String tiny_avatar) {
        this.tiny_avatar = tiny_avatar;
    }

    /**
     * 得到第三方登录openid
     */
    public String getOpenId() {
        return openId;
    }

    /**
     * 设置第三方登录openid
     *
     * @param openId
     */
    public void setOpenId(String openId) {
        this.openId = openId;
    }

    /**
     * 账号类型
     *
     * @author yongliangzhang
     */
    public static enum AccountType {
        /**
         * 手机
         */
        MOBILE,
        /**
         * 邮箱
         */
        EMAIL,
        /**
         * 第三方登陆
         */
        THIRD_PLATFORM_QQ,
        THIRD_PLATFORM_WEIBO,
        THIRD_PLATFORM_RENREN,
        THIRD_PLATFORM_WECHAT,
        THIRD_PLATFORM
    }

    ;
}
