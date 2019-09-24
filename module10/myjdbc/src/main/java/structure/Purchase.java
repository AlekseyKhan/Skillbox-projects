package structure;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "Purchases")
public class Purchase {
    @EmbeddedId
    private PurchaseKey key;

    @Column(name = "student_name", nullable = false)
    private String studentName;

    @Column(name = "course_name", nullable = false)
    private String courseName;

    @Column(nullable = false)
    private Integer price;

    @Column(name = "subscription_date", nullable = false)
    private Date subscriptionDate;

    public Purchase(Student student, Course course, Integer price, Date subscriptionDate) {
        this.key = new PurchaseKey(student, course);
        this.studentName = student.getName();
        this.courseName = course.getName();
        this.price = price;
        this.subscriptionDate = subscriptionDate;
    }

    public PurchaseKey getKey() {
        return key;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Date getSubscriptionDate() {
        return subscriptionDate;
    }

    public void setSubscriptionDate(Date subscriptionDate) {
        this.subscriptionDate = subscriptionDate;
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "studentName='" + studentName + '\'' +
                ", courseName='" + courseName + '\'' +
                ", price=" + price +
                ", subscriptionDate=" + subscriptionDate +
                '}';
    }

    public static class PurchaseKey implements Serializable {
        @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        @JoinColumn(
                foreignKey = @ForeignKey(name = "fk_student"),
                name = "student_id",
                columnDefinition = "int(10) unsigned"
        )
        protected Student student;

        @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        @JoinColumn(
                foreignKey = @ForeignKey(name = "fk_course"),
                name = "course_id",
                columnDefinition = "int(10) unsigned"
        )
        protected Course course;

        public PurchaseKey(Student student, Course course) {
            this.student = student;
            this.course = course;
        }
    }
}
