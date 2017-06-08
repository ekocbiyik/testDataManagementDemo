package com.ekocbiyik.tdmdemo;

import com.ekocbiyik.tdmdemo.event.DashboardEventBus;
import com.ekocbiyik.tdmdemo.service.IUserService;
import com.ekocbiyik.tdmdemo.utils.DBDefaultValues;
import com.ekocbiyik.tdmdemo.view.LoginView;
import com.google.common.eventbus.Subscribe;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.Page;
import com.vaadin.server.Page.BrowserWindowResizeEvent;
import com.vaadin.server.Page.BrowserWindowResizeListener;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import com.ekocbiyik.tdmdemo.enums.EUserRole;
import com.ekocbiyik.tdmdemo.event.DashboardEvent.BrowserResizeEvent;
import com.ekocbiyik.tdmdemo.event.DashboardEvent.CloseOpenWindowsEvent;
import com.ekocbiyik.tdmdemo.event.DashboardEvent.UserLoggedOutEvent;
import com.ekocbiyik.tdmdemo.event.DashboardEvent.UserLoginRequestedEvent;
import com.ekocbiyik.tdmdemo.model.User;
import com.ekocbiyik.tdmdemo.service.ICompanyService;
import com.ekocbiyik.tdmdemo.service.IDataTransferLockService;
import com.ekocbiyik.tdmdemo.service.IPstn_BbkService;
import com.ekocbiyik.tdmdemo.utils.ExpirePstnBbkUtils;
import com.ekocbiyik.tdmdemo.utils.UtilsForSpring;
import com.ekocbiyik.tdmdemo.view.MainView;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

import javax.servlet.annotation.WebServlet;
import java.util.Date;
import java.util.Locale;


/**
 * Created by enbiya on 15.12.2016.
 */
@Theme("tests-valo-facebook")
@Title("TDM-Demo")
public class MainUI extends UI {

    private final DashboardEventBus dashboardEventBus = new DashboardEventBus();
    private Logger logger = Logger.getLogger(MainUI.class);

    public static DashboardEventBus getDashboardEventBus() {
        return ((MainUI) getCurrent()).dashboardEventBus;
    }

    public static User getCurrentUser() {
        return VaadinSession.getCurrent().getAttribute(User.class);
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        if (false) {
            return;
        }

        /** Yapılacaklar
         *
         *  db'de kullanıcıya ait olmayan expireDate olan veriler var
         *  kullanıcılardan hangi pstnleri kullandıklarını iste
         *  data transfer classında start transfer metodunu düzenle
         *  dataValidation view içindeki pstnBbkService.save(data); satırını aç!
         *  sysadmin dashboard da yorum satırlarını kaldır
         *  kullanım kılavuzu ekle
         *
         *
         * */

        logger.info("Program çalıştı.");

        // DB default değerlerini init et
        DBDefaultValues.initialize();


        setLocale(Locale.US);
        DashboardEventBus.register(this);
        Responsive.makeResponsive(this);
        addStyleName(ValoTheme.UI_WITH_MENU);

        updateContent();

        Page.getCurrent().addBrowserWindowResizeListener(new BrowserWindowResizeListener() {
            @Override
            public void browserWindowResized(BrowserWindowResizeEvent event) {
                DashboardEventBus.post(new BrowserResizeEvent());
            }
        });
    }

    private void updateContent() {

        /**
         * menüler oluşturulurken aşağıdaki iki yerde role bakarak oluşturuyor
         * dashboardNavigator -> initViewProviders
         * dashboardMenu -> buildMenuItems
         * */

        User user = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
        if (user != null) {
            // Authenticated user
            setContent(new MainView());
            removeStyleName("loginview");
            getNavigator().navigateTo(getNavigator().getState());
        } else {
            setContent(new LoginView());
            addStyleName("loginview");
        }
    }

    @Subscribe
    public void closeOpenWindows(final CloseOpenWindowsEvent event) {
        for (Window window : getWindows()) {
            window.close();
        }
    }

    @Subscribe
    public void userLoginRequested(final UserLoginRequestedEvent event) {

        String userName = event.getUserName();
        String password = event.getPassword();

        User user = UtilsForSpring.getSingleBeanOfType(IUserService.class).login(userName, password);
        if (user == null) {
            Notification.show("Yanlış kullanıcı adı veya parola!", Type.TRAY_NOTIFICATION);
            return;
        }


        IDataTransferLockService lockService = UtilsForSpring.getSingleBeanOfType(IDataTransferLockService.class);
        if (lockService.getTransferLock().size() > 0 && user.getRole() != EUserRole.SYSADMIN) {

            Notification.show("Uyarı!", "Bakım çalışması olduğu için sisteme erişim geçici olarak durdurulmuştur.", Type.HUMANIZED_MESSAGE);
            return;
        }

        VaadinSession.getCurrent().setAttribute(User.class.getName(), user);

        Notification notif = new Notification("Hoş geldiniz " + user.getFirstName() + " " + user.getLastName(), Type.HUMANIZED_MESSAGE);
        notif.setDelayMsec(3000);
        notif.setPosition(Position.TOP_RIGHT);
        notif.show(Page.getCurrent());

        updateContent();
    }

    @Subscribe
    public void userLoggedOut(final UserLoggedOutEvent event) {
        VaadinSession.getCurrent().close();
        Page.getCurrent().reload();
    }

    @Scheduled(cron = "0 0 23 * * *")
//    @Scheduled(cron = "*/60 * * * * *")
    public void clearReservedPstnBbk() {

        /** Bu metot reserve aşamasında kalan bbk'ları belli bir saatte temizlemek için çalışacak
         *  tetiklendiği yer için hibernate-config içine bakabilirsin
         * */

//        "0 0 * * * *" = the top of every hour of every day.
//        "*/10 * * * * *" = every ten seconds.
//        "0 0 8-10 * * *" = 8, 9 and 10 o'clock of every day.
//        "0 0 6,19 * * *" = 6:00 AM and 7:00 PM every day.
//        "0 0/30 8-10 * * *" = 8:00, 8:30, 9:00, 9:30, 10:00 and 10:30 every day.
//        "0 0 9-17 * * MON-FRI" = on the hour nine-to-five weekdays
//        "0 0 0 25 12 ?" = every Christmas Day at midnight

        logger.info("Reserve aşamasındaki Pstn/Bbk bilgileri temizlenecek: " + new Date());
        UtilsForSpring.getSingleBeanOfType(IPstn_BbkService.class).clearInReservePstnBbk();
        logger.info("Reserve aşamasındaki Pstn/Bbk bilgileri temizlendi: " + new Date());


        logger.info("******************************************** Kullanım süresi dolmuş bbk/pstn'ler silinecek! *********************************************");
        ExpirePstnBbkUtils.startTask();
        logger.info("******************************************** Kullanım süresi dolmuş bbk/pstn'ler silindi! *********************************************");

    }


    @Scheduled(cron = "0 0/5 * * * *")
    public void testDbConnection() {

        logger.info("DB connection test yapılacak: " + new Date());
        logger.info("ping database: " + UtilsForSpring.getSingleBeanOfType(ICompanyService.class).getCompanies() != null ? "Successful" : "Failed");
        logger.info("DB connection test yapıldı: " + new Date());

    }

    @Bean
    public TaskScheduler taskScheduler() {
        return new ConcurrentTaskScheduler();
    }

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = true, ui = MainUI.class)
    public static class Servlet extends VaadinServlet {
    }

}
