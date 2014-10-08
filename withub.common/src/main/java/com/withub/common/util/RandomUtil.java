package com.withub.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 随机数工具类.
 */
public final class RandomUtil {

    public static List<Integer> getUniqueRandom(final int begin, final int end, final int count) {

        List<Integer> returnList = new ArrayList<Integer>();

        List<Integer> allArray = new ArrayList<Integer>();

        for (int i = begin; i <= end; i++) {
            allArray.add(i);
        }

        Random random = new Random();
        String randomIndex = ",";

        for (int i = 0; i < count; i++) {
            boolean findNext = true;
            while (findNext) {
                int j = random.nextInt(end) + 1;
                if (randomIndex.indexOf("," + j + ",") < 0) {
                    findNext = false;
                    randomIndex += j + ",";
                    returnList.add(allArray.get(j - 1));
                }
            }
        }

        return returnList;
    }
}
