SET REFERENTIAL_INTEGRITY FALSE;
DELETE FROM cart_items;
DELETE FROM members WHERE email NOT IN ('user1@example.com', 'admin@example.com');
SET REFERENTIAL_INTEGRITY TRUE;
