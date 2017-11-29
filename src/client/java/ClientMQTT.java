import org.eclipse.paho.client.mqttv3.MqttClient;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;

public class ClientMQTT {
    public static void main(String[] args) throws Exception {
        final InfluxDB influxDB = InfluxDBFactory.connect("http://192.168.20.99:8086", "admin", "admin");
        final String dbName = "tacos";
        final String serveurURL = "http://192.168.20.99";

        if (!influxDB.databaseExists(dbName)) {
            influxDB.createDatabase(dbName);
        }

        MqttClient client=new MqttClient(serveurURL, MqttClient.generateClientId());
        client.setCallback( new SimpleMqttCallBack(influxDB) );
        client.connect();
        client.subscribe("tacos_compteur");

        boolean boucle = true;
        while (boucle){
            Thread.sleep(1000);
        }

        client.disconnect();
        client.close();
        influxDB.close();

    }
}
