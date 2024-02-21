package com.garakaniirhf.spring.data.jpa.pagingsorting.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.garakaniirhf.spring.data.jpa.pagingsorting.model.Book;
import com.garakaniirhf.spring.data.jpa.pagingsorting.repository.BookRepository;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api")
public class BookController {

  @Autowired
  BookRepository bookRepository;

  private Sort.Direction getSortDirection(String direction) {
    if (direction.equals("asc")) {
      return Sort.Direction.ASC;
    } else if (direction.equals("desc")) {
      return Sort.Direction.DESC;
    }

    return Sort.Direction.ASC;
  }

  @GetMapping("/sortedbooks")
  public ResponseEntity<List<Book>> getAllBooks(@RequestParam(defaultValue = "id,desc") String[] sort) {

    try {
      List<Order> orders = new ArrayList<Order>();

      if (sort[0].contains(",")) {
        // will sort more than 2 fields
        // sortOrder="field, direction"
        for (String sortOrder : sort) {
          String[] _sort = sortOrder.split(",");
          orders.add(new Order(getSortDirection(_sort[1]), _sort[0]));
        }
      } else {
        // sort=[field, direction]
        orders.add(new Order(getSortDirection(sort[1]), sort[0]));
      }

      List<Book> books = bookRepository.findAll(Sort.by(orders));

      if (books.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(books, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/books")
  public ResponseEntity<Map<String, Object>> getAllBooksPage(
      @RequestParam(required = false) String title,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "3") int size,
      @RequestParam(defaultValue = "id,desc") String[] sort) {

    try {
      List<Order> orders = new ArrayList<Order>();

      if (sort[0].contains(",")) {
        // will sort more than 2 fields
        // sortOrder="field, direction"
        for (String sortOrder : sort) {
          String[] _sort = sortOrder.split(",");
          orders.add(new Order(getSortDirection(_sort[1]), _sort[0]));
        }
      } else {
        // sort=[field, direction]
        orders.add(new Order(getSortDirection(sort[1]), sort[0]));
      }

      List<Book> books = new ArrayList<Book>();
      Pageable pagingSort = PageRequest.of(page, size, Sort.by(orders));

      Page<Book> pageTuts;
      if (title == null)
        pageTuts = bookRepository.findAll(pagingSort);
      else
        pageTuts = bookRepository.findByCaptionContaining(title, pagingSort);

      books = pageTuts.getContent();

      Map<String, Object> response = new HashMap<>();
      response.put("books", books);
      response.put("currentPage", pageTuts.getNumber());
      response.put("totalItems", pageTuts.getTotalElements());
      response.put("totalPages", pageTuts.getTotalPages());

      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/books/rated")
  public ResponseEntity<Map<String, Object>> findByRated(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "3") int size) {
    
    try {
      List<Book> books = new ArrayList<Book>();
      Pageable paging = PageRequest.of(page, size);

      Page<Book> pageTuts = bookRepository.findByRated(true, paging);
      books = pageTuts.getContent();

      Map<String, Object> response = new HashMap<>();
      response.put("books", books);
      response.put("currentPage", pageTuts.getNumber());
      response.put("totalItems", pageTuts.getTotalElements());
      response.put("totalPages", pageTuts.getTotalPages());

      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/books/{id}")
  public ResponseEntity<Book> getBookById(@PathVariable("id") long id) {
    Optional<Book> bookData = bookRepository.findById(id);
    System.out.println("test");

    if (bookData.isPresent()) {
      return new ResponseEntity<>(bookData.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping("/books")
  public ResponseEntity<Book> createBook(@RequestBody Book book) {
    try {
      Book _book = bookRepository.save(new Book(book.getCaption(), book.getComment(), book.isRated()));
      return new ResponseEntity<>(_book, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping("/books/{id}")
  public ResponseEntity<Book> updateBook(@PathVariable("id") long id, @RequestBody Book book) {
    Optional<Book> bookData = bookRepository.findById(id);

    if (bookData.isPresent()) {
      Book _book = bookData.get();
      _book.setCaption(book.getCaption());
      _book.setComment(book.getComment());
      _book.setRated(book.isRated());
      return new ResponseEntity<>(bookRepository.save(_book), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/books/{id}")
  public ResponseEntity<HttpStatus> deleteBook(@PathVariable("id") long id) {
    try {
      bookRepository.deleteById(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/books")
  public ResponseEntity<HttpStatus> deleteAllBooks() {
    try {
      bookRepository.deleteAll();
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

  }
}
