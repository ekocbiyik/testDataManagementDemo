package com.ekocbiyik.tdmdemo.view.common;

import com.ekocbiyik.tdmdemo.event.DashboardEventBus;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Resource;
import com.vaadin.server.Responsive;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by enbiya on 26.12.2016.
 */
public class UserGuideView extends Panel implements View {


    private VerticalLayout mainLayout;

    public UserGuideView() {

        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();
        DashboardEventBus.register(this);

        mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        mainLayout.setMargin(true);
        mainLayout.setSpacing(true);
        setContent(mainLayout);
        Responsive.makeResponsive(mainLayout);

        Component c = buildContent();
        mainLayout.addComponent(c);
        mainLayout.setExpandRatio(c, 1);
    }

    private Component buildContent() {

        VerticalLayout layout = new VerticalLayout();
        layout.addStyleName("background-transparent");

        int page = 0;
        for (Resource i : createImageList()) {

            Image image = new Image(null, i);
            image.setSizeFull();

            layout.addComponent(new Label("Sayfa." + (++page), ContentMode.HTML));
            layout.addComponent(image);
            layout.addComponent(new Label("<hr/>", ContentMode.HTML));
        }

        return layout;
    }

    private List<Resource> createImageList() {
        List<Resource> img = new ArrayList<Resource>();
        for (int i = 1; i < 10; i++) {
            img.add(new ThemeResource("userguide/res" + i + ".jpg"));
        }
        return img;
    }


    @Override
    public void enter(ViewChangeEvent viewChangeEvent) {

    }
}
