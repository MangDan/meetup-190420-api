/*
 * Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.helidon.examples.quickstart;

import static io.helidon.config.ConfigSources.classpath;
import static io.helidon.config.ConfigSources.file;
import static io.helidon.config.PollingStrategies.regular;

import java.io.IOException;
import java.time.Duration;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import io.helidon.config.Config;
import io.helidon.config.OverrideSources;
import io.helidon.microprofile.server.Server;

/**
 * Main method simulating trigger of main method of the server.
 */
public final class Main {

    private static final Logger logger = Logger.getLogger("io.helidon.microprofile.startup");
    
    /**
     * Cannot be instantiated.
     */
    private Main() { }

    /**
     * Application main entry point.
     * @param args command line arguments
     * @throws IOException if there are problems reading logging properties
     */
    public static void main(final String[] args) throws IOException {
        setupLogging();

        Server server = startServer();

        /*
         * 기본 Config 설정 관련
         */
        logger.info("application.yaml 읽기 시작");
        Config config = Config.create();
        logger.info("================ application.yaml 쓰기 시작 ==================\n"+
        String.format(
        "greeting is %s\n"
                + "web.debug is %b\n"       // 부울
                + "web.page-size is %d\n"   // 정수
                + "web.ratio is %f\n"       // 실수
                + "bl.initial-id is %d\n"   // 정수
                + "origin is %s\n"          // 문자
                + "java.home is %s\n"       // 문자
                + "app.name is %s",         // 문자
                config.get("greeting").asString().orElse("Default Greeting"), 
                config.get("web.debug").asBoolean().orElse(false),
                config.get("web.page-size").asInt().orElse(50),
                config.get("web.ratio").asDouble().orElse(2.0),
                config.get("bl.initial-id").asLong().orElse(1L),
                config.get("origin").asString().orElse("defaults"),
                config.get("java.home").asString().get(),
                config.get("app.name").asString().get())
        + "================ application.yaml 쓰기 완료 ==================");




        /*
         * Config 지정하여 읽기
         */
        logger.info("application.conf 읽기 시작");
        Config hoconConfig = Config.create(classpath("application.conf"));
        
        logger.info("================ application.conf 쓰기 시작 ==================\n"+
        String.format("app.page-size is %d\n"       // 정수
                + "app.storageEnabled is %b\n"      // 부울
                + "app.basic-range is %s\n",        // 실수
                hoconConfig.get("app.page-size").asInt().get(), 
                hoconConfig.get("app.storageEnabled").asBoolean().orElse(false),
                hoconConfig.get("app.basic-range").asList(Integer.class).get())
        + "================ application.conf 쓰기 완료 ==================");






        /*
         * Overriding & Runtime Loading
         */
        logger.info("application.conf 읽기 시작");
        Config overrideConfig = Config.builder().sources(file("conf/priority-config.yaml").pollingStrategy(regular(Duration.ofSeconds(1))), classpath("application.yaml"))
        .overrides(OverrideSources.file("conf/overrides.properties")
        .pollingStrategy(regular(Duration.ofSeconds(1))))
        .build();

        // Resolve current runtime context
        String env = overrideConfig.get("env").asString().get();
        String pod = overrideConfig.get("pod").asString().get();
        
        // get logging config for the current runtime
        Config podConfig = overrideConfig
                .get(env)
                .get(pod);

        // initialize logging from config
        initPod(podConfig);

        // react on changes of logging configuration
        podConfig.onChange(Main::initPod);
        System.out.println("================ override config ==================");










        System.out.println("http://localhost:" + server.port() + "/greet");
    }





    private static boolean initPod(Config loggingConfig) {
        String level = loggingConfig.get("logging.level").asString().orElse("WARNING");
        String greeting = loggingConfig.get("app.greeting").asString().orElse("Hello World");
        //e.g. initialize logging using configured level...

        System.out.println("================ override config ==================");
        System.out.println("Set logging level to " + level + ".");
        System.out.println("Application greeting is " + greeting + ".");
        System.out.println("================ override config ==================");
        return true;
    }

    /**
     * Start the server.
     * @return the created {@link Server} instance
     */
    static Server startServer() {
        // Server will automatically pick up configuration from
        // microprofile-config.properties
        // and Application classes annotated as @ApplicationScoped
        return Server.create().start();
    }

    /**
     * Configure logging from logging.properties file.
     */
    private static void setupLogging() throws IOException {
        // load logging configuration
        LogManager.getLogManager().readConfiguration(
                Main.class.getResourceAsStream("/logging.properties"));
    }
}
