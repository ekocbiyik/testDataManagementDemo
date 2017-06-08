package com.ekocbiyik.tdmdemo.windows;

import com.ekocbiyik.tdmdemo.enums.EUserRole;
import com.ekocbiyik.tdmdemo.service.IUserService;
import com.ekocbiyik.tdmdemo.utils.EncryptionUtils;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import com.ekocbiyik.tdmdemo.model.User;
import com.ekocbiyik.tdmdemo.utils.UtilsForSpring;

/**
 * Created by enbiya on 22.03.2017.
 */
public class EditProfileWindow extends Window {

    private final User user;

    private TextField txtUsername;
    private TextField txtPassword;
    private TextField txtEmail;
    private TextField txtFirstName;
    private TextField txtLastName;
    private TextField txtPhone;
    private DateField dfCreationDate;
    private TextField txtCreatedBy;
    private CheckBox chckPassChange;
    private Button btngeneratePass;


    public EditProfileWindow(User user) {

        this.user = user;

        addStyleName("profile-window");
        Responsive.makeResponsive(this);

        setModal(true);
        setResizable(true);
        setClosable(true);
        setHeight(80.0f, Unit.PERCENTAGE);//yeterli bir değer
        setWidth(60.0f, Unit.PERCENTAGE);
        addCloseShortcut(KeyCode.ESCAPE, null);
        setCaption("Profil Güncelleme");

        VerticalLayout content = new VerticalLayout();
        content.setHeight("100%");
        content.setMargin(true);
        setContent(content);

        Component c = buildContent();
        content.addComponent(c);
        content.setExpandRatio(c, 1f);

        content.addComponent(buildFooter());

    }

    private Component buildContent() {

        HorizontalLayout userContent = new HorizontalLayout();
        userContent.setWidth("100%");
        userContent.setSpacing(true);
//        userContent.setMargin(new MarginInfo(false, true, false, true));
        userContent.setMargin(false);
        userContent.addStyleName("profile-form");

        VerticalLayout pict = new VerticalLayout();
        pict.setSizeUndefined();
        pict.setSpacing(true);
        Image profilePict = new Image(null, new ThemeResource("img/" + (user.getRole() == EUserRole.ADMIN ? "admin.jpg" : "testeng.png")));
        profilePict.setWidth(100.0f, Unit.PIXELS);
        pict.addComponent(profilePict);
        userContent.addComponent(pict);//..

        FormLayout details = new FormLayout();
        details.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        userContent.addComponent(details);
        userContent.setExpandRatio(details, 1);

        Label accountInfo = new Label("Hesap bilgileri");
        accountInfo.addStyleName(ValoTheme.LABEL_H4);
        accountInfo.addStyleName(ValoTheme.LABEL_COLORED);
        details.addComponent(accountInfo);

        txtUsername = new TextField("username");
        txtUsername.setWidth("350px");
        txtUsername.setIcon(FontAwesome.USER);
        txtUsername.setEnabled(false);
        details.addComponent(txtUsername);

        HorizontalLayout wrapPass = new HorizontalLayout();
        wrapPass.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        wrapPass.setSpacing(true);
        wrapPass.setIcon(FontAwesome.LOCK);
        wrapPass.setCaption("password");
        details.addComponent(wrapPass);

        txtPassword = new TextField();
        txtPassword.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
        txtPassword.setWidth("210px");
        txtPassword.setEnabled(false);
        txtPassword.setInputPrompt("*************");
        wrapPass.addComponent(txtPassword);

        chckPassChange = new CheckBox("", false);
        chckPassChange.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent valueChangeEvent) {

                boolean isChecked = chckPassChange.getValue();
                txtPassword.setEnabled(isChecked);
                txtPassword.setInputPrompt(isChecked ? "" : "*************");
                btngeneratePass.setEnabled(isChecked);

                if (!isChecked) {
                    txtPassword.clear();
                }
            }
        });
        wrapPass.addComponent(chckPassChange);

        btngeneratePass = new Button("Parolayı oluştur", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                txtPassword.setValue(EncryptionUtils.getRandomPassword());
            }
        });

        btngeneratePass.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btngeneratePass.setHeight("23px");
        btngeneratePass.setIcon(FontAwesome.COGS);
        btngeneratePass.setEnabled(false);
        wrapPass.addComponent(btngeneratePass);

        txtEmail = new TextField("e-mail");
        txtEmail.setWidth("350px");
        txtEmail.setIcon(FontAwesome.ENVELOPE);
        details.addComponent(txtEmail);


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

        dfCreationDate = new DateField("Oluşturma tarihi");
        dfCreationDate.setDateFormat("yyyy/MM/dd HH:mm:ssS");
        dfCreationDate.setResolution(Resolution.SECOND);
        dfCreationDate.setEnabled(false);
        dfCreationDate.setWidth("250px");
        details.addComponent(dfCreationDate);

        txtCreatedBy = new TextField("Oluşturan Kişi");
        txtCreatedBy.setEnabled(false);
        details.addComponent(txtCreatedBy);

        /** bilgileri burada set et*/
        txtUsername.setValue(user.getUsername());
        txtEmail.setValue(user.getEmail());
        txtFirstName.setValue(user.getFirstName());
        txtLastName.setValue(user.getLastName());
        txtPhone.setValue(user.getPhone());
        dfCreationDate.setValue(user.getCreationDate());
        txtCreatedBy.setValue(user.getCreatedBy());

        return userContent;
    }

    private Component buildFooter() {

        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Unit.PERCENTAGE);

        Button ok = new Button("Kaydet");
        ok.addStyleName(ValoTheme.BUTTON_PRIMARY);
        ok.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {

                if (!isFormFieldsOK()) {
                    return;
                }

                if (chckPassChange.getValue()) {
                    user.setPassword(EncryptionUtils.hexMd5(txtPassword.getValue()));
                }

                user.setEmail(txtEmail.getValue());
                user.setFirstName(txtFirstName.getValue());
                user.setLastName(txtLastName.getValue());
                user.setPhone(txtPhone.getValue());

                UtilsForSpring.getSingleBeanOfType(IUserService.class).save(user);

                Notification success = new Notification("Güncelleme Başarılı");
                success.setDelayMsec(2000);
                success.setStyleName("bar success small");
                success.setPosition(Position.BOTTOM_CENTER);
                success.show(Page.getCurrent());
                close();
            }
        });

        ok.focus();
        footer.addComponent(ok);
        footer.setComponentAlignment(ok, Alignment.TOP_RIGHT);

        return footer;
    }


    private boolean isFormFieldsOK() {

        if (txtPassword.isEnabled() && (txtPassword.getValue() == null || txtPassword.getValue().equalsIgnoreCase("") || txtPassword.getValue().length() < 8)) {
            Notification.show("Parolanız en az 8 karakterden oluşmalıdır!", Type.HUMANIZED_MESSAGE);
            return false;

        } else if (!txtEmail.getValue().contains("@") || !txtEmail.getValue().contains(".") || txtEmail.getValue().length() < 10) {
            Notification.show("Geçersiz mail adresi!", Type.HUMANIZED_MESSAGE);
            return false;

        } else if (txtFirstName.getValue() == null || txtFirstName.getValue().equalsIgnoreCase("") || txtFirstName.getValue().length() < 3) {
            Notification.show("Geçersiz kullanıcı ismi!", Type.HUMANIZED_MESSAGE);
            return false;

        } else if (txtLastName.getValue() == null || txtLastName.getValue().equalsIgnoreCase("") || txtLastName.getValue().length() < 2) {
            Notification.show("Geçersiz kullanıcı soyadı!", Type.HUMANIZED_MESSAGE);
            return false;

        } else {
            return true;
        }
    }

}
