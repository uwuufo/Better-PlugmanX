package core.com.rylinaux.plugman.util.updatechecker;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
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
 * Utilities for checking plugin updates via the Modrinth API.
 */
@UtilityClass
public class ModrinthUtil {

    private static final Logger LOGGER = Logger.getLogger(ModrinthUtil.class.getName());
    public static final String API_BASE_URL = "https://api.modrinth.com/v2/";

    /**
     * Check if a plugin is up-to-date using Modrinth.
     *
     * @param pluginName the name of the plugin
     * @param projectId  the Modrinth project ID/slug, or null to search by name
     * @param pluginManager the plugin manager instance
     * @return the update result
     */
    public static UpdateResult checkUpToDate(String pluginName, String projectId, PluginManager pluginManager) {
        if (projectId == null) projectId = getProjectId(pluginName);

        if (projectId == null) {
            var plugin = pluginManager.getPluginByName(pluginName);
            if (plugin == null) return new UpdateResult(UpdateResult.ResultType.NOT_INSTALLED);
            return new UpdateResult(UpdateResult.ResultType.INVALID_PLUGIN, plugin.getVersion());
        }

        var latestVersion = getLatestVersion(projectId);

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
     * Search Modrinth for a plugin by name and return its project ID.
     *
     * @param name the plugin name
     * @return the Modrinth project ID, or null if not found
     */
    public static String getProjectId(String name) {
        try (var client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build()) {
            var encoded = URLEncoder.encode(name, StandardCharsets.UTF_8);
            var request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE_URL + "search?query=" + encoded
                            + "&facets=[[%22project_type:plugin%22]]&limit=5"))
                    .header("User-Agent", "PlugManX/1.0")
                    .timeout(Duration.ofSeconds(30))
                    .GET()
                    .build();

            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            var root = new Gson().fromJson(response.body(), JsonElement.class);
            if (!root.isJsonObject()) return null;

            var hits = root.getAsJsonObject().getAsJsonArray("hits");
            if (hits == null) return null;

            for (var hit : hits) {
                var json = hit.getAsJsonObject();
                var title = json.get("title").getAsString();
                if (title.equalsIgnoreCase(name))
                    return json.get("project_id").getAsString();
            }
        } catch (IOException | InterruptedException e) {
            LOGGER.log(Level.WARNING, "Failed to search Modrinth for: " + name, e);
        }
        return null;
    }

    /**
     * Get the latest version string for a Modrinth project.
     *
     * @param projectId the Modrinth project ID or slug
     * @return the latest version string, or null on failure
     */
    public static String getLatestVersion(String projectId) {
        try (var client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build()) {
            var request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE_URL + "project/" + projectId + "/version?limit=1"))
                    .header("User-Agent", "PlugManX/1.0")
                    .timeout(Duration.ofSeconds(30))
                    .GET()
                    .build();

            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            var arr = new Gson().fromJson(response.body(), JsonArray.class);
            if (arr == null || arr.isEmpty()) return null;
            return arr.get(0).getAsJsonObject().get("version_number").getAsString();
        } catch (IOException | InterruptedException e) {
            LOGGER.log(Level.WARNING, "Failed to get Modrinth version for: " + projectId, e);
        }
        return null;
    }
}
