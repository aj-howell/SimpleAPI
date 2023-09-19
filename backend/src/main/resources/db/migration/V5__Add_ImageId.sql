ALTER TABLE customer
ADD image_id VARCHAR(65);

ALTER TABLE customer
ADD CONSTRAINT customer_image_id_unqiue
UNIQUE(image_id);