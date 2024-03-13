package com.example.shoppingplatform.ui.tool;

import java.util.ArrayList;

public class Convert {


    //转换方法：string->list
    public ArrayList<Integer> convertStringToArrayList(String str) {
//        ArrayList<Integer> list = new ArrayList<>();
//        if (str != null && str.length() > 1) { // 确保字符串不为空且长度大于1（去除开头和结尾的括号）
//            str = str.substring(1, str.length() - 1); // 去除开头和结尾的括号
//            String[] elements = str.split(", "); // 按逗号和空格分割字符串
//            for (String element : elements) {
//                list.add(Integer.parseInt(element)); // 将字符串转换为整数并添加到列
//            }
//        }
//        return list;

        if (str == null || str.trim().isEmpty()) {
            return new ArrayList<>();
        }
        String[] elements = str.split(","); // 按逗号分割字符串
        ArrayList<Integer> list = new ArrayList<>();
        for (String element : elements) {
            try {
                // 去除每个元素可能的前后空格，并转换为整数
                int number = Integer.parseInt(element.trim());
                list.add(number);
            } catch (NumberFormatException e) {
                // 转换失败时记录错误或采取其他措施
                System.err.println("Failed to parse number from string: " + element);
            }
        }

        return list;
}

    //转换方法：list->string
    public String convertArrayListToString(ArrayList<Integer> list) {
//        StringBuilder sb = new StringBuilder();
//        sb.append("[");
//        for (int i = 0; i < list.size(); i++) {
//            sb.append(list.get(i));
//            if (i < list.size() - 1) {
//                sb.append(", ");
//            }
//        }
//        sb.append("]");
//        return  sb.toString();
//    }

        if (list == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i < list.size() - 1) {
                sb.append(","); // 添加逗号作为分隔符
            }
        }
        return sb.toString();
    }
}
