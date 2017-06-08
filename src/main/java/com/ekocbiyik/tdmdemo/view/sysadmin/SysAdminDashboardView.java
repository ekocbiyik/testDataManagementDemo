package com.ekocbiyik.tdmdemo.view.sysadmin;

import com.ekocbiyik.tdmdemo.enums.EnvironmentType;
import com.ekocbiyik.tdmdemo.model.Pstn_Bbk;
import com.ekocbiyik.tdmdemo.service.IDataTransferLockService;
import com.ekocbiyik.tdmdemo.service.IPstn_BbkService;
import com.ekocbiyik.tdmdemo.view.components.NotificationBar;
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
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.MessageBox;
import com.ekocbiyik.tdmdemo.model.DataTransferLock;
import com.ekocbiyik.tdmdemo.utils.UtilsForSpring;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by enbiya on 18.12.2016.
 */
public class SysAdminDashboardView extends VerticalLayout {

    private static final Logger logger = Logger.getLogger(SysAdminDashboardView.class);
    private static Button btnRegresyonTransfer;
    private static Button btnBugfixTransfer;
    private static ProgressBar progressBar;
    private static HorizontalLayout criteriaRow;
    private IDataTransferLockService transferLockService;

    public Component getSysAdminDashboard() {

        transferLockService = UtilsForSpring.getSingleBeanOfType(IDataTransferLockService.class);

        setSpacing(false);
        setSizeFull();
        Responsive.makeResponsive(this);

        Label titleLabel = new Label("Dashboard");
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        addComponent(titleLabel);
        addComponent(new Label("<hr/>", ContentMode.HTML));

        Component c = getContent();
        addComponent(c);
        setExpandRatio(c, 1);

        return this;
    }

    private Component getContent() {

        VerticalLayout content = new VerticalLayout();
        content.setSpacing(false);
        content.setSizeFull();
        Responsive.makeResponsive(content);

        Component c = getDbDataInfoLayout();
        content.addComponent(c);
        content.setExpandRatio(c, 1);

        Component c2 = getDataTransferLayout();
        content.addComponent(c2);
        content.setExpandRatio(c2, 1.2f);

        return content;
    }

    private Component getDbDataInfoLayout() {

        HorizontalLayout dbInfo = new HorizontalLayout();
        dbInfo.setSizeFull();
        dbInfo.setSpacing(true);

        dbInfo.addComponent(createInfo(EnvironmentType.TEST));
        dbInfo.addComponent(createInfo(EnvironmentType.REGRESYON));
        dbInfo.addComponent(createInfo(EnvironmentType.BUGFIX));

        return dbInfo;
    }

    private Component getDataTransferLayout() {


        FormLayout dataTransferLayout = new FormLayout();
        dataTransferLayout.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        dataTransferLayout.addStyleName("background-transparent");

        Label titleLabel = new Label("      Veri Transfer Paneli");
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H4);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        dataTransferLayout.addComponent(titleLabel);


        criteriaRow = new HorizontalLayout();
        criteriaRow.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        criteriaRow.setSpacing(true);
        criteriaRow.setHeight("40%");
        criteriaRow.setWidth("100%");
        criteriaRow.setCaption("%0");
        dataTransferLayout.addComponent(criteriaRow);


        progressBar = new ProgressBar();
        progressBar.setWidth("300px");
        progressBar.setValue(0f);
        UI.getCurrent().setPollInterval(500);
        criteriaRow.addComponent(progressBar);


        btnBugfixTransfer = new Button("BugFix Transferini başlat", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {

                MessageBox
                        .createQuestion()
                        .withCaption("Uyarı")
                        .withMessage("Bu işlem tablodaki verilerin silinmesine neden olacak, Devam etmek istiyor musunuz?")
                        .withYesButton(new Runnable() {
                            @Override
                            public void run() {
                                startDataTransfer(UI.getCurrent(), EnvironmentType.BUGFIX);
                            }
                        }, ButtonOption.caption("Devam et"))
                        .withNoButton(new Runnable() {
                            @Override
                            public void run() {
                                logger.info("transfer işlemi iptal edildi!");
                            }
                        }, ButtonOption.caption("iptal"))
                        .open();
            }
        });

        btnBugfixTransfer.setEnabled(transferLockService.getTransferLock().size() == 0);
        criteriaRow.addComponent(btnBugfixTransfer);


        btnRegresyonTransfer = new Button("Regresyon Transferini başlat", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {

                MessageBox
                        .createQuestion()
                        .withCaption("Uyarı")
                        .withMessage("Bu işlem tablodaki verilerin silinmesine neden olacak, Devam etmek istiyor musunuz?")
                        .withYesButton(new Runnable() {
                            @Override
                            public void run() {
                                startDataTransfer(UI.getCurrent(), EnvironmentType.REGRESYON);
                            }
                        }, ButtonOption.caption("Devam et"))
                        .withNoButton(new Runnable() {
                            @Override
                            public void run() {
                                logger.info("transfer işlemi iptal edildi!");
                            }
                        }, ButtonOption.caption("iptal"))
                        .open();
            }
        });

        btnRegresyonTransfer.setEnabled(transferLockService.getTransferLock().size() == 0);
        criteriaRow.addComponent(btnRegresyonTransfer);

        return dataTransferLayout;
    }


    private Component createInfo(EnvironmentType env) {

        IPstn_BbkService pstnBbkService = UtilsForSpring.getSingleBeanOfType(IPstn_BbkService.class);

        VerticalLayout layout = new VerticalLayout();
        layout.setWidth("100%");
        layout.addStyleName("background-transparent");


        FormLayout dbInfo = new FormLayout();
        dbInfo.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        layout.addComponent(dbInfo);

        HorizontalLayout wrap = new HorizontalLayout();
        wrap.setSpacing(true);
        wrap.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        wrap.setCaption("Ortam");
        wrap.setIcon(FontAwesome.DATABASE);
        dbInfo.addComponent(wrap);

        TextField txtDbName = new TextField();
        txtDbName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
        txtDbName.setValue(env.name());
        txtDbName.setEnabled(false);
        wrap.addComponent(txtDbName);

        TextField toplamVeri = new TextField("Toplam Veri");
        toplamVeri.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
        toplamVeri.setEnabled(false);
        toplamVeri.setValue(String.valueOf(pstnBbkService.getPstnTotalCountByEnvironment(env)));
        dbInfo.addComponent(toplamVeri);

        TextField kullanilanVeri = new TextField("Kullanılan Veri");
        kullanilanVeri.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
        kullanilanVeri.setEnabled(false);
        kullanilanVeri.setValue(String.valueOf(pstnBbkService.getPstnUsingCountByEnvironment(env)));
        dbInfo.addComponent(kullanilanVeri);

        return layout;
    }

    private void startDataTransfer(UI ui, EnvironmentType envType) {

        btnRegresyonTransfer.setEnabled(false);
        btnBugfixTransfer.setEnabled(false);


        /** kullanıcı arayüzden tekrar tekrar çalıştırmasın diye */
        transferLockService.saveTransferLock(new DataTransferLock(true));

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {


                for (int i = 1; i <= 500; i++) {

                    try {

                        logger.info("bekliyor... " + i);
                        TimeUnit.MILLISECONDS.sleep(80);


                        updateProgresBar(ui, i, 500);

                        Pstn_Bbk aa = new Pstn_Bbk();
                        aa.setPstn(i + "sadfasdfasdf");
                        aa.setBbk(i + "sadfasdfasdf");

                        logger.info("ekledi... " + i);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        continue;
                    }
                }

                transferLockService.deleteTransferLock();
                logger.info("transfer bitti, lock kaldırıldı.");

                btnBugfixTransfer.setEnabled(true);
                btnRegresyonTransfer.setEnabled(true);
                NotificationBar.success("Veri transfer işlemi tamamlandı!");
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

}
