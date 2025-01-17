package com.libraryManagement.Book;



import com.libraryManagement.Librarian.IssuedBookRepository;
import com.libraryManagement.Librarian.IssuedBooks;
import com.libraryManagement.Librarian.ReturnBookRepository;
import com.libraryManagement.Librarian.ReturnBooks;
import com.libraryManagement.Category.Category;
import com.libraryManagement.Category.CategoryRepository;
import com.libraryManagement.Language.Language;
import com.libraryManagement.Language.LanguageRepository;
import com.libraryManagement.User.User;
import com.libraryManagement.User.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private IssuedBookRepository issuedBookRepository;
    @Autowired
    private ReturnBookRepository returnBookRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private LanguageRepository languageRepository;

    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    public List<Book> getBookByCategory(Long categoryId) {
        return bookRepository.findByCategoryId(categoryId);
    }

    public List<Book> getBookByLanguage(Long languageId) {
        return bookRepository.findByLanguageId(languageId);
    }

    public List<Book> getBookByCategoryAndLanguage(Long categoryId, Long languageId) {
        return bookRepository.findByCategoryIdAndLanguageId(categoryId,languageId);
    }

//    public List<Book> getBookByAuthor(String author) {
//        return bookRepository.findByAuthor(author);
//    }
//
//    public Book newBook(Book book)
//    {
//        return bookRepository.save(book);
//    }

    @Transactional
    public Book updateBook(Long id, Book book)
    {
        Optional<Book> existingBook = bookRepository.findById(id);
        if(existingBook.isPresent())
        {
            Book updatedBook = existingBook.get();
            updatedBook.setTitle(book.getTitle());
            updatedBook.setAuthor(book.getAuthor());
            updatedBook.setQuantity(book.getQuantity());
            return bookRepository.save(updatedBook);
        }
        throw new RuntimeException("Book not found with id" + id);
    }

    @Transactional
    public ResponseEntity<?> categoryUpdate(Long id, Long categoryId, Long newCategoryId) {
        Optional<Book>existingBook = bookRepository.findByIdAndCategoryId(id,categoryId);

        if(existingBook.isPresent())
        {
            Book updatedBook = existingBook.get();
            Optional<Category>categoryOptional= categoryRepository.findById(newCategoryId);
            if (categoryOptional.isPresent()){
                Category category = categoryOptional.get();
                updatedBook.setCategoryId(category.getId());
                updatedBook.setCategory(category.getCategoryName());
                bookRepository.save(updatedBook);
                return new ResponseEntity<>(updatedBook,HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
    }

//    public ResponseEntity<?> bookCategoryUpdATE(Long id, Long categoryId, Long newCategoryId) {
//        Optional<Book>bookOptional=bookRepository.findByIdAndCategoryId(id,categoryId);
//        if (bookOptional.isPresent()){
//            Book book =bookOptional.get();
//            Optional<Category>categoryOptional= categoryRepository.findById(newCategoryId);
//            if (categoryOptional.isPresent()){
//                Category category = categoryOptional.get();
//                book.setCategoryId(category.getId());
//                book.setCategory(category.getCategoryName());
//                bookRepository.save(book);
//                return new ResponseEntity<>(book,HttpStatus.OK);
//            }
//        }
//        return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
//    }

    public ResponseEntity<?> languageUpdate(Long id, Long languageId, Long newLanguageId) {
        Optional<Book> existingBook = bookRepository.findByIdAndLanguageId(id,languageId);

        if(existingBook.isPresent())
        {
            Book updatedBook = existingBook.get();
            Optional<Language> languageOptional = languageRepository.findById(newLanguageId);
            if(languageOptional.isPresent()){
                Language language = languageOptional.get();
                updatedBook.setLanguageId(language.getId());
                updatedBook.setLanguage(language.getLanguage());
                bookRepository.save(updatedBook);
                return new ResponseEntity<>(updatedBook,HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
    }
//    @Transactional
//    public Book updateBook(Long id, Book book) //Dynamic Update
//    {
//        Optional<Book> existingBook = bookRepository.findById(id);
//        if(existingBook.isPresent())
//        {
//            Book updatedBook = existingBook.get();
//            if(book.getTitle() != null){
//                updatedBook.setTitle(book.getTitle());
//            }
//            if(book.getAuthor() != null){
//                updatedBook.setAuthor(book.getAuthor());
//            }
//            if(book.getCategory() != null){
//                updatedBook.setCategory(book.getCategory());
//            }
//            if(book.getCategoryId() != null){
//                updatedBook.setCategoryId(book.getCategoryId());
//            }
//            if(book.getLanguage() != null){
//                updatedBook.setLanguage(book.getLanguage());
//            }
//            if(book.getLanguageId() != null) {
//                updatedBook.setLanguageId(book.getLanguageId());
//            }
//            return bookRepository.save(updatedBook);
//        }
//        throw new RuntimeException("Book not found with id" + id);
//    }

    public ResponseEntity<?> addBook(Long categoryId,Long languageId, Book book) {
        Book book1 = new Book();
        Optional<Category>categoryOptional=categoryRepository.findById(categoryId);
        Optional<Language> languageOptional = languageRepository.findById(languageId);
        if (categoryOptional.isPresent() && languageOptional.isPresent()){
            Category category = categoryOptional.get();
            Language language = languageOptional.get();
            book1.setCategoryId(categoryId);
            book1.setCategory(category.getCategoryName());
            book1.setLanguageId(languageId);
            book1.setLanguage(language.getLanguage());
            book1.setTitle(book.getTitle());
            book1.setAuthor(book.getAuthor());
            book1.setQuantity(book.getQuantity());
            bookRepository.save(book1);
            return new ResponseEntity<>(book1,HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Category or Language isn't present", HttpStatus.BAD_REQUEST);
        }
        //return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public void deleteBookById(Long id) 
    {
        bookRepository.deleteById(id);
    }

    @Transactional
    public String issueBook(Long bookId, Long userId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        Optional<User> userOptional = userRepository.findById(userId);

        if(bookOptional.isEmpty())
        {
            return "Book not found";
        }
        if(userOptional.isEmpty())
        {
            return "User not found";
        }

        Book book = bookOptional.get();
        User user = userOptional.get();

        if(book.getQuantity()<=0)
        {
            return "Book is out of stock";
        }

        book.setQuantity(book.getQuantity()-1);
        bookRepository.save(book);

        IssuedBooks issuedBooks = new IssuedBooks();
        issuedBooks.setBook_Id(book.getId());
        issuedBooks.setUserId(user.getId());
        issuedBooks.setIssueDate(LocalDate.now());
        issuedBookRepository.save(issuedBooks);

        return "Book issued successfully to"+user.getName()+".";
    }

    @Transactional
    public String returnBook(Long userId, Long bookId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        Optional<User> userOptional = userRepository.findById(userId);

        if(bookOptional.isEmpty())
        {
            return  "Book not found with ID: "+bookId;
        }
        if(userOptional.isEmpty())
        {
            return "User not found";
        }
        Book book = bookOptional.get();
        User user = userOptional.get();

        book.setQuantity(book.getQuantity()+1);
        bookRepository.save(book);

        ReturnBooks returnBooks = new ReturnBooks();
        returnBooks.setBookId(book.getId());
        returnBooks.setUserId(user.getId());
        returnBooks.setReturnDate(LocalDate.now());
        returnBookRepository.save(returnBooks);

        return "Book returned successfully ";

    }


    public ResponseEntity<List<Book>> getBookByAuthor(String author) {
        return new ResponseEntity<>(bookRepository.findByAuthorContainingIgnoreCase(author),HttpStatus.OK);
    }
}
