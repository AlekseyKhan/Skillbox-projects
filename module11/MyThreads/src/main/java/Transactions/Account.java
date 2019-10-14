package Transactions;

import java.util.concurrent.atomic.AtomicLong;

public class Account
{
    private AtomicLong money = new AtomicLong();
    private String accNumber;
    private boolean isBlock;

    public Account(String accNumber, long money) {
        this.accNumber = accNumber;
        this.money.set(money);
        isBlock = false;
    }

    public long getMoney() {
        return money.get();
    }

    public void setMoney(long money) {
        this.money.set(money);
    }

    public String getAccNumber() {
        return accNumber;
    }

    public void setAccNumber(String accNumber) {
        this.accNumber = accNumber;
    }

    public boolean isBlock() {
        return isBlock;
    }

    public void setBlock(boolean block) {
        isBlock = block;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accNumber='" + accNumber + '\'' +
                ", money=" + money.get() +
                '}';
    }
}
