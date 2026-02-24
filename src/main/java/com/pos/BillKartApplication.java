package com.pos;

import com.pos.security.JwtFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.event.EventListener;


@SpringBootApplication
public class BillKartApplication extends SpringBootServletInitializer {

    private static final Logger logger = LogManager.getLogger(BillKartApplication.class);


    public static void main(String[] args) {
        System.out.println("BillKart Application Started1");
        SpringApplication.run(BillKartApplication.class, args);
        System.out.println("BillKart Application Started");

    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        System.out.println("BillKart Application Started2");
        return builder.sources(BillKartApplication.class);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        logger.info("✅ BillKart Application Started Successfully");
    }
}



