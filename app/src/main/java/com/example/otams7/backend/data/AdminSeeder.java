package com.example.otams7.backend.data;


/** Seeds the Administrator account from README creds. */
public final class AdminSeeder {
    private AdminSeeder() {}

    public static void seed(InMemoryAuthRepository repo,
                            String adminEmail,
                            String adminPassword) {
        repo.seedAdmin(adminEmail, adminPassword);
    }
}