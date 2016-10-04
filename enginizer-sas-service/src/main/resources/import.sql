INSERT INTO `engineDB`.`sas_user` (`user_id`, `email`, `first_name`, `last_name`, `status`) VALUES ('1', 'admin@mail.com', 'some', 'one', 'A');
INSERT INTO `engineDB`.`sas_principal_types` (`principal_type_id`, `description`) VALUES ('USERNAME', 'The username principal type.');
INSERT INTO `engineDB`.`sas_credential_types` (`credential_type_id`, `description`) VALUES ('PASSWORD', 'The password credential type.');
INSERT INTO `engineDB`.`sas_frontends` (`frontend_id`, `description`) VALUES ('WEB', 'The WEB frontend.');
INSERT INTO `engineDB`.`sas_principals` (`principal_id`, `principal_data`, `principal_type_id`, `user_id`, `valid_from`, `valid_until`) VALUES ('1', 'xxx', 'USERNAME', '1', '2001-01-01', '2021-01-01');
INSERT INTO `engineDB`.`sas_credentials` (`credential_id`, `credential_data`, `credential_type_id`, `status`, `valid_until`) VALUES ('1', '$2a$10$NavKdjtEk4u6Swrbf5tQ9e126kLuU86BKNUYutDY5S.n7OgTAiYx6', 'PASSWORD', 'A', '2021-01-01');
INSERT INTO `engineDB`.`sas_credential_frontend` (`frontend_id`, `credential_type_id`) VALUES ('WEB', 'PASSWORD');
INSERT INTO `engineDB`.`sas_principal_credential` (`principal_id`, `credential_id`) VALUES ('1', '1');
INSERT INTO `engineDB`.`sas_role` (`role_id`, `role_name`) VALUES ('WEB_USER', 'WEB_USER');
INSERT INTO `engineDB`.`sas_user_role` (`user_id`, `role_id`) VALUES ('1', 'WEB_USER');
COMMIT ;