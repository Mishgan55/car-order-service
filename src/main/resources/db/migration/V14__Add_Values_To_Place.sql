alter table car_order_service_db.public.places
drop column name;

insert into car_order_service_db.public.places(location)
values (ST_GeographyFromText('SRID=4326;POINT(53.8981819152832 30.331588745117188)')),
       (ST_GeographyFromText('SRID=4326;POINT(53.897743225097656 30.335678100585938)')),
       (ST_GeographyFromText('SRID=4326;POINT(53.895511627197266 30.33317756652832)'));