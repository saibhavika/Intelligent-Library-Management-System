
package library;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LibraryService {

    private List<Book> books = new ArrayList<>();
    private List<Student> students = new ArrayList<>();
    private List<Transaction> transactions = new ArrayList<>();

    // Add book
    public void addBook(Book book) {
        if (findBook(book.getBookId()) != null)
            throw new IllegalArgumentException("Book ID already exists");

        books.add(book);
    }

    // Update book
    public void updateBook(Book updated) {
        Book book = findBook(updated.getBookId());

        if (book == null)
            throw new IllegalArgumentException("Book not found");

        if (updated.getQuantity() < book.getIssued())
            throw new IllegalArgumentException(
                "Quantity cannot be less than issued copies");

        book.setTitle(updated.getTitle());
        book.setAuthor(updated.getAuthor());
        book.setCategory(updated.getCategory());
        book.setQuantity(updated.getQuantity());
    }

    // Delete book
    public void deleteBook(String id) {
        Book book = findBook(id);

        if (book == null)
            throw new IllegalArgumentException("Book not found");

        if (book.getIssued() > 0)
            throw new IllegalArgumentException(
                "Return issued copies before deleting");

        books.remove(book);
    }

    // Search book
    public Book findBook(String id) {
        for (Book book : books) {
            if (book.getBookId().equalsIgnoreCase(id))
                return book;
        }
        return null;
    }

    // Search by title or author
public List<Book> searchBooks(String keyword) {

    List<Book> result = new ArrayList<>();

    String search = keyword.toLowerCase().trim();

    for (Book book : books) {

        if (book.getBookId().toLowerCase().contains(search)
                || book.getTitle().toLowerCase().contains(search)
                || book.getAuthor().toLowerCase().contains(search)
                || book.getCategory().toLowerCase().contains(search)) {

            result.add(book);
        }
    }

    return result;
}

    // Register student
    public void addStudent(Student student) {
        for (Student s : students) {
            if (s.getStudentId().equalsIgnoreCase(
                    student.getStudentId())) {
                throw new IllegalArgumentException(
                    "Student ID already exists");
            }
        }
        students.add(student);
    }

    // Find student
    public Student findStudent(String id) {
        for (Student s : students) {
            if (s.getStudentId().equalsIgnoreCase(id))
                return s;
        }
        return null;
    }

    // Issue book
    public void issueBook(String bookId, String studentId) {
        Book book = findBook(bookId);
        Student student = findStudent(studentId);

        if (book == null)
            throw new IllegalArgumentException("Book not found");

        if (student == null)
            throw new IllegalArgumentException("Student not registered");

        if (book.getAvailable() <= 0)
            throw new IllegalArgumentException("No copies available");

        book.issueBook();

        Transaction transaction = new Transaction(
            UUID.randomUUID().toString(),
            bookId,
            studentId
        );

        transactions.add(transaction);
    }

    // Return book
    public long returnBook(String transactionId) {
        for (Transaction t : transactions) {
            if (t.getTransactionId().equals(transactionId)
                    && !t.isReturned()) {

                Book book = findBook(t.getBookId());

                if (book == null)
                    throw new IllegalStateException("Book not found");

                t.returnBook();
                book.returnBook();

                return t.calculateFine();
            }
        }

        throw new IllegalArgumentException(
            "Active transaction not found");
    }

    public List<Book> getBooks() {
        return books;
    }

    public List<Student> getStudents() {
        return students;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public List<Transaction> getActiveTransactions() {
        List<Transaction> result = new ArrayList<>();

        for (Transaction t : transactions) {
            if (!t.isReturned())
                result.add(t);
        }

        return result;
    }
    public void addLoadedTransaction(Transaction t) {
    transactions.add(t);
}
// Get total copies of a category
public int getCategoryTotal(String category) {
    int total = 0;

    for (Book book : books) {
        if (book.getCategory().equalsIgnoreCase(category)) {
            total += book.getQuantity();
        }
    }

    return total;
}

// Get issued copies of a category
public int getCategoryIssued(String category) {
    int total = 0;

    for (Book book : books) {
        if (book.getCategory().equalsIgnoreCase(category)) {
            total += book.getIssued();
        }
    }

    return total;
}

// Get available copies of a category
public int getCategoryAvailable(String category) {
    return getCategoryTotal(category) - getCategoryIssued(category);
}

// Get returned transactions for a category
public List<Transaction> getReturnedByCategory(String category) {
    List<Transaction> result = new ArrayList<>();

    for (Transaction t : transactions) {
        Book book = findBook(t.getBookId());

        if (book != null
                && book.getCategory().equalsIgnoreCase(category)
                && t.isReturned()) {
            result.add(t);
        }
    }

    return result;
}

// Get active issued transactions for a category
public List<Transaction> getIssuedByCategory(String category) {
    List<Transaction> result = new ArrayList<>();

    for (Transaction t : transactions) {
        Book book = findBook(t.getBookId());

        if (book != null
                && book.getCategory().equalsIgnoreCase(category)
                && !t.isReturned()) {
            result.add(t);
        }
    }

    return result;
}
}