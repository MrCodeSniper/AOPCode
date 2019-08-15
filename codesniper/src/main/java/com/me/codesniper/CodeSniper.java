package com.me.codesniper;

import java.lang.reflect.Constructor;
import java.util.LinkedHashMap;
import java.util.Map;

public class CodeSniper {


    static final Map<Class<?>, Constructor<? extends ViewInjector>> BINDINGS = new LinkedHashMap<>();

    public static void inject(Object activity) {
        inject(activity, activity);
    }

    public static void inject(Object host, Object root) {
        if (host == null|| root==null) {
            return;
        }
        try {
            Class<?> cls = host.getClass();//反射拿到宿主类
            Constructor<? extends ViewInjector> constructor = findBindingConstructorForClass(cls);
            //拿到接口的实现进行绑定
            ViewInjector viewBinding = constructor.newInstance();
            viewBinding.inject(host,root);
        } catch (Exception e) {

        }
    }

    private static Constructor<? extends ViewInjector> findBindingConstructorForClass(Class<?> cls) throws Exception {
        Constructor<? extends ViewInjector> constructor = BINDINGS.get(cls);//缓存拿构造方法
        if (constructor == null) {
            String className = cls.getName();
            //拿到类加载起加载注解生成类
            Class<?> bindingClass = cls.getClassLoader().loadClass(className + "_ViewBinding");
            //拿到实现ViewInjector接口的方法
            constructor = (Constructor<? extends ViewInjector>) bindingClass.getConstructor();
            //放入缓存
            BINDINGS.put(cls, constructor);
        }
        return constructor;
    }







}
