package school.journal;


import org.junit.Test;
import school.journal.utils.exception.ValidationException;

import java.util.Date;

import static school.journal.utils.ValidateServiceUtils.*;


public class ValidateServiceUtilsTest {

    @Test(expected = ValidationException.class)
    public void testValidateId_withBellowZeroValues() throws Exception{
        validateId(-1,"");
        validateId(0,"");
    }

    @Test
    public void testValidateId_withCorrect() throws Exception{
        validateId(1,"");
        validateId(100,"");
    }

    @Test
    public void testValidateNullableString_WithCorrectValue() throws Exception{
        validateNullableString("no","");

    }

    @Test(expected = ValidationException.class)
    public void testValidateNullableString_withEmptyString() throws Exception{
        validateNullableString("","");
    }

    @Test
    public void testValidateString_WithCorrectValue() throws Exception{
        validateString("no","");
    }

    @Test(expected = ValidationException.class)
    public void testValidateString_withNull() throws Exception{
        validateString(null,"");
    }

    @Test
    public void testValidatePhone_WithCorrectValue() throws Exception{
        validatePhone("+375291111111");
    }

    @Test(expected = ValidationException.class)
    public void testValidatePhone_withEmptyString() throws Exception{
        validatePhone("");
    }

    @Test(expected = ValidationException.class)
    public void testValidatePhone_withIncorrectString() throws Exception{
        validatePhone("abs");
    }

    @Test
    public void testValidateEmail_WithCorrectValue() throws Exception{
        validateEmail("abc@g.com");
    }

    @Test(expected = ValidationException.class)
    public void testValidateEmail_withEmptyString() throws Exception{
        validateEmail("");
    }

    @Test(expected = ValidationException.class)
    public void testValidateEmail_withIncorrectString() throws Exception{
        validateEmail("abs");
    }

    @Test
    public void testValidateDatePeriod_WithCorrectValue() throws Exception{
        validateDatePeriod(new Date(123),new Date(124));
    }

    @Test(expected = ValidationException.class)
    public void testValidateDatePeriod_withIncorrectString() throws Exception{
        validateDatePeriod(new Date(125),new Date(124));
    }
}
