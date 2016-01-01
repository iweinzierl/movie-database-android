package iweinzierl.github.com.moviedatabase.util;

import java.util.HashSet;
import java.util.Set;

public class CollectionUtils {

    public static <T> Set<T> extractIntersection(Set<T> left, Set<T> right) {
        Set<T> intersection = new HashSet<>();

        for (T itemLeft : left) {
            for (T itemRight : right) {
                if (itemLeft.equals(itemRight)) {
                    intersection.add(itemLeft);
                }
            }
        }

        return intersection;
    }
}
