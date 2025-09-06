package coding.challenges.redis.resp.handlers;

import coding.challenges.redis.resp.types.RespException;
import org.springframework.stereotype.Component;

@Component
public class ErrorHandler implements RespTypeHandler {


    @Override
    public String serialize(Object input) {
        RespException exception = (RespException) input;
        return "-" + exception.getMessage() + "\r\n";
    }

    @Override
    public Object deserialize(String input) {
        return new RespException(input.substring(1, input.length() - 2));
    }

    @Override
    public boolean supportsSerialization(Class<?> type) {
        return RespException.class.isAssignableFrom(type);
    }

    @Override
    public boolean supportsDeserialization(char prefix) {
        return '-' == prefix;
    }
}
