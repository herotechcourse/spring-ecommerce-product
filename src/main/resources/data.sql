INSERT INTO products (name, price, img, quantity) VALUES
('Lotion', 10, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSn2oLfY5lyGWYclR5fOrDkXxGHU-xgy7rPTQ&s', 10),
('Cream', 11, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ8adusJSXz4emWhIh-gReKrPxjSW4xERp3Hw&s', 15),
('Lip balm', 4, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQQz7yiOlwdwC7Tojs4ziJRxbo_iqXRQCbc2g&s', 20);

INSERT INTO members (id, email, password, role, name) VALUES
('76393748-91b6-40f2-ac4c-392b796e5119', 'admin@example.com', '$2a$10$n2vtK51MdnDEglkYyi.OFOj3700sZIcUMzDssgwUg4GwnodHuul9q', 'ROLE_ADMIN', 'Admin User'),
('63da4b69-58aa-4e66-86aa-900e7e7d7744', 'user1@example.com', '$2a$10$i3PwieKXIgkoTvjPgQW9.eiU.ZIR5T7lYpy2DP36cWxE5gHFmiLXC', 'ROLE_USER', 'User One'),
('7605a72f-8bef-40fb-a0fa-abf55c3d72bf', 'user2@example.com', '$2a$10$TLPPe313R5E8UnUw9KCEm.uGLjl8LF1X4k5c.7rHaYOlpSh5wOLMO', 'ROLE_USER', 'User Two');
--
---- Cart items for user1@example.com (member ID: YOUR_GENERATED_UUID_2)
--INSERT INTO cart_items (member_id, product_id, quantity, created_at) VALUES
--('63da4b69-58aa-4e66-86aa-900e7e7d7744', 1, 1, PARSEDATETIME('2025-07-25 08:00:00.000', 'yyyy-MM-dd HH:mm:ss.SSS')),
--('63da4b69-58aa-4e66-86aa-900e7e7d7744', 3, 3, PARSEDATETIME('2025-07-26 14:00:00.000', 'yyyy-MM-dd HH:mm:ss.SSS'));
--
---- Cart items for user2@example.com (member ID: YOUR_GENERATED_UUID_3)
--INSERT INTO cart_items (member_id, product_id, quantity, created_at) VALUES
--('7605a72f-8bef-40fb-a0fa-abf55c3d72bf', 4, 1, PARSEDATETIME('2025-06-01 09:00:00.000', 'yyyy-MM-dd HH:mm:ss.SSS'));
