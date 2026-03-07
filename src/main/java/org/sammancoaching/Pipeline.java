package org.sammancoaching;

import org.sammancoaching.dependencies.Config;
import org.sammancoaching.dependencies.Emailer;
import org.sammancoaching.dependencies.Logger;
import org.sammancoaching.dependencies.Project;

public class Pipeline {
    private final Config config;
    private final Emailer emailer;
    private final Logger log;
    private final PipelineMessenger messenger; // Nova dependência injetada ou instanciada

    private record PipelineStatus(boolean testsPassed, boolean deploySuccessful) {}

    public Pipeline(Config config, Emailer emailer, Logger log) {
        this.config = config;
        this.emailer = emailer;
        this.log = log;
        this.messenger = new PipelineMessenger();
    }

    public void run(Project project) {
        boolean testsPassed = executeTests(project);
        boolean deploySuccessful = executeDeployment(project, testsPassed);
        sendNotification(new PipelineStatus(testsPassed, deploySuccessful));
    }

    private boolean executeTests(Project project) {
        if (!project.hasTests()) {
            log.info(PipelineMessenger.MSG_NO_TESTS);
            return true;
        }

        if (PipelineMessenger.SUCCESS_STATUS.equals(project.runTests())) {
            log.info(PipelineMessenger.MSG_TESTS_PASSED);
            return true;
        }

        log.error(PipelineMessenger.MSG_TESTS_FAILED);
        return false;
    }

    private boolean executeDeployment(Project project, boolean testsPassed) {
        if (!testsPassed) return false;

        if (PipelineMessenger.SUCCESS_STATUS.equals(project.deploy())) {
            log.info(PipelineMessenger.MSG_DEPLOY_SUCCESS);
            return true;
        }

        log.error(PipelineMessenger.MSG_DEPLOY_FAILED);
        return false;
    }

    private void sendNotification(PipelineStatus status) {
        if (!config.sendEmailSummary()) {
            log.info(PipelineMessenger.MSG_EMAIL_DISABLED);
            return;
        }

        log.info(PipelineMessenger.MSG_SENDING_EMAIL);

        String message = messenger.getNotificationMessage(status.testsPassed(), status.deploySuccessful());
        emailer.send(message);
    }
}