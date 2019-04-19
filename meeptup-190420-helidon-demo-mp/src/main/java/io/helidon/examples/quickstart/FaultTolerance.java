package io.helidon.examples.quickstart;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;

/**
 * @author Jose Henrique Ventura 03 Feb 2019
 */
@ApplicationScoped
public class FaultTolerance {
    private static final Logger LOGGER = Logger.getLogger(FaultTolerance.class.getName());

    @Timeout(3000)
    @Retry(maxRetries = 3)
    @Fallback(fallbackMethod = "fallback")
    @CircuitBreaker(requestVolumeThreshold=2, failureRatio=0.5, delay=1000, successThreshold=3)
    public String faultTolerance(){
        return longRunningTask();
    }

    /**
     * 
     * @return Status of the Day
     */
    public String fallback(){
        LOGGER.log(Level.WARNING, "[폴백] A beautiful day");
        return "A beautiful day";
    }

    /**
     * Simulate a long processing task of 80ms
     * 
     * @return null
     */
    private String longRunningTask(){
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            LOGGER.log(Level.WARNING,"[FaultTolerance] 타임 아웃 오류 발생.");
        }
        return null;
    }
}