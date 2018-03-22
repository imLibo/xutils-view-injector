package com.cnksi.viewinjector;

import android.app.Application;
import android.content.Context;


import com.cnksi.viewinjector.view.ViewInjectorImpl;

import java.lang.reflect.Method;


/**
 * Created by wyouflf on 15/6/10.
 * 任务控制中心, http, image, db, view注入等接口的入口.
 * 需要在在application的onCreate中初始化: xViewInjector.Ext.init(this);
 */
public final class xViewInjector {

    private xViewInjector() {
    }

    public static boolean isDebug() {
        return Ext.debug;
    }

    public static Application app() {
        if (Ext.app == null) {
            try {
                // 在IDE进行布局预览时使用
                Class<?> renderActionClass = Class.forName("com.android.layoutlib.bridge.impl.RenderAction");
                Method method = renderActionClass.getDeclaredMethod("getCurrentContext");
                Context context = (Context) method.invoke(null);
                Ext.app = new MockApplication(context);
            } catch (Throwable ignored) {
                throw new RuntimeException("please invoke xViewInjector.Ext.init(app) on Application#onCreate()"
                        + " and register your Application in manifest.");
            }
        }
        return Ext.app;
    }

    public static ViewInjector view() {
        if (Ext.viewInjector == null) {
            ViewInjectorImpl.registerInstance();
        }
        return Ext.viewInjector;
    }


    public static class Ext {
        private static boolean debug;
        private static Application app;
        private static ViewInjector viewInjector;

        private Ext() {
        }

        public static void init(Application app) {
            if (Ext.app == null) {
                Ext.app = app;
            }
        }

        public static void setDebug(boolean debug) {
            Ext.debug = debug;
        }

        public static void setViewInjector(ViewInjector viewInjector) {
            Ext.viewInjector = viewInjector;
        }
    }

    private static class MockApplication extends Application {
        public MockApplication(Context baseContext) {
            this.attachBaseContext(baseContext);
        }
    }
}
