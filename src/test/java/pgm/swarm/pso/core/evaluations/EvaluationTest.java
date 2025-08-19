package pgm.swarm.pso.core.evaluations;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EvaluationTest {

    private final Evaluation eval = new Evaluation();

    @Test
    void totalTaskExecutionTime_sumsValues() {
        double result = eval.totalTaskExecutionTime(List.of(1.0, 2.0, 3.0));
        assertEquals(6.0, result, 1e-12);
    }

    @Test
    void transferringTime_dividesDataByBandwidth() {
        double result = eval.transferringTime(100.0, 20.0);
        assertEquals(5.0, result, 1e-12);
    }

    @Test
    void totalTransferringTime_sumsValues() {
        double result = eval.totalTransferringTime(List.of(2.0, 3.0, 5.0));
        assertEquals(10.0, result, 1e-12);
    }

    @Test
    void executionCosts_multipliesInputs() {
        double result = eval.executionCosts(2.0, 3, 4.0);
        assertEquals(24.0, result, 1e-12);
    }

    @Test
    void totalExecutionCosts_sumsValues() {
        double result = eval.totalExecutionCosts(List.of(10.0, 20.0, 5.0));
        assertEquals(35.0, result, 1e-12);
    }
}
