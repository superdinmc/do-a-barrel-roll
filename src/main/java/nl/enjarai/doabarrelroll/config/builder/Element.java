package nl.enjarai.doabarrelroll.config.builder;

import com.google.gson.JsonObject;

interface Element {
    String name();

    void encode(JsonObject parent);

    void decode(JsonObject parent);
}
