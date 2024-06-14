package com.dhiman.spring.htmx.spring.htmx;

import java.util.UUID;

public record TaskRecord(String id ,
                         String status,
                         String name,
                         double score) {
}
