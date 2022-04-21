package Auxiliares;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Aleatorio {

    public static int nroRandom(int min, int max){
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    public static boolean booleanRandom(){
        Random random = new Random();
        return random.nextBoolean();
    }
}
