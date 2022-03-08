import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static char[][] gameMap;
    public static Scanner sc = new Scanner(System.in);

    public static final char EMPTY_DOT = '◙';
    public static final char X_DOT = 'X';
    public static final char O_DOT = '○';

    public static int size;
    public static int block;


    public static void main(String[] args) throws IOException {
        startGame();
    }

    static void startGame() throws IOException {
        String human = "Человек выйграл!!!";
        String computer = "Компьютер выйграл!";
        String draw = "Ничья!";
        customizeGame();
        initGameMap();
        paintGameMap();
        while (true) {
            humanTurn();
            paintGameMap();
            if (chekWin(X_DOT)) {
                FileResultWriter.writeFile(human);
                System.out.println(human);
                break;
            }
            if (isMapFull()) {
                FileResultWriter.writeFile(draw);
                System.out.println(draw);
                break;
            }
            computeTurn();
            paintGameMap();
            if (chekWin(O_DOT)) {
                FileResultWriter.writeFile(computer);
                System.out.println(computer);
            }
            if (isMapFull()) {
                FileResultWriter.writeFile(draw);
                System.out.println(draw);
                break;
            }
        }
        System.out.println("Игра закончена!");

    }

    static void customizeGame() {
        do {
            System.out.println("ВВедите размер поля [3-5]: ");
            size = sc.nextInt();
        } while (size < 3 || size > 5);
        do {
            System.out.print("При сколько блоках в ряд будет победа? [3-" + size + "]: ");
            block = sc.nextInt();
        } while (block < 3 || block > size);
    }

    static boolean chekWin(char symb) {
        for (int col = 0; col < size - block + 1; col++) {
            for (int row = 0; row < size - block + 1; row++) {
                if (checkDiagonal(symb, col, row) || checkLanes(symb, col, row)) return true;
            }
        }
        return false;
    }

    static boolean checkDiagonal(char symb, int x, int y) {
        boolean toRight, toLeft;
        toRight = true;
        toLeft = true;
        for (int i = 0; i < block; i++) {
            toRight &= (gameMap[i + x][i + y] == symb);
            toLeft &= (gameMap[block - i - 1 + x][i + y] == symb);
        }
        return toRight || toLeft;
    }

    static boolean checkLanes(char symb, int x, int y) {
        boolean column, rows;
        for (int col = x; col < block + x; col++) {
            column = true;
            rows = true;
            for (int row = y; row < block + y; row++) {
                column &= (gameMap[col][row] == symb);
                rows &= (gameMap[row][col] == symb);
            }
            if (column || rows) return true;
        }
        return false;
    }

    static boolean isMapFull() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (gameMap[i][j] == EMPTY_DOT) return false;
            }
        }
        return true;
    }

    static void computeTurn() {
        System.out.println("Компьютер думает...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }
        int x, y;
        Random random = new Random();
        do {
            x = random.nextInt(size);
            y = random.nextInt(size);
        } while (!isValidCell(x, y));
        gameMap[y][x] = O_DOT;
    }

    static void humanTurn() {
        int x, y;
        do {
            System.out.println("Пожалуйста введите координаты в формате X Y (пример: минимум 1 1, максимум 3 3)");
            x = sc.nextInt() - 1;
            y = sc.nextInt() - 1;
        } while (!isValidCell(x, y));
        gameMap[y][x] = X_DOT;
    }

    static boolean isValidCell(int x, int y) {
        if (x < 0 || x >= size || y < 0 || y >= size) return false;
        return gameMap[y][x] == EMPTY_DOT;
    }

    static void paintGameMap() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(gameMap[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    static void initGameMap() {
        gameMap = new char[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                gameMap[i][j] = EMPTY_DOT;
            }
        }
    }
}

class FileResultWriter {

    private static final String FILE_NAME = "rating.txt";

    public static void writeFile(String text) {

        try {
            FileWriter writer = new FileWriter(FILE_NAME, true);
            BufferedWriter bufferWriter = new BufferedWriter(writer);
            bufferWriter.write(text + "\n");
        } catch (IOException e) {}
    }
}

