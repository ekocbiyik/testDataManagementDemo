package com.ekocbiyik.tdmdemo.view.admin;

import com.ekocbiyik.tdmdemo.MainUI;
import com.ekocbiyik.tdmdemo.event.DashboardEventBus;
import com.ekocbiyik.tdmdemo.service.IUserService;
import com.ekocbiyik.tdmdemo.windows.UserEditWindow;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.util.BeanItemContainer;
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
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import com.ekocbiyik.tdmdemo.model.User;
import com.ekocbiyik.tdmdemo.utils.UtilsForSpring;

import java.util.List;

/**
 * Created by enbiya on 18.12.2016.
 */
public class ManageUserView extends Panel implements View {


    private Table filterTable;
    private TextField txtUsernameFilter = new TextField();

    public ManageUserView() {

        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();
        DashboardEventBus.register(this);
        Responsive.makeResponsive(this);


        VerticalLayout content = new VerticalLayout();
        content.setSpacing(true);
        content.setMargin(true);
        content.setHeight("100%");
        Responsive.makeResponsive(content);
        setContent(content);

        content.addComponent(buildContent());
    }

    private Component buildContent() {

        VerticalLayout tableLayout = new VerticalLayout();
        tableLayout.setSpacing(false);
        tableLayout.setSizeFull();
        Responsive.makeResponsive(tableLayout);

        Label titleLabel = new Label("Kullanıcıları Yönet");
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        tableLayout.addComponent(titleLabel);
        tableLayout.addComponent(new Label("<hr/>", ContentMode.HTML));

        tableLayout.addComponent(getFilterPanel());
        tableLayout.addComponent(new Label("</br>", ContentMode.HTML));


        Component c = getFilterTable();
        tableLayout.addComponent(c);
        tableLayout.setExpandRatio(c, 1);

        return tableLayout;
    }

    private Component getFilterPanel() {

        HorizontalLayout layout = new HorizontalLayout();
        layout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        layout.setSpacing(true);

        txtUsernameFilter.setInputPrompt("Username, Ad, Soyad, Rol Türü...");
        txtUsernameFilter.addStyleName(ValoTheme.TEXTFIELD_TINY);
        txtUsernameFilter.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        txtUsernameFilter.setIcon(FontAwesome.SEARCH);
        txtUsernameFilter.setWidth("100%");
        txtUsernameFilter.addTextChangeListener(new TextChangeListener() {
            @Override
            public void textChange(TextChangeEvent textChangeEvent) {

                String key = textChangeEvent.getText().trim();
                Filterable c = (Filterable) filterTable.getContainerDataSource();
                c.removeAllContainerFilters();

                if (!key.equals("")) {
                    c.addContainerFilter(new Or(
                            new SimpleStringFilter("username", key, true, false),
                            new SimpleStringFilter("firstName", key, true, false),
                            new SimpleStringFilter("lastName", key, true, false),
                            new SimpleStringFilter("role", key, true, false)
                    ));
                }
            }
        });

        layout.addComponent(txtUsernameFilter);
        return layout;
    }

    private Component getFilterTable() {

        if (filterTable == null) {

            User user = MainUI.getCurrentUser();

            List<User> allUsers = UtilsForSpring.getSingleBeanOfType(IUserService.class).getUsersForAdmin(user);
            BeanItemContainer<User> itemContainer = new BeanItemContainer<User>(User.class);
            itemContainer.addAll(allUsers);

            filterTable = new Table();
            filterTable.setContainerDataSource(itemContainer);
            filterTable.setSizeFull();
            filterTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
            filterTable.setSelectable(true);
            filterTable.setMultiSelect(false);
            filterTable.setImmediate(true);
            filterTable.setColumnCollapsingAllowed(true);
            Responsive.makeResponsive(filterTable);

            filterTable.addGeneratedColumn("button", new ColumnGenerator() {
                @Override
                public Object generateCell(Table source, Object itemId, Object columnId) {

                    Button btn = new Button("Güncelle", new ClickListener() {
                        @Override
                        public void buttonClick(ClickEvent clickEvent) {

                            Window w = new UserEditWindow((User) itemId, filterTable);
                            UI.getCurrent().addWindow(w);
                            w.focus();
                        }
                    });

                    btn.addStyleName(ValoTheme.BUTTON_SMALL);

                    User currentUser = MainUI.getCurrentUser();
                    if (currentUser.getUserId() == ((User) itemId).getUserId()) {
                        btn.setEnabled(false);
                        btn.setDescription("Kullanıcı kendi bilgilerini güncelleyemez!");
                    }

                    return btn;
                }
            });

            filterTable.setVisibleColumns(new Object[]{"username", "firstName", "lastName", "role", "creationDate", "button"});
            filterTable.setColumnCollapsible("username", false);
            filterTable.setColumnHeaders("Kullanıcı Adı", "Adı", "Soyadı", "Yetki Türü", "Oluşturulma Tarihi", "");
        }

        return filterTable;
    }

    @Override
    public void enter(ViewChangeEvent viewChangeEvent) {
    }

}
