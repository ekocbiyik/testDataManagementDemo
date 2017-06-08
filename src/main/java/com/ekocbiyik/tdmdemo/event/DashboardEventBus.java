package com.ekocbiyik.tdmdemo.event;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import com.ekocbiyik.tdmdemo.MainUI;

/**
 * Created by enbiya on 15.12.2016.
 */
public class DashboardEventBus implements SubscriberExceptionHandler {


    private final EventBus eventBus = new EventBus(this);

    public static void post(final Object event) {
        MainUI.getDashboardEventBus().eventBus.post(event);
    }

    public static void register(final Object event) {
        MainUI.getDashboardEventBus().eventBus.register(event);
    }

    public static void unregister(final Object event) {
        MainUI.getDashboardEventBus().eventBus.unregister(event);
    }

    @Override
    public void handleException(Throwable throwable, SubscriberExceptionContext context) {
        throwable.printStackTrace();
    }
}
