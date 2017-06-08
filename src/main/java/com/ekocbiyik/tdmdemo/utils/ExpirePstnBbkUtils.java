package com.ekocbiyik.tdmdemo.utils;

import com.ekocbiyik.tdmdemo.enums.EnvironmentType;
import com.ekocbiyik.tdmdemo.enums.PstnStatus;
import com.ekocbiyik.tdmdemo.model.Pstn_Bbk;
import com.ekocbiyik.tdmdemo.service.IPstn_BbkService;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by enbiya on 05.06.2017.
 */
public class ExpirePstnBbkUtils {

    private static final String TEST_URL = "http://127.0.0.1:1522/services/FiberQueryExtensionWS?WSDL";
    private static final String REGRESYON_URL = "127.0.0.1:1523/services/FiberQueryExtensionWS?WSDL";
    private static final String BUGFIX_URL = "127.0.0.1:1524/services/FiberQueryExtensionWS?WSDL";

    private static Logger logger = Logger.getLogger(ExpirePstnBbkUtils.class);

    public static void startTask() {

        /**
         * süresi dolmuş bbkları listele
         * döngü ile -> önce web servis ile kullanılabilir mi kontrol et?
         *              kullanım durumuna göre inUseful true/false yap
         *
         *              hidden pstnlerini getir -> eğer varsa döngü ile expireDate, creationDate, owner = null PstnSatus=BOSTA yap kaydet
         *              son olarak asıl bbk yı expireDate, creationDate, owner = null PstnSatus=BOSTA yap kaydet
         *
         * burada kriter inUseful=true olması,
         * kullanıcılara data verilirken bu kritere bakılıyor. false ise verilmiyor.
         *
         * */
        IPstn_BbkService bbkService = UtilsForSpring.getSingleBeanOfType(IPstn_BbkService.class);


        List<Pstn_Bbk> expiredBbkList = bbkService.getExpiredBbkList();

        for (Pstn_Bbk pstnBbk : expiredBbkList) {

            /** bbkya ait pstn varsa getir, güncelle */
            List<Pstn_Bbk> hiddenPstnlist = bbkService.getHiddenPstnByBbk(pstnBbk);
            for (Pstn_Bbk h : hiddenPstnlist) {
                h.setExpireDate(null);
                h.setCreationDate(null);
                h.setPstnStatus(PstnStatus.BOSTA);
                h.setOwner(null);
                bbkService.save(h);
                logger.info("Bbk: " + h.getBbk() + " Pstn: " + h.getPstn() + " updated successfully!");
            }

            /** bbknın kendisini güncelle */
            pstnBbk.setInUsefull(isUseful(pstnBbk)); // burası kritik!!!
            pstnBbk.setOwner(null);
            pstnBbk.setExpireDate(null);
            pstnBbk.setCreationDate(null);
            pstnBbk.setPstnStatus(PstnStatus.BOSTA);
            bbkService.save(pstnBbk);
            logger.info("Bbk: " + pstnBbk.getBbk() + " Pstn: " + pstnBbk.getPstn() + " updated successfully!");
        }

    }

    public static boolean isUseful(Pstn_Bbk pstnBbk) {

        /** burada pstn üzerinden kontrol etmek gerekir */

        boolean result = true;// defaultta true olsun, webserviste sıkıntı varsa tüm dataları false diye işaretlemesin

        try {

            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = null;

            if (EnvironmentType.TEST == pstnBbk.getEnvironmentType()) {
                httpPost = new HttpPost(TEST_URL);
            } else if (EnvironmentType.REGRESYON == pstnBbk.getEnvironmentType()) {
                httpPost = new HttpPost(REGRESYON_URL);
            } else {
                httpPost = new HttpPost(BUGFIX_URL);
            }

            httpPost.addHeader("Accept-Encoding", "gzip,deflate");
            httpPost.addHeader("Content-Type", "text/xml;charset=UTF-8");
            httpPost.addHeader("SOAPAction", "\"\"");
            httpPost.addHeader("Authorization", "Basic OTg3MzQ5OjEyMzRhcw==");
            httpPost.addHeader("Host", "10.6.152.1:9201");
            httpPost.addHeader("Connection", "Keep-Alive");
            httpPost.addHeader("User-Agent", "Apache-HttpClient/4.1.1 (java 1.5)");

            httpPost.setEntity(new StringEntity(getRequestBody(pstnBbk)));//sorgular pstn'e göre

            CloseableHttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {

                List<String> responseList = IOUtils.readLines(response.getEntity().getContent());

                /** bos_devre=0: kullanılabilir, bos_devre=1: kullanılamaz*/
                String bos_devre = responseList.get(0).split("Bos_Devre")[1].split(">|<")[1];
                result = "0".equals(bos_devre) ? true : false;

                logger.info("pstn: " + pstnBbk.getPstn() + ", envType: " + pstnBbk.getEnvironmentType() + " için webServis sonucu: " + result);
            } else {

                logger.info("pstn: " + pstnBbk.getPstn() + ", envType: " + pstnBbk.getEnvironmentType() + " için webServis hatası: " + response.getStatusLine().getStatusCode());
            }

            response.close();
            httpClient.close();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return result;
        }

    }

    private static String getRequestBody(Pstn_Bbk pstnBbk) {

        /** pstn null ise bbk'ya göre sogu, değilse pstn'e göre sorgu */

        String theCode = pstnBbk.getPstn() != null ? "PSTN" : "BBK";
        String theValue = pstnBbk.getPstn() != null ? pstnBbk.getPstn() : pstnBbk.getBbk();

        return new StringBuilder()
                .append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ws=\"http://www.etiya.com/WS\">")
                .append("<soapenv:Header/>")
                .append("<soapenv:Body>")
                .append("<ws:fiberSorgu>")
                .append("<ws:inputPojo>")
                .append("<ws:theCode>" + theCode + "</ws:theCode>")
                .append("<ws:theValue>" + theValue + "</ws:theValue>")
                .append("<ws:username>testUser</ws:username>")
                .append("</ws:inputPojo>")
                .append("</ws:fiberSorgu>")
                .append("</soapenv:Body>")
                .append("</soapenv:Envelope>").toString();
    }

}
