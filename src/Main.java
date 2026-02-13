public class Main {
    public static void main(String[] args) {
        int K = 5; // Sunucu sayısı
        int TOTAL_REQUESTS = 10000;

        Server[] servers = new Server[K];
        for (int i = 0; i < K; i++) {
            servers[i] = new Server(i, 50.0 + (i * 10)); // Başlangıçta farklı gecikmelere sahipler
        }

        LoadBalancer softmax = new SoftmaxBalancer(K, 5.0, 0.1);

        double totalSoftmaxLatency = 0;

        System.out.println("Simülasyon Başlıyor...");
        long startTime = System.nanoTime();

        for (int i = 0; i < TOTAL_REQUESTS; i++) {
            // Sunucuların performansları zamanla değişiyor (Non-stationary)
            for (Server s : servers) s.drift();

            // Softmax Seçimi
            int selectedSoftmax = softmax.selectServer();
            double latencySoftmax = servers[selectedSoftmax].getResponseTime();
            softmax.update(selectedSoftmax, latencySoftmax);
            totalSoftmaxLatency += latencySoftmax;
        }

        long endTime = System.nanoTime();
        double executionTimeMs = (endTime - startTime) / 1_000_000.0;

        System.out.println("--- SONUÇLAR ---");
        System.out.printf("Softmax Ortalama Gecikme: %.2f ms\n", (totalSoftmaxLatency / TOTAL_REQUESTS));
        System.out.printf("Çalışma Zamanı (Runtime): %.2f ms\n", executionTimeMs);
        System.out.println("Zaman Karmaşıklığı (Time Complexity): Seçim başına O(K)");
    }
}
