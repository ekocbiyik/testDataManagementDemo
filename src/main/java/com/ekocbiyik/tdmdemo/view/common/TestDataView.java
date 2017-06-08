package com.ekocbiyik.tdmdemo.view.common;

import com.ekocbiyik.tdmdemo.MainUI;
import com.ekocbiyik.tdmdemo.event.DashboardEventBus;
import com.ekocbiyik.tdmdemo.service.IInfrastructureTypeService;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import com.ekocbiyik.tdmdemo.enums.EnvironmentType;
import com.ekocbiyik.tdmdemo.model.Pstn_Bbk;
import com.ekocbiyik.tdmdemo.model.User;
import com.ekocbiyik.tdmdemo.service.IPstn_BbkService;
import com.ekocbiyik.tdmdemo.utils.UtilsForSpring;
import com.ekocbiyik.tdmdemo.view.components.PstnBbkTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by enbiya on 26.12.2016.
 */
public class TestDataView extends Panel implements View {


    private IInfrastructureTypeService infTypeService;
    private PstnBbkTable pstnBbkTable;

    private Button btnList;
    private Button btnUseBBK;
    private Window dateWindow;

    //
    private ComboBox cbxInternet;
    private ComboBox cbxTv;
    private ComboBox cbxEnvironmentType;

    private IPstn_BbkService pstnBbkService;


    public TestDataView() {

        pstnBbkService = UtilsForSpring.getSingleBeanOfType(IPstn_BbkService.class);
        infTypeService = UtilsForSpring.getSingleBeanOfType(IInfrastructureTypeService.class);


        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();
        DashboardEventBus.register(this);
        Responsive.makeResponsive(this);

        VerticalLayout root = new VerticalLayout();
        root.setSpacing(true);
        root.setMargin(true);
        root.setHeight("100%");
        Responsive.makeResponsive(root);
        setContent(root);

        root.addComponent(buildContent());
    }


    private Component buildContent() {

        //burada comboboxlar olacak
        VerticalLayout criteriaLayout = new VerticalLayout();
        criteriaLayout.setSpacing(false);
        criteriaLayout.setSizeFull();
        Responsive.makeResponsive(criteriaLayout);

        Label titleLabel = new Label("Verisi Listeleme");
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        criteriaLayout.addComponent(titleLabel);
        criteriaLayout.addComponent(new Label("<hr/>", ContentMode.HTML));


        HorizontalLayout criteriaRow = new HorizontalLayout();
        criteriaRow.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        criteriaRow.setSpacing(true);
        criteriaRow.setHeight("40px");
        criteriaLayout.addComponent(criteriaRow);

        cbxInternet = new ComboBox();
        cbxInternet.setId("cbxInternet");
        cbxInternet.addStyleName(ValoTheme.COMBOBOX_SMALL);
        cbxInternet.setNullSelectionAllowed(false);
        cbxInternet.setTextInputAllowed(false);
        cbxInternet.setInputPrompt("Hizmet Tipi");
        cbxInternet.setWidth("30%");
        cbxInternet.addItems("Yalın Internet", "Internet");
        criteriaRow.addComponent(cbxInternet);


        cbxTv = new ComboBox();
        cbxTv.setId("cbxTv");
        cbxTv.addStyleName(ValoTheme.COMBOBOX_SMALL);
        cbxTv.setNullSelectionAllowed(true);
        cbxTv.setTextInputAllowed(false);
        cbxTv.setInputPrompt("Televizyon");
        cbxTv.setWidth("30%");
        cbxTv.addItems(infTypeService.getTvTypeValues());
        criteriaRow.addComponent(cbxTv);


        cbxEnvironmentType = new ComboBox();
        cbxEnvironmentType.setId("cbxEnvironmentType");
        cbxEnvironmentType.addStyleName(ValoTheme.COMBOBOX_SMALL);
        cbxEnvironmentType.setNullSelectionAllowed(false);
        cbxEnvironmentType.setTextInputAllowed(false);
        cbxEnvironmentType.setInputPrompt("Ortam Türü");
        cbxEnvironmentType.setWidth("30%");
        cbxEnvironmentType.addItems(EnvironmentType.values());
        criteriaRow.addComponent(cbxEnvironmentType);


        btnList = new Button("Listele", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {

                if (!isFormValuesOK()) {
                    return;
                }

                String internet = (String) cbxInternet.getValue();
                String dataType = cbxTv.getValue() == null ? "VDSLADSL" : "VDSLADSLIPTV";
                EnvironmentType envType = (EnvironmentType) cbxEnvironmentType.getValue();

                User currentUser = MainUI.getCurrentUser();
                List<Pstn_Bbk> pstnBbkList = new ArrayList<>();

                /** eğer nvdsl seçilmiş ise bbk'lar listele en az pstn'e sahip bbklardan ilk sırada olanı seç blokla, getir */
                if (internet.equalsIgnoreCase("Yalın Internet")) {
                    pstnBbkList = pstnBbkService.getNvdslBbk(currentUser, dataType, envType);
                }

                /** eğer vdsl seçilmiş ise en çok pstn'e sahip bbk içinden 1 tanenin sadece pstn'ini blokla */
                if (internet.equalsIgnoreCase("Internet")) {
                    pstnBbkList = pstnBbkService.getVdslBbk(currentUser, dataType, envType);
                }

                pstnBbkTable.getItemContainer().removeAllItems();

                if (pstnBbkList.size() == 0) {
                    Notification.show("Aradığınız kriterlerde BBK bulunamadı.", Type.TRAY_NOTIFICATION);
                    return;
                }

                pstnBbkTable.getItemContainer().addAll(pstnBbkList);
                btnUseBBK.setEnabled(true);
                btnList.setEnabled(false);
            }
        });
        btnList.setId("btnList");
        criteriaRow.addComponent(btnList);
        criteriaRow.setComponentAlignment(btnList, Alignment.MIDDLE_LEFT);


        btnUseBBK = new Button("Kullan");
        btnUseBBK.setId("btnUseBBK");
        btnUseBBK.setEnabled(false);
        btnUseBBK.setWidth("120px");

        btnUseBBK.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                openDatePlanningWindow();
            }
        });
        criteriaRow.addComponent(btnUseBBK);

        Table c = getTableContent();
        criteriaLayout.addComponent(c);
        criteriaLayout.setExpandRatio(c, 1);
        Responsive.makeResponsive(c);

        return criteriaLayout;
    }

    private PstnBbkTable getTableContent() {

        User user = MainUI.getCurrentUser();
        List<Pstn_Bbk> pstn_bbkList = UtilsForSpring.getSingleBeanOfType(IPstn_BbkService.class).getInResevedPstnBbk(user);

        if (pstn_bbkList.size() > 0) {
            btnList.setEnabled(false);
            btnUseBBK.setEnabled(true);
        }

        pstnBbkTable = new PstnBbkTable();
        //defaultta kullanıcının tüm pstnlerini çekiyor
        pstnBbkTable.getItemContainer().removeAllItems();
        pstnBbkTable.getItemContainer().addAll(pstn_bbkList);


        /** buradaki fieldlar farklı olduğu için yeniden set etmemiz gerekiyor */
        pstnBbkTable.setVisibleColumns(new Object[]{
                "pstn", "bbk", "dataType", "environmentType", "hizmetTuru",
                "tckNo", "telauraPstnNo", "dslBasvuru", "crmCustNo",
                "inOutDoor", "tmsSantralId", "xdslSantralId", "creationDate",
                "expireDate", "gercekTuzel"});

        pstnBbkTable.setColumnHeaders("Pstn", "Bbk", "Veri Tipi", "Ortam Türü", "Hizmet Türü",
                "TckNo", "Telaura Pstn", "DSL Başvuru", "CRM Cus. No.",
                "InDoor/OutDoor", "TMS Santral Id", "XDSL Santral Id", "Oluşturma Tar.",
                "Bitiş Tar.", "Gerçek/Tüzel");

        //..
        pstnBbkTable.setColumnCollapsed("dslBasvuru", true);
        pstnBbkTable.setColumnCollapsed("crmCustNo", true);
        pstnBbkTable.setColumnCollapsed("inOutDoor", true);
        pstnBbkTable.setColumnCollapsed("creationDate", true);
        pstnBbkTable.setColumnCollapsed("gercekTuzel", true);

        return pstnBbkTable;
    }

    private void openDatePlanningWindow() {

        VerticalLayout windowContent = new VerticalLayout();
        windowContent.setWidth("100%");
        windowContent.setMargin(true);
        windowContent.setSpacing(true);


        Label section = new Label("Bitiş Tarihi");
        section.addStyleName(ValoTheme.LABEL_H4);
        section.addStyleName(ValoTheme.LABEL_COLORED);
        windowContent.addComponent(section);

        HorizontalLayout wrap = new HorizontalLayout();
        wrap.setSpacing(true);
        wrap.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        windowContent.addComponent(wrap);


        DateField dateField = new DateField();
        dateField.addStyleName(ValoTheme.DATEFIELD_BORDERLESS);
        dateField.setWidth("30%");
        dateField.setDateFormat("dd/MM/yyyy");
        dateField.setRangeStart(new Date());
        dateField.setValue(new Date());
        dateField.setInvalidAllowed(true);
        dateField.setRequired(true);
        wrap.addComponent(dateField);

        Button btnAccept = new Button("Onayla", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {

                if (dateField.getValue() == null) {
                    Notification.show("Lütfen bitiş Tarihi belirtin!", Type.TRAY_NOTIFICATION);
                    return;
                }

                Date expireDate = dateField.getValue();

                /** rezerv tablosunda sadece bir tane bbk olacağı için.. */

                List<Pstn_Bbk> bbkList = pstnBbkTable.getItemContainer().getItemIds();
                Pstn_Bbk tempBbk = bbkList.get(0);
                List<Pstn_Bbk> hiddenPstnList = pstnBbkService.getUsersAllHiddenPstnByBbk(MainUI.getCurrentUser(), tempBbk);

                /** burada hidden olanları set et*/
                for (Pstn_Bbk i : hiddenPstnList) {
                    i.setCreationDate(new Date());
                    i.setExpireDate(expireDate);
                    i.setInReserve(false);
                    pstnBbkService.save(i);
                }

                /** burada asıl bbk'yı set et */
                tempBbk.setCreationDate(new Date());
                tempBbk.setExpireDate(expireDate);
                tempBbk.setInReserve(false);
                pstnBbkService.save(tempBbk);

                dateWindow.close();
                clearPage();
            }
        });
        btnAccept.addStyleName(ValoTheme.BUTTON_DANGER);
        wrap.addComponent(btnAccept);
        wrap.setComponentAlignment(btnAccept, Alignment.BOTTOM_RIGHT);

        Grid bbkGrid = new Grid();
        bbkGrid.setHeight("40%");
        bbkGrid.addColumn("BBK");
        bbkGrid.addColumn("PSTN");

        List<Pstn_Bbk> bbkList = pstnBbkTable.getItemContainer().getItemIds();
        for (Pstn_Bbk i : bbkList) {
            bbkGrid.addRow(i.getBbk(), i.getPstn());
        }
        windowContent.addComponent(bbkGrid);

        dateWindow = new Window();
        dateWindow.setCaption("BBK/PSTN Onaylama Ekranı");
        dateWindow.setModal(true);
        dateWindow.setResizable(true);
        dateWindow.addCloseShortcut(KeyCode.ESCAPE);
        dateWindow.setContent(windowContent);

        UI.getCurrent().addWindow(dateWindow);
    }

    private boolean isFormValuesOK() {

        if (cbxInternet.getValue() == null) {
            Notification.show("Internet Bağlantı tipi seçiniz!", Type.HUMANIZED_MESSAGE);
            return false;

        } else if (cbxEnvironmentType.getValue() == null) {
            Notification.show("Ortam türü boş olamaz!", Type.HUMANIZED_MESSAGE);
            return false;
        } else {
            return true;
        }
    }

    private void clearPage() {

        User user = MainUI.getCurrentUser();
        List<Pstn_Bbk> pstn_bbkList = UtilsForSpring.getSingleBeanOfType(IPstn_BbkService.class).getInResevedPstnBbk(user);
        pstnBbkTable.getItemContainer().removeAllItems();
        pstnBbkTable.getItemContainer().addAll(pstn_bbkList);

        btnList.setEnabled(pstn_bbkList.size() == 0);
        btnUseBBK.setEnabled(pstn_bbkList.size() != 0);
        cbxTv.setValue(null);
        cbxInternet.setValue(null);
        cbxEnvironmentType.setValue(null);
    }

    @Override
    public void enter(ViewChangeEvent viewChangeEvent) {

    }
}
