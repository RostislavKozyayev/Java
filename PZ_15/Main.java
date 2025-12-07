// ==================== ИНТЕРФЕЙСЫ ====================

// Интерфейс для сущностей, которые можно отображать
interface Displayable {
    void displayInfo();
}

// Интерфейс для сущностей, которые можно искать по ID
interface Searchable {
    int getId();
}

// ==================== АБСТРАКТНЫЕ КЛАССЫ ====================

// Абстрактный класс для всех людей в библиотеке
abstract class Person implements Displayable, Searchable {
    private int id;
    private String name;
    private String email;
    
    public Person(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
    
    // Геттеры и сеттеры с инкапсуляцией
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    @Override
    public void displayInfo() {
        System.out.println("ID: " + id + ", Имя: " + name + ", Email: " + email);
    }
}

// ==================== КЛАССЫ КНИГ ====================

// Класс для автора книги
class Author extends Person {
    private String nationality;
    
    public Author(int id, String name, String email, String nationality) {
        super(id, name, email);
        this.nationality = nationality;
    }
    
    public String getNationality() {
        return nationality;
    }
    
    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Национальность: " + nationality);
    }
}

// Абстрактный класс для печатных изданий
abstract class Publication implements Displayable, Searchable {
    private int id;
    private String title;
    private int year;
    private boolean isAvailable;
    
    public Publication(int id, String title, int year) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.isAvailable = true;
    }
    
    public int getId() {
        return id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public int getYear() {
        return year;
    }
    
    public boolean isAvailable() {
        return isAvailable;
    }
    
    public void setAvailable(boolean available) {
        isAvailable = available;
    }
    
    @Override
    public void displayInfo() {
        System.out.println("ID: " + id + ", Название: \"" + title + 
                          "\", Год: " + year + ", Доступна: " + (isAvailable ? "Да" : "Нет"));
    }
    
    // Абстрактный метод для получения типа издания
    public abstract String getType();
}

// Класс книги
class Book extends Publication {
    private Author author;
    private String isbn;
    private String genre;
    
    public Book(int id, String title, Author author, int year, String isbn, String genre) {
        super(id, title, year);
        this.author = author;
        this.isbn = isbn;
        this.genre = genre;
    }
    
    public Author getAuthor() {
        return author;
    }
    
    public String getIsbn() {
        return isbn;
    }
    
    public String getGenre() {
        return genre;
    }
    
    @Override
    public void displayInfo() {
        System.out.print("Книга: ");
        super.displayInfo();
        System.out.println("Автор: " + author.getName() + 
                          ", ISBN: " + isbn + ", Жанр: " + genre);
    }
    
    @Override
    public String getType() {
        return "Книга";
    }
    
    // Перегрузка метода (полиморфизм)
    public void displayInfo(boolean showDetails) {
        if (showDetails) {
            displayInfo();
        } else {
            System.out.println("Книга: \"" + getTitle() + "\", Автор: " + author.getName());
        }
    }
}

// Класс журнала
class Magazine extends Publication {
    private int issueNumber;
    private String publisher;
    
    public Magazine(int id, String title, int year, int issueNumber, String publisher) {
        super(id, title, year);
        this.issueNumber = issueNumber;
        this.publisher = publisher;
    }
    
    public int getIssueNumber() {
        return issueNumber;
    }
    
    public String getPublisher() {
        return publisher;
    }
    
    @Override
    public void displayInfo() {
        System.out.print("Журнал: ");
        super.displayInfo();
        System.out.println("Номер: " + issueNumber + ", Издатель: " + publisher);
    }
    
    @Override
    public String getType() {
        return "Журнал";
    }
}

// ==================== КЛАССЫ СОТРУДНИКОВ ====================

// Класс сотрудника библиотеки
class Librarian extends Person {
    private String position;
    private double salary;
    
    public Librarian(int id, String name, String email, String position, double salary) {
        super(id, name, email);
        this.position = position;
        this.salary = salary;
    }
    
    public String getPosition() {
        return position;
    }
    
    public void setPosition(String position) {
        this.position = position;
    }
    
    public double getSalary() {
        return salary;
    }
    
    public void setSalary(double salary) {
        this.salary = salary;
    }
    
    @Override
    public void displayInfo() {
        System.out.print("Сотрудник: ");
        super.displayInfo();
        System.out.println("Должность: " + position + ", Зарплата: " + salary);
    }
    
    // Метод для выдачи книги
    public void issueBook(Book book, Client client) {
        if (book.isAvailable()) {
            book.setAvailable(false);
            System.out.println(getName() + " выдал книгу \"" + book.getTitle() + "\" клиенту " + client.getName());
        } else {
            System.out.println("Книга \"" + book.getTitle() + "\" уже выдана");
        }
    }
}

// ==================== КЛАССЫ КЛИЕНТОВ ====================

// Класс клиента библиотеки
class Client extends Person {
    private String phoneNumber;
    private int borrowedBooksCount;
    
    public Client(int id, String name, String email, String phoneNumber) {
        super(id, name, email);
        this.phoneNumber = phoneNumber;
        this.borrowedBooksCount = 0;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public int getBorrowedBooksCount() {
        return borrowedBooksCount;
    }
    
    public void borrowBook() {
        borrowedBooksCount++;
    }
    
    public void returnBook() {
        if (borrowedBooksCount > 0) {
            borrowedBooksCount--;
        }
    }
    
    @Override
    public void displayInfo() {
        System.out.print("Клиент: ");
        super.displayInfo();
        System.out.println("Телефон: " + phoneNumber + ", Взято книг: " + borrowedBooksCount);
    }
}

// ==================== КЛАССЫ ДЛЯ УПРАВЛЕНИЯ ====================

// Класс для управления книгами
class BookManager {
    private Publication[] publications;
    private int count;
    
    public BookManager(int capacity) {
        publications = new Publication[capacity];
        count = 0;
    }
    
    public void addPublication(Publication publication) {
        if (count < publications.length) {
            publications[count] = publication;
            count++;
        }
    }
    
    public void displayAllPublications() {
        System.out.println("\n=== ВСЕ ИЗДАНИЯ В БИБЛИОТЕКЕ ===");
        for (int i = 0; i < count; i++) {
            publications[i].displayInfo();
            System.out.println("---");
        }
    }
    
    public Publication findPublicationById(int id) {
        for (int i = 0; i < count; i++) {
            if (publications[i].getId() == id) {
                return publications[i];
            }
        }
        return null;
    }
}

// Класс для управления сотрудниками
class StaffManager {
    private Librarian[] librarians;
    private int count;
    
    public StaffManager(int capacity) {
        librarians = new Librarian[capacity];
        count = 0;
    }
    
    public void addLibrarian(Librarian librarian) {
        if (count < librarians.length) {
            librarians[count] = librarian;
            count++;
        }
    }
    
    public void displayAllStaff() {
        System.out.println("\n=== ВСЕ СОТРУДНИКИ ===");
        for (int i = 0; i < count; i++) {
            librarians[i].displayInfo();
            System.out.println("---");
        }
    }
}

// Класс для управления клиентами
class ClientManager {
    private Client[] clients;
    private int count;
    
    public ClientManager(int capacity) {
        clients = new Client[capacity];
        count = 0;
    }
    
    public void addClient(Client client) {
        if (count < clients.length) {
            clients[count] = client;
            count++;
        }
    }
    
    public void displayAllClients() {
        System.out.println("\n=== ВСЕ КЛИЕНТЫ ===");
        for (int i = 0; i < count; i++) {
            clients[i].displayInfo();
            System.out.println("---");
        }
    }
    
    public Client findClientById(int id) {
        for (int i = 0; i < count; i++) {
            if (clients[i].getId() == id) {
                return clients[i];
            }
        }
        return null;
    }
}

// ==================== ГЛАВНЫЙ КЛАСС ====================

public class Main {
    public static void main(String[] args) {
        System.out.println("=== СИСТЕМА УПРАВЛЕНИЯ БИБЛИОТЕКОЙ ===\n");
        
        // Создаем авторов
        Author author1 = new Author(1, "Лев Толстой", "tolstoy@email.com", "Русский");
        Author author2 = new Author(2, "Фёдор Достоевский", "dostoevsky@email.com", "Русский");
        
        // Создаем книги
        Book book1 = new Book(101, "Война и мир", author1, 1869, "978-5-389-00001-1", "Роман");
        Book book2 = new Book(102, "Преступление и наказание", author2, 1866, "978-5-389-00002-2", "Роман");
        Magazine magazine1 = new Magazine(201, "Наука и жизнь", 2023, 5, "Научное издательство");
        
        // Создаем сотрудников
        Librarian librarian1 = new Librarian(301, "Иванова Мария", "ivanova@library.com", "Главный библиотекарь", 50000);
        Librarian librarian2 = new Librarian(302, "Петров Иван", "petrov@library.com", "Библиотекарь", 40000);
        
        // Создаем клиентов
        Client client1 = new Client(401, "Сидоров Алексей", "sidorov@email.com", "+7-999-123-45-67");
        Client client2 = new Client(402, "Козлова Елена", "kozlova@email.com", "+7-999-987-65-43");
        
        // Инициализируем менеджеры
        BookManager bookManager = new BookManager(10);
        StaffManager staffManager = new StaffManager(5);
        ClientManager clientManager = new ClientManager(10);
        
        // Добавляем данные
        bookManager.addPublication(book1);
        bookManager.addPublication(book2);
        bookManager.addPublication(magazine1);
        
        staffManager.addLibrarian(librarian1);
        staffManager.addLibrarian(librarian2);
        
        clientManager.addClient(client1);
        clientManager.addClient(client2);
        
        // Отображаем все данные
        bookManager.displayAllPublications();
        staffManager.displayAllStaff();
        clientManager.displayAllClients();
        
        // Демонстрация работы системы
        System.out.println("\n=== ДЕМОНСТРАЦИЯ РАБОТЫ СИСТЕМЫ ===");
        
        // Демонстрация выдачи книги
        System.out.println("\n1. Выдача книги:");
        librarian1.issueBook(book1, client1);
        client1.borrowBook();
        
        // Демонстрация полиморфизма
        System.out.println("\n2. Полиморфизм (перегрузка метода):");
        book1.displayInfo(false); // Краткая информация
        book1.displayInfo(true);  // Полная информация
        
        // Поиск издания по ID
        System.out.println("\n3. Поиск издания по ID (102):");
        Publication found = bookManager.findPublicationById(102);
        if (found != null) {
            found.displayInfo();
        }
        
        // Демонстрация работы с клиентом
        System.out.println("\n4. Клиент берет еще одну книгу:");
        client1.borrowBook();
        client1.displayInfo();
        
        System.out.println("\n5. Клиент возвращает книгу:");
        client1.returnBook();
        client1.displayInfo();
        
        // Демонстрация разных типов публикаций через полиморфизм
        System.out.println("\n6. Типы всех изданий:");
        Publication[] allPubs = {book1, book2, magazine1};
        for (Publication pub : allPubs) {
            System.out.println("Издание: " + pub.getTitle() + ", Тип: " + pub.getType());
        }
    }
}