package com.ekocbiyik.tdmdemo.view.admin;

import com.ekocbiyik.tdmdemo.MainUI;
import com.ekocbiyik.tdmdemo.enums.EnvironmentType;
import com.ekocbiyik.tdmdemo.service.IPstn_BbkService;
import com.ekocbiyik.tdmdemo.utils.UtilsForSpring;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.ekocbiyik.tdmdemo.model.User;
import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.util.Rotation;
import org.vaadin.addon.JFreeChartWrapper;

import java.awt.*;

/**
 * Created by enbiya on 18.12.2016.
 */
public class AdminDashboardView extends VerticalLayout {

    private Logger logger = Logger.getLogger(AdminDashboardView.class);


    public Component getAdminDashboard() {

        setSpacing(false);
        setSizeFull();
        Responsive.makeResponsive(this);

        Label titleLabel = new Label("Dashboard");
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        addComponent(titleLabel);
        addComponent(new Label("<hr/>", ContentMode.HTML));

        Component c = getPstnBbkChart();
        addComponent(c);
        setExpandRatio(c, 1);

        return this;
    }

    public Component getPstnBbkChart() {

        HorizontalLayout layout = new HorizontalLayout();
        layout.setSizeFull();

        DefaultPieDataset dataset = new DefaultPieDataset();

        IPstn_BbkService pstnService = UtilsForSpring.getSingleBeanOfType(IPstn_BbkService.class);
        User user = MainUI.getCurrentUser();

        int testCount = pstnService.getPstnBbkCountByEnvironment(EnvironmentType.TEST, user);
        int regresyonCount = pstnService.getPstnBbkCountByEnvironment(EnvironmentType.REGRESYON, user);
        int bugfixCount = pstnService.getPstnBbkCountByEnvironment(EnvironmentType.BUGFIX, user);

        dataset.setValue("Test: " + testCount, new Double(testCount));
        dataset.setValue("Regresyon: " + regresyonCount, new Double(regresyonCount));
        dataset.setValue("Bugfix: " + bugfixCount, new Double(bugfixCount));

        JFreeChart piChart = ChartFactory.createPieChart3D("Ortam/Veri Dağılımı", dataset, true, true, true);
        piChart.setBorderVisible(false);
        piChart.setBackgroundPaint(null);

        Font font = new Font("Sans-serif", Font.BOLD, 20);
        piChart.getTitle().setPaint(Color.WHITE);
        piChart.getTitle().setFont(font);
        piChart.getLegend().setItemPaint(Color.BLACK);

        final PiePlot3D plot = (PiePlot3D) piChart.getPlot();
        plot.setStartAngle(61);
        plot.setBackgroundPaint(null);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.5f);
        plot.setNoDataMessage("No data to display");
        plot.setLabelBackgroundPaint(Color.white);


        JFreeChartWrapper wrapper = new JFreeChartWrapper(piChart);
        wrapper.setWidth("500");
        wrapper.setHeight("350");


        layout.addComponent(wrapper);

        return layout;
    }

}
