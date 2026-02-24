//package com.pos;
//
//import org.springframework.boot.builder.SpringApplicationBuilder;
//import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
//
//public class ServletInitializer extends SpringBootServletInitializer {
//
//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//        String catalinaBase = System.getProperty("catalina.base");
//        if (catalinaBase == null) {
//            catalinaBase = System.getenv("CATALINA_BASE");
//        }
//        String configPath = "file:" + catalinaBase + "/pos-config/application.properties";
//
//        String log4jConfigPath = "file:" + catalinaBase + "/pos-config/log4j2.properties";
//        System.out.println("AppPath=" + configPath + "," + log4jConfigPath);
//        return application.sources(BillKartApplication.class).properties(
//                "spring.config.location=" + configPath,
//                "logging.config=" + log4jConfigPath
//        );
//
//    }
//}
//
