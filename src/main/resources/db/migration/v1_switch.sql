CREATE DATABASE IF NOT EXISTS `switch`;

use `switch`;

-- switch.roles definition

CREATE TABLE `roles` (
                         `id` bigint NOT NULL AUTO_INCREMENT,
                         `name` varchar(255) NOT NULL,
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `name` (`name`)
);

INSERT INTO roles (name) VALUES  ('ADMIN'), ( 'USER'),( 'ROLE_USER');

-- switch.users definition

CREATE TABLE `users` (
                         `id` bigint NOT NULL AUTO_INCREMENT,
                         `username` varchar(255) NOT NULL,
                         `email` varchar(255) NOT NULL,
                         `password` varchar(255) NOT NULL,
                         `enabled` tinyint(1) DEFAULT '0',
                         `created_dt` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `username` (`username`),
                         UNIQUE KEY `email` (`email`)
);

-- switch.user_roles definition

CREATE TABLE `user_roles` (
                              `user_id` bigint NOT NULL,
                              `role_id` bigint NOT NULL,
                              PRIMARY KEY (`role_id`,`user_id`),
                              KEY `fk_user` (`user_id`),
                              CONSTRAINT `fk_role` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE CASCADE,
                              CONSTRAINT `fk_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
);

-- switch.rest_apis definition

CREATE TABLE `rest_apis` (
                             `id` bigint NOT NULL AUTO_INCREMENT,
                             `url_patterns_path` varchar(255) DEFAULT NULL,
                             `http_methods_name` varchar(255) DEFAULT NULL,
                             `api_consumes` varchar(255) DEFAULT NULL,
                             `java_class_name` varchar(255) DEFAULT NULL,
                             `return_type` varchar(255) DEFAULT NULL,
                             `method_params_sign` varchar(255) DEFAULT NULL,
                             `created_dt` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                             `updated_dt` timestamp NULL DEFAULT NULL,
                             PRIMARY KEY (`id`)
);

-- switch.system_registry definition

CREATE TABLE `system_registry` (
                                   `id` bigint NOT NULL AUTO_INCREMENT,
                                   `system_key` varchar(255) NOT NULL,
                                   `system_value` varchar(6000) DEFAULT NULL,
                                   `description` varchar(255) DEFAULT NULL,
                                   PRIMARY KEY (`id`),
                                   UNIQUE KEY `system_key` (`system_key`)
);

