import java.util.Random;
import java.util.Scanner;

public class TicTacToe {
    private static int SIZE;  // размеры поля

    private static final char DOT_EMPTY = '•';
    private static final char DOT_HUMAN = 'X';
    private static final char DOT_AI = 'O';

    private static char[][] MAP;
    private static final Scanner in = new Scanner(System.in);
    private static final Random random = new Random();

    private static final String HEADER_FIRST_SYMBOL = "*";
    private static final String SPACE_MAP = " ";

    private static int counter;  // счетчик для количества шагов
    private static int sumCount = 0;  // количество для победы
    private static int turnCount;  // счетчик для рассчета подряд идущих одинаковых символов

    private static int curNumRow = 0; // введенная текущая координата x
    private static int curNumCol = 0; // введенная текущая координата y


}
