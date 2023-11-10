package innowise.khorsun.carorderservice.controller;

import innowise.khorsun.carorderservice.dto.BookingDto;
import innowise.khorsun.carorderservice.model.BookingRequestModel;
import innowise.khorsun.carorderservice.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;
    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }
    @PostMapping
    public void addBooking(@RequestBody @Valid BookingRequestModel bookingRequestModel){
        bookingService.addBooking(bookingRequestModel);
    }
    @GetMapping("/{id}/get-bookings-by-user")
    public ResponseEntity<List<BookingDto>> getBookingsByUser(@PathVariable("id")Integer userId){
        return ResponseEntity.ok(bookingService.getBookingsByUserId(userId));
    }
    @GetMapping("/{id}")
    public ResponseEntity<BookingDto> getBookingDtoById(@PathVariable("id") Integer id){
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }
    @PostMapping("/{id}/return")
    public void returnBooking(@PathVariable("id") Integer id){
        bookingService.returnBooking(id);
    }
}
