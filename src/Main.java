import java.util.Random;

public class Main {
    public static void main(String[] args) {
        int K = 5; // Sunucu sayısı
        int TOTAL_REQUESTS = 10000;
        Random random = new Random();
        
        // Sunucuları oluştur
        Server[] serversForSoftmax = new Server[K];
        Server[] serversForRandom = new Server[K];
        for (int i = 0; i < K; i++) {
            // Başlangıç gecikmeleri 50ms, 60ms, 70ms, 80ms, 90ms
            serversForSoftmax[i] = new Server(i, 50.0 + (i * 10)); 
            serversForRandom[i] = new Server(i, 50.0 + (i * 10)); 
        }

        LoadBalancer softmax = new SoftmaxBalancer(K, 5.0, 0.1);
        
        double totalSoftmaxLatency = 0;
        double totalRandomLatency = 0;

        System.out.println("Simülasyon Başlıyor (" + TOTAL_REQUESTS + " İstek)...\n");
        long startTime = System.nanoTime();

        for (int i = 0; i < TOTAL_REQUESTS; i++) {
            // Sunucuların performansları zamanla değişiyor (Non-stationary)
            for (int j = 0; j < K; j++) {
                serversForSoftmax[j].drift();
                serversForRandom[j].drift();
            }

            // 1. SOFTMAX İLE SEÇİM
            int selectedSoftmax = softmax.selectServer();
            double latencySoftmax = serversForSoftmax[selectedSoftmax].getResponseTime();
            softmax.update(selectedSoftmax, latencySoftmax);
            totalSoftmaxLatency += latencySoftmax;

            // 2. RANDOM (RASTGELE) İLE SEÇİM (Kıyaslama için)
            int selectedRandom = random.nextInt(K);
            double latencyRandom = serversForRandom[selectedRandom].getResponseTime();
            totalRandomLatency += latencyRandom;
        }

        long endTime = System.nanoTime();
        double executionTimeMs = (endTime - startTime) / 1_000_000.0;

        System.out.println("--- TEKNİK DEĞERLENDİRME VE SONUÇLAR ---");
        System.out.printf("Random (Rastgele) Ortalama Gecikme: %.2f ms\n", (totalRandomLatency / TOTAL_REQUESTS));
        System.out.printf("Softmax Ortalama Gecikme        : %.2f ms\n", (totalSoftmaxLatency / TOTAL_REQUESTS));
        System.out.println("----------------------------------------");
        System.out.printf("Çalışma Zamanı (Runtime)        : %.2f ms\n", executionTimeMs);
        System.out.println("Zaman Karmaşıklığı              : Softmax için seçim başına O(K)");
    }
}
