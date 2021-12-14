public class StringRecursion {
    public static void printLetters(String str) {
        if (str == null || str.equals("")) {
            return;
        } else if (str.length() == 1) {
            System.out.println(str.charAt(0));
        } else {
            System.out.print(str.charAt(0) + ", ");
            printLetters(str.substring(1));
        }
    }

    public static String replace(String str, char oldChar, char newChar) {
        if (str == null) {
            return null;
        } else if (str.equals("")) {
            return "";
        } else {
            if (str.charAt(0) == oldChar) {
                str = newChar + str.substring(1);
            }
            return str.charAt(0) + replace(str.substring(1), oldChar, newChar);
        }
    }

    public static int indexOf(char ch, String str) {
        if (str == null || str.equals("") || (str.length() == 1 && ch != str.charAt(0))) {
            return -1;
        } else if (ch == str.charAt(0)) {
            return 0;
        }

        int rest = indexOf(ch, str.substring(1));

        if (rest == -1) {
            return rest;
        } else {
            return rest + 1;
        }
    }


    public static void main(String[] args) {
        String s = "Rabbit";

        printLetters(s);
        System.out.println(replace(s, 'e', 'y'));
        System.out.println(indexOf('b', s));

    }
}
