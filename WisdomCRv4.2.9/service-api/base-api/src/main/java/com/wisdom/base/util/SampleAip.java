package com.wisdom.base.util;

import com.baidu.aip.contentcensor.AipContentCensor;

public class SampleAip {
    //设置APPID/AK/SK
    public String APP_ID = "你的 App ID";
    public String API_KEY = "你的 Api Key";
    public String SECRET_KEY = "你的 Secret Key";

    public SampleAip(String APP_ID, String API_KEY, String SECRET_KEY) {
        this.APP_ID = APP_ID;
        this.API_KEY = API_KEY;
        this.SECRET_KEY = SECRET_KEY;
    }

    public AipContentCensor getAip() {
        return new AipContentCensor(this.APP_ID,  this.API_KEY, this.SECRET_KEY);
    }

}
