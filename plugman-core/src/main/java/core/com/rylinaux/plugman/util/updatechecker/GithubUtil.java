package core.com.rylinaux.plugman.util.updatechecker;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import core.com.rylinaux.plugman.plugins.PluginManager;
import core.com.rylinaux.plugman.pojo.UpdateResult;
import lombok.experimental.UtilityClass;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utilities for checking plugin updates via the GitHub Releases API.
 */
@UtilityClass
public class GithubUtil {

    private static final Logger LOGGER = Logger.getLogger(GithubUtil.class.getName());
    private static final String API_BASE_URL = "https://api.github.com/repos/";

    /**
     * Check if a plugin is up-to-date using the GitHub Releases API.
     *
     * @param pluginName the name of the plugin
     * @param repo       the GitHub repository in "owner/repo" format
     * @param pluginManager the plugin manager instance
     * @return the update result
     */
    public static UpdateResult checkUpToDate(String pluginName, String repo, PluginManager pluginManager) {
        var plugin = pluginManager.getPluginByName(pluginName);
        var latestVersion = getLatestVersion(repo);

        if (latestVersion == null) {
            if (plugin == null) return new UpdateResult(UpdateResult.ResultType.NOT_INSTALLED);
            return new UpdateResult(UpdateResult.ResultType.UP_TO_DATE, plugin.getVersion(), plugin.getVersion());
        }

        if (plugin == null) return new UpdateResult(UpdateResult.ResultType.NOT_INSTALLED);

        var currentVersion = plugin.getVersion();
        var isActual = UpdateUtil.isActualVersion(currentVersion, latestVersion);
        if (isActual != null && isActual)
            return new UpdateResult(UpdateResult.ResultType.UP_TO_DATE, currentVersion, latestVersion);
        return new UpdateResult(UpdateResult.ResultType.OUT_OF_DATE, currentVersion, latestVersion);
    }

    /**
     * Fetch the latest release tag from GitHub.
     *
     * @param repo the GitHub repository in "owner/repo" format
     * @return the latest version string (tag name, v-prefix stripped), or null on failure
     */
    public static String getLatestVersion(String repo) {
        try (var client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build()) {
            var request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE_URL + repo + "/releases/latest"))
                    .header("User-Agent", "PlugManX/1.0")
                    .header("Accept", "application/vnd.github+json")
                    .timeout(Duration.ofSeconds(30))
                    .GET()
                    .build();

            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) return null;

            var json = new Gson().fromJson(response.body(), JsonObject.class);
            if (json == null || !json.has("tag_name")) return null;

            var tag = json.get("tag_name").getAsString();
            if (tag.startsWith("v") || tag.startsWith("V")) tag = tag.substring(1);
            return tag;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to fetch latest GitHub release for " + repo, e);
            return null;
        }
    }
}
