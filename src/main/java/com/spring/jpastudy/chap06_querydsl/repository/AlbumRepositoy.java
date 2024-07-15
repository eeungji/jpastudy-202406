package com.spring.jpastudy.chap06_querydsl.repository;

import com.spring.jpastudy.chap06_querydsl.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepositoy extends JpaRepository<Album, Long> {

}
