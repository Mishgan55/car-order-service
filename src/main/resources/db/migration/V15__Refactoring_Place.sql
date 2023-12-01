alter table car_order_service_db.public.places
drop column location;

alter table car_order_service_db.public.places
add column location point;

insert into car_order_service_db.public.places(location) values (point(53.8981819152832, 30.331588745117188));
