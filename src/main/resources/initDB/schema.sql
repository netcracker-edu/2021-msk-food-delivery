CREATE EXTENSION IF NOT EXISTS postgis;

CREATE TABLE IF NOT EXISTS warehouses(warehouse_id BIGSERIAL PRIMARY KEY,
                                      geo geometry(POINT) NOT NULL,
    address VARCHAR(50) NOT NULL,
    name VARCHAR(50) NOT NULL,
    delivery_zone geometry NOT NULL,
    is_deactivated BOOLEAN NOT NULL DEFAULT false);

CREATE TABLE IF NOT EXISTS couriers(courier_id BIGINT PRIMARY KEY,
                                    warehouse_id BIGINT,
                                    phone_number VARCHAR(20) UNIQUE NOT NULL CHECK(phone_number SIMILAR TO '\+7 \(9\d\d\) \d\d\d-\d\d-\d\d'),
    payment_data JSON,
    address VARCHAR(50) NOT NULL,
    current_balance FLOAT4 NOT NULL CHECK(current_balance >= 0),
    rating NUMERIC(3, 2) CHECK((rating >= 0 AND rating <= 5) OR rating IS NULL),
    FOREIGN KEY(warehouse_id) REFERENCES warehouses(warehouse_id) ON DELETE SET NULL);
    FOREIGN KEY(courier_id) REFERENCES users(user_id) ON DELETE CASCADE);

DO '
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = ''order_status'') THEN
        CREATE TYPE order_status AS ENUM
        (
            ''created'', ''courier_appointed'', ''packing'', ''delivering'', ''delivered'', ''cancelled''
        );
    END IF;
END' language plpgsql;

CREATE TABLE IF NOT EXISTS orders(order_id BIGSERIAL PRIMARY KEY,
                    client_id BIGINT NOT NULL,
                    address VARCHAR(100) NOT NULL,
                    coordinates geometry NOT NULL,
                    warehouse_id BIGINT ON DELETE CASCADE,
                    courier_id BIGINT, 
                    status order_status NOT NULL DEFAULT 'created',
                    date_start TIMESTAMP NOT NULL,
                    date_end TIMESTAMP,
                    overall_cost NUMERIC(7, 2) NOT NULL CHECK(overall_cost > 0), 
                    high_demand_coeff NUMERIC(3, 2) NOT NULL DEFAULT 1 CHECK(high_demand_coeff >= 1 AND high_demand_coeff < 3),
                    discount NUMERIC(7, 2) NOT NULL DEFAULT 0 CHECK(discount >= 0),
                    promo_code_id BIGINT ON DELETE CASCADE, 
                    client_rating NUMERIC(3, 2) CHECK((client_rating IS NULL) OR (client_rating BETWEEN 0 AND 5)),
                    delivery_rating NUMERIC(3, 2) CHECK((delivery_rating IS NULL) OR (delivery_rating BETWEEN 0 AND 5)),
                    FOREIGN KEY(warehouse_id) REFERENCES warehouses(warehouse_id) ON DELETE CASCADE,
                    FOREIGN KEY(client_id) REFERENCES clients(client_id) ON DELETE CASCADE,
                    FOREIGN KEY(courier_id) REFERENCES couriers(courier_id) ON DELETE SET NULL);
                    FOREIGN KEY(promo_code_id) REFERENCES promo_codes(promo_code_id) ON DELETE CASCADE);

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

CREATE TRIGGER warehouse_update_is_deactivated
    AFTER UPDATE OF is_deactivated
    ON warehouses
    FOR EACH ROW
    EXECUTE FUNCTION warehouse_update_is_deactivated();

    
DROP FUNCTION IF EXISTS order_update_status() CASCADE;
CREATE OR REPLACE FUNCTION order_update_status() RETURNS TRIGGER
AS '
BEGIN
        IF NEW.status = ''cancelled'' THEN
            DECLARE pos BIGINT;
            BEGIN
                FOR pos IN (SELECT product_position_id FROM (SELECT * FROM orders INNER JOIN orders_product_positions USING(order_id) WHERE order_id = NEW.order_id) positions) LOOP
                UPDATE product_positions orders_product_positions SET current_amount = current_amount + (SELECT amount FROM orders INNER JOIN orders_product_positions USING(order_id) WHERE order_id = NEW.order_id AND product_position_id = pos) WHERE product_position_id = pos; 
                END LOOP;
            END;
        ELSIF NEW.status = ''delivered'' THEN
            UPDATE orders SET date_end = CURRENT_TIMESTAMP(0) WHERE order_id = NEW.order_id;
        END IF;
    RETURN NEW;
END;
' LANGUAGE plpgsql;

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
    FOREIGN KEY(warehouse_id) REFERENCES warehouses (warehouse_id) ON DELETE CASCADE);



CREATE TABLE IF NOT EXISTS delivery_sessions(delivery_session_id BIGSERIAL PRIMARY KEY,
                              courier_id BIGINT NOT NULL,
                              start_time TIMESTAMP NOT NULL,
                              end_time TIMESTAMP,
                              orders_completed INT4 DEFAULT 0 CHECK(orders_completed >= 0),
                              average_time_per_order INTERVAL,
                              money_earned NUMERIC(7, 2) DEFAULT 0 CHECK(money_earned >= 0),
                              FOREIGN KEY(courier_id) REFERENCES couriers(courier_id) ON DELETE CASCADE);

CREATE TABLE IF NOT EXISTS orders_product_positions(order_product_position_id BIGSERIAL PRIMARY KEY,
                                    order_id BIGINT NOT NULL,
                                    product_position_id BIGINT NOT NULL,
                                    amount INT4 NOT NULL CHECK(amount > 0),
                                    price NUMERIC(7, 2) NOT NULL CHECK(price > 0),
                                    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
                                    FOREIGN KEY (product_position_id) REFERENCES product_positions(product_position_id) ON DELETE CASCADE);
