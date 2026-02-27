package core.com.rylinaux.plugman.util.updatechecker;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import core.com.rylinaux.plugman.plugins.PluginManager;
import core.com.rylinaux.plugman.pojo.UpdateResult;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utilities for checking plugin updates via the Hangar (PaperMC) API.
 */
@UtilityClass
public class HangarUtil {

    private static final Logger LOGGER = Logger.getLogger(HangarUtil.class.getName());
    public static final String API_BASE_URL = "https://hangar.papermc.io/api/v1/";

    /**
     * Check if a plugin is up-to-date using Hangar.
     *
     * @param pluginName  the name of the plugin
     * @param projectSlug "Author/Slug" for the Hangar project, or null to search by name
     * @param pluginManager the plugin manager instance
     * @return the update result
     */
    public static UpdateResult checkUpToDate(String pluginName, String projectSlug, PluginManager pluginManager) {
        if (projectSlug == null) projectSlug = getProjectSlug(pluginName);

        if (projectSlug == null) {
            var plugin = pluginManager.getPluginByName(pluginName);
            if (plugin == null) return new UpdateResult(UpdateResult.ResultType.NOT_INSTALLED);
            return new UpdateResult(UpdateResult.ResultType.INVALID_PLUGIN, plugin.getVersion());
        }

        var latestVersion = getLatestVersion(projectSlug);

        if (latestVersion == null) {
            var plugin = pluginManager.getPluginByName(pluginName);
            if (plugin == null) return new UpdateResult(UpdateResult.ResultType.NOT_INSTALLED);
            return new UpdateResult(UpdateResult.ResultType.INVALID_PLUGIN, plugin.getVersion());
        }

        var plugin = pluginManager.getPluginByName(pluginName);
        if (plugin == null) return new UpdateResult(UpdateResult.ResultType.NOT_INSTALLED);

        var currentVersion = plugin.getVersion();
        var isActual = UpdateUtil.isActualVersion(currentVersion, latestVersion);
        if (isActual != null && isActual)
            return new UpdateResult(UpdateResult.ResultType.UP_TO_DATE, currentVersion, latestVersion);
        return new UpdateResult(UpdateResult.ResultType.OUT_OF_DATE, currentVersion, latestVersion);
    }

    /**
     * Search Hangar for a plugin by name and return its "Author/Slug" string.
     *
     * @param name the plugin name
     * @return "Author/Slug" or null if not found
     */
    public static String getProjectSlug(String name) {
        try (var client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build()) {
            var encoded = URLEncoder.encode(name, StandardCharsets.UTF_8);
            var request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE_URL + "projects?query=" + encoded + "&limit=5"))
                    .header("User-Agent", "PlugManX/1.0")
                    .timeout(Duration.ofSeconds(30))
                    .GET()
                    .build();

            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            var root = new Gson().fromJson(response.body(), JsonElement.class);
            if (!root.isJsonObject()) return null;

            var results = root.getAsJsonObject().getAsJsonArray("result");
            if (results == null) return null;

            for (var result : results) {
                var json = result.getAsJsonObject();
                var projectName = json.get("name").getAsString();
                if (projectName.equalsIgnoreCase(name)) {
                    var ns = json.getAsJsonObject("namespace");
                    return ns.get("owner").getAsString() + "/" + ns.get("slug").getAsString();
                }
            }
        } catch (IOException | InterruptedException e) {
            LOGGER.log(Level.WARNING, "Failed to search Hangar for: " + name, e);
        }
        return null;
    }

    /**
     * Get the latest version string for a Hangar project.
     *
     * @param projectSlug "Author/Slug" of the project
     * @return the latest version name, or null on failure
     */
    public static String getLatestVersion(String projectSlug) {
        try (var client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build()) {
            var request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE_URL + "projects/" + projectSlug + "/versions?limit=1&offset=0"))
                    .header("User-Agent", "PlugManX/1.0")
                    .timeout(Duration.ofSeconds(30))
                    .GET()
                    .build();

            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            var root = new Gson().fromJson(response.body(), JsonElement.class);
            if (!root.isJsonObject()) return null;

            var results = root.getAsJsonObject().getAsJsonArray("result");
            if (results == null || results.isEmpty()) return null;
            return results.get(0).getAsJsonObject().get("name").getAsString();
        } catch (IOException | InterruptedException e) {
            LOGGER.log(Level.WARNING, "Failed to get Hangar version for: " + projectSlug, e);
        }
        return null;
    }
}
