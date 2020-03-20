package antessio.dynamoplus.json;


import antessio.dynamoplus.json.exception.JsonParsingException;
import antessio.dynamoplus.sdk.PaginatedResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
