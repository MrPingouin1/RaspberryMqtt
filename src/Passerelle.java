import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Passerelle {

    public static void main(String[] args) throws Exception {
        final String compteurURL = "192.168.20.164";
        final String serveurURL = "192.168.20.164";
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
            HttpURLConnection connOut = (HttpURLConnection) new URL(serveurURL).openConnection();
            connOut.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(connOut.getOutputStream());
            writer.write(data);
            writer.flush();
            writer.close();
            connOut.disconnect();
            // Tempo
            Thread.sleep(2000);
        }
    }
}