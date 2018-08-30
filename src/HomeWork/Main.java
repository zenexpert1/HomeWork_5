package HomeWork;

import java.util.Arrays;

public class Main {
    static final int size = 10000000;
    static final int h = size / 2;

    public static void main(String[] args) {

        float[] arr1 = new float[size];
        float[] arr2 = new float[size];


        System.out.println("Главный поток: --- запуск метода м2");
        m2(arr2,new ArraysCombiner(arr2));
        System.out.println("Главный поток: --- запуск метода м1");
        m1(arr1);
        System.out.println("Главный поток: --- продолжение работы метода маин");
        if( Arrays.equals(arr1, arr2)) System.out.println("Главный поток: Результаты расчётов одинаковы");

        System.out.println("Главный поток: --- завершение работы метода маин");

    }

    public static void m1(float[] arr){

        long a = System.currentTimeMillis();
        for (int i = 0; i < arr.length; i++) {
            arr[i]=1;
        }
        System.out.printf("Главный поток, метод м1:  На создание массива ушло %d миллисекунд ",System.currentTimeMillis() - a);
        System.out.println();

        for (int i = 0; i < arr.length; i++) {
            arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }
        System.out.printf("Главный поток, метод м1: Первый метод закончил работу за %d миллисекунд. Расчёт завершён ",System.currentTimeMillis() - a);
        System.out.println();
    }

    public static void m2(float[] arr, ArraysCombiner AC){
        long a = System.currentTimeMillis();

        for (int i = 0; i < arr.length; i++) {
            arr[i]=1;
        }
        System.out.printf("Главный поток, метод м2: На создание массива ушло %d миллисекунд ",System.currentTimeMillis() - a);
        System.out.println();

        float[] a1 = new float[h];
        float[] a2 = new float[h];

        System.arraycopy(arr, 0, a1, 0, h);
        System.arraycopy(arr, h, a2, 0, h);

        System.out.printf("Главный поток, метод м2: На деление массива на две части ушло %d миллисекунд ",System.currentTimeMillis() - a);
        System.out.println();


        Thread th1 = new Thread(
                () -> {

                    System.out.printf("Первый поток начал работу через %d миллисекунд от запуска метода ",System.currentTimeMillis() - a);
                    System.out.println();
                    for (int i = 0; i < a1.length; i++) {
                        a1[i] = (float)(a1[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
                    }
                    AC.putA1(a1);
                    System.out.printf("Первый поток закончил расчет за %d миллисекунд ",System.currentTimeMillis() - a);
                    System.out.println();
                }
        );

        Thread th2 = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        System.out.printf("Второй поток начал работу через %d миллисекунд от запуска метода ",System.currentTimeMillis() - a);
                        System.out.println();
                        int pos;
                        for (int i = 0; i < a2.length; i++) {
                            pos = i + h;
                            a2[i] = (float) (a2[i] * Math.sin(0.2f + pos / 5) * Math.cos(0.2f + pos / 5) * Math.cos(0.4f + pos / 2));
                        }
                        AC.putA2(a2);
                        System.out.printf("Второй поток закончил расчет за %d миллисекунд ", System.currentTimeMillis() - a);
                        System.out.println();
                    }
                }
        );

        th1.start();
        th2.start();

        System.out.printf("Главный поток, метод м2: Метод, создающий потоки, закончил работу за %d миллисекунд, но расчёты продолжаются. ",System.currentTimeMillis() - a);
        System.out.println();




    }


    static class ArraysCombiner{
        private float[] arr;
        private float[] a1;
        private float[] a2;

        ArraysCombiner(float[] arr){
            System.out.println("ArraysCombiner: Создан экземпляр класса");

            this.arr = arr;
        }

        public void putA1(float[] a1) {
            System.out.println("ArraysCombiner: Получена первая половина массива");
            this.a1 = a1;
            if(a2 != null) this.Combine();
        }

        public void putA2(float[] a2) {
            System.out.println("ArraysCombiner: Получена вторая половина массива");
            this.a2 = a2;
            if(a2 != null) this.Combine();
        }

        void Combine(){
            System.out.println("ArraysCombiner: Получены обе половины массива");
            System.arraycopy(a1, 0, arr, 0, h);
            System.arraycopy(a2, 0, arr, h, h);
        }
    }
}

