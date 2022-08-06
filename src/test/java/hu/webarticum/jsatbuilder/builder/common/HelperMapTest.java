package hu.webarticum.jsatbuilder.builder.common;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import hu.webarticum.jsatbuilder.builder.core.CollapseException;
import hu.webarticum.jsatbuilder.builder.core.Helper;
import hu.webarticum.jsatbuilder.builder.core.Variable;


class HelperMapTest {

    @Test
    void test() throws CollapseException {
        Variable variable_1_1 = new Variable();
        Variable variable_1_2 = new Variable();
        Variable variable_1_3 = new Variable();
        Helper helper_1 = new AnyHelper(variable_1_1, variable_1_2, variable_1_3).setLabel("Helper.1");
        
        Variable variable_2_1 = new Variable();
        Variable variable_2_2 = new Variable();
        Variable variable_2_3 = new Variable();
        Helper helper_2 = new AnyHelper(variable_2_1, variable_2_2, variable_2_3).setLabel("Helper.2");
        
        HelperMap<Integer> helperMap = new HelperMap<Integer>();
        
        assertThat(helperMap).isEmpty();
        
        helperMap.put(1, helper_1);
        helperMap.put(2, helper_2);

        assertThat(helperMap).hasSize(2);

        assertThat(helper_1.isRemoved()).isFalse();
        assertThat(helper_2.isRemoved()).isFalse();
        
        variable_1_2.remove();
        variable_2_2.remove();

        assertThat(helper_1.isRemoved()).isFalse();
        assertThat(helper_2.isRemoved()).isFalse();

        assertThat(helperMap).hasSize(2);

        variable_1_1.remove();
        variable_1_3.remove();

        assertThat(helper_1.isRemoved()).isTrue();
        assertThat(helper_2.isRemoved()).isFalse();

        assertThat(helperMap).hasSize(1);

        assertThat(helperMap).doesNotContainKey(1);
        assertThat(helperMap).doesNotContainValue(helper_1);
        assertThat(helperMap).containsKey(2);
        assertThat(helperMap).containsValue(helper_2);

        variable_2_1.remove();
        variable_2_3.remove();

        assertThat(helper_1.isRemoved()).isTrue();
        assertThat(helper_2.isRemoved()).isTrue();

        assertThat(helperMap).isEmpty();
    }

}
