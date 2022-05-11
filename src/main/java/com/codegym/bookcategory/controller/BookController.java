package com.codegym.bookcategory.controller;


import com.codegym.bookcategory.model.Book;
import com.codegym.bookcategory.model.BookForm;
import com.codegym.bookcategory.model.Category;
import com.codegym.bookcategory.service.book.IBookService;
import com.codegym.bookcategory.service.category.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/books")
@CrossOrigin("*")
public class BookController {
    @Autowired
    private IBookService bookService;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    Environment env;

    @ModelAttribute("categories")
    private Iterable<Category> categories(){
        return categoryService.findAll();
    }
//    @PostMapping
//    public ResponseEntity<Book> createBook(@RequestBody Book book){
//        return new ResponseEntity<>(bookService.save(book), HttpStatus.CREATED);
//    }
    @GetMapping("/list")
    public  ResponseEntity<Iterable<Book>> showAll(){
        return new ResponseEntity<>(bookService.findAll(),HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<Book> saveBook(@ModelAttribute BookForm bookForm){
        MultipartFile multipartFile=bookForm.getImage();
        String fileName= multipartFile.getOriginalFilename();
        String fileUpload=env.getProperty("upload.path");
        try {
            FileCopyUtils.copy(multipartFile.getBytes(),new File(fileUpload+fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Book book=new Book(bookForm.getName(),bookForm.getPrice(),bookForm.getAuthor(),fileName,bookForm.getCategory());
        bookService.save(book);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
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

    @PostMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @ModelAttribute BookForm bookForm){
        Optional<Book> bookOptional = bookService.findById(id);
        bookForm.setId(bookOptional.get().getId());
        MultipartFile multipartFile=bookForm.getImage();
        String fileName= multipartFile.getOriginalFilename();
        String fileUpload=env.getProperty("upload.path");
        Book existBook=new Book(id,bookForm.getName(),bookForm.getPrice(),bookForm.getAuthor(),fileName,bookForm.getCategory());
        try {
            FileCopyUtils.copy(multipartFile.getBytes(),new File(fileUpload+fileName));
        }catch (IOException e){
            e.printStackTrace();
        }
        if (existBook.getImage().equals("filename.jpg")) {
           existBook.setImage(bookOptional.get().getImage());
        }

        return new ResponseEntity<>(bookService.save(existBook),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> findOne(@PathVariable Long id){
        return new ResponseEntity<>(bookService.findById(id).get(),HttpStatus.OK);
    }

}
