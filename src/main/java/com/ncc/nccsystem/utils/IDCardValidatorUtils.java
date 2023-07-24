package com.ncc.nccsystem.utils;

import java.util.regex.Pattern;

public class IDCardValidatorUtils {
    private static final String ID_CARD_REGEX = "^\\d{17}(\\d|x|X)$";

    public boolean validateIDCard(String idCard) {
        // 验证身份证格式
        if (!Pattern.matches(ID_CARD_REGEX, idCard)) {
            return false;
        }

        // 验证身份证校验码
        int[] weights = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        char[] checkCodes = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};

        int sum = 0;
        for (int i = 0; i < 17; i++) {
            int digit = Character.digit(idCard.charAt(i), 10);
            sum += digit * weights[i];
        }

        int mod = sum % 11;
        char checkCode = checkCodes[mod];

        char lastChar = Character.toUpperCase(idCard.charAt(17));
        return lastChar == checkCode;
    }
}
