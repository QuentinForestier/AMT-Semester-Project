INSERT INTO article(id, description, img_path, in_stock, name, price)
VALUES (1, 'joli snowboard', '~/images/pic5.jpg', true, 'snowboard', 100),
	(2, 'jolis skis', '~/images/pic5.jpg', true, 'skis', 200),
	(3, 'ski de fond salomon skating modèle coupe du monde', '~/images/pic5.jpg', true, 'ski de fond skating salomon', 500),
	(4, 'ski freestyle Movement pour le big air', '~/images/pic5.jpg', true, 'ski freestyle', 400);

INSERT INTO category(id, name)
VALUES (1, 'ski'),
	(2, 'snowboard'),
	(3, 'ski de fond'),
	(4, 'freestyle');

INSERT INTO category_article(fk_category_id, fk_article_id)
VALUES (1, 2),
	(2, 1),
	(3, 3),
	(4, 4);