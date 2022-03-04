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

    public static void turnGame() {
        System.out.println("Добро пожаловать в игру!\nПравила:\nРазмеры поля: от 3 до 6, победа при 3х символах\nРазмеры поля: от 7 до 10, победа при 4х символах\nРазмеры поля: от 11 до 15, победа при 5х символах");
        do {
            System.out.print("Введите размер поля(число от 3 до 15): ");
            selectSize();
            System.out.println("\nИгра начинается!");
            init();
            printMap();
            playGame();
        } while (isContinueGame());
        endGame();
    }


    public static void selectSize() {  // Определение размера поля и количества очков для победы
        SIZE = checkSizeInput();
        MAP = new char[SIZE][SIZE];
        sumCount = defineSumCount(SIZE);
    }

    private static int checkSizeInput() {
        while (true) {
            if (in.hasNextInt()) {
                int n = in.nextInt();
                if (isNumValid(n)) {
                    return n;
                }
                System.out.print("Вводимое число должно быть от 1 до 15: ");
            } else {
                in.next();
                System.out.print("Необходимо вводить только целые числа!\n: ");
            }
        }
    }

    private static boolean isNumValid(int n) { // Проверка на правильность ввода для размера поля
        return n > 0 && n < 16;
    }

    private static int defineSumCount(int n) {  // Опредение числа очков для победы в зависимости от введенного размера поля
        int sum = 0;
        switch (n) {
            case 3, 4, 5, 6 -> sum = 3;
            case 7, 8, 9, 10 -> sum = 4;
            case 11, 12, 13, 14, 15 -> sum = 4;
        }
        return sum;
    }

    private static void init() {
        initMap();
    }

    private static void initMap() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                MAP[i][j] = DOT_EMPTY;
            }
        }
    }

    private static void printMap() {
        printHeaderMap();
        printBodyMap();
    }

    private static void printHeaderMap() {
        System.out.print(HEADER_FIRST_SYMBOL + SPACE_MAP);
        for (int i = 0; i < SIZE; i++) {
            printMapNumber(i);
        }
        System.out.println();
    }

    private static void printMapNumber(int i) {
        System.out.print(i + 1 + SPACE_MAP);
    }

    private static void printBodyMap() {
        for (int i = 0; i < SIZE; i++) {
            printMapNumber(i);
            for (int j = 0; j < SIZE; j++) {
                System.out.print(MAP[i][j] + SPACE_MAP);
            }
            System.out.println();
        }
    }


    private static void playGame() {  // Основная конструкция

        while (true) {
            turnHuman();
            printMap();
            if (checkEnd(DOT_HUMAN)) {
                break;
            }

            turnAI();
            printMap();
            if (checkEnd(DOT_AI)) {
                break;
            }
        }
    }


    private static void turnHuman() { // ход человека
        System.out.println("Ваш ход");
        int rowNumber, columnNumber;

        while (true) {
            rowNumber = getValidNumberFromUser() - 1;
            columnNumber = getValidNumberFromUser() - 1;
            if (isCellFree(rowNumber, columnNumber)) {
                break;
            }
            System.out.println("\nВы выбрали занятую ячейку!");
        }
        curNumRow = rowNumber;
        curNumCol = columnNumber;
        MAP[rowNumber][columnNumber] = DOT_HUMAN;
        counter++;
    }

    private static int getValidNumberFromUser() {  // Проверка правильно ввода координаты
        while (true) {
            System.out.print("Введите координату(1-" + SIZE + "): ");
            if (in.hasNextInt()) {
                int n = in.nextInt();
                if (isNumberValid(n)) {

                    return n;
                }
                System.out.println("\nПроверьте значение координаты. Должно быть от 1 до " + SIZE);
            } else {
                in.next();
                System.out.println("\nНеобходимо вводить только целые числа!");
            }
        }
    }

    private static boolean isNumberValid(int n) {  // Проверка числа координаты для опреределенного промежутка
        return n >= 1 && n <= SIZE;
    }

    private static boolean isCellFree(int rowNumber, int columnNumber) {  // свободна ли ячейка
        return MAP[rowNumber][columnNumber] == DOT_EMPTY;
    }


    private static void turnAI() {
        System.out.println("Ход противника");
        int rowNumber, columnNumber;

        do {
            rowNumber = random.nextInt(SIZE);
            columnNumber = random.nextInt(SIZE);

        } while (!isCellFree(rowNumber, columnNumber));

        MAP[rowNumber][columnNumber] = DOT_AI;
        counter++;
    }

    private static boolean checkEnd(char symbol) {
        if (checkWin(symbol)) {
            if (symbol == DOT_HUMAN) {
                System.out.println("\nВы победили!");
            } else {
                System.out.println("\nВы проиграли!");
            }
            return true;
        }

        if (checkDraw()) {
            System.out.println("Ничья!");
            return true;
        }
        return false;
    }

    private static boolean checkDraw() {  // заполнены ли все клетки
        return counter >= SIZE * SIZE;

    }


    private static boolean checkWin(char symbol) {  // Проверка по ряду, столбцу, главной и второстепенной диагонали
        return checkRow(symbol) || checkColumn(symbol) || checkRightD(symbol) || checkLeftD(symbol);
    }

    private static boolean checkRow(char symbol) {
        turnCount = 0;
        for (int i = 0; i < SIZE; i++) {
            if (MAP[curNumRow][i] == symbol) {
                turnCount++;
                if (turnCount == sumCount) {
                    return true;
                }
            }
            else {
                turnCount = 0;
            }
        }
        return false;
    }

    public static boolean checkColumn(char symbol) {
        turnCount = 0;
        for (int i = 0; i < SIZE; i++) {
            if (MAP[i][curNumCol] == symbol) {
                turnCount++;
                if (turnCount == sumCount) {
                    return true;
                }
            }
            else {
                turnCount = 0;
            }
        }
        return false;
    }

    private static boolean checkRightD(char symbol) {
        turnCount = 0;
        int curRow = (curNumRow > curNumCol) ? curNumRow - curNumCol : 0;
        int curCol = (curNumRow > curNumCol) ? 0 : curNumCol - curNumRow;
        for (int i = 0; i < SIZE; i++) {
            int row = curRow + i;
            int col = curCol + i;
            if (row <= (SIZE - 1) && col <= (SIZE - 1)) {
                if (MAP[row][col] == symbol) {
                    turnCount++;
                    if (turnCount == sumCount) {
                        return true;
                    }
                }
                else {
                    turnCount = 0;
                }
            }
        }
        return false;
    }

    private static boolean checkLeftD(char symbol) {
        turnCount = 0;
        int curRow = Math.min((curNumRow + curNumCol), (SIZE - 1));
        int curCol = (curRow == (SIZE - 1)) ? curNumRow + curNumCol - curRow : 0;
        for (int i = 0; i < SIZE; i++) {
            int row = curRow - i;
            int col = curCol + i;
            if (row >= 0 && col <= (SIZE - 1)) {
                if (MAP[row][col] == symbol) {
                    turnCount++;
                    if (turnCount == sumCount) {
                        return true;
                    }
                }
                else {
                    turnCount = 0;
                }
            }
        }
        return false;
    }



    private static boolean isContinueGame() {
        System.out.println("Хотите продолжить? да\\нет: ");
        counter = 0;
        turnCount = 0;
        return switch (in.next()) {
            case "yes", "да" -> true;
            default -> false;
        };
    }

    private static void endGame() {
        in.close();
        System.out.println("Игра завершена!");
    }
}
