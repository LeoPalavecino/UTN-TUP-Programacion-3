"use strict";
// ==========================================
// 5.1 Interfaces y tipos
// ==========================================
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __generator = (this && this.__generator) || function (thisArg, body) {
    var _ = { label: 0, sent: function() { if (t[0] & 1) throw t[1]; return t[1]; }, trys: [], ops: [] }, f, y, t, g = Object.create((typeof Iterator === "function" ? Iterator : Object).prototype);
    return g.next = verb(0), g["throw"] = verb(1), g["return"] = verb(2), typeof Symbol === "function" && (g[Symbol.iterator] = function() { return this; }), g;
    function verb(n) { return function (v) { return step([n, v]); }; }
    function step(op) {
        if (f) throw new TypeError("Generator is already executing.");
        while (g && (g = 0, op[0] && (_ = 0)), _) try {
            if (f = 1, y && (t = op[0] & 2 ? y["return"] : op[0] ? y["throw"] || ((t = y["return"]) && t.call(y), 0) : y.next) && !(t = t.call(y, op[1])).done) return t;
            if (y = 0, t) op = [op[0] & 2, t.value];
            switch (op[0]) {
                case 0: case 1: t = op; break;
                case 4: _.label++; return { value: op[1], done: false };
                case 5: _.label++; y = op[1]; op = [0]; continue;
                case 7: op = _.ops.pop(); _.trys.pop(); continue;
                default:
                    if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) { _ = 0; continue; }
                    if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) { _.label = op[1]; break; }
                    if (op[0] === 6 && _.label < t[1]) { _.label = t[1]; t = op; break; }
                    if (t && _.label < t[2]) { _.label = t[2]; _.ops.push(op); break; }
                    if (t[2]) _.ops.pop();
                    _.trys.pop(); continue;
            }
            op = body.call(thisArg, _);
        } catch (e) { op = [6, e]; y = 0; } finally { f = t = 0; }
        if (op[0] & 5) throw op[1]; return { value: op[0] ? op[1] : void 0, done: true };
    }
};
Object.defineProperty(exports, "__esModule", { value: true });
// ==========================================
// 5.2 Variables y constantes
// ==========================================
var API_BASE_URL = "https://thesimpsonsapi.com/api/characters";
var CDN_IMAGE_URL = "https://cdn.thesimpsonsapi.com/500";
// Elementos del DOM con su tipado correspondiente
var btnLoad = document.getElementById("btn-load");
var loadingSection = document.getElementById("loading-section");
var errorMessage = document.getElementById("error-message");
var charactersContainer = document.getElementById("characters-container");
var errorTimeoutId = null;
// ==========================================
// 5.3 Funciones requeridas
// ==========================================
/**
 * Muestra el indicador de carga y oculta cualquier mensaje de error activo.
 */
function showLoading() {
    if (loadingSection) {
        loadingSection.classList.remove("hidden");
    }
    if (errorMessage) {
        errorMessage.classList.add("hidden");
    }
}
/**
 * Oculta el indicador de carga.
 */
function hideLoading() {
    if (loadingSection) {
        loadingSection.classList.add("hidden");
    }
}
/**
 * Muestra un mensaje de error y lo oculta automáticamente luego de 5 segundos.
 */
function showError(message) {
    if (!errorMessage)
        return;
    errorMessage.textContent = message;
    errorMessage.classList.remove("hidden");
    // Si ya había un temporizador corriendo, lo reiniciamos
    if (errorTimeoutId !== null) {
        window.clearTimeout(errorTimeoutId);
    }
    errorTimeoutId = window.setTimeout(function () {
        errorMessage.classList.add("hidden");
        errorTimeoutId = null;
    }, 5000);
}
/**
 * Genera el elemento HTML con la tarjeta de un personaje individual.
 */
function createCharacterCard(character) {
    var card = document.createElement("div");
    card.className = "character-card";
    // Imagen del personaje
    var img = document.createElement("img");
    img.src = "".concat(CDN_IMAGE_URL).concat(character.portrait_path);
    img.alt = character.name;
    img.className = "character-img";
    img.loading = "lazy";
    // Fallback si la imagen no carga o viene rota
    img.onerror = function () {
        img.src = "https://via.placeholder.com/200x200?text=No+Image";
    };
    // Nombre (h3)
    var name = document.createElement("h3");
    name.className = "character-name";
    name.textContent = character.name;
    // Frase del personaje (toma la primera disponible o un texto por defecto)
    var quote = document.createElement("p");
    quote.className = "character-quote";
    if (character.phrases && character.phrases.length > 0) {
        quote.textContent = "\"".concat(character.phrases[0], "\"");
    }
    else {
        quote.textContent = "Sin frases célebres disponibles.";
    }
    // Estructura de la tarjeta
    card.appendChild(img);
    card.appendChild(name);
    card.appendChild(quote);
    return card;
}
/**
 * Limpia el contenedor y renderiza la lista de personajes.
 */
var renderCharacters = function (characters) {
    if (!charactersContainer)
        return;
    // Remueve los personajes existentes
    charactersContainer.innerHTML = "";
    // Itera y añade cada tarjeta mediante appendChild
    characters.forEach(function (character) {
        var cardElement = createCharacterCard(character);
        charactersContainer.appendChild(cardElement);
    });
};
/**
 * Consulta la API y delega el renderizado de datos.
 */
var fetchCharacters = function () { return __awaiter(void 0, void 0, void 0, function () {
    var response, data, error_1, errorMsg;
    return __generator(this, function (_a) {
        switch (_a.label) {
            case 0:
                showLoading();
                _a.label = 1;
            case 1:
                _a.trys.push([1, 4, 5, 6]);
                return [4 /*yield*/, fetch(API_BASE_URL)];
            case 2:
                response = _a.sent();
                // Verificación de status HTTP
                if (!response.ok) {
                    throw new Error("Error HTTP en la petici\u00F3n: status ".concat(response.status));
                }
                return [4 /*yield*/, response.json()];
            case 3:
                data = _a.sent();
                // Validación defensiva de datos
                if (!data.results || !Array.isArray(data.results)) {
                    throw new Error("El formato de respuesta de la API no contiene la lista esperada.");
                }
                renderCharacters(data.results);
                return [3 /*break*/, 6];
            case 4:
                error_1 = _a.sent();
                console.error("Detalle del error en fetchCharacters:", error_1);
                errorMsg = error_1 instanceof Error
                    ? error_1.message
                    : "Hubo un problema al cargar los personajes. Inténtalo nuevamente.";
                showError(errorMsg);
                return [3 /*break*/, 6];
            case 5:
                hideLoading();
                return [7 /*endfinally*/];
            case 6: return [2 /*return*/];
        }
    });
}); };
// ==========================================
// 5.4 Event Listeners
// ==========================================
if (btnLoad) {
    btnLoad.addEventListener("click", function () {
        void fetchCharacters();
    });
}
