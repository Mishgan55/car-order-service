package innowise.khorsun.carorderservice.repositorie;

import innowise.khorsun.carorderservice.entity.Booking;
import innowise.khorsun.carorderservice.util.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Integer> {
    @Query("SELECT b " +
            "FROM Booking b " +
            "LEFT JOIN b.user u " +
            "WHERE u.id = (:userId) "+
            "and b.status = (:status)")
    Optional<Booking> findBookingByUserIdAndStatus(Integer userId, Status status);

    Optional<Booking> findBookingByIdAndStatus(Integer bookingId,Status status);

    List<Booking> findBookingByUserId(Integer userId);
}
