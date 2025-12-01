package com.cookandroid.weatherrobe.common;

public class CommonUtils {
    public static String addKeywordIcon(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return "";
        }

        String lowerKeyword = keyword.toLowerCase();
        String icon = "";

        if (lowerKeyword.contains("코트") || lowerKeyword.contains("아우터") || lowerKeyword.contains("자켓") || lowerKeyword.contains("패딩") || lowerKeyword.contains("점퍼")) {
            icon = "🧥 ";
        }
        else if (lowerKeyword.contains("니트") || lowerKeyword.contains("스웨터") || lowerKeyword.contains("가디건")) {
            icon = "🥼 ";
        }
        else if (lowerKeyword.contains("목도리") || lowerKeyword.contains("머플러")) {
            icon = "🧣 ";
        }
        else if (lowerKeyword.contains("장갑")) {
            icon = "🧤 ";
        }
        else if (lowerKeyword.contains("바지") || lowerKeyword.contains("청바지") || lowerKeyword.contains("슬랙스")) {
            icon = "👖 ";
        }
        else if (lowerKeyword.contains("치마") || lowerKeyword.contains("스커트") || lowerKeyword.contains("원피스")) {
            icon = "👗 ";
        }
        else if (lowerKeyword.contains("우산") || lowerKeyword.contains("비")) {
            icon = "☔ ";
        }
        else if (lowerKeyword.contains("바람") || lowerKeyword.contains("추위") || lowerKeyword.contains("강풍")) {
            icon = "💨 ";
        }
        else {
            icon = "✨ ";
        }

        return icon + keyword;
    }
}
