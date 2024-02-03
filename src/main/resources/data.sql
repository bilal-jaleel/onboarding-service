insert into rides(id, manufacturer, model) values
(1, 'Toyota', 'Supra'),
(2, 'Mitsubushi', 'Lancer'),
(3, 'Nissan', 'GTR')
ON CONFLICT(id) DO NOTHING;
