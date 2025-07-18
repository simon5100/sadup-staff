package sadupstaff.service;

import org.springframework.stereotype.Component;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Component
public class UpdatingData {

    public <T>T updatingData(T old, T update, Class<T> clazz){
        Field[] fields = clazz.getDeclaredFields();
        for(Field field: fields) {
            field.setAccessible(true);
            try {
                if (field.get(update) == null ||
                        field.getName().equals("id") ||
                        field.getName().equals("createdAt") ||
                        field.getName().equals("updatedAt")) {
                    continue;
                }
                String valueS = field.getName();
                Method method = clazz.getMethod("set" + valueS.toUpperCase().charAt(0) + valueS.substring(1), field.getType());
                method.invoke(old, field.get(update));
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        return old;
    }
}
