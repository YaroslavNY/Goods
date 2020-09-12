import java.io.*;
import java.lang.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

class Main {
private String word = null;
private ArrayList<String> list = new ArrayList<> ();
private Scanner scan = new Scanner(System.in);

    public static void main(String[] args){
        Main main = new Main();
        main.read();
    }

    private void read (){
        list.clear();
        System.out.println("Для работы с программой используйте команды \"Записать\", \"Найти\", \"Список\" , \"Изменить\", \"Удалить все\", \"Выход\".");
        while(!scan.hasNext()){
            read();
        }
        word = scan.next();
        word = word.toLowerCase();
        if (word.equals("найти")){
            System.out.println("Введите название товара");
            find();
        }
        else if (word.equals("записать")){
            write();
        }
        else if (word.equals("список")){
            System.out.println("Все данные: ");
            try (FileReader fr = new FileReader("List.txt")){
                BufferedReader br = new BufferedReader(fr);
                word = br.readLine();
                while (word != null){
                    list.add(word);
                    word = br.readLine();
                }
            }
            catch (Exception e){
                System.out.println("Данные отсутствуют");
            }
            Collections.sort(list);
            getList(list);
//            for (String x : list) System.out.println(x);
//            read();
        }
        else if (word.equals("удалить все")){
            String message = null;
            System.out.println("Вы уверены что хотите удалить все данные? Да/Нет");
            if (scan.hasNext()){
                message = scan.next();
            }
            message = message.toLowerCase();
            if(message.equals("да")) {
                File file = new File("List.txt");
                file.delete();
                System.out.println("Данные удалены, продолжить работу? Да/Нет");
                word = scan.next();
                word = word.toLowerCase();
                if (word.equals("да")) read();
                else System.exit(0);
            }
            else{
                read();
            }


        }
        else if (word.equals("выход")){
            System.exit(0);
        }
        else if (word.equals("изменить")){
            change();
        }
        else {
            System.out.println("Введите корректную команду" + "\n");
            word = null;
            read();
        }
        scan.close();
    }
    private int find (){
        word = scan.next();
        word = word.toUpperCase();
        int c = 0;
        try (FileReader fr = new FileReader("List.txt")) {
            BufferedReader bf = new BufferedReader(fr);
            while (c == 0) {
                String data = bf.readLine();
                data = data.toUpperCase();
                try {
                    if (data.contains(word)) {
                        System.out.println("Результаты поиска: ");
                        System.out.println(data);
                        c++;
                        System.out.println("Желаете продолжить поиск? Да/Нет");
                        if (scan.next().toLowerCase().equals("да")) {
                            System.out.println("Введите название товара");
                            find();
                        }
                        else read();
                    }
                }
                catch (NullPointerException e){
                    System.out.println("Данные не найдены");
                    read();
 }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return c;
    }

    private void write () {
        System.out.println("Введте значение в формате \"Товар, кол-во, стоимость\"");
        scan.nextLine();
        word = scan.nextLine();
        word = word.replaceAll(",", "");
        if (word.split(" ").length == 3) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("List.txt", true))) {
                bw.write(word + "\n");
                bw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Данные: " + word + " Записаны");
            System.out.println("Продолжить запись? Да/Нет");
            word = scan.next();
            if (word.equals("Да")) {
                write();
            } else {
                System.exit(0);
            }
        }
        else {
            System.out.println("Недостаточно данных для записи");
            write();
        }
    }
    private void change (){
        System.out.println("Введите название товара который хотите изменить");
        String newWord = scan.next();
        boolean check = false;
        list.clear();

        try (FileReader fr = new FileReader("List.txt")){
            BufferedReader br = new BufferedReader(fr);
            word = br.readLine();
            while (word != null) {
                if (word.contains(newWord)){
                    check = true;
                }
                list.add(word);
                word = br.readLine();
            }
        }
        catch (Exception e){
            System.out.println("Данные отсутствуют");
            System.exit(0);
        }

        if (check){

            String[][] wordsArray = new String[list.size()][3];
            String[] substr;
            for (int i = 0; i < list.size(); i++) {
                substr  = list.get(i).split(" ");
                for (int j = 0; j < 1; j++) {
                    wordsArray[i][j] = substr[j];
                    for (int k = 1; k < 3; k++) {
                        wordsArray[i][k] = substr[k];
                    }
                }
            }
            int counter = 0;

            for (int i = 0; i < wordsArray.length; i++) {
                String checkWord = wordsArray[i][0].replaceAll(",", "");
                if (checkWord.equals(newWord)){
                    counter = i;
                    System.out.println("Текущее значение: ");
                    for (int j = 0; j < 3; j++) {
                        System.out.print(wordsArray[i][j] + " ");
                    }
                }
            }
            System.out.println("\n"+"Введите значения котрые хотите переписать - в формате Товар, количество, стоимость");
            while (!scan.hasNext()) {
                scan.next();
            }
            scan.nextLine();
            word = scan.nextLine();
            word.replaceAll(",", "");
            substr = word.split(" ");
            if (substr.length < 3){
                System.out.println("Введено неверное или неполное значение" + "\n");
                wordsArray = null;
                word = "";
                change();
            }
            for (int i = 0; i < 3; i++) {
                wordsArray[counter][i] = substr [i];
            }
            list.clear();
            File file = new File("List.txt");
            file.delete();
            for (int i = 0; i < wordsArray.length; i++) {
                word = "";
                for (int j = 0; j < 3; j++) {
                    word += wordsArray [i][j] + " ";
                }
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))){
                    bw.write(word + "\n");
                    bw.flush();
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
            System.out.println("Данные изменены, хотите продолжить работу? Да/Нет");
            word = scan.next();
            word = word.toLowerCase();
            if(word.equals("да")) read();
            else System.exit(0);
        }
        else {
            System.out.println("Искомое значение не найдено" + "\n");
            read();
        }
    }
    public void getList (ArrayList<String> array){
        System.out.printf("%-20s%-20s%-20s%n", "Наименование", "Кол-во", "Стоимость" + "\n");
        for (int i = 0; i < array.size(); i++) {
            String[] line = array.get(i).split(",");
            System.out.printf("%-20s%-22s%-20s%n", line[0].toUpperCase(), line[1], line[2]);
            line = null;
        }
        read();
    }
        }