package com.ekocbiyik.tdmdemo.view.sysadmin;

import com.ekocbiyik.tdmdemo.MainUI;
import com.ekocbiyik.tdmdemo.event.DashboardEventBus;
import com.ekocbiyik.tdmdemo.service.IUserService;
import com.ekocbiyik.tdmdemo.view.components.NotificationBar;
import com.ekocbiyik.tdmdemo.windows.CreateUserFromFileWindow;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.shared.ui.label.ContentMode;
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
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import com.ekocbiyik.tdmdemo.enums.EUserRole;
import com.ekocbiyik.tdmdemo.model.Company;
import com.ekocbiyik.tdmdemo.model.User;
import com.ekocbiyik.tdmdemo.service.ICompanyService;
import com.ekocbiyik.tdmdemo.utils.EncryptionUtils;
import com.ekocbiyik.tdmdemo.utils.UtilsForSpring;

import java.util.Date;
import java.util.List;

/**
 * Created by enbiya on 18.12.2016.
 */
public class NewUserView extends Panel implements View {


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
    private ComboBox cbxActivePasive;
    private ComboBox cbxCompany;

    public NewUserView() {

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


        Label titleLabel = new Label("Kullanıcı Oluştur");
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        root.addComponent(titleLabel);
        root.addComponent(new Label("<hr/>", ContentMode.HTML));

        Component content = buildContent();
        root.addComponent(content);
        root.setExpandRatio(content, 1);

    }

    private Component buildContent() {

        VerticalLayout contentLayout = new VerticalLayout();
        contentLayout.setSpacing(false);
        contentLayout.setMargin(new MarginInfo(false, true, false, true));
        contentLayout.setWidth("100%");
        contentLayout.setHeight("100%");
        contentLayout.addStyleName("background-transparent");
        Responsive.makeResponsive(contentLayout);

        FormLayout details = new FormLayout();
        details.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        contentLayout.addComponent(details);

        Label accountInfo = new Label("Hesap bilgileri");
        accountInfo.addStyleName(ValoTheme.LABEL_H4);
        accountInfo.addStyleName(ValoTheme.LABEL_COLORED);
        details.addComponent(accountInfo);

        txtUsername = new TextField("username");
        txtUsername.setWidth("250px");
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
        txtPassword.setWidth("250px");
        wrap.addComponent(txtPassword);

        Button generatePass = new Button("Parola oluştur", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                txtPassword.setValue(EncryptionUtils.getRandomPassword());
            }
        });
        generatePass.addStyleName(ValoTheme.BUTTON_PRIMARY);
        generatePass.setHeight("23px");
        generatePass.setIcon(FontAwesome.COGS);
        wrap.addComponent(generatePass);

        txtEmail = new TextField("e-mail");
        txtEmail.setWidth("250px");
        txtEmail.setIcon(FontAwesome.ENVELOPE);
        details.addComponent(txtEmail);

        cbxRole = new ComboBox("Yetki Tipi");
        cbxRole.setInputPrompt("Lütfen seçiniz");
        cbxRole.addItems(EUserRole.values());
        cbxRole.setNewItemsAllowed(false);
        cbxRole.setNullSelectionAllowed(false);
        cbxRole.setWidth("250px");
        details.addComponent(cbxRole);

        cbxRole.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent valueChangeEvent) {
                cbxCompany.setEnabled(cbxRole.getValue() != EUserRole.SYSADMIN);
                cbxCompany.setValue(cbxRole.getValue() == EUserRole.SYSADMIN ? null : null);
            }
        });

        BeanItemContainer<Company> container = new BeanItemContainer<Company>(Company.class);
        List<Company> companyList = UtilsForSpring.getSingleBeanOfType(ICompanyService.class).getCompanies();
        container.addAll(companyList);

        cbxCompany = new ComboBox("Firma", container);
        cbxCompany.setInputPrompt("Lütfen seçiniz");
        cbxCompany.setItemCaptionPropertyId("companyName");
        cbxCompany.addItems(companyList);
        cbxCompany.setNewItemsAllowed(false);
        cbxCompany.setWidth("250px");
        details.addComponent(cbxCompany);

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
        creationDate.setDateFormat("yyyy/MM/dd HH:mm:ssS");
        details.addComponent(creationDate);

        createdBy = new TextField("Created By");
        createdBy.setValue(MainUI.getCurrentUser().getUsername());
        createdBy.setEnabled(false);
        details.addComponent(createdBy);

        cbxActivePasive = new ComboBox("Aktif/Pasif");
        cbxActivePasive.setWidth("250px");
        cbxActivePasive.setNullSelectionAllowed(false);
        cbxActivePasive.addItem(true);
        cbxActivePasive.addItem(false);
        details.addComponent(cbxActivePasive);

        Button btnSave = new Button("Kaydet", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {

                if (!isFormFieldsOK()) {
                    return;
                }

                User user = new User();
                user.setUsername(txtUsername.getValue().toLowerCase());
                user.setPassword(EncryptionUtils.hexMd5(txtPassword.getValue()));//paralo encrypt
                user.setEmail(txtEmail.getValue());
                user.setRole((EUserRole) cbxRole.getValue());
                user.setCompany((Company) cbxCompany.getValue());
                user.setFirstName(txtFirstName.getValue());
                user.setLastName(txtLastName.getValue());
                user.setPhone(txtPhone.getValue());
                user.setActive((Boolean) cbxActivePasive.getValue());
                user.setCreationDate(new Date());
                user.setCreatedBy(MainUI.getCurrentUser().getUsername());

                IUserService userService = UtilsForSpring.getSingleBeanOfType(IUserService.class);
                if (userService.isUsernameExist(user.getUsername())) {
                    Notification.show("Username'e ait kayıt zaten var!", Type.ERROR_MESSAGE);
                    return;
                }

                //..
                userService.save(user);

                NotificationBar.success("Kullanıcı kaydedildi!");
                clearFormFields();
            }
        });

        btnSave.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        btnSave.setWidth("150px");
        details.addComponent(btnSave);


        details.addComponent(new Label());// boşluk için

        Button btnMultiUser = new Button("Dosyadan Yükle", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {

                Window w = new CreateUserFromFileWindow();
                MainUI.getCurrent().addWindow(w);
            }
        });
        details.addComponent(btnMultiUser);

        return contentLayout;
    }

    private boolean isFormFieldsOK() {

        if (txtUsername.getValue() == null || txtUsername.getValue().equalsIgnoreCase("") || txtUsername.getValue().length() < 6) {
            Notification.show("Username adı en az 6 karakterli olmalıdır!", Type.HUMANIZED_MESSAGE);
            return false;

        } else if (txtPassword.isEnabled() && (txtPassword.getValue() == null || txtPassword.getValue().equalsIgnoreCase("") || txtPassword.getValue().length() < 8)) {
            Notification.show("Password en az 8 karakterden oluşmalıdır! ", Type.HUMANIZED_MESSAGE);
            return false;

        } else if (!txtEmail.getValue().contains("@") || !txtEmail.getValue().contains(".") || txtEmail.getValue().length() < 10) {
            Notification.show("Mail adresi geçersiz!", Type.HUMANIZED_MESSAGE);
            return false;

        } else if (txtFirstName.getValue() == null || txtFirstName.getValue().equalsIgnoreCase("") || txtFirstName.getValue().length() < 3) {
            Notification.show("Geçersiz kullanıcı adı! ", Type.HUMANIZED_MESSAGE);
            return false;

        } else if (cbxCompany.isEnabled() && cbxCompany.isEmpty()) {
            Notification.show("Firma seçiniz! ", Type.HUMANIZED_MESSAGE);
            return false;

        } else if (txtLastName.getValue() == null || txtLastName.getValue().equalsIgnoreCase("") || txtLastName.getValue().length() < 2) {
            Notification.show("Kullanıcı soyadı geçersiz! ", Type.HUMANIZED_MESSAGE);
            return false;

        } else if (cbxActivePasive.getValue() == null) {
            Notification.show("Aktif/Pasif durumu seçiniz!", Type.HUMANIZED_MESSAGE);
            return false;

        } else {
            return true;
        }
    }

    private void clearFormFields() {

        txtUsername.clear();
        txtPassword.clear();
        cbxRole.clear();
        cbxCompany.clear();
        txtEmail.clear();
        txtFirstName.clear();
        txtLastName.clear();
        txtPhone.clear();
        creationDate.setValue(new Date());
        cbxActivePasive.clear();
        cbxActivePasive.clear();
    }

    @Override
    public void enter(ViewChangeEvent viewChangeEvent) {
    }

}
