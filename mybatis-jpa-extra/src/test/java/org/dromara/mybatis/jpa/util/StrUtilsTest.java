package org.dromara.mybatis.jpa.util;

import org.junit.jupiter.api.Test;

public class StrUtilsTest {

	@Test
    void test() {
        String aaa = "app_version_fld";
        System.out.println(StrUtils.underlineToCamel(aaa));
        System.out.println(StrUtils.underlineToCamel2(aaa));
        aaa = "appVersionFld";
        System.out.println(StrUtils.camelToUnderline(aaa));
    }

}
