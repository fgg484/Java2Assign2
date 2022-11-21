package application;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main {
    public static void main(String[] args) {
        String idstr = "";
        try {
            Method method = Server.class.getMethod("main", String[].class);
            method.invoke(null, (Object) new String[] {idstr});
            Method method1 = Player1.class.getMethod("main", String[].class);
            method1.invoke(null, (Object) new String[] {idstr});
            Method method2 = Player2.class.getMethod("main", String[].class);
            method2.invoke(null, (Object) new String[] {idstr});
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
