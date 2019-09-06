import java.io.*;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws IOException {
        String pathToCSV = "data/movementList.csv";

        MyCSVParser parser = MyCSVParser.getMyParser();
        Stream<Transaction> stream = parser.getStream(pathToCSV);

        Summary total = stream.map(transaction -> new Summary(transaction.getIncome(), transaction.getExpense()))
                            .reduce(new Summary(), (a,b) -> a.calculate(b));

        System.out.printf("Общий приход: %.02f\nОбщий расход: %.02f\n", total.getIncome() / 100.0, total.getExpense() / 100.0);
        System.out.println("---\nСуммы по контрагентам: Доход | Расход");

        Collector<Summary, ?, Summary> reducing = Collectors.reducing(new Summary(), (a, b) -> a.calculate(b));
        parser.getStream(pathToCSV).collect(Collectors.groupingBy(Transaction::getNamePlace,
                Collectors.mapping(tr -> new Summary(tr.getIncome(), tr.getExpense())
                        , reducing)))
                .forEach((name, summary) ->
                                System.out.printf("%s: %.02f  |  %.02f\n",name, summary.getIncome() / 100.0, summary.getExpense() / 100.0)
                );

    }

    private static class Summary {
        long income;
        long expense;

        Summary(long income, long expense) {
            this.income = income;
            this.expense = expense;
        }

        Summary() {
            this(0,0);
        }


        public long getIncome() {
            return income;
        }

        public long getExpense() {
            return expense;
        }

        private Summary calculate(Summary b) {
            return new Summary(this.income + b.income, this.expense + b.expense);
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
