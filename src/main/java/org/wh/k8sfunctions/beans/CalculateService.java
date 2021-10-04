package org.wh.k8sfunctions.beans;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.wh.k8sfunctions.Count;


import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.CountDownLatch;


@Service


public class CalculateService {

    public Logger logger =  LoggerFactory.getLogger(CalculateService.class);
    public HashMap<String , String> number ;
    public Random rd = new Random();
    public  String basevalue = "1000000";

    public static class A{
        private static final CalculateService calculateService = new CalculateService();
    }
    public static final  CalculateService getCalculateService(){
        return A.calculateService;
    }

    public CalculateService(){
        long maximumsize = Runtime.getRuntime().maxMemory()/(1024*1024);
        setMemsize(maximumsize);
    }


    public void setMemsize(long memsize){
           number = new HashMap<String , String>();
        for(int i = 0; i < (int)memsize; i++){
            number.put(String.valueOf(i),basevalue);
        }
    }
    public  String CalculationTask( long value, int cpunum , int sumStudents )  {


        int studentId = rd.nextInt(sumStudents);
        int threadNum = cpunum;
        long basetimes = 0;
        try {


            for (String id : number.keySet()) {
                if (id.equals(String.valueOf(studentId))) {
                    basetimes = Long.parseLong(number.get(id));
                    break;
                }
            }

            if (basetimes == 0) {
                Object[] indexs = number.keySet().toArray();
                Object index = indexs[(indexs.length - 1) / 2 - 1];
                number.remove(index);
                number.put(String.valueOf(studentId), basevalue);
                Thread.sleep(value);
                basetimes = Long.parseLong(basevalue);
            }
            long gap = value * basetimes / threadNum;
            logger.debug("gap" + gap);
            String interval = Sum(gap, threadNum);
            return interval;

        }
        catch (InterruptedException e){
            e.printStackTrace();
            return "11计算出错\n";
        }


    }

    public synchronized String Sum(long gap, int threadNum){
        long startTime;
        long endTime;
//同步函数，保持单线程计算，可看情况修改
       try {
           CountDownLatch countDownLatch = new CountDownLatch(threadNum);
           startTime = System.currentTimeMillis();

           for (int k = 0; k < threadNum; k++) {
               Count count = new Count(gap, countDownLatch);
               count.start();
           }
           countDownLatch.await();

           endTime = System.currentTimeMillis();
           return String.valueOf(endTime - startTime);
       }catch (InterruptedException e){
          e.printStackTrace();
           return "同步计算出错";
       }


    }


}
