package org.sammancoaching;

public class PipelineMessenger {
    public static final String SUCCESS_STATUS = "success";
    public static final String MSG_NO_TESTS = "No tests";
    public static final String MSG_TESTS_PASSED = "Tests passed";
    public static final String MSG_TESTS_FAILED = "Tests failed";
    public static final String MSG_DEPLOY_SUCCESS = "Deployment successful";
    public static final String MSG_DEPLOY_FAILED = "Deployment failed";
    public static final String MSG_SENDING_EMAIL = "Sending email";
    public static final String MSG_EMAIL_DISABLED = "Email disabled";
    public static final String MSG_EMAIL_SUCCESS = "Deployment completed successfully";

    public String getNotificationMessage(boolean testsPassed, boolean deploySuccessful) {
        if (!testsPassed) {
            return MSG_TESTS_FAILED;
        }
        return deploySuccessful ? MSG_EMAIL_SUCCESS : MSG_DEPLOY_FAILED;
    }
}