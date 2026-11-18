# TRABAJO PRÁCTICO 1 — Spring Boot, Lombok, DTO, JPQL y API REST GET

Esta carpeta contiene **únicamente el primer trabajo** del PDF `CONSIGNA_TP_1_SPRING.pdf`.
Es un proyecto Maven independiente; NO requiere el otro ZIP.

## Consignas implementadas

1. Dependencia Lombok en `pom.xml`, entidades y superclases JPA en
   `src/main/java/com/example/facturaventa/model/`, usando `@Getter`, `@Setter`
   y `@EqualsAndHashCode(callSuper = true)` sin `@Data` en relaciones.
2. `FacturaReporteDTO` con **los siete campos** pedidos.
3. Consulta JPQL `SELECT new ... FacturaReporteDTO(...)`, `COALESCE`, `COUNT(d)`,
   `GROUP BY` y `LEFT JOIN` en `service/FacturaService.java`.
4. Generación de PDF y hoja Excel `.xlsx` desde la misma consulta, mediante
   OpenPDF y Apache POI en `service/ReporteExportService.java`.
5. API Spring Boot `GET /api/facturas` con filtros opcionales por fechas,
   estado y monto mínimo; JSON como salida.

No se incluyen aquí los formularios ni los métodos POST, PUT y DELETE, porque
**forman parte del trabajo práctico 2**, que se entrega en otra carpeta.

## Cómo ejecutarlo en Windows

Requisitos: Java JDK 17 o superior, conexión a Internet al descargar dependencias.
Desde PowerShell, en la carpeta donde está `pom.xml`:

```powershell
.\mvnw.cmd clean test
.\mvnw.cmd spring-boot:run
```

Abrir en el navegador:

- `http://localhost:8080/api/facturas`
- `http://localhost:8080/api/facturas?estado=EMITIDA&montoMinimo=1000`
- `http://localhost:8080/api/facturas?fechaDesde=2026-01-01&fechaHasta=2026-12-31`
- `http://localhost:8080/api/facturas/reporte.pdf`
- `http://localhost:8080/api/facturas/reporte.xlsx`

Los parámetros opcionales funcionan también en las dos exportaciones.
Los archivos se crean y descargan **al pedir la URL**; no son capturas ni
reportes estáticos empaquetados.

## Datos y persistencia

Por defecto arranca con **H2 local persistente** (archivo `facturas.mv.db`) y
crea dos facturas de demostración en la primera ejecución. No es necesario
instalar PostgreSQL para la demo. El perfil `postgres` usa la configuración
`src/main/resources/application-postgres.properties`.

## Qué mostrar al profesor

1. Entidades con Lombok y relaciones JPA seguras.
2. DTO de siete campos y `SELECT new` en la consulta JPQL.
3. Resultado JSON del endpoint GET con y sin filtros.
4. Archivos PDF y XLSX descargados con los mismos filtros.
5. Resultado de `mvnw.cmd clean test`.

**Nota:** la facturación es simulada y no se conecta con ARCA/AFIP.
**Validación:** la compilación y los tests Maven deben correrse en un equipo
con acceso a las dependencias; el empaquetado no equivale a esa verificación.
