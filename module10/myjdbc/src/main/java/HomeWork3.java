import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import structure.Course;
import structure.Student;
import structure.Subscription;

import java.util.ArrayList;

public class HomeWork3 {
    static String hibernateConfig = "hibernate.cfg.xml";

    public static void main(String[] args) {
        try (
                StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                        .configure(hibernateConfig).build();
        ) {
            Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();

            try (
                    SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();
                    Session session = sessionFactory.openSession();
            ) {
                Course course = session.get(Course.class, 1);
                course.getStudents().forEach(System.out::println);

//                Student student = (new ArrayList<Student>(course.getStudents())).get(1);
                Student student = course.getStudents().iterator().next();
                Subscription subscription = session.get(Subscription.class,
                        new Subscription.SubscriptionPK(student.getId(), course.getId()));
                System.out.println(subscription.toString());

            } catch (Exception e) {
                System.out.println("Ошибка чтения БД");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
