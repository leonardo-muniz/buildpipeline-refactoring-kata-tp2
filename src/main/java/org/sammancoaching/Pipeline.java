package org.sammancoaching;

import org.sammancoaching.dependencies.Config;
import org.sammancoaching.dependencies.Emailer;
import org.sammancoaching.dependencies.Logger;
import org.sammancoaching.dependencies.Project;

public class Pipeline {
    // Definindo constantes para evitar Magic Strings
    private static final String SUCCESS_STATUS = "success";
    private static final String MSG_NO_TESTS = "No tests";
    private static final String MSG_TESTS_PASSED = "Tests passed";
    private static final String MSG_TESTS_FAILED = "Tests failed";
    private static final String MSG_DEPLOY_SUCCESS = "Deployment successful";
    private static final String MSG_DEPLOY_FAILED = "Deployment failed";
    private static final String MSG_SENDING_EMAIL = "Sending email";
    private static final String MSG_EMAIL_DISABLED = "Email disabled";
    private static final String MSG_EMAIL_SUCCESS = "Deployment completed successfully";

    private final Config config;
    private final Emailer emailer;
    private final Logger log;

    public Pipeline(Config config, Emailer emailer, Logger log) {
        this.config = config;
        this.emailer = emailer;
        this.log = log;
    }

    public void run(Project project) {
        boolean testsPassed = executeTests(project);
        boolean deploySuccessful = executeDeployment(project, testsPassed);
        sendNotification(testsPassed, deploySuccessful);
    }

    private boolean executeTests(Project project) {
        if (!project.hasTests()) {
            log.info(MSG_NO_TESTS);
            return true;
        }

        boolean isTestRunSuccessful = SUCCESS_STATUS.equals(project.runTests());

        if (isTestRunSuccessful) {
            log.info(MSG_TESTS_PASSED);
            return true;
        }

        log.error(MSG_TESTS_FAILED);
        return false;
    }

    private boolean executeDeployment(Project project, boolean testsPassed) {
        if (!testsPassed) {
            return false;
        }

        boolean isDeploySuccessful = SUCCESS_STATUS.equals(project.deploy());

        if (isDeploySuccessful) {
            log.info(MSG_DEPLOY_SUCCESS);
            return true;
        }

        log.error(MSG_DEPLOY_FAILED);
        return false;
    }

    private void sendNotification(boolean testsPassed, boolean deploySuccessful) {
        if (!config.sendEmailSummary()) {
            log.info(MSG_EMAIL_DISABLED);
            return;
        }

        log.info(MSG_SENDING_EMAIL); // Agora usa constante

        if (!testsPassed) {
            emailer.send(MSG_TESTS_FAILED);
            return;
        }

        String finalMessage = deploySuccessful ? MSG_EMAIL_SUCCESS : MSG_DEPLOY_FAILED;
        emailer.send(finalMessage);
    }
}