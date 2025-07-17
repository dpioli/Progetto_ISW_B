package persistenza;

import com.google.gson.*;
import applicazione.*;

import java.lang.reflect.Type;

/**
 * Classe a supporto della Deserializzazione di oggetti di tipo CategoriaComponent (classe astratta)
 */
public class CategoriaComponentDeserializer implements JsonDeserializer<CategoriaComponent> {

    @Override
    public CategoriaComponent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();

        if (obj.has("figli")) {
            return context.deserialize(obj, CategoriaNonFoglia.class);
        } else {
            return context.deserialize(obj, CategoriaFoglia.class);
        }
    }
}
