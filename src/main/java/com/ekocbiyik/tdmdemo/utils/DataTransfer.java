package com.ekocbiyik.tdmdemo.utils;

import com.ekocbiyik.tdmdemo.enums.EnvironmentType;
import com.ekocbiyik.tdmdemo.enums.PstnStatus;
import com.ekocbiyik.tdmdemo.model.Pstn_Bbk;
import com.ekocbiyik.tdmdemo.view.sysadmin.SysAdminDashboardView;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by enbiya on 12.05.2017.
 */
public class DataTransfer {

    private static final String CLASS_FOR_NAME = "oracle.jdbc.driver.OracleDriver";
    private static final String USERNAME = "TDM";
    private static final String PASSWORD = "TDM_2016";
    private static final String TEST_URL = "jdbc:oracle:thin:@127.0.0.1:1521/XE";
    private static final String BUGFIX_URL = "jdbc:oracle:thin:@127.0.0.1:1521/XE";
    private static final String REGRESYON_URL = "jdbc:oracle:thin:@127.0.0.1:1521/XE";

    private static final Logger logger = Logger.getLogger(SysAdminDashboardView.class);

    public static List<Pstn_Bbk> getTransferData(EnvironmentType envType) {


        logger.info("Transfer " + envType.name() + " için başladı...");

        List<Pstn_Bbk> resultList = new ArrayList<>();
        String url = envType == EnvironmentType.BUGFIX ? BUGFIX_URL : REGRESYON_URL;

        try {

            Class.forName(CLASS_FOR_NAME);
            Connection conn = DriverManager.getConnection(url, USERNAME, PASSWORD);
            if (conn.isValid(100)) {

                String sql = "select * from t_pstn_bbk";
                Statement statement = conn.createStatement();
                ResultSet rs = statement.executeQuery(sql);

                while (rs.next()) {

                    Pstn_Bbk pb = new Pstn_Bbk();
                    pb.setPstn(rs.getString("pstn"));
                    pb.setBbk(rs.getString("bbk"));
                    pb.setEnvironmentType(envType);
                    pb.setDataType(rs.getString("data_type"));
                    pb.setHizmetTuru(null);
                    pb.setPstnStatus(PstnStatus.BOSTA);
                    pb.setTckNo(rs.getString("tckno"));
                    pb.setTelauraPstnNo(rs.getString("telaura_pstn_no"));
                    pb.setInUsefull(rs.getBoolean("is_usefull"));
                    pb.setDslBasvuru(rs.getBoolean("dsl_basvuru"));
                    pb.setCrmCustNo(rs.getString("crm_cust_no"));
                    pb.setInOutDoor(rs.getBoolean("in_out_door"));
                    pb.setTmsSantralId(rs.getString("tms_santral_id"));
                    pb.setXdslSantralId(rs.getString("xdsl_santral_id"));
                    pb.setInReserve(rs.getBoolean("in_reserve"));
                    pb.setCreationDate(rs.getDate("creation_date"));
                    pb.setExpireDate(rs.getDate("expire_date"));
                    pb.setOwner(null);
                    pb.setGercekTuzel(rs.getBoolean("gercek_tuzel"));

                    printLog(pb);
                    resultList.add(pb);
                }

                rs.close();
                statement.close();
                conn.close();

                logger.info("Transfer " + envType.name() + " için bitti.");
                logger.info("Toplam kayıt sayısı: " + resultList.size());
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            logger.info("Transfer işleminde hata oluştu! " + e.getMessage());

        } catch (SQLException e) {
            e.printStackTrace();
            logger.info("Transfer işleminde hata oluştu! " + e.getMessage());

        } finally {
            return resultList;
        }
    }

    private static void printLog(Pstn_Bbk p) {

        StringBuilder sb = new StringBuilder();

        sb.append(p.getPstn())
                .append(p.getBbk())
                .append(p.getTckNo())
                .append(p.getTelauraPstnNo())
                .append(p.getInUsefull())
                .append(p.getDslBasvuru())
                .append(p.getCrmCustNo())
                .append(p.getInOutDoor())
                .append(p.getTmsSantralId())
                .append(p.getXdslSantralId())
                .append(p.getInReserve())
                .append(p.getCreationDate())
                .append(p.getExpireDate())
                .append(p.getDataType())
                .append(p.getEnvironmentType())
                .append(p.getOwner())
                .append(p.getGercekTuzel());

        logger.info(sb.toString());
    }

}
