package com.shuwang.cloud.util;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GsonUtils {
    public static Gson getGson(){
        Gson gson = new GsonBuilder().registerTypeAdapter(HashMap.class, new JsonDeserializer<HashMap<String, Object>>() {
            public HashMap<String, Object> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                    throws JsonParseException{
            	HashMap<String, Object> resultMap = new HashMap<String, Object>();
                JsonObject jsonObject = json.getAsJsonObject();
                Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
                for (Map.Entry<String, JsonElement> entry : entrySet) {
                    resultMap.put(entry.getKey(), entry.getValue());
                }
                return resultMap;
            }
        }).create();
        return gson;
    }
}
