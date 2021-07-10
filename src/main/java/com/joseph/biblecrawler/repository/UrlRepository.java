package com.joseph.biblecrawler.repository;

import com.joseph.biblecrawler.model.URL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRepository extends JpaRepository<URL, Long> {

}
