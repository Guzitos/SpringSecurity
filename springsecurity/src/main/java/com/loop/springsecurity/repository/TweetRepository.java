package com.loop.springsecurity.repository;

import com.loop.springsecurity.entities.Tweet;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TweetRepository  extends JpaRepository<Tweet, Long> {

}
