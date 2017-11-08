package malakoff.dykh.Factory;

import malakoff.dykh.BackEnd.GsonEventFactory;
import malakoff.dykh.Event.EventManager;

/**
 * Created by user on 21/06/2016.
 */

public class FactoryManagers {

    private static GsonEventFactory gsonEventFactory = new GsonEventFactory();
    private static EventManager mEventManager = new EventManager();


    private FactoryManagers(){}

    public static GsonEventFactory getGsonEventInstance(){
        return gsonEventFactory;
    }

    public static EventManager getmEventManagerInstance(){
        return mEventManager;
    }
}
