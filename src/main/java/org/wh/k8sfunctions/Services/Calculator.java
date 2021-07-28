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

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.logging.Logger;


@RestController


public class Calculator  {


    Logger logger = Logger.getLogger(this.getClass().getName());
    CloseableHttpClient httpClient = HttpClients.createDefault();

    @RequestMapping(value="/test",method= RequestMethod.GET)
    @ResponseBody
    public String heallth() throws IOException, URISyntaxException {
            URI uri = new URIBuilder().setScheme("http")
                    //.setHost("myfibonacci.wh/fibonacci"+i+"/Fibonacci")
                    .setHost("localhost:8080/Fibonacci")
                    .setParameter("param1", "1")
                    .setParameter("param2", "1")
                    .build();
            HttpGet get = new HttpGet(uri);   //使用Get方法提交

            int connectTime = 3000;
            //请求的参数配置，分别设置连接池获取连接的超时时间，连接上服务器的时间，服务器返回数据的时间

            RequestConfig config = RequestConfig.custom()
                    .setConnectionRequestTimeout(connectTime)
                    .setConnectTimeout(1000)
                    .setSocketTimeout(connectTime)
                    .build();
            //配置信息添加到Get请求中
            get.setConfig(config);
            //通过httpclient的execute提交 请求 ，并用CloseableHttpResponse接受返回信息
            long startTime = new Date().getTime();
            CloseableHttpResponse response = httpClient.execute(get);
            long endTime = new Date().getTime();
            logger.info(String.valueOf(endTime-startTime));
            //服务器返回的状态
            int statusCode = response.getStatusLine().getStatusCode();

        //判断返回的状态码是否是200 ，200 代表服务器响应成功，并成功返回信息
        if (statusCode == HttpStatus.SC_OK) {
            return EntityUtils.toString(response.getEntity(), Consts.UTF_8);
            //  httpClient.close();
        } else {
            return "失败";
            //  httpClient.close();
        }

    }
    public BigInteger Fibonacci(int i) {
        if (i == 1 || i == 2)
            return BigInteger.valueOf(1);
        else
            return Fibonacci(i - 1).add(Fibonacci(i - 2));
    }

/*    @RequestMapping(value="/Asyncfibonacci",method= RequestMethod.GET)
    @ResponseBody
    public String TestAsyn(@RequestParam("param1") String param1, @RequestParam("param2") String param2, HttpServletRequest request) throws Exception {
        CloseableHttpAsyncClient client = HttpAsyncClients.createDefault();
        client.start();
         String result = "";
        if (param1.length() == 1) {
            result = Fibonacci(Integer.valueOf(param2)).toString();
            return result;
        } else {

            int var = param1.indexOf("-");
            String i = param1.substring(0, var);
            String x = param1.substring(var + 1);
            var = param2.indexOf("-");
            String j = param2.substring(0, var);
            String y = param2.substring(var + 1);

            long startTime = new Date().getTime();


            try {


                URI uri = new URIBuilder().setScheme("http")

                        .setHost("myfibonacci.wh/fibonacci" + i + "/Fibonacci")
                        //.setHost("127.0.0.1:8080/Fibonacci")
                        .setParameter("param1", x)
                        .setParameter("param2", y)
                        .build();

                HttpGet get = new HttpGet(uri);   //使用Get方法提交
                int connectTime = 8000;

                RequestConfig config = RequestConfig.custom()
                        .setConnectionRequestTimeout(connectTime)
                        .setConnectTimeout(3000)
                        .setSocketTimeout(connectTime)
                        .build();

                get.setConfig(config);


                // 通过请求对象获取响应对象



                final CountDownLatch latch = new CountDownLatch(1);
                final String[] resdata=new String[1];
                client.execute(get, new FutureCallback<HttpResponse>() {
                    //执行异步操作  请求完成后
                    @Override
                    public void completed(final HttpResponse response) {
                        latch.countDown();
                        //响应内容
                        int a = response.getStatusLine().getStatusCode();
                        System.out.println("状态码:" + a);
                        if (a == 200) {
                            HttpEntity entity = response.getEntity();
                            try {
                                resdata[0] = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            logger.info("成功!"+resdata[0]);

                        } else {
                            try {

                                logger.info(response.getStatusLine().getStatusCode()
                                        + "  " + EntityUtils.toString(response.getEntity(), "UTF-8"));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    //请求失败处理
                    @Override
                    public void failed(final Exception ex) {
                        latch.countDown();
                    }

                    //请求取消后处理
                    @Override
                    public void cancelled() {
                        latch.countDown();
                    }

                });
//                try {
//                    latch.await();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                //关闭
//                try {
//                    client.close();
//                } catch (IOException ignore) {
//
//                }
                result +=resdata[0];
            } catch (Exception e) {
               logger.info("uri编译有问题");
               logger.info("x:" + x);
               logger.info("y:" + y);
              logger.info("result: " + result);
                result = "失败";
                // httpClient.close();
            }

            long endTime = new Date().getTime();
            return result + "\n" + Fibonacci(Integer.valueOf(j)).toString() + ":" + (endTime - startTime);
        }
    }*/
    @RequestMapping(value="/Fibonacci",method= RequestMethod.GET)
    @ResponseBody

    public String Test(@RequestParam("param1") String param1, @RequestParam("param2") String param2, HttpServletRequest request) throws Exception {

        String result = "";
        if (param1.length() == 1) {
            result = Fibonacci(Integer.valueOf(param2)).toString();
            return result;
        } else {

            int var = param1.indexOf("-");
            String i = param1.substring(0, var);
            String x = param1.substring(var + 1);
            var = param2.indexOf("-");
            String j = param2.substring(0, var);
            String y = param2.substring(var + 1);

            long startTime = new Date().getTime();


            try {


                URI uri = new URIBuilder().setScheme("http")

                        .setHost("myfibonacci.wh/fibonacci"+i+"/Fibonacci")
                        //.setHost("127.0.0.1:8080/Fibonacci")
                        .setParameter("param1", x)
                        .setParameter("param2", y)
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
            return result + "\n" + Fibonacci(Integer.valueOf(j)).toString() + ":" + (endTime - startTime);
        }
    }

}
