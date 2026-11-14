
package library;

public class Book {
    private String bookId;
    private String title;
    private String author;
    private String category;
    private int quantity;
    private int issued;

    public Book(String bookId, String title, String author,
                String category, int quantity) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.category = category;
        this.quantity = quantity;
        this.issued = 0;
    }

    public String getBookId() { return bookId; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public int getQuantity() { return quantity; }
    public int getIssued() { return issued; }
    public int getAvailable() { return quantity - issued; }

    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setCategory(String category) { this.category = category; }

    public void setQuantity(int quantity) {
        if (quantity >= issued) this.quantity = quantity;
    }

    public void issueBook() {
        if (getAvailable() > 0) issued++;
    }

    public void returnBook() {
        if (issued > 0) issued--;
    }
}