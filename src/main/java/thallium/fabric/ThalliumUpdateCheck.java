package thallium.fabric;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import net.minecraft.client.MinecraftClient;

public class ThalliumUpdateCheck {

    public static boolean outdated;
    public static boolean checked;
    public static String current;
    public static String latest;
    public static String latestFull;

    public static boolean check() {
        if (checked) return outdated;
        current = "CURRENT_VERSION";

        HttpURLConnection httpurlconnection = null;

        try {
            ThalliumMod.LOGGER.info("Checking for new version");
            URL url = new URL("https://addons-ecs.forgesvc.net/api/v2/addon/search?gameId=432&sectionId=6&searchFilter=thallium"); // TODO Find better Curse API URL
            httpurlconnection = (HttpURLConnection)url.openConnection();

            httpurlconnection.setDoInput(true);
            httpurlconnection.setDoOutput(false);
            httpurlconnection.connect();

            try {
                InputStream inputstream = httpurlconnection.getInputStream();
                String s = toString(inputstream);
                inputstream.close();
                String str = (s = s.substring(s.indexOf("393938"))).substring(s.indexOf("latestFiles")+11, s.indexOf("gameName"));
                String displayName = (displayName = str.substring(str.indexOf("displayName\":"))).substring(14, displayName.indexOf("\","));
                System.out.println("THALLIUM DEBUG: " + displayName);

                String cur = toString(ThalliumUpdateCheck.class.getClassLoader().getResourceAsStream("thallium_version.txt"));
                current = cur;
                outdated = isOutdated(cur, displayName);
                checked = true;
            } finally {
                if (httpurlconnection != null) httpurlconnection.disconnect();
            }
        } catch (Exception exception) {
            ThalliumMod.LOGGER.info(exception.getClass().getName() + ": " + exception.getMessage());
        }
        return outdated;
    }

    private static boolean isOutdated(String current, String latestD) {
        String latestVer = latestD.replace("Thallium","").trim().split(" ")[0];
        String mcVersion = latestD.substring(latestD.indexOf(latestVer) + latestVer.length() + 6).replace(")", "");
        latestFull = latestD;
        latest = latestVer;

        return !(latestVer.equalsIgnoreCase(current) && mcVersion.equalsIgnoreCase(MinecraftClient.getInstance().getGame().getVersion().getName()));
    }

    private static String toString(InputStream inputStream) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))){
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();
            while ((inputLine = bufferedReader.readLine()) != null)
                stringBuilder.append(inputLine);
            return stringBuilder.toString();
        }
    }

}