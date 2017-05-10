package com.shuwang.cloud.util;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class GsonUtils {
    public static Gson getGson(){
        Gson gson = new GsonBuilder().registerTypeAdapter(Map.class, new JsonDeserializer<TreeMap<String, Object>>() {
            public TreeMap<String, Object> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                    throws JsonParseException{
            	TreeMap<String, Object> resultMap = new TreeMap<String, Object>();
                JsonObject jsonObject = json.getAsJsonObject();
                Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
                for (Map.Entry<String, JsonElement> entry : entrySet) {
                	JsonElement value_element = entry.getValue();
                	if (value_element.isJsonPrimitive()) {
                		JsonPrimitive value_primitive = value_element.getAsJsonPrimitive();
                		if (value_primitive.isString()) {
                			resultMap.put(entry.getKey(), value_primitive.getAsString());
                		} else if (value_primitive.isNumber()) {
                			Double value_double = value_primitive.getAsDouble();
                			if (value_double == value_double.longValue()) {
                				resultMap.put(entry.getKey(), value_double.longValue());
                			} else {
                				resultMap.put(entry.getKey(), value_double);
                			}
                		} else {
                			resultMap.put(entry.getKey(), value_element);
                		}
                	} else {
                		resultMap.put(entry.getKey(), value_element);
                	}
                }
                return resultMap;
            }
        }).create();
        return gson;
    }
}
