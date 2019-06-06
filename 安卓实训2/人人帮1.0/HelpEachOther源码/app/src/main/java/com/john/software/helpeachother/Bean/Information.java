package com.john.software.helpeachother.Bean;

import cn.bmob.v3.BmobObject;

/**
 *
 * Created by Administrator on 2018/1/29.
 */

public class Information extends BmobObject {
    private String things;
    private String address_take;
    private String time_send;
    private String sign;
    private String name;
    private String tel;
    private String address_send;
    private String types;
    private String school;
    private String password;
    private boolean finish ;
    private String helper;
    private boolean helping;
    private boolean apply;
    private boolean ok;

    public String getThings() {
        return things;
    }

    public void setThings(String things) {
        this.things = things;
    }

    public String getAddress_take() {
        return address_take;
    }

    public void setAddress_take(String address_take) {
        this.address_take = address_take;
    }

    public String getTime_send() {
        return time_send;
    }

    public void setTime_send(String time_send) {
        this.time_send = time_send;
    }

    public String getSign() {
        return sign;
    }
    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress_send() {
        return address_send;
    }

    public void setAddress_send(String address_send) {
        this.address_send = address_send;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public boolean getFinish(){
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public String getHelper() {
        return helper;
    }

    public void setHelper(String helper) {
        this.helper = helper;
    }

    public void setApply(boolean apply) {
        this.apply = apply;
    }
    public boolean getApply(){
        return apply;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }
    public boolean getOk(){
        return ok;
    }

    public void setHelping(boolean helping) {
        this.helping = helping;
    }
    public boolean getHelping(){
        return helping;
    }
}
