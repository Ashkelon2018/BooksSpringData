package telran.ashkelon2018.books.service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import telran.ashkelon2018.books.dao.AuthorRepository;
import telran.ashkelon2018.books.dao.BookRepository;
import telran.ashkelon2018.books.dao.PublisherRepository;
import telran.ashkelon2018.books.domain.Author;
import telran.ashkelon2018.books.domain.Book;
import telran.ashkelon2018.books.domain.Publisher;
import telran.ashkelon2018.books.dto.AuthorDto;
import telran.ashkelon2018.books.dto.BookDto;

@Service
public class BookServiceImpl implements BookService {
	
	@Autowired
	BookRepository bookRepository;
	
	@Autowired
	AuthorRepository authorRepository;
	
	@Autowired
	PublisherRepository publisherRepository;

	@Override
	@Transactional
	public boolean addBook(BookDto bookDto) {
		if(bookRepository.existsById(bookDto.getIsbn())) {
			return false;
		}
		//Publisher
		String publisherName = bookDto.getPublisher();
		Publisher publisher = 
				publisherRepository.findById(publisherName)
				.orElse(publisherRepository.save(new Publisher(publisherName)));
		//Set<Author>
		Set<AuthorDto> authorDtos = bookDto.getAuthors();
		Set<Author> authors = new HashSet<>();
		for (AuthorDto authorDto : authorDtos) {
			Author author = 
					authorRepository.findById(authorDto.getName()).orElse(null);
			if (author == null) {
				author = 
						new Author(authorDto.getName(), authorDto.getBirthDate());
				authorRepository.save(author);
			}
			authors.add(author);
		}
//		Set<Author> authors = authorDtos.stream()
//				.map(a -> authorRepository.findById(a.getName()).orElse(authorRepository.save(new Author(a.getName(), a.getBirthDate()))))
//				.collect(Collectors.toSet());
		
		Book book = 
				new Book(bookDto.getIsbn(), bookDto.getTitle(), authors, publisher);
		bookRepository.save(book);
		return true;
	}

	@Override
	public BookDto removeBook(Long isbn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BookDto getBookByIsbn(Long isbn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<BookDto> getBooksByAuthor(String authorName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<BookDto> getBooksByPublisher(String publisherName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<AuthorDto> getBookAuthors(Long isbn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<String> getPublishersByAuthor(String authorName) {
		// TODO Auto-generated method stub
		return null;
	}

}
