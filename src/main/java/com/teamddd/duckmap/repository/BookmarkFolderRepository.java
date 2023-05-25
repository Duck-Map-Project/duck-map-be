package com.teamddd.duckmap.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.teamddd.duckmap.entity.EventBookmarkFolder;

public interface BookmarkFolderRepository extends JpaRepository<EventBookmarkFolder, Long> {
}
