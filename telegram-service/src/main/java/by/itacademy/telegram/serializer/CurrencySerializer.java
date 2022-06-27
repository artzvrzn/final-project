package by.itacademy.telegram.serializer;

import by.itacademy.telegram.model.Currency;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class CurrencySerializer extends JsonSerializer<Currency> {

    @Override
    public void serialize(Currency currency, JsonGenerator jg, SerializerProvider sp) throws IOException {
        jg.writeString(currency.getId().toString());
    }
}
