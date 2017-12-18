import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;

import java.nio.charset.StandardCharsets;

import java.util.concurrent.TimeUnit;


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
        String message = new String(mqttMessage.getPayload(), StandardCharsets.UTF_8);
        System.out.println("Message reçu : "+ message );

        Point.Builder builder = Point.measurement("mesures")
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        JsonObject jobject = new JsonParser().parse(message).getAsJsonObject();

        for (String key : jobject.keySet()){
            builder.addField(key, jobject.get(key).getAsString());
        }
        connectionBD.write("tacos", "autogen", builder.build());

    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        // not used in this example
    }
}
