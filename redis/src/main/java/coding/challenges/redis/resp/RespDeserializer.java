package coding.challenges.redis.resp;


import coding.challenges.redis.resp.handlers.RespTypeHandler;
import coding.challenges.redis.resp.handlers.factory.RespTypeHandlerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RespDeserializer {

    private final RespTypeHandlerFactory handlerFactory;

    public Object deserialize(String input) {
        char prefix = input.charAt(0);
        RespTypeHandler handler = handlerFactory.getDeserializationHandler(prefix);
        return handler.deserialize(input);
    }
}
