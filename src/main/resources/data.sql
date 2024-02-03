insert into vehicles(id, manufacturer, model, type) values
(1, 'Toyota', 'Etios', 'Sedan'),
(2, 'Toyota', 'Innova', 'XL'),
(3, 'Nissan', 'GTR', 'Sports')
ON CONFLICT(id) DO NOTHING;
