-- -----------------------------------------------------
-- Role
-- -----------------------------------------------------
insert into role (name) values ('ROLE_ADMIN');
insert into role (name) values ('ROLE_HOST');
insert into role (name) values ('ROLE_GUEST');

-- -----------------------------------------------------
-- User(password2)
-- -----------------------------------------------------
insert into user(user_name, email, first_name, infix, last_name, password, gender, profile_image, enabled)
values ('admin', 'w9bHh@example.com', 'Admin', '',
        'Admin', '$2a$10$/cmodmRqdwhNLIS5wLJIgO/VghNwFRzHnxJYrA96I0dyms/MFV5U6', 'male', null, true);

insert into user(user_name, email, first_name, infix, last_name, password, gender, profile_image, enabled)
values ('host', 'w9bHh1@example.com', 'Host', '',
        'Host', '$2a$10$/cmodmRqdwhNLIS5wLJIgO/VghNwFRzHnxJYrA96I0dyms/MFV5U6', 'female', null, true);

insert into user(user_name, email, first_name, infix, last_name, password, gender, profile_image, enabled)
values ('guest', 'w9bHh2@example.com', 'Guest', '',
        'Guest', '$2a$10$/cmodmRqdwhNLIS5wLJIgO/VghNwFRzHnxJYrA96I0dyms/MFV5U6', 'male', null, true);

insert into user_role (user_id, role_id) values (1, 1);
insert into user_role (user_id, role_id) values (2, 2);
insert into user_role (user_id, role_id) values (3, 3);

-- -----------------------------------------------------
-- Message
-- -----------------------------------------------------
INSERT INTO message_subject (title)
VALUES
    ('Reservation Confirmation'),
    ('Travel Itinerary Update'),
    ('Payment Receipt'),
    ('Special Offers & Discounts'),
    ('Travel Insurance Information'),
    ('Flight Cancellation Notice'),
    ('Booking Assistance'),
    ('Visa Requirements & Assistance'),
    ('Customer Feedback Request'),
    ('Loyalty Program Updates');

insert into message (content,  parent_message_id, sender_id, reciever_id, reservation_id, message_subject_id)
values ('Please let me know if you have any availability next week, and we will schedule the meeting accordingly. In the meantime, please let me know if you have any questions or concerns. Thank you for your time and consideration. Best regards, Home Boys', null, 2, 3, null, 1);
insert into message (content,  parent_message_id, sender_id, reciever_id, reservation_id, message_subject_id)
values ('Please let me know if you have any availability next week, and we will schedule the meeting accordingly. In the meantime, please let me know if you have any questions or concerns. Thank you for your time and consideration. Best regards, Home Boys', null, 2, 3, null, 2);

-- -----------------------------------------------------
-- Accommodation
-- -----------------------------------------------------
insert into accommodation_type (name) values ('Huis');
insert into accommodation_type (name) values ('Villa');
insert into accommodation_type (name) values ('Appartement');

insert into accommodation (zip_code, house_number, title, description, price_per_day, number_of_guests, number_of_bedrooms, number_of_bathrooms, number_of_beds, host_id, accommodation_type_id)
values
    ('1234AA', '1', 'Reisparadijs - accommodatie-1',
     'Reisparadijs - accommodatie-1 beschrijving',
     100, 1, 1, 1, 1,
     2,1);

insert into accommodation (zip_code, house_number, title, description, price_per_day, number_of_guests, number_of_bedrooms, number_of_bathrooms, number_of_beds, host_id, accommodation_type_id)
values
    ('1234AB', '1', 'Reisparadijs - accommodatie-2',
     'Reisparadijs - accommodatie-1 beschrijving',
     200, 4, 2, 1, 2,
     2,2);

-- -----------------------------------------------------
-- Amenities
-- -----------------------------------------------------
INSERT INTO amenities (name, description) VALUES ('Wifi', 'Reisparadijs - Wifi beschrijving');
INSERT INTO amenities (name, description) VALUES ('Kitchen', 'Reisparadijs - Kitchen beschrijving');
INSERT INTO amenities (name, description) VALUES ('Washer', 'Reisparadijs - Washer beschrijving');
INSERT INTO amenities (name, description) VALUES ('Dryer', 'Reisparadijs - Dryer beschrijving');
INSERT INTO amenities (name, description) VALUES ('Pool', 'Reisparadijs - Pool beschrijving');
INSERT INTO amenities (name, description) VALUES ('Hot tub', 'Reisparadijs - Hot tub beschrijving');
INSERT INTO amenities (name, description) VALUES ('Free parking', 'Reisparadijs - Free parking beschrijving');
INSERT INTO amenities (name, description) VALUES ('EV charger', 'Reisparadijs - EV charger beschrijving');
INSERT INTO amenities (name, description) VALUES ('Crib', 'Reisparadijs - Crib beschrijving');
INSERT INTO amenities (name, description) VALUES ('King-size bed', 'Reisparadijs - King-size bed beschrijving');
INSERT INTO amenities (name, description) VALUES ('Gym', 'Reisparadijs - Gym beschrijving');
INSERT INTO amenities (name, description) VALUES ('BBQ grill', 'Reisparadijs - BBQ grill beschrijving');
INSERT INTO amenities (name, description) VALUES ('Breakfast', 'Reisparadijs - Breakfast beschrijving');
INSERT INTO amenities (name, description) VALUES ('Indoor fireplace', 'Reisparadijs - Indoor fireplace beschrijving');
INSERT INTO amenities (name, description) VALUES ('Smoking allowed', 'Reisparadijs - Smoking allowed beschrijving');
INSERT INTO amenities (name, description) VALUES ('Air conditioning', 'Reisparadijs - Air conditioning beschrijving');
INSERT INTO amenities (name, description) VALUES ('Heating', 'Reisparadijs - Heating beschrijving');
INSERT INTO amenities (name, description) VALUES ('Dedicated workspace', 'Reisparadijs - Dedicated workspace beschrijving');
INSERT INTO amenities (name, description) VALUES ('TV', 'Reisparadijs - TV beschrijving');
INSERT INTO amenities (name, description) VALUES ('Hair dryer', 'Reisparadijs - Hair dryer beschrijving');
INSERT INTO amenities (name, description) VALUES ('Iron', 'Reisparadijs - Iron beschrijving');
INSERT INTO amenities (name, description) VALUES ('Smoke alarm', 'Reisparadijs - Smoke alarm beschrijving');
INSERT INTO amenities (name, description) VALUES ('Carbon monoxide alarm', 'Reisparadijs - Carbon monoxide alarm beschrijving');


