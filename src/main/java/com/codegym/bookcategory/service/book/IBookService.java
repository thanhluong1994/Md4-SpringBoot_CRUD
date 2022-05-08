package com.codegym.bookcategory.service.book;

import com.codegym.bookcategory.model.Book;
import com.codegym.bookcategory.service.IGeneralService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IBookService extends IGeneralService<Book> {
    Page<Book> findAll(Pageable pageable);

    Page<Book>findAllByName(String name,Pageable pageable);
}
