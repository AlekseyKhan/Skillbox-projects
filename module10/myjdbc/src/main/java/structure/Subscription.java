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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "course_id", insertable = false, updatable = false)
    protected Course course;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "student_id", insertable = false, updatable = false)
    protected Student student;

    public SubscriptionPK getKey() {
        return key;
    }

    public Date getSubscriptionsDate() {
        return subscriptionsDate;
    }

    public void setSubscriptionsDate(Date subscriptionsDate) {
        this.subscriptionsDate = subscriptionsDate;
    }

    public Course getCourse() {
        return course;
    }

    public Student getStudent() {
        return student;
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

        @Column(name = "course_id")
        protected int courseId;

        @Column(name = "student_id")
        protected int studentId;

        public SubscriptionPK(int studentId, int courseId) {
            this.studentId = studentId;
            this.courseId = courseId;
        }

        public SubscriptionPK() {
        }

        public int getCourse() {
            return courseId;
        }

        public int getStudent() {
            return studentId;
        }

        @Override
        public String toString() {
            return "SubscriptionPK{" +
                    "courseId=" + courseId +
                    ", studentId=" + studentId +
                    '}';
        }
    }
}
