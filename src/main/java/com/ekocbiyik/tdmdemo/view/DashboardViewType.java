package com.ekocbiyik.tdmdemo.view;

import com.ekocbiyik.tdmdemo.view.common.TestDataView;
import com.ekocbiyik.tdmdemo.view.dashboard.DashboardView;
import com.ekocbiyik.tdmdemo.view.sysadmin.DataValidationView;
import com.ekocbiyik.tdmdemo.view.sysadmin.NewUserView;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.ekocbiyik.tdmdemo.enums.EUserRole;
import com.ekocbiyik.tdmdemo.view.admin.AdminDataView;
import com.ekocbiyik.tdmdemo.view.admin.AllUsersDataView;
import com.ekocbiyik.tdmdemo.view.admin.CreateUserView;
import com.ekocbiyik.tdmdemo.view.admin.ManageUserView;
import com.ekocbiyik.tdmdemo.view.common.UserGuideView;
import com.ekocbiyik.tdmdemo.view.sysadmin.NewCompanyView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by enbiya on 15.12.2016.
 */
public enum DashboardViewType {

    /**
     * tüm menüler burada olsun login olduktan sonra yetkiye göre menüleri manuel olarak oluştur
     */
    DASHBOARD("dashboard", DashboardView.class, FontAwesome.HOME, false, "Anasayfa"),

    //SYSADMIN panelleri
    NEW_COMPANY("newcompany", NewCompanyView.class, FontAwesome.ARROW_CIRCLE_O_RIGHT, false, "Yeni Firma"),
    NEW_USER("newuser", NewUserView.class, FontAwesome.ARROW_CIRCLE_O_RIGHT, false, "Yeni Kullanıcı"),
    DATA_VALIDATION("datavalidation", DataValidationView.class, FontAwesome.ARROW_CIRCLE_O_RIGHT, false, "Veri Doğrulama"),
//    NEW_INFRASTRUCTURE_TYPE("newinfrastructuretype", NewInfrastructureTypeView.class, FontAwesome.ARROW_CIRCLE_O_RIGHT, false, "Altyapı Tipi Ekle"),

    //ADMIN panelleri
    CREATE_USER("createuser", CreateUserView.class, FontAwesome.ARROW_CIRCLE_O_RIGHT, false, "Kullanıcı Oluştur"),
    MANAGE_USER("manageuser", ManageUserView.class, FontAwesome.ARROW_CIRCLE_O_RIGHT, false, "Kullanıcıları Yönet"),
    USER_DATA("userdata", AllUsersDataView.class, FontAwesome.ARROW_CIRCLE_O_RIGHT, false, "Kullanıcı Verileri"),
    ADMIN_DATA("admindata", AdminDataView.class, FontAwesome.ARROW_CIRCLE_O_RIGHT, false, "Bana ait Veriler"),
//    NEW_INFRASTRUCTURE_VALUE("newinfrastructurevalue", NewInfrastructureValueView.class, FontAwesome.ARROW_CIRCLE_O_RIGHT, false, "Altyapı Değeri Ekle"),

    //TESTENG panelleri
    TEST("test", TestDataView.class, FontAwesome.ARROW_CIRCLE_O_RIGHT, false, "Veri listeleme"),
    USER_GUIDE("userguide", UserGuideView.class, FontAwesome.QUESTION_CIRCLE, false, "Kullanım Yönergesi");


    private final String viewName;
    private final Class<? extends View> viewClass;
    private final Resource icon;
    private final boolean stateful;
    private final String caption;

    DashboardViewType(final String viewName, final Class<? extends View> viewClass, final Resource icon, final boolean stateful, final String caption) {
        this.viewName = viewName;
        this.viewClass = viewClass;
        this.icon = icon;
        this.stateful = stateful;
        this.caption = caption;
    }

    public static DashboardViewType getByViewName(final String viewName) {
        DashboardViewType result = null;
        for (DashboardViewType viewType : values()) {
            if (viewType.getViewName().equals(viewName)) {
                result = viewType;
                break;
            }
        }
        return result;
    }

    public static List<DashboardViewType> getViewsByRole(EUserRole userRole) {

        List<DashboardViewType> userRoleViews = null;

        if (userRole == EUserRole.SYSADMIN) {
            userRoleViews = DashboardViewType.getSysAdminViews();
        } else if (userRole == EUserRole.ADMIN) {
            userRoleViews = DashboardViewType.getAdminViews();
        } else if (userRole == EUserRole.TEST_ENG) {
            userRoleViews = DashboardViewType.getTestEngViews();
        }

        return userRoleViews;
    }

    private static List<DashboardViewType> getSysAdminViews() {

        List<DashboardViewType> sysAdminViews = new ArrayList<>();
        sysAdminViews.add(DASHBOARD);
        sysAdminViews.add(NEW_COMPANY);
        sysAdminViews.add(NEW_USER);
        sysAdminViews.add(DATA_VALIDATION);
        sysAdminViews.add(USER_GUIDE);

        return sysAdminViews;
    }

    private static List<DashboardViewType> getAdminViews() {

        List<DashboardViewType> adminViews = new ArrayList<>();
        adminViews.add(DASHBOARD);
        adminViews.add(CREATE_USER);
        adminViews.add(MANAGE_USER);
        adminViews.add(USER_DATA);
        adminViews.add(TEST);
        adminViews.add(ADMIN_DATA);
        adminViews.add(USER_GUIDE);

        return adminViews;
    }

    private static List<DashboardViewType> getTestEngViews() {

        List<DashboardViewType> testEngViews = new ArrayList<>();
        testEngViews.add(DASHBOARD);
        testEngViews.add(TEST);
        testEngViews.add(USER_GUIDE);

        return testEngViews;
    }

    public String getViewName() {
        return viewName;
    }

    public Class<? extends View> getViewClass() {
        return viewClass;
    }

    public Resource getIcon() {
        return icon;
    }

    public boolean isStateful() {
        return stateful;
    }

    public String getCaption() {
        return caption;
    }
}
