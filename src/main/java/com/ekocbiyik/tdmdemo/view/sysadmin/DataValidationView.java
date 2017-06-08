package com.ekocbiyik.tdmdemo.view.sysadmin;

import com.ekocbiyik.tdmdemo.event.DashboardEventBus;
import com.ekocbiyik.tdmdemo.model.Pstn_Bbk;
import com.ekocbiyik.tdmdemo.service.IPstn_BbkService;
import com.ekocbiyik.tdmdemo.view.components.NotificationBar;
import com.vaadin.data.Container.Filterable;
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
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.ekocbiyik.tdmdemo.model.DataTransferLock;
import com.ekocbiyik.tdmdemo.service.IDataTransferLockService;
import com.ekocbiyik.tdmdemo.utils.ExpirePstnBbkUtils;
import com.ekocbiyik.tdmdemo.utils.UtilsForSpring;
import com.ekocbiyik.tdmdemo.view.components.PstnBbkTable;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.util.Date;
import java.util.List;

/**
 * Created by enbiya on 18.12.2016.
 */
public class DataValidationView extends Panel implements View {

    private static final Logger logger = Logger.getLogger(SysAdminDashboardView.class);

    private static ProgressBar progressBar;
    private static Button btnStartValidation;
    private static PstnBbkTable pstnBbkTable;
    private static HorizontalLayout criteriaRow;

    private IDataTransferLockService lockService;

    private IPstn_BbkService pstnBbkService;
    private TextField txtFilter = new TextField();
    private Label txtExplain;
    private Label txtExplain2;
    private Label txtExplain3;

    public DataValidationView() {

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

        lockService = UtilsForSpring.getSingleBeanOfType(IDataTransferLockService.class);
        pstnBbkService = UtilsForSpring.getSingleBeanOfType(IPstn_BbkService.class);

        root.addComponent(buildContent());
    }

    private Component buildContent() {

        VerticalLayout contentLayout = new VerticalLayout();
        contentLayout.setSpacing(false);
        contentLayout.setSizeFull();
        Responsive.makeResponsive(contentLayout);

        Label titleLabel = new Label("Veri Doğrulama");
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        contentLayout.addComponent(titleLabel);
        contentLayout.addComponent(new Label("<hr/>", ContentMode.HTML));

        Component c = getDataValidationLayout();
        contentLayout.addComponent(c);
        contentLayout.setExpandRatio(c, 1);

        Component c2 = getTableLayout();
        contentLayout.addComponent(c2);
        contentLayout.setExpandRatio(c2, 2);

        return contentLayout;
    }

    private Component getDataValidationLayout() {

        FormLayout details = new FormLayout();
        details.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        details.addStyleName("background-transparent");


        Label accountInfo = new Label("Uyarı!");
        accountInfo.addStyleName(ValoTheme.LABEL_H4);
        accountInfo.addStyleName(ValoTheme.LABEL_COLORED);
        details.addComponent(accountInfo);

        txtExplain = new Label();
        txtExplain.setWidth("100%");
        txtExplain.setIcon(FontAwesome.CIRCLE);
        txtExplain.setValue("Bu işlem veri tabanındaki tüm bilgiler üzerinde kalıcı değişiklik yapacaktır. İşlemi başlatmadan önce tüm kullanıcıları uyarın.");
        details.addComponent(txtExplain);

        txtExplain2 = new Label();
        txtExplain2.setWidth("100%");
        txtExplain2.setIcon(FontAwesome.CIRCLE);
        txtExplain2.setValue("Doğrulama işlemi bittikten sonra kullanılamayan veriler tabloda listelenecektir.");
        details.addComponent(txtExplain2);

        txtExplain3 = new Label("İşlemler bitene kadar sayfayı yenilemeyin! Sayfa yenileme işlemlerin iptal edilmesine neden olur (delete from t_data_transfer_lock).");
        txtExplain3.setWidth("100%");
        txtExplain3.setIcon(FontAwesome.CIRCLE);
        details.addComponent(txtExplain3);

        criteriaRow = new HorizontalLayout();
        criteriaRow.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        criteriaRow.setSpacing(true);
        criteriaRow.setHeight("40%");
        criteriaRow.setWidth("100%");
        criteriaRow.setCaption("%0");
        details.addComponent(criteriaRow);

        progressBar = new ProgressBar();
        progressBar.setWidth("300px");
        progressBar.setValue(0f);
//        progressBar.setCaption("0%");
        UI.getCurrent().setPollInterval(500);
        criteriaRow.addComponent(progressBar);

        btnStartValidation = new Button("BAŞLAT", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                startDataValidation(UI.getCurrent());
            }
        });

        btnStartValidation.setEnabled(lockService.getTransferLock().size() == 0);
        btnStartValidation.addStyleName(ValoTheme.BUTTON_DANGER);
        criteriaRow.addComponent(btnStartValidation);

        return details;
    }

    private Component getTableLayout() {


        VerticalLayout tableContent = new VerticalLayout();
        tableContent.setSpacing(false);
        tableContent.setSizeFull();
        Responsive.makeResponsive(tableContent);

        tableContent.addComponent(getFilterPanel());
        tableContent.addComponent(new Label("</br>", ContentMode.HTML));


        Component c = getFilterTable();
        tableContent.addComponent(c);
        tableContent.setExpandRatio(c, 1);

        return tableContent;
    }

    private Component getFilterPanel() {

        VerticalLayout layout = new VerticalLayout();
        layout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        layout.setWidth("100%");
        layout.setSpacing(true);

        layout.addComponent(new Label("</br>", ContentMode.HTML));

        txtFilter.setInputPrompt("Pstn, Bbk, Veri Tipi, Ortam Türü...");
        txtFilter.addStyleName(ValoTheme.TEXTFIELD_TINY);
        txtFilter.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        txtFilter.setIcon(FontAwesome.SEARCH);
        txtFilter.setWidth("100%");
        txtFilter.addTextChangeListener(new TextChangeListener() {
            @Override
            public void textChange(TextChangeEvent textChangeEvent) {

                String key = textChangeEvent.getText().trim();
                Filterable c = (Filterable) pstnBbkTable.getContainerDataSource();
                c.removeAllContainerFilters();

                if (!key.equals("")) {
                    c.addContainerFilter(new Or(
                            new SimpleStringFilter("pstn", key, true, false),
                            new SimpleStringFilter("bbk", key, true, false),
                            new SimpleStringFilter("dataType", key, true, false),
                            new SimpleStringFilter("environmentType", key, true, false)
                    ));
                }
            }
        });

        layout.addComponent(txtFilter);

        return layout;
    }

    private Component getFilterTable() {

        if (pstnBbkTable == null) {

            pstnBbkTable = new PstnBbkTable();
            pstnBbkTable.getItemContainer().removeAllItems();

            /** table columns */
            pstnBbkTable.setVisibleColumns(new Object[]{
                    "pstn", "bbk", "dataType", "environmentType",
                    "tckNo", "telauraPstnNo", "dslBasvuru", "crmCustNo",
                    "inOutDoor", "tmsSantralId", "xdslSantralId", "gercekTuzel"});

            pstnBbkTable.setColumnHeaders(
                    "Pstn", "Bbk", "Veri Tipi", "Ortam Türü",
                    "TckNo", "Telaura Pstn", "DSL Başvuru", "CRM Cus. No.",
                    "InDoor/OutDoor", "TMS Santral Id", "XDSL Santral Id", "Gerçek/Tüzel");

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


    private void startDataValidation(UI ui) {

        btnStartValidation.setEnabled(false);

        /** kullanıcı arayüzden tekrar tekrar çalıştırmasın diye */
        lockService.saveTransferLock(new DataTransferLock(true));

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                logger.info("Veri doğrulama işlemi başladı: " + new Date());
                List<Pstn_Bbk> allPstnBbk = pstnBbkService.getAllPstnBbk();

                int totalSize = allPstnBbk.size();
                int index = 1;


                for (Pstn_Bbk data : allPstnBbk) {

                    logger.info(index + ".kayıt: " + data.getBbk() + " - " + data.getPstn() + " denenecek.");
                    boolean isUseful = ExpirePstnBbkUtils.isUseful(data);

                    updateProgresBar(ui, index++, totalSize);

                    if (isUseful) {

                        logger.info(index + ".kayıt: " + data.getBbk() + " - " + data.getPstn() + " kullanılabilir.");
                        continue; // kullanılır durumda ise devam et
                    }


                    data.setInUsefull(false);
                    pstnBbkTable.getItemContainer().addItem(data);
//                    pstnBbkService.save(data); // TODO: 08.06.2017 burayı canlı ortam da aç
                    logger.info(index + ".kayıt: " + data.getBbk() + " - " + data.getPstn() + " kullanılamaz, tabloya eklendi.");

                }

                lockService.deleteTransferLock();
                btnStartValidation.setEnabled(true);
                logger.info("transfer bitti, lock kaldırıldı.");

                NotificationBar.success("Veri doğrulama işlemi tamamlandı!");
            }
        });
    }

    private void updateProgresBar(UI ui, double current, double maxSize) {

        ui.getCurrent().access(new Runnable() {
            @Override
            public void run() {

                float value = (float) (current / maxSize);
                progressBar.setValue(value);
                criteriaRow.setCaption("%" + String.valueOf((int) (value * 100)));
//                progressBar.setCaption(String.valueOf((int) (value * 100)) + "%");
            }
        });
    }

    @Override
    public void enter(ViewChangeEvent viewChangeEvent) {

    }
}
