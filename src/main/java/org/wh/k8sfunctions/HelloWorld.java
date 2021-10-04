package org.wh.k8sfunctions;



import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorld {
    @RequestMapping(value="/hello",method= RequestMethod.GET)
    @ResponseBody
    public String hello() throws InterruptedException {
        Thread.sleep(2000);
        return "Hello World";
    }
}
