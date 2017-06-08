package com.ekocbiyik.tdmdemo.view.admin;

import com.ekocbiyik.tdmdemo.MainUI;
import com.ekocbiyik.tdmdemo.event.DashboardEventBus;
import com.ekocbiyik.tdmdemo.model.Pstn_Bbk;
import com.ekocbiyik.tdmdemo.service.IPstn_BbkService;
import com.ekocbiyik.tdmdemo.view.components.PstnBbkTable;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.ekocbiyik.tdmdemo.utils.UtilsForSpring;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by enbiya on 18.12.2016.
 */
public class AllUsersDataView extends Panel implements View {

    Logger logger = Logger.getLogger(AllUsersDataView.class);

    private PstnBbkTable pstnBbkTable;
    private TextField txtUsernameFilter = new TextField();

    public AllUsersDataView() {

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


        Label titleLabel = new Label("Kullanıcı Verileri");
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        content.addComponent(titleLabel);
        content.addComponent(new Label("<hr/>", ContentMode.HTML));

        content.addComponent(getFilterPanel());
        content.addComponent(new Label("</br>", ContentMode.HTML));

        Component c = getFilterTable();
        content.addComponent(c);
        content.setExpandRatio(c, 1);

        return content;
    }

    private Component getFilterPanel() {

        HorizontalLayout layout = new HorizontalLayout();
        layout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        layout.setSpacing(true);

        txtUsernameFilter.setInputPrompt("Username, Pstn, Bbk, Veri Tipi, Ortam Türü...");
        txtUsernameFilter.addStyleName(ValoTheme.TEXTFIELD_TINY);
        txtUsernameFilter.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        txtUsernameFilter.setIcon(FontAwesome.SEARCH);
        txtUsernameFilter.setWidth("100%");
        txtUsernameFilter.addTextChangeListener(new TextChangeListener() {
            @Override
            public void textChange(TextChangeEvent textChangeEvent) {

                String key = textChangeEvent.getText().trim();
                Filterable c = (Filterable) pstnBbkTable.getContainerDataSource();
                c.removeAllContainerFilters();

                if (!key.equals("")) {
                    c.addContainerFilter(new Or(
                            new SimpleStringFilter("owner.username", key, true, false),
                            new SimpleStringFilter("pstn", key, true, false),
                            new SimpleStringFilter("bbk", key, true, false),
                            new SimpleStringFilter("dataType", key, true, false),
                            new SimpleStringFilter("environmentType", key, true, false)
                    ));
                }
            }
        });

        layout.addComponent(txtUsernameFilter);
        return layout;
    }

    private Component getFilterTable() {

        if (pstnBbkTable == null) {

            pstnBbkTable = new PstnBbkTable();

            List<Pstn_Bbk> usersDataList = UtilsForSpring.getSingleBeanOfType(IPstn_BbkService.class).getUsersDataByAdmin(MainUI.getCurrentUser());
            BeanItemContainer<Pstn_Bbk> itemContainer = pstnBbkTable.getItemContainer();
            itemContainer.addNestedContainerProperty("owner.username");
            itemContainer.addAll(usersDataList);
            pstnBbkTable.setContainerDataSource(itemContainer);


            /** table columns */
            pstnBbkTable.setVisibleColumns(new Object[]{
                    "owner.username",
                    "pstn", "bbk", "dataType", "environmentType", "hizmetTuru",
                    "tckNo", "telauraPstnNo", "dslBasvuru", "crmCustNo",
                    "inOutDoor", "tmsSantralId", "xdslSantralId", "creationDate",
                    "expireDate", "gercekTuzel", "button"});

            pstnBbkTable.setColumnHeaders(
                    "Kullanıcı Adı",
                    "Pstn", "Bbk", "Veri Tipi", "Ortam Türü", "Hizmet Türü",
                    "TckNo", "Telaura Pstn", "DSL Başvuru", "CRM Cus. No.",
                    "InDoor/OutDoor", "TMS Santral Id", "XDSL Santral Id", "Oluşturma Tar.",
                    "Bitiş Tar.", "Gerçek/Tüzel", "_");

            //..
            pstnBbkTable.setColumnCollapsed("tckNo", true);
            pstnBbkTable.setColumnCollapsed("dslBasvuru", true);
            pstnBbkTable.setColumnCollapsed("crmCustNo", true);
            pstnBbkTable.setColumnCollapsed("inOutDoor", true);
            pstnBbkTable.setColumnCollapsed("creationDate", true);
            pstnBbkTable.setColumnCollapsed("gercekTuzel", true);
            pstnBbkTable.setColumnCollapsed("tmsSantralId", true);
            pstnBbkTable.setColumnCollapsed("telauraPstnNo", true);
            pstnBbkTable.setColumnCollapsed("xdslSantralId", true);
        }

        return pstnBbkTable;
    }

    @Override
    public void enter(ViewChangeEvent viewChangeEvent) {

    }
}
