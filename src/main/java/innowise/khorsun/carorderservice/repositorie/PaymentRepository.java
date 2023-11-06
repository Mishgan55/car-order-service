package innowise.khorsun.carorderservice.repositorie;

import innowise.khorsun.carorderservice.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Integer> {
    Payment findBySessionId(String sessionId);
}
