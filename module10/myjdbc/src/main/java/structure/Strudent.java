package structure;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "students")
public class Strudent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private int age;
    @Column(name = "registration_date")
    private Date registrationDate;
}
