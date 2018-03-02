package com.huang.aio.util;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * @author simple_huang@foxmail.com on 2018/1/30.
 */
public class Calculator {
    private final static ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");
    public static Object cal(String expression) throws ScriptException {
        return jse.eval(expression);
    }
}
