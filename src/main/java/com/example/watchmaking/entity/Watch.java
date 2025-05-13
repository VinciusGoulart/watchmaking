package com.example.watchmaking.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "watches")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Watch extends Product {

    @Column(nullable = false)
    private String reference;

    @Column(nullable = false)
    private String mechanism;

    private String gender;
    private String material;
    private String waterResistance;
    private String dialColor;
    private String strapMaterial;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_uuid")
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "watch_type_uuid")
    private WatchType watchType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_uuid")
    private WatchCategory watchCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_uuid")
    private Storage image;
}
