import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;

import java.nio.charset.StandardCharsets;

import java.util.HashMap;
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
        System.out.println("Message received :\n\t"+ message );

        Point.Builder point = Point.measurement("mesures")
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS);

        HashMap<String,String> map = new Gson().fromJson(message, new TypeToken<HashMap<String, String>>(){}.getType());
        map.forEach(point::addField);

        Point p = point.build();
        connectionBD.write("tacos", "autogen", p);
    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        // not used in this example
    }
}
