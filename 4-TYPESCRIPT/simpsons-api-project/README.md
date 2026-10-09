UTN - FRM - Tecnicatura en Programación - 2026
Comisión: 2 (Tarde) - Cátedra: PROG3
Alumno: Leonardo Palavecino - Legajo: 53994

Trabajo Práctico: Consumo de The Simpsons API con TypeScript

Objetivo:
Crear una aplicación web que consuma la API de Los Simpsons para mostrar personajes de la serie con sus imágenes y frases célebres.

Requisitos previos
● Node.js instalado (versión 14 o superior)
● Editor de código (VS Code recomendado)

Estructura del proyecto:
simpsons-api-project/
│
├── src/
│ ├── styles.css
│ └── main.ts
│—-index.html
├── package.json
├──tsconfig.json
└──README.md

Documentación de la API:

Nombre: The Simpsons Quote API
URL Base: https://thesimpsonsapi.com/api
Método: GET
Ejemplo de petición:
https://thesimpsonsapi.com/api/characters

Estructura de datos devuelta por la API:

La respuesta raíz de la API devuelve un objeto con metadatos de paginación y el arreglo de personajes:   
Campo       Tipo            Descripción
count       number          Cantidad total de personajes registrados en la API.   
next        string | null   URL de siguiente pág. de resultados(o null si es la última).   prev        string | null   URL de la pág. previa (o null si es la primera página).   
pages       number          Cantidad total de páginas disponibles.   
results     Array           Lista con los objetos individuales de cada personaje.   
        <SimpsonCharacter>

Respuestas a consignas del trabajo práctico:

Consigna 1.2: ¿Qué hace npm init -y y qué archivo genera?
Qué hace: Inicializa un nuevo proyecto Node.js configurando los valores predeterminados (nombre del proyecto tomado de la carpeta actual, versión 1.0.0, scripts básicos, autor vacío, licencia ISC, etc.) sin hacer preguntas interactivas en la terminal (la bandera -y significa yes a todo).

Qué archivo genera: Genera el archivo package.json, que es el manifiesto central del proyecto donde se registran las dependencias, metadatos y scripts ejecutables.

Consigna 1.3: Al utilizar la instrucción "npm install typescript --save-dev" ¿Por qué se usa --save-dev en lugar de una dependencia normal?
TypeScript es una herramienta de desarrollo y compilación, no de ejecución.
Los navegadores no entienden TypeScript nativamente; necesitan JavaScript plano. Por eso, el compilador tsc se usa únicamente en la máquina o entorno de build para validar tipos y transpilar el código a .js.

Una vez transpilado a JavaScript dentro de /dist, el paquete typescript no vuelve a intervenir en producción. Guardarlo con --save-dev evita empaquetar herramientas pesadas e innecesarias en los despliegues de producción.

Consigna 1.4: ¿Qué hacen strict, target y outDir en tsconfig.json?
target ("ES2020"): Define la versión de JavaScript a la cual se transpilará el código. ES2020 le indica a TypeScript que puede utilizar características modernas de ECMAScript (soporte nativo de async/await, BigInt, operador de encadenamiento opcional ?., etc.) soportadas por los navegadores actuales.

outDir ("./dist"): Especifica la carpeta de destino donde TypeScript colocará los archivos JavaScript transpilados (main.js). Esto mantiene separado el código fuente (src/) del código procesado listo para producción.

strict (true): Activa un conjunto amplio de reglas estrictas de comprobación de tipos (incluye noImplicitAny, strictNullChecks, strictFunctionTypes, etc.). Esto garantiza mayor seguridad frente a errores comunes como intentar acceder a propiedades de valores null o undefined.

Consigna 1.5: ¿Qué hacen npm run build y npm run watch?
build (tsc): Ejecuta una única pasada de compilación sobre todos los archivos de src/, genera la carpeta dist/ con el JavaScript y finaliza el proceso. Se usa para verificar errores de tipos o para preparar la versión final de despliegue.

watch (tsc --watch): Inicia el compilador en modo escucha permanente en segundo plano. Cada vez que se modifica y se guarda un archivo .ts, lo recompila automáticamente en milisegundos sin necesidad de relanzar el comando en la terminal.
