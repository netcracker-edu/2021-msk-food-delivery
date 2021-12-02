DO ' DECLARE
BEGIN

IF NOT EXISTS (SELECT 1 FROM warehouses WHERE warehouse_id = 1) THEN
INSERT INTO warehouses(geo, address, name, delivery_zone)
VALUES
    (ST_SetSRID(ST_MakePoint(55.765396, 37.633737), 4326), ''Бобров переулок, 4'', ''Центр'', ST_Buffer(ST_SetSRID(ST_MakePoint(55.765396, 37.633737)::geography, 4326), 7000.0)::geometry),
    (ST_SetSRID(ST_MakePoint(55.661976, 37.630066), 4326), ''Каширское шоссе, 16'', ''Юг'', ST_Buffer(ST_SetSRID(ST_MakePoint(55.648725, 37.645632)::geography, 4326), 13500.0)::geometry),
    (ST_SetSRID(ST_MakePoint(55.751664, 37.414714), 4326), ''Рублёвское шоссе, 42'', ''Запад'', ST_Buffer(ST_SetSRID(ST_MakePoint(55.752424, 37.489551)::geography, 4236), 11000.0)::geometry),
    (ST_SetSRID(ST_MakePoint(55.848651, 37.617646), 4326), ''Берёзовая аллея, 12'', ''Север'', ST_Buffer(ST_SetSRID(ST_MakePoint(55.831202, 37.602237)::geography, 4236), 14000.0)::geometry),
    (ST_SetSRID(ST_MakePoint(55.754514, 37.738260), 4326), ''Шоссе Энтузиастов, 38'', ''Восток'', ST_Buffer(ST_SetSRID(ST_MakePoint(55.754683, 37.738931)::geography, 4236), 12500.0)::geometry);
END IF;
END;
' language plpgsql;

DO ' DECLARE
BEGIN
IF NOT EXISTS (SELECT 1 FROM couriers WHERE courier_id = 52) THEN
INSERT INTO couriers(courier_id, warehouse_id, phone_number, address, current_balance)
    VALUES

        (52, 1, ''+7 (977) 777-77-77'', ''Кадашёвская набережная, 30'', 2000),
        (53, 1, ''+7 (977) 777-77-78'', ''Кадашёвская набережная, 30'', 0),
        (54, 1, ''+7 (977) 777-77-79'', ''Кадашёвская набережная, 24'', 320),
        (55, 1, ''+7 (977) 777-77-80'', ''Кадашёвская набережная, 32'', 1242.2),

        (56, 2, ''+7 (977) 777-77-81'', ''Бакинская улица, 4'', 0),
        (57, 2, ''+7 (977) 777-77-82'', ''Бакинская улица, 6'', 4201.23),
        (58, 2, ''+7 (977) 777-77-83'', ''Бакинская улица, 2'', 121.47),
        (59, 2, ''+7 (977) 777-77-84'', ''Бакинская улица, 8'', 0),


        (60, 3, ''+7 (977) 777-77-85'', ''Молодогвардейская улица, 4'', 3821),
        (61, 3, ''+7 (977) 777-77-86'', ''Молодогвардейская улица, 6'', 843.1),
        (62, 3, ''+7 (977) 777-77-87'', ''Молодогвардейская улица, 8'', 451),
        (63, 3, ''+7 (977) 777-77-88'', ''Молодогвардейская улица, 2'', 901.98),


        (64, 4, ''+7 (977) 777-77-89'', ''Отрадный проезд, 10'', 3821),
        (65, 4, ''+7 (977) 777-77-90'', ''Отрадный проезд, 14'', 843.1),
        (66, 4, ''+7 (977) 777-77-91'', ''Отрадный проезд, 12'', 451),
        (67, 4, ''+7 (977) 777-77-92'', ''Отрадный проезд, 9'', 901.98),

        (68, 5, ''+7 (977) 777-77-93'', ''Братская улица, 19к1'', 141.32),
        (69, 5, ''+7 (977) 777-77-94'', ''Братская улица, 19к2'', 890),
        (70, 5, ''+7 (977) 777-77-95'', ''Братская улица, 19к3'', 5345.9),
        (71, 5, ''+7 (977) 777-77-96'', ''Братская улица, 15к1'', 735);

END IF;
END;
' language plpgsql;

DO ' DECLARE
BEGIN
IF NOT EXISTS (SELECT 1 FROM product_positions WHERE product_position_id = 1) THEN
INSERT INTO product_positions(product_id, warehouse_id, warehouse_section, supply_amount, current_amount, supply_date, supplier_invoice, supplier_name, is_invoice_paid, manufacture_date)
    VALUES
        (1, 1, ''Ц-1-100-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 8435, ''ЭкоНива'', false, date (CURRENT_DATE) - integer ''3''),  -- 120.5 * 0.7 = 84,35р
        (1, 2, ''Ю-1-100-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 8435, ''ЭкоНива'', true, date (CURRENT_DATE) - integer ''3''),
        (1, 3, ''З-1-100-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 8435, ''ЭкоНива'', true, date (CURRENT_DATE) - integer ''3''),
        (1, 4, ''С-1-100-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 8435, ''ЭкоНива'', true, date (CURRENT_DATE) - integer ''3''),
        (1, 5, ''В-1-100-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 8435, ''ЭкоНива'', true, date (CURRENT_DATE) - integer ''3''),

        (2, 1, ''Ц-1-101-Л'', 90, 45, date (CURRENT_DATE) - integer ''3'', 5040, ''ЭкоНива'', false, date (CURRENT_DATE) - integer ''3''),   -- 80 * 0.7 = 56р
        (2, 2, ''Ю-1-101-Л'', 90, 45, date (CURRENT_DATE) - integer ''3'', 5040, ''ЭкоНива'', true, date (CURRENT_DATE) - integer ''3''),
        (2, 3, ''З-1-101-Л'', 90, 45, date (CURRENT_DATE) - integer ''3'', 5040, ''ЭкоНива'', true, date (CURRENT_DATE) - integer ''3''),
        (2, 4, ''С-1-101-Л'', 90, 45, date (CURRENT_DATE) - integer ''3'', 5040, ''ЭкоНива'', true, date (CURRENT_DATE) - integer ''3''),
        (2, 5, ''В-1-101-Л'', 90, 45, date (CURRENT_DATE) - integer ''3'', 5040, ''ЭкоНива'', true, date (CURRENT_DATE) - integer ''3''),

        (3, 1, ''Ц-1-102-Л'', 80, 40, date (CURRENT_DATE) - integer ''3'', 6160, ''Nemoloko'', true, date (CURRENT_DATE) - integer ''3''),  -- 110 * 0.7 = 77
        (3, 2, ''Ю-1-102-Л'', 80, 40, date (CURRENT_DATE) - integer ''3'', 6160, ''Nemoloko'', true, date (CURRENT_DATE) - integer ''3''),
        (3, 3, ''З-1-102-Л'', 80, 40, date (CURRENT_DATE) - integer ''3'', 6160, ''Nemoloko'', true, date (CURRENT_DATE) - integer ''3''),
        (3, 4, ''С-1-102-Л'', 80, 40, date (CURRENT_DATE) - integer ''3'', 6160, ''Nemoloko'', true, date (CURRENT_DATE) - integer ''3''),
        (3, 5, ''В-1-102-Л'', 80, 40, date (CURRENT_DATE) - integer ''3'', 6160, ''Nemoloko'', true, date (CURRENT_DATE) - integer ''3''),

        (4, 1, ''Ц-1-103-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 10185, ''ЭкоНива'', true, date (CURRENT_DATE) - integer ''3''), -- 145.5 * 0.7 = 101.85
        (4, 2, ''Ю-1-103-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 10185, ''ЭкоНива'', true, date (CURRENT_DATE) - integer ''3''),
        (4, 3, ''З-1-103-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 10185, ''ЭкоНива'', true, date (CURRENT_DATE) - integer ''3''),
        (4, 4, ''С-1-103-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 10185, ''ЭкоНива'', true, date (CURRENT_DATE) - integer ''3''),
        (4, 5, ''В-1-103-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 10185, ''ЭкоНива'', true, date (CURRENT_DATE) - integer ''3''),

        (5, 1, ''Ц-1-104-Л'', 90, 45, date (CURRENT_DATE) - integer ''3'', 8694, ''Простоквашино'', true, date (CURRENT_DATE) - integer ''3''), -- 138 * 0.7 = 96.6
        (5, 2, ''Ю-1-104-Л'', 90, 45, date (CURRENT_DATE) - integer ''3'', 8694, ''Простоквашино'', true, date (CURRENT_DATE) - integer ''3''),
        (5, 3, ''З-1-104-Л'', 90, 45, date (CURRENT_DATE) - integer ''3'', 8694, ''Простоквашино'', true, date (CURRENT_DATE) - integer ''3''),
        (5, 4, ''С-1-104-Л'', 90, 45, date (CURRENT_DATE) - integer ''3'', 8694, ''Простоквашино'', true, date (CURRENT_DATE) - integer ''3''),
        (5, 5, ''В-1-104-Л'', 90, 45, date (CURRENT_DATE) - integer ''3'', 8694, ''Простоквашино'', true, date (CURRENT_DATE) - integer ''3''),

        (6, 1, ''Ц-1-105-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 5850, ''Простоквашино'', true, date (CURRENT_DATE) - integer ''3''),    -- 83.9 * 0.7 = 58.5
        (6, 2, ''Ю-1-105-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 5850, ''Простоквашино'', true, date (CURRENT_DATE) - integer ''3''),
        (6, 3, ''З-1-105-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 5850, ''Простоквашино'', true, date (CURRENT_DATE) - integer ''3''),
        (6, 4, ''С-1-105-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 5850, ''Простоквашино'', true, date (CURRENT_DATE) - integer ''3''),
        (6, 5, ''В-1-105-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 5850, ''Простоквашино'', true, date (CURRENT_DATE) - integer ''3''),

        (7, 1, ''Ц-1-106-Л'', 90, 45, date (CURRENT_DATE) - integer ''3'', 2385, ''Простоквашино'', true, date (CURRENT_DATE) - integer ''3''), -- 37.9 * 0.7 = 26.5
        (7, 2, ''Ю-1-106-Л'', 90, 45, date (CURRENT_DATE) - integer ''3'', 2385, ''Простоквашино'', true, date (CURRENT_DATE) - integer ''3''),
        (7, 3, ''З-1-106-Л'', 90, 45, date (CURRENT_DATE) - integer ''3'', 2385, ''Простоквашино'', true, date (CURRENT_DATE) - integer ''3''),
        (7, 4, ''С-1-106-Л'', 90, 45, date (CURRENT_DATE) - integer ''3'', 2385, ''Простоквашино'', true, date (CURRENT_DATE) - integer ''3''),
        (7, 5, ''В-1-106-Л'', 90, 45, date (CURRENT_DATE) - integer ''3'', 2385, ''Простоквашино'', true, date (CURRENT_DATE) - integer ''3''),

        (8, 1, ''Ц-1-107-Л'', 80, 40, date (CURRENT_DATE) - integer ''3'', 3040, ''Сарафаново'', true, date (CURRENT_DATE) - integer ''3''),    -- 54.9 * 0.7 = 38
        (8, 2, ''Ю-1-107-Л'', 80, 40, date (CURRENT_DATE) - integer ''3'', 3040, ''Сарафаново'', true, date (CURRENT_DATE) - integer ''3''),
        (8, 3, ''З-1-107-Л'', 80, 40, date (CURRENT_DATE) - integer ''3'', 3040, ''Сарафаново'', true, date (CURRENT_DATE) - integer ''3''),
        (8, 4, ''С-1-107-Л'', 80, 40, date (CURRENT_DATE) - integer ''3'', 3040, ''Сарафаново'', true, date (CURRENT_DATE) - integer ''3''),
        (8, 5, ''В-1-107-Л'', 80, 40, date (CURRENT_DATE) - integer ''3'', 3040, ''Сарафаново'', true, date (CURRENT_DATE) - integer ''3''),


        (9, 1, ''Ц-1-108-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4900, ''Простоквашино'', true, date (CURRENT_DATE) - integer ''3''),    --  69.9* 0.7 = 49
        (9, 2, ''Ю-1-108-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4900, ''Простоквашино'', true, date (CURRENT_DATE) - integer ''3''),
        (9, 3, ''З-1-108-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4900, ''Простоквашино'', true, date (CURRENT_DATE) - integer ''3''),
        (9, 4, ''С-1-108-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4900, ''Простоквашино'', true, date (CURRENT_DATE) - integer ''3''),
        (9, 5, ''В-1-108-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4900, ''Простоквашино'', true, date (CURRENT_DATE) - integer ''3''),


        (10, 1, ''Ц-1-109-Л'', 90, 45, date (CURRENT_DATE) - integer ''3'', 6300, ''Сарафаново'', true, date (CURRENT_DATE) - integer ''3''),   --  99.9* 0.7 =  70
        (10, 2, ''Ю-1-109-Л'', 90, 45, date (CURRENT_DATE) - integer ''3'', 6300, ''Сарафаново'', true, date (CURRENT_DATE) - integer ''3''),
        (10, 3, ''З-1-109-Л'', 90, 45, date (CURRENT_DATE) - integer ''3'', 6300, ''Сарафаново'', true, date (CURRENT_DATE) - integer ''3''),
        (10, 4, ''С-1-109-Л'', 90, 45, date (CURRENT_DATE) - integer ''3'', 6300, ''Сарафаново'', true, date (CURRENT_DATE) - integer ''3''),
        (10, 5, ''В-1-109-Л'', 90, 45, date (CURRENT_DATE) - integer ''3'', 6300, ''Сарафаново'', true, date (CURRENT_DATE) - integer ''3''),

        (11, 1, ''Ц-1-110-Л'', 80, 40, date (CURRENT_DATE) - integer ''3'', 4640, ''Милава'', true, date (CURRENT_DATE) - integer ''3''),   --  82.9* 0.7 = 58
        (11, 2, ''Ю-1-110-Л'', 80, 40, date (CURRENT_DATE) - integer ''3'', 4640, ''Милава'', true, date (CURRENT_DATE) - integer ''3''),
        (11, 3, ''З-1-110-Л'', 80, 40, date (CURRENT_DATE) - integer ''3'', 4640, ''Милава'', true, date (CURRENT_DATE) - integer ''3''),
        (11, 4, ''С-1-110-Л'', 80, 40, date (CURRENT_DATE) - integer ''3'', 4640, ''Милава'', true, date (CURRENT_DATE) - integer ''3''),
        (11, 5, ''В-1-110-Л'', 80, 40, date (CURRENT_DATE) - integer ''3'', 4640, ''Милава'', true, date (CURRENT_DATE) - integer ''3''),

        (12, 1, ''Ц-1-111-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4900, ''Милава'', true, date (CURRENT_DATE) - integer ''3''),  --  70.2* 0.7 =49
        (12, 2, ''Ю-1-111-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4900, ''Милава'', true, date (CURRENT_DATE) - integer ''3''),
        (12, 3, ''З-1-111-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4900, ''Милава'', true, date (CURRENT_DATE) - integer ''3''),
        (12, 4, ''С-1-111-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4900, ''Милава'', true, date (CURRENT_DATE) - integer ''3''),
        (12, 5, ''В-1-111-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4900, ''Милава'', true, date (CURRENT_DATE) - integer ''3''),

        (13, 1, ''Ц-1-112-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 1785, ''Чудское озеро'', true, date (CURRENT_DATE) - integer ''3''),   --  25.5* 0.7 = 17.85
        (13, 2, ''Ю-1-112-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 1785, ''Чудское озеро'', true, date (CURRENT_DATE) - integer ''3''),
        (13, 3, ''З-1-112-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 1785, ''Чудское озеро'', true, date (CURRENT_DATE) - integer ''3''),
        (13, 4, ''С-1-112-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 1785, ''Чудское озеро'', true, date (CURRENT_DATE) - integer ''3''),
        (13, 5, ''В-1-112-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 1785, ''Чудское озеро'', true, date (CURRENT_DATE) - integer ''3''),

        (14, 1, ''Ц-1-113-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 5800,''Чудское озеро'', true, date (CURRENT_DATE) - integer ''3''),    --  83.9* 0.7 = 58
        (14, 2, ''Ю-1-113-Л'', 90, 45, date (CURRENT_DATE) - integer ''3'', 5220, ''Чудское озеро'', true, date (CURRENT_DATE) - integer ''3''),
        (14, 3, ''З-1-113-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 5800,''Чудское озеро'', true, date (CURRENT_DATE) - integer ''3''),
        (14, 4, ''С-1-113-Л'', 90, 45, date (CURRENT_DATE) - integer ''3'', 5220,''Чудское озеро'', true, date (CURRENT_DATE) - integer ''3''),
        (14, 5, ''В-1-113-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 5800,''Чудское озеро'', true, date (CURRENT_DATE) - integer ''3''),

        (15, 1, ''Ц-1-114-Л'', 90, 45, date (CURRENT_DATE) - integer ''3'', 11880, ''Брест-Литовск'', true, date (CURRENT_DATE) - integer ''3''),   --  189* 0.7 =132
        (15, 2, ''Ю-1-114-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 13200, ''Брест-Литовск'', true, date (CURRENT_DATE) - integer ''3''),
        (15, 3, ''З-1-114-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 13200, ''Брест-Литовск'', true, date (CURRENT_DATE) - integer ''3''),
        (15, 4, ''С-1-114-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 13200, ''Брест-Литовск'', true, date (CURRENT_DATE) - integer ''3''),
        (15, 5, ''В-1-114-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 13200, ''Брест-Литовск'', true, date (CURRENT_DATE) - integer ''3''),

        (16, 1, ''Ц-1-115-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 13600, ''Брест-Литовск'', true, date (CURRENT_DATE) - integer ''3''),  --  194* 0.7 =136
        (16, 2, ''Ю-1-115-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 13600, ''Брест-Литовск'', true, date (CURRENT_DATE) - integer ''3''),
        (16, 3, ''З-1-115-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 13600, ''Брест-Литовск'', true, date (CURRENT_DATE) - integer ''3''),
        (16, 4, ''С-1-115-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 13600, ''Брест-Литовск'', true, date (CURRENT_DATE) - integer ''3''),
        (16, 5, ''В-1-115-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 13600, ''Брест-Литовск'', true, date (CURRENT_DATE) - integer ''3''),

        (17, 1, ''Ц-1-116-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 13300, ''Экомилк'', true, date (CURRENT_DATE) - integer ''3''),    --  190* 0.7 = 133
        (17, 2, ''Ю-1-116-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 13300, ''Экомилк'', true, date (CURRENT_DATE) - integer ''3''),
        (17, 3, ''З-1-116-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 13300, ''Экомилк'', true, date (CURRENT_DATE) - integer ''3''),
        (17, 4, ''С-1-116-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 13300, ''Экомилк'', true, date (CURRENT_DATE) - integer ''3''),
        (17, 5, ''В-1-116-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 13300, ''Экомилк'', true, date (CURRENT_DATE) - integer ''3''),

        (18, 1, ''Ц-1-117-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 14200, ''Экомилк'', true, date (CURRENT_DATE) - integer ''3''),    -- 203 * 0.7 = 142
        (18, 2, ''Ю-1-117-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 14200, ''Экомилк'', true, date (CURRENT_DATE) - integer ''3''),
        (18, 3, ''З-1-117-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 14200, ''Экомилк'', true, date (CURRENT_DATE) - integer ''3''),
        (18, 4, ''С-1-117-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 14200, ''Экомилк'', true, date (CURRENT_DATE) - integer ''3''),
        (18, 5, ''В-1-117-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 14200, ''Экомилк'', true, date (CURRENT_DATE) - integer ''3''),

        (19, 1, ''Ц-1-118-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 1950, ''Чудо'', true, date (CURRENT_DATE) - integer ''3''),    --  27.9* 0.7 =19.5
        (19, 2, ''Ю-1-118-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 1950, ''Чудо'', true, date (CURRENT_DATE) - integer ''3''),
        (19, 3, ''З-1-118-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 1950, ''Чудо'', true, date (CURRENT_DATE) - integer ''3''),
        (19, 4, ''С-1-118-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 1950, ''Чудо'', true, date (CURRENT_DATE) - integer ''3''),
        (19, 5, ''В-1-118-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 1950, ''Чудо'', true, date (CURRENT_DATE) - integer ''3''),

        (20, 1, ''Ц-1-119-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 2100, ''Чудо'', true, date (CURRENT_DATE) - integer ''3''),    --  29.9* 0.7 = 21
        (20, 2, ''Ю-1-119-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 2100, ''Чудо'', true, date (CURRENT_DATE) - integer ''3''),
        (20, 3, ''З-1-119-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 2100, ''Чудо'', true, date (CURRENT_DATE) - integer ''3''),
        (20, 4, ''С-1-119-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 2100, ''Чудо'', true, date (CURRENT_DATE) - integer ''3''),
        (20, 5, ''В-1-119-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 2100, ''Чудо'', true, date (CURRENT_DATE) - integer ''3''),

        (21, 1, ''Ц-1-120-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 2890, ''Чудо'', true, date (CURRENT_DATE) - integer ''3''),    --  41.3* 0.7 = 28.9
        (21, 2, ''Ю-1-120-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 2890, ''Чудо'', true, date (CURRENT_DATE) - integer ''3''),
        (21, 3, ''З-1-120-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 2890, ''Чудо'', true, date (CURRENT_DATE) - integer ''3''),
        (21, 4, ''С-1-120-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 2890, ''Чудо'', true, date (CURRENT_DATE) - integer ''3''),
        (21, 5, ''В-1-120-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 2890, ''Чудо'', true, date (CURRENT_DATE) - integer ''3''),

        (22, 1, ''Ц-1-121-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 2890, ''Чудо'', true, date (CURRENT_DATE) - integer ''3''),    --  41.3 * 0.7 =28.9
        (22, 2, ''Ю-1-121-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 2890, ''Чудо'', true, date (CURRENT_DATE) - integer ''3''),
        (22, 3, ''З-1-121-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 2890, ''Чудо'', true, date (CURRENT_DATE) - integer ''3''),
        (22, 4, ''С-1-121-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 2890, ''Чудо'', true, date (CURRENT_DATE) - integer ''3''),
        (22, 5, ''В-1-121-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 2890, ''Чудо'', true, date (CURRENT_DATE) - integer ''3''),

        (23, 1, ''Ц-1-122-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 10700, ''Простоквашино'', true, date (CURRENT_DATE) - integer ''3''),  --  152.9* 0.7 =107
        (23, 2, ''Ю-1-122-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 10700, ''Простоквашино'', true, date (CURRENT_DATE) - integer ''3''),
        (23, 3, ''З-1-122-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 10700, ''Простоквашино'', true, date (CURRENT_DATE) - integer ''3''),
        (23, 4, ''С-1-122-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 10700, ''Простоквашино'', true, date (CURRENT_DATE) - integer ''3''),
        (23, 5, ''В-1-122-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 10700, ''Простоквашино'', true, date (CURRENT_DATE) - integer ''3''),

        (24, 1, ''Ц-1-123-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6100, ''Биобаланс'', true, date (CURRENT_DATE) - integer ''3''),   --  87.5* 0.7 = 61
        (24, 2, ''Ю-1-123-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6100, ''Биобаланс'', true, date (CURRENT_DATE) - integer ''3''),
        (24, 3, ''З-1-123-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6100, ''Биобаланс'', true, date (CURRENT_DATE) - integer ''3''),
        (24, 4, ''С-1-123-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6100, ''Биобаланс'', true, date (CURRENT_DATE) - integer ''3''),
        (24, 5, ''В-1-123-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6100, ''Биобаланс'', true, date (CURRENT_DATE) - integer ''3''),


        (25, 1, ''Ц-1-124-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6100, ''Биобаланс'', true, date (CURRENT_DATE) - integer ''3''),   --  87.5* 0.7 = 61
        (25, 2, ''Ю-1-124-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6100, ''Биобаланс'', true, date (CURRENT_DATE) - integer ''3''),
        (25, 3, ''З-1-124-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6100, ''Биобаланс'', true, date (CURRENT_DATE) - integer ''3''),
        (25, 4, ''С-1-124-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6100, ''Биобаланс'', true, date (CURRENT_DATE) - integer ''3''),
        (25, 5, ''В-1-124-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6100, ''Биобаланс'', true, date (CURRENT_DATE) - integer ''3''),


        (26, 1, ''Ц-1-125-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6100, ''Биобаланс'', true, date (CURRENT_DATE) - integer ''3''),   --  87.5* 0.7 = 61
        (26, 2, ''Ю-1-125-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6100, ''Биобаланс'', true, date (CURRENT_DATE) - integer ''3''),
        (26, 3, ''З-1-125-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6100, ''Биобаланс'', true, date (CURRENT_DATE) - integer ''3''),
        (26, 4, ''С-1-125-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6100, ''Биобаланс'', true, date (CURRENT_DATE) - integer ''3''),
        (26, 5, ''В-1-125-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6100, ''Биобаланс'', true, date (CURRENT_DATE) - integer ''3''),


        (27, 1, ''Ц-1-126-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 2780, ''Покровский хлеб'', true, date (CURRENT_DATE) - integer ''3''), --  39.70* 0.7 =27.8
        (27, 2, ''Ю-1-126-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 2780, ''Покровский хлеб'', true, date (CURRENT_DATE) - integer ''3''),
        (27, 3, ''З-1-126-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 2780, ''Покровский хлеб'', true, date (CURRENT_DATE) - integer ''3''),
        (27, 4, ''С-1-126-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 2780, ''Покровский хлеб'', true, date (CURRENT_DATE) - integer ''3''),
        (27, 5, ''В-1-126-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 2780, ''Покровский хлеб'', true, date (CURRENT_DATE) - integer ''3''),


        (28, 1, ''Ц-1-127-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 2650, ''Хлебный дом'', true, date (CURRENT_DATE) - integer ''3''), --  37.9* 0.7 =  26.5
        (28, 2, ''Ю-1-127-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 2650, ''Хлебный дом'', true, date (CURRENT_DATE) - integer ''3''),
        (28, 3, ''З-1-127-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 2650, ''Хлебный дом'', true, date (CURRENT_DATE) - integer ''3''),
        (28, 4, ''С-1-127-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 2650, ''Хлебный дом'', true, date (CURRENT_DATE) - integer ''3''),
        (28, 5, ''В-1-127-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 2650, ''Хлебный дом'', true, date (CURRENT_DATE) - integer ''3''),


        (29, 1, ''Ц-1-128-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 3633, ''Рижский хлеб'', true, date (CURRENT_DATE) - integer ''3''),    --  51.9* 0.7 = 36.33
        (29, 2, ''Ю-1-128-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 3633, ''Рижский хлеб'', true, date (CURRENT_DATE) - integer ''3''),
        (29, 3, ''З-1-128-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 3633, ''Рижский хлеб'', true, date (CURRENT_DATE) - integer ''3''),
        (29, 4, ''С-1-128-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 3633, ''Рижский хлеб'', true, date (CURRENT_DATE) - integer ''3''),
        (29, 5, ''В-1-128-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 3633, ''Рижский хлеб'', true, date (CURRENT_DATE) - integer ''3''),


        (30, 1, ''Ц-1-129-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6780, ''Зелёная миля'', true, date (CURRENT_DATE) - integer ''3''),    --  96.9* 0.7 = 67.8
        (30, 2, ''Ю-1-129-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6780, ''Зелёная миля'', true, date (CURRENT_DATE) - integer ''3''),
        (30, 3, ''З-1-129-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6780, ''Зелёная миля'', true, date (CURRENT_DATE) - integer ''3''),
        (30, 4, ''С-1-129-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6780, ''Зелёная миля'', true, date (CURRENT_DATE) - integer ''3''),
        (30, 5, ''В-1-129-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6780, ''Зелёная миля'', true, date (CURRENT_DATE) - integer ''3''),

        (31, 1, ''Ц-1-130-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6080, ''Зелёная миля'', true, date (CURRENT_DATE) - integer ''3''),    --  86.9* 0.7 = 60.8
        (31, 2, ''Ю-1-130-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6080, ''Зелёная миля'', true, date (CURRENT_DATE) - integer ''3''),
        (31, 3, ''З-1-130-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6080, ''Зелёная миля'', true, date (CURRENT_DATE) - integer ''3''),
        (31, 4, ''С-1-130-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6080, ''Зелёная миля'', true, date (CURRENT_DATE) - integer ''3''),
        (31, 5, ''В-1-130-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6080, ''Зелёная миля'', true, date (CURRENT_DATE) - integer ''3''),

        (32, 1, ''Ц-1-131-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4390, ''Русский сахар'', true, date (CURRENT_DATE) - integer ''3''),   --  62.8* 0.7 = 43.9
        (32, 2, ''Ю-1-131-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4390, ''Русский сахар'', true, date (CURRENT_DATE) - integer ''3''),
        (32, 3, ''З-1-131-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4390, ''Русский сахар'', true, date (CURRENT_DATE) - integer ''3''),
        (32, 4, ''С-1-131-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4390, ''Русский сахар'', true, date (CURRENT_DATE) - integer ''3''),
        (32, 5, ''В-1-131-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4390, ''Русский сахар'', true, date (CURRENT_DATE) - integer ''3''),

        (33, 1, ''Ц-1-132-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 17700, ''Integrita'', true, date (CURRENT_DATE) - integer ''3''),  --  254.0* 0.7 = 177
        (33, 2, ''Ю-1-132-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 17700, ''Integrita'', true, date (CURRENT_DATE) - integer ''3''),
        (33, 3, ''З-1-132-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 17700, ''Integrita'', true, date (CURRENT_DATE) - integer ''3''),
        (33, 4, ''С-1-132-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 17700, ''Integrita'', true, date (CURRENT_DATE) - integer ''3''),
        (33, 5, ''В-1-132-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 17700, ''Integrita'', true, date (CURRENT_DATE) - integer ''3''),

        (34, 1, ''Ц-1-133-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 3140, ''Arkhyz'', true, date (CURRENT_DATE) - integer ''3''),  --  44.9* 0.7 = 31.40
        (34, 2, ''Ю-1-133-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 3140, ''Arkhyz'', true, date (CURRENT_DATE) - integer ''3''),
        (34, 3, ''З-1-133-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 3140, ''Arkhyz'', true, date (CURRENT_DATE) - integer ''3''),
        (34, 4, ''С-1-133-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 3140, ''Arkhyz'', true, date (CURRENT_DATE) - integer ''3''),
        (34, 5, ''В-1-133-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 3140, ''Arkhyz'', true, date (CURRENT_DATE) - integer ''3''),

        (35, 1, ''Ц-1-134-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4809, ''Arkhyz'', true, date (CURRENT_DATE) - integer ''3''),  --  68.7* 0.7 = 48.09
        (35, 2, ''Ю-1-134-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4809, ''Arkhyz'', true, date (CURRENT_DATE) - integer ''3''),
        (35, 3, ''З-1-134-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4809, ''Arkhyz'', true, date (CURRENT_DATE) - integer ''3''),
        (35, 4, ''С-1-134-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4809, ''Arkhyz'', true, date (CURRENT_DATE) - integer ''3''),
        (35, 5, ''В-1-134-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4809, ''Arkhyz'', true, date (CURRENT_DATE) - integer ''3''),

        (36, 1, ''Ц-1-135-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 9600, ''ИП Иванов И.И.'', true, date (CURRENT_DATE) - integer ''3''),  --  137.25* 0.7 = 96
        (36, 2, ''Ю-1-135-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 9600, ''ИП Иванов И.И.'', true, date (CURRENT_DATE) - integer ''3''),
        (36, 3, ''З-1-135-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 9600, ''ИП Иванов И.И.'', true, date (CURRENT_DATE) - integer ''3''),
        (36, 4, ''С-1-135-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 9600, ''ИП Иванов И.И.'', true, date (CURRENT_DATE) - integer ''3''),
        (36, 5, ''В-1-135-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 9600, ''ИП Иванов И.И.'', true, date (CURRENT_DATE) - integer ''3''),

        (37, 1, ''Ц-1-136-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 18800, ''ИП Иванов И.И.'', true, date (CURRENT_DATE) - integer ''3''), --  269.9* 0.7 = 188
        (37, 2, ''Ю-1-136-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 18800, ''ИП Иванов И.И.'', true, date (CURRENT_DATE) - integer ''3''),
        (37, 3, ''З-1-136-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 18800, ''ИП Иванов И.И.'', true, date (CURRENT_DATE) - integer ''3''),
        (37, 4, ''С-1-136-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 18800, ''ИП Иванов И.И.'', true, date (CURRENT_DATE) - integer ''3''),
        (37, 5, ''В-1-136-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 18800, ''ИП Иванов И.И.'', true, date (CURRENT_DATE) - integer ''3''),

        (38, 1, ''Ц-1-137-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6550, ''ИП Фёдоров Ф.Ф.'', true, date (CURRENT_DATE) - integer ''3''), --  93.8* 0.7 = 65.5
        (38, 2, ''Ю-1-137-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6550, ''ИП Фёдоров Ф.Ф.'', true, date (CURRENT_DATE) - integer ''3''),
        (38, 3, ''З-1-137-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6550, ''ИП Фёдоров Ф.Ф.'', true, date (CURRENT_DATE) - integer ''3''),
        (38, 4, ''С-1-137-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6550, ''ИП Фёдоров Ф.Ф.'', true, date (CURRENT_DATE) - integer ''3''),
        (38, 5, ''В-1-137-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6550, ''ИП Фёдоров Ф.Ф.'', true, date (CURRENT_DATE) - integer ''3''),

        (39, 1, ''Ц-1-138-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 10493, ''Дядя Ваня'', true, date (CURRENT_DATE) - integer ''3''),  --  149.9* 0.7 = 104.93
        (39, 2, ''Ю-1-138-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 10493, ''Дядя Ваня'', true, date (CURRENT_DATE) - integer ''3''),
        (39, 3, ''З-1-138-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 10493, ''Дядя Ваня'', true, date (CURRENT_DATE) - integer ''3''),
        (39, 4, ''С-1-138-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 10493, ''Дядя Ваня'', true, date (CURRENT_DATE) - integer ''3''),
        (39, 5, ''В-1-138-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 10493, ''Дядя Ваня'', true, date (CURRENT_DATE) - integer ''3''),

        (40, 1, ''Ц-1-139-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 9090, ''ИП Петров П.П.'', true, date (CURRENT_DATE) - integer ''3''),  --  129.9* 0.7 = 90.9
        (40, 2, ''Ю-1-139-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 9090, ''ИП Петров П.П.'', true, date (CURRENT_DATE) - integer ''3''),
        (40, 3, ''З-1-139-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 9090, ''ИП Петров П.П.'', true, date (CURRENT_DATE) - integer ''3''),
        (40, 4, ''С-1-139-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 9090, ''ИП Петров П.П.'', true, date (CURRENT_DATE) - integer ''3''),
        (40, 5, ''В-1-139-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 9090, ''ИП Петров П.П.'', true, date (CURRENT_DATE) - integer ''3''),

        (41, 1, ''Ц-1-140-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 7690, ''ИП Петров П.П.'', true, date (CURRENT_DATE) - integer ''3''),  --  109.9* 0.7 = 76.9
        (41, 2, ''Ю-1-140-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 7690, ''ИП Петров П.П.'', true, date (CURRENT_DATE) - integer ''3''),
        (41, 3, ''З-1-140-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 7690, ''ИП Петров П.П.'', true, date (CURRENT_DATE) - integer ''3''),
        (41, 4, ''С-1-140-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 7690, ''ИП Петров П.П.'', true, date (CURRENT_DATE) - integer ''3''),
        (41, 5, ''В-1-140-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 7690, ''ИП Петров П.П.'', true, date (CURRENT_DATE) - integer ''3''),

        (42, 1, ''Ц-1-141-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 12590, ''Моё лето'', true, date (CURRENT_DATE) - integer ''3''),   --  179.9* 0.7 = 125.9
        (42, 2, ''Ю-1-141-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 12590, ''Моё лето'', true, date (CURRENT_DATE) - integer ''3''),
        (42, 3, ''З-1-141-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 12590, ''Моё лето'', true, date (CURRENT_DATE) - integer ''3''),
        (42, 4, ''С-1-141-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 12590, ''Моё лето'', true, date (CURRENT_DATE) - integer ''3''),
        (42, 5, ''В-1-141-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 12590, ''Моё лето'', true, date (CURRENT_DATE) - integer ''3''),

        (43, 1, ''Ц-1-142-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 9090, ''Моё лето'', true, date (CURRENT_DATE) - integer ''3''),    --  129.9* 0.7 = 90.9
        (43, 2, ''Ю-1-142-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 9090, ''Моё лето'', true, date (CURRENT_DATE) - integer ''3''),
        (43, 3, ''З-1-142-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 9090, ''Моё лето'', true, date (CURRENT_DATE) - integer ''3''),
        (43, 4, ''С-1-142-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 9090, ''Моё лето'', true, date (CURRENT_DATE) - integer ''3''),
        (43, 5, ''В-1-142-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 9090, ''Моё лето'', true, date (CURRENT_DATE) - integer ''3''),

        (44, 1, ''Ц-1-143-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 18890, ''Моё лето'', true, date (CURRENT_DATE) - integer ''3''),   --  269.9* 0.7 = 188.9
        (44, 2, ''Ю-1-143-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 18890, ''Моё лето'', true, date (CURRENT_DATE) - integer ''3''),
        (44, 3, ''З-1-143-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 18890, ''Моё лето'', true, date (CURRENT_DATE) - integer ''3''),
        (44, 4, ''С-1-143-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 18890, ''Моё лето'', true, date (CURRENT_DATE) - integer ''3''),
        (44, 5, ''В-1-143-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 18890, ''Моё лето'', true, date (CURRENT_DATE) - integer ''3''),

        (45, 1, ''Ц-1-144-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 2160, ''ИП Петров П.П.'', true, date (CURRENT_DATE) - integer ''3''),  --  30.9* 0.7 = 21.6
        (45, 2, ''Ю-1-144-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 2160, ''ИП Петров П.П.'', true, date (CURRENT_DATE) - integer ''3''),
        (45, 3, ''З-1-144-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 2160, ''ИП Петров П.П.'', true, date (CURRENT_DATE) - integer ''3''),
        (45, 4, ''С-1-144-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 2160, ''ИП Петров П.П.'', true, date (CURRENT_DATE) - integer ''3''),
        (45, 5, ''В-1-144-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 2160, ''ИП Петров П.П.'', true, date (CURRENT_DATE) - integer ''3''),

        (46, 1, ''Ц-1-145-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 5990, ''ИП Петров П.П.'', true, date (CURRENT_DATE) - integer ''3''),  --  50 -> 59.9
        (46, 2, ''Ю-1-145-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 5990, ''ИП Петров П.П.'', true, date (CURRENT_DATE) - integer ''3''),
        (46, 3, ''З-1-145-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 5990, ''ИП Петров П.П.'', true, date (CURRENT_DATE) - integer ''3''),
        (46, 4, ''С-1-145-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 5990, ''ИП Петров П.П.'', true, date (CURRENT_DATE) - integer ''3''),
        (46, 5, ''В-1-145-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 5990, ''ИП Петров П.П.'', true, date (CURRENT_DATE) - integer ''3''),

        (47, 1, ''Ц-1-146-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 3900, ''ИП Фёдоров Ф.Ф.'', true, date (CURRENT_DATE) - integer ''3''), --  55.9 * 0.7 = 39
        (47, 2, ''Ю-1-146-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 3900, ''ИП Фёдоров Ф.Ф.'', true, date (CURRENT_DATE) - integer ''3''),
        (47, 3, ''З-1-146-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 3900, ''ИП Фёдоров Ф.Ф.'', true, date (CURRENT_DATE) - integer ''3''),
        (47, 4, ''С-1-146-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 3900, ''ИП Фёдоров Ф.Ф.'', true, date (CURRENT_DATE) - integer ''3''),
        (47, 5, ''В-1-146-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 3900, ''ИП Фёдоров Ф.Ф.'', true, date (CURRENT_DATE) - integer ''3''),

        (48, 1, ''Ц-1-147-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 3550, ''ИП Фёдоров Ф.Ф.'', true, date (CURRENT_DATE) - integer ''3''), --  50.85* 0.7 = 35.5
        (48, 2, ''Ю-1-147-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 3550, ''ИП Фёдоров Ф.Ф.'', true, date (CURRENT_DATE) - integer ''3''),
        (48, 3, ''З-1-147-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 3550, ''ИП Фёдоров Ф.Ф.'', true, date (CURRENT_DATE) - integer ''3''),
        (48, 4, ''С-1-147-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 3550, ''ИП Фёдоров Ф.Ф.'', true, date (CURRENT_DATE) - integer ''3''),
        (48, 5, ''В-1-147-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 3550, ''ИП Фёдоров Ф.Ф.'', true, date (CURRENT_DATE) - integer ''3''),

        (49, 1, ''Ц-1-148-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6990, ''ИП Фёдоров Ф.Ф.'', true, date (CURRENT_DATE) - integer ''3''), --  99.9* 0.7 = 69.9
        (49, 2, ''Ю-1-148-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6990, ''ИП Фёдоров Ф.Ф.'', true, date (CURRENT_DATE) - integer ''3''),
        (49, 3, ''З-1-148-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6990, ''ИП Фёдоров Ф.Ф.'', true, date (CURRENT_DATE) - integer ''3''),
        (49, 4, ''С-1-148-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6990, ''ИП Фёдоров Ф.Ф.'', true, date (CURRENT_DATE) - integer ''3''),
        (49, 5, ''В-1-148-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6990, ''ИП Фёдоров Ф.Ф.'', true, date (CURRENT_DATE) - integer ''3''),

        (50, 1, ''Ц-1-149-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 2440, ''ИП Фёдоров Ф.Ф.'', true, date (CURRENT_DATE) - integer ''3''), --  34.9* 0.7 = 24.4
        (50, 2, ''Ю-1-149-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 2440, ''ИП Фёдоров Ф.Ф.'', true, date (CURRENT_DATE) - integer ''3''),
        (50, 3, ''З-1-149-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 2440, ''ИП Фёдоров Ф.Ф.'', true, date (CURRENT_DATE) - integer ''3''),
        (50, 4, ''С-1-149-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 2440, ''ИП Фёдоров Ф.Ф.'', true, date (CURRENT_DATE) - integer ''3''),
        (50, 5, ''В-1-149-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 2440, ''ИП Фёдоров Ф.Ф.'', true, date (CURRENT_DATE) - integer ''3''),

        (51, 1, ''Ц-1-150-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 8390, ''ИП Алексеев А.А.'', true, date (CURRENT_DATE) - integer ''3''),    --  119.90 * 0/7 = 83.9
        (51, 2, ''Ю-1-150-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 8390, ''ИП Алексеев А.А.'', true, date (CURRENT_DATE) - integer ''3''),
        (51, 3, ''З-1-150-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 8390, ''ИП Алексеев А.А.'', true, date (CURRENT_DATE) - integer ''3''),
        (51, 4, ''С-1-150-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 8390, ''ИП Алексеев А.А.'', true, date (CURRENT_DATE) - integer ''3''),
        (51, 5, ''В-1-150-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 8390, ''ИП Алексеев А.А.'', true, date (CURRENT_DATE) - integer ''3''),


        (52, 1, ''Ц-1-151-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 5590, ''ИП Алексеев А.А.'', true, date (CURRENT_DATE) - integer ''3''),    --  79.9* 0.7 = 55.9
        (52, 2, ''Ю-1-151-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 5590, ''ИП Алексеев А.А.'', true, date (CURRENT_DATE) - integer ''3''),
        (52, 3, ''З-1-151-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 5590, ''ИП Алексеев А.А.'', true, date (CURRENT_DATE) - integer ''3''),
        (52, 4, ''С-1-151-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 5590, ''ИП Алексеев А.А.'', true, date (CURRENT_DATE) - integer ''3''),
        (52, 5, ''В-1-151-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 5590, ''ИП Алексеев А.А.'', true, date (CURRENT_DATE) - integer ''3''),


        (53, 1, ''Ц-1-152-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 18470, ''ИП Алексеев А.А.'', true, date (CURRENT_DATE) - integer ''3''),   --  263.9* 0.7 = 184.7
        (53, 2, ''Ю-1-152-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 18470, ''ИП Алексеев А.А.'', true, date (CURRENT_DATE) - integer ''3''),
        (53, 3, ''З-1-152-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 18470, ''ИП Алексеев А.А.'', true, date (CURRENT_DATE) - integer ''3''),
        (53, 4, ''С-1-152-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 18470, ''ИП Алексеев А.А.'', true, date (CURRENT_DATE) - integer ''3''),
        (53, 5, ''В-1-152-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 18470, ''ИП Алексеев А.А.'', true, date (CURRENT_DATE) - integer ''3''),


        (54, 1, ''Ц-1-153-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 3490, ''ИП Алексеев А.А.'', true, date (CURRENT_DATE) - integer ''3''),    --  49.9* 0.7 = 34.9
        (54, 2, ''Ю-1-153-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 3490, ''ИП Алексеев А.А.'', true, date (CURRENT_DATE) - integer ''3''),
        (54, 3, ''З-1-153-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 3490, ''ИП Алексеев А.А.'', true, date (CURRENT_DATE) - integer ''3''),
        (54, 4, ''С-1-153-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 3490, ''ИП Алексеев А.А.'', true, date (CURRENT_DATE) - integer ''3''),
        (54, 5, ''В-1-153-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 3490, ''ИП Алексеев А.А.'', true, date (CURRENT_DATE) - integer ''3''),



        (55, 1, ''Ц-1-154-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 13390, ''ИП Алексеев А.А.'', true, date (CURRENT_DATE) - integer ''3''),   --  199.9* 0.7 = 133.9
        (55, 2, ''Ю-1-154-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 13390, ''ИП Алексеев А.А.'', true, date (CURRENT_DATE) - integer ''3''),
        (55, 3, ''З-1-154-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 13390, ''ИП Алексеев А.А.'', true, date (CURRENT_DATE) - integer ''3''),
        (55, 4, ''С-1-154-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 13390, ''ИП Алексеев А.А.'', true, date (CURRENT_DATE) - integer ''3''),
        (55, 5, ''В-1-154-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 13390, ''ИП Алексеев А.А.'', true, date (CURRENT_DATE) - integer ''3''),



        (56, 1, ''Ц-1-155-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 11893, ''ИП Михайлов М.М.'', true, date (CURRENT_DATE) - integer ''3''),   --  169.9* 0.7 = 118.93
        (56, 2, ''Ю-1-155-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 11893, ''ИП Михайлов М.М.'', true, date (CURRENT_DATE) - integer ''3''),
        (56, 3, ''З-1-155-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 11893, ''ИП Михайлов М.М.'', true, date (CURRENT_DATE) - integer ''3''),
        (56, 4, ''С-1-155-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 11893, ''ИП Михайлов М.М.'', true, date (CURRENT_DATE) - integer ''3''),
        (56, 5, ''В-1-155-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 11893, ''ИП Михайлов М.М.'', true, date (CURRENT_DATE) - integer ''3''),



        (57, 1, ''Ц-1-156-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 11893, ''ИП Михайлов М.М.'', true, date (CURRENT_DATE) - integer ''3''),   --  169.9* 0.7 = 118.93
        (57, 2, ''Ю-1-156-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 11893, ''ИП Михайлов М.М.'', true, date (CURRENT_DATE) - integer ''3''),
        (57, 3, ''З-1-156-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 11893, ''ИП Михайлов М.М.'', true, date (CURRENT_DATE) - integer ''3''),
        (57, 4, ''С-1-156-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 11893, ''ИП Михайлов М.М.'', true, date (CURRENT_DATE) - integer ''3''),
        (57, 5, ''В-1-156-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 11893, ''ИП Михайлов М.М.'', true, date (CURRENT_DATE) - integer ''3''),


        (58, 1, ''Ц-1-157-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 13990, ''ИП Михайлов М.М.'', true, date (CURRENT_DATE) - integer ''3''),   --  199.9 * 0.7=139.9
        (58, 2, ''Ю-1-157-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 13990, ''ИП Михайлов М.М.'', true, date (CURRENT_DATE) - integer ''3''),
        (58, 3, ''З-1-157-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 13990, ''ИП Михайлов М.М.'', true, date (CURRENT_DATE) - integer ''3''),
        (58, 4, ''С-1-157-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 13990, ''ИП Михайлов М.М.'', true, date (CURRENT_DATE) - integer ''3''),
        (58, 5, ''В-1-157-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 13990, ''ИП Михайлов М.М.'', true, date (CURRENT_DATE) - integer ''3''),


        (59, 1, ''Ц-1-158-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 12950, ''ИП Александров А.А.'', true, date (CURRENT_DATE) - integer ''3''),    --  185* 0.7 = 129.5
        (59, 2, ''Ю-1-158-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 12950, ''ИП Александров А.А.'', true, date (CURRENT_DATE) - integer ''3''),
        (59, 3, ''З-1-158-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 12950, ''ИП Александров А.А.'', true, date (CURRENT_DATE) - integer ''3''),
        (59, 4, ''С-1-158-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 12950, ''ИП Александров А.А.'', true, date (CURRENT_DATE) - integer ''3''),
        (59, 5, ''В-1-158-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 12950, ''ИП Александров А.А.'', true, date (CURRENT_DATE) - integer ''3''),


        (60, 1, ''Ц-1-159-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 12250, ''ИП Александров А.А.'', true, date (CURRENT_DATE) - integer ''3''),    --  175 * 0.7 = 122.5
        (60, 2, ''Ю-1-159-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 12250, ''ИП Александров А.А.'', true, date (CURRENT_DATE) - integer ''3''),
        (60, 3, ''З-1-159-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 12250, ''ИП Александров А.А.'', true, date (CURRENT_DATE) - integer ''3''),
        (60, 4, ''С-1-159-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 12250, ''ИП Александров А.А.'', true, date (CURRENT_DATE) - integer ''3''),
        (60, 5, ''В-1-159-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 12250, ''ИП Александров А.А.'', true, date (CURRENT_DATE) - integer ''3''),

        (61, 1, ''Ц-1-160-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 9090, ''ИП Александров А.А.'', true, date (CURRENT_DATE) - integer ''3''), --  129.9* 0.7 = 90.90
        (61, 2, ''Ю-1-160-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 9090, ''ИП Александров А.А.'', true, date (CURRENT_DATE) - integer ''3''),
        (61, 3, ''З-1-160-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 9090, ''ИП Александров А.А.'', true, date (CURRENT_DATE) - integer ''3''),
        (61, 4, ''С-1-160-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 9090, ''ИП Александров А.А.'', true, date (CURRENT_DATE) - integer ''3''),
        (61, 5, ''В-1-160-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 9090, ''ИП Александров А.А.'', true, date (CURRENT_DATE) - integer ''3''),

        (62, 1, ''Ц-1-161-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6900, ''ИП Александров А.А.'', true, date (CURRENT_DATE) - integer ''3''), --  60 -> 69
        (62, 2, ''Ю-1-161-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6900, ''ИП Александров А.А.'', true, date (CURRENT_DATE) - integer ''3''),
        (62, 3, ''З-1-161-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6900, ''ИП Александров А.А.'', true, date (CURRENT_DATE) - integer ''3''),
        (62, 4, ''С-1-161-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6900, ''ИП Александров А.А.'', true, date (CURRENT_DATE) - integer ''3''),
        (62, 5, ''В-1-161-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6900, ''ИП Александров А.А.'', true, date (CURRENT_DATE) - integer ''3''),

        (63, 1, ''Ц-1-162-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 5940, ''ИП Александров А.А.'', true, date (CURRENT_DATE) - integer ''3''), --  84.9* 0.7 =59.4
        (63, 2, ''Ю-1-162-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 5940, ''ИП Александров А.А.'', true, date (CURRENT_DATE) - integer ''3''),
        (63, 3, ''З-1-162-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 5940, ''ИП Александров А.А.'', true, date (CURRENT_DATE) - integer ''3''),
        (63, 4, ''С-1-162-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 5940, ''ИП Александров А.А.'', true, date (CURRENT_DATE) - integer ''3''),
        (63, 5, ''В-1-162-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 5940, ''ИП Александров А.А.'', true, date (CURRENT_DATE) - integer ''3''),

        (64, 1, ''Ц-1-163-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 8390, ''ИП Александров А.А.'', true, date (CURRENT_DATE) - integer ''3''), --  119.9* 0.7 =83.9
        (64, 2, ''Ю-1-163-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 8390, ''ИП Александров А.А.'', true, date (CURRENT_DATE) - integer ''3''),
        (64, 3, ''З-1-163-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 8390, ''ИП Александров А.А.'', true, date (CURRENT_DATE) - integer ''3''),
        (64, 4, ''С-1-163-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 8390, ''ИП Александров А.А.'', true, date (CURRENT_DATE) - integer ''3''),
        (64, 5, ''В-1-163-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 8390, ''ИП Александров А.А.'', true, date (CURRENT_DATE) - integer ''3''),

        (65, 1, ''Ц-1-164-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6990, ''ИП Александров А.А.'', true, date (CURRENT_DATE) - integer ''3''), --  99.9* 0.7 =69.9
        (65, 2, ''Ю-1-164-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6990, ''ИП Александров А.А.'', true, date (CURRENT_DATE) - integer ''3''),
        (65, 3, ''З-1-164-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6990, ''ИП Александров А.А.'', true, date (CURRENT_DATE) - integer ''3''),
        (65, 4, ''С-1-164-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6990, ''ИП Александров А.А.'', true, date (CURRENT_DATE) - integer ''3''),
        (65, 5, ''В-1-164-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6990, ''ИП Александров А.А.'', true, date (CURRENT_DATE) - integer ''3''),

        (66, 1, ''Ц-1-165-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 5459, ''Chiquita'', true, date (CURRENT_DATE) - integer ''3''),    --  77.99* 0.7 =54.59
        (66, 2, ''Ю-1-165-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 5459, ''Chiquita'', true, date (CURRENT_DATE) - integer ''3''),
        (66, 3, ''З-1-165-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 5459, ''Chiquita'', true, date (CURRENT_DATE) - integer ''3''),
        (66, 4, ''С-1-165-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 5459, ''Chiquita'', true, date (CURRENT_DATE) - integer ''3''),
        (66, 5, ''В-1-165-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 5459, ''Chiquita'', true, date (CURRENT_DATE) - integer ''3''),

        (67, 1, ''Ц-1-166-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 9926, ''Chiquita'', true, date (CURRENT_DATE) - integer ''3''),    --  141.8 * 0.7 = 99.26
        (67, 2, ''Ю-1-166-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 9926, ''Chiquita'', true, date (CURRENT_DATE) - integer ''3''),
        (67, 3, ''З-1-166-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 9926, ''Chiquita'', true, date (CURRENT_DATE) - integer ''3''),
        (67, 4, ''С-1-166-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 9926, ''Chiquita'', true, date (CURRENT_DATE) - integer ''3''),
        (67, 5, ''В-1-166-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 9926, ''Chiquita'', true, date (CURRENT_DATE) - integer ''3''),

        (68, 1, ''Ц-1-167-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 11000, ''ЕВРОСНАБ АГРО'', true, date (CURRENT_DATE) - integer ''3''),  --  110 -> 114
        (68, 2, ''Ю-1-167-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 11000, ''ЕВРОСНАБ АГРО'', true, date (CURRENT_DATE) - integer ''3''),
        (68, 3, ''З-1-167-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 11000, ''ЕВРОСНАБ АГРО'', true, date (CURRENT_DATE) - integer ''3''),
        (68, 4, ''С-1-167-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 11000, ''ЕВРОСНАБ АГРО'', true, date (CURRENT_DATE) - integer ''3''),
        (68, 5, ''В-1-167-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 11000, ''ЕВРОСНАБ АГРО'', true, date (CURRENT_DATE) - integer ''3''),

        (69, 1, ''Ц-1-168-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 10000, ''ЕВРОСНАБ АГРО'', true, date (CURRENT_DATE) - integer ''3''),  --  100 -> 109
        (69, 2, ''Ю-1-168-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 10000, ''ЕВРОСНАБ АГРО'', true, date (CURRENT_DATE) - integer ''3''),
        (69, 3, ''З-1-168-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 10000, ''ЕВРОСНАБ АГРО'', true, date (CURRENT_DATE) - integer ''3''),
        (69, 4, ''С-1-168-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 10000, ''ЕВРОСНАБ АГРО'', true, date (CURRENT_DATE) - integer ''3''),
        (69, 5, ''В-1-168-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 10000, ''ЕВРОСНАБ АГРО'', true, date (CURRENT_DATE) - integer ''3''),


        (70, 1, ''Ц-1-169-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 13000, ''ЕВРОСНАБ АГРО'', true, date (CURRENT_DATE) - integer ''3''),  --  130 -> 139.9
        (70, 2, ''Ю-1-169-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 13000, ''ЕВРОСНАБ АГРО'', true, date (CURRENT_DATE) - integer ''3''),
        (70, 3, ''З-1-169-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 13000, ''ЕВРОСНАБ АГРО'', true, date (CURRENT_DATE) - integer ''3''),
        (70, 4, ''С-1-169-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 13000, ''ЕВРОСНАБ АГРО'', true, date (CURRENT_DATE) - integer ''3''),
        (70, 5, ''В-1-169-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 13000, ''ЕВРОСНАБ АГРО'', true, date (CURRENT_DATE) - integer ''3''),

        (71, 1, ''Ц-1-170-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 11000, ''ЕВРОСНАБ АГРО'', true, date (CURRENT_DATE) - integer ''3''),  --  110 -> 119.9
        (71, 2, ''Ю-1-170-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 11000, ''ЕВРОСНАБ АГРО'', true, date (CURRENT_DATE) - integer ''3''),
        (71, 3, ''З-1-170-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 11000, ''ЕВРОСНАБ АГРО'', true, date (CURRENT_DATE) - integer ''3''),
        (71, 4, ''С-1-170-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 11000, ''ЕВРОСНАБ АГРО'', true, date (CURRENT_DATE) - integer ''3''),
        (71, 5, ''В-1-170-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 11000, ''ЕВРОСНАБ АГРО'', true, date (CURRENT_DATE) - integer ''3''),

        (72, 1, ''Ц-1-171-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 18990, ''ЕВРОСНАБ АГРО'', true, date (CURRENT_DATE) - integer ''3''),  --  189.9 * 0.7=132.9
        (72, 2, ''Ю-1-171-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 18990, ''ЕВРОСНАБ АГРО'', true, date (CURRENT_DATE) - integer ''3''),
        (72, 3, ''З-1-171-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 18990, ''ЕВРОСНАБ АГРО'', true, date (CURRENT_DATE) - integer ''3''),
        (72, 4, ''С-1-171-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 18990, ''ЕВРОСНАБ АГРО'', true, date (CURRENT_DATE) - integer ''3''),
        (72, 5, ''В-1-171-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 18990, ''ЕВРОСНАБ АГРО'', true, date (CURRENT_DATE) - integer ''3''),

        (73, 1, ''Ц-1-172-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 16990, ''ЕВРОСНАБ АГРО'', true, date (CURRENT_DATE) - integer ''3''),  --  169.9 * 0.7= 118.9
        (73, 2, ''Ю-1-172-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 16990, ''ЕВРОСНАБ АГРО'', true, date (CURRENT_DATE) - integer ''3''),
        (73, 3, ''З-1-172-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 16990, ''ЕВРОСНАБ АГРО'', true, date (CURRENT_DATE) - integer ''3''),
        (73, 4, ''С-1-172-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 16990, ''ЕВРОСНАБ АГРО'', true, date (CURRENT_DATE) - integer ''3''),
        (73, 5, ''В-1-172-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 16990, ''ЕВРОСНАБ АГРО'', true, date (CURRENT_DATE) - integer ''3''),

        (74, 1, ''Ц-1-173-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 8260, ''Мистраль'', true, date (CURRENT_DATE) - integer ''3''),    --  118.0 * 0.7= 82.6
        (74, 2, ''Ю-1-173-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 8260, ''Мистраль'', true, date (CURRENT_DATE) - integer ''3''),
        (74, 3, ''З-1-173-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 8260, ''Мистраль'', true, date (CURRENT_DATE) - integer ''3''),
        (74, 4, ''С-1-173-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 8260, ''Мистраль'', true, date (CURRENT_DATE) - integer ''3''),
        (74, 5, ''В-1-173-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 8260, ''Мистраль'', true, date (CURRENT_DATE) - integer ''3''),

        (75, 1, ''Ц-1-174-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6990, ''Мистраль'', true, date (CURRENT_DATE) - integer ''3''),    --  99.9 * 0.7= 69.9
        (75, 2, ''Ю-1-174-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6990, ''Мистраль'', true, date (CURRENT_DATE) - integer ''3''),
        (75, 3, ''З-1-174-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6990, ''Мистраль'', true, date (CURRENT_DATE) - integer ''3''),
        (75, 4, ''С-1-174-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6990, ''Мистраль'', true, date (CURRENT_DATE) - integer ''3''),
        (75, 5, ''В-1-174-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6990, ''Мистраль'', true, date (CURRENT_DATE) - integer ''3''),

        (76, 1, ''Ц-1-175-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 5280, ''Мистраль'', true, date (CURRENT_DATE) - integer ''3''),    -- 75.50 * 0.7 = 52.80
        (76, 2, ''Ю-1-175-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 5280, ''Мистраль'', true, date (CURRENT_DATE) - integer ''3''),
        (76, 3, ''З-1-175-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 5280, ''Мистраль'', true, date (CURRENT_DATE) - integer ''3''),
        (76, 4, ''С-1-175-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 5280, ''Мистраль'', true, date (CURRENT_DATE) - integer ''3''),
        (76, 5, ''В-1-175-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 5280, ''Мистраль'', true, date (CURRENT_DATE) - integer ''3''),

        (77, 1, ''Ц-1-176-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 9300, ''Мистраль'', true, date (CURRENT_DATE) - integer ''3''),    -- 75 -> 93
        (77, 2, ''Ю-1-176-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 9300, ''Мистраль'', true, date (CURRENT_DATE) - integer ''3''),
        (77, 3, ''З-1-176-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 9300, ''Мистраль'', true, date (CURRENT_DATE) - integer ''3''),
        (77, 4, ''С-1-176-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 9300, ''Мистраль'', true, date (CURRENT_DATE) - integer ''3''),
        (77, 5, ''В-1-176-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 9300, ''Мистраль'', true, date (CURRENT_DATE) - integer ''3''),

        (78, 1, ''Ц-1-177-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 8050, ''Мистраль'', true, date (CURRENT_DATE) - integer ''3''),    -- 115 * 0.7 = 80.5
        (78, 2, ''Ю-1-177-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 8050, ''Мистраль'', true, date (CURRENT_DATE) - integer ''3''),
        (78, 3, ''З-1-177-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 8050, ''Мистраль'', true, date (CURRENT_DATE) - integer ''3''),
        (78, 4, ''С-1-177-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 8050, ''Мистраль'', true, date (CURRENT_DATE) - integer ''3''),
        (78, 5, ''В-1-177-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 8050, ''Мистраль'', true, date (CURRENT_DATE) - integer ''3''),

        (79, 1, ''Ц-1-178-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 10000, ''Мистраль'', true, date (CURRENT_DATE) - integer ''3''),   --  100 -> 119
        (79, 2, ''Ю-1-178-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 10000, ''Мистраль'', true, date (CURRENT_DATE) - integer ''3''),
        (79, 3, ''З-1-178-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 10000, ''Мистраль'', true, date (CURRENT_DATE) - integer ''3''),
        (79, 4, ''С-1-178-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 10000, ''Мистраль'', true, date (CURRENT_DATE) - integer ''3''),
        (79, 5, ''В-1-178-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 10000, ''Мистраль'', true, date (CURRENT_DATE) - integer ''3''),


        (80, 1, ''Ц-1-179-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 10000, ''Мистраль'', true, date (CURRENT_DATE) - integer ''3''),   --  143 * 0.7 =  100
        (80, 2, ''Ю-1-179-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 10000, ''Мистраль'', true, date (CURRENT_DATE) - integer ''3''),
        (80, 3, ''З-1-179-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 10000, ''Мистраль'', true, date (CURRENT_DATE) - integer ''3''),
        (80, 4, ''С-1-179-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 10000, ''Мистраль'', true, date (CURRENT_DATE) - integer ''3''),
        (80, 5, ''В-1-179-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 10000, ''Мистраль'', true, date (CURRENT_DATE) - integer ''3''),

        (81, 1, ''Ц-1-180-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4340, ''Агро-Альянс'', true, date (CURRENT_DATE) - integer ''3''), --   62* 0.7 = 43.4
        (81, 2, ''Ю-1-180-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4340, ''Агро-Альянс'', true, date (CURRENT_DATE) - integer ''3''),
        (81, 3, ''З-1-180-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4340, ''Агро-Альянс'', true, date (CURRENT_DATE) - integer ''3''),
        (81, 4, ''С-1-180-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4340, ''Агро-Альянс'', true, date (CURRENT_DATE) - integer ''3''),
        (81, 5, ''В-1-180-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4340, ''Агро-Альянс'', true, date (CURRENT_DATE) - integer ''3''),

        (82, 1, ''Ц-1-181-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4970, ''Makfa'', true, date (CURRENT_DATE) - integer ''3''),   --   71.10 * 0.7 = 49.70
        (82, 2, ''Ю-1-181-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4970, ''Makfa'', true, date (CURRENT_DATE) - integer ''3''),
        (82, 3, ''З-1-181-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4970, ''Makfa'', true, date (CURRENT_DATE) - integer ''3''),
        (82, 4, ''С-1-181-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4970, ''Makfa'', true, date (CURRENT_DATE) - integer ''3''),
        (82, 5, ''В-1-181-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4970, ''Makfa'', true, date (CURRENT_DATE) - integer ''3''),

        (83, 1, ''Ц-1-182-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4130, ''Makfa'', true, date (CURRENT_DATE) - integer ''3''),   --  59 * 0.7 =41.3
        (83, 2, ''Ю-1-182-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4130, ''Makfa'', true, date (CURRENT_DATE) - integer ''3''),
        (83, 3, ''З-1-182-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4130, ''Makfa'', true, date (CURRENT_DATE) - integer ''3''),
        (83, 4, ''С-1-182-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4130, ''Makfa'', true, date (CURRENT_DATE) - integer ''3''),
        (83, 5, ''В-1-182-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4130, ''Makfa'', true, date (CURRENT_DATE) - integer ''3''),

        (84, 1, ''Ц-1-183-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 5590, ''Makfa'', true, date (CURRENT_DATE) - integer ''3''),   --   79.9* 0.7 = 55.90
        (84, 2, ''Ю-1-183-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 5590, ''Makfa'', true, date (CURRENT_DATE) - integer ''3''),
        (84, 3, ''З-1-183-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 5590, ''Makfa'', true, date (CURRENT_DATE) - integer ''3''),
        (84, 4, ''С-1-183-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 5590, ''Makfa'', true, date (CURRENT_DATE) - integer ''3''),
        (84, 5, ''В-1-183-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 5590, ''Makfa'', true, date (CURRENT_DATE) - integer ''3''),

        (85, 1, ''Ц-1-184-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 1540, ''Полесье'', true, date (CURRENT_DATE) - integer ''3''), --   22.0 * 0.7 = 15.4
        (85, 2, ''Ю-1-184-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 1540, ''Полесье'', true, date (CURRENT_DATE) - integer ''3''),
        (85, 3, ''З-1-184-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 1540, ''Полесье'', true, date (CURRENT_DATE) - integer ''3''),
        (85, 4, ''С-1-184-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 1540, ''Полесье'', true, date (CURRENT_DATE) - integer ''3''),
        (85, 5, ''В-1-184-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 1540, ''Полесье'', true, date (CURRENT_DATE) - integer ''3''),

        (86, 1, ''Ц-1-185-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4500, ''Полесье'', true, date (CURRENT_DATE) - integer ''3''), --   64.40 * 0.7 = 45
        (86, 2, ''Ю-1-185-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4500, ''Полесье'', true, date (CURRENT_DATE) - integer ''3''),
        (86, 3, ''З-1-185-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4500, ''Полесье'', true, date (CURRENT_DATE) - integer ''3''),
        (86, 4, ''С-1-185-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4500, ''Полесье'', true, date (CURRENT_DATE) - integer ''3''),
        (86, 5, ''В-1-185-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4500, ''Полесье'', true, date (CURRENT_DATE) - integer ''3''),

        (87, 1, ''Ц-1-186-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4590, ''Восточный гость'', true, date (CURRENT_DATE) - integer ''3''), --  37 -> 45.9
        (87, 2, ''Ю-1-186-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4590, ''Восточный гость'', true, date (CURRENT_DATE) - integer ''3''),
        (87, 3, ''З-1-186-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4590, ''Восточный гость'', true, date (CURRENT_DATE) - integer ''3''),
        (87, 4, ''С-1-186-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4590, ''Восточный гость'', true, date (CURRENT_DATE) - integer ''3''),
        (87, 5, ''В-1-186-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4590, ''Восточный гость'', true, date (CURRENT_DATE) - integer ''3''),

        (88, 1, ''Ц-1-187-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4590, ''Восточный гость'', true, date (CURRENT_DATE) - integer ''3''), --   37 -> 45.9
        (88, 2, ''Ю-1-187-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4590, ''Восточный гость'', true, date (CURRENT_DATE) - integer ''3''),
        (88, 3, ''З-1-187-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4590, ''Восточный гость'', true, date (CURRENT_DATE) - integer ''3''),
        (88, 4, ''С-1-187-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4590, ''Восточный гость'', true, date (CURRENT_DATE) - integer ''3''),
        (88, 5, ''В-1-187-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4590, ''Восточный гость'', true, date (CURRENT_DATE) - integer ''3''),

        (89, 1, ''Ц-1-188-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 9100, ''Рот-Фронт'', true, date (CURRENT_DATE) - integer ''3''),   --   130 * 0.7 = 91
        (89, 2, ''Ю-1-188-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 9100, ''Рот-Фронт'', true, date (CURRENT_DATE) - integer ''3''),
        (89, 3, ''З-1-188-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 9100, ''Рот-Фронт'', true, date (CURRENT_DATE) - integer ''3''),
        (89, 4, ''С-1-188-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 9100, ''Рот-Фронт'', true, date (CURRENT_DATE) - integer ''3''),
        (89, 5, ''В-1-188-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 9100, ''Рот-Фронт'', true, date (CURRENT_DATE) - integer ''3''),

        (90, 1, ''Ц-1-189-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 10500, ''Рот-Фронт'', true, date (CURRENT_DATE) - integer ''3''),  --   150* 0.7 = 105
        (90, 2, ''Ю-1-189-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 10500, ''Рот-Фронт'', true, date (CURRENT_DATE) - integer ''3''),
        (90, 3, ''З-1-189-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 10500, ''Рот-Фронт'', true, date (CURRENT_DATE) - integer ''3''),
        (90, 4, ''С-1-189-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 10500, ''Рот-Фронт'', true, date (CURRENT_DATE) - integer ''3''),
        (90, 5, ''В-1-189-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 10500, ''Рот-Фронт'', true, date (CURRENT_DATE) - integer ''3''),

        (91, 1, ''Ц-1-190-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 11000, ''Рот-Фронт'', true, date (CURRENT_DATE) - integer ''3''),  --   110 -> 110
        (91, 2, ''Ю-1-190-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 11000, ''Рот-Фронт'', true, date (CURRENT_DATE) - integer ''3''),
        (91, 3, ''З-1-190-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 11000, ''Рот-Фронт'', true, date (CURRENT_DATE) - integer ''3''),
        (91, 4, ''С-1-190-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 11000, ''Рот-Фронт'', true, date (CURRENT_DATE) - integer ''3''),
        (91, 5, ''В-1-190-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 11000, ''Рот-Фронт'', true, date (CURRENT_DATE) - integer ''3''),

        (92, 1, ''Ц-1-191-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 5270, ''Юбилейное'', true, date (CURRENT_DATE) - integer ''3''),   --   75.4 * 0.7 = 52.7
        (92, 2, ''Ю-1-191-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 5270, ''Юбилейное'', true, date (CURRENT_DATE) - integer ''3''),
        (92, 3, ''З-1-191-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 5270, ''Юбилейное'', true, date (CURRENT_DATE) - integer ''3''),
        (92, 4, ''С-1-191-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 5270, ''Юбилейное'', true, date (CURRENT_DATE) - integer ''3''),
        (92, 5, ''В-1-191-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 5270, ''Юбилейное'', true, date (CURRENT_DATE) - integer ''3''),

        (93, 1, ''Ц-1-192-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 8190, ''Юбилейное'', true, date (CURRENT_DATE) - integer ''3''),   --   117 * 0.7 = 81.9
        (93, 2, ''Ю-1-192-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 8190, ''Юбилейное'', true, date (CURRENT_DATE) - integer ''3''),
        (93, 3, ''З-1-192-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 8190, ''Юбилейное'', true, date (CURRENT_DATE) - integer ''3''),
        (93, 4, ''С-1-192-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 8190, ''Юбилейное'', true, date (CURRENT_DATE) - integer ''3''),
        (93, 5, ''В-1-192-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 8190, ''Юбилейное'', true, date (CURRENT_DATE) - integer ''3''),

        (94, 1, ''Ц-1-193-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6760, ''Бискотти'', true, date (CURRENT_DATE) - integer ''3''),    --  96.6 * 0.7 = 67.6
        (94, 2, ''Ю-1-193-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6760, ''Бискотти'', true, date (CURRENT_DATE) - integer ''3''),
        (94, 3, ''З-1-193-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6760, ''Бискотти'', true, date (CURRENT_DATE) - integer ''3''),
        (94, 4, ''С-1-193-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6760, ''Бискотти'', true, date (CURRENT_DATE) - integer ''3''),
        (94, 5, ''В-1-193-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6760, ''Бискотти'', true, date (CURRENT_DATE) - integer ''3''),

        (95, 1, ''Ц-1-194-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 7590, ''Greenfield'', true, date (CURRENT_DATE) - integer ''3''),  --   100 -> 75.9
        (95, 2, ''Ю-1-194-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 7590, ''Greenfield'', true, date (CURRENT_DATE) - integer ''3''),
        (95, 3, ''З-1-194-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 7590, ''Greenfield'', true, date (CURRENT_DATE) - integer ''3''),
        (95, 4, ''С-1-194-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 7590, ''Greenfield'', true, date (CURRENT_DATE) - integer ''3''),
        (95, 5, ''В-1-194-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 7590, ''Greenfield'', true, date (CURRENT_DATE) - integer ''3''),

        (96, 1, ''Ц-1-195-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 7630, ''Greenfield'', true, date (CURRENT_DATE) - integer ''3''),  --   109 * 0.7 = 76.3
        (96, 2, ''Ю-1-195-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 7630, ''Greenfield'', true, date (CURRENT_DATE) - integer ''3''),
        (96, 3, ''З-1-195-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 7630, ''Greenfield'', true, date (CURRENT_DATE) - integer ''3''),
        (96, 4, ''С-1-195-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 7630, ''Greenfield'', true, date (CURRENT_DATE) - integer ''3''),
        (96, 5, ''В-1-195-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 7630, ''Greenfield'', true, date (CURRENT_DATE) - integer ''3''),

        (97, 1, ''Ц-1-196-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 35000, ''Jacobs'', true, date (CURRENT_DATE) - integer ''3''), --   350 -> 399.0
        (97, 2, ''Ю-1-196-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 35000, ''Jacobs'', true, date (CURRENT_DATE) - integer ''3''),
        (97, 3, ''З-1-196-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 35000, ''Jacobs'', true, date (CURRENT_DATE) - integer ''3''),
        (97, 4, ''С-1-196-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 35000, ''Jacobs'', true, date (CURRENT_DATE) - integer ''3''),
        (97, 5, ''В-1-196-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 35000, ''Jacobs'', true, date (CURRENT_DATE) - integer ''3''),

        (98, 1, ''Ц-1-197-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 20000, ''Egoiste Noir'', true, date (CURRENT_DATE) - integer ''3''),   --  200 -> 179
        (98, 2, ''Ю-1-197-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 20000, ''Egoiste Noir'', true, date (CURRENT_DATE) - integer ''3''),
        (98, 3, ''З-1-197-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 20000, ''Egoiste Noir'', true, date (CURRENT_DATE) - integer ''3''),
        (98, 4, ''С-1-197-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 20000, ''Egoiste Noir'', true, date (CURRENT_DATE) - integer ''3''),
        (98, 5, ''В-1-197-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 20000, ''Egoiste Noir'', true, date (CURRENT_DATE) - integer ''3''),

        (99, 1, ''Ц-1-198-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 22000, ''Мираторг'', true, date (CURRENT_DATE) - integer ''3''),   --   220 -> 259
        (99, 2, ''Ю-1-198-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 22000, ''Мираторг'', true, date (CURRENT_DATE) - integer ''3''),
        (99, 3, ''З-1-198-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 22000, ''Мираторг'', true, date (CURRENT_DATE) - integer ''3''),
        (99, 4, ''С-1-198-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 22000, ''Мираторг'', true, date (CURRENT_DATE) - integer ''3''),
        (99, 5, ''В-1-198-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 22000, ''Мираторг'', true, date (CURRENT_DATE) - integer ''3''),

        (100, 1, ''Ц-1-199-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 19880, ''Мираторг'', true, date (CURRENT_DATE) - integer ''3''),  --   284 * 0.7 = 198.8
        (100, 2, ''Ю-1-199-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 19880, ''Мираторг'', true, date (CURRENT_DATE) - integer ''3''),
        (100, 3, ''З-1-199-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 19880, ''Мираторг'', true, date (CURRENT_DATE) - integer ''3''),
        (100, 4, ''С-1-199-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 19880, ''Мираторг'', true, date (CURRENT_DATE) - integer ''3''),
        (100, 5, ''В-1-199-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 19880, ''Мираторг'', true, date (CURRENT_DATE) - integer ''3''),

        (101, 1, ''Ц-1-200-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 52430, ''Мираторг'', true, date (CURRENT_DATE) - integer ''3''),  --  749 * 0.7 = 524.3
        (101, 2, ''Ю-1-200-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 52430, ''Мираторг'', true, date (CURRENT_DATE) - integer ''3''),
        (101, 3, ''З-1-200-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 52430, ''Мираторг'', true, date (CURRENT_DATE) - integer ''3''),
        (101, 4, ''С-1-200-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 52430, ''Мираторг'', true, date (CURRENT_DATE) - integer ''3''),
        (101, 5, ''В-1-200-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 52430, ''Мираторг'', true, date (CURRENT_DATE) - integer ''3''),

        (102, 1, ''Ц-1-201-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 19740, ''Останкино'', true, date (CURRENT_DATE) - integer ''3''), --   282* 0.7 = 197.4
        (102, 2, ''Ю-1-201-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 19740, ''Останкино'', true, date (CURRENT_DATE) - integer ''3''),
        (102, 3, ''З-1-201-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 19740, ''Останкино'', true, date (CURRENT_DATE) - integer ''3''),
        (102, 4, ''С-1-201-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 19740, ''Останкино'', true, date (CURRENT_DATE) - integer ''3''),
        (102, 5, ''В-1-201-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 19740, ''Останкино'', true, date (CURRENT_DATE) - integer ''3''),

        (103, 1, ''Ц-1-202-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 51975, ''Останкино'', true, date (CURRENT_DATE) - integer ''3''), --   742.5 * 0.7 = 519.75
        (103, 2, ''Ю-1-202-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 51975, ''Останкино'', true, date (CURRENT_DATE) - integer ''3''),
        (103, 3, ''З-1-202-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 51975, ''Останкино'', true, date (CURRENT_DATE) - integer ''3''),
        (103, 4, ''С-1-202-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 51975, ''Останкино'', true, date (CURRENT_DATE) - integer ''3''),
        (103, 5, ''В-1-202-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 51975, ''Останкино'', true, date (CURRENT_DATE) - integer ''3''),

        (104, 1, ''Ц-1-203-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 50862, ''Останкино'', true, date (CURRENT_DATE) - integer ''3''), --   726.6 * 0.7 = 508.62
        (104, 2, ''Ю-1-203-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 50862, ''Останкино'', true, date (CURRENT_DATE) - integer ''3''),
        (104, 3, ''З-1-203-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 50862, ''Останкино'', true, date (CURRENT_DATE) - integer ''3''),
        (104, 4, ''С-1-203-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 50862, ''Останкино'', true, date (CURRENT_DATE) - integer ''3''),
        (104, 5, ''В-1-203-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 50862, ''Останкино'', true, date (CURRENT_DATE) - integer ''3''),

        (105, 1, ''Ц-1-204-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 27468, ''Ближние горки'', true, date (CURRENT_DATE) - integer ''3''), --   392.4 * 0.7 = 274,68
        (105, 2, ''Ю-1-204-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 27468, ''Ближние горки'', true, date (CURRENT_DATE) - integer ''3''),
        (105, 3, ''З-1-204-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 27468, ''Ближние горки'', true, date (CURRENT_DATE) - integer ''3''),
        (105, 4, ''С-1-204-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 27468, ''Ближние горки'', true, date (CURRENT_DATE) - integer ''3''),
        (105, 5, ''В-1-204-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 27468, ''Ближние горки'', true, date (CURRENT_DATE) - integer ''3''),

        (106, 1, ''Ц-1-205-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 26670, ''Кудашка'', true, date (CURRENT_DATE) - integer ''3''),   --   266.7 -> 362
        (106, 2, ''Ю-1-205-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 26670, ''Кудашка'', true, date (CURRENT_DATE) - integer ''3''),
        (106, 3, ''З-1-205-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 26670, ''Кудашка'', true, date (CURRENT_DATE) - integer ''3''),
        (106, 4, ''С-1-205-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 26670, ''Кудашка'', true, date (CURRENT_DATE) - integer ''3''),
        (106, 5, ''В-1-205-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 26670, ''Кудашка'', true, date (CURRENT_DATE) - integer ''3''),

        (107, 1, ''Ц-1-206-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 21280, ''Петелинка'', true, date (CURRENT_DATE) - integer ''3''), --   212.8 -> 251
        (107, 2, ''Ю-1-206-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 21280, ''Петелинка'', true, date (CURRENT_DATE) - integer ''3''),
        (107, 3, ''З-1-206-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 21280, ''Петелинка'', true, date (CURRENT_DATE) - integer ''3''),
        (107, 4, ''С-1-206-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 21280, ''Петелинка'', true, date (CURRENT_DATE) - integer ''3''),
        (107, 5, ''В-1-206-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 21280, ''Петелинка'', true, date (CURRENT_DATE) - integer ''3''),

        (108, 1, ''Ц-1-207-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 38500, ''Петелинка'', true, date (CURRENT_DATE) - integer ''3''), --   550 * 0.7 = 385
        (108, 2, ''Ю-1-207-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 38500, ''Петелинка'', true, date (CURRENT_DATE) - integer ''3''),
        (108, 3, ''З-1-207-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 38500, ''Петелинка'', true, date (CURRENT_DATE) - integer ''3''),
        (108, 4, ''С-1-207-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 38500, ''Петелинка'', true, date (CURRENT_DATE) - integer ''3''),
        (108, 5, ''В-1-207-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 38500, ''Петелинка'', true, date (CURRENT_DATE) - integer ''3''),

        (109, 1, ''Ц-1-208-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 16700, ''Клинский'', true, date (CURRENT_DATE) - integer ''3''),  --   163.1 -> 167
        (109, 2, ''Ю-1-208-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 16700, ''Клинский'', true, date (CURRENT_DATE) - integer ''3''),
        (109, 3, ''З-1-208-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 16700, ''Клинский'', true, date (CURRENT_DATE) - integer ''3''),
        (109, 4, ''С-1-208-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 16700, ''Клинский'', true, date (CURRENT_DATE) - integer ''3''),
        (109, 5, ''В-1-208-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 16700, ''Клинский'', true, date (CURRENT_DATE) - integer ''3''),

        (110, 1, ''Ц-1-209-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 20900, ''Папа Может'', true, date (CURRENT_DATE) - integer ''3''),    --   243.6 -> 209
        (110, 2, ''Ю-1-209-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 20900, ''Папа Может'', true, date (CURRENT_DATE) - integer ''3''),
        (110, 3, ''З-1-209-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 20900, ''Папа Может'', true, date (CURRENT_DATE) - integer ''3''),
        (110, 4, ''С-1-209-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 20900, ''Папа Может'', true, date (CURRENT_DATE) - integer ''3''),
        (110, 5, ''В-1-209-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 20900, ''Папа Может'', true, date (CURRENT_DATE) - integer ''3''),

        (111, 1, ''Ц-1-210-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 11690, ''Атяшево'', true, date (CURRENT_DATE) - integer ''3''),   --   167 * 0.7 = 116.9
        (111, 2, ''Ю-1-210-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 11690, ''Атяшево'', true, date (CURRENT_DATE) - integer ''3''),
        (111, 3, ''З-1-210-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 11690, ''Атяшево'', true, date (CURRENT_DATE) - integer ''3''),
        (111, 4, ''С-1-210-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 11690, ''Атяшево'', true, date (CURRENT_DATE) - integer ''3''),
        (111, 5, ''В-1-210-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 11690, ''Атяшево'', true, date (CURRENT_DATE) - integer ''3''),

        (112, 1, ''Ц-1-211-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 11690, ''Атяшево'', true, date (CURRENT_DATE) - integer ''3''),   --   167 * 0.7 = 116.9
        (112, 2, ''Ю-1-211-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 11690, ''Атяшево'', true, date (CURRENT_DATE) - integer ''3''),
        (112, 3, ''З-1-211-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 11690, ''Атяшево'', true, date (CURRENT_DATE) - integer ''3''),
        (112, 4, ''С-1-211-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 11690, ''Атяшево'', true, date (CURRENT_DATE) - integer ''3''),
        (112, 5, ''В-1-211-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 11690, ''Атяшево'', true, date (CURRENT_DATE) - integer ''3''),

        (113, 1, ''Ц-1-212-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 34400, ''Русское море'', true, date (CURRENT_DATE) - integer ''3''),  --   265.3 -> 344
        (113, 2, ''Ю-1-212-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 34400, ''Русское море'', true, date (CURRENT_DATE) - integer ''3''),
        (113, 3, ''З-1-212-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 34400, ''Русское море'', true, date (CURRENT_DATE) - integer ''3''),
        (113, 4, ''С-1-212-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 34400, ''Русское море'', true, date (CURRENT_DATE) - integer ''3''),
        (113, 5, ''В-1-212-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 34400, ''Русское море'', true, date (CURRENT_DATE) - integer ''3''),

        (114, 1, ''Ц-1-213-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 13100, ''Русское море'', true, date (CURRENT_DATE) - integer ''3''),  --   105.7 -> 131
        (114, 2, ''Ю-1-213-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 13100, ''Русское море'', true, date (CURRENT_DATE) - integer ''3''),
        (114, 3, ''З-1-213-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 13100, ''Русское море'', true, date (CURRENT_DATE) - integer ''3''),
        (114, 4, ''С-1-213-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 13100, ''Русское море'', true, date (CURRENT_DATE) - integer ''3''),
        (114, 5, ''В-1-213-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 13100, ''Русское море'', true, date (CURRENT_DATE) - integer ''3''),

        (115, 1, ''Ц-1-214-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 17710, ''Fish & More'', true, date (CURRENT_DATE) - integer ''3''),   --   177.1 -> 229
        (115, 2, ''Ю-1-214-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 17710, ''Fish & More'', true, date (CURRENT_DATE) - integer ''3''),
        (115, 3, ''З-1-214-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 17710, ''Fish & More'', true, date (CURRENT_DATE) - integer ''3''),
        (115, 4, ''С-1-214-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 17710, ''Fish & More'', true, date (CURRENT_DATE) - integer ''3''),
        (115, 5, ''В-1-214-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 17710, ''Fish & More'', true, date (CURRENT_DATE) - integer ''3''),

        (116, 1, ''Ц-1-215-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 50600, ''Agama'', true, date (CURRENT_DATE) - integer ''3''), --   580* 0.7 =506
        (116, 2, ''Ю-1-215-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 50600, ''Agama'', true, date (CURRENT_DATE) - integer ''3''),
        (116, 3, ''З-1-215-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 50600, ''Agama'', true, date (CURRENT_DATE) - integer ''3''),
        (116, 4, ''С-1-215-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 50600, ''Agama'', true, date (CURRENT_DATE) - integer ''3''),
        (116, 5, ''В-1-215-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 50600, ''Agama'', true, date (CURRENT_DATE) - integer ''3''),

        (117, 1, ''Ц-1-216-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 14980, ''Русское море'', true, date (CURRENT_DATE) - integer ''3''),  --   214* 0.7 = 149.8
        (117, 2, ''Ю-1-216-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 14980, ''Русское море'', true, date (CURRENT_DATE) - integer ''3''),
        (117, 3, ''З-1-216-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 14980, ''Русское море'', true, date (CURRENT_DATE) - integer ''3''),
        (117, 4, ''С-1-216-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 14980, ''Русское море'', true, date (CURRENT_DATE) - integer ''3''),
        (117, 5, ''В-1-216-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 14980, ''Русское море'', true, date (CURRENT_DATE) - integer ''3''),

        (118, 1, ''Ц-1-217-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 7630, ''Слобода'', true, date (CURRENT_DATE) - integer ''3''),    --   109* 0.7 = 76.3
        (118, 2, ''Ю-1-217-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 7630, ''Слобода'', true, date (CURRENT_DATE) - integer ''3''),
        (118, 3, ''З-1-217-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 7630, ''Слобода'', true, date (CURRENT_DATE) - integer ''3''),
        (118, 4, ''С-1-217-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 7630, ''Слобода'', true, date (CURRENT_DATE) - integer ''3''),
        (118, 5, ''В-1-217-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 7630, ''Слобода'', true, date (CURRENT_DATE) - integer ''3''),

        (119, 1, ''Ц-1-218-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4330, ''Махеевъ'', true, date (CURRENT_DATE) - integer ''3''),    --   61.9* 0.7 =43.3
        (119, 2, ''Ю-1-218-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4330, ''Махеевъ'', true, date (CURRENT_DATE) - integer ''3''),
        (119, 3, ''З-1-218-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4330, ''Махеевъ'', true, date (CURRENT_DATE) - integer ''3''),
        (119, 4, ''С-1-218-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4330, ''Махеевъ'', true, date (CURRENT_DATE) - integer ''3''),
        (119, 5, ''В-1-218-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 4330, ''Махеевъ'', true, date (CURRENT_DATE) - integer ''3''),

        (120, 1, ''Ц-1-219-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6290, ''Кухмастер'', true, date (CURRENT_DATE) - integer ''3''),  --   82.9* 0.7 =62.9
        (120, 2, ''Ю-1-219-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6290, ''Кухмастер'', true, date (CURRENT_DATE) - integer ''3''),
        (120, 3, ''З-1-219-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6290, ''Кухмастер'', true, date (CURRENT_DATE) - integer ''3''),
        (120, 4, ''С-1-219-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6290, ''Кухмастер'', true, date (CURRENT_DATE) - integer ''3''),
        (120, 5, ''В-1-219-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6290, ''Кухмастер'', true, date (CURRENT_DATE) - integer ''3''),

        (121, 1, ''Ц-1-220-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6010, ''Heinz'', true, date (CURRENT_DATE) - integer ''3''),  --  85.9 * 0.7 = 60.1
        (121, 2, ''Ю-1-220-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6010, ''Heinz'', true, date (CURRENT_DATE) - integer ''3''),
        (121, 3, ''З-1-220-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6010, ''Heinz'', true, date (CURRENT_DATE) - integer ''3''),
        (121, 4, ''С-1-220-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6010, ''Heinz'', true, date (CURRENT_DATE) - integer ''3''),
        (121, 5, ''В-1-220-Л'', 100, 50, date (CURRENT_DATE) - integer ''3'', 6010, ''Heinz'', true, date (CURRENT_DATE) - integer ''3'');

END IF;
END;
' language plpgsql;


DO ' DECLARE
BEGIN
    IF NOT EXISTS (SELECT 1 FROM orders WHERE order_id = 1) THEN
        INSERT INTO orders(client_id, address, coordinates, warehouse_id, courier_id, status, date_start, date_end, overall_cost, promo_code_id, discount)
        VALUES
            -- заказы с центрального склада,
            (2, ''42-й квартал, район Арбат'', ST_SetSRID(ST_MakePoint(55.754582, 37.607513), 4326), 1, 52, ''DELIVERED'', CURRENT_DATE - integer ''3'' + time ''09:02'', CURRENT_DATE - integer ''3'' + time ''09:30'', 349.20, NULL, 0 ),
            (2, ''42-й квартал, район Арбат'', ST_SetSRID(ST_MakePoint(55.754582, 37.607515), 4326), 1, 52, ''CANCELLED'', CURRENT_DATE - integer ''3'' + time ''14:19'', CURRENT_DATE - integer ''3'' + time ''14:20'', 587.09, NULL, 0 ),
            (2, ''42-й квартал, район Арбат'', ST_SetSRID(ST_MakePoint(55.754582, 37.607512), 4326), 1, 52, ''DELIVERED'', CURRENT_DATE - integer ''3'' + time ''18:10'', CURRENT_DATE - integer ''3'' + time ''18:47'', 324.69, NULL, 0 ),
            (2, ''42-й квартал, район Арбат'', ST_SetSRID(ST_MakePoint(55.754583, 37.607513), 4326), 1, 54, ''DELIVERED'', CURRENT_DATE - integer ''2'' + time ''10:22'', CURRENT_DATE - integer ''2'' + time ''10:49'', 817.80, NULL, 0 ),
            (2, ''42-й квартал, район Арбат'', ST_SetSRID(ST_MakePoint(55.754582, 37.607513), 4326), 1, 54, ''DELIVERED'', CURRENT_DATE - integer ''2'' + time ''15:26'', CURRENT_DATE - integer ''2'' + time ''15:50'', 525.05, NULL, 0 ),
            (2, ''42-й квартал, район Арбат'', ST_SetSRID(ST_MakePoint(55.754582, 37.607513), 4326), 1, 55, ''DELIVERED'', CURRENT_DATE - integer ''2'' + time ''19:22'', CURRENT_DATE - integer ''2'' + time ''19:54'', 1019.40, NULL, 0),
            (2, ''42-й квартал, район Арбат'', ST_SetSRID(ST_MakePoint(55.754582, 37.607513), 4326), 1, 55, ''DELIVERED'', CURRENT_DATE - integer ''2'' + time ''22:01'', CURRENT_DATE - integer ''2'' + time ''22:40'', 1571.26, NULL, 0),
            (2, ''42-й квартал, район Арбат'', ST_SetSRID(ST_MakePoint(55.754582, 37.607513), 4326), 1, 53, ''DELIVERED'', CURRENT_DATE - integer ''1'' + time ''10:22'', CURRENT_DATE - integer ''1'' + time ''10:49'', 971.20, NULL, 0),
            (2, ''42-й квартал, район Арбат'', ST_SetSRID(ST_MakePoint(55.754582, 37.607513), 4326), 1, 53, ''DELIVERED'', CURRENT_DATE - integer ''1'' + time ''14:21'', CURRENT_DATE - integer ''1'' + time ''14:59'', 592.50, NULL, 0),
            (2, ''42-й квартал, район Арбат'', ST_SetSRID(ST_MakePoint(55.754582, 37.607513), 4326), 1, 53, ''DELIVERED'', CURRENT_DATE - integer ''1'' + time ''20:53'', CURRENT_DATE - integer ''1'' + time ''21:29'', 689.70, NULL, 0),

            (3, ''Берниковская набережная, 14'', ST_SetSRID(ST_MakePoint(55.748883, 37.652256), 4326), 1, 52, ''DELIVERED'', CURRENT_DATE - integer ''3'' + time ''09:32'', CURRENT_DATE - integer ''3'' + time ''09:57'', 447.80, NULL, 0),
            (4, ''Берниковская набережная, 14'', ST_SetSRID(ST_MakePoint(55.748884, 37.652253), 4326), 1, 53, ''DELIVERED'', CURRENT_DATE - integer ''3'' + time ''09:02'', CURRENT_DATE - integer ''3'' + time ''09:40'', 371.40, NULL, 0),
            (5, ''Берниковская набережная, 14'', ST_SetSRID(ST_MakePoint(55.748882, 37.652254), 4326), 1, 54, ''DELIVERED'', CURRENT_DATE - integer ''3'' + time ''08:59'', CURRENT_DATE - integer ''3'' + time ''09:30'', 607.20, NULL, 0),
            (6, ''Берниковская набережная, 14'', ST_SetSRID(ST_MakePoint(55.748886, 37.652255), 4326), 1, 55, ''DELIVERED'', CURRENT_DATE - integer ''3'' + time ''16:22'', CURRENT_DATE - integer ''3'' + time ''16:56'', 759.60, NULL, 0),
            (7, ''Берниковская набережная, 14'', ST_SetSRID(ST_MakePoint(55.748883, 37.652257), 4326), 1, 52, ''DELIVERED'', CURRENT_DATE - integer ''2'' + time ''10:00'', CURRENT_DATE - integer ''2'' + time ''10:30'', 457.20, NULL, 0),
            (8, ''Берниковская набережная, 14'', ST_SetSRID(ST_MakePoint(55.748885, 37.652258), 4326), 1, 53, ''DELIVERED'', CURRENT_DATE - integer ''2'' + time ''15:02'', CURRENT_DATE - integer ''2'' + time ''15:45'', 983.80, NULL, 0),
            (9, ''Берниковская набережная, 14'', ST_SetSRID(ST_MakePoint(55.748881, 37.652256), 4326), 1, 54, ''DELIVERED'', CURRENT_DATE - integer ''2'' + time ''11:03'', CURRENT_DATE - integer ''2'' + time ''11:32'', 527.40, NULL, 0),
            (10,''Берниковская набережная, 14'' ,ST_SetSRID(ST_MakePoint(55.748883, 37.652251), 4326) ,1 ,55 ,''DELIVERED'' ,CURRENT_DATE - integer ''1'' + time ''09:02'', CURRENT_DATE - integer ''1'' + time ''09:30'', 488.80, NULL, 0),
            (11,''Берниковская набережная, 14'' ,ST_SetSRID(ST_MakePoint(55.748884, 37.652252), 4326) ,1 ,52 ,''DELIVERED'' ,CURRENT_DATE - integer ''1'' + time ''20:02'', CURRENT_DATE - integer ''1'' + time ''20:38'', 674.80, 24, 10.0),
            (12,''Берниковская набережная, 14'' ,ST_SetSRID(ST_MakePoint(55.748883, 37.652253), 4326) ,1 ,53 ,''DELIVERED'' ,CURRENT_DATE - integer ''1'' + time ''11:04'', CURRENT_DATE - integer ''1'' + time ''11:29'', 802.40, NULL, 0),


            -- заказы с южного склада

            (33, ''Каширский проезд, 25к2'', ST_SetSRID(ST_MakePoint(55.754582, 37.607513), 4326), 2, NULL, ''CANCELLED'', CURRENT_DATE - integer ''3'' + time ''09:29'', CURRENT_DATE - integer ''3'' + time ''09:30'', 478.90, NULL, 0),
            (34, ''Каширский проезд, 25к2'', ST_SetSRID(ST_MakePoint(55.754583, 37.607516), 4326), 2, 56, ''DELIVERED'', CURRENT_DATE - integer ''3'' + time ''09:34'', CURRENT_DATE - integer ''3'' + time ''10:00'',   922.40, NULL, 0),
            (37, ''Каширский проезд, 25к2'', ST_SetSRID(ST_MakePoint(55.754582, 37.607518), 4326), 2, 56, ''DELIVERED'', CURRENT_DATE - integer ''3'' + time ''10:02'', CURRENT_DATE - integer ''3'' + time ''10:40'',   1316.40, NULL, 0),
            (36, ''Каширский проезд, 25к2'', ST_SetSRID(ST_MakePoint(55.754581, 37.607511), 4326), 2, 56, ''DELIVERED'', CURRENT_DATE - integer ''1'' + time ''12:22'', CURRENT_DATE - integer ''1'' + time ''12:50'',   398.10, NULL, 0),
            (37, ''Каширский проезд, 25к2'', ST_SetSRID(ST_MakePoint(55.754583, 37.607512), 4326), 2, 56, ''DELIVERED'', CURRENT_DATE - integer ''1'' + time ''12:55'', CURRENT_DATE - integer ''1'' + time ''13:30'',   537.0, NULL, 0),

            (38, ''Каширский проезд, 25к2'', ST_SetSRID(ST_MakePoint(55.754584, 37.607514), 4326), 2, 57, ''DELIVERED'', CURRENT_DATE - integer ''3'' + time ''12:05'', CURRENT_DATE - integer ''3'' + time ''12:36'', 1456.60, NULL, 0),
            (39, ''Каширский проезд, 25к2'', ST_SetSRID(ST_MakePoint(55.754585, 37.607515), 4326), 2, 57, ''DELIVERED'', CURRENT_DATE - integer ''3'' + time ''12:55'', CURRENT_DATE - integer ''3'' + time ''13:32'', 677.80, NULL, 0),
            (40, ''Каширский проезд, 25к2'', ST_SetSRID(ST_MakePoint(55.754582, 37.607514), 4326), 2, 57, ''DELIVERED'', CURRENT_DATE - integer ''2'' + time ''19:00'', CURRENT_DATE - integer ''2'' + time ''19:42'', 824.10, NULL, 0),
            (41, ''Каширский проезд, 25к2'', ST_SetSRID(ST_MakePoint(55.754581, 37.607511), 4326), 2, 57, ''DELIVERED'', CURRENT_DATE - integer ''1'' + time ''10:55'', CURRENT_DATE - integer ''1'' + time ''11:35'', 501.30, NULL, 0),
            (42, ''Каширский проезд, 25к2'', ST_SetSRID(ST_MakePoint(55.754586, 37.607510), 4326), 2, 57, ''DELIVERED'', CURRENT_DATE - integer ''1'' + time ''11:40'', CURRENT_DATE - integer ''1'' + time ''12:11'', 758.50, NULL, 0),

            (43, ''Каширский проезд, 25к2'', ST_SetSRID(ST_MakePoint(55.754584, 37.607515), 4326), 2, 58, ''DELIVERED'', CURRENT_DATE - integer ''3'' + time ''09:17'', CURRENT_DATE - integer ''3'' + time ''09:40'', 643.00, NULL, 0),
            (44, ''Каширский проезд, 25к2'', ST_SetSRID(ST_MakePoint(55.754581, 37.607516), 4326), 2, 58, ''DELIVERED'', CURRENT_DATE - integer ''3'' + time ''09:42'', CURRENT_DATE - integer ''3'' + time ''10:05'', 523.10, NULL, 0),
            (44, ''Каширский проезд, 25к2'', ST_SetSRID(ST_MakePoint(55.754583, 37.607511), 4326), 2, 58, ''DELIVERED'', CURRENT_DATE - integer ''3'' + time ''10:07'', CURRENT_DATE - integer ''3'' + time ''10:50'', 804.00, 24, 10.0),
            (46, ''Каширский проезд, 25к2'', ST_SetSRID(ST_MakePoint(55.754582, 37.607513), 4326), 2, 58, ''DELIVERED'', CURRENT_DATE - integer ''3'' + time ''10:53'', CURRENT_DATE - integer ''3'' + time ''11:29'', 583.80, NULL, 0),
            (47, ''Каширский проезд, 25к2'', ST_SetSRID(ST_MakePoint(55.754584, 37.607514), 4326), 2, 58, ''DELIVERED'', CURRENT_DATE - integer ''1'' + time ''19:07'', CURRENT_DATE - integer ''1'' + time ''19:47'', 746.10, NULL, 0),

            (48, ''Каширский проезд, 25к2'', ST_SetSRID(ST_MakePoint(55.754582, 37.607515), 4326), 2, 59, ''DELIVERED'', CURRENT_DATE - integer ''1'' + time ''08:57'', CURRENT_DATE - integer ''1'' + time ''09:30'', 731.50, NULL, 0),
            (49, ''Каширский проезд, 25к2'', ST_SetSRID(ST_MakePoint(55.754581, 37.607515), 4326), 2, 59, ''DELIVERED'', CURRENT_DATE - integer ''1'' + time ''09:34'', CURRENT_DATE - integer ''1'' + time ''10:01'', 1070.20, NULL, 0),
            (50, ''Каширский проезд, 25к2'', ST_SetSRID(ST_MakePoint(55.754583, 37.607516), 4326), 2, 59, ''DELIVERED'', CURRENT_DATE - integer ''1'' + time ''10:06'', CURRENT_DATE - integer ''1'' + time ''10:32'', 415.10, NULL, 0),
            (51, ''Каширский проезд, 25к2'', ST_SetSRID(ST_MakePoint(55.754584, 37.607511), 4326), 2, 59, ''DELIVERED'', CURRENT_DATE - integer ''1'' + time ''10:37'', CURRENT_DATE - integer ''1'' + time ''11:01'', 533.80, NULL, 0),
            (3 , ''Каширский проезд, 25к2'', ST_SetSRID(ST_MakePoint(55.754585, 37.607514), 4326), 2, 59, ''DELIVERED'', CURRENT_DATE - integer ''1'' + time ''11:02'', CURRENT_DATE - integer ''1'' + time ''11:39'', 432.80, NULL, 0),

            -- заказы с западного склада

            (4 , ''Новозаводская улица, 18с144'', ST_SetSRID(ST_MakePoint(55.757318, 37.482154), 4326), 3, 60, ''DELIVERED'', CURRENT_DATE - integer ''3'' + time ''09:01'', CURRENT_DATE - integer ''3'' + time ''09:30'', 535.90, NULL, 0),
            (5 , ''Новозаводская улица, 18с144'', ST_SetSRID(ST_MakePoint(55.757319, 37.482150), 4326), 3, 60, ''DELIVERED'', CURRENT_DATE - integer ''3'' + time ''09:32'', CURRENT_DATE - integer ''3'' + time ''09:59'', 480.80, NULL, 0),
            (6 , ''Новозаводская улица, 18с144'', ST_SetSRID(ST_MakePoint(55.757310, 37.482151), 4326), 3, 60, ''DELIVERED'', CURRENT_DATE - integer ''3'' + time ''10:02'', CURRENT_DATE - integer ''3'' + time ''10:39'', 586.70, NULL, 0),
            (7 , ''Новозаводская улица, 18с144'', ST_SetSRID(ST_MakePoint(55.757311, 37.482152), 4326), 3, 60, ''DELIVERED'', CURRENT_DATE - integer ''2'' + time ''16:02'', CURRENT_DATE - integer ''2'' + time ''16:45'', 751.90, NULL, 0),
            (8 , ''Новозаводская улица, 18с144'', ST_SetSRID(ST_MakePoint(55.757312, 37.482153), 4326), 3, 60, ''DELIVERED'', CURRENT_DATE - integer ''2'' + time ''16:48'', CURRENT_DATE - integer ''2'' + time ''17:36'', 492.0, NULL, 0),

            (9 , ''Новозаводская улица, 18с144'', ST_SetSRID(ST_MakePoint(55.757313, 37.482154), 4326), 3, 61, ''DELIVERED'', CURRENT_DATE - integer ''3'' + time ''10:22'', CURRENT_DATE - integer ''3'' + time ''10:58'', 643.70, NULL, 0),
            (10, ''Новозаводская улица, 18с144'', ST_SetSRID(ST_MakePoint(55.757314, 37.482155), 4326), 3, NULL, ''CANCELLED'', CURRENT_DATE - integer ''3'' + time ''11:02'', CURRENT_DATE - integer ''3'' + time ''11:04'', 606.70, NULL, 0),
            (11, ''Новозаводская улица, 18с144'', ST_SetSRID(ST_MakePoint(55.757315, 37.482156), 4326), 3, 61, ''DELIVERED'', CURRENT_DATE - integer ''3'' + time ''11:06'', CURRENT_DATE - integer ''3'' + time ''11:45'', 465.00, NULL, 0),
            (12, ''Новозаводская улица, 18с144'', ST_SetSRID(ST_MakePoint(55.757316, 37.482157), 4326), 3, 61, ''DELIVERED'', CURRENT_DATE - integer ''1'' + time ''21:02'', CURRENT_DATE - integer ''1'' + time ''21:40'', 707.70, NULL, 0),
            (13, ''Новозаводская улица, 18с144'', ST_SetSRID(ST_MakePoint(55.757317, 37.482158), 4326), 3, 61, ''DELIVERED'', CURRENT_DATE - integer ''1'' + time ''21:42'', CURRENT_DATE - integer ''1'' + time ''22:34'', 373.60, NULL, 0),

            (14, ''Новозаводская улица, 18с144'', ST_SetSRID(ST_MakePoint(55.757310, 37.482151), 4326), 3, 62, ''DELIVERED'', CURRENT_DATE - integer ''2'' + time ''20:25'', CURRENT_DATE - integer ''2'' + time ''20:56'', 1233.90, NULL, 0),
            (15, ''Новозаводская улица, 18с144'', ST_SetSRID(ST_MakePoint(55.757311, 37.482152), 4326), 3, 62, ''DELIVERED'', CURRENT_DATE - integer ''2'' + time ''21:00'', CURRENT_DATE - integer ''2'' + time ''21:39'', 748.70, 4, 50.0),
            (16, ''Новозаводская улица, 18с144'', ST_SetSRID(ST_MakePoint(55.757312, 37.482153), 4326), 3, 62, ''DELIVERED'', CURRENT_DATE - integer ''1'' + time ''13:22'', CURRENT_DATE - integer ''1'' + time ''13:59'', 474.60, NULL, 0),
            (17, ''Новозаводская улица, 18с144'', ST_SetSRID(ST_MakePoint(55.757313, 37.482154), 4326), 3, 62, ''DELIVERED'', CURRENT_DATE - integer ''1'' + time ''15:01'', CURRENT_DATE - integer ''1'' + time ''15:48'', 721.40, 5, 50.0),
            (18, ''Новозаводская улица, 18с144'', ST_SetSRID(ST_MakePoint(55.757314, 37.482155), 4326), 3, 62, ''DELIVERED'', CURRENT_DATE - integer ''1'' + time ''15:50'', CURRENT_DATE - integer ''1'' + time ''16:34'', 972.40, NULL, 0),

            (19, ''Новозаводская улица, 18с144'', ST_SetSRID(ST_MakePoint(55.757315, 37.482156), 4326), 3, 63, ''DELIVERED'', CURRENT_DATE - integer ''2'' + time ''16:43'', CURRENT_DATE - integer ''2'' + time ''17:20'', 370.60, 6, 50.0),
            (20, ''Новозаводская улица, 18с144'', ST_SetSRID(ST_MakePoint(55.757316, 37.482157), 4326), 3, 63, ''DELIVERED'', CURRENT_DATE - integer ''2'' + time ''17:22'', CURRENT_DATE - integer ''2'' + time ''17:58'', 544.90, NULL, 0),
            (21, ''Новозаводская улица, 18с144'', ST_SetSRID(ST_MakePoint(55.757317, 37.482158), 4326), 3, 63, ''DELIVERED'', CURRENT_DATE - integer ''2'' + time ''18:00'', CURRENT_DATE - integer ''2'' + time ''18:46'', 793.80, 7, 50.0),
            (22, ''Новозаводская улица, 18с144'', ST_SetSRID(ST_MakePoint(55.757318, 37.482159), 4326), 3, 63, ''DELIVERED'', CURRENT_DATE - integer ''2'' + time ''20:01'', CURRENT_DATE - integer ''2'' + time ''20:34'', 1724.70, NULL, 0),
            (23, ''Новозаводская улица, 18с144'', ST_SetSRID(ST_MakePoint(55.757319, 37.482150), 4326), 3, 63, ''DELIVERED'', CURRENT_DATE - integer ''2'' + time ''20:35'', CURRENT_DATE - integer ''2'' + time ''21:02'', 481.80, NULL, 0),

            -- заказы с северного склада

            (24, ''Олонецкая улица, 17'', ST_SetSRID(ST_MakePoint(55.853279, 37.618241), 4326), 4, 64, ''DELIVERED'', CURRENT_DATE - integer ''3'' + time ''12:10'', CURRENT_DATE - integer ''3'' + time ''12:48'', 405.69, 8, 50.0),
            (25, ''Олонецкая улица, 17'', ST_SetSRID(ST_MakePoint(55.853278, 37.618242), 4326), 4, 64, ''DELIVERED'', CURRENT_DATE - integer ''3'' + time ''12:51'', CURRENT_DATE - integer ''3'' + time ''13:30'', 897.60, NULL, 0),
            (26, ''Олонецкая улица, 17'', ST_SetSRID(ST_MakePoint(55.853277, 37.618243), 4326), 4, 64, ''DELIVERED'', CURRENT_DATE - integer ''3'' + time ''13:32'', CURRENT_DATE - integer ''3'' + time ''14:11'', 899.80, 9, 50.0),
            (27, ''Олонецкая улица, 17'', ST_SetSRID(ST_MakePoint(55.853276, 37.618244), 4326), 4, 64, ''DELIVERED'', CURRENT_DATE - integer ''2'' + time ''14:22'', CURRENT_DATE - integer ''2'' + time ''14:57'', 341.30, NULL, 0),
            (28, ''Олонецкая улица, 17'', ST_SetSRID(ST_MakePoint(55.853275, 37.618245), 4326), 4, 64, ''DELIVERED'', CURRENT_DATE - integer ''2'' + time ''14:59'', CURRENT_DATE - integer ''2'' + time ''15:45'', 830.50, 10, 50.0),

            (29, ''Олонецкая улица, 17'', ST_SetSRID(ST_MakePoint(55.853278, 37.618246), 4326), 4, 65, ''DELIVERED'', CURRENT_DATE - integer ''3'' + time ''16:12'', CURRENT_DATE - integer ''3'' + time ''16:56'', 1702.40, NULL, 0),
            (30, ''Олонецкая улица, 17'', ST_SetSRID(ST_MakePoint(55.853277, 37.618247), 4326), 4, 65, ''DELIVERED'', CURRENT_DATE - integer ''3'' + time ''16:59'', CURRENT_DATE - integer ''3'' + time ''17:41'', 468.60, 11, 50.0),
            (31, ''Олонецкая улица, 17'', ST_SetSRID(ST_MakePoint(55.853276, 37.618248), 4326), 4, 65, ''DELIVERED'', CURRENT_DATE - integer ''1'' + time ''09:42'', CURRENT_DATE - integer ''1'' + time ''10:29'', 845.70, NULL, 0),
            (32, ''Олонецкая улица, 17'', ST_SetSRID(ST_MakePoint(55.853275, 37.618249), 4326), 4, 65, ''DELIVERED'', CURRENT_DATE - integer ''1'' + time ''10:34'', CURRENT_DATE - integer ''1'' + time ''11:11'', 833.60, 12, 50.0),
            (33, ''Олонецкая улица, 17'', ST_SetSRID(ST_MakePoint(55.853274, 37.618240), 4326), 4, 65, ''DELIVERED'', CURRENT_DATE - integer ''1'' + time ''11:15'', CURRENT_DATE - integer ''1'' + time ''11:58'', 749.30, NULL, 0),

            (34, ''Олонецкая улица, 15'', ST_SetSRID(ST_MakePoint(55.852525, 37.617640), 4326), 4, 66, ''DELIVERED'', CURRENT_DATE - integer ''2'' + time ''11:12'', CURRENT_DATE - integer ''2'' + time ''11:57'', 1539.70, NULL, 0),
            (34, ''Олонецкая улица, 15'', ST_SetSRID(ST_MakePoint(55.852528, 37.617641), 4326), 4, 66, ''DELIVERED'', CURRENT_DATE - integer ''2'' + time ''12:02'', CURRENT_DATE - integer ''2'' + time ''12:49'', 535.30, 13, 50.0),
            (36, ''Олонецкая улица, 15'', ST_SetSRID(ST_MakePoint(55.852527, 37.617642), 4326), 4, 66, ''DELIVERED'', CURRENT_DATE - integer ''2'' + time ''12:50'', CURRENT_DATE - integer ''2'' + time ''13:37'', 361.60, NULL, 0),
            (37, ''Олонецкая улица, 15'', ST_SetSRID(ST_MakePoint(55.852526, 37.617643), 4326), 4, 66, ''DELIVERED'', CURRENT_DATE - integer ''2'' + time ''13:40'', CURRENT_DATE - integer ''2'' + time ''14:21'', 734.96, NULL, 0),
            (38, ''Олонецкая улица, 15'', ST_SetSRID(ST_MakePoint(55.852525, 37.617644), 4326), 4, 66, ''DELIVERED'', CURRENT_DATE - integer ''2'' + time ''14:24'', CURRENT_DATE - integer ''2'' + time ''15:10'', 426.41, NULL, 0),

            (39, ''Олонецкая улица, 15'', ST_SetSRID(ST_MakePoint(55.852524, 37.617645), 4326), 4, 67, ''DELIVERED'', CURRENT_DATE - integer ''3'' + time ''18:12'', CURRENT_DATE - integer ''3'' + time ''18:50'', 892.10, 14, 50.0),
            (40, ''Олонецкая улица, 15'', ST_SetSRID(ST_MakePoint(55.852523, 37.617646), 4326), 4, 67, ''DELIVERED'', CURRENT_DATE - integer ''3'' + time ''18:52'', CURRENT_DATE - integer ''3'' + time ''19:36'', 367.90, NULL, 0),
            (41, ''Олонецкая улица, 15'', ST_SetSRID(ST_MakePoint(55.852522, 37.617647), 4326), 4, 67, ''DELIVERED'', CURRENT_DATE - integer ''3'' + time ''19:39'', CURRENT_DATE - integer ''3'' + time ''20:26'', 922.70, NULL, 0),
            (42, ''Олонецкая улица, 15'', ST_SetSRID(ST_MakePoint(55.852521, 37.617648), 4326), 4, 67, ''CANCELLED'', CURRENT_DATE - integer ''3'' + time ''20:29'', CURRENT_DATE - integer ''3'' + time ''20:44'', 378.60, NULL, 0),
            (43, ''Олонецкая улица, 15'', ST_SetSRID(ST_MakePoint(55.852520, 37.617640), 4326), 4, 67, ''DELIVERED'', CURRENT_DATE - integer ''3'' + time ''20:53'', CURRENT_DATE - integer ''3'' + time ''21:35'', 393.10, NULL, 0),

            -- заказы с восточного склада

            (44, ''Свободный проспект, 6к3'', ST_SetSRID(ST_MakePoint(55.758412, 37.816477), 4326), 5, NULL, ''CANCELLED'', CURRENT_DATE - integer ''3'' + time ''10:32'', CURRENT_DATE - integer ''3'' + time ''10:33'', 555.50, 15, 50.0),
            (46, ''Свободный проспект, 6к3'', ST_SetSRID(ST_MakePoint(55.758413, 37.816478), 4326), 5, 68,   ''DELIVERED'', CURRENT_DATE - integer ''3'' + time ''10:23'', CURRENT_DATE - integer ''3'' + time ''11:01'', 1249.40, NULL, 0),
            (47, ''Свободный проспект, 6к3'', ST_SetSRID(ST_MakePoint(55.758414, 37.816479), 4326), 5, 68,   ''DELIVERED'', CURRENT_DATE - integer ''3'' + time ''11:03'', CURRENT_DATE - integer ''3'' + time ''11:49'', 347.30, NULL, 0),
            (48, ''Свободный проспект, 6к3'', ST_SetSRID(ST_MakePoint(55.758415, 37.816470), 4326), 5, 68,   ''DELIVERED'', CURRENT_DATE - integer ''1'' + time ''16:13'', CURRENT_DATE - integer ''1'' + time ''16:46'', 594.00, NULL, 0),
            (49, ''Свободный проспект, 6к3'', ST_SetSRID(ST_MakePoint(55.758416, 37.816471), 4326), 5, 68,   ''DELIVERED'', CURRENT_DATE - integer ''1'' + time ''16:49'', CURRENT_DATE - integer ''1'' + time ''17:30'', 373.70, 16, 50.0),

            (50, ''Свободный проспект, 6к3'', ST_SetSRID(ST_MakePoint(55.758417, 37.816472), 4326), 5, 69, ''DELIVERED'', CURRENT_DATE - integer ''3'' + time ''19:31'', CURRENT_DATE - integer ''3'' + time ''20:13'', 564.70, NULL, 0),
            (51, ''Свободный проспект, 6к3'', ST_SetSRID(ST_MakePoint(55.758418, 37.816473), 4326), 5, 69, ''DELIVERED'', CURRENT_DATE - integer ''3'' + time ''20:17'', CURRENT_DATE - integer ''3'' + time ''20:52'', 530.90, 24, 10.0),
            (3 , ''Свободный проспект, 6к3'', ST_SetSRID(ST_MakePoint(55.758419, 37.816474), 4326), 5, 69, ''DELIVERED'', CURRENT_DATE - integer ''2'' + time ''11:32'', CURRENT_DATE - integer ''2'' + time ''12:20'', 571.10, NULL, 0),
            (4 , ''Свободный проспект, 6к3'', ST_SetSRID(ST_MakePoint(55.758410, 37.816475), 4326), 5, 69, ''DELIVERED'', CURRENT_DATE - integer ''2'' + time ''12:24'', CURRENT_DATE - integer ''2'' + time ''13:04'', 650.80, NULL, 0),
            (5 , ''Свободный проспект, 6к3'', ST_SetSRID(ST_MakePoint(55.758411, 37.816476), 4326), 5, 69, ''DELIVERED'', CURRENT_DATE - integer ''2'' + time ''13:09'', CURRENT_DATE - integer ''2'' + time ''13:51'', 652.60, NULL, 0),

            (6 , ''Кетчерская улица, 10'', ST_SetSRID(ST_MakePoint(55.743758, 37.827537), 4326), 5, 70, ''DELIVERED'', CURRENT_DATE - integer ''3'' + time ''08:12'', CURRENT_DATE - integer ''3'' + time ''08:54'', 976.49, NULL, 0),
            (7 , ''Кетчерская улица, 10'', ST_SetSRID(ST_MakePoint(55.743759, 37.827538), 4326), 5, 70, ''DELIVERED'', CURRENT_DATE - integer ''3'' + time ''08:57'', CURRENT_DATE - integer ''3'' + time ''09:39'', 208.59, NULL, 0),
            (8 , ''Кетчерская улица, 10'', ST_SetSRID(ST_MakePoint(55.743750, 37.827539), 4326), 5, 70, ''DELIVERED'', CURRENT_DATE - integer ''3'' + time ''09:42'', CURRENT_DATE - integer ''3'' + time ''10:25'', 354.80, NULL, 0),
            (9 , ''Кетчерская улица, 10'', ST_SetSRID(ST_MakePoint(55.743751, 37.827530), 4326), 5, 70, ''DELIVERED'', CURRENT_DATE - integer ''1'' + time ''19:42'', CURRENT_DATE - integer ''1'' + time ''20:14'', 407.60, 24, 10.0),
            (10, ''Кетчерская улица, 10'', ST_SetSRID(ST_MakePoint(55.743752, 37.827531), 4326), 5, 70, ''DELIVERED'', CURRENT_DATE - integer ''1'' + time ''20:18'', CURRENT_DATE - integer ''1'' + time ''20:57'', 765.60, NULL, 0),

            (11, ''Кетчерская улица, 10'', ST_SetSRID(ST_MakePoint(55.743753, 37.827532), 4326), 5, 71, ''DELIVERED'', CURRENT_DATE - integer ''2'' + time ''13:22'', CURRENT_DATE - integer ''2'' + time ''13:56'', 580.10, NULL, 0),
            (12, ''Кетчерская улица, 10'', ST_SetSRID(ST_MakePoint(55.743754, 37.827533), 4326), 5, 71, ''DELIVERED'', CURRENT_DATE - integer ''2'' + time ''13:59'', CURRENT_DATE - integer ''2'' + time ''14:43'', 985.50, NULL, 0),
            (13, ''Кетчерская улица, 10'', ST_SetSRID(ST_MakePoint(55.743755, 37.827534), 4326), 5, 71, ''DELIVERED'', CURRENT_DATE - integer ''1'' + time ''13:01'', CURRENT_DATE - integer ''1'' + time ''13:54'', 529.40, NULL, 0),
            (14, ''Кетчерская улица, 10'', ST_SetSRID(ST_MakePoint(55.743756, 37.827535), 4326), 5, 71, ''DELIVERED'', CURRENT_DATE - integer ''1'' + time ''13:59'', CURRENT_DATE - integer ''1'' + time ''14:42'', 777.60, NULL, 0),
            (15, ''Кетчерская улица, 10'', ST_SetSRID(ST_MakePoint(55.743757, 37.827536), 4326), 5, 71, ''DELIVERED'', CURRENT_DATE - integer ''1'' + time ''14:45'', CURRENT_DATE - integer ''1'' + time ''15:33'', 374.50, NULL, 0);

    END IF;
END;
' language plpgsql;

DO ' DECLARE
BEGIN
IF NOT EXISTS (SELECT 1 FROM orders_product_positions WHERE order_product_position_id = 1) THEN
INSERT INTO orders_product_positions(order_id, product_position_id, amount, price)
    VALUES
        -- Заказы с центрального склада
        (1, 1, 1, 120.50),
        (1, 6, 1, 80.0),
        (1, 601, 1, 85.9),
        (1, 156, 2, 31.4),

        (2, 11, 1, 110.0),
        (2, 16, 1, 145.5),
        (2, 161, 1, 254.0),
        (2, 326, 1, 77.59),

        (3, 406, 1, 71.1),
        (3, 421, 1, 22.0),
        (3, 161, 1, 177.0),
        (3, 326, 1, 54.59),

        (4, 596, 1, 82.9),
        (4, 576, 1, 580.0),
        (4, 476, 1, 109.0),
        (4, 431, 1, 45.9),

        (5, 211, 1, 129.9),
        (5, 76, 1, 194.29),
        (5, 51, 1, 82.86),
        (5, 366, 1, 118.0),

        (6, 461, 1, 117.0),
        (6, 386, 1, 115.0),
        (6, 166, 1, 44.90),
        (6, 511, 1, 742.50),

        (7, 171, 2, 68.7),
        (7, 46, 3, 300.0),
        (7, 321, 1, 99.86),
        (7, 441, 1, 130.0),
        (7, 531, 1, 304.0),

        (8, 241, 1, 99.9),
        (8, 41, 1, 70.0),
        (8, 561, 1, 491.40),
        (8, 556, 1, 167.0),
        (8, 396, 1, 142.9),

        (9, 326, 1, 77.9),
        (9, 276, 1, 169.9),
        (9, 236, 1, 50.7),
        (9, 176, 1, 137.0),
        (9, 451, 1, 157.0),

        (10, 566, 1, 187.0),
        (10, 486, 1, 285.7),
        (10, 441, 1, 130.0),
        (10, 126, 1, 87.0),


        (11, 6, 2, 80.0),
        (11, 21, 1, 138.0),
        (11, 386, 1, 115.0),
        (11, 246, 1, 34.8),


        (12, 186, 1, 93.5),
        (12, 421, 3, 66.0),
        (12, 256, 1, 79.9),

        (13, 196, 1, 129.9),
        (13, 261, 1, 263.9),
        (13, 21, 1, 138.0),
        (13, 376, 1, 75.4),

        (14, 161, 1, 252.9),
        (14, 556, 1, 167.0),
        (14, 446, 2, 150.0),
        (14, 131, 1, 39.7),

        (15, 441, 1, 130.0),
        (15, 1, 1, 120.5),
        (15, 131, 1, 39.7),
        (15, 551, 1, 167.0),

        (16, 476, 1, 109.0),
        (16, 196, 1, 129.9),
        (16, 576, 1, 722.9),
        (16, 421, 1, 22.0),

        (17, 551, 1, 167.0),
        (17, 206, 1, 179.9),
        (17, 186, 1, 93.6),
        (17, 151, 1, 86.9),

        (18, 181, 1, 268.6),
        (18, 26, 1, 83.6),
        (18, 436, 1, 65.6),
        (18, 406, 1, 71.0),

        (19, 396, 1, 142.9),
        (19, 296, 1, 175.0),
        (19, 311, 1, 84.9),
        (19, 506, 1, 282.0),

        (20, 351, 1, 157.0),
        (20, 426, 1, 64.0),
        (20, 376, 1, 75.4),
        (20, 571, 2, 253.0),


        -- Заказы с южного склада
        (21, 367, 1, 118.0),
        (21, 82, 1, 190.0),
        (21, 407, 1, 71.0),
        (21, 322, 1, 99.9),

        (22, 527, 1, 381.0),
        (22, 382, 1, 132.9),
        (22, 277, 1, 169.9),
        (22, 542, 1, 238.60),

        (23, 517, 1, 726.60),
        (23, 552, 1, 167.60),
        (23, 527, 1, 381.0),
        (23, 102, 1, 41.20),

        (24, 122, 1, 87.10),
        (24, 242, 1, 99.9),
        (24, 17, 1, 145.5),
        (24, 437, 1, 65.6),

        (25, 317, 1, 119.9),
        (25, 307, 1, 98.6),
        (25, 72, 1, 188.6),
        (25, 197, 1, 129.9),

        (26, 537, 1, 550.0),
        (26, 517, 1, 726.6),
        (26, 407, 1, 71.0),
        (26, 587, 1, 109.0),

        (27, 572, 1, 253.0),
        (27, 532, 1, 304),
        (27, 67, 1, 82.9),
        (27, 137, 1, 37.9),

        (28, 42, 1, 70.0),
        (28, 482, 1, 500.0),
        (28, 127, 1, 87.10),
        (28, 557, 1, 167.0),

        (29, 452, 1, 157.1),
        (29, 427, 1, 64.3),
        (29, 12, 1, 110.0),
        (29, 277, 1, 169.9),

        (30, 162, 1, 252.9),
        (30, 122, 1, 87.1),
        (30, 447, 1, 150.0),
        (30, 182, 1, 268.5),

        (31, 437, 1, 65.0),
        (31, 357, 1, 271.0),
        (31, 337, 1, 157.1),
        (31, 192, 1, 149.9),

        (32, 232, 1, 55.0),
        (32, 352, 1, 157.1),
        (32, 412, 1, 59.0),
        (32, 162, 1, 252.0),

        (33, 572, 1, 252.0),
        (33, 542, 1, 238.0),
        (33, 452, 1, 157.0),
        (33, 557, 1, 167.0),

        (34, 142, 1, 51.9),
        (34, 257, 1, 79.9),
        (34, 552, 1, 167.0),
        (34, 487, 1, 285.0),

        (35, 222, 1, 30.8),
        (35, 182, 1, 268.5),
        (35, 532, 1, 304.0),
        (35, 342, 1, 142.8),

        (36, 507, 1, 282.0),
        (36, 297, 1, 175.0),
        (36, 152, 1, 86.0),
        (36, 72, 1, 188.5),

        (37, 517, 1, 726.6),
        (37, 397, 1, 142.8),
        (37, 332, 1, 141.8),
        (37, 412, 1, 59.0),

        (38, 412, 1, 59.0),
        (38, 342, 1, 142.8),
        (38, 212, 1, 129.8),
        (38, 27, 1, 83.5),

        (39, 142, 1, 51.9),
        (39, 572, 1, 253.0),
        (39, 252, 1, 119.9),
        (39, 477, 1, 109.0),

        (40, 277, 1, 169.9),
        (40, 57, 1, 70.0),
        (40, 27, 1, 83.0),
        (40, 202, 1, 109.9),

        (41, 357, 1, 271.0),
        (41, 47, 1, 100.0),
        (41, 257, 1, 79.9),
        (41, 227, 1, 85.0),

        (42, 57, 1, 70.0),
        (42, 572, 1, 253.0),
        (42, 327, 1, 77.9),
        (42, 257, 1, 79.9),

        (43, 322, 1, 99.9),
        (43, 397, 1, 142.9),
        (43, 212, 1, 129.9),
        (43, 582, 1, 214.0),

        (44, 472, 1, 108.0),
        (44, 522, 1, 392.0),
        (44, 477, 1, 109.0),
        (44, 342, 1, 142.9),

        (45, 2, 1, 120.5),
        (45, 582, 1, 214.0),
        (45, 307, 1, 98.5),
        (45, 412, 1, 59.0),

        (46, 217, 1, 269.9),
        (46, 147, 1, 96.9),
        (46, 317, 1, 119.9),
        (46, 452, 1, 157.0),

        -- заказы с западного склада

        (47, 363, 1, 242.0),
        (47, 193, 1, 149.9),
        (47, 283, 1, 169.9),
        (47, 168, 1, 44.9),

        (48, 568, 1, 187.0),
        (48, 28, 1, 83.0),
        (48, 228, 1, 85.0),
        (48, 13, 1, 110.0),

        (49, 528, 1, 381.0),
        (49, 478, 1, 109.0),
        (49, 238, 1, 50.7),
        (49, 553, 1, 167.0),

        (50, 313, 1, 84.9),
        (50, 143, 1, 51.9),
        (50, 193, 1, 149.9),
        (50, 153, 1, 86.9),

        (51, 493, 1, 314.0),
        (51, 348, 1, 185.8),
        (51, 563, 1, 491.4),
        (51, 363, 1, 242.7),

        (52, 498, 1, 284.0),
        (52, 158, 1, 62.7),
        (52, 488, 1, 285.0),
        (52, 558, 1, 167.0),

        (53, 238, 1, 50.7),
        (53, 583, 1, 214.0),
        (53, 418, 1, 79.9),
        (53, 443, 1, 130.0),

        (54, 193, 1, 149.9),
        (54, 528, 1, 381.0),
        (54, 143, 1, 51.9),
        (54, 73, 1, 188.6),

        (55, 223, 1, 30.9),
        (55, 243, 1, 99.9),
        (55, 518, 1, 726.6),
        (55, 388, 1, 115.0),

        (56, 368, 1, 118.0),
        (56, 428, 1, 64.0),
        (56, 173, 1, 68.7),
        (56, 278, 1, 169.9),

        (57, 478, 1, 109.0),
        (57, 598, 1, 89.9),
        (57, 403, 1, 62.0),
        (57, 498, 1, 284.0),

        (58, 373, 1, 99.9),
        (58, 268, 1, 49.9),
        (58, 78, 1, 194.0),
        (58, 483, 1, 500.0),

        (59, 513, 1, 742.5),
        (59, 308, 1, 98.6),
        (59, 518, 1, 726.6),
        (59, 338, 1, 157.0),

        (60, 598, 1, 89.9),
        (60, 288, 1, 199.9),
        (60, 463, 1, 117.0),
        (60, 458, 1, 75.0),

        (61, 153, 1, 86.8),
        (61, 328, 1, 77.99),
        (61, 273, 1, 191.0),
        (61, 373, 1, 99.9),

        -- заказы с северного склада

        (62, 424, 1, 22.0),
        (62, 129, 1, 87.10),
        (62, 579, 1, 722.9),
        (62, 434, 1, 65.6),

        (63, 184, 1, 268.6),
        (63, 454, 1, 157.1),
        (63, 354, 1, 157.1),
        (63, 554, 1, 167.0),

        (64, 169, 1, 44.9),
        (64, 419, 1, 79.9),
        (64, 469, 1, 96.6),
        (64, 254, 1, 119.9),

        (65, 269, 1, 44.9),
        (65, 349, 1, 185.7),
        (65, 539, 1, 550.0),
        (65, 374, 1, 99.9),

        (66, 579, 1, 722.9),
        (66, 339, 1, 157.1),
        (66, 514, 1, 742.5),
        (66, 419, 1, 79.9),

        (67, 164, 1, 252.9),
        (67, 69, 1, 82.9),
        (67, 374, 1, 99.9),
        (67, 54, 1, 82.9),

        (68, 589, 1, 109.0),
        (68, 174, 1, 68.7),
        (68, 539, 1, 550.0),
        (68, 369, 1, 118.0),

        (69, 484, 1, 500.0),
        (69, 49, 1, 100.0),
        (69, 84, 1, 190.0),
        (69, 189, 1, 93.6),

        (70, 474, 1, 108.4),
        (70, 194, 1, 149.9),
        (70, 529, 1, 381.0),
        (70, 14, 1, 110.0),

        (71, 504, 1, 749.0),
        (71, 29, 1, 83.6),
        (71, 454, 1, 157.1),
        (71, 539, 1, 550.0),

        (72, 434, 1, 65.6),
        (72, 324, 1, 99.9),
        (72, 219, 1, 269.9),
        (72, 194, 1, 149.9),

        (73, 479, 1, 109.0),
        (73, 239, 1, 50.7),
        (73, 394, 1, 142.9),
        (73, 414, 1, 59.0),

        (74, 74, 1, 188.6),
        (74, 469, 1, 96.6),
        (74, 209, 1, 179.9),
        (74, 219, 1, 269.86),

        (75, 259, 1, 79.9),
        (75, 349, 1, 185.71),
        (75, 384, 1, 132.9),
        (75, 94, 1, 27.9),

        (76, 79, 1, 194.0),
        (76, 184, 1, 268.6),
        (76, 129, 1, 87.10),
        (76, 524, 1, 392.4),

        (77, 604, 1, 85.9),
        (77, 139, 1, 37.9),
        (77, 19, 1, 145.5),
        (77, 309, 1, 98.6),

        (78, 529, 1, 381.0),
        (78, 294, 1, 185.0),
        (78, 489, 1, 285.7),
        (78, 409, 1, 71.0),

        (79, 254, 1, 119.9),
        (79, 54, 1, 82.9),
        (79, 69, 1, 82.9),
        (79, 149, 1, 92.9),

        (80, 474, 1, 108.0),
        (80, 19, 1, 145.5),
        (80, 374, 1, 99.9),
        (80, 134, 1, 39.7),

        -- заказы с восточного склада

        (81, 380, 1, 75.4),
        (81, 295, 1, 185.0),
        (81, 220, 1, 269.9),
        (81, 460, 1, 75.2),

        (82, 365, 1, 242.7),
        (82, 80, 1, 194.2),
        (82, 520, 1, 726.6),
        (82, 605, 1, 85.9),

        (83, 120, 1, 87.1),
        (83, 15, 1, 110.0),
        (83, 590, 1, 109.0),
        (83, 110, 1, 41.2),

        (84, 575, 1, 253.0),
        (84, 280, 1, 169.9),
        (84, 110, 1, 41.2),
        (84, 200, 1, 129.9),

        (85, 375, 1, 99.9),
        (85, 200, 1, 129.9),
        (85, 480, 1, 109.0),
        (85, 315, 1, 84.9),

        (86, 325, 1, 99.9),
        (86, 425, 1, 22.0),
        (86, 490, 1, 285.7),
        (86, 185, 1, 157.1),

        (87, 290, 1, 199.9),
        (87, 390, 1, 115.0),
        (87, 415, 1, 59.0),
        (87, 560, 1, 167.0),

        (88, 285, 1, 169.9),
        (88, 355, 1, 157.1),
        (88, 105, 1, 41.2),
        (88, 90, 1, 202.9),

        (89, 45, 1, 70.0),
        (89, 150, 1, 96.9),
        (89, 535, 1, 304.0),
        (89, 210, 1, 179.9),

        (90, 545, 1, 238.6),
        (90, 535, 1, 304.0),
        (90, 100, 1, 30.0),
        (90, 10, 1, 80.0),

        (91, 260, 1, 79.9),
        (91, 185, 1, 268.6),
        (91, 330, 1, 77.99),
        (91, 540, 1, 550.0),

        (92, 330, 1, 77.99),
        (92, 100, 1, 30.0),
        (92, 140, 1, 37.9),
        (92, 160, 1, 62.7),

        (93, 175, 1, 68.7),
        (93, 390, 1, 115.0),
        (93, 200, 1, 129.9),
        (93, 110, 1, 41.2),

        (94, 215, 1, 129.9),
        (94, 605, 1, 85.9),
        (94, 195, 1, 149.9),
        (94, 145, 1, 51.9),

        (95, 220, 1, 269.9),
        (95, 210, 1, 179.9),
        (95, 145, 1, 51.9),
        (95, 265, 1, 263.9),

        (96, 310, 1, 98.6),
        (96, 175, 1, 68.7),
        (96, 400, 1, 142.9),
        (96, 220, 1, 269.9),

        (97, 435, 1, 65.6),
        (97, 100, 1, 30.0),
        (97, 580, 1, 722.9),
        (97, 555, 1, 167.0),

        (98, 80, 1, 194.3),
        (98, 180, 1, 137.1),
        (98, 10, 1, 80.0),
        (98, 370, 1, 118.0),

        (99, 550, 1, 298.5),
        (99, 10, 1, 80.0),
        (99, 495, 1, 314.2),
        (99, 315, 1, 84.9),

        (100, 335, 1, 141.8),
        (100, 160, 1, 62.7),
        (100, 55, 1, 82.9),
        (100, 130, 1, 87.1);

END IF;
END;
' language plpgsql;


DO ' DECLARE
BEGIN
IF NOT EXISTS (SELECT 1 FROM delivery_sessions WHERE delivery_session_id = 1) THEN
    INSERT INTO delivery_sessions(courier_id, start_time, end_time, orders_completed, average_time_per_order, money_earned)
        SELECT courier_id, date_start, date_end, 1, (date_end - date_start)::INTERVAL, ROUND(overall_cost / 10, 2) FROM orders WHERE (courier_id IS NOT NULL AND status = ''DELIVERED'') ORDER BY courier_id ASC;
    INSERT INTO delivery_sessions(courier_id, start_time, end_time, orders_completed, average_time_per_order, money_earned)
        SELECT courier_id, date_start, date_end, 1, (date_end - date_start)::INTERVAL, 0.0 FROM orders WHERE (courier_id IS NOT NULL AND status = ''CANCELLED'') ORDER BY courier_id ASC;
END IF;
END;
' language plpgsql;