package Transactions;

import java.util.HashMap;
import java.util.Random;

public class Bank {
    private HashMap<String, Account> accounts;
    private final Random random = new Random();

    public HashMap<String, Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(HashMap<String, Account> accounts) {
        this.accounts = accounts;
    }

    public synchronized boolean isFraud(String fromAccountNum, String toAccountNum, long amount)
            throws InterruptedException {
        Thread.sleep(1000);
        return random.nextBoolean();
    }

    /**
     * TODO: реализовать метод. Метод переводит деньги между счетами.
     * Если сумма транзакции > 50000, то после совершения транзакции,
     * она отправляется на проверку Службе Безопасности – вызывается
     * метод isFraud. Если возвращается true, то делается блокировка
     * счетов (как – на ваше усмотрение)
     */
    public synchronized void transfer(String fromAccountNum, String toAccountNum, long amount) {
        Account from = accounts.get(fromAccountNum);
        Account to = accounts.get(toAccountNum);
        System.out.printf("Перевод %d со счета %s на счет %s \n", amount, from, to);
        if (!isChecked(from, to, amount)) {
            return;
        }

        decreaseMoney(from, amount);
        increaseMoney(to, amount);

        try {
            if (amount > 50000 && isFraud(fromAccountNum, toAccountNum, amount)) {
                from.setBlock(true);
                to.setBlock(true);
                System.out.printf("Счета %s и %s заблокированы службой безопасности\n", from.getAccNumber(), to.getAccNumber());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("%s остаток: %d\n", from.getAccNumber(), from.getMoney());
        System.out.printf("%s остаток: %d\n", to.getAccNumber(), to.getMoney());

    }

    /**
     * TODO: реализовать метод. Возвращает остаток на счёте.
     */
    public long getBalance(String accountNum) {
        return accounts.get(accountNum).getMoney();
    }

    private boolean isChecked(Account from, Account to, long value) {
        if (from.isBlock() || to.isBlock()) {
            System.out.println("Один из счетов заблокирован");
            return false;
        }

        if (!(from.getMoney() >= value && value > 0)) {
            System.out.println("Неверная сумма или недостаточно средств");
            return false;
        }

        return true;
    }

    private void increaseMoney(Account account, long amount) {
        account.setMoney(account.getMoney() + amount);
    }

    private void decreaseMoney(Account account, long amount) {
        account.setMoney(account.getMoney() - amount);
    }
}
