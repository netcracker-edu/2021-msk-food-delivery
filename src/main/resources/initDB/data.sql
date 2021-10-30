INSERT INTO test (id, title)
VALUES (1, 'test'), (2, 'test2'), (3, 'test3')
ON CONFLICT DO NOTHING;