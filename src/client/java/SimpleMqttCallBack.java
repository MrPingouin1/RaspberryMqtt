import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.influxdb.InfluxDB;

public class SimpleMqttCallBack implements MqttCallback {
    private InfluxDB connectionBD;

    public SimpleMqttCallBack(InfluxDB influxDB) {
        super();
        this.connectionBD = influxDB;
    }

    public void connectionLost(Throwable throwable) {
        System.out.println("Connection to MQTT broker lost!");
    }

    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        String message = new String(mqttMessage.getPayload());
        connectionBD.write(message);
        System.out.println("Message received:\n\t"+ message );
    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        // not used in this example
    }
}
