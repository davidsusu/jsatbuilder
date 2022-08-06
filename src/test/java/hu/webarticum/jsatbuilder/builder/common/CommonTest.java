package hu.webarticum.jsatbuilder.builder.common;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import hu.webarticum.jsatbuilder.builder.core.ConstraintSetSolverFiller;
import hu.webarticum.jsatbuilder.builder.core.Variable;
import hu.webarticum.jsatbuilder.solver.core.Solver;


class CommonTest {

    @Test
    void test1() throws Exception {
        ConstraintSetSolverFiller constraints = new ConstraintSetSolverFiller(true);

        Variable v1_1 = new Variable("1.1");
        Variable v1_2 = new Variable("1.2");
        Variable v1_3 = new Variable("1.3");
        Variable v2_1 = new Variable("2.1");
        Variable v2_2 = new Variable("2.2");

        constraints.add(new OneConstraint(v1_1, v1_2, v1_3));
        constraints.add(new OneConstraint(v2_1, v2_2));
        constraints.add(new OneConstraint(v1_1, v2_1));
        constraints.add(new OrConstraint(v1_2, v1_3));
        constraints.add(new OrConstraint(v1_2, v2_2));
        
        Solver solver = createSolverWith(constraints);
        boolean success = solver.run();
        
        assertThat(success).as("A model should be found").isTrue();
        
        Solver.Model model = solver.getModel();

        assertThat(model.get(v1_1)).as("Variable 1.1 must be false").isFalse();
        assertThat(model.get(v1_2)).as("Variable 1.2 must be true").isTrue();
        assertThat(model.get(v1_3)).as("Variable 1.3 must be false").isFalse();
        assertThat(model.get(v2_1)).as("Variable 2.1 must be true").isTrue();
        assertThat(model.get(v2_2)).as("Variable 2.2 must be false").isFalse();
    }

    @Test
    void test2AIsTrue() throws Exception {
        doSimpleTest(true);
    }

    @Test
    void test2AIsFalse() throws Exception {
        doSimpleTest(false);
    }
    
    private void doSimpleTest(boolean aIsTrue) throws Exception {
        Variable a = new Variable("a");
        Variable b = new Variable("b");
        Variable c = new Variable("c");
        Variable d = new Variable("d");
        
        ConstraintSetSolverFiller constraints = new ConstraintSetSolverFiller();
        constraints.add(new OrConstraint(a, b));
        constraints.add(new EqualConstraint(
            new DefinitionLiteral(c, true),
            new DefinitionLiteral(d, false))
        );
        constraints.add(new EqualConstraint(a, c));
        constraints.add(new EqualConstraint(b, d));
        constraints.add(new GeneralClauseSetConstraint(new DefinitionLiteral[][] {
            { new DefinitionLiteral(a, aIsTrue) }
        }));

        Solver solver = createSolverWith(constraints);
        boolean success = solver.run();
        
        assertThat(success).as("A model should be found").isTrue();
        
        Solver.Model model = solver.getModel();

        assertThat(model.get(a)).isEqualTo(aIsTrue);
        assertThat(model.get(b)).isNotEqualTo(aIsTrue);
        assertThat(model.get(c)).isEqualTo(aIsTrue);
        assertThat(model.get(d)).isNotEqualTo(aIsTrue);
    }
    
    private Solver createSolverWith(ConstraintSetSolverFiller constraints) throws Exception {
        String className = "hu.webarticum.jsatbuilder.solver.sat4j.LightSat4jSolver";
        Solver solver = (Solver)Class.forName(className).getConstructor().newInstance();
        constraints.fillSolver(solver);
        return solver;
    }
    
}
