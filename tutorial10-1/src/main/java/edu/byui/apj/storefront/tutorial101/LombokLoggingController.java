package edu.byui.apj.storefront.tutorial101;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class LombokLoggingController {

    @RequestMapping("/lombok")
    public String index() {
        log.debug("Logger class: {}", log.getClass().getName());
//        log.trace("A TRACE Message");
//        log.debug("A DEBUG Message");
//        log.info("An INFO Message");
//        log.warn("A WARN Message");
//        log.error("An ERROR Message");

        return "Howdy! Check out the Logs to see the output...";
    }
}