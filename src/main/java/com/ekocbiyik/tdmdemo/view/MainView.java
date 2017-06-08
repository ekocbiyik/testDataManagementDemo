package com.ekocbiyik.tdmdemo.view;

import com.ekocbiyik.tdmdemo.DashboardNavigator;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;

/**
 * Created by enbiya on 15.12.2016.
 */
public class MainView extends HorizontalLayout {

    public MainView() {

        setSizeFull();
        addStyleName("mainview");
        addComponent(new DashboardMenu());// sol menu bar


        ComponentContainer content = new CssLayout();
        content.addStyleName("view-content");
        content.setSizeFull();
        addComponent(content);
        setExpandRatio(content, 1.0f);

        new DashboardNavigator(content);// sayfa içeriği
    }

}
