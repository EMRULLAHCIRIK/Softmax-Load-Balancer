import java.util.Random;

public class Server {
    private final int id;
    private double currentMeanLatency;
    private final Random random;

    public Server(int id, double initialLatency) {
        this.id = id;
        this.currentMeanLatency = initialLatency;
        this.random = new Random();
    }

    // Non-stationary yapı: Ortalama gecikme süresi zamanla değişir
    public void drift() {
        // Her adımda ortalama gecikme küçük bir miktar kayar
        currentMeanLatency += random.nextGaussian() * 2.0;
        if (currentMeanLatency < 10) currentMeanLatency = 10; // Minimum 10ms gecikme
    }

    // Gürültülü yanıt süresi döndürür
    public double getResponseTime() {
        // Anlık gürültü (noise) eklenerek yanıt süresi simüle edilir
        return currentMeanLatency + random.nextGaussian() * 5.0;
    }

    public int getId() { return id; }
}
