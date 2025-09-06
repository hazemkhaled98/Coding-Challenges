package coding.challenges.redis.resp.handlers;

import coding.challenges.redis.resp.types.SimpleString;
import org.springframework.stereotype.Component;

@Component
public class SimpleStringHandler implements RespTypeHandler {


    @Override
    public String serialize(Object input) {
        return "+" + input + "\r\n";
    }

    @Override
    public Object deserialize(String input) {
        return new SimpleString(input.substring(1, input.length() - 2));
    }

    @Override
    public boolean supportsSerialization(Class<?> type) {
        return SimpleString.class.isAssignableFrom(type);
    }

    @Override
    public boolean supportsDeserialization(char prefix) {
        return '+' == prefix;
    }
}
