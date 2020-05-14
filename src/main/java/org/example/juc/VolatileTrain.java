package org.example.juc;

/**
 * Java内存模型告诉我们，各个线程会将共享变量从主内存中拷贝到工作内存，然后执行引擎会基于工作内存中的数据进行操作处理。
 * 线程在工作内存进行操作后何时会写到主内存中？这个时机对普通变量是没有规定的，而针对volatile修饰的变量给java虚拟机特殊
 * 的约定，线程对volatile变量的修改会立刻被其他线程所感知，即不会出现数据脏读的现象，从而保证数据的“可见性”。
 *
 * 现在我们有了一个大概的印象就是：被volatile修饰的变量能够保证每个线程能够获取该变量的最新值，从而避免出现数据脏读的现象。
 *
 * volatile变量可以通过缓存一致性协议保证每个线程都能获得最新值，即满足数据的“可见性”
 */
public class VolatileTrain {

    private int a = 0;
    private volatile boolean flag = false;

    public void writer(){
        a = 1;          //1
        flag = true;   //2
        System.out.println("writer() , a: " + a);
    }

    public void reader(){
        System.out.println("reader(), flag: " + flag );
        if(flag){      //3
            int i = a; //4
            System.out.println("i: " +i  );
        }
    }

    public static void main(String[] args) throws InterruptedException {
        VolatileTrain train = new VolatileTrain();
        new Thread(new Thread2()).start();
        Thread.sleep(1000);
        System.out.println("main flag:" + train.flag);
        new Thread(new Thread1()).start();
//        VolatileTrain volatileTrain = new VolatileTrain();
//        volatileTrain.writer();

    }

}

class Thread1 implements Runnable {

    @Override
    public void run() {
        VolatileTrain volatileTrain = new VolatileTrain();
        volatileTrain.reader();
    }
}

class Thread2 implements Runnable {

    @Override
    public void run() {
        VolatileTrain volatileTrain = new VolatileTrain();
        volatileTrain.writer();
    }
}
