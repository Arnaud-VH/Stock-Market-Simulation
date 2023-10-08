package nl.rug.aoop.networking.NetworkMessage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * Message that gets sent across the network. Gets converted into a JSON String.
 */
@Slf4j
@Getter
public class NetworkMessage {
    private final String header;
    private final String body;
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(NetworkMessage.class, new JsonMessageAdapter().nullSafe())
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

    /**
     * Converts a Json String into a networkMessage object.
     * @param json The Json string that will be converted.
     * @return Returns the network Message object that is made.
     */
    public static NetworkMessage fromJson(String json) {
        if (json == null) {
            log.error("converted null json string to message object");
            return null;
        }
        return GSON.fromJson(json, NetworkMessage.class);
    }

    /**
     * Converts the network Message into a Json String.
     * @return Returns a JsonString.
     */
    public String toJson() {
        return GSON.toJson(this);
    }

    private static final class JsonMessageAdapter extends TypeAdapter<NetworkMessage> {
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
                    case HEADER_FIELD:
                        header = reader.nextString();
                        break;
                    case BODY_FIELD:
                        body = reader.nextString();
                        break;
                }
            }
            reader.endObject();
            return new NetworkMessage(header,body);
        }
    }
}
