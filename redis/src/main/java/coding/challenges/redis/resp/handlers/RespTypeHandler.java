package coding.challenges.redis.resp.handlers;

public interface RespTypeHandler {


    String serialize(Object input);

    Object deserialize(String input);

    boolean supportsSerialization(Class<?> type);

    boolean supportsDeserialization(char prefix);
}
