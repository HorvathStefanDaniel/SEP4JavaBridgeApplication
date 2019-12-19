import LoRa.JSON;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Testing {

    @Test
    public static void decodeJsonFile(){
        JSON json = new JSON();
        String jString = " {  \n" +
                "        \"EUI\":       \"asdfg2134=\",   \n" +
                "        \"data\":    018200f1019a  \n" +
                "    }";

        String hex = "018200f1019a";

        String data[] = json.getDataFromJSON(jString);

        assertEquals("38.6",data[0]);
        assertEquals("24.1",data[1]);
        assertEquals("410",data[2]);
    }


    //0182 00f1 019a   hex
    ////  386 241 410     decimal

    public static void main(String[] args) {
        decodeJsonFile();
    }
}
