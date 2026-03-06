package org.sammancoaching;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.sammancoaching.dependencies.Config;
import org.sammancoaching.dependencies.Emailer;
import org.sammancoaching.dependencies.Logger;
import org.sammancoaching.dependencies.Project;

class PipelineTest {

    private Config config;
    private Emailer emailer;
    private Logger logger;
    private Project project;
    private Pipeline pipeline;

    @BeforeEach
    void setUp() {
        config = mock(Config.class);
        emailer = mock(Emailer.class);
        logger = mock(Logger.class);
        project = mock(Project.class);

        pipeline = new Pipeline(config, emailer, logger);
    }

    @Test
    void givenProjectWithTestsAndSuccessfulBuild_whenRun_shouldSendSuccessEmail() {
        when(project.hasTests()).thenReturn(true);
        when(project.runTests()).thenReturn("success");
        when(project.deploy()).thenReturn("success");
        when(config.sendEmailSummary()).thenReturn(true);

        pipeline.run(project);

        verify(logger).info("Tests passed");
        verify(logger).info("Deployment successful");
        verify(logger).info("Sending email");
        verify(emailer).send("Deployment completed successfully");
    }

    @Test
    void givenProjectWithFailingTests_whenRun_shouldNotDeployAndSendFailureEmail() {
        when(project.hasTests()).thenReturn(true);
        when(project.runTests()).thenReturn("failure");
        when(config.sendEmailSummary()).thenReturn(true);

        pipeline.run(project);

        verify(logger).error("Tests failed");
        verify(project, never()).deploy();
        verify(emailer).send("Tests failed");
    }

    @Test
    void givenProjectWithoutTestsAndFailingDeploy_whenRun_shouldSendDeployFailureEmail() {
        when(project.hasTests()).thenReturn(false);
        when(project.deploy()).thenReturn("error");
        when(config.sendEmailSummary()).thenReturn(true);

        pipeline.run(project);

        verify(logger).info("No tests");
        verify(logger).error("Deployment failed");
        verify(emailer).send("Deployment failed");
    }

    @Test
    void givenEmailDisabled_whenRun_shouldOnlyLogEmailDisabled() {
        when(project.hasTests()).thenReturn(false);
        when(project.deploy()).thenReturn("success");
        when(config.sendEmailSummary()).thenReturn(false);

        pipeline.run(project);

        verify(logger).info("Email disabled");
        verify(emailer, never()).send(anyString());
    }

}
