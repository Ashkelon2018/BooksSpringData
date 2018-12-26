package telran.ashkelon2018.books.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import telran.ashkelon2018.books.domain.Publisher;

public interface PublisherRepository extends JpaRepository<Publisher, String> {
	
	@Query(value="select distinct b.publisher_publisher_name from book b join book_authors ba on b.isbn=ba.books_isbn\r\n" + 
			"where ba.authors_name=?1", nativeQuery=true)
	Iterable<String> findPublishersByAuthor(String author);
}
