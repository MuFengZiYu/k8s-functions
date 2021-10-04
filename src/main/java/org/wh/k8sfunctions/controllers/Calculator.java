package org.wh.k8sfunctions.controllers;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.wh.k8sfunctions.beans.CalculateService;
import org.wh.k8sfunctions.beans.RequestService;


import java.util.Date;


@RestController



public class Calculator {

    int num = Runtime.getRuntime().availableProcessors();
    Logger logger = LoggerFactory.getLogger(Calculator.class);

    @Autowired
    CalculateService calculateService ;

    @Autowired
    RequestService requestService;


    @RequestMapping(value="/Calculate",method= RequestMethod.GET)
    @ResponseBody
    public String Calculate(@RequestParam("path") String path, @RequestParam("value") String value, @RequestParam(value = "size", required = false , defaultValue = "2000") String size ) throws InterruptedException {

        String result = "";

        logger.debug("cpu num : " + num);
        if (path.length() == 1) {

            result = calculateService.CalculationTask(Long.valueOf(value), num, Integer.valueOf(size));
            return result;
        } else {

            int var = path.indexOf("-");
            String i = path.substring(0, var);
            String x = path.substring(var + 1);
            var = value.indexOf("-");
            String j = value.substring(0, var);
            String y = value.substring(var + 1);

            long startTime = new Date().getTime();

            result = requestService.Request(i, x, y, size);
            result += calculateService.CalculationTask(Long.valueOf(j), num, Integer.valueOf(size)) + "/n";
            return result;

        }

    }



}
