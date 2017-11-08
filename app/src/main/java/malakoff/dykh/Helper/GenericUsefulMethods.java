package malakoff.dykh.Helper;

import java.util.Random;

import malakoff.dykh.User.User;

/**
 * Created by user on 23/06/2016.
 */

public class GenericUsefulMethods {

    public static User isHistorian(String userId) {
        return new User();
    }

    public static long generateLongId() {

        return System.currentTimeMillis();
    }


}
