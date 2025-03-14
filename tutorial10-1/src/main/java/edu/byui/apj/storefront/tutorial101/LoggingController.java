package edu.byui.apj.storefront.tutorial101;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;


@RestController
@Slf4j
public class LoggingController {

    Logger logger = LoggerFactory.getLogger(LoggingController.class);

    @RequestMapping("/")
    public String index() {
        log.debug("Logger class: {}", log.getClass().getName());
//        logger.trace("A TRACE Message");
//        logger.debug("A DEBUG Message");
//        logger.info("An INFO Message");
//        logger.warn("A WARN Message");
//        logger.error("An ERROR Message");

        return "Howdy! Check out the Logs to see the output...";
    }
}