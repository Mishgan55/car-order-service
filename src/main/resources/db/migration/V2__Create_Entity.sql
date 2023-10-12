insert into car_order_service_db.public.places(name, address, work_hours, contact_information) VALUES
('Mog-service', 'blvd Nepokaronnyh 61', '24H', '+37529884912242');
insert into car_order_service_db.public.cars(place_id, brand, model, year_of_production, plate_number, is_available)
VALUES (1, 'Mercedes', 'E-class', 2015, '0005 IT-6', false);
insert into car_order_service_db.public.cars(place_id, brand, model, year_of_production, plate_number, is_available)
VALUES (1, 'Mercedes', 'E-class', 2020, '7226 IT-6', false);