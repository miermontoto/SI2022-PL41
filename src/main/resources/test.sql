SELECT f.*, c.nombre AS curso_nombre, c.importe AS remuneracion, e.nombre AS nombre_entidad,
CASE WHEN sum(p.importe) IS NOT NULL THEN sum(p.importe) ELSE 0 END AS pagado
FROM factura AS f
INNER JOIN curso AS c ON f.curso_id = c.id
INNER JOIN entidad AS e ON c.entidad_id = e.id
LEFT JOIN pago AS p ON f.id = p.factura_id
GROUP BY f.id ORDER BY f.fecha;

