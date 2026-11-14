
package library;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.beans.property.SimpleStringProperty;

public class DashboardController {

    private final LibraryService service;

    private BorderPane root;
    private Label totalBooks;
    private Label availableBooks;
    private Label issuedBooks;
    private Label totalStudents;

    public DashboardController(LibraryService service) {
        this.service = service;
    }

    public Parent createView() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: #F3F6FA;");

        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(25));
        sidebar.setPrefWidth(220);
        sidebar.setStyle("-fx-background-color: #163B65;");

        Label heading = new Label("LIBRARY\nMANAGEMENT");
        heading.setTextFill(Color.WHITE);
        heading.setFont(Font.font("Arial", 20));

        sidebar.getChildren().add(heading);

        addMenuButton(sidebar, "Dashboard", this::showDashboard);
        addMenuButton(sidebar, "Books", this::showBooks);
        addMenuButton(sidebar, "Students", this::showStudents);
        addMenuButton(sidebar, "Issue / Return", this::showIssueReturn);
        addMenuButton(sidebar, "Transactions", this::showTransactions);

        root.setLeft(sidebar);
        showDashboard();

        return root;
    }

    private void addMenuButton(
            VBox sidebar, String name, Runnable action) {

        Button button = new Button(name);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefHeight(42);
        button.setStyle(
            "-fx-background-color: #245580;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-background-radius: 7;"
        );

        button.setOnAction(e -> action.run());
        sidebar.getChildren().add(button);
    }

    private Label pageTitle(String title) {
        Label label = new Label(title);
        label.setFont(Font.font("Arial", 25));
        label.setTextFill(Color.web("#163B65"));
        return label;
    }

    // DASHBOARD

    private void showDashboard() {

        totalBooks = new Label();
        availableBooks = new Label();
        issuedBooks = new Label();
        totalStudents = new Label();

        VBox content = new VBox(25);
        content.setPadding(new Insets(30));

        content.getChildren().add(pageTitle("Dashboard"));

        HBox cards = new HBox(15,
            createCard("Total Books", totalBooks),
            createCard("Available Books", availableBooks),
            createCard("Issued Books", issuedBooks),
            createCard("Students", totalStudents)
        );

        content.getChildren().add(cards);

        Label welcome = new Label(
            "Welcome to the Intelligent Library Book Management System!"
        );

        welcome.setFont(Font.font(16));
        content.getChildren().add(welcome);

        updateDashboard();
        root.setCenter(content);
    }

    private VBox createCard(String title, Label value) {

        Label heading = new Label(title);
        heading.setTextFill(Color.GRAY);

        value.setFont(Font.font("Arial", 28));
        value.setTextFill(Color.web("#1769AA"));

        VBox card = new VBox(12, heading, value);
        card.setPadding(new Insets(20));
        card.setPrefWidth(170);

        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 12;" +
            "-fx-effect: dropshadow(gaussian, #CCCCCC, 8, 0, 0, 2);"
        );

        return card;
    }

    private void updateDashboard() {

        int total = 0;
        int available = 0;
        int issued = 0;

        for (Book book : service.getBooks()) {
            total += book.getQuantity();
            available += book.getAvailable();
            issued += book.getIssued();
        }

        totalBooks.setText(String.valueOf(total));
        availableBooks.setText(String.valueOf(available));
        issuedBooks.setText(String.valueOf(issued));

        totalStudents.setText(
            String.valueOf(service.getStudents().size())
        );
    }

    // BOOK MANAGEMENT AND SEARCH

    private void showBooks() {

        VBox content = new VBox(15);
        content.setPadding(new Insets(25));

        content.getChildren().add(pageTitle("Book Management"));

        // Java statistics

        Label javaHeading = new Label("JAVA BOOK STATISTICS");
        javaHeading.setFont(Font.font("Arial", 18));

        Label javaTotal = new Label();
        Label javaIssued = new Label();
        Label javaAvailable = new Label();
        Label javaReturned = new Label();
        Label issuedBookIds = new Label();
        Label returnedBookIds = new Label();

        Runnable updateJavaStats = () -> {

            javaTotal.setText("Total Java Copies: "
                + service.getCategoryTotal("Java"));

            javaIssued.setText("Currently Issued: "
                + service.getCategoryIssued("Java"));

            javaAvailable.setText("Available Copies: "
                + service.getCategoryAvailable("Java"));

            javaReturned.setText("Returned Transactions: "
                + service.getReturnedByCategory("Java").size());

            String issuedIds = service.getIssuedByCategory("Java")
                .stream()
                .map(t -> t.getBookId())
                .distinct()
                .reduce((a, b) -> a + ", " + b)
                .orElse("None");

            String returnedIds = service.getReturnedByCategory("Java")
                .stream()
                .map(t -> t.getBookId())
                .distinct()
                .reduce((a, b) -> a + ", " + b)
                .orElse("None");

            issuedBookIds.setText("Issued Book IDs: " + issuedIds);
            returnedBookIds.setText("Returned Book IDs: " + returnedIds);
        };

        VBox javaStats = new VBox(8,
            javaHeading,
            javaTotal,
            javaIssued,
            javaAvailable,
            javaReturned,
            issuedBookIds,
            returnedBookIds
        );

        javaStats.setPadding(new Insets(12));
        javaStats.setStyle(
            "-fx-background-color: #eaf2ff;" +
            "-fx-background-radius: 10;"
        );

        updateJavaStats.run();

        // Input fields

        TextField id = new TextField();
        id.setPromptText("Book ID");

        TextField title = new TextField();
        title.setPromptText("Book Title");

        TextField author = new TextField();
        author.setPromptText("Author");

        TextField category = new TextField();
        category.setPromptText("Subject / Category");

        TextField quantity = new TextField();
        quantity.setPromptText("Quantity");

        Button add = new Button("Add Book");
        Button delete = new Button("Delete Selected Book");

        Label message = new Label();

        // SEARCH BAR

        TextField searchField = new TextField();
        searchField.setPromptText(
            "Search by Book ID, Book Name, Author or Subject..."
        );
        searchField.setPrefWidth(400);

        Label searchLabel = new Label("Search Books:");

        HBox searchBox = new HBox(10, searchLabel, searchField);
        searchBox.setAlignment(Pos.CENTER_LEFT);

        // BOOK TABLE

        TableView<Book> table = new TableView<>();

        TableColumn<Book, String> bookId =
            new TableColumn<>("Book ID");

        bookId.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().getBookId())
        );

        TableColumn<Book, String> bookTitle =
            new TableColumn<>("Title");

        bookTitle.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().getTitle())
        );

        TableColumn<Book, String> bookAuthor =
            new TableColumn<>("Author");

        bookAuthor.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().getAuthor())
        );

        TableColumn<Book, String> bookCategory =
            new TableColumn<>("Subject");

        bookCategory.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().getCategory())
        );

        TableColumn<Book, String> totalQty =
            new TableColumn<>("Total");

        totalQty.setCellValueFactory(data ->
            new SimpleStringProperty(
                String.valueOf(data.getValue().getQuantity())
            )
        );

        TableColumn<Book, String> issuedQty =
            new TableColumn<>("Issued");

        issuedQty.setCellValueFactory(data ->
            new SimpleStringProperty(
                String.valueOf(data.getValue().getIssued())
            )
        );

        TableColumn<Book, String> bookAvailable =
            new TableColumn<>("Available");

        bookAvailable.setCellValueFactory(data ->
            new SimpleStringProperty(
                String.valueOf(data.getValue().getAvailable())
            )
        );

        TableColumn<Book, String> status =
            new TableColumn<>("Status");

        status.setCellValueFactory(data ->
            new SimpleStringProperty(
                data.getValue().getAvailable() > 0
                    ? "Available" : "Out of Stock"
            )
        );

        table.getColumns().addAll(
            bookId,
            bookTitle,
            bookAuthor,
            bookCategory,
            totalQty,
            issuedQty,
            bookAvailable,
            status
        );

        // REFRESH AND SEARCH

        Runnable refresh = () -> {

            String keyword = searchField.getText()
                .trim()
                .toLowerCase();

            if (keyword.isEmpty()) {
                table.getItems().setAll(service.getBooks());
            } else {
                table.getItems().setAll(
                    service.searchBooks(keyword)
                );
            }

            updateDashboard();
            updateJavaStats.run();
        };

        searchField.textProperty().addListener(
            (observable, oldValue, newValue) -> refresh.run()
        );

        // ADD BOOK

        add.setOnAction(e -> {

            try {

                if (id.getText().isBlank()
                    || title.getText().isBlank()
                    || author.getText().isBlank()
                    || category.getText().isBlank()
                    || quantity.getText().isBlank()) {

                    message.setText("Please fill all fields.");
                    return;
                }

                int qty = Integer.parseInt(quantity.getText());

                if (qty < 1) {
                    message.setText("Quantity must be at least 1.");
                    return;
                }

                service.addBook(new Book(
                    id.getText().trim(),
                    title.getText().trim(),
                    author.getText().trim(),
                    category.getText().trim(),
                    qty
                ));

                FileManager.save(service);
                refresh.run();

                message.setText("Book added successfully!");

                id.clear();
                title.clear();
                author.clear();
                category.clear();
                quantity.clear();

            } catch (Exception ex) {
                message.setText(ex.getMessage());
            }
        });

        // DELETE BOOK

        delete.setOnAction(e -> {

            Book selected = table.getSelectionModel()
                .getSelectedItem();

            if (selected == null) {
                message.setText("Select a book first.");
                return;
            }

            try {

                service.deleteBook(selected.getBookId());

                FileManager.save(service);
                refresh.run();

                message.setText("Book deleted successfully!");

            } catch (Exception ex) {
                message.setText(ex.getMessage());
            }
        });

        HBox form = new HBox(
            8, id, title, author, category, quantity, add
        );

        form.setAlignment(Pos.CENTER_LEFT);

        content.getChildren().addAll(
            javaStats,
            searchBox,
            form,
            delete,
            message,
            table
        );

        VBox.setVgrow(table, Priority.ALWAYS);

        refresh.run();
        root.setCenter(content);
    }

    // STUDENT MANAGEMENT

    private void showStudents() {

        VBox content = new VBox(15);
        content.setPadding(new Insets(25));

        content.getChildren().add(pageTitle("Student Management"));

        TextField id = new TextField();
        id.setPromptText("Student ID");

        TextField name = new TextField();
        name.setPromptText("Student Name");

        TextField department = new TextField();
        department.setPromptText("Department");

        TextField phone = new TextField();
        phone.setPromptText("Phone Number");

        Button add = new Button("Register Student");
        Label message = new Label();

        TableView<Student> table = new TableView<>();

        TableColumn<Student, String> studentId =
            new TableColumn<>("Student ID");

        studentId.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().getStudentId())
        );

        TableColumn<Student, String> studentName =
            new TableColumn<>("Name");

        studentName.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().getName())
        );

        TableColumn<Student, String> studentDepartment =
            new TableColumn<>("Department");

        studentDepartment.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().getDepartment())
        );

        TableColumn<Student, String> studentPhone =
            new TableColumn<>("Phone");

        studentPhone.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().getPhone())
        );

        table.getColumns().addAll(
            studentId,
            studentName,
            studentDepartment,
            studentPhone
        );

        Runnable refresh = () ->
            table.getItems().setAll(service.getStudents());

        add.setOnAction(e -> {

            if (id.getText().isBlank()
                || name.getText().isBlank()
                || department.getText().isBlank()
                || phone.getText().isBlank()) {

                message.setText("Please fill all fields.");
                return;
            }

            try {

                service.addStudent(new Student(
                    id.getText().trim(),
                    name.getText().trim(),
                    department.getText().trim(),
                    phone.getText().trim()
                ));

                FileManager.save(service);
                refresh.run();
                updateDashboard();

                message.setText("Student registered successfully!");

                id.clear();
                name.clear();
                department.clear();
                phone.clear();

            } catch (Exception ex) {
                message.setText(ex.getMessage());
            }
        });

        HBox form = new HBox(
            10, id, name, department, phone, add
        );

        content.getChildren().addAll(form, message, table);

        VBox.setVgrow(table, Priority.ALWAYS);

        refresh.run();
        root.setCenter(content);
    }

    // ISSUE BOOK

    private void showIssueReturn() {

        VBox content = new VBox(18);
        content.setPadding(new Insets(25));

        content.getChildren().add(
            pageTitle("Issue / Return Books")
        );

        ComboBox<Book> books = new ComboBox<>();
        books.setPromptText("Select Book");
        books.setPrefWidth(250);

        books.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Book book, boolean empty) {
                super.updateItem(book, empty);

                setText(empty || book == null ? null :
                    book.getTitle() + " (" +
                    book.getAvailable() + " available)");
            }
        });

        books.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Book book, boolean empty) {
                super.updateItem(book, empty);
                setText(empty || book == null ? null : book.getTitle());
            }
        });

        ComboBox<Student> students = new ComboBox<>();
        students.setPromptText("Select Student");
        students.setPrefWidth(250);

        students.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Student student, boolean empty) {
                super.updateItem(student, empty);

                setText(empty || student == null ? null :
                    student.getName() + " (" +
                    student.getStudentId() + ")");
            }
        });

        students.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Student student, boolean empty) {
                super.updateItem(student, empty);
                setText(empty || student == null ? null : student.getName());
            }
        });

        Button issue = new Button("Issue Book");
        Label issueMessage = new Label();

        Runnable refresh = () -> {
            books.getItems().setAll(service.getBooks());
            students.getItems().setAll(service.getStudents());
        };

        issue.setOnAction(e -> {

            if (books.getValue() == null
                || students.getValue() == null) {

                issueMessage.setText("Select both book and student.");
                return;
            }

            try {

                service.issueBook(
                    books.getValue().getBookId(),
                    students.getValue().getStudentId()
                );

                FileManager.save(service);

                issueMessage.setText(
                    "Book issued! Due date: 14 days from today."
                );

                refresh.run();
                updateDashboard();

            } catch (Exception ex) {
                issueMessage.setText(ex.getMessage());
            }
        });

        content.getChildren().addAll(
            new Label("Issue a Book"),
            books,
            students,
            issue,
            issueMessage,
            new Separator(),
            new Label("To return a book, open the Transactions page.")
        );

        refresh.run();
        root.setCenter(content);
    }

    // TRANSACTION HISTORY AND RETURN

    private void showTransactions() {

        VBox content = new VBox(15);
        content.setPadding(new Insets(25));

        content.getChildren().add(
            pageTitle("Transactions")
        );

        TableView<Transaction> table = new TableView<>();

        TableColumn<Transaction, String> transactionId =
            new TableColumn<>("Transaction ID");

        transactionId.setCellValueFactory(data ->
            new SimpleStringProperty(
                data.getValue().getTransactionId()
            )
        );

        TableColumn<Transaction, String> book =
            new TableColumn<>("Book ID");

        book.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().getBookId())
        );

        TableColumn<Transaction, String> student =
            new TableColumn<>("Student ID");

        student.setCellValueFactory(data ->
            new SimpleStringProperty(
                data.getValue().isReturned() ? "Returned" : "Issued"
            )
        );

        TableColumn<Transaction, String> fine =
            new TableColumn<>("Fine (Rs.)");

        fine.setCellValueFactory(data ->
            new SimpleStringProperty(
                String.valueOf(data.getValue().calculateFine())
            )
        );

        table.getColumns().addAll(
            transactionId, book, student, due, status, fine
        );

        Button returnButton = new Button("Return Selected Book");
        Label message = new Label();

        Runnable refresh = () -> {
            table.getItems().setAll(service.getTransactions());
            updateDashboard();
        };

        returnButton.setOnAction(e -> {

            Transaction selected =
                table.getSelectionModel().getSelectedItem();

            if (selected == null) {
                message.setText("Select a transaction first.");
                return;
            }

            try {

                long fineAmount = service.returnBook(
                    selected.getTransactionId()
                );

                FileManager.save(service);
                refresh.run();

                message.setText(
                    "Book returned successfully! Fine: Rs. " + fineAmount
                );

            } catch (Exception ex) {
                message.setText(ex.getMessage());
            }
        });

        content.getChildren().addAll(
            table,
            returnButton,
            message
        );

        VBox.setVgrow(table, Priority.ALWAYS);

        refresh.run();
        root.setCenter(content);
    }
}