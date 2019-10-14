package Transactions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Main {

    public static void main(String[] args) {

//        HashMap<String, Account> accounts = generateAccounts();
        Bank bank = new Bank();

        HashMap<String, Account> accounts = new HashMap<>();
        Account acc1 = new Account("111", 500000);
        Account acc2 = new Account("222", 500000);
        Account acc3 = new Account("333", 1000000);

        accounts.put(acc1.getAccNumber(), acc1);
        accounts.put(acc2.getAccNumber(), acc2);
        accounts.put(acc3.getAccNumber(), acc3);

        accounts.forEach((k, v) -> System.out.println(k + ": " + v));

        bank.setAccounts(accounts);

        ArrayList<Thread> threads = new ArrayList<>();
        threads.add(new Thread(() -> {
            for (int i = 0; i < 30; i++) {
                bank.transfer(acc1.getAccNumber(), acc2.getAccNumber(), 1000 + i * 1000);
            }
        }));
        threads.add(new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                bank.transfer(acc2.getAccNumber(), acc3.getAccNumber(), 5000);
            }
        }));
        threads.add(new Thread(() -> {
            for (int i = 0; i < 15; i++) {
                bank.transfer(acc3.getAccNumber(), acc1.getAccNumber(), 5000 * i);
            }
        }));

        threads.forEach(Thread::start);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("111: " + bank.getBalance("111") + " статус: " + !acc1.isBlock());
        System.out.println("222: " + bank.getBalance("222") + " статус: " + !acc2.isBlock());
        System.out.println("333: " + bank.getBalance("222") + " статус: " + !acc3.isBlock());
    }

    private static void doTransactionRun(Account account) {

    }
}
