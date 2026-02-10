package com.keyswitch.keyswitchwiki.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "switches")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Switch {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SwitchType switchType;

    // Technical specs
    private Integer actuationForceGrams;
    private BigDecimal actuationPointMm;
    private BigDecimal totalTravelMm;
    private String springType;

    // Sound/Feel characteristics
    private String soundProfile;
    private String smoothness;

    // Practical info
    private BigDecimal typicalPriceUsd;
    private Boolean isFactoryLubed = false;
    private Boolean isRgbCompatible = false;
    private Integer durabilityCycles;

    // Content
    @Column(length = 2000)
    private String description;

    private String audioFilePath;
    private String imagePath;
    @OneToMany(mappedBy = "switchEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExternalLink> externalLinks = new ArrayList<>();

    // Metadata
    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
