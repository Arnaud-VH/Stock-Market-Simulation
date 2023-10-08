package nl.rug.aoop.messagequeue;

import lombok.Getter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Message class. Messages go into Message queues.
 */
@Slf4j
@Getter
public class Message implements Comparable<Message> {
    private final String header;
    private final String body;
    private final LocalDateTime timestamp;
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Message.class, new JsonMessageAdapter().nullSafe())
            .create();

    /**
     * Constructor for the Messages.
     * @param messageHeader The header (Title) of the message.
     * @param messageBody   The text content of the message.
     */
    public Message(String messageHeader, String messageBody) {
        this.header = messageHeader;
        this.body = messageBody;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Constructor for messages to specify custom timestamp.
     * @param messageHeader    The header (Title) of the message.
     * @param messageBody      The text content of the message.
     * @param messageTimestamp The timestamp of the message.
     */
    public Message(String messageHeader, String messageBody, LocalDateTime messageTimestamp) {
        this.header = messageHeader;
        this.body = messageBody;
        this.timestamp = messageTimestamp;
    }

    /**
     * Converts a JSON string into a message object.
     * @param json the JSON String
     * @return Returns a Message object.
     */
    public static Message fromJson(String json) {
        if (json == null) {
            log.error("converted null json string to message object");
            return null;
        }
        return GSON.fromJson(json, Message.class);
    }

    /**
     * Converts Message into JSON String.
     * @return Returns JSON String.
     */
    public String toJson() {
        return GSON.toJson(this);
    }

    @Override
    public int compareTo(Message o) {
        return this.timestamp.compareTo(o.timestamp);
    }

    private static final class JsonMessageAdapter extends TypeAdapter<Message> {
        public static final String HEADER_FIELD = "Header";
        public static final String BODY_FIELD = "Body";
        public static final String TIMESTAMP_FIELD = "Time";

        @Override
        public void write(JsonWriter writer, Message message) throws IOException {
            writer.beginObject();
            writer.name(HEADER_FIELD);
            writer.value(message.header);
            writer.name(BODY_FIELD);
            writer.value(message.body);
            writer.name(TIMESTAMP_FIELD);
            writer.value(message.timestamp.toString());
            writer.endObject();
        }

        @Override
        public Message read(JsonReader reader) throws IOException {
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
                    case HEADER_FIELD:
                        header = reader.nextString();
                        break;
                    case BODY_FIELD:
                        body = reader.nextString();
                        break;
                    case TIMESTAMP_FIELD:
                        timestamp = LocalDateTime.parse(reader.nextString());
                        break;
                }
            }
            reader.endObject();
            return new Message(header,body,timestamp);
        }
    }
}
