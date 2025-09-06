package coding.challenges.redis.resp.types;

public class RespException extends Exception {



    public RespException(String message) {
        super(message);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RespException other)) return false;
        return this.getMessage().equals(other.getMessage());
    }
}
