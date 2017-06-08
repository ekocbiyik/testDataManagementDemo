package com.ekocbiyik.tdmdemo.view.admin;

import com.ekocbiyik.tdmdemo.event.DashboardEventBus;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.ekocbiyik.tdmdemo.view.components.PstnBbkTable;
import org.apache.log4j.Logger;

/**
 * Created by enbiya on 18.12.2016.
 */
public class AdminDataView extends Panel implements View {

    Logger logger = Logger.getLogger(AdminDataView.class);

    private PstnBbkTable pstnBbkTable;

    public AdminDataView() {

        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();
        Responsive.makeResponsive(this);
        DashboardEventBus.register(this);

        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setMargin(true);
        layout.setHeight("100%");
        Responsive.makeResponsive(layout);
        setContent(layout);

        layout.addComponent(buildContent());
    }

    private Component buildContent() {


        VerticalLayout content = new VerticalLayout();
        content.setSpacing(false);
        content.setSizeFull();
        Responsive.makeResponsive(content);


        Label titleLabel = new Label("Bana Ait Veriler");
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        content.addComponent(titleLabel);
        content.addComponent(new Label("<hr/>", ContentMode.HTML));


        pstnBbkTable = new PstnBbkTable();

        content.addComponent(pstnBbkTable);
        content.setExpandRatio(pstnBbkTable, 1);

        return content;
    }

    @Override
    public void enter(ViewChangeEvent viewChangeEvent) {

    }
}
