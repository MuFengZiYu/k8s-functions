package org.wh.k8sfunctions.Services;

import java.util.concurrent.CountDownLatch;

public class Matrix extends Thread {
    private double[][] A;
    private double[][] B;
    private int index;
    private int gap;
    private double[][] result;
    private CountDownLatch countDownLatch;

    public Matrix(double[][] A, double[][] B, int index, int gap, double[][] result, CountDownLatch countDownLatch) {
        this.A = A;
        this.B = B;
        this.index = index;
        this.gap = gap;
        this.result = result;
        this.countDownLatch = countDownLatch;
    }

    public void run() {
        // TODO Auto-generated method stub
        for (int i = index * gap; i < (index + 1) * gap; i++)
            for (int j = 0; j < B[0].length; j++) {
                for (int k = 0; k < B.length; k++)
                    result[i][j] += A[i][k] * B[k][j];
            }
        // 线程数减1
        countDownLatch.countDown();
    }
}
