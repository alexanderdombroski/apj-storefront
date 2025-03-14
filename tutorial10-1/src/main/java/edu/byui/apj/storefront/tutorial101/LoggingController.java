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
        logger.debug("Controller class: {}", this.getClass().getName());

        return "Howdy! Check out the Logs to see the output...";
    }
}