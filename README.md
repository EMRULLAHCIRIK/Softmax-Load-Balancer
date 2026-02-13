# Softmax Action Selection ile Dağıtık Yük Dengeleyici (Load Balancer)

Bu proje, K adet farklı sunucudan oluşan ve performansları zamanla değişen (non-stationary) bir kümede toplam gecikmeyi minimize eden istemci taraflı bir yük dengeleyici (client-side load balancer) simülasyonudur.

## Projenin Amacı
Geleneksel "Round-Robin" (sırayla dağıtım) veya "Random" (rastgele dağıtım) algoritmaları, sunucuların anlık yük durumlarını ve performans dalgalanmalarını göz ardı eder. Bu projede, geçmiş performansa dayalı olasılıksal bir seçim yapan **Softmax Action Selection** algoritması implemente edilmiştir.

Sistem, en düşük gecikmeye sahip sunucuları sömürürken (exploitation), anlık performans değişimlerini kaçırmamak için diğer sunucuları da belli bir olasılıkla keşfeder (exploration).

## Teknik Detaylar ve Nümerik Stabilite
Softmax fonksiyonu üslü sayılar ($e^x$) ile çalıştığı için, x değerinin çok büyümesi durumunda programlama dillerinde **Overflow (Sınır Aşımı)** ve `Infinity` (veya `NaN`) hataları yaşanır.

Bu projede nümerik stabilite problemini çözmek için "Max Çıkarma" (Subtract Max) hilesi uygulanmıştır:
1. Tüm tahmin edilen Q değerleri içinden en büyük olanı (maxQ) bulunur.
2. Üstel işlem yapılmadan önce bu değer tüm Q değerlerinden çıkarılır: $e^{(Q_i - maxQ) / \tau}$
3. Bu sayede en yüksek üstel değer $e^0 = 1$ olur ve overflow engellenirken, olasılık dağılımının matematiksel doğruluğu korunur.

## Zaman Karmaşıklığı (Runtime Analysis)
Her istek geldiğinde, Softmax algoritması tüm K sunucunun olasılıklarını hesaplar. Toplam zaman karmaşıklığı sunucu sayısına bağlı olarak **O(K)**'dır. Bellek karmaşıklığı ise Q-değerlerini tuttuğumuz dizi sebebiyle yine **O(K)**'dır.

## Kurulum ve Çalıştırma
1. Repoyu klonlayın.
2. IntelliJ IDEA veya herhangi bir Java IDE'si ile projeyi açın.
3. `Main.java` dosyasını çalıştırarak simülasyon sonuçlarını konsolda görüntüleyin.
