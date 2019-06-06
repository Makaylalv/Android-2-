package com.john.software.helpeachother.code.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.john.software.helpeachother.Bean.Address;
import com.john.software.helpeachother.Bean.MyUser;
import com.john.software.helpeachother.R;
import com.john.software.helpeachother.code.LoadingDialog.LoadingDialog;
import com.john.software.helpeachother.code.Util.MyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class AddNewAddress extends AppCompatActivity {
    private String name,tel,des,id,username,add_sheng,add_shi,add_school;
    private TextView title,tv_sheng,tv_shi,tv_quxian,tv_show,tv_tel;
    private ImageButton back;
    private EditText et_name,et_des;
    private LoadingDialog loadingDialog;
    private Handler handler;
    private Mycity mycity;
    private Button btn_add_address;
    private ArrayList<String []> list_shi,sheng_school;
    private String  code;
    private CityChooseDialog cityChooseDialog;
    private ListView show_city;
    private int pos_sheng,o;
    private MyUser useri;
    private boolean shengc=false;
    public String [] sheng,item,shi_beiji,shi_tianjin,shi_hebei,shi_shanxi,shi_shanxi1,shi_neimenggu,shi_liaoning,
            shi_heilongjiang,shi_jilin,shi_shanghai,shi_jiangsu,shi_zhejiang,shi_anhui,shi_fujian
            ,shi_jiangxi,shi_shandong,shi_henan,shi_hubei,shi_hunan,shi_guangdong,shi_guangxi,
            shi_hainan,shi_chongqing,shi_sichuan,shi_guizhou,shi_yunnan,shi_xizang,shi_gansu
            ,shi_qinghai,shi_ningxia,shi_xinjiang,shi_taiwan,shi_xianggan,shi_aomen
            ,beijing_school,tianjin_school,hebei_school,shanxi_school,neimenggu_school,liaoning_school,
            heilongjiang_school,jilin_school,shanghai_school,jiangsu_school,zhejiang_school,anhui_school,
            fujian_school,jiangxi_school,shandong_school,henan_school,hubei_school,hunan_school,guangdong_school,
            guangxi_school,hainan_school,chongqing_school,sichuan_school,guizhou_school,yunnan_school,xizang_school,
            gansu_school,qinghai_school,ningxia_school,xinjiang_school,taiwan_school,xianggan_school,aomen_school;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_address);
        final Map<String, String> map= new HashMap<>();
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#FF1A92E7"),false);
        et_name= (EditText) findViewById(R.id.et_new_address_name);
        //et_tel= (EditText) findViewById(R.id.et_new_address_tel);
        et_des= (EditText) findViewById(R.id.et_new_address_des);
        back= (ImageButton) findViewById(R.id.detial_btn_back);
        title= (TextView) findViewById(R.id.detial_tv_title);
        tv_sheng= (TextView) findViewById(R.id.tv_sheng);
        tv_shi= (TextView) findViewById(R.id.tv_shi);
        tv_tel= (TextView) findViewById(R.id.tv_tel);
        tv_quxian= (TextView) findViewById(R.id.tv_quxian);
        btn_add_address= (Button) findViewById(R.id.btn_add_address);
        useri= BmobUser.getCurrentUser(MyUser.class);
        initcity();
        mycity=new Mycity();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showNormalDialog();
                finish();
            }
        });
        Intent it=getIntent();
        code=it.getStringExtra("code");
        if(code.equals("0")){
            title.setText("修改地址");
            btn_add_address.setText("修改");
            name=it.getStringExtra("name");
            //tel=it.getStringExtra("tel");
            des=it.getStringExtra("des");
            id=it.getStringExtra("Id");
            username=it.getStringExtra("username");
            tv_sheng.setText(it.getStringExtra("sheng"));
            tv_shi.setText(it.getStringExtra("shi"));
            tv_quxian.setText(it.getStringExtra("school"));
            et_name.setText(name);
            et_name.setSelection(name.length());
            tv_tel.setText(useri.getUsername());
            //et_tel.setSelection(tel.length());
            et_des.setText(des);
            et_des.setSelection(des.length());
        }else if(code.equals("1")){
            username=it.getStringExtra("username");
            title.setText("新增地址");
            btn_add_address.setText("提交");
            tv_tel.setText(useri.getUsername());
            MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
            if(userInfo!=null){
                tv_quxian.setText(userInfo.getSchool());
            }else {tv_quxian.setText("");}

        }
        tv_sheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置省级
                o=0;
                shengc=true;
                tv_shi.setText("");
                tv_quxian.setText("");
                cityChooseDialog=new CityChooseDialog();
                cityChooseDialog.startLoadingDialog(AddNewAddress.this);
                show_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        pos_sheng=position;
                        tv_sheng.setText(sheng[position]);
                        cityChooseDialog.closeLoadingDialog(cityChooseDialog.loadingDialog);
                    }
                });
            }
        });
        tv_shi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置市级
                if( tv_sheng.getText().toString().length()!=0&shengc){
                    o=1;
                    cityChooseDialog=new CityChooseDialog();
                    cityChooseDialog.startLoadingDialog(AddNewAddress.this);
                    show_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            tv_shi.setText(list_shi.get(pos_sheng)[position]);
                            cityChooseDialog.closeLoadingDialog(cityChooseDialog.loadingDialog);
                        }
                    });
                }else MyToast.makeText(AddNewAddress.this,"请先设置上一级地址", Toast.LENGTH_SHORT).show();
            }
        });
        //设置学校
        tv_quxian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置学校
                o = 3;
                if (tv_sheng.getText().toString().length() != 0 & shengc) {
                    cityChooseDialog = new CityChooseDialog();
                    cityChooseDialog.startLoadingDialog(AddNewAddress.this);
                    show_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            tv_quxian.setText(sheng_school.get(pos_sheng)[position]);
                            cityChooseDialog.closeLoadingDialog(cityChooseDialog.loadingDialog);
                        }

                    });
                }else MyToast.makeText(AddNewAddress.this,"请先设置省级",Toast.LENGTH_SHORT).show();

            }
        });

        // MyToast.makeText(this,String.valueOf(i)+name+tel+des, Toast.LENGTH_SHORT).show();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {                // handler接收到消息后就会执行此方法
                if(msg.what==1){
                    MyToast.makeText(AddNewAddress.this,
                            "操作失败，网络又偷懒了，请稍后再试",Toast.LENGTH_SHORT).show();
                    loadingDialog.closeLoadingDialog(loadingDialog.loadingDialog);// 关闭ProgressDialog
                }else {
                    loadingDialog.closeLoadingDialog(loadingDialog.loadingDialog);// 关闭ProgressDialog
                    Intent intent=new Intent();
                    intent.putExtra("E","U");
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        };
        //提交数据
        btn_add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = et_name.getText().toString();
                tel = tv_tel.getText().toString();
                des = et_des.getText().toString();
                add_sheng=tv_sheng.getText().toString();
                add_shi=tv_shi.getText().toString();
                add_school=tv_quxian.getText().toString();
                if (name.length()==0| tel.length()!=11| des.length()==0|add_sheng.length()==0|add_school.length()==0|add_shi.length()==0)
                {
                    MyToast.makeText(AddNewAddress.this, "填写错误，请重新填写", Toast.LENGTH_SHORT).show();
                } else {
                    //提交网络数据
                    //开启progressDialog
                    loadingDialog = new LoadingDialog();
                    loadingDialog.startLoadingDialog(AddNewAddress.this, "加载中...");
                    new Thread() {
                        @Override
                        public void run() {
                            if (code.equals("0")) {
                                //修改数据
                                Address address = new Address();
                                address.setUsername(username);
                                address.setPersonname(name);
                                address.setTel(tel);
                                address.setSheng(add_sheng);
                                address.setShi(add_shi);
                                address.setSchool(add_school);
                                address.setAddressdes(des);
                                address.update(id, new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null) {
                                            handler.sendEmptyMessage(0);
                                            MyToast.makeText(AddNewAddress.this,
                                                    "修改成功", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            Message message=new Message();
                                            message.what=1;
                                            handler.sendMessage(message);
                                        }
                                    }
                                });
                            } else if (code.equals("1")) {
                                //新建数据
                                Address address = new Address();
                                address.setUsername(username);
                                address.setPersonname(name);
                                address.setTel(tel);
                                address.setSheng(add_sheng);
                                address.setShi(add_shi);
                                address.setSchool(add_school);
                                address.setAddressdes(des);
                                address.save(new SaveListener<String>() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if (e == null) {
                                            handler.sendEmptyMessage(0);
                                            MyToast.makeText(AddNewAddress.this, "新建成功", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            Message message=new Message();
                                            message.what=1;
                                            handler.sendMessage(message);  }
                                    }
                                });
                            }
                        }
                    }.start();
                }
            }
        });
    }

    public void initcity() {
        sheng = new String[]{"北京(直辖市)", "天津（直辖市）", "河北省", "山西省", "内蒙古自治区", "辽宁省", "吉林省", "黑龙江省", "上海（直辖市）", "江苏省", "浙江省",
                "安徽省", "福建省", "江西省", "山东省", "河南省", "湖北省", "湖南省", "广东省", "广西壮族自治区", "海南省", "重庆（直辖市）", "四川省", "贵州省",
                "云南省", "西藏自治区", "陕西省", "甘肃省", "青海省", "宁夏回族自治区", "新疆维吾尔族自治区", "台湾省", "香港特别行政区", "澳门特别行政区"};
        shi_beiji = new String[]{"东城区", "西城区", "崇文区", "宣武区", "朝阳区", "海淀区", "丰台区", "石景山区", "房山区",
                "通州区", "顺义区", "门头沟区", "昌平区", "大兴区", "怀柔区", "平谷区", "密云县", "延庆县"};
        shi_tianjin = new String[]{"和平区", "河东区", "河西区", "南开区", "河北区", "红桥区", "塘沽区", "汉沽区", "大港区", "东丽区",
                "西青区", "津南区", "北辰区", "武清区", "宝坻区", "宁河县", "静海县", "蓟县"};
        shi_hebei = new String[]{"石家庄市", "唐山市", "秦皇岛市", "邯郸市", "邢台市", "保定市", "张家口市", "承德市", "沧州市", "廊坊市", "衡水市"};
        shi_shanxi = new String[]{"太原市", "大同市", "阳泉市", "长治市", "晋城市", "朔州市", "晋中市", "运城市", "忻州市", "临汾市", "吕梁市"};
        shi_neimenggu = new String[]{"呼和浩特市", "包头市", "乌海市", "赤峰市", "通辽市", "鄂尔多斯市", "呼伦贝尔市",
                "巴彦淖尔市", "乌兰察布市", "兴安盟", "锡林郭勒盟", "阿拉善盟"};
        shi_liaoning = new String[]{"沈阳市", "大连市", "鞍山市", "抚顺市", "本溪市", "丹东市", "锦州市", "营口市",
                "阜新市", "辽阳市", "盘锦市", "铁岭市", "朝阳市", "葫芦岛市"};
        shi_jilin = new String[]{"长春市", "吉林市", "四平市", "辽源市", "通化市", "白山市"
                , "松原市", "白城市", "延边朝鲜族自治州"};
        shi_heilongjiang = new String[]{"哈尔滨市", "齐齐哈尔市", "鸡西市", "鹤岗市", "双鸭山市", "大庆市", "伊春市", "佳木斯市",
                "七台河市", "牡丹江市", "黑河市", "绥化市", "大兴安岭地区"};
        shi_shanghai = new String[]{"黄浦区", "卢湾区", "徐汇区", "长宁区", "静安区", "普陀区", "闸北区", "虹口区", "杨浦区", "宝山区", "闵行区",
                "嘉定区", "浦东新区", "金山区", "松江区", "青浦区", "南汇区", "奉贤区", "崇明县"};
        shi_jiangsu = new String[]{"南京市", "无锡市", "徐州市", "常州市", "苏州市", "南通市", "连云港市", "淮安市", "盐城市",
                "扬州市", "镇江市", "泰州市", "宿迁市"};
        shi_zhejiang = new String[]{"杭州市", "宁波市", "温州市", "嘉兴市", "湖州市", "绍兴市", "金华市", "衢州市", "舟山市", "台州市", "丽水市"};
        shi_anhui = new String[]{"合肥市", "芜湖市", "蚌埠市", "淮南市", "马鞍山市", "淮北市", "铜陵市", "安庆市", "黄山市", "滁州市", "阜阳市"
                , "宿州市", "巢湖市", "六安市", "亳州市", "池州市", "宣城市"};
        shi_fujian = new String[]{"福州市", "厦门市", "莆田市", "三明市", "泉州市", "漳州市", "南平市", "龙岩市", "宁德市"};
        shi_jiangxi = new String[]{"南昌市", "景德镇市", "萍乡市", "九江市", "新余市", "鹰潭市", "赣州市", "吉安市", "宜春市", "抚州市", "上饶市"};
        shi_shandong = new String[]{"济南市", "青岛市", "淄博市", "枣庄市", "东营市", "烟台市", "潍坊市", "威海市", "济宁市", "泰安市",
                "日照市", "莱芜市", "临沂市", "德州市", "聊城市", "滨州市", "菏泽市"};
        shi_henan = new String[]{"郑州市", "开封市", "洛阳市", "平顶山市", "安阳市", "鹤壁市", "新乡市", "焦作市", "濮阳市", "许昌市", "漯河市",
                "三门峡市", "南阳市", "商丘市", "信阳市", "周口市", "驻马店市", "济源市"};
        shi_hubei = new String[]{"武汉市", "黄石市", "襄樊市", "十堰市", "荆州市", "宜昌市", "荆门市", "鄂州市", "孝感市", "黄冈市", "咸宁市",
                "随州市", "恩施州", "仙桃市", "潜江市", "天门市", "神农架林区"};
        shi_hunan = new String[]{"长沙市", "株洲市", "湘潭市", "衡阳市", "邵阳市", "岳阳市", "常德市", "张家界市", "益阳市"
                , "郴州市", "永州市", "怀化市", "娄底市", "湘西州"};
        shi_guangdong = new String[]{"广州市", "深圳市", "珠海市", "汕头市", "韶关市", "佛山市", "江门市", "湛江市", "茂名市", "肇庆市", "惠州市",
                "梅州市", "汕尾市", "河源市", "阳江市", "清远市", "东莞市", "中山市", "潮州市", "揭阳市", "云浮市"};
        shi_guangxi = new String[]{"南宁市", "柳州市", "桂林市", "梧州市", "北海市", "防城港市", "钦州市", "贵港市", "玉林市", "百色市", "贺州市", "河池市", "来宾市", "崇左市"};
        shi_hainan = new String[]{"海口市", "龙华区", "秀英区", "琼山区", "美兰区", "三亚市"};
        shi_chongqing = new String[]{"渝中区", "大渡口区", "江北区", "沙坪坝区", "九龙坡区", "南岸区", "北碚区", "万盛区", "双桥区", "渝北区", "巴南区", "万县区", "涪陵区",
                "永川市", "合川市", "江津市", "南川市", "长寿县", "綦江县", "潼南县", "荣昌县", "壁山县", "大足县", "铜梁县", "梁平县", "城口县", "垫江县", "武隆县",
                "丰都县", "忠 县", "开 县", "云阳县", "青龙镇青龙嘴", "奉节县", "巫山县", "巫溪县", "南宾镇", "中和镇", "钟多镇", "联合镇", "汉葭镇"};
        shi_sichuan = new String[]{"成都市", "自贡市", "攀枝花市", "泸州市", "德阳市", "绵阳市", "广元市", "遂宁市", "内江市", "乐山市", "南充市", "宜宾市", "广安市", "达州市"
                , "眉山市", "雅安市", "巴中市", "资阳市", "阿坝州", "甘孜州", "凉山州"};
        shi_guizhou = new String[]{"贵阳市", "六盘水市", "遵义市", "安顺市", "铜仁地区", "毕节地区", "黔西南州", "黔东南州", "黔南州"};
        shi_yunnan = new String[]{"昆明市", "大理市", "曲靖市", "玉溪市", "昭通市", "楚雄市", "红河市", "文山市", "思茅市", "西双版纳市",
                "保山市", "德宏市", "丽江市", "怒江市", "迪庆市", "临沧市"};
        shi_xizang = new String[]{"拉萨市", "昌都地区", "山南地区", "日喀则地区", "那曲地区", "阿里地区", "林芝地区"};
        shi_shanxi1 = new String[]{"西安市", "铜川市", "宝鸡市", "咸阳市", "渭南市", "延安市", "汉中市", "榆林市", "安康市", "商洛市"};
        shi_gansu = new String[]{"兰州市", "嘉峪关市", "金昌市", "白银市", "天水市", "武威市", "张掖市", "平凉市", "酒泉市", "庆阳市"
                , "定西市", "陇南市", "临夏州", "甘南州"};
        shi_qinghai = new String[]{"西宁市", "海东地区", "海北州", "黄南州", "海南州", "果洛州", "玉树州", "海西州"};
        shi_ningxia = new String[]{"银川市", "石嘴山市", "吴忠市", "固原市", "中卫市"};
        shi_xinjiang = new String[]{"乌鲁木齐市", "克拉玛依市", "吐鲁番地区", "哈密地区", "和田地区", "阿克苏地区", "喀什地区", "克孜勒苏柯尔克孜自治州",
                "巴音郭楞蒙古自治州", "昌吉回族自治州", "博尔塔拉蒙古自治州", "伊犁哈萨克自治州", "塔城地区", "阿勒泰地区", "石河子市", "阿拉尔市", "图木舒克市", "五家渠市"};
        shi_taiwan = new String[]{"台北市", "高雄市", "基隆市", "台中市", "台南市", "新竹市", "嘉义市", "台北县", "宜兰县", "桃园县", "新竹县", "苗栗县", "台中县", "彰化县", "南投县",
                "云林县", "嘉义县", "台南县", "高雄县", "屏东县", "澎湖县", "台东县", "花莲县"};
        shi_xianggan = new String[]{"中西区", "东区", "九龙城区", "观塘区", "南区", "深水埗区", "黄大仙区", "湾仔区", "油尖旺区", "离岛区", "葵青区", "北区", "西贡区", "沙田区", "屯门区"
                , "大埔区", "荃湾区", "元朗区"};
        shi_aomen = new String[]{"澳门地区"};
        list_shi = new ArrayList<>();
        list_shi.add(shi_beiji);
        list_shi.add(shi_tianjin);
        list_shi.add(shi_hebei);
        list_shi.add(shi_shanxi);
        list_shi.add(shi_neimenggu);
        list_shi.add(shi_liaoning);
        list_shi.add(shi_jilin);
        list_shi.add(shi_heilongjiang);
        list_shi.add(shi_shanghai);
        list_shi.add(shi_jiangsu);
        list_shi.add(shi_zhejiang);
        list_shi.add(shi_anhui);
        list_shi.add(shi_fujian);
        list_shi.add(shi_jiangxi);
        list_shi.add(shi_shandong);
        list_shi.add(shi_henan);
        list_shi.add(shi_hunan);
        list_shi.add(shi_guangdong);
        list_shi.add(shi_guangxi);
        list_shi.add(shi_hubei);
        list_shi.add(shi_hainan);
        list_shi.add(shi_chongqing);
        list_shi.add(shi_sichuan);
        list_shi.add(shi_guizhou);
        list_shi.add(shi_yunnan);
        list_shi.add(shi_xizang);
        list_shi.add(shi_shanxi1);
        list_shi.add(shi_gansu);
        list_shi.add(shi_qinghai);
        list_shi.add(shi_ningxia);
        list_shi.add(shi_xinjiang);
        list_shi.add(shi_taiwan);
        list_shi.add(shi_xianggan);
        list_shi.add(shi_aomen);
        beijing_school=new String[]{ "北京大学", "北方工业大学", "北京城市学院", "北京第二外国语学院", "北京电影学院", "北京电子科技学院", "北京服装学院", "北京工商大学", "北京工业大学", "北京航空航天大学",
                "北京化工大学", "北京吉利学院","北京建筑大学", "北京交通大学", "北京警察学院", "北京科技大学", "北京理工大学", "北京联合大学", "北京林业大学", "北京农学院", "北京师范大学", "北京石油化工学院",
                "北京体育大学", "北京外国语大学", "北京舞蹈学院", "北京物资学院", "北京协和医学院", "北京信息科技大学", "北京印刷学院", "北京邮电大学", "北京语言大学", "北京中医药大学", "对外经济贸易大学",
                "国际关系学院", "华北电力大学", "清华大学", "首都经济贸易大学", "首都师范大学", "首都体育学院", "首都医科大学", "首钢工学院", "外交学院", "中国传媒大学", "中国地质大学(北京)", "中国科学院大学",
                "中国矿业大学(北京)", "中国劳动关系学院", "中国农业大学", "中国青年政治学院", "中国人民大学", "中国人民公安大学", "中国石油大学(北京)", "中国戏曲学院", "中国音乐学院", "中国政法大学",
                "中华女子学院", "中央财经大学", "中央美术学院", "中央民族大学", "中央戏剧学院", "中央音乐学院"};
        tianjin_school=new String[]{ "天津财经大学", "天津城建大学", "天津大学", "天津工业大学", "天津科技大学", "天津理工大学", "天津美术学院","天津农学院", "天津商业大学", "天津师范大学", "天津体育学院",
                "天津天狮学院", "天津外国语大学", "天津医科大学", "天津音乐学院", "天津职业技术师范大学", "天津中医药大学", "中国民航大学", "南开大学"};
        hebei_school=new String[]{"保定学院", "北华航天工业学院", "沧州师范学院", "承德医学院", "防灾科技学院", "邯郸学院", "河北北方学院", "河北传媒学院", "河北工程大学", "河北工程技术学院","河北工业大学",
                "河北建筑工程学院", "河北金融学院", "河北经贸大学", "河北科技大学", "河北科技师范学院", "河北科技学院", "河北美术学院", "河北民族师范学院", "河北农业大学", "河北师范大学", "河北体育学院",
                "河北外国语学院", "河北医科大学", "河北中医学院", "衡水学院", "华北科技学院", "华北理工大学", "廊坊师范学院", "石家庄经济学院","石家庄铁道大学", "石家庄学院", "唐山师范学院", "唐山学院",
                "邢台学院", "燕京理工学院", "燕山大学", "张家口学院", "中国人民武装警察部队学院", "中央司法警官学院", "河北大学"};
        shanxi_school=new String []{  "晋中学院", "吕梁学院", "山西财经大学", "山西传媒学院", "山西大同大学", "山西工程技术学院", "山西工商学院", "山西农业大学", "山西师范大学", "山西医科大学",
                "山西应用科技学院","山西中医学院", "太原工业学院", "太原科技大学", "太原理工大学", "太原师范学院", "太原学院", "忻州师范学院", "运城学院", "长治学院", "长治医学院", "中北大学", "山西大学"};
        neimenggu_school=new String[]{"赤峰学院", "鄂尔多斯应用技术学院","河套学院", "呼和浩特民族学院", "呼伦贝尔学院", "集宁师范学院", "内蒙古财经大学",
                "内蒙古工业大学", "内蒙古科技大学", "内蒙古民族大学", "内蒙古农业大学", "内蒙古师范大学", "内蒙古医科大学", "内蒙古艺术学院", "内蒙古大学"};
        liaoning_school=new String[]{ "鞍山师范学院", "渤海大学", "大连理工大学", "大连财经学院", "大连大学", "大连东软信息学院", "大连工业大学", "大连海事大学", "大连海洋大学", "大连交通大学", "大连科技学院",
                "大连民族大学", "大连外国语大学", "大连医科大学", "大连艺术学院", "东北财经大学", "东北大学", "辽东学院", "辽宁财贸学院", "辽宁传媒学院", "辽宁大学", "辽宁对外经贸学院", "辽宁工程技术大学",
                "辽宁工业大学", "辽宁何氏医学院", "辽宁警察学院", "辽宁科技大学", "辽宁科技学院", "辽宁理工学院","辽宁师范大学", "辽宁石油化工大学", "辽宁医学院", "辽宁中医药大学",
                "鲁迅美术学院", "沈阳城市建设学院", "沈阳城市学院", "沈阳大学", "沈阳工程学院", "沈阳工学院",
                "沈阳工业大学", "沈阳航空航天大学", "沈阳化工大学", "沈阳建筑大学", "沈阳理工大学", "沈阳农业大学", "沈阳师范大学", "沈阳体育学院",
                "沈阳药科大学", "沈阳医学院", "沈阳音乐学院", "营口理工学院", "中国刑事警察学院", "中国医科大学"};
        jilin_school=new String[]{ "白城师范学院", "北华大学", "东北电力大学", "东北师范大学", "吉林财经大学", "吉林大学", "吉林动画学院", "吉林工程技术师范学院", "吉林工商学院", "吉林华桥外国语学院",
                "吉林化工学院", "吉林建筑大学", "吉林警察学院", "吉林农业大学", "吉林农业科技学院", "吉林师范大学", "吉林体育学院", "吉林医药学院", "吉林艺术学院", "通化师范学院", "延边大学", "长春财经学院",
                "长春大学", "长春工程学院", "长春工业大学", "长春光华学院", "长春建筑学院", "长春科技学院", "长春理工大学", "长春师范大学", "长春中医药大学"};
        heilongjiang_school=new String[]{"大庆师范学院", "东北林业大学", "东北农业大学", "东北石油大学", "哈尔滨工程大学", "哈尔滨工业大学", "哈尔滨广厦学院", "哈尔滨华德学院", "哈尔滨剑桥学院",
                "哈尔滨金融学院", "哈尔滨理工大学", "哈尔滨商业大学", "哈尔滨师范大学", "哈尔滨石油学院", "哈尔滨体育学院", "哈尔滨信息工程学院", "哈尔滨学院", "哈尔滨医科大学", "哈尔滨远东理工学院",
                "黑河学院", "黑龙江八一农垦大学", "黑龙江财经学院", "黑龙江大学", "黑龙江东方学院", "黑龙江工程学院", "黑龙江工商学院","黑龙江工业学院", "黑龙江科技大学", "黑龙江外国语学院", "黑龙江中医药大学",
                "佳木斯大学", "牡丹江师范学院", "牡丹江医学院", "齐齐哈尔大学", "齐齐哈尔工程学院", "齐齐哈尔医学院", "绥化学院",};
        shanghai_school=new String[]{"复旦大学", "同济大学", "上海交通大学", "华东理工大学", "东华大学", "华东师范大学", "上海外国语大学", "上海财经大学", "上海大学",
                "上海理工大学", "上海海事大学", "上海工程技术大学", "上海海洋大学", "上海中医药大学", "上海师范大学", "华东政法大学", "上海海关学院", "上海建桥学院",
                "上海政法学院", "上海应用技术学院", "上海第二工业大学", "上海电机学院", "上海电力学院", "上海对外经贸大学", "上海金融学院", "上海立信会计学院",
                "上海体育学院", "上海音乐学院", "上海戏剧学院", "上海商学院", "上海杉达学院", "上海视觉艺术学院", "上海纽约大学", "上海科技大学", "上海兴伟学院", "上海健康医学院"};
        jiangsu_school=new String[]{"常熟理工学院", "常州大学", "常州工学院", "东南大学", "河海大学", "淮海工学院", "淮阴工学院", "淮阴师范学院", "江南大学", "江苏大学", "江苏第二师范学院", "江苏警官学院",
                "江苏科技大学", "江苏理工学院", "江苏师范大学", "金陵科技学院", "昆山杜克大学", "南京大学", "南京财经大学", "南京工程学院", "南京工业大学", "南京航空航天大学",
                "南京理工大学", "南京林业大学", "南京农业大学", "南京森林警察学院", "南京审计学院", "南京师范大学", "南京特殊教育师范学院","南京体育学院", "南京晓庄学院", "南京信息工程大学", "南京医科大学", "南京艺术学院",
                "南京邮电大学", "南京中医药大学", "南通大学", "南通理工学院", "三江学院", "苏州大学", "苏州科技学院", "泰州学院", "无锡太湖学院", "西交利物浦大学", "宿迁学院",
                "徐州工程学院", "徐州医学院", "盐城工学院", "盐城师范学院", "扬州大学", "中国矿业大学", "中国药科大学"};
        zhejiang_school=new String[]{"公安海警学院", "杭州电子科技大学", "杭州师范大学", "湖州师范学院", "嘉兴学院", "丽水学院", "宁波大红鹰学院", "宁波大学", "宁波工程学院"
                ,"宁波诺丁汉大学", "衢州学院", "绍兴文理学院", "台州学院", "温州大学", "温州肯恩大学", "温州医科大学",
                "浙江财经大学", "浙江传媒学院", "浙江大学", "浙江工商大学", "浙江工业大学", "浙江海洋学院", "浙江警察学院", "浙江科技学院", "浙江理工大学", "浙江农林大学",
                "浙江师范大学", "浙江树人学院", "浙江水利水电学院", "浙江外国语学院", "浙江万里学院", "浙江音乐学院(筹)","浙江越秀外国语学院", "浙江中医药大学", "中国计量学院", "中国美术学院"};
        anhui_school=new String[]{"安徽财经大学", "安徽大学", "安徽工程大学", "安徽工业大学", "安徽建筑大学", "安徽科技学院", "安徽理工大学", "安徽农业大学", "安徽三联学院",
                "安徽师范大学", "安徽外国语学院", "安徽文达信息工程学院","安徽新华学院", "安徽医科大学", "安徽中医药大学", "安庆师范学院", "蚌埠学院", "蚌埠医学院", "巢湖学院",
                "池州学院", "滁州学院", "阜阳师范学院", "合肥工业大学", "合肥师范学院", "合肥学院", "淮北师范大学", "淮南师范学院", "黄山学院", "铜陵学院",
                "皖南医学院", "皖西学院", "宿州学院", "中国科学技术大学"};
        fujian_school=new String[]{ "福建工程学院", "福建江夏学院", "福建警察学院", "福建农林大学", "福建师范大学", "福建医科大学", "福建中医药大学", "福州大学", "福州理工学院", "福州外语外贸学院",
                "华侨大学", "集美大学", "龙岩学院", "闽江学院", "闽南理工学院", "闽南师范大学", "宁德师范学院", "莆田学院", "泉州师范学院", "泉州信息工程学院", "三明学院", "厦门大学", "厦门工学院",
                "厦门华厦学院", "厦门理工学院", "武夷学院", "阳光学院", "仰恩大学"};
        jiangxi_school=new String[]{"东华理工大学","赣南师范学院", "赣南医学院", "华东交通大学", "江西财经大学", "江西服装学院", "江西工程学院",
                "江西警察学院", "江西科技师范大学", "江西科技学院", "江西理工大学", "江西农业大学", "江西师范大学", "江西应用科技学院","江西中医药大学", "井冈山大学", "景德镇陶瓷学院",
                "景德镇学院", "九江学院", "南昌大学", "南昌工程学院", "南昌工学院", "南昌航空大学", "南昌理工学院", "南昌师范学院",
                "萍乡学院", "上饶师范学院", "新余学院", "宜春学院"};
        shandong_school=new String[]{ "滨州学院", "滨州医学院", "德州学院", "菏泽学院", "济南大学", "济宁学院", "济宁医学院", "聊城大学", "临沂大学", "鲁东大学", "齐鲁工业大学", "齐鲁理工学院",
                "齐鲁师范学院", "齐鲁医药学院", "青岛滨海学院", "青岛大学", "青岛工学院", "青岛恒星科技学院", "青岛黄海学院", "青岛科技大学", "青岛理工大学", "青岛农业大学", "曲阜师范大学", "山东财经大学", "山东大学",
                "山东工商学院", "山东工艺美术学院", "山东管理学院", "山东华宇工学院", "山东建筑大学", "山东交通学院", "山东警察学院", "山东科技大学", "山东理工大学", "山东农业大学",
                "山东农业工程学院", "山东女子学院", "山东青年政治学院", "山东师范大学", "山东体育学院", "山东现代学院","山东协和学院", "山东艺术学院", "山东英才学院",
                "山东政法学院", "山东中医药大学", "泰山学院", "泰山医学院", "潍坊科技学院", "潍坊学院", "潍坊医学院", "烟台大学", "烟台南山学院", "枣庄学院", "中国海洋大学", "中国石油大学(华东)"};
        henan_school=new String[]{"安阳工学院", "安阳师范学院", "河南财经政法大学", "河南城建学院", "河南大学", "河南工程学院", "河南工业大学",
                "河南警察学院", "河南科技大学", "河南科技学院", "河南理工大学",
                "河南牧业经济学院", "河南农业大学", "河南师范大学", "河南中医学院", "华北水利水电大学", "黄河交通学院", "黄河科技学院",
                "黄淮学院", "洛阳理工学院", "洛阳师范学院", "南阳理工学院", "南阳师范学院", "平顶山学院", "商丘工学院", "商丘师范学院", "商丘学院", "铁道警察学院", "新乡学院",
                "新乡医学院", "信阳农林学院", "信阳师范学院", "许昌学院", "郑州财经学院","郑州成功财经学院", "郑州大学", "郑州工业应用技术学院", "郑州航空工业管理学院",
                "郑州科技学院", "郑州轻工业学院", "郑州升达经贸管理学院", "郑州师范学院", "中原工学院", "周口师范学院"};
        hunan_school=new String[]{"国防科学技术大学", "衡阳师范学院", "湖南财政经济学院", "湖南城市学院", "湖南大学", "湖南第一师范学院", "湖南工程学院", "湖南工学院", "湖南工业大学", "湖南交通工程学院",
                "湖南警察学院", "湖南科技大学", "湖南科技学院", "湖南理工学院", "湖南农业大学", "湖南女子学院", "湖南人文科技学院", "湖南商学院", "湖南涉外经济学院", "湖南师范大学", "湖南文理学院",
                "湖南信息学院", "湖南医药学院", "湖南应用技术学院", "湖南中医药大学", "怀化学院", "吉首大学", "南华大学", "邵阳学院", "湘南学院",
                "湘潭大学", "长沙理工大学", "长沙师范学院", "长沙学院", "长沙医学院", "中南大学", "中南林业科技大学"};
        guangdong_school=new String[]{"北京师范大学-香港浸会大学联合国际学院", "东莞理工学院", "佛山科学技术学院", "广东白云学院", "广东财经大学", "广东第二师范学院", "广东东软学院", "广东工业大学", "广东海洋大学",
                "广东技术师范学院", "广东金融学院", "广东警官学院", "广东科技学院", "广东理工学院", "广东培正学院", "广东石油化工学院", "广东外语外贸大学", "广东药学院", "广东医学院", "广州大学",
                "广州工商学院", "广州航海学院", "广州美术学院", "广州商学院", "广州体育学院", "广州医科大学", "广州中医药大学", "韩山师范学院", "华南理工大学", "华南农业大学", "华南师范大学", "惠州学院",
                "暨南大学", "嘉应学院", "岭南师范学院", "南方科技大学", "南方医科大学", "汕头大学", "韶关学院", "深圳大学", "五邑大学", "香港中文大学(深圳)", "星海音乐学院", "肇庆学院", "中山大学", "仲恺农业工程学院"};
        guangxi_school=new String[]{"广西大学", "桂林电子科技大学", "广西医科大学", "广西师范大学", "广西民族大学", "桂林理工大学", "广西中医药大学", "广西科技大学",
                "桂林医学院", "右江民族医学院", "广西师范学院", "桂林航天工业学院", "玉林师范学院", "广西财经学院", "广西艺术学院", "广西民族师范学院", "广西外国语学院", "百色学院", "河池学院",
                "钦州学院", "贺州学院", "梧州学院", "南宁学院", "北海艺术设计学院", "广西警察学院", "桂林旅游学院", "广西科技师范学院"};
        hubei_school=new String[]{"武汉大学", "华中科技大学", "中南财经政法大学", "武汉理工大学", "中国地质大学(武汉)", "华中农业大学", "华中师范大学", "中南民族大学",
                "湖北大学", "长江大学", "三峡大学", "江汉大学", "武汉科技大学", "湖北工业大学", "武汉工程大学", "武汉纺织大学", "湖北中医药大学", "武汉轻工大学", "湖北汽车工业学院", "湖北医药学院",
                "湖北工程学院", "黄冈师范学院", "湖北师范学院", "湖北经济学院", "湖北警官学院", "武汉体育学院", "湖北美术学院", "武汉音乐学院", "湖北民族学院", "湖北文理学院",
                "湖北理工学院", "湖北科技学院", "荆楚理工学院", "湖北第二师范学院", "武汉生物工程学院", "武汉东湖学院", "武汉工商学院", "汉口学院", "武昌理工学院", "武昌工学院", "武汉商学院", "武汉工程科技学院",
                "文华学院", "武汉设计工程学院", "湖北商贸学院", "武汉学院", "武昌首义学院"};
        hainan_school=new String[]{"海口经济学院","海南大学","海南热带海洋学院","海南师范大学","海南医学院", "三亚学院"};
        chongqing_school=new String[]{"重庆大学", "西南大学", "重庆交通大学", "重庆邮电大学", "重庆医科大学", "重庆师范大学", "重庆理工大学", "重庆工商大学", "西南政法大学", "重庆科技学院", "长江师范学院",
                "重庆第二师范学院", "重庆警察学院", "四川外国语大学", "四川美术学院", "重庆三峡学院", "重庆文理学院", "重庆人文科技学院", "重庆工程学院"};
        sichuan_school=new String[]{"阿坝师范学院", "成都东软学院", "成都工业学院", "成都理工大学", "成都师范学院", "成都体育学院", "成都文理学院", "成都信息工程大学", "成都学院", "成都医学院", "成都中医药大学",
                "川北医学院", "电子科技大学", "乐山师范学院", "绵阳师范学院", "内江师范学院", "攀枝花学院", "四川传媒学院", "四川大学", "四川电影电视学院", "四川工商学院",
                "四川工业科技学院", "四川警察学院", "四川理工学院", "四川旅游学院", "四川民族学院", "四川农业大学", "四川师范大学", "四川文化艺术学院", "四川文理学院", "四川医科大学", "四川音乐学院", "西昌学院", "西华大学",
                "西华师范大学", "西南财经大学", "西南交通大学", "西南科技大学", "西南民族大学", "西南石油大学", "宜宾学院", "中国民用航空飞行学院"};
        guizhou_school=new String[]{"安顺学院", "贵阳学院", "贵阳中医学院", "贵州财经大学", "贵州大学", "贵州工程应用技术学院", "贵州理工学院", "贵州民族大学", "贵州商学院", "贵州师范大学", "贵州师范学院", "贵州医科大学",
                "凯里学院", "六盘水师范学院", "黔南民族师范学院", "铜仁学院", "兴义民族师范学院", "遵义师范学院", "遵义医学院"};
        yunnan_school=new String[]{"保山学院", "楚雄师范学院", "大理大学", "滇西科技师范学院","红河学院", "昆明理工大学", "昆明学院", "昆明医科大学", "普洱学院", "曲靖师范学院", "文山学院",
                "西南林业大学", "玉溪师范学院", "云南财经大学", "云南大学", "云南工商学院", "云南经济管理学院", "云南警官学院", "云南民族大学", "云南农业大学", "云南师范大学", "云南艺术学院", "云南中医学院", "昭通学院"};
        xizang_school=new String[]{"西藏藏医学院", "西藏大学", "西藏民族大学"};
        shanxi_school=new String[]{"安康学院", "宝鸡文理学院", "陕西服装工程学院", "陕西国际商贸学院", "陕西科技大学", "陕西理工学院", "陕西师范大学", "陕西学前师范学院", "陕西中医药大学", "商洛学院", "渭南师范学院",
                "西安财经学院", "西安电子科技大学", "西安翻译学院", "西安工程大学", "西安工业大学", "西安航空学院", "西安建筑科技大学", "西安交通大学", "西安交通工程学院",
                "西安科技大学", "西安理工大学", "西安美术学院", "西安欧亚学院", "西安培华学院", "西安石油大学", "西安思源学院", "西安体育学院", "西安外国语大学", "西安外事学院", "西安文理学院", "西安医学院",
                "西安音乐学院", "西安邮电大学", "西北大学", "西北工业大学", "西北农林科技大学", "西北政法大学", "西京学院", "咸阳师范学院", "延安大学", "榆林学院", "长安大学"};
        gansu_school=new String[]{"甘肃民族师范学院", "甘肃农业大学", "甘肃医学院","甘肃政法学院", "甘肃中医药大学", "河西学院", "兰州财经大学", "兰州城市学院", "兰州大学", "兰州工业学院", "兰州交通大学",
                "兰州理工大学", "兰州文理学院", "陇东学院", "天水师范学院", "西北民族大学", "西北师范大学"};
        qinghai_school=new String[]{"青海大学", "青海民族大学","青海师范大学"};
        ningxia_school=new String[]{"北方民族大学", "宁夏大学", "宁夏理工学院", "宁夏师范学院", "宁夏医科大学", "银川能源学院"};
        xinjiang_school=new String[]{"昌吉学院", "喀什大学", "石河子大学", "塔里木大学", "新疆财经大学", "新疆大学", "新疆工程学院", "新疆警察学院","新疆农业大学", "新疆师范大学", "新疆医科大学",
                "新疆艺术学院", "伊犁师范学院"};
        taiwan_school=new String[]{"台湾中央大学", "台湾清华大学", "交通大学", "台湾大学", "嘉义大学", "元智大学"};
        xianggan_school=new String[]{"东华学院", "恒生管理学院","明爱专上学院", "明德学院", "香港城市大学", "香港大学", "香港高等科技教育学院", "香港公开大学", "香港教育学院",
                "香港浸会大学", "香港科技大学", "香港理工大学", "香港岭南大学", "香港树仁大学", "香港演艺学院", "香港中文大学", "珠海学院"};
        aomen_school=new String[]{"澳门城市大学", "澳门大学", "澳门镜湖护理学院", "澳门科技大学", "澳门理工学院", "澳门旅游学院"};
        sheng_school=new ArrayList<>();
        sheng_school.add(beijing_school);
        sheng_school.add(tianjin_school);
        sheng_school.add(hebei_school);
        sheng_school.add(shanxi_school);
        sheng_school.add(neimenggu_school);
        sheng_school.add(liaoning_school);
        sheng_school.add(jilin_school);
        sheng_school.add(heilongjiang_school);
        sheng_school.add(shanghai_school);
        sheng_school.add(jiangsu_school);
        sheng_school.add(zhejiang_school);
        sheng_school.add(anhui_school);
        sheng_school.add(fujian_school);
        sheng_school.add(jiangxi_school);
        sheng_school.add(shandong_school);
        sheng_school.add(henan_school);
        sheng_school.add(hubei_school);
        sheng_school.add(hunan_school);
        sheng_school.add(guangdong_school);
        sheng_school.add(guangxi_school);
        sheng_school.add(hainan_school);
        sheng_school.add(chongqing_school);
        sheng_school.add(sichuan_school);
        sheng_school.add(guizhou_school);
        sheng_school.add(yunnan_school);
        sheng_school.add(xizang_school);
        sheng_school.add(shanxi_school);
        sheng_school.add(gansu_school);
        sheng_school.add(qinghai_school);
        sheng_school.add(ningxia_school);
        sheng_school.add(xinjiang_school);
        sheng_school.add(taiwan_school);
        sheng_school.add(xianggan_school);
        sheng_school.add(aomen_school);
    }
    //    }
    public class CityChooseDialog {
        public Dialog loadingDialog;
        public  Dialog startLoadingDialog(Context context) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(R.layout.address_citychoose, null);// 得到加载view
            LinearLayout layout = (LinearLayout) v.findViewById(R.id.jjj);// 加载布局
            show_city= (ListView) v.findViewById(R.id.lv_show_city);
            loadingDialog = new Dialog(context);// 创建自定义样式dialog
            loadingDialog.setCancelable(false); // 是否可以按“返回键”消失
            loadingDialog.setCanceledOnTouchOutside(false); // 点击加载框以外的区域
            loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
            show_city.setAdapter(mycity);
            /**
             *将显示Dialog的方法封装在这里面
             */
            Window window = loadingDialog.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setGravity(Gravity.CENTER);
            window.setAttributes(lp);
            loadingDialog.show();
            return loadingDialog;
        }
        public  void closeLoadingDialog(Dialog mDialogUtils) {
            if (mDialogUtils != null && mDialogUtils.isShowing()) {
                mDialogUtils.dismiss();
            }
        }
    }
    class Mycity extends BaseAdapter {
        @Override
        public int getCount() {
            if(o==0){
                return sheng.length;
            }else if(o==1) {
                return list_shi.get(pos_sheng).length;
            }else {
                return sheng_school.get(pos_sheng).length;
            }
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view=View.inflate(AddNewAddress.this,R.layout.item_city_list,null);
            tv_show= (TextView) view.findViewById(R.id.tv_item);
            if(o==0){
                item=new String[]{};
                item=sheng;
            }else if(o==1){
                item=new String[]{};
                item=list_shi.get(pos_sheng);
            }else {
                item=sheng_school.get(pos_sheng);
                //o==3县区级
            }
            tv_show.setText(item[position]);
            return view;
        }
    }

}

