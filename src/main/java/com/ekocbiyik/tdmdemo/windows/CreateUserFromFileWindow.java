package com.ekocbiyik.tdmdemo.windows;

import com.ekocbiyik.tdmdemo.MainUI;
import com.ekocbiyik.tdmdemo.service.IUserService;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import com.ekocbiyik.tdmdemo.enums.EUserRole;
import com.ekocbiyik.tdmdemo.model.Company;
import com.ekocbiyik.tdmdemo.model.User;
import com.ekocbiyik.tdmdemo.service.ICompanyService;
import com.ekocbiyik.tdmdemo.utils.EncryptionUtils;
import com.ekocbiyik.tdmdemo.utils.UtilsForSpring;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by enbiya on 17.05.2017.
 */
public class CreateUserFromFileWindow extends Window {


    private String userFilename;
    private ByteArrayOutputStream userOutStream = new ByteArrayOutputStream(10240);
    private List<String> userFileLines;
    private Map<String, User> userMapList;
    private Table usersTable;
    private ComboBox cbxCompany;

    private IUserService userService;

    private Logger logger = Logger.getLogger(CreateUserFromFileWindow.class);


    public CreateUserFromFileWindow() {

        userService = UtilsForSpring.getSingleBeanOfType(IUserService.class);

        addStyleName("profile-window");
        Responsive.makeResponsive(this);

        setModal(true);
        setResizable(true);
        setClosable(true);
        setHeight("70%");
        setWidth("61%");
        addCloseShortcut(KeyCode.ESCAPE, null);
        setCaption("Dosyadan Test Kullanıcısı Ekleme");

        VerticalLayout content = new VerticalLayout();
        content.setHeight("100%");
        content.setWidth("100%");
        content.setMargin(true);
        content.setSpacing(false);
        setContent(content);

        Component c = buildContent();
        content.addComponent(c);
        content.setExpandRatio(c, 1f);

        content.addComponent(buildFooter());
    }

    private Component buildContent() {

        VerticalLayout contentLayout = new VerticalLayout();
        contentLayout.setWidth("100%");
        contentLayout.setSpacing(false);
        contentLayout.setMargin(new MarginInfo(false, true, false, true));
        contentLayout.addStyleName("profile-form");

        Label accountInfo = new Label("Açıklama");
        accountInfo.addStyleName(ValoTheme.LABEL_H4);
        accountInfo.addStyleName(ValoTheme.LABEL_COLORED);
        contentLayout.addComponent(accountInfo);

        Label row1 = new Label("* Kullanıcıya ait bilgilerin arasına \";\" işareti konularak dosyaya satır satır eklenmelidir.");
        Label row2 = new Label("* username/password bilgisi mail adresinin kendisi olarak tanımlanır: abc.def@mail.com -> user: abc.def, pass: abc.def ");
        Label row3 = new Label("* Örnek dosya satırı: <b>email;firstName;lastName;</b>", ContentMode.HTML);


        contentLayout.addComponents(row1, row2, row3, new Label("<hr/>", ContentMode.HTML));
        contentLayout.addComponents(buildUploadContent(), new Label("<hr/>", ContentMode.HTML));
        contentLayout.addComponent(buildUserTable());

        return contentLayout;
    }

    private Component buildUserTable() {

        VerticalLayout v = new VerticalLayout();
        v.setSizeFull();
        Responsive.makeResponsive(v);

        BeanItemContainer<User> container = new BeanItemContainer<User>(User.class);

        usersTable = new Table();
        Responsive.makeResponsive(usersTable);
        usersTable.setContainerDataSource(container);
        usersTable.setSizeFull();
        usersTable.setSelectable(true);
        usersTable.setMultiSelect(false);
        usersTable.setImmediate(true);
        usersTable.setColumnCollapsingAllowed(true);
        usersTable.setPageLength(4);

        usersTable.setContainerDataSource(container);
        usersTable.addGeneratedColumn("btn", new ColumnGenerator() {
            @Override
            public Object generateCell(Table table, Object o, Object o1) {

                Button btnRemove = new Button("Kaldır", new ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent clickEvent) {
                        usersTable.removeItem(o);
                    }
                });

                return btnRemove;
            }
        });

        usersTable.setVisibleColumns(new Object[]{"username", "firstName", "lastName", "email", "btn"});
        usersTable.setColumnHeaders("Username", "Adı", "Soyadı", "e-mail", "_");
        usersTable.setColumnCollapsible("username", false);

        v.addComponent(usersTable);

        return v;
    }

    private Component buildUploadContent() {

        HorizontalLayout content = new HorizontalLayout();
        content.setWidth("100%");

        Upload upload = new Upload();
        upload.setEnabled(false);
        upload.setReceiver(new Receiver() {
            @Override
            public OutputStream receiveUpload(String filename, String mimeType) {
                userFilename = filename;
                userOutStream.reset();
                return userOutStream;
            }
        });

        upload.addSucceededListener(new SucceededListener() {
            @Override
            public void uploadSucceeded(SucceededEvent succeededEvent) {

                StreamSource source = new StreamSource() {
                    @Override
                    public InputStream getStream() {
                        return new ByteArrayInputStream(userOutStream.toByteArray());
                    }
                };

                try {
                    StreamResource streamResource = new StreamResource(source, userFilename);
                    userFileLines = IOUtils.readLines(streamResource.getStream().getStream(), "UTF-8");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (userFileLines == null) {
                    Notification.show("Dosya okuma hatası!", Type.ERROR_MESSAGE);
                    logger.info("Dosya okuma hatası: userFileLines = null");
                    return;
                }

                if (userFileLines.size() == 0) {
                    Notification.show("Lütfen uygun bir dosya seçiniz!", Type.TRAY_NOTIFICATION);
                    logger.info("Dosya okuma hatası: userFileLines.size() = 0");
                    return;
                }

                /** satırları parse et */
                parseUserFileLines(userFileLines);

                /** userMapList'i tabloya doldur */
                addUsers2Table();

            }
        });

        content.addComponent(upload);


        BeanItemContainer<Company> container = new BeanItemContainer<Company>(Company.class);
        List<Company> companyList = UtilsForSpring.getSingleBeanOfType(ICompanyService.class).getCompanies();
        container.addAll(companyList);

        cbxCompany = new ComboBox();
        cbxCompany.setContainerDataSource(container);
        cbxCompany.setInputPrompt("Firma seçiniz..");
        cbxCompany.addStyleName(ValoTheme.COMBOBOX_BORDERLESS);
        cbxCompany.setItemCaptionPropertyId("companyName");
        cbxCompany.addItems(companyList);
        cbxCompany.setNewItemsAllowed(false);
        cbxCompany.setNullSelectionAllowed(false);
        cbxCompany.setWidth("200px");
        cbxCompany.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent valueChangeEvent) {
                upload.setEnabled(cbxCompany.getValue() != null);
            }
        });

        content.addComponent(cbxCompany);

        return content;
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

                /** tablo boş ise zaten buraya düşmüyor! */

                saveUsersFromTable2Db();

                Notification success = new Notification("İşlem tamamlandı!");
                success.setDelayMsec(2000);
                success.setStyleName("bar success small");
                success.setPosition(Position.BOTTOM_CENTER);
                success.show(Page.getCurrent());

                if (usersTable.size() > 0) {
                    Notification.show("Ekrandaki kullanıcılar veritabanında zaten kayıtlı!", Type.ERROR_MESSAGE);
                } else {
                    close();
                }
            }
        });

        ok.focus();
        footer.addComponent(ok);
        footer.setComponentAlignment(ok, Alignment.TOP_RIGHT);

        return footer;
    }

    private void parseUserFileLines(List<String> userFileLines) {

        userMapList = new HashMap<>();

        for (String line : userFileLines) {

            if (line.split(";").length != 3) {
                logger.info("Dosya satırı uygun formatta değil: " + line);
                continue;
            }

            String email = line.split(";")[0];
            String firstName = line.split(";")[1];
            String lastName = line.split(";")[2];

            if (!email.contains("@") || !email.contains(".") || firstName.length() < 3 || lastName.length() < 2) {
                logger.info("Satır bilgileri uygun formatta değil: " + line);
                continue;
            }

            User user = new User();
            user.setUsername(email.split("@")[0]);
            user.setPassword(EncryptionUtils.hexMd5(email.split("@")[0]));
            user.setRole(EUserRole.TEST_ENG);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setPhone(null);
            user.setCreationDate(new Date());
            user.setCreatedBy(MainUI.getCurrentUser().getUsername());
            user.setActive(true);
            user.setCompany((Company) cbxCompany.getValue());

            userMapList.put(user.getUsername(), user);
        }
    }

    private void addUsers2Table() {

        /** tablodaki kayıtları sil */
        usersTable.removeAllItems();

        if (userMapList.size() == 0) {
            Notification.show("Uygun formatta kayıt bulunamadı!", Type.TRAY_NOTIFICATION);
            logger.info("Dosya boş yada uygun formatta kayıt yok!");
            return;
        }

        for (Entry<String, User> u : userMapList.entrySet()) {
            usersTable.addItem(u.getValue());
        }
    }

    private void saveUsersFromTable2Db() {

        List<User> savedUsers = new ArrayList<>();

        for (User u : (List<User>) usersTable.getItemIds()) {

            if (userService.isUsernameExist(u.getUsername())) {
                logger.info(u.getUsername() + " kullanıcısı zaten mevcut!");
                continue;
            }

            userService.save(u);
            savedUsers.add(u);
            userMapList.remove(u.getUsername()); /** gerek yok ama olsun */
            logger.info(u.getUsername() + " kullanıcısı kaydedildi.");
        }

        for (User su : savedUsers) {
            usersTable.removeItem(su);
        }
    }

}
