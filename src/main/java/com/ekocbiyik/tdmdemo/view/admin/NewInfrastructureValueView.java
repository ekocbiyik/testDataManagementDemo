package com.ekocbiyik.tdmdemo.view.admin;

import com.ekocbiyik.tdmdemo.event.DashboardEventBus;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.ekocbiyik.tdmdemo.MainUI;
import com.ekocbiyik.tdmdemo.enums.EUserRole;

import java.util.Date;

/**
 * Created by enbiya on 18.12.2016.
 */
public class NewInfrastructureValueView extends Panel implements View {

    private final VerticalLayout root;

    //fileds
    private TextField txtUsername;
    private TextField txtPassword;
    private ComboBox cbxRole;
    private TextField txtEmail;
    private TextField txtFirstName;
    private TextField txtLastName;
    private TextField txtPhone;
    private DateField creationDate;
    private TextField createdBy; // önemli değil sadece bilgi olarak tutalım yeterli
    private boolean isActive;

    public NewInfrastructureValueView() {

        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();
        DashboardEventBus.register(this);


        root = new VerticalLayout();
//        root.setSizeFull();
        root.setMargin(true);
        setContent(root);
        Responsive.makeResponsive(root);

        Component content = buildContent();
        root.addComponent(content);
        root.setExpandRatio(content, 1);

    }

    private Component buildContent() {

        VerticalLayout profileLayout = new VerticalLayout();

        profileLayout.setWidth(100.0f, Unit.PERCENTAGE);
        profileLayout.setSpacing(true);
        profileLayout.setMargin(true);
        Responsive.makeResponsive(profileLayout);

        FormLayout details = new FormLayout();
        details.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        profileLayout.addComponent(details);
        profileLayout.setExpandRatio(details, 1);

        Label accountInfo = new Label("Hesap bilgileri");
        accountInfo.addStyleName(ValoTheme.LABEL_H4);
        accountInfo.addStyleName(ValoTheme.LABEL_COLORED);
        details.addComponent(accountInfo);

        txtUsername = new TextField("username");
        txtUsername.setWidth("350px");
        txtUsername.setIcon(FontAwesome.USER);
        details.addComponent(txtUsername);

        HorizontalLayout wrap = new HorizontalLayout();
        wrap.setSpacing(true);
        wrap.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        wrap.setCaption("password");
        wrap.setIcon(FontAwesome.LOCK);
        details.addComponent(wrap);

        txtPassword = new TextField();
        txtPassword.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
        txtPassword.setWidth("350px");
        wrap.addComponent(txtPassword);

        Button generatePass = new Button("Parola oluştur", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                txtPassword.setValue("denemeParola123@.,");
            }
        });
        generatePass.addStyleName(ValoTheme.BUTTON_PRIMARY);
        generatePass.setHeight("23px");
        generatePass.setIcon(FontAwesome.COGS);
        wrap.addComponent(generatePass);
//        details.addComponent(generatePass);

        txtEmail = new TextField("e-mail");
        txtEmail.setWidth("350px");
        txtEmail.setIcon(FontAwesome.ENVELOPE);
        details.addComponent(txtEmail);

        cbxRole = new ComboBox("Yetki Tipi");
        cbxRole.setInputPrompt("Lütfen seçiniz");
        cbxRole.addItems(EUserRole.values());
        cbxRole.setNewItemsAllowed(true);
        cbxRole.setNullSelectionAllowed(false);
        cbxRole.setWidth("250px");
        details.addComponent(cbxRole);


        Label userInfo = new Label("Kullanıcı bilgileri");
        userInfo.addStyleName(ValoTheme.LABEL_H4);
        userInfo.addStyleName(ValoTheme.LABEL_COLORED);
        details.addComponent(userInfo);

        txtFirstName = new TextField("Adı");
        details.addComponent(txtFirstName);

        txtLastName = new TextField("Soyadı");
        details.addComponent(txtLastName);

        txtPhone = new TextField("Telefon");
        details.addComponent(txtPhone);

        creationDate = new DateField("Oluşturma tarihi");
        creationDate.setValue(new Date());
        creationDate.setResolution(Resolution.SECOND);
        creationDate.setEnabled(false);
        creationDate.setWidth("250px");
        details.addComponent(creationDate);

        createdBy = new TextField("Created By");
        createdBy.setValue(MainUI.getCurrentUser().getUsername());
        createdBy.setEnabled(false);
        details.addComponent(createdBy);


        TextField txtActive = new TextField("Aktif/Pasif");
        txtActive.setValue(String.valueOf(isActive));
        txtActive.setEnabled(false);
        details.addComponent(txtActive);

        Button btnSave = new Button("Kaydet", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                Notification.show("Kullanıcıyı kaydet.....!");
            }
        });
        btnSave.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        btnSave.setWidth("150px");
//        details.addComponent(btnSave);
        profileLayout.addComponent(btnSave);
        profileLayout.setComponentAlignment(btnSave, Alignment.TOP_LEFT);
        return profileLayout;
    }

    @Override
    public void enter(ViewChangeEvent viewChangeEvent) {

    }
}
