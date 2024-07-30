package com.vargas.training.junit.healthycoderapp;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class BMICalculatorTest {
    @ParameterizedTest
    @ValueSource(doubles = {89.0,95.0,110.})
    void  should_ReturnTrue_When_DietRecommended(Double coderWeight){
        //given
        double weight =coderWeight;
        double height = 1.72;

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

    @Test
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
    void should_ReturnCoderWithWorstBMI_When_CoderListEmpty(){

        //given
        List<Coder> coders = new ArrayList<>();

        //when
        Coder coderWorstBMI = BMICalculator.findCoderWithWorstBMI(coders);

        //then
        assertNull(coderWorstBMI);
    }

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
