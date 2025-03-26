package edu.byui.apj.storefront.tutorial121;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SmokeTest {

    private final HomeController controller;

    @Autowired
    public SmokeTest(HomeController homeController) {
        this.controller = homeController;
    }

    @Test
    void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }
}