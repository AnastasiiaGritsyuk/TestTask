import java.util.Arrays;
import java.util.Scanner;

import static java.lang.System.*;

public class Main {
    public static void main(String[] args) throws IllegalArgumentException, ArithmeticException{
        Scanner in = new Scanner(System.in);
        out.println("Введите строку с арифметическим выражением между двумя числами:");
        String line = in.nextLine();
        double result = calc(line);
        out.println("Результат вычислений: ");
        out.println(result);
    }

    public static double calc(String input) throws IllegalArgumentException, ArithmeticException{
        String[] words = input.split(" ");
        String sortedstring = splitString(words);
        String[] elements = deleteSpaceElem(sortedstring);
        String postfixstring = postfix(elements, 0);
        out.println("Строка, преобразованная из инфиксной формы записи, в постфиксную: ");
        out.println(postfixstring);
        return countResult(postfixstring.trim().split(" "));
    }

    public static String splitString (String[] string) throws IllegalArgumentException {
        StringBuilder builder1 = new StringBuilder();
        for (String elem : string) {
            char[] chararray = elem.toCharArray();
            for (char ch : chararray) {
                if (((int) ch >= 48 && (int) ch <= 57) || (int) ch == 46) {
                    builder1.append(ch);
                } else {
                    switch (ch) {
                        case '+': case '-': case '*': case '/': case '%': case '^': case '(': case ')':
                            builder1.append(' ').append(ch).append(' ');
                            break;
                        default:
                            throw new IllegalArgumentException("Выражение не соответствует требованиям!");
                    }
                }
            }
        }
        return builder1.toString().trim();
    }


    public static String[] deleteSpaceElem(String line2) {
        StringBuilder builder2 = new StringBuilder();
        String[] masselem = line2.split(" ");
        for (String elem: masselem){
            if (!elem.isEmpty()) {
                builder2.append(elem).append(" ");
            }
        }
        return builder2.toString().trim().split(" ");
    }

    public static String postfix (String[] args, int flag) throws IllegalArgumentException{
        String[] mass_op = new String[10];
        StringBuilder builder3 = new StringBuilder();
        int countop = 0;
        int i;
        for (i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "+":
                case "-":
                case "*":
                case "/":
                case "%":
                case "^":
                    while ((countop >= 1) && ((prioritetOp(args[i]) <= prioritetOp(mass_op[countop - 1])))) {
                        builder3.append(mass_op[countop - 1]).append(" ");
                        mass_op[countop - 1] = null;
                        countop--;
                    }
                    mass_op[countop] = args[i];
                    countop++;
                    break;
                case "(":
                    builder3.append(args[i]).append(" ");
                    i++;
                    builder3.append(postfix(Arrays.copyOfRange(args, i, args.length), 1));
                    int countopen = 0;
                    int countclose = 0;
                    while ((i < args.length && countopen >= countclose)){
                        if (args[i].equals("(")){
                            countopen++;
                        } else if (args[i].equals(")")){
                            countclose++;
                        }
                        i++;
                    }
                    i--;
                    builder3.append(args[i]).append(" ");
                    break;
                case ")":
                    if (flag == 0){
                        throw new IllegalArgumentException("Отсутствует открывающая скобка!");
                    }
                    for (int index = mass_op.length -1; index >= 0; index--) {
                        if (mass_op[index] != null){
                            builder3.append(mass_op[index]).append(" ");
                            mass_op[index] = null;
                        }
                    }
                    return builder3.toString();
                default:
                    builder3.append(args[i]).append(" ");
            }
        }
        if (flag == 1){
            throw new IllegalArgumentException("Отсутствует закрывающая скобка!");
        }
        for (int index = mass_op.length -1; index >= 0; index--) {
            if (mass_op[index] != null){
                builder3.append(mass_op[index]).append(" ");
                mass_op[index] = null;
            }
        }
        return builder3.toString().trim();
    }

    public static int prioritetOp (String op) {
        int mean = 0;
        switch(op){
            case "+": case "-":
                mean = 1;
                break;
            case "/": case "%": case "*":
                mean = 2;
                break;
            case "^":
                mean = 3;
                break;
        }
        return mean;
    }

    public static double countResult (String[] masselem) throws IllegalArgumentException, ArithmeticException{
        double[] arr_num = new double[5];
        int i = 0;
        boolean elNotNull2 = false;
        for (int j = 0; j < masselem.length; j++){
            switch(masselem[j]){
                case "-":
                    if (i == 1){
                        arr_num[i-1] = 0 - arr_num[i-1];
                    } else if (i >= 2){
                        arr_num[i-2] = arr_num[i-2] - arr_num[i-1];
                        arr_num[i-1] = 0;
                        i--;
                    } else {
                        throw new IllegalArgumentException("Выражение не соответствует требованиям!");
                    }
                    break;
                case "+":
                    if (i == 1){
                        arr_num[i-1] = 0 + arr_num[i-1];
                    } else if (i >= 2) {
                        arr_num[i-2] = arr_num[i-2] + arr_num[i-1];
                        arr_num[i-1] = 0;
                        i--;
                    } else {
                        throw new IllegalArgumentException("Выражение не соответствует требованиям!");
                    }
                    break;
                case "*":
                    if (i >= 2){
                        arr_num[i-2] = arr_num[i-2] * arr_num[i-1];
                        arr_num[i-1] = 0;
                        i--;
                    } else {
                        throw new IllegalArgumentException("Выражение не соответствует требованиям!");
                    }
                    break;
                case "/":
                    if (i >= 2){
                        if (arr_num[i-1] == 0){
                            throw new ArithmeticException("Деление на ноль недопустимо!!!");
                        }
                        arr_num[i-2] = arr_num[i-2] / arr_num[i-1];
                        arr_num[i-1] = 0;
                        i--;
                    } else {
                        throw new IllegalArgumentException("Выражение не соответствует требованиям!");
                    }
                    break;
                case "%":
                    if (i >= 2){
                        if (arr_num[i-1] == 0){
                            throw new ArithmeticException("Деление на ноль недопустимо!!!");
                        }
                        arr_num[i-2] = arr_num[i-2] % arr_num[i-1];
                        arr_num[i-1] = 0;
                        i--;
                    } else {
                        throw new IllegalArgumentException("Выражение не соответствует требованиям!");
                    }
                    break;
                case "^":
                    if (i >= 2){
                        arr_num[i-2] = Math.pow(arr_num[i-2],arr_num[i-1]);
                        arr_num[i-1] = 0;
                        i--;
                    } else {
                        throw new IllegalArgumentException("Выражение не соответствует требованиям!");
                    }
                    break;
                case "(":
                    try
                    {
                        j++;
                        arr_num[i] = countResult((Arrays.copyOfRange(masselem, j, masselem.length)));
                        i++;
                        int countopen = 0;
                        int countclose = 0;
                        while (j < masselem.length && countopen >= countclose){
                            if (masselem[j].equals("(")) {
                                countopen++;
                            } else if (masselem[j].equals(")")) {
                                countclose++;
                            }
                            j++;
                        }
                    } catch (NumberFormatException e) {
                        throw new RuntimeException("Введено не число!");
                    }
                    j--;
                    break;
                case ")":
                    for (double el: (Arrays.copyOfRange(arr_num, 1, arr_num.length))){
                        if (el != 0) {
                            elNotNull2 = true;
                            break;
                        }
                    }
                    if (elNotNull2) {
                        throw new IllegalArgumentException("Некорректно задано арифметичнское выражение!");
                    }
                    return arr_num[0];
                default:
                    try {
                        arr_num[i] = Double.parseDouble(masselem[j]);
                        i++;
                    } catch (NumberFormatException e) {
                        throw new RuntimeException("Введено не число: " + masselem[j]);
                    }

            }
        }
        for (double el: (Arrays.copyOfRange(arr_num, 1, arr_num.length))){
            if (el != 0){
                elNotNull2 = true;
                break;
            }
        }
        if (elNotNull2) {
            throw new IllegalArgumentException("Некорректно задано арифметичнское выражение!");
        }
        return arr_num[0];
    }
}
//Отладка:
// 45 - 12*34+ 347 - 29.13+222 -7^12 *(8 -13/12) - 6
// 45 - 12*(34+ 347 - 29.13)+222 -7^12 *(8 -13/12) - 6
// 45 - 12*(34+ 347 - 29.13 *(8 -13/12) - 7)+222 -7^12 * (8 -2/12) - 6
// 57863+6754-999999*0.9+((456^2)-90*12345/78960)
//((1.5 + 2.75) * (4.2 - 1.25)) ^ 2 - (100 / (2.5 * 4)) + ((-5) * 3.2 + 15.8) / 2.1 + 2 ^ (3 - 1) * (7.5 - 2.5) - ((12.8 / (3.2 + 1.6)) ^ 0.5) * 4 + (10 + (2 * (3 - (1 + 1)))) ^ 2 - 1000 / (2 * 5 * (2 + 3)) + (1 + 1) ^ 10 / 1024 + 10 * (1 - (0.5 + 0.25 + 0.125)) - (-3 + (5 * (2 - (1 / 2)))) + 1 / (2 ^ (-1))


