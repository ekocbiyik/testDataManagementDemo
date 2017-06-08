package com.ekocbiyik.tdmdemo.view.dashboard;

import com.ekocbiyik.tdmdemo.MainUI;
import com.ekocbiyik.tdmdemo.event.DashboardEventBus;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.ekocbiyik.tdmdemo.enums.EUserRole;
import com.ekocbiyik.tdmdemo.view.admin.AdminDashboardView;
import com.ekocbiyik.tdmdemo.view.sysadmin.SysAdminDashboardView;
import com.ekocbiyik.tdmdemo.view.testeng.TestEngDashboardView;

/**
 * Created by enbiya on 15.12.2016.
 */
public class DashboardView extends Panel implements View {

    private VerticalLayout root;
    private VerticalLayout dashboardPanels;

    public DashboardView() {

        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();
        DashboardEventBus.register(this);
        Responsive.makeResponsive(this);
        setContent(buildContent());

    }

    private Component buildContent() {


        /** Dashboard menüsü tüm yetki tipleri için ortak olacak,
         *  fakat yetki tipine göre içerik farklı olacak. Bu yüzden yetki tipine göre hangi
         *  içeriğin geleceğini burada beliremek gerekir.
         *
         *  sysadmin ise -> new sysadmin()
         *  admin ise -> new admin()
         *  user ise -> new user()
         * */

        dashboardPanels = new VerticalLayout();
        dashboardPanels.setSpacing(true);
        dashboardPanels.setMargin(true);
        dashboardPanels.setHeight("100%");
        Responsive.makeResponsive(dashboardPanels);

        EUserRole role = MainUI.getCurrentUser().getRole();

        Component content;
        if (role == EUserRole.SYSADMIN) {
            content = new SysAdminDashboardView().getSysAdminDashboard();
        } else if (role == EUserRole.ADMIN) {
            content = new AdminDashboardView().getAdminDashboard();
        } else {
            content = new TestEngDashboardView().getTestEngDashboard();
        }

        dashboardPanels.addComponent(content);

        return dashboardPanels;
    }

    @Override
    public void enter(ViewChangeEvent viewChangeEvent) {
    }
}
