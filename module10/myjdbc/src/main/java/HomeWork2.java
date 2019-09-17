import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import structure.Course;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class HomeWork2 {
    static String hibernateConfig = "hibernate.cfg.xml";

    public static void main(String[] args) {

        try (
                StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                        .configure(hibernateConfig).build();
        ) {
            Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
            SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();
            Session session = sessionFactory.openSession();

            Transaction transaction = session.beginTransaction();
            Course course = session.get(Course.class, 46);
            course.setStudentsCount(5);
            course.setDescription("placeholder");
            session.save(course);
            transaction.commit();

            try {
                getCorses(session).forEach(System.out::println);

            } catch (Exception e) {
                System.out.println("Ошибка чтения БД");
            }

            session.close();
            sessionFactory.close();
        }
    }

    private static Stream getCorses(Session session) {
        try {
            Query query = session.createQuery("FROM Course");
            List<Course> list = (List<Course>) query.list();
            return list.stream();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Collections.emptyList().stream();
    }
}
