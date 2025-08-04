package com.opencart.utils;

import java.util.List;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class UserPoolManager {

    private static final List<String> users = List.of(
            ConfigReader.getProperty("testUserEmail1"),
            ConfigReader.getProperty("testUserEmail2"),
            ConfigReader.getProperty("testUserEmail3"),
            ConfigReader.getProperty("testUserEmail4"),
            ConfigReader.getProperty("testUserEmail5")
    );

    private static final BlockingQueue<String> availableUsers = new LinkedBlockingQueue<>(users);
    private static final ThreadLocal<String> threadUser = new ThreadLocal<>();

    // ⏳ Will WAIT until a user is available
    public static String acquireUser() {
        try {
            String user = availableUsers.take(); // Waits if empty
            threadUser.set(user);
            System.out.println("[USER POOL] ✅ Acquired: " + user + " on thread " + Thread.currentThread().getName());
            return user;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while waiting for test user", e);
        }
    }

    public static void releaseUser() {
        String user = threadUser.get();
        if (user != null) {
            availableUsers.offer(user);
            threadUser.remove();
            System.out.println("[USER POOL] ♻️ Released: " + user + " from thread " + Thread.currentThread().getName());
        }
    }

    public static String getCurrentUser() {
        return threadUser.get();
    }
}
