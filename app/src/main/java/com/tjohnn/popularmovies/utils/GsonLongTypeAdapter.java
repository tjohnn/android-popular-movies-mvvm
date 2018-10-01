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

public class GsonLongTypeAdapter implements JsonDeserializer<Long>
{
    public Long deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        try {
            String val = json.getAsString();
            if(val == null || val.isEmpty() ) return 0L;
            return json.getAsLong();
        }catch (Exception ex){
            return  0L;
        }
    }


}
