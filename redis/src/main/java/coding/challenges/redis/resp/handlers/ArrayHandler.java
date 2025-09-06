package coding.challenges.redis.resp.handlers;

import coding.challenges.redis.resp.RespDeserializer;
import coding.challenges.redis.resp.RespSerializer;
import coding.challenges.redis.resp.types.RespArray;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class ArrayHandler implements RespTypeHandler{

    private final RespSerializer respSerializer;
    private final RespDeserializer respDeserializer;

    public ArrayHandler(@Lazy RespSerializer respSerializer, @Lazy RespDeserializer respDeserializer) {
        this.respSerializer = respSerializer;
        this.respDeserializer = respDeserializer;
    }

    @Override
    public String serialize(Object input) {
        RespArray<?> respArray = (RespArray<?>) input;
        if(respArray.elements() == null){
            return "*-1\r\n";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("*").append(respArray.size()).append("\r\n");
        for (Object item : respArray) {
            sb.append(respSerializer.serialize(item));
        }
        return sb.toString();
    }

    @Override
    public Object deserialize(String input) {

        if (input.startsWith("*-1")) {
            return new RespArray<>(null);
        }

        int firstCRLF = input.indexOf("\r\n");

        int size = Integer.parseInt(input.substring(1, firstCRLF));

        if (size == 0) {
            return new RespArray<>(Collections.emptyList());
        }

        Object[] elements = new Object[size];
        int currentPos = firstCRLF + 2; // Skip past "*{size}\r\n"

        for (int i = 0; i < size; i++) {

            int elementEnd = findElementEnd(input, currentPos);
            String elementData = input.substring(currentPos, elementEnd);

            elements[i] = respDeserializer.deserialize(elementData);

            currentPos = elementEnd;
        }

        return new RespArray<>(List.of(elements));
    }

    @Override
    public boolean supportsSerialization(Class<?> type) {
        return RespArray.class.isAssignableFrom(type);
    }

    @Override
    public boolean supportsDeserialization(char prefix) {
        return '*' == prefix;
    }

    private int findElementEnd(String input, int start) {
        char prefix = input.charAt(start);

        return switch (prefix) {
            case '+', '-', ':' -> {
                int simpleEnd = input.indexOf("\r\n", start);
                yield simpleEnd == -1 ? input.length() : simpleEnd + 2;
            }
            case '$' -> {
                int lengthEnd = input.indexOf("\r\n", start);
                String lengthStr = input.substring(start + 1, lengthEnd);
                int length = Integer.parseInt(lengthStr);

                if (length == -1) {
                    yield lengthEnd + 2;
                }

                int contentStart = lengthEnd + 2;
                yield contentStart + length + 2;
            }
            case '*' -> findNestedArrayEnd(input, start);
            default -> throw new IllegalArgumentException("Unknown RESP type prefix: " + prefix);
        };
    }

    private int findNestedArrayEnd(String input, int start) {
        int lengthEnd = input.indexOf("\r\n", start);
        if (lengthEnd == -1) {
            throw new IllegalArgumentException("Invalid array: missing length CRLF");
        }

        String lengthStr = input.substring(start + 1, lengthEnd);
        int arraySize = Integer.parseInt(lengthStr);

        if (arraySize == -1) {
            return lengthEnd + 2;
        }

        int currentPos = lengthEnd + 2;

        for (int i = 0; i < arraySize; i++) {
            currentPos = findElementEnd(input, currentPos);
        }

        return currentPos;
    }

}
