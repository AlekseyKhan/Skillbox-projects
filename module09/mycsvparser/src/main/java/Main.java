import java.io.*;
import java.sql.SQLOutput;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    private static final int AMOUNT_OF_FIELDS = 8;
    private static Pattern patternForInt = Pattern.compile("\\d+");
    private static Pattern patternForFract = Pattern.compile("\\d+[\\.|,]\\d{1,2}");
    private static Pattern expenseOperation = Pattern.compile("[\\/\\\\].+?\\s{3,}");

    public static void main(String[] args) throws IOException {
        String pathOfCSV = "data/movementList.csv";

        List<Transaction> list = getDataFromCSV(pathOfCSV);

        if (!list.isEmpty()) {
            showReport(list);
        }
    }

    private static void showReport(List<Transaction> data) {
        long incomeSum = 0;
        long expenseSum = 0;
        Map<String, Long> operationSum = new HashMap();

        for (Transaction record : data.subList(1, data.size())) {
            if (!record.getIncome().trim().equals("0") && record.getExpense().trim().equals("0")) {
                incomeSum = calculateIncome(incomeSum, record.getIncome());

            } else if (!record.getExpense().trim().equals("0") && record.getIncome().trim().equals("0")) {
                expenseSum = calculateExpense(expenseSum, record.getExpense(), record.getOperationName(), operationSum);
            } else {
                showWarning("Расход/Приход", record.toString());
            }

        }
        System.out.println("Общий приход: " + convertToFract(incomeSum));
        System.out.println("Общий расход: " + convertToFract(expenseSum));
        System.out.println("Расходы по операциям");
        for (Map.Entry entry : operationSum.entrySet()) {
            System.out.println(entry.getKey() + "= " + convertToFract((Long) entry.getValue()));
        }


        System.out.println("\nЗадание со звездочкой \nКонтрагент: сумма_прихода  сумма_расхода\n");
        data.stream().skip(1)
                .collect(Collectors.groupingBy(Transaction::getOperationName))
                .forEach((name, transactions) -> {

                    Summary summary = transactions.stream()
                            .map(transaction -> new Summary(
                                    convertToLong(transaction.getIncome()),
                                    convertToLong(transaction.getExpense())))
                            .reduce((a, b) -> a.calculateSum(b))
                            .get();

                    System.out.println(name + ": " + convertToFract(summary.getIncome()) + " | " + convertToFract(summary.getExpense()));
                });


    }

    private static long convertToLong(String input) {
        if (patternForFract.matcher(input.replaceAll("\"", "")).matches()) {
            double num = Double.parseDouble(input
                    .replaceAll("\"", "")
                    .replace(",", "."));
            return (long) (num * 100);
        } else if (patternForInt.matcher(input).matches()) {
            return Long.parseLong(input + "00");
        } else {
            return -1;
        }
    }

    private static String convertToFract(long input) {
        return String.format("%d.%02d", input / 100, input % 100);
    }

    private static long calculateIncome(long incomeSum, String value) {
        long income = convertToLong(value);
        if (income >= 0) {
            incomeSum += income;
        } else {
            showWarning("сумма прихода", value);
        }

        return incomeSum;
    }

    private static long calculateExpense(long expenseSum, String value, String operationName, Map<String, Long> operationSum) {
        long expense = convertToLong(value);

        if (expense >= 0) {
            expenseSum += expense;
            if (operationSum.containsKey(operationName)) {
                long oldValue = operationSum.get(operationName);
                operationSum.put(operationName, oldValue + expense);
            } else {
                operationSum.put(operationName, expense);
            }
        } else {
            showWarning("сумма расхода", value);
        }

        return expenseSum;
    }

    private static List<Transaction> getDataFromCSV(String path) {
        List<Transaction> transactions = new ArrayList<Transaction>();

        try (BufferedReader reader = new BufferedReader(new FileReader(path))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] splitedText = line.split(",");
                ArrayList<String> columnList = new ArrayList<String>();

                for (int i = 0; i < splitedText.length; i++) {
                    String text = splitedText[i];
                    if (isColumnStartWithQuot(text)) {
                        while (!isColumnEndAtQuot(text)) {
                            i++;
                            text = text + "," + splitedText[i];
                        }
                    }
                    columnList.add(text);
                }

                if (columnList.size() != AMOUNT_OF_FIELDS) {
                    showWarning("входные данные", line);
                    continue;
                }

                Transaction tr = new Transaction();
                tr.setType(columnList.get(0));
                tr.setAccountNumber(columnList.get(1));
                tr.setCurrency(columnList.get(2));
                tr.setOperationDate(columnList.get(3));
                tr.setReference(columnList.get(4));
                tr.setOperation(columnList.get(5));
                tr.setIncome(columnList.get(6));
                tr.setExpense(columnList.get(7));
                transactions.add(tr);
            }

        } catch (Exception e) {
            showWarning("Файл не найден", path);
        }

        return transactions;
    }

    private static boolean isColumnStartWithQuot(String text) {
        return text.trim().startsWith("\"");
    }

    private static boolean isColumnEndAtQuot(String text) {
        return text.trim().endsWith("\"");
    }

    private static void showWarning(String message, String source) {
        System.out.printf("Не удалось прочитать '%s' из '%s' \n", message, source);
    }

    private static class Summary {
        long income;
        long expense;

        Summary(long income, long expense) {
            this.income = income;
            this.expense = expense;
        }


        public long getIncome() {
            return income;
        }

        public long getExpense() {
            return expense;
        }

        Summary calculateSum(Summary b) {
            income += b.getIncome();
            expense += b.getExpense();
            return this;
        }

        @Override
        public String toString() {
            return "Summary{" +
                    "income=" + income +
                    ", expense=" + expense +
                    '}';
        }
    }
}
