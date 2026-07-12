package org.dromara.mybatis.jpa.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConditionValueTest {

    private static final Logger logger = LoggerFactory.getLogger(ConditionValueTest.class);

    @Test
    public void valueOfList() {
        ArrayList<Integer> list =new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        logger.debug("{}", ConditionValue.getCollectionValues(list));
    }
    
    @Test
    public void valueOfArray() {
        Integer []array =new Integer[] {1,2,3,4,5};
        logger.debug("{}", ConditionValue.getCollectionValues(array));
    }
    
    @Test
    public void valueOfIterator() {
        ArrayList<String> list =new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        logger.debug("{}", ConditionValue.getCollectionValues(list));
    }
    
    @Test
    public void valueOfCollection() {
        ArrayList<String> list =new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        logger.debug("{}", ConditionValue.getCollectionValues(list));
    }
    
    @Test
    public void valueOfAsList() {
        List<String> list = new ArrayList<>(Arrays.asList("政治","数学"));
        logger.debug("{}", ConditionValue.getCollectionValues(list));
    }
    
    @Test
    public void valueOfOfList() {
        List<String> list = List.of("政治","化学");
        logger.debug("{}", ConditionValue.getCollectionValues(list));
    }
    
}