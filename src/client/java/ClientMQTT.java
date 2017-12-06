import org.eclipse.paho.client.mqttv3.MqttClient;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;

public class ClientMQTT {
    public static void main(String[] args) throws Exception {
        final InfluxDB influxDB = InfluxDBFactory.connect("http://192.168.20.99:8086", "joseph", "joseph");
        final String dbName = "tacos";
        if (!influxDB.databaseExists(dbName)) {
            influxDB.createDatabase(dbName);
        }

        MqttClient client = new MqttClient("tcp://192.168.20.99", "server");
        client.connect();
        client.setCallback(new SimpleMqttCallBack(influxDB));
        client.subscribe("tacos_compteur");

    }
}
