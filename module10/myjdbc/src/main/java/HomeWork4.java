import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import structure.Course;
import structure.Purchase;
import structure.Student;
import structure.Subscription;

import java.util.ArrayList;
import java.util.List;

public class HomeWork4 {
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
                List<Subscription> subscriptions = session.createQuery(
                        " from Subscription sub" +
                        " join fetch sub.course c" +
                        " join fetch sub.student s" , Subscription.class).getResultList();

                List<Purchase> purchaseList = new ArrayList<>();
                subscriptions.forEach(subscription -> {
                    purchaseList.add(new Purchase(
                            subscription.getStudent(),
                            subscription.getCourse(),
                            subscription.getCourse().getPrice(),
                            subscription.getSubscriptionsDate())
                    );
                });

                System.out.println("Начало транзакции");
                Transaction transaction = session.beginTransaction();
                purchaseList.forEach(p -> {
                    try {
                        session.save(p);

                    } catch (Exception e) {
                        System.out.println("Save failed");
                    }
                });
                transaction.commit();
                System.out.println("Изменения внесены в БД");

            } catch (Exception e) {
                System.out.println("Ошибка доступа к БД");
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}