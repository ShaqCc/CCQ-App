package com.ccq.app.ui.message;

import cn.jiguang.imui.commons.models.IUser;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/5/8 23:04
 * 描述：
 * 版本：
 *
 **************************************************/
public class DefaultUser implements IUser {

    private String id;
    private String displayName;
    private String avatar;

    public DefaultUser(String id, String displayName, String avatar) {
        this.id = id;
        this.displayName = displayName;
        this.avatar = avatar;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getAvatarFilePath() {
        return avatar;
    }
}
