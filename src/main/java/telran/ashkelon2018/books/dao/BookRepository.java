package telran.ashkelon2018.books.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import telran.ashkelon2018.books.domain.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

}
