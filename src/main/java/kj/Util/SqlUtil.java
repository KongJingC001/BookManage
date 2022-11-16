package kj.Util;

import kj.mapper.BookMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.util.function.Consumer;

public class SqlUtil {

    private static SqlSessionFactory sqlSessionFactory;

    static {
        try {
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader("mybatis-config.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private SqlUtil() {}

    public static SqlSession getSqlSession() {
        return sqlSessionFactory.openSession(true);
    }

    public static void doSqlWork(Consumer<BookMapper> consumer) {
        try(SqlSession sqlSession = sqlSessionFactory.openSession(true)) {
            BookMapper bookMapper = sqlSession.getMapper(BookMapper.class);
            consumer.accept(bookMapper);
        }
    }

}
