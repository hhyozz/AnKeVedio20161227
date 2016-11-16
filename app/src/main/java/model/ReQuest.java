package model;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/11/3.
 */

public class ReQuest extends BmobObject {
    private String Userid;
    private String Type;
    private String Name;

    public String getUserid() {
        return Userid;
    }

    public void setUserid(String userid) {
        Userid = userid;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
