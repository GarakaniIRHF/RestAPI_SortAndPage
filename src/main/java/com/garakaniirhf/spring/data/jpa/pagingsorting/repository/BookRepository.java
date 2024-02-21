package com.garakaniirhf.spring.data.jpa.pagingsorting.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.garakaniirhf.spring.data.jpa.pagingsorting.model.Book;
import com.garakaniirhf.spring.data.jpa.pagingsorting.model.Tutorial;

public interface BookRepository extends JpaRepository<Book, Long> {
	  Page<Book> findByRated(boolean rated, Pageable pageable);

	  Page<Book> findByCaptionContaining(String caption, Pageable pageable);
	  
	  List<Book> findByCaptionContaining(String caption, Sort sort);
	}

