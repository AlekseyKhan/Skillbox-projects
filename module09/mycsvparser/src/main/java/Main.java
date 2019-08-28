import java.io.*;
import java.sql.SQLOutput;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static final int AMOUNT_OF_FIELDS = 8;
    private static Pattern patternForInt = Pattern.compile("\\d+");
    private static Pattern patternForFract = Pattern.compile("\\d+[\\.|,]\\d{1,2}");
    private static Pattern expenseOperation = Pattern.compile("[\\/\\\\].+?\\s{3,}");

    public static void main(String[] args) throws IOException {
        String pathOfCSV = "data/movementList.csv";

        List<Transaction> list = getData(pathOfCSV);

        if (!list.isEmpty()) {
            showReport(list);
        }
    }

    private static void showReport(List<Transaction> data) {
        long incomeSum = 0;
        long expenseSum = 0;
        Map<String, Long> operationSum = new HashMap();

        for(Transaction record: data.subList(1, data.size())) {
            if (!record.getIncome().trim().equals("0") && record.getExpense().trim().equals("0")) {
                incomeSum = calculateIncome(incomeSum, record.getIncome());

            } else if (!record.getExpense().trim().equals("0") && record.getIncome().trim().equals("0")) {
                String operationName = getOperationName(expenseOperation.matcher(record.getOperation()));
                expenseSum = calculateExpense(expenseSum, record.getExpense(), operationName, operationSum);
            } else {
                showWarning("Расход/Приход", record.toString());
            }

        }
        System.out.println("Общий приход: " + convertToFract(incomeSum));
        System.out.println("Общий расход: " + convertToFract(expenseSum));
        System.out.println("Расходы по операциям");
        for(Map.Entry entry: operationSum.entrySet()) {
            System.out.println(entry.getKey() + "= " + convertToFract((Long) entry.getValue()));
        }
    }

    private static long convertToLong(String input) {
        if(patternForFract.matcher(input.replaceAll("\"","")).matches()) {
            double num = Double.parseDouble(input
                    .replaceAll("\"","")
                    .replace(",","."));
            return (long) (num * 100);
        } else if (patternForInt.matcher(input).matches()){
            return Long.parseLong(input + "00");
        } else {
            return -1;
        }
    }

    private static String convertToFract(long input) {
        return String.format("%d.%02d",input / 100, input % 100);
    }

    private static String getOperationName(Matcher matcher) {
        String source;
        if (matcher.find()) {
            source = matcher.group(0);
            String[] splittedSource = source.split("\\\\|\\/");
            return splittedSource[splittedSource.length - 1].trim();
        }

        return "unknown";
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

    private static List<Transaction> getData(String path) throws IOException {

        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader(path));
        } catch (Exception e) {
            showWarning("Файл не найден", path);
            return Collections.emptyList();
        }

        List<Transaction> result = new ArrayList<Transaction>();

        String line;
        while ((line = reader.readLine()) != null) {
            String[] splitedText = line.split(",");
            ArrayList<String> columnList = new ArrayList<String>();

            for (int i = 0; i < splitedText.length; i++) {
                String text = splitedText[i];
                if (isColumnStartWithQuot(text)) {
                    do {
                        if(!isColumnEndAtQuot(text)) {
                            i++;
                            text = text + "," + splitedText[i];
                        }

                    } while(!isColumnEndAtQuot(text));
                    columnList.add(text);
                } else {
                    columnList.add(text);
                }
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
            result.add(tr);

        }

        return result;
    }

    private static boolean isColumnStartWithQuot(String text) {
        String trimText = text.trim();
        boolean bool = trimText.startsWith("\"");
        return bool;
    }

    private static boolean isColumnEndAtQuot(String text) {
        String trimText = text.trim();
        boolean bool = trimText.endsWith("\"");
        return bool;
    }

    private static void showWarning(String message, String source) {
        System.out.printf("Не удалось прочитать '%s' из '%s' \n", message, source);
    }

}
