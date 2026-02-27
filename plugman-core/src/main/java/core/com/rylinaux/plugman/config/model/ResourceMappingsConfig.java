package core.com.rylinaux.plugman.config.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

/**
 * Jackson-based configuration model for resource mappings.
 * Represents the structure of resourcemaps.yml file.
 *
 * @author rylinaux
 */
@Data
public class ResourceMappingsConfig {

    /**
     * Map of resource names to their configuration
     */
    @JsonProperty("Resources")
    private Map<String, ResourceInfo> resources = Map.of();

    @Data
    public static class ResourceInfo {
        /**
         * The resource ID (SpigotMC or CurseForge)
         */
        @JsonProperty("ID")
        private Long id;

        /**
         * Whether this resource is on SpigotMC
         */
        @JsonProperty("spigotmc")
        private Boolean spigotmc;

        /**
         * Modrinth project slug or ID
         */
        @JsonProperty("modrinth")
        private String modrinth;

        /**
         * Hangar project as "Author/Slug"
         */
        @JsonProperty("hangar")
        private String hangar;

        /**
         * GitHub repository as "owner/repo"
         */
        @JsonProperty("github")
        private String github;
    }
}