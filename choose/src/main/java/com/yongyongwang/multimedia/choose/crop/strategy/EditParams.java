package com.yongyongwang.multimedia.choose.crop.strategy;

import com.yongyongwang.multimedia.choose.crop.exceptions.HandleStrategyException;

import java.lang.reflect.Field;

/**
 * Strategy parameter class
 *
 * @param <T>
 */
public class EditParams<T> {
    private T mParams;

    public EditParams(T options) {
        mParams = options;
    }

    /**
     * Get Property
     *
     * @param cls  Class Instance
     * @param name Strategy Name
     * @param <K>  Params Class
     * @return Strategy Params
     * @throws HandleStrategyException Handle Exception
     */
    @SuppressWarnings("unchecked")
    public <K> K getProperty(Class<K> cls, String name) throws HandleStrategyException {
        try {
            Field field = mParams.getClass().getField(name);
            Object value = field.get(mParams);
            if (cls.isInstance(value)) {
                return (K) value;
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new HandleStrategyException("get options value failed：" + e.getMessage());
        }
        return null;
    }

    /**
     * Obtains Params
     *
     * @return Params Instance
     */
    public T getParams() {
        return mParams;
    }
}
