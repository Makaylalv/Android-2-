package com.john.software.helpeachother.Bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2018/1/21.
 */

public class Address extends BmobObject {
    //Username
    public String username;
    //名字
    public String personname;
    //电话
    public String tel;
    //省
    private String sheng;
    //市
    private String shi;
    //学校
    private String school;
    //详细地址
    public String addressdes;

    public String getPersonname(){
        return personname;
    }
    public void setPersonname(String personname){
        this.personname=personname;
    }
    public String getTel(){
        return tel;
    }
    public void setTel(String tel){
        this.tel=tel;
    }
    public String getAddressdes(){
        return addressdes;
    }
    public void setAddressdes(String addressdes){
        this.addressdes=addressdes;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getSheng() {
        return sheng;
    }
    public void setSheng(String sheng) {
        this.sheng = sheng;
    }
    public String getShi() {
        return shi;
    }
    public void setShi(String shi) {
        this.shi = shi;
    }
    public String getSchool() {
        return school;
    }
    public void setSchool(String school) {
        this.school = school;
    }
}
