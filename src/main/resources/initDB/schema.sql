CREATE TABLE IF NOT EXISTS warehouses
(
	warehouse_id BIGSERIAL PRIMARY KEY,
	geo POINT NOT NULL,
	address VARCHAR(50) NOT NULL,
	name VARCHAR(50) NOT NULL UNIQUE,
	delivery_zone CIRCLE NOT NULL
);

DO ' DECLARE
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = ''role_enum'') THEN
        CREATE TYPE role_enum AS ENUM
        (
           ''CLIENT'', ''ADMIN'', ''MODERATOR'', ''COURIER''
        );
    END IF;
END;
' LANGUAGE PLPGSQL;

CREATE TABLE IF NOT EXISTS users
(
	user_id BIGSERIAL PRIMARY KEY,
	role ROLE_ENUM NOT NULL,
	password TEXT NOT NULL,
	full_name VARCHAR(50) NOT NULL CHECK(LENGTH(full_name) > 5),
	email VARCHAR(50) NOT NULL UNIQUE CHECK (email != ''),
	reg_date TIMESTAMP NOT NULL,
	last_signin_date TIMESTAMP,
	avatar_id UUID,
	lock_date TIMESTAMP
);

CREATE TABLE IF NOT EXISTS clients
(
	client_id BIGINT PRIMARY KEY REFERENCES users (user_id) ON DELETE CASCADE,
	payment_data JSON,
	phone_number VARCHAR(20) NOT NULL UNIQUE CHECK (phone_number SIMILAR TO '\+7 \(9\d\d\) \d\d\d-\d\d-\d\d'),
	rating NUMERIC(3,2) CHECK ( (rating >= 0.00 AND rating <= 5.00) OR (rating is NULL) )
);

CREATE TABLE IF NOT EXISTS moderators
(
	moderator_id BIGINT PRIMARY KEY REFERENCES users (user_id) ON DELETE CASCADE,
	warehouse_id BIGINT REFERENCES warehouses (warehouse_id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS products
(
	product_id BIGSERIAL PRIMARY KEY,
	name VARCHAR(50) NOT NULL,
	description TEXT,
	picture_id UUID,
	weight INT NOT NULL,
	composition VARCHAR(250),
	expiration_days SMALLINT,
	in_showcase BOOLEAN NOT NULL DEFAULT FALSE,
	price NUMERIC(7,2) NOT NULL CHECK(price > 0),
	discount NUMERIC(7,2) NOT NULL DEFAULT 0 CHECK(discount >= 0)
);


CREATE TABLE IF NOT EXISTS promo_codes
(
	promo_code_id BIGSERIAL PRIMARY KEY,
	created_by_id BIGINT REFERENCES users (user_id) ON DELETE SET NULL,
	creation_date TIMESTAMP NOT NULL,
	description TEXT,
	activation_phrase VARCHAR(50) NOT NULL UNIQUE,
	is_active BOOLEAN DEFAULT TRUE,
	date_start TIMESTAMP,
	date_end TIMESTAMP CHECK(date_end > date_start),
	limit_amount INT CHECK(limit_amount>0 OR limit_amount IS NULL),
	current_amount INT NOT NULL DEFAULT 0 CHECK(current_amount >= 0),
	discount_value NUMERIC(7,2) CHECK(discount_value >= 0 OR discount_value IS NULL),
	discount_percent FLOAT4 CHECK ( (discount_percent > 0 AND discount_percent < 100) OR discount_percent IS NULL),
	user_id BIGINT REFERENCES users (user_id) ON DELETE CASCADE,
	is_first_order BOOLEAN DEFAULT FALSE

	CONSTRAINT value_or_percent_discount_notnull CHECK (
	(discount_value IS NOT NULL AND discount_value > 0)
	OR
	(discount_percent IS NOT NULL) )
);

CREATE OR REPLACE FUNCTION check_promo_amount_limit() RETURNS trigger AS '
	BEGIN
		IF (NEW.limit_amount IS NULL) THEN
			RETURN NEW;
		ELSEIF (NEW.current_amount >= NEW.limit_amount) THEN
			NEW.is_active := FALSE;
			RETURN NEW;
		END IF;
		RETURN NEW;
	END;
' LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS check_promo_amount_limit ON promo_codes;

CREATE TRIGGER check_promo_amount_limit
	BEFORE UPDATE ON promo_codes
	FOR EACH ROW
	EXECUTE FUNCTION check_promo_amount_limit();

DO ' DECLARE
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = ''file_type'') THEN
        CREATE TYPE file_type as ENUM (''PNG'', ''JPEG'');
    END IF;
END;
' LANGUAGE PLPGSQL;

CREATE TABLE IF NOT EXISTS files
(
	file_id UUID PRIMARY KEY,
	owner_id BIGINT REFERENCES users (user_id) ON DELETE SET NULL,
	type FILE_TYPE NOT NULL,
	size FLOAT4 NOT NULL CHECK (size > 0),
	upload_date TIMESTAMP NOT NULL

);

/* Добавляем FK files на таблицы users, products, потому что до этого таблицы files еще не существовало.
*/
DO ' DECLARE
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_catalog.pg_constraint WHERE conname = ''fk_users_files'') THEN
        ALTER TABLE users
        	ADD CONSTRAINT fk_users_files FOREIGN KEY (avatar_id) REFERENCES files (file_id) ON DELETE SET NULL;
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_catalog.pg_constraint WHERE conname = ''fk_products_files'') THEN
            ALTER TABLE products
            	ADD CONSTRAINT fk_products_files FOREIGN KEY (picture_id) REFERENCES files (file_id) ON DELETE SET NULL;
    END IF;
END;
' LANGUAGE PLPGSQL;

CREATE TABLE IF NOT EXISTS chats
(
	chat_id BIGSERIAL PRIMARY KEY,
	user_id BIGINT NOT NULL REFERENCES users (user_id) ON DELETE CASCADE,
	create_date TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS messages
(
	message_id BIGSERIAL PRIMARY KEY,
	replier_id BIGINT REFERENCES moderators (moderator_id),
	chat_id BIGINT NOT NULL REFERENCES chats (chat_id) ON DELETE CASCADE,
	send_date TIMESTAMP NOT NULL,
	message_text VARCHAR(200),
	picture_id UUID,
	has_been_read BOOLEAN NOT NULL DEFAULT FALSE,

	CONSTRAINT text_or_picture_notnull
	CHECK (
		(message_text IS NOT NULL AND message_text != '')
		OR
		(picture_id IS NOT NULL)
	)
);