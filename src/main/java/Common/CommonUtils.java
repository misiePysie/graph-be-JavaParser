package Common;

import static java.lang.Double.toHexString;

public class CommonUtils {

    public static String randomId(){
        return toHexString((Math.random()*((2.3*Math.pow(10,150)-0))+0));
    }

}
