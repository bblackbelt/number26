package number26.de.bitcoins.net;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import number26.de.bitcoins.model.PriceTrend;

/**
 * Created by emanuele on 02.06.16.
 */
public class PriceTrendDeserializer implements JsonDeserializer<List<PriceTrend>> {

    @Override
    public List<PriceTrend> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray values = json.getAsJsonObject().getAsJsonArray("values");
        Type listType = new TypeToken<List<PriceTrend>>() {}.getType();
        return new Gson().fromJson(values, listType);
    }
}
