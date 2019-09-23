import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import structure.Course;
import structure.Subscription;

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
                Subscription subscription = session.get(Subscription.class,
                        new Subscription.SubscriptionPK(course.getStudents().get(1), course));
                System.out.println(subscription.toString());

            } catch (Exception e) {
                System.out.println("Ошибка чтения БД");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
