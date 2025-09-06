package coding.challenges.redis.resp.handlers.factory;

import coding.challenges.redis.resp.handlers.RespTypeHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RespTypeHandlerFactory {
    private final List<RespTypeHandler> handlers;


    public RespTypeHandler getSerializationHandler(Class<?> type) {
        return handlers.stream()
                .filter(handler -> handler.supportsSerialization(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No handler found for type: " + type));
    }

    public RespTypeHandler getDeserializationHandler(char prefix) {
        return handlers.stream()
                .filter(handler -> handler.supportsDeserialization(prefix))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No handler found for prefix: " + prefix));
    }
}
