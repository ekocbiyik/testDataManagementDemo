package com.ekocbiyik.tdmdemo.windows;

import com.ekocbiyik.tdmdemo.model.InfrastructureType;
import com.ekocbiyik.tdmdemo.service.IInfrastructureTypeService;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.shared.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import com.ekocbiyik.tdmdemo.utils.UtilsForSpring;

import java.util.List;

/**
 * Created by enbiya on 03.03.2017.
 */
public class InfrastructureTypeEditWindow extends Window {


    private IInfrastructureTypeService infTypeService;
    private InfrastructureType infType;
    private Table infTable;
    private ComboBox cbxType;
    private TextField txtValue;
    private TextField txtInfo;

    public InfrastructureTypeEditWindow(InfrastructureType infType, Table infTable) {

        this.infType = infType;
        this.infTable = infTable;
        this.infTypeService = UtilsForSpring.getSingleBeanOfType(IInfrastructureTypeService.class);

        addStyleName("profile-window");
        Responsive.makeResponsive(this);

        setModal(true);
        setResizable(true);
        setClosable(true);
        setHeight(70.0f, Unit.PERCENTAGE);//yeterli bir değer
        setWidth(40.0f, Unit.PERCENTAGE);
        addCloseShortcut(KeyCode.ESCAPE, null);
        setCaption("Altyapı Tipi Güncelleme Ekranı");

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

        VerticalLayout infraLayout = new VerticalLayout();
        infraLayout.setWidth("100%");
        infraLayout.setHeight("100%");
        infraLayout.setSpacing(false);
        Responsive.makeResponsive(infraLayout);

        FormLayout details = new FormLayout();
        details.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        infraLayout.addComponent(details);
        infraLayout.setExpandRatio(details, 1);

        Label accountInfo = new Label("Altyapı bilgileri");
        accountInfo.addStyleName(ValoTheme.LABEL_H4);
        accountInfo.addStyleName(ValoTheme.LABEL_COLORED);
        details.addComponent(accountInfo);


        cbxType = new ComboBox("Değişken Tipi");
        cbxType.setInputPrompt("Lütfen seçiniz");
        cbxType.setNewItemsAllowed(false);
        cbxType.setNullSelectionAllowed(false);
        cbxType.setWidth("250px");
        List<String> typeFields = UtilsForSpring.getSingleBeanOfType(IInfrastructureTypeService.class).getDistinctTypeFields();
        cbxType.addItems(typeFields);
        details.addComponent(cbxType);


        txtValue = new TextField("Değişken Değeri");
        txtValue.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
        txtValue.setWidth("100%");
        txtValue.setInputPrompt("Değeri Giriniz");
        txtValue.addTextChangeListener(new TextChangeListener() {
            @Override
            public void textChange(TextChangeEvent event) {
                txtValue.setValue(event.getText().toUpperCase());
            }
        });
        details.addComponent(txtValue);


        txtInfo = new TextField("Bilgi");
        txtInfo.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
        txtInfo.setWidth("250px");
        txtInfo.setInputPrompt("Açıklama Giriniz");
        txtInfo.addTextChangeListener(new TextChangeListener() {
            @Override
            public void textChange(TextChangeEvent event) {
                txtInfo.setValue(event.getText().toUpperCase());
            }
        });
        details.addComponent(txtInfo);

        cbxType.setValue(infType.getType());
        txtValue.setValue(infType.getValue());
        txtInfo.setValue(infType.getInfo());

        return infraLayout;
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

                String type = (String) cbxType.getValue();
                String value = txtValue.getValue();
                String info = txtInfo.getValue();

                if (value.equals("") || value == null || type == null || info.equals("")) {
                    Notification.show("Lütfen Bilgileri Doldurunuz!", Type.WARNING_MESSAGE);
                    return;
                }

                infType.setType((String) cbxType.getValue());
                infType.setValue(txtValue.getValue());
                infType.setInfo(txtInfo.getValue());

                infTypeService.save(infType);
                infTable.refreshRowCache();

                cbxType.clear();
                txtValue.clear();
                txtInfo.clear();

                Notification success = new Notification("Altyapı tipi güncellendi!");
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

}
