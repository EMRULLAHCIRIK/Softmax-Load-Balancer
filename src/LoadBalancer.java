public interface LoadBalancer {
    int selectServer();
    void update(int serverId, double responseTime);
}
