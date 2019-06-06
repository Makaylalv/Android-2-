package com.john.software.helpeachother.Bean;

import cn.bmob.v3.BmobObject;

/*
 * 项目名:   PullLoadMore
 * 包名:     com.john.software.pullloadmore.Bean
 * 文件名:   People
 * 创建者:   software.John
 * 创建时间: 2019/5/8 19:39
 * 描述:      TODO
 */
public class People extends BmobObject {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    private String name;
    private String sex;
    private String school;

}
