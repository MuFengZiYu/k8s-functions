package org.wh.k8sfunctions.Services;




import java.io.*;

import java.util.concurrent.CountDownLatch;

public class ReadComputing2 {



    public String CalculationTask( int value, int cpunum, int trunksize) throws InterruptedException, FileNotFoundException {
        int row_A = 500;
        int col_A = value;
        int col_B = 500;
        long startTime;
        long endTime;

        int threadNum = cpunum;
        int gap = row_A / threadNum;

        CountDownLatch countDownLatch = new CountDownLatch(threadNum);
        startTime = System.currentTimeMillis();
        for (int k = 0; k < threadNum; k++) {

            Matrix mx = new Matrix(gap, col_A, col_A, col_B, trunksize, countDownLatch);
            mx.start();
            }
        countDownLatch.await();
        endTime =System.currentTimeMillis();
        //System.out.println("Using thread num="+cpunum +" truncksize"+trunksize);
        return String.valueOf(endTime-startTime);

    }

}