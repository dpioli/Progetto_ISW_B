package persistenza;

import com.google.gson.*;
import com.google.gson.stream.*;

import applicazione.CategoriaComponent;
import applicazione.CategoriaFoglia;
import applicazione.CategoriaNonFoglia;

import java.io.IOException;

/**
 * TypeAdapter per la classe astratta CategoriaComponent introdotta con l'applicazione del pattern COMPOSITE.
 */
public class CategoriaComponentAdapter extends TypeAdapter<CategoriaComponent> {

    private final Gson gson = new Gson();

    @Override
    public void write(JsonWriter out, CategoriaComponent value) throws IOException {
        if (value instanceof CategoriaFoglia) {
            gson.toJson(value, CategoriaFoglia.class, out);
        } else if (value instanceof CategoriaNonFoglia) {
            gson.toJson(value, CategoriaNonFoglia.class, out);
        } else {
            throw new IllegalArgumentException("Tipo sconosciuto: " + value.getClass());
        }
    }

    @Override
    public CategoriaComponent read(JsonReader in) throws IOException {
        JsonObject obj = JsonParser.parseReader(in).getAsJsonObject();

        if (obj.has("figli")) {
            // Oggetto con figli: CategoriaNonFoglia
            return gson.fromJson(obj, CategoriaNonFoglia.class);
        } else {
            // Altrimenti: CategoriaFoglia
            return gson.fromJson(obj, CategoriaFoglia.class);
        }
    }
}

