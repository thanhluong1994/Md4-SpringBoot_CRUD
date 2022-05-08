package com.codegym.bookcategory.controller;


import com.codegym.bookcategory.model.Book;
import com.codegym.bookcategory.model.Category;
import com.codegym.bookcategory.service.book.IBookService;
import com.codegym.bookcategory.service.category.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/books")
@CrossOrigin("*")
public class BookController {
    @Autowired
    private IBookService bookService;
    @Autowired
    private ICategoryService categoryService;

    @ModelAttribute("categories")
    private Iterable<Category> categories(){
        return categoryService.findAll();
    }
    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book){
        return new ResponseEntity<>(bookService.save(book), HttpStatus.CREATED);
    }
    @GetMapping("/list")
    public  ResponseEntity<Iterable<Book>> showAll(){
        return new ResponseEntity<>(bookService.findAll(),HttpStatus.OK);
    }

    @GetMapping("/category")
    public ResponseEntity<Iterable<Category>> showCate(){
        return  new ResponseEntity<>(categoryService.findAll(),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Book> deleteBook(@PathVariable Long id) {
        Optional<Book> bookOptional = bookService.findById(id);
        if (!bookOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        bookService.remove(id);
        return new ResponseEntity<>(bookOptional.get(), HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book){
        Optional<Book> bookOptional = bookService.findById(id);
        if (!bookOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        book.setId(bookOptional.get().getId());
        return new ResponseEntity<>(bookService.save(book), HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> findOne(@PathVariable Long id){
        return new ResponseEntity<>(bookService.findById(id).get(),HttpStatus.OK);
    }

}
