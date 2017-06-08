package com.ekocbiyik.tdmdemo.view;

import com.ekocbiyik.tdmdemo.event.DashboardEventBus;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.shared.Position;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.ekocbiyik.tdmdemo.event.DashboardEvent.UserLoginRequestedEvent;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@SuppressWarnings("serial")
public class LoginView extends VerticalLayout {

    public LoginView() {

        setSizeFull();

        Component loginForm = buildLoginForm();
        addComponent(loginForm);
        setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);

        Notification notification = new Notification("");
        notification.setDescription("<span>Copyright &#169; 2017 <a href=\"https://www.ekocbiyik.com\">" +
                "<b style=\"color:#C11410; font-size:14px\">E</b>" +
                "<b style=\"color:#ffffff;\">KOCBIYIK</b>" +
                "</a> All rights reserved. </span><span></span>");
        notification.setHtmlContentAllowed(true);
        notification.setStyleName("tray dark small closable login-help");
        notification.setPosition(Position.BOTTOM_CENTER);
        notification.setDelayMsec(3000);
        notification.show(Page.getCurrent());

    }

    private Component buildLoginForm() {

        final VerticalLayout loginPanel = new VerticalLayout();
        loginPanel.setSizeUndefined();
        loginPanel.setSpacing(true);
        Responsive.makeResponsive(loginPanel);
        loginPanel.addStyleName("login-panel");

        loginPanel.addComponent(buildLabels());
        loginPanel.addComponent(buildFields());
        loginPanel.addComponent(buildVersion());

        return loginPanel;
    }

    private Component buildFields() {

        HorizontalLayout fields = new HorizontalLayout();
        fields.setSpacing(true);
        fields.addStyleName("fields");

        final TextField username = new TextField("Kullanıcı Adı");
        username.setIcon(FontAwesome.USER);
        username.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        final PasswordField password = new PasswordField("Parola");
        password.setIcon(FontAwesome.LOCK);
        password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        final Button signin = new Button("Giriş Yap");
        signin.addStyleName(ValoTheme.BUTTON_PRIMARY);
        signin.setClickShortcut(KeyCode.ENTER);
        signin.focus();

        fields.addComponents(username, password, signin);
        fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);


        signin.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                DashboardEventBus.post(new UserLoginRequestedEvent(username.getValue(), password.getValue()));
            }
        });
        return fields;
    }

    private Component buildLabels() {

        CssLayout labels = new CssLayout();
        labels.addStyleName("labels");

        Label welcome = new Label("TDM-Demo");
        welcome.setSizeUndefined();
        welcome.addStyleName(ValoTheme.LABEL_H4);
        welcome.addStyleName(ValoTheme.LABEL_COLORED);
        labels.addComponent(welcome);

        return labels;
    }

    private Component buildVersion() {

        AbsoluteLayout layout = new AbsoluteLayout();
        layout.setWidth("100%");
        layout.setHeight("40px");

        CheckBox remember = new CheckBox("Beni hatırla", true);
        layout.addComponent(remember, "left: 0px; bottom: 0px;");

        Label version = new Label();
        version.setSizeUndefined();
        version.addStyleName("version-label");
        layout.addComponent(version, "right: 0px; bottom: 0px;");

        Properties prop = getAppProperties();
        if (prop != null) {
            version.setValue("Version: " + prop.getProperty("application.version"));
        }

        return layout;
    }

    private Properties getAppProperties() {

        Properties prop = new Properties();

        try {

            String filename = "application.properties";
            InputStream input = LoginView.class.getClassLoader().getResourceAsStream(filename);
            prop.load(input);
            input.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            return prop;
        }

    }


}
