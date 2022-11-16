package kj;

import kj.Util.SqlUtil;
import kj.entity.Book;
import kj.entity.Student;
import kj.mapper.BookMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class MainTest {

    @Test
    void dbTest() throws IOException {
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader("mybatis-config.xml"));
        try(SqlSession sqlSession = sqlSessionFactory.openSession(true)) {
            BookMapper bookMapper = sqlSession.getMapper(BookMapper.class);
//            System.out.println(bookMapper.addStudent(new Student("小明", "男", 2021)));
            System.out.println(bookMapper.addBook(new Book("三国演义", "四大名著之一", 30.42)));
        }
    }

    @Test
    void strTest() {
        System.out.println(String.format("%d, %d, %d", 1, 2, 3));
    }

    @Test
    void sqlTest() {
        SqlUtil.doSqlWork(bookMapper -> {
            System.out.println(bookMapper.getBookByBid(3));
            System.out.println(bookMapper.getStudentBySid(5));
            System.out.println(bookMapper.getBorrows());
            System.out.println(bookMapper.getBooks());
            System.out.println(bookMapper.getStudents());
        });
    }

    @Test
    void formatTest() {
        SqlUtil.doSqlWork(bookMapper -> {
            List<Book> bookList = bookMapper.getBooks();
            System.out.println("------------------------------");
            bookList.forEach(book ->
                    System.out.printf("ID:%d %s(￥%.2f) - %s\n",
                            book.getBid(), book.getTitle(), book.getPrice(), book.getDesc()));
        });
    }
}
