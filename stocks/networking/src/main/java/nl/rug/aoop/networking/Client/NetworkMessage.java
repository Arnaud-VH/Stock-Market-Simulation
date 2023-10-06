package nl.rug.aoop.networking.Client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.messagequeue.Message;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Getter
public class NetworkMessage {
    private final String header;
    private final String body;
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(NetworkMessage.class, new NetworkMessage.jsonMessageAdapter().nullSafe())
            .create();

    /**
     * Constructor for the Messages.
     * @param messageHeader The header (Title) of the message.
     * @param messageBody   The text content of the message.
     */
    public NetworkMessage(String messageHeader, String messageBody) {
        this.header = messageHeader;
        this.body = messageBody;
    }

    public static NetworkMessage fromJson(String json) {
        if (json == null) {
            log.error("converted null json string to message object");
            return null;
        }
        return gson.fromJson(json, NetworkMessage.class);
    }

    public String toJson() {
        return gson.toJson(this);
    }

    private static final class jsonMessageAdapter extends TypeAdapter<NetworkMessage> {
        public static final String HEADER_FIELD = "Header";
        public static final String BODY_FIELD = "Body";

        @Override
        public void write(JsonWriter writer, NetworkMessage message) throws IOException {
            writer.beginObject();
            writer.name(HEADER_FIELD);
            writer.value(message.header);
            writer.name(BODY_FIELD);
            writer.value(message.body);
            writer.endObject();
        }

        @Override
        public NetworkMessage read(JsonReader reader) throws IOException {
            reader.beginObject();
            String header = null;
            String body = null;
            LocalDateTime timestamp = null;
            while (reader.hasNext()) {
                JsonToken token = reader.peek();
                String fieldName = null;
                if (token.equals(JsonToken.NAME)) {
                    fieldName = reader.nextName();
                }
                if (fieldName == null) {
                    continue;
                }
                switch (fieldName) {
                    case HEADER_FIELD -> header = reader.nextString();
                    case BODY_FIELD -> body = reader.nextString();
                }
            }
            reader.endObject();
            return new NetworkMessage(header,body);
        }
    }
}
