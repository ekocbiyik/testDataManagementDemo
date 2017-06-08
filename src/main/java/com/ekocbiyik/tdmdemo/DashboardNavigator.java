package com.ekocbiyik.tdmdemo;

import com.ekocbiyik.tdmdemo.event.DashboardEvent.BrowserResizeEvent;
import com.ekocbiyik.tdmdemo.event.DashboardEvent.CloseOpenWindowsEvent;
import com.ekocbiyik.tdmdemo.event.DashboardEvent.PostViewChangeEvent;
import com.ekocbiyik.tdmdemo.event.DashboardEventBus;
import com.ekocbiyik.tdmdemo.view.DashboardViewType;
import com.ekocbiyik.tdmdemo.model.User;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;
import org.vaadin.googleanalytics.tracking.GoogleAnalyticsTracker;

import java.util.List;

/**
 * Created by enbiya on 15.12.2016.
 */
public class DashboardNavigator extends Navigator {

    private static final String TRACKER_ID = null;
    private static final DashboardViewType ERROR_VIEW = DashboardViewType.DASHBOARD;
    private GoogleAnalyticsTracker tracker;
    private ViewProvider errorViewProvider;

    public DashboardNavigator(final ComponentContainer container) {

        super(UI.getCurrent(), container);

        String host = getUI().getPage().getLocation().getHost();
        if (TRACKER_ID != null && host.endsWith("localhost")) {
            initGATracker(TRACKER_ID);
        }

        initViewChangeListener();
        initViewProviders();
    }

    private void initGATracker(final String trackerId) {

        // TODO: 15.12.2016 burası ne bilmiyorum, fakat değişmeyi unutma
        tracker = new GoogleAnalyticsTracker(trackerId, "localhost");
        tracker.extend(UI.getCurrent());
    }

    private void initViewChangeListener() {

        addViewChangeListener(new ViewChangeListener() {
            @Override
            public boolean beforeViewChange(ViewChangeEvent event) {
                return true;
            }

            @Override
            public void afterViewChange(ViewChangeEvent event) {

                DashboardViewType view = DashboardViewType.getByViewName(event.getViewName());
                DashboardEventBus.post(new PostViewChangeEvent(view));
                DashboardEventBus.post(new BrowserResizeEvent());
                DashboardEventBus.post(new CloseOpenWindowsEvent());

                if (tracker != null) {
//                    tracker.trackPageview("/enbiya/"+event.getViewName());
                    tracker.trackPageview(event.getViewName());
                }

            }
        });

    }

    private void initViewProviders() {

        /** burada o anki user'ın rolünü al, o role ait menüleri getir */
        User user = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
        List<DashboardViewType> userRoleViews = DashboardViewType.getViewsByRole(user.getRole());

        for (final DashboardViewType viewType : userRoleViews) {
            ViewProvider viewProvider = new ClassBasedViewProvider(viewType.getViewName(), viewType.getViewClass()) {

                private View cachedInstance;

                @Override
                public View getView(String viewName) {

                    View result = null;
                    if (viewType.getViewName().equals(viewName)) {
                        if (viewType.isStateful()) {

                            if (cachedInstance == null) {
                                cachedInstance = super.getView(viewType.getViewName());
                            }
                            result = cachedInstance;
                        } else {
                            result = super.getView(viewType.getViewName());
                        }
                    }
                    return result;
                }
            };

            if (viewType == ERROR_VIEW) {
                errorViewProvider = viewProvider;
            }

            addProvider(viewProvider);
        }

        setErrorProvider(new ViewProvider() {
            @Override
            public String getViewName(final String s) {
                return ERROR_VIEW.getViewName();
            }

            @Override
            public View getView(final String s) {
                return errorViewProvider.getView(ERROR_VIEW.getViewName());
            }
        });


    }

}
