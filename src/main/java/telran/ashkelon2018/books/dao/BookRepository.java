package telran.ashkelon2018.books.dao;

import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;

import telran.ashkelon2018.books.domain.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
	
	Stream<Book> findByAuthorsName(String name);
	
	Stream<Book> findByPublisherPublisherName(String name);

}
