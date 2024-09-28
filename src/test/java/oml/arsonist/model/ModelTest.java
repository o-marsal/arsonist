package oml.arsonist.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

class ModelTest {

	@Test
    public void testToJson() {
        Model.Builder builder = new Model.Builder(2, 3);
        builder.setTime(1234);
        Model model = builder.build();

        String json = model.toJson();
        String expected = "{\"state\":\"STOPPED\",\"width\":2,\"height\":3,\"time\":1234,\"step\":0,\"grid\":\"TTTTTT\"}";
        Assertions.assertEquals(expected, json);
    }

}
