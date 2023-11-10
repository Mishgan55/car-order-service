package innowise.khorsun.carorderservice.entity;

import innowise.khorsun.carorderservice.util.enums.Status;
import innowise.khorsun.carorderservice.util.enums.Type;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;
    @Column(name = "session_id")
    private String sessionId;
    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private Status status;
    @Column(name = "url")
    private URL url;
    @Column(name = "payment_amount")
    private BigDecimal paymentAmount;
    @Column(name = "type")
    @Enumerated(value = EnumType.STRING)
    private Type type;
    @Column(name = "payment_date")
    private LocalDateTime paymentDate;
}
