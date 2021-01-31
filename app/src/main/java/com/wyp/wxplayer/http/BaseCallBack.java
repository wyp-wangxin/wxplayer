package com.wyp.wxplayer.http;



import com.google.gson.internal.$Gson$Types;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by Ding on 2016/12/19.
 */
public abstract class BaseCallBack<T> {

    Type type;

    static Type getSuperclassTypeParameter(Class<?> subclass)
    {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class)
        {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }


    public BaseCallBack()
    {
        type = getSuperclassTypeParameter(getClass());
    }

    public abstract void onFailure(int code, Exception e);

    public abstract void onSuccess(T t) ;
}
