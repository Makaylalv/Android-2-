package com.john.software.helpeachother.Bean;

import cn.bmob.v3.BmobUser;

/**
 * 用户
 * Created by Administrator on 2018/1/27.
 */
    public class MyUser extends BmobUser {

        private String sex;
        private String myname;
        private String mytel;
        private String school;
        private String myaddress;
        private String des;
        private Integer money;

        public String  getSex() {
            return this.sex;
        }

        public void setSex(String  sex) {
            this.sex = sex;
        }

        public String getSchool() {
            return this.school;
        }

        public void setSchool(String school) {
            this.school = school;
        }

        public String getMyaddress() {
            return myaddress;
        }

        public void setMyaddress(String myaddress) {
            this.myaddress = myaddress;
        }
        public String getDes(){
            return this.des;
        }

        public void setDes(String des) {
             this.des = des;
    }
        public String getMyname(){
           return this.myname;
    }

       public void setMyname(String myname) {
          this.myname = myname;
    }
    public String  getMytel() {
        return this.mytel;
    }

    public void setMytel(String  mytel) {
        this.mytel = mytel;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }
}

