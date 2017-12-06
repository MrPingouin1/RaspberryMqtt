import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Passerelle {

    public static void main(String[] args) throws Exception {
        final String compteurURL = "http://192.168.20.104/api/xdevices.json";
        final String serveurURL = "tcp://192.168.20.99";
        int dureeTempoMs = 1000;
        String data, temp;
        boolean boucle = true;


        MqttClient client = new MqttClient(serveurURL, MqttClient.generateClientId());
        client.connect();
        MqttMessage message = new MqttMessage();

        while (boucle) {
            // Lecture des données sur l'API
            data = "";
            HttpURLConnection connIn = (HttpURLConnection) new URL(compteurURL).openConnection();
            connIn.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connIn.getInputStream()));

            while ((temp = reader.readLine()) != null) {
                data += temp;
            }
            System.out.println(data);
            reader.close();
            connIn.disconnect();

            // Envoi des données au serveur
            try {
                message.setPayload(data.getBytes());
                client.publish("tacos_compteur", message);
            } catch (Exception e) {
                // Connexion perdue, on essaye de se reconnecter
                System.out.println("Connexion MQTT perdue, reconnexion");
                client = new MqttClient(serveurURL, MqttClient.generateClientId());
                client.connect();
                message = new MqttMessage();
            }

            // Tempo
            Thread.sleep(dureeTempoMs);
        }

        client.disconnect();
        client.close();
    }
}