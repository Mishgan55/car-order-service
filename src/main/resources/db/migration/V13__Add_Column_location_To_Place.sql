alter table car_order_service_db.public.places
drop column address,drop column work_hours;

alter table car_order_service_db.public.places
add column  location geography(Point, 4326);