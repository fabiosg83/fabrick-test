package com.fabrick;

import java.io.File;
import java.nio.file.Paths;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 *
 * @author fabio.sgroi
 */
public class PidFileManager {

    private final static Logger jLogger = LoggerFactory.getLogger("main");
    private static String PID_FILE_NAME = "application.pid";
    private static File PID_FILE = null;
    private static String ENVIRONMENT = "empty";

    /*
     * Verifica che non ci sia già un "PidFile" per evitare che ci siano più istanze
     * dell'applicativo in running. La verifica non viene effettuata in ambiente di "dev"
     */
    protected static void runWithPidFile(String[] args) {
        setEnviroment();

        String applicationName = Paths.get(".").normalize().toAbsolutePath().getFileName().toString();
        jLogger.info("[RUN] APPLICATION_NAME [" + applicationName + "]");
        PID_FILE_NAME = applicationName.trim().toLowerCase() + ".pid";

        if (!(PID_FILE = new File(PID_FILE_NAME)).exists()) {
            jLogger.info("[RUN] PID FILE [" + PID_FILE_NAME + "] NOT FOUND");
            SpringApplication springApplication = new SpringApplication(Application.class);

            if (!PidFileManager.getEnviroment().equals("dev") && !PidFileManager.getEnviroment().equals("empty")) {
                springApplication.addListeners(new ApplicationPidFileWriter(PID_FILE_NAME));
            }
            springApplication.run(args);
        } else {
            jLogger.info("[RUN] PID FILE [" + PID_FILE_NAME + "] FOUND....RUN NOT VALID");
        }
    }

    /*
     * Verifica della property "spring.config.location" per identificare il nome dell'ambiente (dev/stage/prod)
     */
    private static void setEnviroment() {
        try {
            AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
            String property_conf = (ctx.getEnvironment().getProperty("spring.profiles.active") != null)
                    ? ctx.getEnvironment().getProperty("spring.profiles.active")
                    : ctx.getEnvironment().getProperty("spring.config.location");

            if (property_conf == null) {
                throw new Exception("PROPERTIES FILE NOT FOUND - DESTROY APP");
            }
            property_conf = property_conf.toLowerCase();

            if (property_conf.contains("dev")) {
                ENVIRONMENT = "dev";
            } else if (property_conf.contains("stage")) {
                ENVIRONMENT = "stage";
            } else if (property_conf.contains("prod")) {
                ENVIRONMENT = "prod";
            }
        } catch (Exception e) {
            jLogger.error("Error: " + e.getMessage());
            jLogger.error("Error", e);
        }
    }

    private static String getEnviroment() {
        return ENVIRONMENT;
    }

    public static boolean checkPidFileExist() {
        if (ENVIRONMENT.equalsIgnoreCase("dev") || ENVIRONMENT.equalsIgnoreCase("empty")) {
            return true;
        }
        return PID_FILE.exists();
    }

    public static void shutdownWithPidFile(ApplicationContext _applicationContext, String enviroment, boolean forceShutdown) {
        jLogger.info("******************* [PidFileManager::ShutdownWithPidFile] *******************");
        if (forceShutdown || (!(new File(PID_FILE_NAME)).exists() && !enviroment.equals("dev") && !enviroment.equals("empty"))) {
            SpringApplication.exit(_applicationContext);
            System.exit(-1);
        } else {
            jLogger.info("[PidFileManager::ShutdownWithPidFile] PID FILE EXIST!!!");
        }
        jLogger.info("*****************************************************************************");
    }

    public static void shutdownForce(ApplicationContext _applicationContext) {
        jLogger.info("******************* [PidFileManager::ShutdownForce] *******************");
        if (PID_FILE != null) {
            PID_FILE.delete();
        }
        SpringApplication.exit(_applicationContext);
        System.exit(-1);
        jLogger.info("*****************************************************************************");
    }

    @PreDestroy
    public void destroy() {
        jLogger.info("******************* [APP::DESTROY] *******************");
        File pidFile = new File(PID_FILE_NAME);
        if (pidFile.exists()) {
            jLogger.info("[DESTROY] PID FILE [" + PID_FILE_NAME + "] FOUND - DESTROY");
            pidFile.delete();
        } else {
            jLogger.info("[DESTROY] PID FILE [" + PID_FILE_NAME + "] NOT FOUND");
        }
        jLogger.info("******************************************************");
    }

}
