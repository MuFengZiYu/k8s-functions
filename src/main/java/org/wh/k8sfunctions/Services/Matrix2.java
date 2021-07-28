package org.wh.k8sfunctions.Services;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class Matrix2 extends Thread {
    private int AR;
    private int AC;
    private int BR;
    private int BC;
    private int truncksize;
    Random rm=new Random();
    long maximumsize=0;
    private CountDownLatch countDownLatch;
    private HashMap<Integer,Double[][]> result=null;
    public Matrix2(int AR, int AC, int BR, int BC, int truncksize, CountDownLatch countDownLatch) {
        this.AR = AR;
        this.AC = AC;
        this.BR = BR;
        this.BC = BC;
        this.truncksize=truncksize;
        result=new HashMap<Integer,Double[][]>();
        this.countDownLatch = countDownLatch;

    }

    public Double[][] getBcol(int colindex)
    {
        if(result.containsKey(colindex))
        {
            //System.out.println("obtain existing col");
            return result.get(colindex);
        }
        else
        {
            //long freememsize=Runtime.getRuntime().freeMemory();

            //long totalsize=maximumsize/(BR*4);
            //freememsize=freememsize/(1024*1024); // M

            //System.out.println("maximumsize="+maximumsize+"freememsize="+freememsize+" before inserting");
            Double[][] Bp = new Double[BR][1];
            for (int i = 0; i < BR; i++) {
                Bp[i][0] = Double.valueOf(rm.nextInt(6));
            }



            result.put(colindex, Bp);
            //freememsize=Runtime.getRuntime().freeMemory();
            //freememsize=freememsize/(1024*1024); // M

            //double freeratio=(double)freememsize/maximumsize;
            if(result.size()>maximumsize*2)
            {
                //System.out.println("result size="+result.size()+", clear one element");
                int rindex=rm.nextInt(result.size());
                Integer key=(Integer) result.keySet().toArray()[rindex];
                result.remove(key);
                //result.clear();
                //Runtime.getRuntime().gc();
                //freememsize=Runtime.getRuntime().freeMemory();
                //freememsize=freememsize/(1024*1024); // M
                //System.out.println("freememsize changed to="+freememsize);
            }
            else
            {
                //System.out.println("freememsize="+freememsize+" sufficient");
            }
            return Bp;
        }


    }
    public void run() {
        // TODO Auto-generated method stub
        maximumsize=Runtime.getRuntime().maxMemory();
        maximumsize=maximumsize/(1024*1024); // M
        if(AC!=BR)
        {
            System.out.println("AC!=BR");
        }
        double[][] Ap = new double[AR][AC];
        for (int i = 0; i < AR; i++) {
            for (int j = 0; j < AC; j++) {
                Ap[i][j] = rm.nextInt(6);
            }
        }
        int BCp=truncksize;
        int divnum=BC/BCp;
        long ops=0;

        //long startTime = System.currentTimeMillis();
        //ops=0;
        //double[][] result=new double[AR][BCp];
        //long endTime =System.currentTimeMillis();
        //System.out.println(String.valueOf(endTime-startTime));
        //startTime = System.currentTimeMillis();
        for (int d=0;d<divnum;d++)
        {
            Double[][] Bp = new Double[BR][BCp];
            for (int i = 0; i < BR; i++) {
                for (int j = 0; j < BCp; j++) {
                    Bp[i][j] = Double.valueOf(rm.nextInt(6));
                }
            }
            double[][] result=new double[AR][BCp];
            for (int i = 0; i < AR; i++)
            {
                for (int j = 0; j < BCp; j++)
                {
                    //int index=rm.nextInt(divnum*BCp);
                    //Double[][] Bp=getBcol(j+d*BCp);

                    for (int k = 0; k < AC; k++)
                    {
                        result[i][j]+=Ap[i][k] * Bp[k][j];
                        //double a=Ap[i][k] * 5;
                        //result[i][j]=a;
                        //double b = Double.valueOf(a);
                        // double b=rm.nextInt(10);
                        //=a;
                        //result[i][j+d*BCp]+=a;
                        //result[i][j+d*BCp] += Ap[i][k] * Bp[k][j];
                        ops++;

                    }

                }
            }

            //long maximumsize=Runtime.getRuntime().maxMemory();
            //maximumsize=maximumsize/(1024*1024); // M
            for(int w=0;w<AR/50;w++)
            {
                double[][] result2=new double[AR][BC*5];
	        	/*for (int i = 0; i < AR; i++) {
	        		for (int j = 0; j < BC*15; j++) {
	        			int base=(int) Math.floor(j/BC);
	        			int jindex=(j-base*BC)%BCp;
	        			result2[i][j] = result[i][jindex];
	        		}
	            }*/

            }
            long freememsize=Runtime.getRuntime().freeMemory();
            freememsize=freememsize/(1024*1024); // M
            //System.out.println("freememsize="+freememsize);
        }
	        /*long freememsize=Runtime.getRuntime().freeMemory();
    		long maximumsize=Runtime.getRuntime().maxMemory();
    		double freeratio=(double)freememsize/maximumsize;
    		if(freememsize<50)
    		{
    			Runtime.getRuntime().gc();
    			freememsize=Runtime.getRuntime().freeMemory();

        		freeratio=(double)freememsize/maximumsize;
        		System.out.println("freeratio after gc="+freeratio);
    		}*/
        // System.out.println("change to divnum="+d);
        //long freememsize=Runtime.getRuntime().freeMemory();
        //long maximumsize=Runtime.getRuntime().maxMemory();
        //freememsize=freememsize/(1024*1024); // M
        //maximumsize=maximumsize/(1024*1024); // M
        //	System.out.println("maximumsize="+maximumsize+"freememsize="+freememsize);
        // endTime =System.currentTimeMillis();
        // long gap=endTime-startTime;
        // double average=Double.valueOf(gap)/(AR*BCp*AC);
        // System.out.println(String.valueOf(average));
        //double average=Double.valueOf(gap)/ops;
        // System.out.println(String.valueOf(gap)+", ops="+ops+",average="+average);


        System.out.println("divnum="+divnum+"divnum*ops="+divnum*ops);
        // 线程数减1
        countDownLatch.countDown();
    }
}
