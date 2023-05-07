package com.teamddd.duckmap.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity @Getter
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Artist group;

    private String username;
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="artist_type_id")
    private ArtistType artistType;


}
