public class TestImmutableRGB {
    public static void main(String[] args) throws InterruptedException {
        // Objeto inicial
        ImmutableRGB color = new ImmutableRGB(0, 0, 0, "Pitch Black");

        // Usamos um array de 1 elemento para poder alterar a referencia da variavel
        // e simular a "mudança" da cor ativa.
        final ImmutableRGB[] currentColor = { color };

        // Thread 1: Leitura composta
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                // Ao pegar a referencia local, a thread trabalha com aquela copia inalterável
                ImmutableRGB myColor = currentColor[0];
                int rgb = myColor.getRGB();
                Thread.yield();
                String name = myColor.getName();
                
                if ((rgb == 0 && !name.equals("Pitch Black")) ||
                    (rgb != 0 && name.equals("Pitch Black"))) {
                    System.out.println("INCONSISTENCIA ENCONTRADA no ImmutableRGB!");
                    System.out.println("RGB lido: " + rgb + " | Nome lido: " + name);
                    System.exit(1);
                }
            }
        });

        // Thread 2: Atualização (criando novos objetos pois é imutável)
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                if (i % 2 == 0) {
                    currentColor[0] = new ImmutableRGB(255, 255, 255, "White");
                } else {
                    currentColor[0] = new ImmutableRGB(0, 0, 0, "Pitch Black");
                }
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        
        System.out.println("Teste do ImmutableRGB concluído com sucesso. Nenhuma inconsistência foi gerada.");
    }
}
