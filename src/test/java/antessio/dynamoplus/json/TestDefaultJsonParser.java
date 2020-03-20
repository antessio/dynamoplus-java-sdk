package antessio.dynamoplus.json;


import antessio.dynamoplus.authentication.provider.basic.BasicAuthCredentialsProvider;
import antessio.dynamoplus.http.HttpConfiguration;
import antessio.dynamoplus.http.unirest.UniRestSdkHttpClient;
import antessio.dynamoplus.json.exception.JsonParsingException;
import antessio.dynamoplus.sdk.*;
import antessio.dynamoplus.sdk.domain.system.collection.Collection;
import antessio.dynamoplus.sdk.domain.system.collection.CollectionBuilder;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.fail;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class TestDefaultJsonParser {

    DefaultJsonParser defaultJsonParser;


    @BeforeEach
    void setUp() {
        defaultJsonParser = new DefaultJsonParser();
    }

    @Test
    void parseCategory() throws JsonParsingException {
        //given
        String payload = "{\"data\": [{\"id\": \"9d88eb16-702d-4eef-8280-3d1e347f0333\", \"name\": \"Pulp\", \"creation_date_time\": \"2020-02-09T13:56:29.960417\"}], \"last_key\": null}";
        //when
        PaginatedResult<Category> result = defaultJsonParser.jsonStringToPaginatedResult(payload, Category.class);
        //then
        assertThat(result).matches(r -> r.getData().size() == 1, "doesn't match size");
        assertThat(result.getData().get(0)).matches(c -> c.getName().equals("Pulp"));
    }


    @Test
    void testUnirest() {
        SDK sdk = new SdkBuilder("http://localhost:3000",
                new UniRestSdkHttpClient(new HttpConfiguration(60000, 60000, 60000),
                        new BasicAuthCredentialsProvider("root", "12345"))
        ).build();
        Either<Collection, SdkException> r = sdk.createCollection(new CollectionBuilder()
                .name("mmmt")
                .idKey("mmmt")
                .createCollection());
        r.error().ifPresent(combineConsumers(Throwable::printStackTrace, (e) -> fail("test failed " + e.getMessage())));
        r.ok().ifPresent(combineConsumers(System.out::println, (c) -> assertThat(c).matches(coll -> coll.getName().equals("mmmt"))));
    }

    <T> Consumer<T> combineConsumers(Consumer<T>... consumers) {
        return o -> {
            for (Consumer<T> c : consumers) {
                c.accept(o);
            }
        };
    }

    class Category {
        private String id;
        private String name;

        public Category() {
        }

        public Category(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
