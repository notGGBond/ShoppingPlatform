package com.example.shoppingplatform.model;

public class ConstellationData {



        private String name; // 星座名称
        private String datetime; // 日期
        private int date; // 日期数字形式
        private String all; // 综合指数
        private String color; // 幸运色
        private String health; // 健康指数
        private String love; // 爱情指数
        private String money; // 财运指数
        private int number; // 幸运数字
        private String friend; // 速配星座
        private String summary; // 今日概述
        private String work; // 工作指数
        private int errorCode; // 返回码

        // 构造方法
        public ConstellationData (String name, String datetime, int date, String all, String color,
                                 String health, String love, String money, int number, String friend,
                                 String summary, String work, int errorCode) {
            this.name = name;
            this.datetime = datetime;
            this.date = date;
            this.all = all;
            this.color = color;
            this.health = health;
            this.love = love;
            this.money = money;
            this.number = number;
            this.friend = friend;
            this.summary = summary;
            this.work = work;
            this.errorCode = errorCode;
        }

        // Getter和Setter方法
        public String getXName() {
            return name;
        }

        public void setXName(String name) {
            this.name = name;
        }

        public String getDatetime() {
            return datetime;
        }

        public void setDatetime(String datetime) {
            this.datetime = datetime;
        }

        public int getDate() {
            return date;
        }

        public void setDate(int date) {
            this.date = date;
        }

        public String getAll() {
            return all;
        }

        public void setAll(String all) {
            this.all = all;
        }

        public String getXColor() {
            return color;
        }

        public void setXColor(String color) {
            this.color = color;
        }

        public String getHealth() {
            return health;
        }

        public void setHealth(String health) {
            this.health = health;
        }

        public String getLove() {
            return love;
        }

        public void setLove(String love) {
            this.love = love;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public int getXNumber() {
            return number;
        }

        public void setXNumber(int number) {
            this.number = number;
        }

        public String getFriend() {
            return friend;
        }

        public void setFriend(String friend) {
            this.friend = friend;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getWork() {
            return work;
        }

        public void setWork(String work) {
            this.work = work;
        }

        public int getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(int errorCode) {
            this.errorCode = errorCode;
        }


    }

