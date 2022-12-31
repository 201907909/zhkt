package com.wisdom.base.util;

import com.baidu.aip.contentcensor.AipContentCensor;
import com.baidu.aip.contentcensor.EImgType;
import org.json.JSONObject;

import java.util.HashMap;

public class Sample {

    public static Integer sampleImage(AipContentCensor client, String img) {
        if(img != null && !img.equals("")) {
            // 参数为本地图片路径
            String url = img;

            System.out.println(url);

            HashMap<String, String> options = new HashMap<>();
            options.put("Content-Type", "application/x-www-form-urlencoded");

            JSONObject response = client.imageCensorUserDefined(url, EImgType.URL, options);

            System.out.println(response.toString());

            int conclusionType = response.getInt("conclusionType");

            return conclusionType;
        }else {
            return 1;
        }
    }

    public static Integer sampleText(AipContentCensor client, String text) {

        if(text != null && text != "") {

            JSONObject response = client.textCensorUserDefined(text);

            System.out.println(response.toString());


            int conclusionType = response.getInt("conclusionType");

            return conclusionType;

        }else {
            return 1;
        }
    }
}
