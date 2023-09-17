ALTER TABLE customer
ADD image_id VARCHAR(36);

ALTER TABLE customer
ADD CONSTRAINT customer_image_id_unqiue
UNIQUE(image_id);