package com.opencart.utils;

import com.opencart.ui.base.BaseTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class UserPoolManager {
    private static final Logger logger = LoggerFactory.getLogger(UserPoolManager.class);

    private static final List<String> users = List.of(
            ConfigReader.getProperty("testUserEmail1"),
            ConfigReader.getProperty("testUserEmail2")
//            ConfigReader.getProperty("testUserEmail3"),
//            ConfigReader.getProperty("testUserEmail4"),
//            ConfigReader.getProperty("testUserEmail5")
    );

    private static final BlockingQueue<String> availableUsers = new LinkedBlockingQueue<>(users);
    private static final ThreadLocal<String> threadUser = new ThreadLocal<>();

    /**
     * Acquire a user from the pool (waits if empty)
     */
    public static String acquireUser() {
        try {
            String user = availableUsers.take(); // Will block until available
            threadUser.set(user);
            logger.info("[THREAD: {}] ✅ Acquired user: {}", Thread.currentThread().getName(), user);
            return user;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while waiting for test user", e);
        }
    }

    /**
     * Release user back to pool
     */
    public static void releaseUser() {
        String user = threadUser.get();
        if (user != null) {
            availableUsers.offer(user);
            threadUser.remove();
            logger.info("[THREAD: {}] ♻️ Released user: {}", Thread.currentThread().getName(), user);
        } else {
            logger.warn("[THREAD: {}] Tried to release user but none was found for this thread", Thread.currentThread().getName());
        }
    }

    /**
     * Get current thread’s user
     */
    public static String getCurrentUser() {
        return threadUser.get();
    }
}
