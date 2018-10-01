package com.tjohnn.popularmovies.utils;

/**
 * Created by Tjohn on 10/31/2017.
 *
 * To avoid exceptions when gson is expecting boolean and 1 or zero is gotten
 * This class is to handle the numbers (1 and 0) gotten and convert them to java understandable boolean values
 */
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class GsonBooleanTypeAdapter implements JsonDeserializer<Boolean>
{
    public Boolean deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        try {
            int code = json.getAsInt();
            return code != 0;
        }catch (Exception ex){
            return  Boolean.valueOf(String.valueOf(json));
        }
    }


}
