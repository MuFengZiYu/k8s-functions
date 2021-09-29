package org.wh.k8sfunctions.Services;


import org.apache.http.Consts;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import org.apache.http.util.EntityUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;


import java.io.FileNotFoundException;
import java.net.URI;
import java.util.Date;
import java.util.logging.Logger;


@RestController
@Component
@ConfigurationProperties(prefix = "myhost")

public class Calculator  {

    @Value("${myhost.hostname}")
    private String hostname;
    int num = Runtime.getRuntime().availableProcessors();
    Logger logger = Logger.getLogger(this.getClass().getName());

    long maximumsize=Runtime.getRuntime().maxMemory()/(1024*1024);
    ReadComputing rc=new ReadComputing(maximumsize);

    CloseableHttpClient httpClient = HttpClients.createDefault();

    @RequestMapping(value="/test",method= RequestMethod.GET)
    @ResponseBody
    public String heallth(@RequestParam("a") String a)  {
        long x = 0;
        for(long i =0 ; i < Long.valueOf(a); i++){
            x = x +1;
        }
        return String.valueOf(x);

    }

    @RequestMapping(value="/Calculate",method= RequestMethod.GET)
    @ResponseBody
    public String Calculate(@RequestParam("path") String path, @RequestParam("value") String value, @RequestParam(value = "size", required = false , defaultValue = "2000") String size ) throws InterruptedException {

        String result = "";

        logger.info("memosize"+ maximumsize);
        if (path.length() == 1) {
            result = rc.CalculationTask(Long.valueOf(value),num,Integer.valueOf(size));
            return result;
        } else {
            int var = path.indexOf("-");
            String i = path.substring(0, var);
            String x = path.substring(var + 1);
            var = value.indexOf("-");
            String j = value.substring(0, var);
            String y = value.substring(var + 1);

            long startTime = new Date().getTime();

            try {
                URI uri = new URIBuilder().setScheme("http")
                        .setHost(hostname + "/calculate"+i+"/Calculate")
                        //.setHost("127.0.0.1:8080/Calculate")
                        .setParameter("path", x)
                        .setParameter("value", y)
                        .setParameter("size", size)
                        .build();
                HttpGet get = new HttpGet(uri);   //使用Get方法提交

                // 通过请求对象获取响应对象

                int connectTime = 8000;
                //请求的参数配置，分别设置连接池获取连接的超时时间，连接上服务器的时间，服务器返回数据的时间

                RequestConfig config = RequestConfig.custom()
                        .setConnectionRequestTimeout(connectTime)
                        .setConnectTimeout(3000)
                        .setSocketTimeout(connectTime)
                        .build();

                get.setConfig(config);

                long exstarttime = new Date().getTime();

                CloseableHttpResponse response = httpClient.execute(get);

                long exendtime = new Date().getTime();
                int statusCode = response.getStatusLine().getStatusCode();
                //判断返回的状态码是否是200 ，200 代表服务器响应成功，并成功返回信息
                if (statusCode == HttpStatus.SC_OK) {
                    result = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
                    // httpClient.close();
                } else {
                    result = "失败";
                    //  httpClient.close();
                }
                // logger.info(String.valueOf(exendtime - exstarttime));
            } catch (Exception e) {
//                logger.info("uri编译有问题");
//                logger.info("x:" + x);
//                logger.info("y:" + y);
//                logger.info("result: " + result);
                result = "失败";
                // httpClient.close();
            }
            long endTime = new Date().getTime();
            return result + "\n" + rc.CalculationTask(Long.valueOf(j), num, Integer.valueOf(size) ) + ":" + (endTime - startTime);
        }

    }


}
