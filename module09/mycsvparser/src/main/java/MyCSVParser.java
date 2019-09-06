import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class MyCSVParser {
    private static MyCSVParser parser = new MyCSVParser();

    private static final int AMOUNT_OF_FIELDS = 8;
    private static Pattern patternForInt = Pattern.compile("\\d+");
    private static Pattern patternForFract = Pattern.compile("\\d+[\\.|,]\\d{1,2}");
    private static Pattern isOperation = Pattern.compile("[\\/\\\\].+?\\s{3,}");

    Stream<Transaction> getStream(String path) {
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
                    System.out.printf("Не удалось распарсить '%s'\n", line);
                    continue;
                }

                Transaction tr = new Transaction();
                tr.setType(columnList.get(0));
                tr.setAccountNumber(columnList.get(1));
                tr.setCurrency(columnList.get(2));
                tr.setOperationDate(columnList.get(3));
                tr.setReference(columnList.get(4));
                tr.setOperation(columnList.get(5));
                tr.setNamePlace(getNamePlace(columnList.get(5)));
                tr.setIncome(convertToLong(columnList.get(6)));
                tr.setExpense(convertToLong(columnList.get(7)));

                transactions.add(tr);

            }

        } catch (FileNotFoundException e) {
            System.out.printf("Файл не найден '%s'\n", path);
            return Stream.empty();
        } catch (IOException e) {
            e.printStackTrace();
            return Stream.empty();
        }

        return transactions.stream().skip(1);
    }

    private static boolean isColumnStartWithQuot(String text) {
        return text.trim().startsWith("\"");
    }

    private static boolean isColumnEndAtQuot(String text) {
        return text.trim().endsWith("\"");
    }

    private String getNamePlace(String input) {
        Matcher matcher = isOperation.matcher(input);
        String source;
        if (matcher.find()) {
            source = matcher.group(0);
            String[] splittedSource = source.split("\\\\|\\/");
            return splittedSource[splittedSource.length - 1].trim();
        }

        return "unknown";
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

    private MyCSVParser() {

    }

    static public MyCSVParser getMyParser() {
        return parser;
    }
}
