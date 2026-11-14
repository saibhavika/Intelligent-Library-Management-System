
package library;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileManager {

    private static final String FOLDER = "data";

    private static final String BOOKS =
            FOLDER + "/books.txt";

    private static final String STUDENTS =
            FOLDER + "/students.txt";

    private static final String TRANSACTIONS =
            FOLDER + "/transactions.txt";

    public static void save(LibraryService service) {
        try {
            Files.createDirectories(Paths.get(FOLDER));

            try (PrintWriter p = new PrintWriter(BOOKS)) {
                for (Book b : service.getBooks()) {
                    p.println(String.join("|",
                        encode(b.getBookId()),
                        encode(b.getTitle()),
                        encode(b.getAuthor()),
                        encode(b.getCategory()),
                        String.valueOf(b.getQuantity()),
                        String.valueOf(b.getIssued())
                    ));
                }
                WordExporter.export(service);
            }

            try (PrintWriter p = new PrintWriter(STUDENTS)) {
                for (Student s : service.getStudents()) {
                    p.println(String.join("|",
                        encode(s.getStudentId()),
                        encode(s.getName()),
                        encode(s.getDepartment()),
                        encode(s.getPhone())
                    ));
                }
            }

            try (PrintWriter p = new PrintWriter(TRANSACTIONS)) {
                for (Transaction t : service.getTransactions()) {
                    p.println(String.join("|",
                        encode(t.getTransactionId()),
                        encode(t.getBookId()),
                        encode(t.getStudentId()),
                        t.getIssueDate().toString(),
                        t.getDueDate().toString(),
                        t.getReturnDate() == null ? "" :
                            t.getReturnDate().toString()
                    ));
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(
                "Unable to save library data: " + e.getMessage(), e);
        }
    }

    public static void load(LibraryService service) {
        try {
            Files.createDirectories(Paths.get(FOLDER));

            Path bookPath = Paths.get(BOOKS);
            if (Files.exists(bookPath)) {
                for (String line : Files.readAllLines(bookPath)) {
                    String[] a = line.split("\\|", -1);
                    Book b = new Book(
                        decode(a[0]), decode(a[1]),
                        decode(a[2]), decode(a[3]),
                        Integer.parseInt(a[4])
                    );

                    int issued = Integer.parseInt(a[5]);
                    for (int i = 0; i < issued; i++)
                        b.issueBook();

                    service.addBook(b);
                }
            }

            Path studentPath = Paths.get(STUDENTS);
            if (Files.exists(studentPath)) {
                for (String line : Files.readAllLines(studentPath)) {
                    String[] a = line.split("\\|", -1);
                    service.addStudent(new Student(
                        decode(a[0]), decode(a[1]),
                        decode(a[2]), decode(a[3])
                    ));
                }
            }

            Path transactionPath = Paths.get(TRANSACTIONS);
            if (Files.exists(transactionPath)) {
                for (String line :
                        Files.readAllLines(transactionPath)) {
                    String[] a = line.split("\\|", -1);

                    Transaction t = new Transaction(
                        decode(a[0]), decode(a[1]), decode(a[2])
                    );

                    t.restoreDates(
                        java.time.LocalDate.parse(a[3]),
                        java.time.LocalDate.parse(a[4]),
                        a[5].isEmpty() ? null :
                            java.time.LocalDate.parse(a[5])
                    );

                    service.addLoadedTransaction(t);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(
                "Unable to load library data: " + e.getMessage(), e);
        }
    }

    private static String encode(String value) {
        return Base64.getEncoder().encodeToString(
            value.getBytes(java.nio.charset.StandardCharsets.UTF_8));
    }

    private static String decode(String value) {
        return new String(
            Base64.getDecoder().decode(value),
            java.nio.charset.StandardCharsets.UTF_8);
    }
}