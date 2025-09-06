package coding.challenges.redis.resp;

import coding.challenges.redis.resp.handlers.RespTypeHandler;
import coding.challenges.redis.resp.handlers.factory.RespTypeHandlerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RespSerializer {

    private final RespTypeHandlerFactory handlerFactory;

    public String serialize(Object input) {
        Class<?> type = input.getClass();
        RespTypeHandler handler = handlerFactory.getSerializationHandler(type);
        return handler.serialize(input);
    }
}
