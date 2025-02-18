package com.vargas.training.junit.healthycoderapp;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

public class BMICalculatorTest {
    private String environment = "prod";

    @Nested
    class isDietRecommendedTest{
        @ParameterizedTest(name = "weight={0}, height={1}")
        //ValueSource alows on value at a time
        // @ValueSource(doubles = {89.0,95.0,110.})
        //CsvSource allows for multiple values at a time. For example weight will be 89.0 and height 1.72 for first test
        //@CsvSource(value = {"89.0, 1.72","95.0, 1.75","110.0, 1.78"})
        @CsvFileSource(resources = "/diet-recommended-input-data.csv", numLinesToSkip = 1)
        void  should_ReturnTrue_When_DietRecommended(Double coderWeight, Double coderHeight){
            //given
            double weight =coderWeight;
            double height = coderHeight;

            //when
            boolean recommended = BMICalculator.isDietRecommended(weight, height);

            //then
            assertTrue(recommended);
            //above is breakdown of below for better redability
            // assertTrue(BMICalculator.isDietRecommended(81.2,1.65));
        }

        @Test
        void  should_ReturnFalse_When_DietNotRecommended(){
            //given
            double weight = 50.0;
            double height = 1.92;

            //when
            boolean recommended = BMICalculator.isDietRecommended(weight, height);
            //then
            assertFalse(recommended);
        }

        @Test
        void  should_ThrowArithmeticException_When_HeightZero(){
            //given
            double weight = 50.0;
            double height = 0.0;

            //Create Executable using lambda or the exception would  prevent reaching the "assertThrows"
            //that follows. The executable is passes to  "assertThrows" as arument
            Executable executable = () -> BMICalculator.isDietRecommended(weight, height);

            //then
            assertThrows(ArithmeticException.class, executable);
        }

    }

    @Nested
    @DisplayName("{} Sample inner class display name")
    class FindCoderWithWorstBMITests{
        @Test
        @DisplayName(">>>> sample method display name")
        @DisabledOnOs(OS.WINDOWS)
        void should_ReturnCoderWithWorstBMI_When_CoderListNotEmpty(){
            //given
            List<Coder> coders = new ArrayList<>();

            coders.add(new Coder(1.80, 60.0));
            coders.add(new Coder(1.82, 98.0));
            coders.add(new Coder(1.82, 64.7));

            //when
            Coder coderWorstBMI = BMICalculator.findCoderWithWorstBMI(coders);

            //then
            //user assert all to figure out if not only first, but if subsequent assertion(s) failed
            //use for assertions that should go together
            assertAll(
                    () -> assertEquals(1.82, coderWorstBMI.getHeight()),
                    () -> assertEquals(98.0, coderWorstBMI.getWeight())
            );
        }

        @Test
        void should_ReturnCoderWithWorstBMIIn1Ms_When_CoderListHas10000Elements(){
            //given
            //wiil only run when environment matches assumption
            assumeTrue(BMICalculatorTest.this.environment.equals("prod"));
            List<Coder> coders = new ArrayList<>();
            for (int i = 0; i < 10000; i++) {
                coders.add(new Coder(1.0 + i, 10.0 + i));
            }
            //when
            Executable executable = () -> BMICalculator.findCoderWithWorstBMI(coders);
            //then
            assertTimeout(Duration.ofMillis(500),executable);
        }

        @Test
        void should_ReturnCoderWithWorstBMI_When_CoderListEmpty(){

            //given
            List<Coder> coders = new ArrayList<>();

            //when
            Coder coderWorstBMI = BMICalculator.findCoderWithWorstBMI(coders);

            //then
            assertNull(coderWorstBMI);
        }
    }

    @Nested
    class GetBMIScoresTests{
        @Test
        void should_ReturnCorrectBMIScoreArray_When_CoderListNotEmpty(){
            //given
            List<Coder> coders = new ArrayList<>();

            coders.add(new Coder(1.80, 60.0));
            coders.add(new Coder(1.82, 98.0));
            coders.add(new Coder(1.82, 64.7));
            double[] expected = {18.52, 29.59, 19.53};

            //when
            double[] bmiscores = BMICalculator.getBMIScores(coders);

            //then
            assertArrayEquals(expected, bmiscores);
        }
    }
}
