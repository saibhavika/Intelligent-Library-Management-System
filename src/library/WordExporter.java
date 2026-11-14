
package library;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.lang.reflect.Method;

public class WordExporter {

    public static void export(LibraryService service) {

        StringBuilder report = new StringBuilder();

        report.append("{\\rtf1\\ansi\\deff0\n");
        report.append("\\b\\fs32 LIBRARY MANAGEMENT SYSTEM\\b0\\fs20\\par\n");
        report.append("Library Data Report\\par\\par\n");

        report.append("\\b BOOK DETAILS\\b0\\par\n");

        for (Object book : service.getBooks()) {
            report.append("Book ID: ").append(get(book, "getBookId"));
            report.append(" | Title: ").append(get(book, "getTitle"));
            report.append(" | Author: ").append(get(book, "getAuthor"));
            report.append(" | Category: ").append(get(book, "getCategory"));
            report.append(" | Quantity: ").append(get(book, "getQuantity"));
            report.append("\\par\n");
        }

        report.append("\\par\\b STUDENT DETAILS\\b0\\par\n");

        for (Object student : service.getStudents()) {
            report.append("Student ID: ").append(get(student, "getStudentId"));
            report.append(" | Name: ").append(get(student, "getName"));
            report.append(" | Department: ").append(get(student, "getDepartment"));
            report.append(" | Phone: ").append(get(student, "getPhone"));
            report.append("\\par\n");
        }

        report.append("\\par\\b TRANSACTION DETAILS\\b0\\par\n");

        for (Object transaction : service.getTransactions()) {
            report.append("Transaction ID: ").append(get(transaction, "getTransactionId"));
            report.append(" | Book ID: ").append(get(transaction, "getBookId"));
            report.append(" | Student ID: ").append(get(transaction, "getStudentId"));
            report.append(" | Issue Date: ").append(get(transaction, "getIssueDate"));
            report.append(" | Due Date: ").append(get(transaction, "getDueDate"));
            report.append(" | Return Date: ").append(get(transaction, "getReturnDate"));
            report.append("\\par\n");
        }

        report.append("}");

        try {
            Files.writeString(Path.of("Library_Report.rtf"), report.toString());
            System.out.println("Word report updated successfully!");
        } catch (IOException e) {
            System.out.println("Report error: " + e.getMessage());
        }
    }

    private static String get(Object object, String methodName) {
        try {
            Method method = object.getClass().getMethod(methodName);
            Object value = method.invoke(object);
            return value == null ? "N/A" : value.toString();
        } catch (Exception e) {
            return "N/A";
        }
    }
}