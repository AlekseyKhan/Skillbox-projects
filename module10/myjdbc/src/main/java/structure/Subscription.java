package structure;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "Subscriptions")
public class Subscription {
    @EmbeddedId
    private SubscriptionPK key;

    @Column(name = "subscription_date")
    private Date subscriptionsDate;

    public SubscriptionPK getKey() {
        return key;
    }

    public Date getSubscriptionsDate() {
        return subscriptionsDate;
    }

    public void setSubscriptionsDate(Date subscriptionsDate) {
        this.subscriptionsDate = subscriptionsDate;
    }

    @Override
    public String toString() {
        return "Subscription{(" +
                key +
                "), subscriptionsDate=" + subscriptionsDate +
                '}';
    }


    @Embeddable
    public static class SubscriptionPK implements Serializable {

        @ManyToOne(cascade = CascadeType.ALL)
        protected Course course;

        @ManyToOne(cascade = CascadeType.ALL)
        protected Student student;

        public SubscriptionPK(Student student, Course course) {
            this.student = student;
            this.course = course;
        }

        public SubscriptionPK() {
        }

        public Course getCourse() {
            return course;
        }

        public Student getStudent() {
            return student;
        }

        @Override
        public String toString() {
            return "SubscriptionPK{" +
                    "course=" + course.getName() +
                    ", student=" + student.getName() +
                    '}';
        }
    }
}
