import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Calc
{

    private static Boolean isRoman;
    private static Integer firstNumber;
    private static Integer secondNumber;
    private static OperationTypes operation;
    private static String buffer = "";

    public static void main(String[] args)
    { System.out.println(calc(new Scanner(System.in).nextLine())); }

    public static String calc(String input)
    {
        int result;
        for (int i = 0; i < input.length(); i++)
        {
            char currentSymbol = input.charAt(i);
            if (Character.isDigit(currentSymbol))
            {
                checkCompatible(false);
                buffer += currentSymbol;
            }
            else if (Character.isLetter(currentSymbol))
            {
                checkCompatible(true);
                buffer += currentSymbol;
            }
            else if (' ' == currentSymbol)
            { setNumber(); }
            else
            {
                boolean isValueFound = false;
                for (OperationTypes op : OperationTypes.values())
                    if (currentSymbol == op.meaning)
                    {
                        operation = op;
                        setNumber();
                        isValueFound = true;
                        break;
                    }
                if (!isValueFound)
                    throw new RuntimeException("Неизвестная операция");
            }
        }

        setNumber();

        if (firstNumber == null || secondNumber == null)
            throw new RuntimeException("строка не является математической операцией");
        else if (firstNumber > 10 || secondNumber > 10)
            throw new RuntimeException("Слишком большие входные числа");

        switch (operation)
        {
            case PLUS -> result = firstNumber + secondNumber;
            case MINUS -> result = firstNumber - secondNumber;
            case DIVISION -> result = firstNumber/secondNumber;
            case MULTIPLICATION -> result = firstNumber*secondNumber;
            default -> throw new RuntimeException("Error");
        }

        if (isRoman)
        {
            if (result <= 0)
                throw new RuntimeException("в римской системе нет отрицательных чисел");
            return RomanValues.intToRoman(result);
        }
        else
            return String.valueOf(result);
    }

    private static void setNumber()
    {
        int value = 0;
        if (!buffer.isEmpty())
        {
            if (isRoman)
                value = RomanValues.romanToInt(buffer);
            else
                value = Integer.parseInt(buffer);

            if (firstNumber == null)
                firstNumber = value;
            else if (secondNumber == null)
                secondNumber = value;
            else
                throw new RuntimeException("формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, /, *)");

            buffer = "";
        }
    }

    private static void checkCompatible(boolean _isRoman)
    {
        if (isRoman == null || isRoman == _isRoman)
            isRoman = _isRoman;
        else
            throw new RuntimeException("используются одновременно разные системы счисления");
    }

    enum OperationTypes
    {
        PLUS('+'),
        MINUS('-'),
        MULTIPLICATION('*'),
        DIVISION('/');

        public char meaning;

        OperationTypes(char meaning)
        { this.meaning = meaning; }
    }

    static class RomanValues
    {
        static private final Map<Integer, Character> numberValue = new HashMap<>();
        static
        {
            numberValue.put(1, 'I');
            numberValue.put(5, 'V');
            numberValue.put(10, 'X');
            numberValue.put(50, 'L');
            numberValue.put(100, 'C');
            numberValue.put(500, 'D');
            numberValue.put(1000, 'M');
        }
        static private final Map<Character, Integer> valueNumber = new HashMap<>();
        static
        {
            for (Map.Entry<Integer, Character> entry: numberValue.entrySet())
                valueNumber.put(entry.getValue(), entry.getKey());
        }

        static public int romanToInt(String value)
        {
            int result = 0;
            for (int i = 0; i < value.length(); i++)
            {
                char currentSymbol = value.charAt(i);
                int currentNumber = valueNumber.get(currentSymbol);
                if (i + 1 < value.length())
                {
                    int nextNumber = valueNumber.get(value.charAt(i+1));
                    if (currentNumber < nextNumber)
                    {
                        result += nextNumber - currentNumber;
                        i++;
                        continue;
                    }
                }
                result += currentNumber;
            }
            return result;
        }

        static public String intToRoman(int value)
        {
            String result = "";
            String stringNumber = String.valueOf(value);
            int numberLength = stringNumber.length();
            for (int i = 0; i < numberLength ; i++)
            {
                int currentNumber = Integer.parseInt(String.valueOf(stringNumber.charAt(i)));
                int temporalNumber = 1;
                for (int j = i; j < numberLength-1; j++)
                    temporalNumber *= 10;

                if (4 == currentNumber)
                {
                    result += numberValue.get(temporalNumber);
                    result += numberValue.get(temporalNumber*5);
                    break;
                }
                else if (9 == currentNumber)
                {
                    result += numberValue.get(temporalNumber);
                    result += numberValue.get(temporalNumber*10);
                    break;
                }
                if (currentNumber >= 5)
                {
                    currentNumber -= 5;
                    result += numberValue.get(temporalNumber*5);
                }

                    for (int l = 0; l < currentNumber; l++)
                        result += numberValue.get(temporalNumber);
            }
            return result;
        }
    }

}
