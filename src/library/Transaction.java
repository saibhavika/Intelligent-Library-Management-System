
package library;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Transaction {
    private String transactionId;
    private String bookId;
    private String studentId;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;

    public Transaction(String transactionId, String bookId,
                       String studentId) {
        this.transactionId = transactionId;
        this.bookId = bookId;
        this.studentId = studentId;
        this.issueDate = LocalDate.now();
        this.dueDate = issueDate.plusDays(14);
        this.returnDate = null;
    }

    public String getTransactionId() { return transactionId; }
    public String getBookId() { return bookId; }
    public String getStudentId() { return studentId; }
    public LocalDate getIssueDate() { return issueDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }

    public boolean isReturned() {
        return returnDate != null;
    }

    public long calculateFine() {
        LocalDate date = returnDate == null
                ? LocalDate.now() : returnDate;

        long days = ChronoUnit.DAYS.between(dueDate, date);
        return Math.max(0, days) * 5;
    }

    public void returnBook() {
        if (returnDate == null)
            returnDate = LocalDate.now();
    }
    public void restoreDates(
        java.time.LocalDate issue,
        java.time.LocalDate due,
        java.time.LocalDate returned) {
    this.issueDate = issue;
    this.dueDate = due;
    this.returnDate = returned;
}
}