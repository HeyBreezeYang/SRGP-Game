package test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gmdesign.bean.other.GmHashMap;
import com.gmdesign.util.DateUtil;

/**
 * Created by DJL on 2017/7/8.
 *
 * @ClassName GM
 * @Description
 */
public class TestClient {

    public static void main(String[] args) {
        String msg="{\"ret\":[{\"qq\":\"12\",\"ff\":\"0\",\"owner\":\"\",\"vx\":\"1231231\",\"max\":13,\"lv\":1,\"needReq\":false,\"guildId\":2,\"members\":[{\"ff\":480,\"img\":10032501,\"logout\":1524559713,\"dCid\":0,\"hisCoin\":\"0\",\"pid\":328,\"lv\":1,\"join\":1524557486,\"right\":101,\"login\":1524557596,\"nm\":\"\"},{\"ff\":480,\"img\":10032501,\"logout\":1524560226,\"dCid\":0,\"hisCoin\":\"0\",\"pid\":330,\"lv\":1,\"join\":1524557987,\"right\":101,\"login\":1524558094,\"nm\":\"\"},{\"ff\":0,\"img\":10032501,\"logout\":1524478891,\"dCid\":0,\"hisCoin\":\"0\",\"pid\":316,\"lv\":1,\"join\":1524476277,\"right\":104,\"login\":1524476593,\"nm\":\"绗ㄩ噸鐨勭媱鏇存柉\"},{\"ff\":6760,\"img\":10032501,\"logout\":1524575379,\"dCid\":0,\"hisCoin\":\"0\",\"pid\":332,\"lv\":7,\"join\":1524558363,\"right\":101,\"login\":1524573656,\"nm\":\"鍗曠嫭\"}],\"mny\":0,\"memSize\":4,\"nm\":\"\",\"desc\":\"\"}],\"code\":0}";
        GmHashMap res= JSON.parseObject(msg,GmHashMap.class) ;
        List<JSONObject> list= (List<JSONObject>) res.get("ret");
        List<JSONObject>members= (List<JSONObject>) list.get(0).get("members");
        List<GmHashMap> resList=new ArrayList<>();
        for(JSONObject mem:members){
            GmHashMap map=new GmHashMap();
            map.putAll(mem);
            map.put("join",DateUtil.formatDateTime((Integer) map.get("join")*1000L));
            map.put("login",DateUtil.formatDateTime((Integer) map.get("login")*1000L));
            map.put("logout",DateUtil.formatDateTime((Integer) map.get("logout")*1000L));
            resList.add(map);
        }
        System.out.println(Arrays.toString(resList.toArray()));
    }

    public static Object ByteToObject(byte[] bytes)
    {
        Object obj = null;
        try
        {
            ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
            ObjectInputStream oi = new ObjectInputStream(bi);
            obj = oi.readObject();
            bi.close();
            oi.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return obj;
    }

    private static byte[] ObjectToByte(Object obj)
    {
        byte[] bytes = null;
        try
        {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(obj);
            bytes = bo.toByteArray();
            bo.close();
            oo.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return bytes;
    }
}
