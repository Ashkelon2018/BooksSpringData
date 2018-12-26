package telran.ashkelon2018.books.service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	@Transactional
	public BookDto removeBook(Long isbn) {
		Book book = bookRepository.findById(isbn).orElse(null);
		if (book == null) {
			return null;
		}
		bookRepository.deleteById(isbn);
		return bookToBookDto(book);
	}

	private BookDto bookToBookDto(Book book) {
		Set<AuthorDto> authors = book.getAuthors().stream()
				.map(this::authorToAuthorDto)
				.collect(Collectors.toSet());
		return new BookDto(book.getIsbn(), book.getTitle(),authors, book.getPublisher().getPublisherName());
	}
	
	private AuthorDto authorToAuthorDto(Author author) {
		return new AuthorDto(author.getName(), author.getBirthDate());
	}

	@Override
	public BookDto getBookByIsbn(Long isbn) {
		Book book = bookRepository.findById(isbn).orElse(null);
		if (book == null) {
			return null;
		}
		return bookToBookDto(book);
	}

	@Override
	@Transactional(readOnly=true)
	public Iterable<BookDto> getBooksByAuthor(String authorName) {
		return bookRepository.findByAuthorsName(authorName)
				.map(this::bookToBookDto)
				.collect(Collectors.toSet());
	}

	@Override
	@Transactional(readOnly=true)
	public Iterable<BookDto> getBooksByPublisher(String publisherName) {
		return bookRepository.findByPublisherPublisherName(publisherName)
				.map(this::bookToBookDto)
				.collect(Collectors.toSet());
	}

	@Override
	@Transactional(readOnly=true)
	public Iterable<AuthorDto> getBookAuthors(Long isbn) {
		Book book = bookRepository.findById(isbn).orElse(null);
		if (book == null) {
			return null;
		}
		return book.getAuthors().stream()
				.map(this::authorToAuthorDto)
				.collect(Collectors.toSet());
	}

	@Override
	public Iterable<String> getPublishersByAuthor(String authorName) {
		return publisherRepository.findPublishersByAuthor(authorName);
	}

}
