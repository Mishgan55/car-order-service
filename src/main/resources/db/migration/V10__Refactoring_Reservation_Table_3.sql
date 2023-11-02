alter table car_order_service_db.public.reservations
    drop constraint reservations_car_id_fkey;

alter table car_order_service_db.public.reservations
rename to booking;

alter table booking add column status varchar;

