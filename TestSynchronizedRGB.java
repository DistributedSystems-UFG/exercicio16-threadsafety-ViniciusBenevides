public class TestSynchronizedRGB {
    public static void main(String[] args) throws InterruptedException {
        SynchronizedRGB color = new SynchronizedRGB(0, 0, 0, "Pitch Black");

        // Thread 1: Leitura composta (getRGB e getName)
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                int rgb = color.getRGB();
                // Pequena pausa para aumentar a chance de interleaving
                Thread.yield();
                String name = color.getName();
                
                // Verifica se houve inconsistencia
                if ((rgb == 0 && !name.equals("Pitch Black")) ||
                    (rgb != 0 && name.equals("Pitch Black"))) {
                    System.out.println("INCONSISTENCIA ENCONTRADA no SynchronizedRGB!");
                    System.out.println("RGB lido: " + rgb + " | Nome lido: " + name);
                    System.exit(1);
                }
            }
        });

        // Thread 2: Atualização (set)
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                if (i % 2 == 0) {
                    color.set(255, 255, 255, "White");
                } else {
                    color.set(0, 0, 0, "Pitch Black");
                }
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        
        System.out.println("Teste do SynchronizedRGB concluído (se chegou aqui, nao deu erro - mas o erro é possível).");
    }
}
