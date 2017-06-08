package com.ekocbiyik.tdmdemo.view.components;

import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;

/**
 * Created by enbiya on 26.05.2017.
 */
public class NotificationBar {

    public static void success(String text) {

        Notification success = new Notification(text);
        success.setDelayMsec(2000);
        success.setStyleName("bar success small");
        success.setPosition(Position.BOTTOM_CENTER);
        success.show(Page.getCurrent());
    }

    public static void error(String text) {

        Notification success = new Notification(text);
        success.setDelayMsec(2000);
        success.setStyleName("bar failure small");
        success.setPosition(Position.BOTTOM_CENTER);
        success.show(Page.getCurrent());
    }

}
