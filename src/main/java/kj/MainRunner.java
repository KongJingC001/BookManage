package kj;

import kj.Util.SqlUtil;
import kj.entity.Book;
import kj.entity.Borrow;
import kj.entity.Student;
import lombok.extern.java.Log;
import org.apache.ibatis.io.Resources;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.LogManager;

@Log
public class MainRunner {


    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            LogManager logManager = LogManager.getLogManager();
            logManager.readConfiguration(Resources.getResourceAsStream("logging.properties"));
            while (true) {
                System.out.println("==============================");
                System.out.println("1.录入学生信息");
                System.out.println("2.录入书籍信息");
                System.out.println("3.录入借阅信息");
                System.out.println("4.查询借阅信息");
                System.out.println("5.查询学生信息");
                System.out.println("6.查询书籍信息");
                System.out.print("输入你想要执行的操作（其它键退出）:");
                int input;
                try {
                    input = scanner.nextInt();
                } catch (Exception e) {
                    return;
                }
                scanner.nextLine();
                switch (input) {
                    case 1:
                        addStudent(scanner);
                        break;
                    case 2:
                        addBook(scanner);
                        break;
                    case 3:
                        addBorrow(scanner);
                        break;
                    case 4:
                        showBorrows();
                        break;
                    case 5:
                        showStudents();
                        break;
                    case 6:
                        showBooks();
                        break;
                    default:
                        return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 功能1：添加学生信息
     */
    private static void addStudent(Scanner scanner) {
        String name, sex;
        int grade;
        System.out.println("------------------------------");
        System.out.print("请输入学生姓名:");
        name = scanner.nextLine();
        System.out.print("请输入学生性别（男/女）:");
        sex = scanner.nextLine();
        System.out.print("请输入学生入学年份:");
        grade = scanner.nextInt();
        Student student = new Student(name, sex, grade);
        // Consumer的使用，函数式编程
        // doSqlWork需要一个Consumer，这里理解为下面的表达式中的函数
        // 在doSqlWork中accept方法准备了一个BookMapper，传入Consumer中的参数，就是这里的表达式参数bookMapper
        // 最后再执行{}中的内容
        SqlUtil.doSqlWork(bookMapper -> {
            int count = bookMapper.addStudent(student);
            if (count > 0) {
                System.out.println("学生信息录入成功！");
                log.info("插入一条学生信息" + student);
            } else {
                System.err.println(student + "学生信息录入失败，请重新录入！");
                log.warning("插入学生信息失败！");
            }
        });
    }

    /**
     * 功能2：录入书籍信息
     */
    private static void addBook(Scanner scanner) {
        String title, desc;
        double price;
        System.out.println("------------------------------");
        System.out.print("请输入书籍标题:");
        title = scanner.nextLine();
        System.out.print("请输入书籍描述:");
        desc = scanner.nextLine();
        System.out.print("请输入书籍价格（保留两位小数点）:");
        price = scanner.nextDouble();
        Book book = new Book(title, desc, price);
        SqlUtil.doSqlWork(bookMapper -> {
            int count = bookMapper.addBook(book);
            if (count > 0) {
                System.out.println("书籍信息录入成功！");
                log.info("录入一条书籍信息" + book);
            } else {
                System.err.println("书籍信息录入失败，请重新录入！");
                log.warning(book + "录入书籍信息失败！");
            }
        });
    }

    /**
     * 功能3：录入借阅信息
     */
    private static void addBorrow(Scanner scanner) {
        int sid, bid;
        System.out.println("------------------------------");
        System.out.print("请输入学生ID:");
        sid = scanner.nextInt();
        System.out.print("请输入书籍ID:");
        bid = scanner.nextInt();
        SqlUtil.doSqlWork(bookMapper -> {
            int count = bookMapper.addBorrow(sid, bid);
            if (count > 0) {
                System.out.println("借阅信息录入成功！");
                log.info("录入一条借阅信息，学生" + sid + "借阅书籍" + bid);
            } else {
                System.err.println("书籍信息录入失败，请重新录入！");
                log.warning("录入借阅信息失败！学生" + sid + "借阅书籍" + bid);
            }
        });
    }


    private static void showBorrows() {
        SqlUtil.doSqlWork(bookMapper -> {
            List<Borrow> borrowList = bookMapper.getBorrows();
            System.out.println("------------------------------");
            borrowList.forEach(borrow -> System.out.println("ID:" + borrow.getId() + " " + borrow.getStudent().getName() + "借阅《" + borrow.getBook().getTitle() + "》"));
            log.info("查询所有借阅信息，共" + borrowList.size() + "条！");
        });
    }

    private static void showStudents() {
        SqlUtil.doSqlWork(bookMapper -> {
            List<Student> studentList = bookMapper.getStudents();
            System.out.println("------------------------------");
            studentList.forEach(student -> System.out.printf("ID:%d %s(%s) - %d年入学\n", student.getSid(), student.getName(), student.getSex(), student.getGrade()));
            log.info("查询所有学生信息，共" + studentList.size() + "条！");
        });
    }

    private static void showBooks() {
        SqlUtil.doSqlWork(bookMapper -> {
            List<Book> bookList = bookMapper.getBooks();
            System.out.println("------------------------------");
            bookList.forEach(book -> System.out.printf("ID:%d %s(￥%.2f) - %s\n", book.getBid(), book.getTitle(), book.getPrice(), book.getDesc()));
            log.info("查询所有书籍信息，共" + bookList.size() + "条！");
        });
    }

}
