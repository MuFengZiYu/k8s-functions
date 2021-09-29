package org.wh.k8sfunctions.Services;




import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class ReadComputing {

    public HashMap<String , String> number ;
    public Random rd = new Random();
    public  String basevalue = "1000000";
    public ReadComputing (long memsize){
        this.number = new HashMap<String , String>();
        for(int i = 0; i < (int)memsize; i++){
            number.put(String.valueOf(i),basevalue);
        }
    }

    public String CalculationTask( long value, int cpunum , int sumStudents ) throws InterruptedException {

        long startTime;
        long endTime;
        int studentId = rd.nextInt(sumStudents);
        //cpunum = 1;
        long basetimes = 0;
        for ( String id : number.keySet()){
            if ( id.equals(String.valueOf(studentId))){
                basetimes = Long.parseLong(number.get(id));
                System.out.println("命中");
                break;
            }

        }
        if( basetimes == 0 ){
            Object[] indexs = number.keySet().toArray();
            Object index = indexs[(indexs.length-1)/2-1];
            number.remove(index);
            System.out.println("未命中");
            number.put(String.valueOf(studentId), basevalue);
            Thread.sleep(value);
            //basetimes  = Long.parseLong(number.get(String.valueOf(studentId)));
            basetimes  = Long.parseLong(basevalue);
        }
        long gap = value*basetimes / cpunum;
        System.out.println("gap"+gap);
        CountDownLatch countDownLatch = new CountDownLatch(cpunum);
        startTime = System.currentTimeMillis();
        for (int k = 0; k < cpunum; k++) {

            Count count = new Count(gap , countDownLatch);
            count.start();
            }
        countDownLatch.await();
        endTime =System.currentTimeMillis();
        //System.out.println("Using thread num="+cpunum +" time"+String.valueOf(endTime-startTime));
        return String.valueOf(endTime-startTime);

    }

}