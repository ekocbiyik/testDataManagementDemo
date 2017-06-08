package com.ekocbiyik.tdmdemo.windows;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import com.ekocbiyik.tdmdemo.MainUI;
import com.ekocbiyik.tdmdemo.enums.EUserRole;
import com.ekocbiyik.tdmdemo.model.Pstn_Bbk;
import com.ekocbiyik.tdmdemo.service.IPstn_BbkService;
import com.ekocbiyik.tdmdemo.utils.UtilsForSpring;
import com.ekocbiyik.tdmdemo.view.components.NotificationBar;
import com.ekocbiyik.tdmdemo.view.components.PstnBbkTable;

import java.util.Date;
import java.util.List;

/**
 * Created by enbiya on 26.05.2017.
 */
public class PstnBbkUpdateWindow extends Window {

    private Pstn_Bbk pstn_bbk;
    private PstnBbkTable pstnBbkTable;


    public PstnBbkUpdateWindow(Pstn_Bbk pstn_bbk, PstnBbkTable pstnBbkTable) {

        this.pstn_bbk = pstn_bbk;
        this.pstnBbkTable = pstnBbkTable;

        setCaption("BBK/PSTN Onaylama Ekranı");
        setModal(true);
        setResizable(true);
        addCloseShortcut(KeyCode.ESCAPE);
        setContent(buildContent());
    }

    private Component buildContent() {

        VerticalLayout content = new VerticalLayout();
        content.setWidth("100%");
        content.setMargin(true);
        content.setSpacing(true);

        Label section = new Label("Bitiş Tarihi");
        section.addStyleName(ValoTheme.LABEL_H4);
        section.addStyleName(ValoTheme.LABEL_COLORED);
        content.addComponent(section);

        HorizontalLayout wrapRow = new HorizontalLayout();
        wrapRow.setWidth("100%");
        wrapRow.setSpacing(false);
        content.addComponent(wrapRow);

        DateField dateField = new DateField();
        dateField.setDateFormat("dd/MM/yyyy");
        dateField.setWidth("140px");
        dateField.setRangeStart(new Date());
        dateField.setValue(pstn_bbk.getExpireDate());
        dateField.setInvalidAllowed(true);
        dateField.setRequired(true);
        wrapRow.addComponent(dateField);

        Button btnUpdate = new Button("Güncelle", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {

                if (dateField.getValue() == null) {
                    Notification.show("Lütfen bitiş tarihi belirtin!", Type.TRAY_NOTIFICATION);
                    return;
                }

                IPstn_BbkService bbkService = UtilsForSpring.getSingleBeanOfType(IPstn_BbkService.class);
                Date endDate = dateField.getValue();
                pstn_bbk.setExpireDate(endDate);//ekrandan gelen bbk

                /** burada bbk ya ait gizli pstnleri update et (varsa) */
                List<Pstn_Bbk> hiddenPstnList = bbkService.getUsersAllHiddenPstnByBbk(pstn_bbk.getOwner(), pstn_bbk);
                for (Pstn_Bbk i : hiddenPstnList) {
                    i.setExpireDate(endDate);
                    bbkService.save(i);
                }
                /** bbk nın orjinalini update et */
                bbkService.save(pstn_bbk);

                pstnBbkTable.refreshRowCache();
                NotificationBar.success("Işlem başarıyla tamamlandı!");
                close();
            }
        });


        btnUpdate.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        wrapRow.addComponent(btnUpdate);
        wrapRow.setComponentAlignment(btnUpdate, Alignment.BOTTOM_RIGHT);

        Label lblSpace = new Label();
        lblSpace.setWidth("20px");
        wrapRow.addComponent(lblSpace);


        Button btnDelete = new Button("Sil", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {

                IPstn_BbkService pstnBbkService = UtilsForSpring.getSingleBeanOfType(IPstn_BbkService.class);
                pstnBbkService.removeUserPstnBbk(pstn_bbk.getOwner(), pstn_bbk);
                pstnBbkTable.removeItem(pstn_bbk);

                pstnBbkTable.refreshRowCache();
                NotificationBar.success("Işlem başarıyla tamamlandı!");
                close();
            }
        });

        btnDelete.addStyleName(ValoTheme.BUTTON_DANGER);
        btnDelete.setVisible(MainUI.getCurrentUser().getRole() == EUserRole.ADMIN);
        wrapRow.addComponent(btnDelete);
        wrapRow.setComponentAlignment(btnDelete, Alignment.BOTTOM_RIGHT);

        Grid bbkGrid = new Grid();
        bbkGrid.setHeight("40%");
        bbkGrid.addColumn("BBK");
        bbkGrid.addColumn("PSTN");
        bbkGrid.addRow(pstn_bbk.getBbk(), pstn_bbk.getPstn());
        content.addComponent(bbkGrid);

        return content;
    }

}
