package coding.challenges.redis.resp.handlers;

import coding.challenges.redis.resp.types.BulkString;
import org.springframework.stereotype.Component;

@Component
public class BulkStringHandler implements RespTypeHandler{

    @Override
    public String serialize(Object input) {
        BulkString str = (BulkString) input;
        if (str.value() == null) {
            return "$-1\r\n";
        }
        return "$" + str.length() + "\r\n" + str.value() + "\r\n";
    }

    @Override
    public Object deserialize(String input) {
        if (input.startsWith("$-1")) {
            return new BulkString(null);
        }
        int firstCRLF = input.indexOf("\r\n");
        int length = Integer.parseInt(input.substring(1, firstCRLF));
        return new BulkString(input.substring(firstCRLF + 2, firstCRLF + 2 + length));
    }

    @Override
    public boolean supportsSerialization(Class<?> type) {
        return BulkString.class.isAssignableFrom(type);
    }

    @Override
    public boolean supportsDeserialization(char prefix) {
        return '$' == prefix;
    }
}
