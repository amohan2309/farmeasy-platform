package com.farmeasy.catalog.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "subtopics", schema = "catalog")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subtopic {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID topicId;

    private String titleEn;
    private String titleHi;
    private String contentType;
    private String contentUrl;
    private int sortOrder;
}
