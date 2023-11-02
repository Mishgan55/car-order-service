package innowise.khorsun.carorderservice.mapper;

import innowise.khorsun.carorderservice.dto.BookingDto;
import innowise.khorsun.carorderservice.entity.Booking;
import innowise.khorsun.carorderservice.model.BookingRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface BookingMapper {
    @Mapping(target = "user.id",source = "userId")
    @Mapping(target = "car.id",source = "carId")
    Booking bookingDtoToBooking(BookingDto bookingDto);
    @Mapping(target = "userId",source = "user.id")
    @Mapping(target = "carId",source = "car.id")
    BookingDto bookingToBookingDto(Booking booking);
    @Mapping(target = "user.id",source = "userId")
    @Mapping(target = "car.id",source = "carId")
    Booking bookingRequestModelToBooking(BookingRequestModel bookingRequestModel);
}
