package com.ekocbiyik.tdmdemo.view.testeng;

import com.vaadin.data.Container.Filterable;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import com.ekocbiyik.tdmdemo.view.components.PstnBbkTable;

/**
 * Created by enbiya on 26.12.2016.
 */
public class TestEngDashboardView extends VerticalLayout {

    private PstnBbkTable pstnBbkTable;
    private Window pstnBbkUpdateWindow;
    private TextField txtPstnBbkFilter;

    public Component getTestEngDashboard() {

        setSpacing(false);
        setSizeFull();
        Responsive.makeResponsive(this);

        Label titleLabel = new Label("Dashboard");
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        addComponent(titleLabel);
        addComponent(new Label("<hr/>", ContentMode.HTML));

        addComponent(getFilterPanel());
        addComponent(new Label("</br>", ContentMode.HTML));

        Component c = getContent();
        addComponent(c);
        setExpandRatio(c, 1);

        return this;
    }

    private Component getContent() {

        pstnBbkTable = new PstnBbkTable();

        return pstnBbkTable;
    }

    private Component getFilterPanel() {

        HorizontalLayout layout = new HorizontalLayout();
        layout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        layout.setSpacing(true);

        txtPstnBbkFilter = new TextField();
        txtPstnBbkFilter.setInputPrompt("Bbk, Pstn, Tms Id, Xdsl Id, Ortam Türü...");
        txtPstnBbkFilter.addStyleName(ValoTheme.TEXTFIELD_TINY);
        txtPstnBbkFilter.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        txtPstnBbkFilter.setIcon(FontAwesome.SEARCH);
        txtPstnBbkFilter.setWidth("100%");
        txtPstnBbkFilter.addTextChangeListener(new TextChangeListener() {
            @Override
            public void textChange(TextChangeEvent textChangeEvent) {

                String key = textChangeEvent.getText().trim();
                Filterable c = (Filterable) pstnBbkTable.getContainerDataSource();

                c.removeAllContainerFilters();

                if (!key.equals("")) {

                    c.addContainerFilter(new Or(
                            new SimpleStringFilter("pstn", key, true, false),
                            new SimpleStringFilter("bbk", key, true, false),
                            new SimpleStringFilter("tmsSantralId", key, true, false),
                            new SimpleStringFilter("xdslSantralId", key, true, false),
                            new SimpleStringFilter("environmentType", key, true, false)
                    ));
                }
            }
        });

        layout.addComponent(txtPstnBbkFilter);
        return layout;
    }

}
