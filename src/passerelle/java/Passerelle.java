import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Passerelle {

    public static void main(String[] args) throws Exception {
        final String compteurURL = "http://192.168.20.104/api/xdevices.json";
        final String serveurURL = "http://192.168.20.99";
        String data, temp;
        boolean boucle = true;
        while (boucle) {
            // Lecture des données sur l'API
            data = "";
            HttpURLConnection connIn = (HttpURLConnection) new URL(compteurURL).openConnection();
            connIn.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connIn.getInputStream()));
            while ((temp = reader.readLine()) != null) {
                data += temp;
            }
            reader.close();
            connIn.disconnect();

            // Envoi des données au serveur
            MqttClient client = new MqttClient(serveurURL, MqttClient.generateClientId());
            client.connect();
            MqttMessage message = new MqttMessage();
            message.setPayload(data.getBytes());
            client.publish("tacos_compteur", message);
            client.disconnect();
            client.close();

            // Tempo
            Thread.sleep(1000);
        }
    }
}