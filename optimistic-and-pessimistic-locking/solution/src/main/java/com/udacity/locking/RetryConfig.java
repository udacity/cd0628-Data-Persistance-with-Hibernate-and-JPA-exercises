package com.udacity.locking;

import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

/**
 * Spring Retry is enabled on the Application class via @EnableRetry.
 * Optimistic update methods use @Retryable directly on the method to
 * declare backoff and max attempts. No additional bean wiring needed
 * here; this class exists as a placeholder for any future custom
 * RetryTemplate or RetryListener.
 */
@Configuration
public class RetryConfig {
}