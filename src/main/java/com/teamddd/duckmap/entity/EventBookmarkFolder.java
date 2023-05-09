package com.teamddd.duckmap.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity @Getter
public class EventBookmarkFolder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String image;

    @OneToMany(mappedBy = "eventBookmarkFolder")
    private List<EventBookmark> eventBookmarks = new ArrayList<>();

}
