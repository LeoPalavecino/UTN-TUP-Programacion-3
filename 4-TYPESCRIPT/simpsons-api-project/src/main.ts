// ==========================================
// 5.1 Interfaces y tipos
// ==========================================

export interface SimpsonCharacter {
  id: number;
  age: number | null;
  birthdate: string | null;
  gender: string;
  name: string;
  occupation: string;
  portrait_path: string;
  phrases: string[];
  status: string;
}

export interface IResponseApi {
  count: number;
  next: string | null;
  prev: string | null;
  pages: number;
  results: SimpsonCharacter[];
}

// ==========================================
// 5.2 Variables y constantes
// ==========================================

const API_BASE_URL: string = "https://thesimpsonsapi.com/api/characters";
const CDN_IMAGE_URL: string = "https://cdn.thesimpsonsapi.com/500";

// Elementos del DOM con su tipado correspondiente
const btnLoad = document.getElementById("btn-load") as HTMLButtonElement | null;
const loadingSection = document.getElementById("loading-section") as HTMLElement | null;
const errorMessage = document.getElementById("error-message") as HTMLDivElement | null;
const charactersContainer = document.getElementById("characters-container") as HTMLElement | null;

let errorTimeoutId: number | null = null;

// ==========================================
// 5.3 Funciones requeridas
// ==========================================

/**
 * Muestra el indicador de carga y oculta cualquier mensaje de error activo.
 */
function showLoading(): void {
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
function hideLoading(): void {
  if (loadingSection) {
    loadingSection.classList.add("hidden");
  }
}

/**
 * Muestra un mensaje de error y lo oculta automáticamente luego de 5 segundos.
 */
function showError(message: string): void {
  if (!errorMessage) return;

  errorMessage.textContent = message;
  errorMessage.classList.remove("hidden");

  // Si ya había un temporizador corriendo, lo reiniciamos
  if (errorTimeoutId !== null) {
    window.clearTimeout(errorTimeoutId);
  }

  errorTimeoutId = window.setTimeout(() => {
    errorMessage.classList.add("hidden");
    errorTimeoutId = null;
  }, 5000);
}

/**
 * Genera el elemento HTML con la tarjeta de un personaje individual.
 */
function createCharacterCard(character: SimpsonCharacter): HTMLElement {
  const card: HTMLDivElement = document.createElement("div");
  card.className = "character-card";

  // Imagen del personaje
  const img: HTMLImageElement = document.createElement("img");
  img.src = `${CDN_IMAGE_URL}${character.portrait_path}`;
  img.alt = character.name;
  img.className = "character-img";
  img.loading = "lazy";

  // Fallback si la imagen no carga o viene rota
  img.onerror = () => {
    img.src = "https://via.placeholder.com/200x200?text=No+Image";
  };

  // Nombre (h3)
  const name: HTMLHeadingElement = document.createElement("h3");
  name.className = "character-name";
  name.textContent = character.name;

  // Frase del personaje (toma la primera disponible o un texto por defecto)
  const quote: HTMLParagraphElement = document.createElement("p");
  quote.className = "character-quote";
  if (character.phrases && character.phrases.length > 0) {
    quote.textContent = `"${character.phrases[0]}"`;
  } else {
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
const renderCharacters = (characters: SimpsonCharacter[]): void => {
  if (!charactersContainer) return;

  // Remueve los personajes existentes
  charactersContainer.innerHTML = "";

  // Itera y añade cada tarjeta mediante appendChild
  characters.forEach((character: SimpsonCharacter) => {
    const cardElement = createCharacterCard(character);
    charactersContainer.appendChild(cardElement);
  });
};

/**
 * Consulta la API y delega el renderizado de datos.
 */
const fetchCharacters = async (): Promise<void> => {
  showLoading();

  try {
    const response: Response = await fetch(API_BASE_URL);

    // Verificación de status HTTP
    if (!response.ok) {
      throw new Error(`Error HTTP en la petición: status ${response.status}`);
    }

    // Parseo del JSON tipado con la interfaz creada
    const data: IResponseApi = await response.json();

    // Validación defensiva de datos
    if (!data.results || !Array.isArray(data.results)) {
      throw new Error("El formato de respuesta de la API no contiene la lista esperada.");
    }

    renderCharacters(data.results);
  } catch (error: unknown) {
    console.error("Detalle del error en fetchCharacters:", error);
    const errorMsg =
      error instanceof Error
        ? error.message
        : "Hubo un problema al cargar los personajes. Inténtalo nuevamente.";
    showError(errorMsg);
  } finally {
    hideLoading();
  }
};

// ==========================================
// 5.4 Event Listeners
// ==========================================

if (btnLoad) {
  btnLoad.addEventListener("click", () => {
    void fetchCharacters();
  });
}
