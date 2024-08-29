-- -- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema reisparadijs
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema reisparadijs
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `reisparadijs` DEFAULT CHARACTER SET utf8 ;
USE `reisparadijs` ;

-- -----------------------------------------------------
-- Drop tables
-- -----------------------------------------------------

-- Disable foreign key checks to allow dropping tables in any order
SET FOREIGN_KEY_CHECKS=0;

-- Drop tables in the reverse order of creation, taking into account foreign key dependencies
DROP TABLE IF EXISTS `reisparadijs`.`favorite_accommodation`;
DROP TABLE IF EXISTS `reisparadijs`.`service_rating`;
DROP TABLE IF EXISTS `reisparadijs`.`reservation_accommodation_guest`;
DROP TABLE IF EXISTS `reisparadijs`.`amenities_park_location`;
DROP TABLE IF EXISTS `reisparadijs`.`accommodation_amenities`;
DROP TABLE IF EXISTS `reisparadijs`.`user_role`;
# DROP TABLE IF EXISTS `reisparadijs`.`area`;
DROP TABLE IF EXISTS `reisparadijs`.`review`;
DROP TABLE IF EXISTS `reisparadijs`.`reservation_accommodation`;
DROP TABLE IF EXISTS `reisparadijs`.`message`;
DROP TABLE IF EXISTS `reisparadijs`.`message_subject`;
DROP TABLE IF EXISTS `reisparadijs`.`reservation`;
DROP TABLE IF EXISTS `reisparadijs`.`image`;
DROP TABLE IF EXISTS `reisparadijs`.`amenities`;
DROP TABLE IF EXISTS `reisparadijs`.`role`;
DROP TABLE IF EXISTS `reisparadijs`.`accommodation`;
DROP TABLE IF EXISTS `reisparadijs`.`park_location`;
DROP TABLE IF EXISTS `reisparadijs`.`accommodation_type`;
DROP TABLE IF EXISTS `reisparadijs`.`user`;
DROP TABLE IF EXISTS `reisparadijs`.`token`;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS=1;


-- -----------------------------------------------------
-- Table `reisparadijs`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `reisparadijs`.`user` (
                                                     `id` INT NOT NULL AUTO_INCREMENT,
                                                     `user_name` VARCHAR(45) NOT NULL,
                                                     `email` VARCHAR(45) NOT NULL,
                                                     `first_name` VARCHAR(255) NOT NULL,
                                                     `infix` VARCHAR(45) NULL,
                                                     `last_name` VARCHAR(255) NOT NULL,
                                                     `password` LONGTEXT NOT NULL,
                                                     `gender` ENUM('male', 'female', 'undisclosed') NOT NULL,
                                                     `profile_image` BLOB NULL,
                                                     `joined_at` DATE NOT NULL DEFAULT (CURRENT_DATE),
                                                     `enabled` TINYINT NOT NULL DEFAULT 0,
                                                     PRIMARY KEY (`id`),
                                                     UNIQUE INDEX `user_name_UNIQUE` (`user_name` ASC) VISIBLE,
                                                     UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `reisparadijs`.`token`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `reisparadijs`.`token` (
                                                       `id` INT NOT NULL AUTO_INCREMENT,
                                                       `token` VARCHAR(155) NOT NULL,
                                                       `token_type` ENUM('verification', 'password_reset') NOT NULL DEFAULT 'verification',
                                                       `expiration_date` DATETIME NOT NULL,
                                                       `user_id` INT NOT NULL,
                                                       PRIMARY KEY (`id`),
                                                       INDEX `fk_token_user_idx` (`user_id` ASC) VISIBLE,
                                                       CONSTRAINT `fk_token_user`
                                                           FOREIGN KEY (`user_id`)
                                                               REFERENCES `reisparadijs`.`user` (`id`)
                                                               ON DELETE CASCADE
                                                               ON UPDATE CASCADE)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `reisparadijs`.`accommodation_type`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `reisparadijs`.`accommodation_type` (
                                                                   `id` INT NOT NULL AUTO_INCREMENT,
                                                                   `name` VARCHAR(45) NOT NULL,
                                                                   PRIMARY KEY (`id`),
                                                                   UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `reisparadijs`.`park_location`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `reisparadijs`.`park_location` (
                                                              `id` INT NOT NULL,
                                                              `zipcode` VARCHAR(45) NOT NULL,
                                                              `number` VARCHAR(45) NOT NULL,
                                                              `description` LONGTEXT NOT NULL,
                                                              `name` VARCHAR(45) NOT NULL,
                                                              PRIMARY KEY (`id`))
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `reisparadijs`.`accommodation`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `reisparadijs`.`accommodation` (
                                                              `id` INT NOT NULL AUTO_INCREMENT,
                                                              `zip_code` VARCHAR(45) NOT NULL,
                                                              `house_number` VARCHAR(45) NOT NULL,
                                                              `title` MEDIUMTEXT NOT NULL,
                                                              `description` LONGTEXT NOT NULL,
                                                              `price_per_day` DECIMAL(8,2) NOT NULL,
                                                              `number_of_guests` INT NOT NULL,
                                                              `number_of_bedrooms` INT NOT NULL,
                                                              `number_of_bathrooms` INT NOT NULL,
                                                              `number_of_beds` INT NOT NULL,
                                                              `published_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                                              `is_active` TINYINT NOT NULL DEFAULT 1,
                                                              `deleted_at` DATETIME NULL,
                                                              `host_id` INT NOT NULL,
                                                              `accommodation_type_id` INT NOT NULL,
                                                              `park_location_id` INT NULL,
                                                              PRIMARY KEY (`id`, `host_id`, `accommodation_type_id`),
                                                              INDEX `fk_accommodation_user1_idx` (`host_id` ASC) VISIBLE,
                                                              INDEX `fk_accommodation_accommodation_type1_idx` (`accommodation_type_id` ASC) VISIBLE,
                                                              INDEX `fk_accommodation_park_location1_idx` (`park_location_id` ASC) VISIBLE,
                                                              CONSTRAINT `fk_accommodation_user1`
                                                                  FOREIGN KEY (`host_id`)
                                                                      REFERENCES `reisparadijs`.`user` (`id`)
                                                                      ON DELETE NO ACTION
                                                                      ON UPDATE NO ACTION,
                                                              CONSTRAINT `fk_accommodation_accommodation_type1`
                                                                  FOREIGN KEY (`accommodation_type_id`)
                                                                      REFERENCES `reisparadijs`.`accommodation_type` (`id`)
                                                                      ON DELETE NO ACTION
                                                                      ON UPDATE NO ACTION,
                                                              CONSTRAINT `fk_accommodation_park_location1`
                                                                  FOREIGN KEY (`park_location_id`)
                                                                      REFERENCES `reisparadijs`.`park_location` (`id`)
                                                                      ON DELETE NO ACTION
                                                                      ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `reisparadijs`.`role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `reisparadijs`.`role` (
                                                     `id` INT NOT NULL AUTO_INCREMENT,
                                                     `name` VARCHAR(45) NOT NULL,
                                                     PRIMARY KEY (`id`),
                                                     UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `reisparadijs`.`amenities`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `reisparadijs`.`amenities` (
                                                          `id` INT NOT NULL AUTO_INCREMENT,
                                                          `name` VARCHAR(255) NOT NULL,
                                                          `description` LONGTEXT CHARACTER SET 'big5' NULL,
                                                          PRIMARY KEY (`id`))
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `reisparadijs`.`image`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `reisparadijs`.`image` (
                                                      `id` INT NOT NULL AUTO_INCREMENT,
                                                      `image` BLOB NOT NULL,
                                                      `accommodation_id` INT NOT NULL,
                                                      PRIMARY KEY (`id`, `accommodation_id`),
                                                      INDEX `fk_image_accommodation1_idx` (`accommodation_id` ASC) VISIBLE,
                                                      CONSTRAINT `fk_image_accommodation1`
                                                          FOREIGN KEY (`accommodation_id`)
                                                              REFERENCES `reisparadijs`.`accommodation` (`id`)
                                                              ON DELETE NO ACTION
                                                              ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `reisparadijs`.`reservation`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `reisparadijs`.`reservation` (
                                                            `id` INT NOT NULL AUTO_INCREMENT,
                                                            `total_price` DECIMAL(8,2) NULL,
                                                            `booking_status` ENUM('Awaiting', 'Approval', 'Approved', 'Cancelled', 'Completed') NOT NULL DEFAULT 'Awaiting',
                                                            `guest_id` INT NOT NULL,
                                                            `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                                            PRIMARY KEY (`id`, `guest_id`),
                                                            INDEX `fk_reservation_user1_idx` (`guest_id` ASC) VISIBLE,
                                                            CONSTRAINT `fk_reservation_user1`
                                                                FOREIGN KEY (`guest_id`)
                                                                    REFERENCES `reisparadijs`.`user` (`id`)
                                                                    ON DELETE NO ACTION
                                                                    ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `reisparadijs`.`message_subject`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `reisparadijs`.`message_subject` (
                                                                `id` INT NOT NULL AUTO_INCREMENT,
                                                                `title` VARCHAR(255) NOT NULL,
                                                                PRIMARY KEY (`id`))
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `reisparadijs`.`message`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `reisparadijs`.`message` (
                                                        `id` INT NOT NULL AUTO_INCREMENT,
                                                        `content` LONGTEXT NOT NULL,
                                                        `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                                        `parent_message_id` INT NULL,
                                                        `sender_id` INT NOT NULL,
                                                        `reciever_id` INT NOT NULL,
                                                        `reservation_id` INT NULL,
                                                        `message_subject_id` INT NOT NULL,
                                                        PRIMARY KEY (`id`, `sender_id`, `reciever_id`, `message_subject_id`),
                                                        INDEX `fk_message_message_idx` (`parent_message_id` ASC) VISIBLE,
                                                        INDEX `fk_message_user1_idx` (`sender_id` ASC) VISIBLE,
                                                        INDEX `fk_message_user2_idx` (`reciever_id` ASC) VISIBLE,
                                                        INDEX `fk_message_reservation1_idx` (`reservation_id` ASC) VISIBLE,
                                                        INDEX `fk_message_message_subject1_idx` (`message_subject_id` ASC) VISIBLE,
                                                        CONSTRAINT `fk_message_message`
                                                            FOREIGN KEY (`parent_message_id`)
                                                                REFERENCES `reisparadijs`.`message` (`id`)
                                                                ON DELETE NO ACTION
                                                                ON UPDATE NO ACTION,
                                                        CONSTRAINT `fk_message_user1`
                                                            FOREIGN KEY (`sender_id`)
                                                                REFERENCES `reisparadijs`.`user` (`id`)
                                                                ON DELETE NO ACTION
                                                                ON UPDATE NO ACTION,
                                                        CONSTRAINT `fk_message_user2`
                                                            FOREIGN KEY (`reciever_id`)
                                                                REFERENCES `reisparadijs`.`user` (`id`)
                                                                ON DELETE NO ACTION
                                                                ON UPDATE NO ACTION,
                                                        CONSTRAINT `fk_message_reservation1`
                                                            FOREIGN KEY (`reservation_id`)
                                                                REFERENCES `reisparadijs`.`reservation` (`id`)
                                                                ON DELETE NO ACTION
                                                                ON UPDATE NO ACTION,
                                                        CONSTRAINT `fk_message_message_subject1`
                                                            FOREIGN KEY (`message_subject_id`)
                                                                REFERENCES `reisparadijs`.`message_subject` (`id`)
                                                                ON DELETE NO ACTION
                                                                ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `reisparadijs`.`reservation_accommodation`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `reisparadijs`.`reservation_accommodation` (
                                                                          `id` INT NOT NULL AUTO_INCREMENT,
                                                                          `reservation_id` INT NOT NULL,
                                                                          `accommodation_id` INT NOT NULL,
                                                                          `checkin_date` DATE NOT NULL,
                                                                          `checkout_date` DATE NOT NULL,
                                                                          `price_per_day` DECIMAL(8,2) NOT NULL,
                                                                          PRIMARY KEY (`id`, `reservation_id`, `accommodation_id`),
                                                                          INDEX `fk_reservation_has_accommodation_accommodation1_idx` (`accommodation_id` ASC) VISIBLE,
                                                                          INDEX `fk_reservation_has_accommodation_reservation1_idx` (`reservation_id` ASC) VISIBLE,
                                                                          CONSTRAINT `fk_reservation_has_accommodation_reservation1`
                                                                              FOREIGN KEY (`reservation_id`)
                                                                                  REFERENCES `reisparadijs`.`reservation` (`id`)
                                                                                  ON DELETE NO ACTION
                                                                                  ON UPDATE NO ACTION,
                                                                          CONSTRAINT `fk_reservation_has_accommodation_accommodation1`
                                                                              FOREIGN KEY (`accommodation_id`)
                                                                                  REFERENCES `reisparadijs`.`accommodation` (`id`)
                                                                                  ON DELETE NO ACTION
                                                                                  ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `reisparadijs`.`review`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `reisparadijs`.`review` (
                                                       `id` INT NOT NULL AUTO_INCREMENT,
                                                       `reservation_accommodation_id` INT NOT NULL,
                                                       `rating` INT NOT NULL,
                                                       `comment` LONGTEXT NOT NULL,
                                                       PRIMARY KEY (`id`, `reservation_accommodation_id`),
                                                       INDEX `fk_review_reservation_accommodation1_idx` (`reservation_accommodation_id` ASC) VISIBLE,
                                                       CONSTRAINT `fk_review_reservation_accommodation1`
                                                           FOREIGN KEY (`reservation_accommodation_id`)
                                                               REFERENCES `reisparadijs`.`reservation_accommodation` (`id`)
                                                               ON DELETE NO ACTION
                                                               ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `reisparadijs`.`area`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `reisparadijs`.`area` (
                                                     `zipcodeNumbers` INT NOT NULL,
                                                     `place` VARCHAR(255) NOT NULL,
                                                     `municipality` VARCHAR(255) NOT NULL,
                                                     `province` VARCHAR(255) NOT NULL,
                                                     `zcLat` DOUBLE NOT NULL,
                                                     `zcLon` DOUBLE NOT NULL,
                                                     `Area_1` TEXT NULL,
                                                     `Area_2` TEXT NULL,
                                                     `Area_3` TEXT NULL,
                                                     PRIMARY KEY (`zipcodeNumbers`))
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `reisparadijs`.`user_role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `reisparadijs`.`user_role` (
                                                          `user_id` INT NOT NULL,
                                                          `role_id` INT NOT NULL,
                                                          PRIMARY KEY (`user_id`, `role_id`),
                                                          INDEX `fk_user_has_role_role1_idx` (`role_id` ASC) VISIBLE,
                                                          INDEX `fk_user_has_role_user1_idx` (`user_id` ASC) VISIBLE,
                                                          CONSTRAINT `fk_user_has_role_user1`
                                                              FOREIGN KEY (`user_id`)
                                                                  REFERENCES `reisparadijs`.`user` (`id`)
                                                                  ON DELETE NO ACTION
                                                                  ON UPDATE NO ACTION,
                                                          CONSTRAINT `fk_user_has_role_role1`
                                                              FOREIGN KEY (`role_id`)
                                                                  REFERENCES `reisparadijs`.`role` (`id`)
                                                                  ON DELETE NO ACTION
                                                                  ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `reisparadijs`.`accommodation_amenities`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `reisparadijs`.`accommodation_amenities` (
                                                                        `accommodation_id` INT NOT NULL,
                                                                        `amenities_id` INT NOT NULL,
                                                                        PRIMARY KEY (`accommodation_id`, `amenities_id`),
                                                                        INDEX `fk_accommodation_has_amenities_amenities1_idx` (`amenities_id` ASC) VISIBLE,
                                                                        INDEX `fk_accommodation_has_amenities_accommodation1_idx` (`accommodation_id` ASC) VISIBLE,
                                                                        CONSTRAINT `fk_accommodation_has_amenities_accommodation1`
                                                                            FOREIGN KEY (`accommodation_id`)
                                                                                REFERENCES `reisparadijs`.`accommodation` (`id`)
                                                                                ON DELETE NO ACTION
                                                                                ON UPDATE NO ACTION,
                                                                        CONSTRAINT `fk_accommodation_has_amenities_amenities1`
                                                                            FOREIGN KEY (`amenities_id`)
                                                                                REFERENCES `reisparadijs`.`amenities` (`id`)
                                                                                ON DELETE NO ACTION
                                                                                ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `reisparadijs`.`amenities_park_location`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `reisparadijs`.`amenities_park_location` (
                                                                        `amenities_id` INT NOT NULL,
                                                                        `park_location_id` INT NOT NULL,
                                                                        PRIMARY KEY (`amenities_id`, `park_location_id`),
                                                                        INDEX `fk_amenities_has_park_location_park_location1_idx` (`park_location_id` ASC) VISIBLE,
                                                                        INDEX `fk_amenities_has_park_location_amenities1_idx` (`amenities_id` ASC) VISIBLE,
                                                                        CONSTRAINT `fk_amenities_has_park_location_amenities1`
                                                                            FOREIGN KEY (`amenities_id`)
                                                                                REFERENCES `reisparadijs`.`amenities` (`id`)
                                                                                ON DELETE NO ACTION
                                                                                ON UPDATE NO ACTION,
                                                                        CONSTRAINT `fk_amenities_has_park_location_park_location1`
                                                                            FOREIGN KEY (`park_location_id`)
                                                                                REFERENCES `reisparadijs`.`park_location` (`id`)
                                                                                ON DELETE NO ACTION
                                                                                ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `reisparadijs`.`reservation_accommodation_guest`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `reisparadijs`.`reservation_accommodation_guest` (
                                                                                `id` INT NOT NULL AUTO_INCREMENT,
                                                                                `reservation_accommodation_reservation_id` INT NOT NULL,
                                                                                `guest_type` ENUM('Adults', 'Children', 'Infants', 'Pets') NOT NULL,
                                                                                `number_of_guests` INT NOT NULL,
                                                                                PRIMARY KEY (`id`),
                                                                                INDEX `fk_reservation_accommodation_has_guest_type_reservation_acc_idx` (`reservation_accommodation_reservation_id` ASC) VISIBLE,
                                                                                CONSTRAINT `fk_reservation_accommodation_has_guest_type_reservation_accom1`
                                                                                    FOREIGN KEY (`reservation_accommodation_reservation_id`)
                                                                                        REFERENCES `reisparadijs`.`reservation_accommodation` (`reservation_id`)
                                                                                        ON DELETE NO ACTION
                                                                                        ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `reisparadijs`.`service_rating`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `reisparadijs`.`service_rating` (
                                                               `id` INT NOT NULL AUTO_INCREMENT,
                                                               `rating` INT NOT NULL,
                                                               `service_review` ENUM('Cleanliness', 'Accuracy', 'Check-in', 'Communication', 'Location', 'Value') NOT NULL,
                                                               `reservation_accommodation_id` INT NOT NULL,
                                                               PRIMARY KEY (`id`, `reservation_accommodation_id`),
                                                               INDEX `fk_service_rating_reservation_accommodation1_idx` (`reservation_accommodation_id` ASC) VISIBLE,
                                                               CONSTRAINT `fk_service_rating_reservation_accommodation1`
                                                                   FOREIGN KEY (`reservation_accommodation_id`)
                                                                       REFERENCES `reisparadijs`.`reservation_accommodation` (`id`)
                                                                       ON DELETE NO ACTION
                                                                       ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `reisparadijs`.`favorite_accommodation`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `reisparadijs`.`favorite_accommodation` (
                                                                       `guest_id` INT NOT NULL,
                                                                       `accommodation_id` INT NOT NULL,
                                                                       PRIMARY KEY (`guest_id`, `accommodation_id`),
                                                                       INDEX `fk_user_has_accommodation_accommodation1_idx` (`accommodation_id` ASC) VISIBLE,
                                                                       INDEX `fk_user_has_accommodation_user1_idx` (`guest_id` ASC) VISIBLE,
                                                                       CONSTRAINT `fk_user_has_accommodation_user1`
                                                                           FOREIGN KEY (`guest_id`)
                                                                               REFERENCES `reisparadijs`.`user` (`id`)
                                                                               ON DELETE NO ACTION
                                                                               ON UPDATE NO ACTION,
                                                                       CONSTRAINT `fk_user_has_accommodation_accommodation1`
                                                                           FOREIGN KEY (`accommodation_id`)
                                                                               REFERENCES `reisparadijs`.`accommodation` (`id`)
                                                                               ON DELETE NO ACTION
                                                                               ON UPDATE NO ACTION)
    ENGINE = InnoDB;







# CREATE USER 'userReisParadijs' @'localhost' IDENTIFIED BY 'pwReisParadijsrole';
#
# GRANT ALL privileges ON reisparadijs.* TO 'userReisParadijs' @'localhost';




SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;