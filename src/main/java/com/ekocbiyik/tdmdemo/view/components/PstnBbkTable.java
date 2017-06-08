package com.ekocbiyik.tdmdemo.view.components;

import com.ekocbiyik.tdmdemo.MainUI;
import com.ekocbiyik.tdmdemo.model.Pstn_Bbk;
import com.ekocbiyik.tdmdemo.service.IPstn_BbkService;
import com.ekocbiyik.tdmdemo.windows.PstnBbkUpdateWindow;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import com.ekocbiyik.tdmdemo.utils.UtilsForSpring;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by enbiya on 26.05.2017.
 */
public class PstnBbkTable extends Table {


    private BeanItemContainer<Pstn_Bbk> itemContainer;

    public PstnBbkTable() {

        Responsive.makeResponsive(this);
        setSizeFull();
        addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        setSelectable(true);
        setMultiSelect(false);
        setImmediate(true);
        setColumnCollapsingAllowed(true);

        addGeneratedColumn("button", new ColumnGenerator() {
            @Override
            public Object generateCell(Table table, Object o, Object o1) {

                Button btn = new Button("Düzenle");
                btn.addClickListener(new ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent clickEvent) {

                        Window w = new PstnBbkUpdateWindow((Pstn_Bbk) o, (PstnBbkTable) table);
                        UI.getCurrent().addWindow(w);
                        w.focus();
                    }
                });
                return btn;
            }
        });

        setCellStyleGenerator(new CellStyleGenerator() {
            @Override
            public String getStyle(Table table, Object o, Object o1) {
                if (o == null) {
                    return null;
                }
                if (isExpireDate(((Pstn_Bbk) o).getExpireDate())) {
                    return "highlight-red";
                }
                return null;
            }
        });

        /** table container */
        List<Pstn_Bbk> pstn_bbkList = UtilsForSpring.getSingleBeanOfType(IPstn_BbkService.class).getUserPstnBbkList(MainUI.getCurrentUser());
        itemContainer = new BeanItemContainer<Pstn_Bbk>(Pstn_Bbk.class);
        itemContainer.addNestedContainerProperty("owner.username");
        itemContainer.addAll(pstn_bbkList);
        setContainerDataSource(itemContainer);

        /** table columns */
        setVisibleColumns(new Object[]{
                "pstn", "bbk", "dataType", "environmentType", "hizmetTuru",
                "tckNo", "telauraPstnNo", "dslBasvuru", "crmCustNo",
                "inOutDoor", "tmsSantralId", "xdslSantralId", "creationDate",
                "expireDate", "gercekTuzel", "button"});

        setColumnHeaders("Pstn", "Bbk", "Veri Tipi", "Ortam Türü", "Hizmet Türü",
                "TckNo", "Telaura Pstn", "DSL Başvuru", "CRM Cus. No.",
                "InDoor/OutDoor", "TMS Santral Id", "XDSL Santral Id", "Oluşturma Tar.",
                "Bitiş Tar.", "Gerçek/Tüzel", "_");

        //..
        setColumnCollapsed("tckNo", true);
        setColumnCollapsed("dslBasvuru", true);
        setColumnCollapsed("crmCustNo", true);
        setColumnCollapsed("inOutDoor", true);
        setColumnCollapsed("creationDate", true);
        setColumnCollapsed("gercekTuzel", true);
    }

    @Override
    protected String formatPropertyValue(Object rowId, Object colId, Property<?> property) {
        Object v = property.getValue();

        if (v != null) {

            if (v instanceof Date) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                return sdf.format((Date) v);
            }

            if ("dslBasvuru".equals(colId)) {
                return (Boolean) v == true ? "Var" : "Yok";
            }

            if ("gercekTuzel".equals(colId)) {
                return (Boolean) v == true ? "Gerçek" : "Tüzel";
            }

            if ("inOutDoor".equals(colId)) {
                return (Boolean) v == true ? "In Door" : "Out Door";
            }
        }

        return super.formatPropertyValue(rowId, colId, property);
    }

    private boolean isExpireDate(Date expireDate) {

        if (expireDate != null) {
            long diff = expireDate.getTime() - new Date().getTime();

            if (diff <= 259200000) {
                return true;
            }
        }

        return false;
    }

    public BeanItemContainer<Pstn_Bbk> getItemContainer() {
        return itemContainer;
    }

    public void setItemContainer(BeanItemContainer<Pstn_Bbk> itemContainer) {
        this.itemContainer = itemContainer;
    }

}
