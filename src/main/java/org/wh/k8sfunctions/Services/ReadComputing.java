package org.wh.k8sfunctions.Services;



import org.springframework.web.bind.annotation.*;
import java.io.*;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;
import java.util.stream.Stream;

@RestController

public class ReadComputing {


    long maxMemory = Runtime.getRuntime().maxMemory() / 1024*1024;
    int num = Runtime.getRuntime().availableProcessors();
    Logger logger = Logger.getLogger(this.getClass().getName());

    @RequestMapping(value="/Calculate",method= RequestMethod.GET)
    @ResponseBody
    public String CalculationTask(@RequestParam("param") String param) throws InterruptedException, FileNotFoundException {
        int row_A = 1000;
        int col_A = 1000;
        int col_B = 1000;
        long startTime;
        long endTime;

        String path = "static/matrix.txt";
        //path = "D:/Program Files/JetBrains/IntelliJ IDEA/workspace/k8s-functions/target/classes/static/matrix.txt";
        double[][] A = new double[row_A][col_A];
        double[][] B = new double[col_A][col_B];
        double[][] result = new double[A.length][B[0].length];
        Date starttime =new Date();
        String[] line = readString(path).split("\n");
        Date endtime = new Date();
        System.out.println(endtime.getTime()-starttime.getTime());

        for (int i = 0; i < row_A; i++) {
            String[] tempStr = line[i].split(" ");
            for (int j = 0; j < col_A; j++) {
                A[i][j] = Double.valueOf(tempStr[j]);
                B[i][j] = Double.valueOf(tempStr[j]);
            }



        }
        //Thread.sleep(5* 1000/maxMemory);
        if(param.equals("1")){
            int threadNum = 3;
            int gap = A.length / threadNum;
            CountDownLatch countDownLatch = new CountDownLatch(threadNum);
            startTime = System.currentTimeMillis();
            for (int i = 0; i < threadNum; i++) {
                Matrix mx = new Matrix(A, B, i, gap, result, countDownLatch);
                mx.start();
            }
            countDownLatch.await();
            endTime =System.currentTimeMillis();
            return String.valueOf(endTime-startTime);

        }
            else{
            startTime = System.currentTimeMillis();
            for (int i = 0; i < A.length; i++) {
                for (int j = 0; j < B[0].length; j++) {
                    for (int k = 0; k < A[0].length; k++)
                        result[i][j] += A[i][k] * B[k][j];
                }
            }
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
