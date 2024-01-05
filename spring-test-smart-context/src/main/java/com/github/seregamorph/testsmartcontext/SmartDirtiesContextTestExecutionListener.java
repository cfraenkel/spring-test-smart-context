package com.github.seregamorph.testsmartcontext;

import com.github.seregamorph.testsmartcontext.testng.SmartDirtiesSuiteListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

/**
 * Listener that works in more tricky way than spring
 * {@link org.springframework.test.context.support.DirtiesContextTestExecutionListener}.
 * Based on known list (ordered) of tests to execute (obtained in {@link SmartDirtiesSuiteListener}),
 * the last test in each group that share the same configuration (=share the same spring context) will automatically
 * close the ApplicationContext, which will release resources as well (like Docker containers).
 *
 * @see SmartDirtiesSuiteListener
 * @see SpringContextEventTestLogger
 */
public class SmartDirtiesContextTestExecutionListener extends AbstractTestExecutionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmartDirtiesContextTestExecutionListener.class);

    @Override
    public int getOrder() {
        // DirtiesContextTestExecutionListener.getOrder() + 1
        //noinspection MagicNumber
        return 3001;
    }

    @Override
    public void afterTestClass(TestContext testContext) {
        Class<?> testClass = testContext.getTestClass();
        if (SmartDirtiesTestsHolder.isLastClassPerConfig(testClass)) {
            LOGGER.info("markDirty {}", testClass.getName());
            testContext.markApplicationContextDirty(null);
        }
    }
}
