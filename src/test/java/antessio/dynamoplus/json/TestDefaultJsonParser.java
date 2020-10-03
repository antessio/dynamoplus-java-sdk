package antessio.dynamoplus.json;


import antessio.dynamoplus.json.exception.JsonParsingException;
import antessio.dynamoplus.sdk.PaginatedResult;
import antessio.dynamoplus.sdk.domain.conditions.Predicate;
import antessio.dynamoplus.sdk.domain.conditions.PredicateBuilder;
import antessio.dynamoplus.sdk.domain.system.index.Index;
import antessio.dynamoplus.sdk.domain.system.index.IndexBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class TestDefaultJsonParser {

    DefaultJsonParser defaultJsonParser;


    @BeforeEach
    void setUp() {
        defaultJsonParser = new DefaultJsonParser();
    }


    @Test
    void fromStringToCondition() throws JsonParsingException {
        //given
        //when
        Predicate result = defaultJsonParser.jsonStringToObject("{\"and\":[{\"eq\":{\"field_name\":\"field1\",\"value\":\"value1\"}},{\"eq\":{\"field_name\":\"field2\",\"value\":\"value2\"}}]}", Predicate.class);
        //then
        System.out.println("result = " + result);
    }

    @Test
    void parseIndex() throws JsonParsingException {
        //given
        //when
        Predicate condition = new PredicateBuilder()
                .withAnd(Arrays.asList(
                        new PredicateBuilder().withEq("field1", "value1"),
                        new PredicateBuilder().withEq("field2", "value2"),
                        new PredicateBuilder().withRange("field3", "value3", "value4")
                ));
        Index index = new IndexBuilder()
                .collection("example")
                .name("example_by_whatever")
                .orderingKey("field3")
                .createIndex();
        //then
        String indexStr = defaultJsonParser.objectToJsonString(index);
        System.out.println("indexStr = " + indexStr);
    }

    @Test
    void parseQuery() throws JsonParsingException {
        Predicate predicate = new PredicateBuilder()
                .withAnd(Arrays.asList(
                        new PredicateBuilder().withEq("field1", "value1"),
                        new PredicateBuilder().withEq("field2", "value2")
                ));
        String c = defaultJsonParser.objectToJsonString(predicate);
        System.out.println("c = " + c);
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
