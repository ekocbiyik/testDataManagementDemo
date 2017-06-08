package com.ekocbiyik.tdmdemo.view.sysadmin;

import com.ekocbiyik.tdmdemo.event.DashboardEventBus;
import com.ekocbiyik.tdmdemo.model.InfrastructureType;
import com.ekocbiyik.tdmdemo.service.IInfrastructureTypeService;
import com.ekocbiyik.tdmdemo.windows.InfrastructureTypeEditWindow;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractSelect.NewItemHandler;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
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
import com.ekocbiyik.tdmdemo.utils.UtilsForSpring;

import java.util.List;

/**
 * Created by enbiya on 18.12.2016.
 */
public class NewInfrastructureTypeView extends Panel implements View {

    //fileds
    private ComboBox cbxType;
    private TextField txtValue;
    private TextField txtInfo;
    private Table infTypeTable;

    private IInfrastructureTypeService infTypeService;
    private CheckBox chckNewInfType;


    public NewInfrastructureTypeView() {

        infTypeService = UtilsForSpring.getSingleBeanOfType(IInfrastructureTypeService.class);

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

        Label titleLabel = new Label("Altyapı Tipi Ekle");
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        root.addComponent(titleLabel);
        root.addComponent(new Label("<hr/>", ContentMode.HTML));


        Component c = buildContent();
        root.addComponent(c);
        root.setExpandRatio(c, 1);

    }

    private Component buildContent() {

        VerticalLayout infraLayout = new VerticalLayout();
        infraLayout.setSpacing(false);
        infraLayout.setWidth("100%");
        infraLayout.setHeight("100%");
        Responsive.makeResponsive(infraLayout);

        FormLayout details = new FormLayout();
        details.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        details.addStyleName("background-transparent");
        details.setMargin(true);
        infraLayout.addComponent(details);
        infraLayout.addComponent(new Label("</br>", ContentMode.HTML));
//        infraLayout.setExpandRatio(details, 1);

        Label accountInfo = new Label("Altyapı bilgileri");
        accountInfo.addStyleName(ValoTheme.LABEL_H4);
        accountInfo.addStyleName(ValoTheme.LABEL_COLORED);
        details.addComponent(accountInfo);

        HorizontalLayout wrap = new HorizontalLayout();
        wrap.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        wrap.setCaption("Altyapı Tipi");
        details.addComponent(wrap);

        cbxType = new ComboBox();
        cbxType.setInputPrompt("Lütfen seçiniz");
        cbxType.setNewItemsAllowed(false);
        cbxType.setNullSelectionAllowed(false);
        cbxType.setWidth("250px");
        cbxType.addStyleName(ValoTheme.COMBOBOX_BORDERLESS);
        cbxType.addItems(infTypeService.getDistinctTypeFields());
        cbxType.setNewItemHandler(new NewItemHandler() {
            @Override
            public void addNewItem(String newItemCaption) {
                cbxType.addItem(newItemCaption.toUpperCase());
                cbxType.select(((List<String>) cbxType.getItemIds()).get(cbxType.size() - 1));
            }
        });

        wrap.addComponent(cbxType);


        chckNewInfType = new CheckBox("Yeni Ekle");
        chckNewInfType.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent valueChangeEvent) {

                cbxType.clear();
                if (!chckNewInfType.getValue()) {
                    cbxType.removeAllItems();
                    cbxType.addItems(infTypeService.getDistinctTypeFields());
                } else {
                    cbxType.setNewItemsAllowed(chckNewInfType.getValue());
                }

            }
        });
        wrap.addComponent(chckNewInfType);

        txtValue = new TextField("Altyapı Değeri");
        txtValue.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
        txtValue.setWidth("100%");
        txtValue.setInputPrompt("Değer Giriniz");
        txtValue.addTextChangeListener(new TextChangeListener() {
            @Override
            public void textChange(TextChangeEvent event) {
                txtValue.setValue(event.getText().toUpperCase());
            }
        });
        details.addComponent(txtValue);


        HorizontalLayout wrapSave = new HorizontalLayout();
        wrapSave.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
//        wrapSave.setSpacing(true);
        wrapSave.setCaption("Bilgi");
        details.addComponent(wrapSave);

        txtInfo = new TextField();
        txtInfo.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
        txtInfo.setWidth("250px");
        txtInfo.setInputPrompt("Açıklama Giriniz");
        txtInfo.addTextChangeListener(new TextChangeListener() {
            @Override
            public void textChange(TextChangeEvent event) {
                txtInfo.setValue(event.getText().toUpperCase());
            }
        });
        wrapSave.addComponent(txtInfo);

        Button btn = new Button("Kaydet", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {

                String type = (String) cbxType.getValue();
                String value = txtValue.getValue();
                String info = txtInfo.getValue();

                if (value.equals("") || value == null || type == null || info.equals("")) {
                    Notification.show("Lütfen Bilgileri Doldurunuz!", Type.WARNING_MESSAGE);
                    return;
                }

                Notification success = new Notification("Kullanıcı başarıyla güncellendi!");
                success.setDelayMsec(2000);
                success.setStyleName("bar success small");
                success.setPosition(Position.BOTTOM_CENTER);
                success.show(Page.getCurrent());

                infTypeService.save(new InfrastructureType(type, value, info));
                infTypeTable.getContainerDataSource().removeAllItems();

                List<InfrastructureType> typeList = infTypeService.getAllInfrastructureTypes();
                for (InfrastructureType i : typeList) {
                    infTypeTable.getContainerDataSource().addItem(i);
                }

                cbxType.removeAllItems();
                cbxType.addItems(infTypeService.getDistinctTypeFields());

                cbxType.clear();
                txtValue.clear();
                txtInfo.clear();
                chckNewInfType.clear();
            }
        });
        btn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        wrapSave.addComponent(btn);

        BeanItemContainer<InfrastructureType> itemContainer = new BeanItemContainer<InfrastructureType>(InfrastructureType.class);
        itemContainer.addAll(infTypeService.getAllInfrastructureTypes());

        infTypeTable = new Table();
        infTypeTable.setContainerDataSource(itemContainer);
        infTypeTable.setHeight("100%");
        infTypeTable.setWidth("100%");
        infTypeTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        infTypeTable.setSelectable(true);
        infTypeTable.setMultiSelect(false);
        infTypeTable.setImmediate(true);
        infTypeTable.setColumnCollapsingAllowed(true);
//        infTypeTable.setPageLength(10);

        infTypeTable.addGeneratedColumn("sil", new ColumnGenerator() {
            @Override
            public Object generateCell(Table table, Object o, Object o1) {

                Button btn = new Button("Güncelle");
                btn.addStyleName(ValoTheme.BUTTON_DANGER);
                btn.addClickListener(new ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent clickEvent) {

                        Window w = new InfrastructureTypeEditWindow((InfrastructureType) o, infTypeTable);
                        UI.getCurrent().addWindow(w);
                        w.focus();
                    }
                });
                return btn;
            }
        });

        infTypeTable.setVisibleColumns(new Object[]{"id", "type", "value", "info", "sil"});
        infTypeTable.setColumnHeaders("Id", "Altyapı Tipi", "Altyapı Değeri", "Bilgi", "");

        infraLayout.addComponent(infTypeTable);
        infraLayout.setExpandRatio(infTypeTable, 1);
        Responsive.makeResponsive(infTypeTable);

        return infraLayout;
    }

    @Override
    public void enter(ViewChangeEvent viewChangeEvent) {

    }
}
