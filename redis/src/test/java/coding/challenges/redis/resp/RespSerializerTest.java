package coding.challenges.redis.resp;

import coding.challenges.redis.resp.types.BulkString;
import coding.challenges.redis.resp.types.RespArray;
import coding.challenges.redis.resp.types.RespException;
import coding.challenges.redis.resp.types.SimpleString;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RespSerializerTest {

    @Autowired
    private RespSerializer serializer;

    @Test
    void serializeSimpleString() {
        assertThat(serializer.serialize(new SimpleString("OK")))
                .isEqualTo("+OK\r\n");
    }

    @Test
    void serializeError() {
        assertThat(serializer.serialize(new RespException("ERR Invalid Input")))
                .isEqualTo("-ERR Invalid Input\r\n");
    }

    @ParameterizedTest(name = "{2}")
    @MethodSource("serializeIntegers")
    void serializeInteger(long input, String expected, String description) {
        assertThat(serializer.serialize(input)).isEqualTo(expected);
    }


    @ParameterizedTest(name = "{2}")
    @MethodSource("serializeBulkStrings")
    void serializeBulkString(String input, String expected, String description) {
        assertThat(serializer.serialize(new BulkString(input))).isEqualTo(expected);
    }


    @ParameterizedTest(name = "{2}")
    @MethodSource("serializeArray")
    void serializeArray(RespArray<?> input, String expected, String description) {
        assertThat(serializer.serialize(input))
                .isEqualTo(expected);
    }

    private static Stream<Arguments> serializeIntegers() {
        return Stream.of(
                Arguments.of(123L, ":123\r\n", "Positive integer"),
                Arguments.of(-123L, ":-123\r\n", "Negative integer")
        );
    }



    private static Stream<Arguments> serializeBulkStrings() {
        return Stream.of(
                Arguments.of("foo bar", "$7\r\nfoo bar\r\n", "Regular string"),
                Arguments.of("", "$0\r\n\r\n", "Empty String"),
                Arguments.of(null, "$-1\r\n", "Null")
        );
    }

    private static Stream<Arguments> serializeArray() {
        return Stream.of(

                Arguments.of(new RespArray<>(Collections.emptyList()), "*0\r\n", "Empty array"),


                Arguments.of(
                        new RespArray<>(List.of(new SimpleString("OK"))),
                        "*1\r\n+OK\r\n",
                        "Single simple string"
                ),
                Arguments.of(
                        new RespArray<>(List.of(new BulkString("hello"))),
                        "*1\r\n$5\r\nhello\r\n",
                        "Single bulk string"
                ),
                Arguments.of(
                        new RespArray<>(List.of(42L)),
                        "*1\r\n:42\r\n",
                        "Single integer"
                ),
                Arguments.of(
                        new RespArray<>(List.of(new RespException("ERR something went wrong"))),
                        "*1\r\n-ERR something went wrong\r\n",
                        "Single error"
                ),


                Arguments.of(
                        new RespArray<>(List.of(new SimpleString("OK"), new BulkString("world"))),
                        "*2\r\n+OK\r\n$5\r\nworld\r\n",
                        "Mixed simple string and bulk string"
                ),
                Arguments.of(
                        new RespArray<>(List.of(new BulkString("foo"), new BulkString("bar"), new BulkString("baz"))),
                        "*3\r\n$3\r\nfoo\r\n$3\r\nbar\r\n$3\r\nbaz\r\n",
                        "Multiple bulk strings"
                ),
                Arguments.of(
                        new RespArray<>(List.of(1L, 2L, 3L)),
                        "*3\r\n:1\r\n:2\r\n:3\r\n",
                        "Multiple integers"
                ),

                Arguments.of(
                        new RespArray<>(List.of(
                                new SimpleString("SET"),
                                new BulkString("key"),
                                new BulkString("value"),
                                100L
                        )),
                        "*4\r\n+SET\r\n$3\r\nkey\r\n$5\r\nvalue\r\n:100\r\n",
                        "Mixed types (SET command simulation)"
                ),

                Arguments.of(
                        new RespArray<>(List.of(new BulkString(null))),
                        "*1\r\n$-1\r\n",
                        "RespArray with null bulk string"
                ),
                Arguments.of(
                        new RespArray<>(List.of(new BulkString(""))),
                        "*1\r\n$0\r\n\r\n",
                        "RespArray with empty bulk string"
                ),
                Arguments.of(
                        new RespArray<>(List.of(new BulkString("hello\r\nworld"))),
                        "*1\r\n$12\r\nhello\r\nworld\r\n",
                        "Bulk string with CRLF in content"
                ),
                Arguments.of(
                        new RespArray<>(List.of(new BulkString("+OK-ERR:123$5*2"))),
                        "*1\r\n$15\r\n+OK-ERR:123$5*2\r\n",
                        "Bulk string with RESP-like prefixes"
                ),

                Arguments.of(
                        new RespArray<>(List.of(
                                new BulkString("outer"),
                                new RespArray<>(List.of(new BulkString("inner")))
                        )),
                        "*2\r\n$5\r\nouter\r\n*1\r\n$5\r\ninner\r\n",
                        "Nested array"
                ),
                Arguments.of(
                        new RespArray<>(List.of(
                                new RespArray<>(List.of(new BulkString("a"), new BulkString("b"))),
                                new RespArray<>(List.of(new BulkString("c"), new BulkString("d")))
                        )),
                        "*2\r\n*2\r\n$1\r\na\r\n$1\r\nb\r\n*2\r\n$1\r\nc\r\n$1\r\nd\r\n",
                        "Multiple nested arrays"
                ),

                Arguments.of(
                        new RespArray<>(List.of(0L)),
                        "*1\r\n:0\r\n",
                        "Zero integer"
                ),
                Arguments.of(
                        new RespArray<>(List.of(-1L)),
                        "*1\r\n:-1\r\n",
                        "Negative integer"
                )
        );
    }
}
