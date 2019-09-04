import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Transaction {
    private static Pattern isOperation = Pattern.compile("[\\/\\\\].+?\\s{3,}");

    private String type;
    private String accountNumber;
    private String currency;
    private String operationDate;
    private String reference;
    private String operation;
    private String income;
    private String expense;
    private String operationName;

    public String getOperationName() {
        return operationName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(String operationDate) {
        this.operationDate = operationDate;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
        this.operationName = getOperationName(operation);
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getExpense() {
        return expense;
    }

    public void setExpense(String expense) {
        this.expense = expense;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "type='" + type + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", currency='" + currency + '\'' +
                ", operationDate='" + operationDate + '\'' +
                ", reference='" + reference + '\'' +
                ", operation='" + operation + '\'' +
                ", income='" + income + '\'' +
                ", expense='" + expense + '\'' +
                '}';
    }

    private String getOperationName(String input) {
        Matcher matcher = isOperation.matcher(input);
        String source;
        if (matcher.find()) {
            source = matcher.group(0);
            String[] splittedSource = source.split("\\\\|\\/");
            return splittedSource[splittedSource.length - 1].trim();
        }

        return "unknown";
    }

}
