package org.wh.k8sfunctions;


import java.util.concurrent.CountDownLatch;

public class Count extends Thread {
    private CountDownLatch countDownLatch;
    private long times ;
    private long result;
    public Count( long times, CountDownLatch countDownLatch) {
        this.times = times;
        this.countDownLatch = countDownLatch;

    }
    public void run() {
        // TODO Auto-generated method stub
        int sum =0;

        long starttime = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            sum += 1;
            //防止计算太大超时
            if(i%1000000 == 22){
                long currenttime = System.currentTimeMillis();
                if(currenttime - starttime > 1000 ){
                    break;
                }
            }
        }
        countDownLatch.countDown();
        return;
    }
}
