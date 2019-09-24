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

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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

                String hql = "From " + Subscription.class.getSimpleName();
                List<Subscription> subscriptions = session.createQuery(hql).getResultList();

                List<Purchase> purchaseList = new ArrayList<>();
                subscriptions.forEach(subscription -> {
                    purchaseList.add(new Purchase(
                            subscription.getKey().getStudent(),
                            subscription.getKey().getCourse(),
                            subscription.getKey().getCourse().getPrice(),
                            subscription.getSubscriptionsDate())
                    );
//                    Transaction transaction = session.beginTransaction();
//                    session.save(new Purchase(
//                            subscription.getKey().getStudent(),
//                            subscription.getKey().getCourse(),
//                            subscription.getKey().getCourse().getPrice(),
//                            subscription.getSubscriptionsDate())
//                    );
//                    transaction.commit();
                });

                purchaseList.forEach(p -> {
                    try {
                        Transaction transaction = session.beginTransaction();
                        System.out.println("Перезапись " + p + "...");
                        session.save(p);
                        transaction.commit();
                        System.out.println("OK");

                    } catch (Exception e) {
                        System.out.println("Failed");
                    }
                });

            } catch (Exception e) {
                System.out.println("Ошибка чтения БД");
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
