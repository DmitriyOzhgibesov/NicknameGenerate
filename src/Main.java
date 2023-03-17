import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static AtomicInteger palindromeWordsCounter = new AtomicInteger();
    public static AtomicInteger oneCharWordsCounter = new AtomicInteger();
    public static AtomicInteger sortedWordsCounter = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {

        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        List<Thread> threads = new ArrayList<>();

        Runnable palindromeCheck = () -> {
            for (String s : texts) {
                if (s.length() == 4 && isPalindrome(s)) {
                    palindromeWordsCounter.getAndAdd(1);
                }
            }
        };

        Runnable oneCharCheck = () -> {
            for (String s : texts) {
                if (s.length() == 3 && isOneChar(s)) {
                    oneCharWordsCounter.getAndAdd(1);
                }
            }
        };

        Runnable sortedCheck = () -> {
            for (String s : texts) {
                if (s.length() == 5 && isSorted(s)) {
                    sortedWordsCounter.getAndAdd(1);
                }
            }
        };

        Thread palindromeLogicThread = new Thread(palindromeCheck);
        Thread oneCharLogicThread = new Thread(oneCharCheck);
        Thread sortedLogicThread = new Thread(sortedCheck);

        threads.add(palindromeLogicThread);
        threads.add(oneCharLogicThread);
        threads.add(sortedLogicThread);

        palindromeLogicThread.start();
        oneCharLogicThread.start();
        sortedLogicThread.start();

        for (Thread thread : threads) {
            thread.join();
        }

        int palindromeWordsCount = palindromeWordsCounter.get();
        int oneCharWordsCount = oneCharWordsCounter.get();
        int sortedWordsCount = sortedWordsCounter.get();

        printResult(palindromeWordsCount, oneCharWordsCount, sortedWordsCount);
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    static boolean isPalindrome(String word) {
        int length = word.length();

        for (int i = 0; i < (length / 2); i++) {
            if (word.charAt(i) != word.charAt(length - i - 1)) {
                return false;
            }
        }

        return true;
    }

    public static boolean isOneChar(String word) {
        char[] wordAsArray = word.toCharArray();

        for (int i = 1; i < word.length(); i++) {
            if (wordAsArray[i] != wordAsArray[i - 1]) {
                return false;
            }
        }

        return true;
    }

    public static boolean isSorted(String word) {
        char[] wordAsArray = word.toCharArray();
        Arrays.sort(wordAsArray);

        if (word.equals(new String(wordAsArray))) {
            return true;
        }

        return false;
    }

    public static void printResult(int palindromeWordsCount, int oneCharWordsCount, int sortedWordsCount) {
        StringBuilder result = new StringBuilder();
        result.append(String.format("Красивых слов с длиной 3: %d шт\n", oneCharWordsCount));
        result.append(String.format("Красивых слов с длиной 4: %d шт\n", palindromeWordsCount));
        result.append(String.format("Красивых слов с длиной 5: %d шт", sortedWordsCount));
        System.out.println(result);
    }
}