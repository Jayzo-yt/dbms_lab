package com.gym.gym_backend.common.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuditLogService {

    public void log(
            String action,
            UUID performedBy,
            UUID referenceId,
            String description
    ) {

        System.out.println("====== AUDIT LOG ======");
        System.out.println("Action: " + action);
        System.out.println("Performed By: " + performedBy);
        System.out.println("Reference ID: " + referenceId);
        System.out.println("Description: " + description);
        System.out.println("=======================");

        // Later Dev 6 will save this into audit_logs table
    }
}