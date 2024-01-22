package com.github.seregamorph.testsmartcontext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Helper bean that logs spring bootstrap and shutdown events.
 *
 * @see SmartDirtiesContextTestExecutionListener
 */
public class SpringContextEventTestLogger implements InitializingBean, DisposableBean {

    private static final Log LOG = LogFactory.getLog(SpringContextEventTestLogger.class);

    private static final ThreadLocal<Class<?>> currentAfterClass = new ThreadLocal<>();

    static void setCurrentAfterClass(Class<?> testClass) {
        currentAfterClass.set(testClass);
    }

    static void resetCurrentAfterClass() {
        currentAfterClass.remove();
    }

    @Override
    public void afterPropertiesSet() {
        LOG.info("Creating context for " + CurrentTestContext.getCurrentTestClassIdentifier());
    }

    @Override
    public void destroy() {
        Class<?> afterClass = currentAfterClass.get();
        if (afterClass == null) {
            // system shutdown hook
            LOG.info("Destroying context (hook)");
        } else {
            // triggered from IntegrationTestRunner.springTestContextAfterTestClass via
            // SmartDirtiesContextTestExecutionListener or spring DirtiesContextTestExecutionListener
            LOG.info("Destroying context for " + afterClass);
        }
    }
}
