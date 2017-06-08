package com.ekocbiyik.tdmdemo.view.sysadmin;

import com.ekocbiyik.tdmdemo.event.DashboardEventBus;
import com.ekocbiyik.tdmdemo.model.Company;
import com.ekocbiyik.tdmdemo.service.ICompanyService;
import com.ekocbiyik.tdmdemo.utils.UtilsForSpring;
import com.ekocbiyik.tdmdemo.view.components.NotificationBar;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;

/**
 * Created by enbiya on 18.12.2016.
 */
public class NewCompanyView extends Panel implements View {

    //fileds
    private TextField txtCompanyname;
    private Table companyTable;

    public NewCompanyView() {

        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();
        DashboardEventBus.register(this);
        Responsive.makeResponsive(this);

        VerticalLayout root = new VerticalLayout();
        root.setMargin(true);
        root.setSpacing(false);
        root.setHeight("100%");
        Responsive.makeResponsive(root);
        setContent(root);

        root.addComponent(buildContent());
    }

    private Component buildContent() {

        VerticalLayout contentLayout = new VerticalLayout();
        contentLayout.setSpacing(false);
        contentLayout.setSizeFull();
        Responsive.makeResponsive(contentLayout);

        Label titleLabel = new Label("Firma Oluştur");
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        contentLayout.addComponent(titleLabel);
        contentLayout.addComponent(new Label("<hr/>", ContentMode.HTML));


        FormLayout details = new FormLayout();
        details.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        details.addStyleName("background-transparent");
        contentLayout.addComponent(details);
        contentLayout.addComponent(new Label("</br>", ContentMode.HTML));


        Label accountInfo = new Label("Firma bilgileri");
        accountInfo.addStyleName(ValoTheme.LABEL_H4);
        accountInfo.addStyleName(ValoTheme.LABEL_COLORED);
        details.addComponent(accountInfo);

        HorizontalLayout wrapSave = new HorizontalLayout();
        wrapSave.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        wrapSave.setIcon(FontAwesome.INDUSTRY);
        wrapSave.setCaption("Firma adı");
        details.addComponent(wrapSave);

        txtCompanyname = new TextField();
        txtCompanyname.setWidth("250px");
        txtCompanyname.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
        txtCompanyname.setInputPrompt("Firma ismini giriniz...");
        wrapSave.addComponent(txtCompanyname);


        Button btnSave = new Button("Kaydet", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {

                if (txtCompanyname.isEmpty()) {
                    Notification.show("Lütfen Şirket ismini giriniz!", Type.WARNING_MESSAGE);
                    return;
                }

                Company company = new Company();
                company.setCompanyName(txtCompanyname.getValue().toUpperCase());

                ICompanyService companyService = UtilsForSpring.getSingleBeanOfType(ICompanyService.class);
                companyService.save(company);

                companyTable.getContainerDataSource().removeAllItems();
                companyTable.refreshRowCache();
                for (Company i : companyService.getCompanies()) {
                    companyTable.getContainerDataSource().addItem(i);
                }

                NotificationBar.success("Firma kaydedildi!");
                clearFormFields();
            }
        });

        btnSave.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        wrapSave.addComponent(btnSave);

        List<Company> companyList = UtilsForSpring.getSingleBeanOfType(ICompanyService.class).getCompanies();
        BeanItemContainer<Company> itemContainer = new BeanItemContainer<Company>(Company.class);
        itemContainer.addAll(companyList);

        companyTable = new Table();
        Responsive.makeResponsive(companyTable);
        companyTable.setContainerDataSource(itemContainer);
        companyTable.setWidth("100%");
        companyTable.setHeight("100%");
        companyTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        companyTable.setSelectable(true);
        companyTable.setMultiSelect(false);
        companyTable.setImmediate(true);
        companyTable.setColumnCollapsingAllowed(true);

        companyTable.addGeneratedColumn("btn", new ColumnGenerator() {
            @Override
            public Object generateCell(Table table, Object o, Object o1) {
                Button btn = new Button("Güncelle");
                btn.addClickListener(new ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent clickEvent) {
                        updateCompanyWindow((Company) o);
                    }
                });
                return btn;
            }
        });

        companyTable.setVisibleColumns(new Object[]{"companyId", "companyName", "btn"});
        companyTable.setColumnHeaders("Firma Id", "Firma Adı", "");

        contentLayout.addComponent(companyTable);
        contentLayout.setExpandRatio(companyTable, 1);

        return contentLayout;
    }

    private void updateCompanyWindow(Company company) {

        Window w = new Window();
        w.setCaption("Firma Güncelleme Ekranı");
        w.setWidth("30%");
        w.setModal(true);
        w.setResizable(true);
        w.addCloseShortcut(KeyCode.ESCAPE);

        VerticalLayout content = new VerticalLayout();
        w.setContent(content);

        content.setMargin(true);
        Responsive.makeResponsive(content);

        FormLayout details = new FormLayout();
        details.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        content.addComponent(details);
        content.setExpandRatio(details, 1);

        Label accountInfo = new Label("Şirket bilgileri");
        accountInfo.addStyleName(ValoTheme.LABEL_H4);
        accountInfo.addStyleName(ValoTheme.LABEL_COLORED);
        details.addComponent(accountInfo);

        TextField txtName = new TextField("Firma adı");
        txtName.setWidth("250px");
        txtName.setIcon(FontAwesome.INDUSTRY);
        txtName.setValue(company.getCompanyName());
        details.addComponent(txtName);


        Button btnSave = new Button("Güncelle", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {

                if (txtName.isEmpty()) {
                    Notification.show("Lütfen Firma adını giriniz!", Type.WARNING_MESSAGE);
                    return;
                }

                company.setCompanyName(txtName.getValue().toUpperCase());
                UtilsForSpring.getSingleBeanOfType(ICompanyService.class).save(company);

                List<Company> companyList = UtilsForSpring.getSingleBeanOfType(ICompanyService.class).getCompanies();
                companyTable.removeAllItems();

                for (Company i : companyList) {
                    companyTable.getContainerDataSource().addItem(i);
                }

                NotificationBar.success("Firma kaydedildi!");
                w.close();
            }
        });

        btnSave.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        btnSave.setWidth("150px");
        content.addComponent(btnSave);
        content.setComponentAlignment(btnSave, Alignment.TOP_LEFT);

        UI.getCurrent().addWindow(w);
    }

    private void clearFormFields() {
        txtCompanyname.clear();
    }

    @Override
    public void enter(ViewChangeEvent viewChangeEvent) {

    }
}
