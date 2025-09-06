package coding.challenges.redis.resp.handlers;

import org.springframework.stereotype.Component;

@Component
public class IntegerHandler implements RespTypeHandler {

    @Override
    public String serialize(Object input) {
        return ":" + input + "\r\n";
    }

    @Override
    public Object deserialize(String input) {
        return Long.parseLong(input.substring(1, input.length() - 2));
    }

    @Override
    public boolean supportsSerialization(Class<?> type) {
        return Long.class.isAssignableFrom(type) || long.class.isAssignableFrom(type);
    }

    @Override
    public boolean supportsDeserialization(char prefix) {
        return ':' == prefix;
    }
}
