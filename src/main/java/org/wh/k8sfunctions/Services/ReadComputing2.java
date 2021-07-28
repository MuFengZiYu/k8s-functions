package org.wh.k8sfunctions.Services;




import java.io.*;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;
import java.util.stream.Stream;
import java.util.Random;

public class ReadComputing2 {

    Random rm=new Random();
    long maxMemory = Runtime.getRuntime().maxMemory() / 1024*1024;
    int num = Runtime.getRuntime().availableProcessors();
    Logger logger = Logger.getLogger(this.getClass().getName());
    public static void main(String[] args)
    {


        ReadComputing2 rc=new ReadComputing2();
        try {
            System.out.println("final time"+rc.CalculationTask("1",1,10));
            //System.out.println("final time"+rc.CalculationTask("1",1,10));
            //System.out.println("final time"+rc.CalculationTask("1",1,30));
            //System.out.println("final time"+rc.CalculationTask("1",1,100));
            //System.out.println("final time"+rc.CalculationTask("1",1,200));
            //System.out.println("final time"+rc.CalculationTask("1",1,300));
            System.out.println("final time"+rc.CalculationTask("1",2,10));
            System.out.println("final time"+rc.CalculationTask("1",3,10));
            System.out.println("final time"+rc.CalculationTask("1",4,10));
            System.out.println("final time"+rc.CalculationTask("1",5,10));
        } catch (FileNotFoundException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String CalculationTask( String param, int cpunum, int trunksize) throws InterruptedException, FileNotFoundException {
        int row_A = 500;
        int col_A = 500;
        int col_B = 500;
        long startTime;
        long endTime;



        if(param.equals("1")){

            int threadNum = cpunum;
            int gap = row_A / threadNum;

            CountDownLatch countDownLatch = new CountDownLatch(threadNum);
            startTime = System.currentTimeMillis();
            for (int k = 0; k < threadNum; k++) {

                Matrix2 mx = new Matrix2(gap, col_A, col_A, col_B, trunksize, countDownLatch);
                mx.start();
            }
            countDownLatch.await();
            endTime =System.currentTimeMillis();
            System.out.println("Using thread num="+cpunum +" truncksize"+trunksize);
            return String.valueOf(endTime-startTime);

        }
        else{
            startTime = System.currentTimeMillis();

            endTime =System.currentTimeMillis();
            return String.valueOf(endTime-startTime);
        }

    }

    public String readString(String path){
        StringBuffer sb = new StringBuffer();
        InputStream inputStream=null;

        try {
            ClassLoader classLoader = getClass().getClassLoader();

            inputStream =  classLoader.getResourceAsStream(path);

            BufferedReader br =new BufferedReader(new InputStreamReader(inputStream));
            String currentLine;
//                Stream<String> lines = br.lines();
//               lines.forEach(s->sb.append(s+"\n"));
            while ((currentLine = br.readLine()) != null){
                sb.append(currentLine+"\n");
            }

        } catch (Exception e) {
            logger.info("file wrong");
            e.printStackTrace();
            inputStream.close();

        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            logger.info("file right");
            return  sb.toString();
        }
    }
}