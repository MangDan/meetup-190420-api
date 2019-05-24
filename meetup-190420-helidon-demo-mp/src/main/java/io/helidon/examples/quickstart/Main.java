package io.helidon.examples.quickstart;

import static io.helidon.config.ConfigSources.classpath;
import static io.helidon.config.ConfigSources.file;
import static io.helidon.config.PollingStrategies.regular;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import io.helidon.config.Config;
import io.helidon.config.OverrideSources;
import io.helidon.config.git.GitConfigSourceBuilder;
import io.helidon.microprofile.server.Server;

public final class Main {

    private static final Logger logger = Logger.getLogger("io.helidon.microprofile.startup");
    
    private Main() { }

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
                + "app.basic-range is %s\n",        // 문자
                hoconConfig.get("app.page-size").asInt().get(), 
                hoconConfig.get("app.storageEnabled").asBoolean().orElse(false),
                hoconConfig.get("app.basic-range").asList(Integer.class).get())
        + "================ application.conf 쓰기 완료 ==================");


        /*
         * Git Config 읽기
         */
        logger.info("git config 읽기 시작");
        Config gitConfig = Config.create(
                GitConfigSourceBuilder.create("git-application.conf")
                        .uri(URI.create("https://github.com/MangDan/meetup-190420-api.git"))
                        .branch("master")
                        .build());

        logger.info("================ git config 쓰기 시작 ==================\n"+
        String.format("git.greeting is %s\n",       // 문자
                gitConfig.get("greeting").asString().get())
        + "================ git config 쓰기 완료 ==================");

        

        /*
         * onfig 오버라이드, 런타임 읽기
         */
        logger.info("Config 오버라이드, 런타임 읽기 시작");
        Config overrideConfig = Config.builder().sources(file("conf/priority-config.yaml").pollingStrategy(regular(Duration.ofSeconds(1))), classpath("application.yaml"))
        .overrides(OverrideSources.file("conf/overrides.properties")
        .pollingStrategy(regular(Duration.ofSeconds(1))))
        .build();

        String env = overrideConfig.get("env").asString().get();
        String pod = overrideConfig.get("pod").asString().get();
        
        Config podConfig = overrideConfig
                .get(env)
                .get(pod);

        initPod(podConfig);

        podConfig.onChange(Main::initPod);
        logger.info("================ Config 오버라이드, 런타임 읽기 완료 ==================");

        System.out.println("http://localhost:" + server.port() + "/greet");
    }





    private static boolean initPod(Config loggingConfig) {
        String level = loggingConfig.get("logging.level").asString().orElse("WARNING");
        String greeting = loggingConfig.get("app.greeting").asString().orElse("Hello World");
        //e.g. initialize logging using configured level...

        logger.info("================ Config 런타임 변경 시작 ==================");
        logger.info("Set logging level to " + level + ".");
        logger.info("Application greeting is " + greeting + ".");
        logger.info("================ Config 런타임 변경 완료 ==================");
        return true;
    }

    static Server startServer() {
        return Server.create().start();
    }

    private static void setupLogging() throws IOException {
        // load logging configuration
        LogManager.getLogManager().readConfiguration(
                Main.class.getResourceAsStream("/logging.properties"));
    }
}
