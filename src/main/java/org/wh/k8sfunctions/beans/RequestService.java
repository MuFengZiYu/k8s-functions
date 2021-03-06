package org.wh.k8sfunctions.beans;


import org.apache.http.Consts;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


import java.net.URI;
import java.util.Date;

@Service

@ConfigurationProperties(prefix = "myhost")
public class RequestService {


    @Value("${myhost.hostname}")
    private String hostname;
    @Value("${myhost.port}")
    private String port;
    @Value("${myhost.flag}")
    private boolean flag;
    private Logger logger =  LoggerFactory.getLogger(RequestService.class);
    CloseableHttpClient httpClient = HttpClients.createDefault();
    public RequestService(){

    }
    public static class B{
        private static final RequestService requestsService = new RequestService();
    }
    public static final   RequestService getRequestService(){
        return B.requestsService;
    }


    public String Request( String service, String path, String value , String size ) {

        String result = "";

        try {

            URI uri;
            if(flag == true){
                uri = new URIBuilder().setScheme("http")
                        .setHost(hostname + "/calculate" + service +"/Calculate")
                        //.setHost("127.0.0.1:8080/Calculate")
                        .setParameter("path", path)
                        .setParameter("value", value)
                        .setParameter("size", size)
                        .build();
            }
            else{
                uri = new URIBuilder().setScheme("http")
                        //.setHost(hostname + "/calculate" + service +"/Calculate")
                        .setHost("127.0.0.1:" + port + "/Calculate")
                        .setParameter("path", path)
                        .setParameter("value", value)
                        .setParameter("size", size)
                        .build();
            }
            HttpGet get = new HttpGet(uri);   //??????Get????????????

            logger.debug(uri.toString());

            int connectTime = 8000;
            //???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????

            RequestConfig config = RequestConfig.custom()
                    .setConnectionRequestTimeout(connectTime)
                    .setConnectTimeout(3000)
                    .setSocketTimeout(connectTime)
                    .build();

            get.setConfig(config);



            CloseableHttpResponse response = httpClient.execute(get);


            int statusCode = response.getStatusLine().getStatusCode();
            //?????????????????????????????????200 ???200 ???????????????????????????????????????????????????
            if (statusCode == HttpStatus.SC_OK) {
                long endTime = new Date().getTime();
                result = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
                //httpClient.close();
                return result + "\n";

            } else {
                result = "??????";
                //httpClient.close();
                return result + "\n " + "???????????????";

            }
            // logger.info(String.valueOf(exendtime - exstarttime));
        } catch (Exception e) {
            e.printStackTrace();
            result = "??????";

            return result + "\n " + "???????????????";

        }


    }


}
