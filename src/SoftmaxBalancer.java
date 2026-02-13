import java.util.Arrays;
import java.util.Random;

public class SoftmaxBalancer implements LoadBalancer {
    private final double[] qValues; // Her sunucu için tahmini ödül (kalite) değeri
    private final int[] selectionCounts;
    private final double temperature; // Keşif/Sömürü dengesi (Tau)
    private final double alpha; // Öğrenme oranı (Geçmişi ne kadar unutacağımız)
    private final Random random;

    public SoftmaxBalancer(int serverCount, double temperature, double alpha) {
        this.qValues = new double[serverCount];
        this.selectionCounts = new int[serverCount];
        this.temperature = temperature;
        this.alpha = alpha;
        this.random = new Random();
    }

    @Override
    public int selectServer() {
        double[] probabilities = new double[qValues.length];
        double maxQ = Arrays.stream(qValues).max().orElse(0.0);
        double sumExp = 0.0;

        // NÜMERİK STABİLİTE ÇÖZÜMÜ:
        // Math.exp() çok büyük sayılarda overflow'a (Infinity) neden olur.
        // Bunu önlemek için tüm değerlerden maxQ'yu çıkarıyoruz. 
        // Matematiksel olarak olasılık dağılımını değiştirmez ama kodun patlamasını engeller.
        for (int i = 0; i < qValues.length; i++) {
            probabilities[i] = Math.exp((qValues[i] - maxQ) / temperature);
            sumExp += probabilities[i];
        }

        // Rulet tekerleği (Roulette Wheel) seçimi
        double rand = random.nextDouble() * sumExp;
        double cumulativeProbability = 0.0;

        for (int i = 0; i < probabilities.length; i++) {
            cumulativeProbability += probabilities[i];
            if (rand <= cumulativeProbability) {
                return i;
            }
        }
        return qValues.length - 1;
    }

    @Override
    public void update(int serverId, double responseTime) {
        selectionCounts[serverId]++;
        // Ödül: Gecikmenin negatifi (Daha düşük gecikme = daha yüksek ödül)
        double reward = -responseTime;

        // Non-stationary ortam için sabit adımlı Q-değeri güncellemesi (Exponential Recency-Weighted Average)
        qValues[serverId] = qValues[serverId] + alpha * (reward - qValues[serverId]);
    }
}
