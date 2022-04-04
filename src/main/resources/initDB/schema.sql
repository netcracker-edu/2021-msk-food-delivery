CREATE EXTENSION IF NOT EXISTS postgis;
CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE IF NOT EXISTS warehouses(warehouse_id BIGSERIAL PRIMARY KEY,
                                      geo geometry(POINT) NOT NULL,
                                      address VARCHAR(50) NOT NULL,
                                      name VARCHAR(50) NOT NULL,
                                      delivery_zone geometry NOT NULL,
                                      is_deactivated BOOLEAN NOT NULL DEFAULT false);



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

CREATE TABLE IF NOT EXISTS users_search (
	user_search_id BIGINT PRIMARY KEY REFERENCES users (user_id) ON DELETE CASCADE,
	search_vector tsvector
);

/*
    INSERT TO  USERS_SEARCH if NEW USER ADDED
*/
CREATE OR REPLACE FUNCTION copy_users_to_search() RETURNS TRIGGER AS '
BEGIN
    INSERT INTO users_search
        VALUES(new.user_id,
			   setweight(to_tsvector(''russian'', new.email), ''A'') ||
			   setweight(to_tsvector(''russian'', new.full_name), ''B'') ||
			   setweight(to_tsvector(''russian'', new.role::text), ''C''));
    RETURN new;
END;
' language plpgsql;

DROP TRIGGER IF EXISTS copy_users_to_search ON users;

CREATE TRIGGER copy_users_to_search
	 AFTER INSERT ON users
     FOR EACH ROW
     EXECUTE PROCEDURE copy_users_to_search();

/*
    UPDATE USERS_SEARCH if USER UPDATED
*/
CREATE OR REPLACE FUNCTION update_users_in_search() RETURNS TRIGGER AS '
BEGIN
    UPDATE users_search
        SET search_vector =
			   setweight(to_tsvector(''russian'', new.email), ''A'') ||
			   setweight(to_tsvector(''russian'', new.full_name), ''B'') ||
			   setweight(to_tsvector(''russian'', new.role::text), ''C'')
		WHERE user_search_id = new.user_id;
    RETURN new;
END;
'
language plpgsql;

DROP TRIGGER IF EXISTS update_users_in_search ON users;

CREATE TRIGGER update_users_in_search
	 AFTER UPDATE ON users
     FOR EACH ROW
     EXECUTE PROCEDURE update_users_in_search();

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

CREATE TABLE IF NOT EXISTS user_refresh_tokens
(
	token_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	create_date TIMESTAMP NOT NULL,
	user_agent TEXT,
	owner_id BIGINT REFERENCES users (user_id) ON DELETE CASCADE
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

CREATE TABLE IF NOT EXISTS products_search (
	product_search_id BIGINT PRIMARY KEY REFERENCES products (product_id) ON DELETE CASCADE,
	search_vector tsvector
);

/*
    INSERT TO PRODUCTS_SEARCH if NEW PRODUCT ADDED
*/
CREATE OR REPLACE FUNCTION copy_products_to_search() RETURNS TRIGGER AS '
BEGIN
    INSERT INTO products_search
        VALUES(new.product_id,
			   setweight(to_tsvector(''russian'', new.name), ''A'') ||
			   setweight(to_tsvector(''russian'', new.description), ''B'') ||
			   setweight(to_tsvector(''russian'', new.composition), ''C''));
    RETURN new;
END;
' language plpgsql;

DROP TRIGGER IF EXISTS copy_products_to_search ON products;

CREATE TRIGGER copy_products_to_search
	 AFTER INSERT ON products
     FOR EACH ROW
     EXECUTE PROCEDURE copy_products_to_search();

/*
    UPDATE PRODUCTS_SEARCH if PRODUCTS UPDATED
*/
CREATE OR REPLACE FUNCTION update_products_in_search() RETURNS TRIGGER AS '
BEGIN
    UPDATE products_search
        SET search_vector =
			   setweight(to_tsvector(''russian'', new.name), ''A'') ||
			   setweight(to_tsvector(''russian'', new.description), ''B'') ||
			   setweight(to_tsvector(''russian'', new.composition), ''C'')
		WHERE product_search_id = new.product_id;
    RETURN new;
END;
'
language plpgsql;

DROP TRIGGER IF EXISTS update_products_in_search ON products;

CREATE TRIGGER update_products_in_search
	 AFTER UPDATE ON products
     FOR EACH ROW
     EXECUTE PROCEDURE update_products_in_search();

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

CREATE TABLE IF NOT EXISTS couriers(courier_id BIGINT PRIMARY KEY,
                                    warehouse_id BIGINT,
                                    phone_number VARCHAR(20) UNIQUE NOT NULL CHECK(phone_number SIMILAR TO '\+7 \(9\d\d\) \d\d\d-\d\d-\d\d'),
                                    payment_data JSON,
                                    address VARCHAR(50) NOT NULL,
                                    current_balance FLOAT4 NOT NULL CHECK(current_balance >= 0),
                                    rating NUMERIC(3, 2) CHECK((rating >= 0 AND rating <= 5) OR rating IS NULL),
                                    FOREIGN KEY(warehouse_id) REFERENCES warehouses(warehouse_id) ON DELETE SET NULL,
                                    FOREIGN KEY(courier_id) REFERENCES users(user_id) ON DELETE CASCADE);

DO '
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = ''order_status'') THEN
        CREATE TYPE order_status AS ENUM
            (
                ''CREATED'', ''COURIER_APPOINTED'', ''PACKING'', ''DELIVERING'', ''DELIVERED'', ''CANCELLED''
                );
    END IF;
END' language plpgsql;

CREATE TABLE IF NOT EXISTS orders(order_id BIGSERIAL PRIMARY KEY,
                                  client_id BIGINT NOT NULL,
                                  address VARCHAR(100) NOT NULL,
                                  coordinates geometry NOT NULL,
                                  warehouse_id BIGINT,
                                  courier_id BIGINT,
                                  status order_status NOT NULL DEFAULT 'CREATED',
                                  date_start TIMESTAMP NOT NULL,
                                  date_end TIMESTAMP,
                                  overall_cost NUMERIC(7, 2) NOT NULL CHECK(overall_cost > 0),
                                  high_demand_coeff NUMERIC(3, 2) NOT NULL DEFAULT 1 CHECK(high_demand_coeff >= 1 AND high_demand_coeff < 3),
                                  discount NUMERIC(7, 2) NOT NULL DEFAULT 0 CHECK(discount >= 0),
                                  promo_code_id BIGINT,
                                  client_rating NUMERIC(3, 2) CHECK((client_rating IS NULL) OR (client_rating BETWEEN 0 AND 5)),
                                  delivery_rating NUMERIC(3, 2) CHECK((delivery_rating IS NULL) OR (delivery_rating BETWEEN 0 AND 5)),
                                  FOREIGN KEY(warehouse_id) REFERENCES warehouses(warehouse_id) ON DELETE CASCADE,
                                  FOREIGN KEY(client_id) REFERENCES clients(client_id) ON DELETE CASCADE,
                                  FOREIGN KEY(courier_id) REFERENCES couriers(courier_id) ON DELETE SET NULL,
                                  FOREIGN KEY(promo_code_id) REFERENCES promo_codes(promo_code_id) ON DELETE CASCADE);

CREATE TABLE IF NOT EXISTS delivery_sessions(delivery_session_id BIGSERIAL PRIMARY KEY,
                                             courier_id BIGINT NOT NULL,
                                             start_time TIMESTAMP NOT NULL,
                                             end_time TIMESTAMP,
                                             orders_completed INT4 DEFAULT 0 CHECK(orders_completed >= 0),
                                             average_time_per_order INTERVAL,
                                             money_earned NUMERIC(7, 2) DEFAULT 0 CHECK(money_earned >= 0),
                                             FOREIGN KEY(courier_id) REFERENCES couriers(courier_id) ON DELETE CASCADE);


DROP FUNCTION IF EXISTS warehouse_update_is_deactivated() CASCADE;
CREATE OR REPLACE FUNCTION warehouse_update_is_deactivated() RETURNS TRIGGER
AS '
    BEGIN
        IF NEW.is_deactivated = true THEN
            UPDATE couriers SET warehouse_id = NULL WHERE warehouse_id = NEW.warehouse_id;
            UPDATE moderators SET warehouse_id = NULL WHERE warehouse_id = NEW.warehouse_id;
            UPDATE orders SET warehouse_id = NULL WHERE warehouse_id = NEW.warehouse_id;
        END IF;
        RETURN NEW;
    END;
' LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS  warehouse_update_is_deactivated ON warehouses;

CREATE TRIGGER warehouse_update_is_deactivated
    AFTER UPDATE OF is_deactivated
    ON warehouses
    FOR EACH ROW
EXECUTE FUNCTION warehouse_update_is_deactivated();


DROP FUNCTION IF EXISTS order_update_status() CASCADE;
CREATE OR REPLACE FUNCTION order_update_status() RETURNS TRIGGER
AS '
    BEGIN
        IF NEW.status = ''CANCELLED'' THEN
            DECLARE pos BIGINT;
            BEGIN
                FOR pos IN (SELECT product_position_id FROM (SELECT * FROM orders INNER JOIN orders_product_positions USING(order_id) WHERE order_id = NEW.order_id) positions) LOOP
                        UPDATE product_positions orders_product_positions SET current_amount = current_amount + (SELECT amount FROM orders INNER JOIN orders_product_positions USING(order_id) WHERE order_id = NEW.order_id AND product_position_id = pos) WHERE product_position_id = pos;
                    END LOOP;
            END;
            UPDATE orders SET date_end = CURRENT_TIMESTAMP(0) WHERE order_id = NEW.order_id;
        ELSIF NEW.status = ''DELIVERED'' AND OLD.status = ''DELIVERING'' THEN
            UPDATE orders SET date_end = CURRENT_TIMESTAMP WHERE order_id = NEW.order_id;
            UPDATE delivery_sessions SET orders_completed = coalesce(orders_completed, 0) + 1, money_earned = coalesce(money_earned, 0) + ROUND
            (NEW.overall_cost, 1) / 10, average_time_per_order = ((CURRENT_TIMESTAMP - start_time) /
            (coalesce(orders_completed, 0) + 1))::INTERVAL WHERE courier_id = NEW.courier_id AND end_time IS NULL;
        END IF;
        RETURN NEW;
    END;
' LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS order_update_status ON orders;

CREATE TRIGGER order_update_status
    AFTER UPDATE OF status
    ON orders
    FOR EACH ROW
EXECUTE FUNCTION order_update_status();


CREATE TABLE IF NOT EXISTS product_positions(product_position_id BIGSERIAL PRIMARY KEY,
                                             product_id BIGINT NOT NULL,
                                             warehouse_id BIGINT NOT NULL,
                                             warehouse_section VARCHAR(10) NOT NULL,
                                             supply_amount INT4 NOT NULL CHECK(supply_amount > 0),
                                             current_amount INT4 NOT NULL CHECK(current_amount >= 0 AND current_amount <= supply_amount),
                                             supply_date DATE NOT NULL,
                                             supplier_invoice NUMERIC(12, 2) NOT NULL CHECK(supplier_invoice > 0),
                                             supplier_name VARCHAR(30) NOT NULL,
                                             is_invoice_paid BOOLEAN NOT NULL DEFAULT false,
                                             manufacture_date DATE NOT NULL,
                                             FOREIGN KEY(product_id) REFERENCES products (product_id) ON DELETE CASCADE,
                                             FOREIGN KEY(warehouse_id) REFERENCES warehouses (warehouse_id) ON DELETE CASCADE);

CREATE TABLE IF NOT EXISTS orders_product_positions(order_product_position_id BIGSERIAL PRIMARY KEY,
                                                    order_id BIGINT NOT NULL,
                                                    product_position_id BIGINT NOT NULL,
                                                    amount INT4 NOT NULL CHECK(amount > 0),
                                                    price NUMERIC(7, 2) NOT NULL CHECK(price > 0),
                                                    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
                                                    FOREIGN KEY (product_position_id) REFERENCES product_positions(product_position_id) ON DELETE CASCADE);

CREATE TABLE IF NOT EXISTS files
(
	file_id UUID PRIMARY KEY,
	owner_id BIGINT REFERENCES users (user_id) ON DELETE SET NULL,
	type FILE_TYPE NOT NULL,
	name VARCHAR(100) NOT NULL,
	size BIGINT NOT NULL CHECK (size > 0),
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

CREATE OR REPLACE PROCEDURE update_courier_rating()
AS '
BEGIN
UPDATE couriers c SET rating = (
	SELECT ROUND(AVG(orders.delivery_rating), 2)
	FROM orders
	WHERE orders.courier_id = c.courier_id AND ((current_timestamp - orders.date_end)::interval < interval ''1 month''))
WHERE courier_id IN (SELECT user_id FROM users WHERE lock_date IS NULL);
END;
' LANGUAGE plpgsql ;

CREATE OR REPLACE PROCEDURE update_client_rating()
AS '
BEGIN
UPDATE clients c SET rating = (
	SELECT ROUND(AVG(orders.client_rating), 2)
	FROM orders
	WHERE orders.client_id = c.client_id AND ((current_timestamp - orders.date_end)::interval < interval ''1 month''))
WHERE client_id IN (SELECT user_id FROM users WHERE lock_date IS NULL);
END;
' LANGUAGE plpgsql ;

DROP FUNCTION IF EXISTS update_orders_client_rating() CASCADE;
CREATE OR REPLACE FUNCTION update_orders_client_rating() RETURNS TRIGGER
AS '
    BEGIN
        UPDATE clients c SET rating = (
			SELECT ROUND(AVG(orders.client_rating), 2)
			FROM orders
			WHERE orders.client_id = NEW.client_id AND ((current_timestamp - orders.date_end)::interval < interval ''1 month''))
		WHERE client_id = NEW.client_id;
		RETURN NEW;
    END;
' LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS update_orders_client_rating ON orders;

CREATE TRIGGER update_orders_client_rating
    AFTER UPDATE OF client_rating
    ON orders
    FOR EACH ROW
EXECUTE FUNCTION update_orders_client_rating();

DROP FUNCTION IF EXISTS update_orders_courier_rating() CASCADE;
CREATE OR REPLACE FUNCTION update_orders_courier_rating() RETURNS TRIGGER
AS '
    BEGIN
        UPDATE couriers c SET rating = (
			SELECT ROUND(AVG(orders.delivery_rating), 2)
			FROM orders
			WHERE orders.courier_id = NEW.courier_id AND ((current_timestamp - orders.date_end)::interval < interval ''1 month''))
		WHERE courier_id = NEW.courier_id;
        RETURN NEW;
    END;
' LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS update_orders_courier_rating ON orders;

CREATE TRIGGER update_orders_courier_rating
    AFTER UPDATE OF delivery_rating
    ON orders
    FOR EACH ROW
EXECUTE FUNCTION update_orders_courier_rating();

DROP FUNCTION IF EXISTS delivery_session_update_end_time() CASCADE;
CREATE OR REPLACE FUNCTION delivery_session_update_end_time() RETURNS TRIGGER
AS '
    BEGIN
        IF NEW.orders_completed > 0 THEN
            UPDATE couriers SET current_balance = ROUND(current_balance::NUMERIC + NEW.money_earned, 2) WHERE courier_id =
            NEW
            .courier_id;
            UPDATE delivery_sessions SET average_time_per_order = ((CURRENT_TIMESTAMP - NEW.start_time) / NEW
            .orders_completed)::INTERVAL WHERE delivery_session_id = NEW.delivery_session_id;
        END IF;
		RETURN NEW;
    END;
' LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS delivery_session_update_end_time ON delivery_sessions;

CREATE TRIGGER delivery_session_update_end_time
    AFTER UPDATE OF end_time
    ON delivery_sessions
    FOR EACH ROW
EXECUTE FUNCTION delivery_session_update_end_time();