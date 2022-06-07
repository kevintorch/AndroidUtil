package com.torch.androidutil.reflection;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zimong on 10/25/2017.
 */

public class MethodsFinder {

    private static Method[] methods;
    private static List<Method> getters;
    private static List<Method> setters;
    private static List<Method> otherMethods;


    public static List<Method> getGettersOf(Class<?> c) {
        methods = c.getDeclaredMethods();
        seperateMethod();
        return getters;
    }

    public static List<Method> getSettersOf(Class<?> c) {
        methods = c.getDeclaredMethods();
        seperateMethod();
        return setters;
    }

    public static List<Method> getOtherMethodsExceptGettersAndSettersOf(Class<?> c) {
        methods = c.getDeclaredMethods();
        seperateMethod();
        return otherMethods;
    }

    public static List<Method> getAllMethodsOf(Class<?> c) {
        methods = c.getDeclaredMethods();
        return Arrays.asList(methods);
    }

    private static void seperateMethod() {
        for (Method method : methods) {
            if (isGetter(method)) {
                getters.add(method);
                continue;
            }
            if (isSetter(method)) {
                setters.add(method);
                continue;
            }
            otherMethods.add(method);
        }
    }

    public static boolean isGetter(Method method) {
        if (Modifier.isPublic(method.getModifiers()) &&
                method.getParameterTypes().length == 0) {
            if (method.getName().matches("^get[A-Z].*") &&
                    !method.getReturnType().equals(void.class))
                return true;
            return method.getName().matches("^is[A-Z].*") &&
                    method.getReturnType().equals(boolean.class);
        }
        return false;
    }

    public static boolean isSetter(Method method) {
        return Modifier.isPublic(method.getModifiers()) &&
                method.getReturnType().equals(void.class) &&
                method.getParameterTypes().length == 1 &&
                method.getName().matches("^set[A-Z].*");
    }
}
