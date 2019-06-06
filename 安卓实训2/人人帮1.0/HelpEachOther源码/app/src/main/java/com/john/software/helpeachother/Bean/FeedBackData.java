package com.john.software.helpeachother.Bean;

import cn.bmob.v3.BmobObject;

/*
 * 项目名:   PullLoadMore
 * 包名:     com.john.software.pullloadmore.Bean
 * 文件名:   FeedBackData
 * 创建者:   software.John
 * 创建时间: 2019/5/29 15:15
 * 描述:      TODO
 */
public class FeedBackData extends BmobObject {
    public String type;
    public String des;
    public String username;
    public String tel_feedback;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTel_feedback() {
        return tel_feedback;
    }

    public void setTel_feedback(String tel_feedback) {
        this.tel_feedback = tel_feedback;
    }
}
