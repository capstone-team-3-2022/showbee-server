package com.capstone3.showbee.model;

public class MonthlyFinancial {
    private String date;
    private int income;
    private int expense;

    public String getDate(){
        return date;
    }
    public int getIncome(){
        return income;
    }
    public int getExpense(){
        return expense;
    }

    public MonthlyFinancial(String date, int income, int expense){
        this.date = date;
        this.income = income;
        this.expense = expense;
    }
}
