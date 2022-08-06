package hu.webarticum.jsatbuilder.builder.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import hu.webarticum.jsatbuilder.builder.common.DefaultViability;
import hu.webarticum.jsatbuilder.solver.core.Solver;

class CoreTest {

    @Test
    void testCollapse() throws CollapseException {
        Variable variable1 = new Variable();
        Variable variable2 = new Variable();
        Variable variable3 = new Variable();

        Constraint constraint1 = new TestConstraint(variable1, false);
        Constraint constraint2 = new TestConstraint(variable2, true);

        assertThat(variable1.isRemoved()).isFalse();
        assertThat(variable2.isRemoved()).isFalse();
        assertThat(variable3.isRemoved()).isFalse();
        assertThat(constraint1.isRemoved()).isFalse();
        assertThat(constraint2.isRemoved()).isFalse();
        
        
        variable1.remove();

        assertThat(variable1.isRemoved()).isTrue();
        assertThat(variable2.isRemoved()).isFalse();
        assertThat(variable3.isRemoved()).isFalse();
        assertThat(constraint1.isRemoved()).isTrue();
        assertThat(constraint2.isRemoved()).isFalse();
        
        assertThatThrownBy(() -> variable2.remove())
            .isInstanceOf(CollapseException.class)
            .extracting(e -> ((CollapseException)e).getConstraint())
            .isSameAs(constraint2)
        ;
        
        assertThat(variable1.isRemoved()).isTrue();
        assertThat(variable2.isRemoved()).isTrue();
        assertThat(variable3.isRemoved()).isFalse();
        assertThat(constraint1.isRemoved()).isTrue();
        assertThat(constraint2.isRemoved()).isTrue();
        
        variable3.remove();

        assertThat(variable3.isRemoved()).isTrue();
    }

    public class TestConstraint extends AbstractConstraint {
        
        final Definition definition;
        
        final Viability viability;
        
        public TestConstraint(Definition definition, boolean required) {
            super(required);
            this.definition = definition;
            getDependencyLinker().linkDependency(definition);
            viability = new DefaultViability(definition);
        }

        @Override
        public void fillSolver(Solver solver) {
        }

        @Override
        public List<Definition> getDependencies() {
            return Arrays.asList(definition);
        }
        
        @Override
        public Viability getViability() {
            return viability;
        }

        @Override
        protected void freeDefinition(Definition definition) {
            // nothing to do
        }

    }

}
