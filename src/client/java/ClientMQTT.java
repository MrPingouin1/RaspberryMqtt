import org.eclipse.paho.client.mqttv3.MqttClient;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;


public class ClientMQTT {
    public static void main(String[] args) throws Exception {
        // Adresse et logs pour se connecter à la base de données InfluxDB
        final InfluxDB influxDB = InfluxDBFactory.connect("http://192.168.20.99:8086", "joseph", "joseph");
        // Nom de notre database
        final String dbName = "tacos";

        if (!influxDB.databaseExists(dbName)) {
            influxDB.createDatabase(dbName);
        }

        // Adresse du serveur MQTT
        MqttClient client = new MqttClient("tcp://192.168.20.99", "server");
        client.connect();
        client.setCallback(new SimpleMqttCallBack(influxDB));
        // Inscription au topic sur lequel la passerelle publie les messages
        client.subscribe("tacos_compteur");

    }
}
