/*
 * @author Loc Truong
 */
package json;
import java.text.*;
import java.util.*;
public class ReportObject {
    private final String RECORD_TYPE;
    private final String DATA_SET_NAME;
    private final Date PRODUCT_CREATION_TIME;
    public ReportObject(String type, String name, Date createdOn) throws ParseException{
        this.RECORD_TYPE = type;
        this.DATA_SET_NAME = name;
        this.PRODUCT_CREATION_TIME = createdOn;
    }
    @Override
    public String toString(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        return String.format("{\"RECORD_TYPE\":\"%s\",\"DATA_SET_NAME\":\"%s\",\"PRODUCT_CREATION_TIME\":\"%s\"}", RECORD_TYPE, DATA_SET_NAME, dateFormat.format(PRODUCT_CREATION_TIME));
    }
}
